package de.kifo.simpleNavigation.common.files.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NaviPoint {

    private final String NaviPointName;
    private final String worldName;
    private final int x;
    private final int y;
    private final int z;
    private final boolean publicNaviPoint;
    private final UUID player; // null if it's a public navi point

}
