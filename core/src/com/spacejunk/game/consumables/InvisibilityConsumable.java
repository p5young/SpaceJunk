package com.spacejunk.game.consumables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/10/18.
 */

public class InvisibilityConsumable extends Consumable {

    public InvisibilityConsumable(Level level) {
        this.consumableTexture = level.getCurrentGame().manager.get("invisibility.png");
        this.consumableTextureSmall = level.getCurrentGame().manager.get("invisibility_small.png");
        this.sound =  Gdx.audio.newSound(Gdx.files.internal("sounds/consumable_picked_up.mp3"));
        this.level = level;
        this.consumableType = CONSUMABLES.INVISIBILITY;
    }
}
