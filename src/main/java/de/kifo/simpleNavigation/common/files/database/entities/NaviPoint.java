package de.kifo.simpleNavigation.common.files.database.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class NaviPoint {

    private String worldName;
    private int x;
    private int y;
    private int z;
    private boolean publicNaviPoint;
    private UUID player; // null if it's a public navi point

}
