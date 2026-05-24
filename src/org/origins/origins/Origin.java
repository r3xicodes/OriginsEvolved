package org.origins.origins;

import java.util.List;

public class Origin {
    private final String id;
    private final String displayName;
    private final String description;
    private final String primaryAbility;
    private final String secondaryAbility;
    private final String crouchAbility;
    private final List<String> weaknesses;
    private final String evolution;
    private final org.bukkit.Material icon; // material for GUI icon

    public Origin(String id, String displayName, String description, String primaryAbility, String secondaryAbility, String crouchAbility, List<String> weaknesses, String evolution, org.bukkit.Material icon) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.primaryAbility = primaryAbility;
        this.secondaryAbility = secondaryAbility;
        this.crouchAbility = crouchAbility;
        this.weaknesses = weaknesses;
        this.evolution = evolution;
        this.icon = icon;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public String getDescription() { return description; }
    public String getPrimaryAbility() { return primaryAbility; }
    public String getSecondaryAbility() { return secondaryAbility; }
    public String getCrouchAbility() { return crouchAbility; }
    public List<String> getWeaknesses() { return weaknesses; }
    public String getEvolution() { return evolution; }
    public org.bukkit.Material getIcon() { return icon; }
}
