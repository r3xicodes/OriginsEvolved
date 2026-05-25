import re
from collections import OrderedDict
from pathlib import Path

text = Path('originsinfo.md').read_text(encoding='utf-8')
ids = {}
for line in Path('origins.yml').read_text(encoding='utf-8').splitlines():
    m = re.match(r'^([a-z0-9_]+):$', line)
    if m:
        ids[m.group(1)] = m.group(1)

def norm(name):
    name = name.strip()
    name = re.sub(r'\s*\(.*\)$', '', name)
    name = re.sub(r'\s*\[.*\]$', '', name)
    name = re.sub(r'[^a-z0-9]', '', name.lower())
    return name

id_by_name = {norm(k): k for k in ids.keys()}
lines = text.splitlines()
sections = []
for i, line in enumerate(lines):
    if line.strip() == '':
        continue
    if i + 1 < len(lines) and lines[i+1].strip() == 'General Stats':
        sections.append({'name': line.strip(), 'start': i + 2})

for sec in sections:
    name = sec['name']
    simplified = norm(name)
    if simplified not in id_by_name:
        simp = re.sub(r'[^a-z0-9]', '', name.split()[0].lower())
        if simp in id_by_name:
            simplified = simp
        else:
            continue
    oid = id_by_name[simplified]
    start = sec['start']
    end = min(len(lines), start + 60)
    block = '\n'.join(lines[start:end])
    attrs = OrderedDict()
    def num(pattern):
        m = re.search(pattern, block)
        return float(m.group(1)) if m else None
    m = num(r'Speed:\s*([0-9.]+)%')
    if m: attrs['movement_speed_pct'] = m
    m = num(r'Size:\s*([0-9.]+)%')
    if m: attrs['size_pct'] = m
    m = num(r'Fall Damage Taken:\s*([0-9.]+)%')
    if m: attrs['fall_damage_pct'] = m
    mh = None
    for line2 in lines[start:end]:
        if 'Health:' in line2:
            h = re.search(r'Health:\s*([0-9.]+)', line2)
            if h:
                mh = float(h.group(1))
                break
            h2 = re.search(r'([0-9.]+)\s*[–-]\s*([0-9.]+)', line2)
            if h2:
                mh = (float(h2.group(1)) + float(h2.group(2))) / 2.0
                break
    if mh: attrs['max_health'] = mh
    m = num(r'Base:\s*([0-9.]+)')
    if m: attrs['attack_base'] = m
    m = num(r'Base Speed:\s*([0-9.]+)%')
    if m: attrs['mining_speed_pct'] = m
    if 'mining_speed_pct' not in attrs:
        m = re.search(r'Mining Capability[\s\S]{0,80}?Base Speed:\s*([0-9.]+)%', block)
        if m: attrs['mining_speed_pct'] = float(m.group(1))
    if attrs:
        print(oid + ':')
        for k, v in attrs.items():
            if float(v).is_integer():
                v = int(v)
            print(f'  {k}: {v}')
        print()
