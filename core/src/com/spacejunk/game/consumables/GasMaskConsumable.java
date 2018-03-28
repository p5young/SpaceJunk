package com.spacejunk.game.consumables;

import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/10/18.
 */

public class GasMaskConsumable extends Consumable {

    public GasMaskConsumable(Level level) {
        SpaceJunk.MyAssetManager manager = level.getCurrentGame().getManager();

        this.consumableTexture = manager.get("gas_mask.png");
        this.consumableTextureSmall = manager.get("gas_mask_small.png");
        this.sound = manager.get("sounds/consumable_picked_up.mp3");

        this.level = level;
        this.consumableType = CONSUMABLES.GAS_MASK;
    }
}
