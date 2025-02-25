package de.kifo.simpleNavigation.common.files.database.entities;

import lombok.Data;

import java.util.UUID;

@Data
public class NaviPlayer {

    private final UUID uuid;
    private PlayerSettings playerSettings;

}

