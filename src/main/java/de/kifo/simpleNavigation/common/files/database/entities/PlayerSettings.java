package de.kifo.simpleNavigation.common.files.database.entities;

import de.kifo.simpleNavigation.common.enums.NavigationType;
import lombok.Data;

import static de.kifo.simpleNavigation.common.enums.NavigationType.PARTICLES;

@Data
public class PlayerSettings {

    private NavigationType navigationType = PARTICLES;

}
