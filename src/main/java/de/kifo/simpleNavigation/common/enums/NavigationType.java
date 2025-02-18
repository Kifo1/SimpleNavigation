package de.kifo.simpleNavigation.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum NavigationType {

    COMPASS(false),
    BOSSBAR(true),
    PARTICLES(true);

    private final boolean isSchedulesNavigation;
}
