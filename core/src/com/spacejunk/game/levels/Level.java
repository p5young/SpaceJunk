package com.spacejunk.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.obstacles.FireObstacle;
import com.spacejunk.game.obstacles.Obstacle;
import com.spacejunk.game.obstacles.WallObstacle;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by vidxyz on 2/8/18.
 */

public class Level {

    public static final int MAX_NUMBER_OF_OBSTACLES = 5;
    public static final int MAX_PLATFORMS = 3;
    public static final int VELOCITY = 8;
    public static final double SCORING_RATE = 0.25;

    private ArrayList<Obstacle> obstaclesList;
    private Random randomGenerator;


    // Co-ordinates for platform boundaries
    int topPlatformY;
    int middlePlatformY;
    int bottomPlatformY;

    // Co-ordinates for boundaries
    int xMax;
    int yMax;

    // Velocity
    int velocity;
    int minimumDistanceBetweenObstacles;

    private double scoringRate;

    private SpaceJunk currentGame;

    public Level(SpaceJunk currentGame) {
        this.obstaclesList = new ArrayList<Obstacle>();
        this.randomGenerator = new Random();
        this.velocity = VELOCITY;
        this.scoringRate = SCORING_RATE;

        this.currentGame = currentGame;
        this.minimumDistanceBetweenObstacles = currentGame.getxMax() / 4;
        Gdx.app.log("applog", "minimum distance between obstacles is " + minimumDistanceBetweenObstacles);
    }

    /**
    This function generates obstacles for the level specified
    It generates it and random and makes sure it is not unbeatable
    **/
    public void generateInitialObstacles() {

        // Initializing what the types of the obstacles are going to be
        for(int i = 0; i < MAX_NUMBER_OF_OBSTACLES; i++) {
            obstaclesList.add(this.getNextRandomObstacle());
        }

        this.generateInitialCoordinatesForObstacles();

    }

    /**
    Returns a random obstacle from our list of obstacles
    **/
    private Obstacle getNextRandomObstacle() {

        int randomInt = randomGenerator.nextInt(2);

        switch (randomInt) {
            case 0:
                return new WallObstacle(this);
            case 1:
                return new FireObstacle(this);
            default:
                Gdx.app.log("applog", "Error: This should'nt happen");
                return null;
        }
    }

    /**
     * Generates initial co-ordinates while making sure that they are not impossible
     **/
    private void generateInitialCoordinatesForObstacles() {

        for (Obstacle o : obstaclesList) {
            int[] coordinates = this.getNextCoordinates();
            coordinates[0] += xMax;
            o.setCoordinates(coordinates[0], coordinates[1]);
        }

    }

    public boolean failsInitialOverlappingCheck(int x, Obstacle o) {
        return (x >= o.getX() && x <= (o.getX() + o.getTexture().getWidth()));
    }

    /**
     * Returns TRUE if x is too close to an obstacle's x co-ordinate
     * TOO CLOSE is defined by level difficulty
     * */
    private boolean failsGapCheck(int x, Obstacle o) {
        return(x >= o.getX() + o.getTexture().getWidth() &&
                x <= o.getX() + o.getTexture().getWidth() + this.minimumDistanceBetweenObstacles);
    }


    /**
     * Returns 'True' if @param:x is not present as x-coordinate for any other obstacle
     * */
    private boolean isXCoordinateAcceptable(int x) {

        for (Obstacle o : obstaclesList) {
            if(this.failsInitialOverlappingCheck(x, o) || this.failsGapCheck(x, o)) {
                return false;
            }
        }
        return true;
    }

    public int[] getNextCoordinates() {

        int[] coordinates = new int[2];

        int x = randomGenerator.nextInt(Math.abs(this.xMax));

        // Generate new x coordinate until all x coordinates are different
        while(!this.isXCoordinateAcceptable(x)) {
            x = randomGenerator.nextInt(Math.abs(this.xMax));
        }


        int y = 0;
        int temp = randomGenerator.nextInt(MAX_PLATFORMS);

        switch (temp) {
            case 0:
                y = this.topPlatformY;
                break;
            case 1:
                y = this.middlePlatformY;
                break;
            case 2:
                y = this.bottomPlatformY;
                break;
            default:
                break;
        }

        // This is done so that the obstacles are initially off the screen totally
        coordinates[0] = x;
        coordinates[1] = y;

        return coordinates;
    }


    /**
     * Renders the obstacles on screen, while constantly updating positions throughout
     **/
    public void renderObstacles(SpriteBatch canvas) {

        for (Obstacle o : obstaclesList) {
            o.moveLeft();
            canvas.draw(o.getTexture(), o.getX(), o.getY());
        }
    }


    public void setPlatformCoordinates(int topPlatformY, int middlePlatformY, int bottomPlatformY) {
        this.topPlatformY = topPlatformY;
        this.middlePlatformY = middlePlatformY;
        this.bottomPlatformY = bottomPlatformY;
    }

    public void setMaxCoordinates(int xMax, int yMax) {
        this.xMax = xMax;
        this.yMax = yMax;
    }

    public int getVelocity() {
        return this.velocity;
    }

    public int getXMax() {
        return this.xMax;
    }

    public int getYMax() {
        return this.yMax;
    }

    public double getScoringRate() {
        return this.scoringRate;
    }
}
