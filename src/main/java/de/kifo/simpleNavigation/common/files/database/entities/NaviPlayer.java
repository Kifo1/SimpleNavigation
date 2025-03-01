package de.kifo.simpleNavigation.common.files.database.entities;

import de.kifo.simpleNavigation.common.enums.NavigationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

import static de.kifo.simpleNavigation.common.enums.NavigationType.PARTICLES;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class NaviPlayer {

    private final UUID uuid;
    private NavigationType preferredNavigationType = PARTICLES;

}
