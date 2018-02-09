package com.spacejunk.game.obstacles;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by vidxyz on 2/8/18.
 */

public abstract class Obstacle {

    int x;
    int y;


    public void setCoordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public abstract void moveLeft();

    public abstract Texture getTexture();


}
