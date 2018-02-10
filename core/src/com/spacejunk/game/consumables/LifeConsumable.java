package com.spacejunk.game.consumables;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/10/18.
 */

public class LifeConsumable extends Consumable {

    public LifeConsumable(Level level, int consumableNumber) {
        this.consumableTexture = new Texture("toxic_gas_green.png");
        this.level = level;
        this.consumableNumber = consumableNumber;
        this.consumableType = CONSUMABLES.LIFE;
    }
}
