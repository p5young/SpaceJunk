package com.spacejunk.game.consumables;

import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/10/18.
 */

public class SpaceHammerConsumable extends Consumable {

    public SpaceHammerConsumable(Level level) {
        SpaceJunk.MyAssetManager manager = level.getCurrentGame().getManager();

        this.consumableTexture = manager.get("space_hammer.png", Texture.class);
        this.consumableTextureSmall = manager.get("space_hammer_small.png");
        this.sound = manager.get("sounds/consumable_picked_up.mp3");

        this.level = level;
        this.consumableType = CONSUMABLES.SPACE_HAMMER;
    }
}
