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

    public ToxicGasObstacle(Level level) {
        this.obstacleTexture = level.getCurrentGame().manager.get("toxic_gas_green.png");
        this.brokenTexture = level.getCurrentGame().manager.get("toxic_gas_green.png");

        this.pixmap = level.getCurrentGame().manager.getPixmap("toxic_gas_green.png");

        this.level = level;
        this.obstacleType = OBSTACLES.TOXIC_GAS;

        this.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/gas_sound.mp3"));

        this.breaksOnConsumable = Consumable.CONSUMABLES.GAS_MASK;
    }

}
