package de.kifo.simpleNavigation.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NavigationType {

    COMPASS("Compass"),
    BOSSBAR("Bossbar"),
    PARTICLES("Particles");

    private final String displayName;
}
