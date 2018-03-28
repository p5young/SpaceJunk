package com.spacejunk.game.obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/8/18.
 */

public class AsteroidObstacle extends Obstacle {

    public AsteroidObstacle(Level level) {
        this.obstacleTexture = level.getCurrentGame().manager.get("asteroid.png");
        this.brokenTexture = level.getCurrentGame().manager.get("asteroid_broken.png");

        this.pixmap = level.getCurrentGame().manager.getPixmap("asteroid.png");

        this.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/asteroid_sound.mp3"));

        this.level = level;
        this.obstacleType = OBSTACLES.ASTEROID;
        this.breaksOnConsumable = Consumable.CONSUMABLES.SPACE_HAMMER;
    }

}

