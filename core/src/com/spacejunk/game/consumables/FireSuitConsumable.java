package com.spacejunk.game.consumables;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/10/18.
 */

public class FireSuitConsumable extends Consumable {

    public FireSuitConsumable(Level level) {
        this.consumableTexture = new Texture("firesuit.png");
        this.consumableTextureSmall = new Texture("firesuit_small.png");
        this.level = level;
        this.consumableType = CONSUMABLES.FIRESUIT;
    }
}
