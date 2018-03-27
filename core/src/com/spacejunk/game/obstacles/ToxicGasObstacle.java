package com.spacejunk.game.obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/9/18.
 */

public class ToxicGasObstacle extends Obstacle {

    private static final Pixmap myPixmap = new Pixmap(Gdx.files.internal("toxic_gas_green.png"));
    private static final Texture myTexture = new Texture("toxic_gas_green.png");
    private static final Texture myBrokenTexture = new Texture("toxic_gas_green.png");
    private static final Sound mySound = Gdx.audio.newSound(Gdx.files.internal("sounds/gas_sound.mp3"));

    public ToxicGasObstacle(Level level) {
        this.obstacleTexture = myTexture;
        this.brokenTexture = myBrokenTexture;

        this.pixmap = myPixmap;

        this.level = level;
        this.obstacleType = OBSTACLES.TOXIC_GAS;

        this.sound = mySound;

        this.breaksOnConsumable = Consumable.CONSUMABLES.GAS_MASK;
    }

}
