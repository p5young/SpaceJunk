package com.spacejunk.game.consumables;

import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/10/18.
 */

public class FireSuitConsumable extends Consumable {

    public FireSuitConsumable(Level level) {
        SpaceJunk.MyAssetManager manager = level.getCurrentGame().getManager();

        this.consumableTexture = manager.get("firesuit.png");
        this.consumableTextureSmall = manager.get("firesuit_small.png");
        this.sound = manager.get("sounds/consumable_picked_up.mp3");

        this.level = level;
        this.consumableType = CONSUMABLES.FIRESUIT;
    }
}
