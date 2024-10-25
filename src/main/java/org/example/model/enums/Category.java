package org.example.model.enums;

public enum Category {
    T_SHIRTS("T-Shirts"),
    HOODIES("Hoodies");

    private final String displayName;

    Category(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
