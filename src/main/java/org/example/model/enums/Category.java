package org.example.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Category {
    T_SHIRTS("T-Shirts"),
    HOODIES("Hoodies");

    private final String displayName;
}
