package com.spacejunk.game.consumables;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/10/18.
 */

public class SpaceHammerConsumable extends Consumable {

    public SpaceHammerConsumable(Level level, int consumableNumber) {
        this.consumableTexture = new Texture("heart.png");
        this.level = level;
        this.consumableNumber = consumableNumber;
        this.consumableType = CONSUMABLES.SPACE_HAMMER;
    }
}
