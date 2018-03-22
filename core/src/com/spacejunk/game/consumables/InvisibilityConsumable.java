package com.spacejunk.game.consumables;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/10/18.
 */

public class InvisibilityConsumable extends Consumable {

    public InvisibilityConsumable(Level level) {
        this.consumableTexture = new Texture("invisibility.png");
        this.consumableTextureSmall = new Texture("invisibility_small.png");
        this.level = level;
        this.consumableType = CONSUMABLES.INVISIBILITY;
    }
}
