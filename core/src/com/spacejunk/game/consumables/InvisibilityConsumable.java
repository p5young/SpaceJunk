package com.spacejunk.game.consumables;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/10/18.
 */

public class InvisibilityConsumable extends Consumable {


    private static final Texture myTexture = new Texture("invisibility.png");
    private static final Texture mySmallTexture = new Texture("invisibility_small.png");
    private static final Sound mySound = Gdx.audio.newSound(Gdx.files.internal("sounds/consumable_picked_up.mp3"));

    public InvisibilityConsumable(Level level) {
        this.consumableTexture = myTexture;
        this.consumableTextureSmall = mySmallTexture;
        this.sound = mySound;
        this.level = level;
        this.consumableType = CONSUMABLES.INVISIBILITY;
    }
}
