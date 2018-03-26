package com.spacejunk.game.obstacles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.levels.Level;

/**
 * Created by vidxyz on 2/9/18.
 */

public class AlienObstacle extends Obstacle {

    private static final Pixmap myPixmap = new Pixmap(Gdx.files.internal("alien.png"));

    public AlienObstacle(Level level) {
        super();
        this.obstacleTexture = new Texture("alien.png");
        this.brokenTexture = new Texture("alien.png");

        this.pixmap = myPixmap;

        this.sound = Gdx.audio.newSound(Gdx.files.internal("sounds/alien_sound.wav"));

        this.level = level;
        this.obstacleType = OBSTACLES.ALIEN;
        this.breaksOnConsumable = Consumable.CONSUMABLES.INVISIBILITY;
    }

}
