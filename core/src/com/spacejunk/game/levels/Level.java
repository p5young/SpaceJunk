package com.spacejunk.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.obstacles.AlienObstacle;
import com.spacejunk.game.obstacles.FireObstacle;
import com.spacejunk.game.obstacles.Obstacle;
import com.spacejunk.game.obstacles.AsteroidObstacle;
import com.spacejunk.game.obstacles.ToxicGasObstacle;

import java.util.ArrayList;
import java.util.Random;


/**
 * Created by vidxyz on 2/8/18.
 */

public class Level {

    public static final int TOTAL_NUMBER_OF_OBSTACLES = 4;
    public static final int MAX_NUMBER_OF_OBSTACLES = 5;
    public static final int MAX_PLATFORMS = 3;
    public static final int VELOCITY = 8;
    public static final double SCORING_RATE = 0.25;
    public static final int MAX_LIVES = 3;

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

    int maxLives;

    private double scoringRate;

    private SpaceJunk currentGame;

    public Level(SpaceJunk currentGame) {
        this.currentGame = currentGame;
        this.obstaclesList = new ArrayList<Obstacle>();
        this.randomGenerator = new Random();
        this.velocity = VELOCITY;
        this.scoringRate = SCORING_RATE;
        this.maxLives = MAX_LIVES;

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

        int randomInt = randomGenerator.nextInt(TOTAL_NUMBER_OF_OBSTACLES);

        switch (randomInt) {
            case 0:
                return new AsteroidObstacle(this);
            case 1:
                return new FireObstacle(this);
            case 2:
                return new ToxicGasObstacle(this);
            case 3:
                return new AlienObstacle(this);
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
            int[] coordinates = this.getNextInitialCoordinatesForObstacle();
            // This is done so that initially, at the start, the obstacles are off screen
            // Giving the user some time to get accustomed to the in-game physics
            coordinates[0] += xMax;
            o.setCoordinates(coordinates[0], coordinates[1]);
        }

        printObstacleCoordinates();

    }

    private void printObstacleCoordinates() {

        Gdx.app.log("applog", "Printing out newly generated coorindates");
        int i = 0;
        for (Obstacle o : obstaclesList) {
            i++;
            Gdx.app.log("applog",
                    String.format("Obstacle %d is a %s has coordinates (%d, %d)", i, o.getType(),
                            o.getX(), o.getY()));
        }
    }


    /**
     * Returns TRUE if (x,y) overlaps any other obstacle's coordinate region (including textures)
     * */
    public boolean failsInitialOverlappingCheck(int x, Obstacle o) {
        return (x >= o.getX() &&
                x <= (o.getX() + o.getTexture().getWidth()));
    }

    /**
     * Returns TRUE if x is 'too close' to an obstacle's x co-ordinate
     * TOO CLOSE is defined by level difficulty
     * */
    private boolean failsGapCheck(int x, int y, Obstacle o) {
        return (y == o.getY() && // Platforms match up
                ((x >= o.getX() &&
                x <= o.getX() + o.getTexture().getWidth() + this.minimumDistanceBetweenObstacles) ||
                        (x <= o.getX() && x >= o.getX() - this.minimumDistanceBetweenObstacles)));
    }

    /**
     * Return 'TRUE' if there (x,y) causes any 3 obstacles in this.obstaclesList to have the
     * x-coordinates such that it forms a blockade completely on all three platforms
     * ASSUMPTION :- (x,y) has already been checked to ensure that it doesn't overlap any existing coordinates
     *               as well as (x,y) adheres to the minimum distance between obstacles
     * */
    private boolean fails3obstaclesInSingleColumnCheck(int x, int y) {
        int count = 0;

        for(Obstacle o : obstaclesList) {
            if(x >= o.getX() - (2 * o.getTexture().getWidth()) &&
                    x <= o.getX() + (2 * o.getTexture().getWidth())
                    && o.getY() != y) {
                count++;
            }
        }

        return count > 2;
    }


    /**
     * Returns 'True' if (x,y) does'nt violate any constraints
     * */
    private boolean areCoordinatesAcceptable(int x, int y) {

        for (Obstacle o : obstaclesList) {
            if(this.failsInitialOverlappingCheck(x, o) || this.failsGapCheck(x, y, o)) {
                return false;
            }
        }

        if (this.fails3obstaclesInSingleColumnCheck(x, y)) {
            Gdx.app.log("applog", "Failing 3 obs column check!");
            return false;
        }

        return true;
    }


    /**
     * This exists because while generating initial coordinates, the first few obstacles are off the screen
     * */
    private int[] getNextInitialCoordinatesForObstacle() {

        int[] coordinates = new int[2];

        int y = generateRandomYCoordinate();

        int x = randomGenerator.nextInt(Math.abs(this.xMax));

        // Generate new (x,y) coordinates until it is acceptable and system is in equilibrium
        //  x = x + xMax is done because initially, we spawn obstacles off the screen
        // to give the user some time to get accustomed to in-game physics
        while(!this.areCoordinatesAcceptable(x + xMax, y)) {
            x = randomGenerator.nextInt(Math.abs(this.xMax));
            y = generateRandomYCoordinate();
        }

        coordinates[0] = x;
        coordinates[1] = y;

        return coordinates;
    }


    /**
     * Generates a pair of (x,y) coordinates for the next obstacle
     * */
    public int[] getNextCoordinatesForObstacle() {

        int[] coordinates = new int[2];

        int y = generateRandomYCoordinate();
        // This is done so that new obstacles are ALWAYS generated AFTER our character's current position
        int x = generateRandomXCoorindateInFrontOfCharacter();


        // Generate new (x,y) coordinates until it is acceptable and system is in equilibrium
        while(!this.areCoordinatesAcceptable(x, y)) {
            x = generateRandomXCoorindateInFrontOfCharacter();
            y = generateRandomYCoordinate();
        }

        coordinates[0] = x;
        coordinates[1] = y;

        return coordinates;
    }


    private int generateRandomXCoorindateInFrontOfCharacter() {
        return  randomGenerator.nextInt(
                Math.abs(currentGame.getCharacter().getCurrentX() +
                        currentGame.getCharacter().getCharacterTextures()[0].getWidth() / 2
                        - this.xMax));
    }


    private int generateRandomYCoordinate() {
        int temp = randomGenerator.nextInt(MAX_PLATFORMS);

        switch (temp) {
            case 0:
                return this.topPlatformY;
            case 1:
                return this.middlePlatformY;
            case 2:
                 return this.bottomPlatformY;
            default:
                Gdx.app.log("applog", "Error: This shouldn't be happening");
                return this.middlePlatformY;
        }
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

    public int getMaxLives() {
        return this.maxLives;
    }
}
