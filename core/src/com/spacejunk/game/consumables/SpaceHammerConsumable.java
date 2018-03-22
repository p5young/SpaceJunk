package com.spacejunk.game.consumables;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/10/18.
 */

public class SpaceHammerConsumable extends Consumable {

    public SpaceHammerConsumable(Level level) {
        this.consumableTexture = new Texture("space_hammer.png");
        this.consumableTextureSmall = new Texture("space_hammer_small.png");
        this.level = level;
        this.consumableType = CONSUMABLES.SPACE_HAMMER;
    }
}
