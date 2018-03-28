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

public class FireObstacle extends Obstacle {

    public FireObstacle(Level level) {
        this.obstacleTexture = level.getCurrentGame().manager.get("fire.png");
        this.brokenTexture = level.getCurrentGame().manager.get("fire.png");

        this.pixmap = level.getCurrentGame().manager.getPixmap("fire.png");

        this.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/fire_sound.mp3"));

        this.level = level;
        this.obstacleType = OBSTACLES.FIRE;
        this.breaksOnConsumable = Consumable.CONSUMABLES.FIRESUIT;
    }

}
