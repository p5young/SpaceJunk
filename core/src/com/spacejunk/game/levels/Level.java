package com.spacejunk.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.consumables.FireSuitConsumable;
import com.spacejunk.game.consumables.GasMaskConsumable;
import com.spacejunk.game.consumables.InvisibilityConsumable;
import com.spacejunk.game.consumables.LifeConsumable;
import com.spacejunk.game.consumables.SpaceHammerConsumable;
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

    public static final int TOTAL_NUMBER_OF_OBSTACLE_TYPES = 4;

    public static final int MAX_NUMBER_OF_OBSTACLES = 5;
    public static final int MAX_PLATFORMS = 3;
    public static final int VELOCITY = 10;
    public static final double SCORING_RATE = 0.25;
    public static final int MAX_LIVES = 3;

    private ArrayList<Obstacle> obstaclesList;
    private ArrayList<Consumable> consumablesList;

    private ArrayList<Consumable> inventoryList;
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

    private int furthestObstacleIndex;
    private int randomRegion;

    private double scoringRate;

    private SpaceJunk currentGame;

    public Level(SpaceJunk currentGame) {

        Gdx.app.log("applog", "Level constructor called here");

        this.currentGame = currentGame;
        this.obstaclesList = new ArrayList<Obstacle>();
        this.randomGenerator = new Random();
        this.velocity = VELOCITY;
        this.scoringRate = SCORING_RATE;
        this.maxLives = MAX_LIVES;
        this.furthestObstacleIndex = MAX_NUMBER_OF_OBSTACLES - 1;

        this.inventoryList = new ArrayList<Consumable>();
        // Temporary
        inventoryList.add(new InvisibilityConsumable(this, 0));
        inventoryList.add(new SpaceHammerConsumable(this, 1));
        inventoryList.add(new GasMaskConsumable(this, 1));
        inventoryList.add(new FireSuitConsumable(this, 2));


        this.currentGame = currentGame;
        this.minimumDistanceBetweenObstacles = currentGame.getxMax() / 6;
        Gdx.app.log("applog", "minimum distance between obstacles is " + minimumDistanceBetweenObstacles);
    }

    /**
    This function generates obstacles for the level specified
    It generates it and random and makes sure it is not unbeatable
    **/
    public void generateInitialObstacles() {

        // Initializing what the types of the obstacles are going to be
        for(int i = 0; i < MAX_NUMBER_OF_OBSTACLES; i++) {
            obstaclesList.add(this.getNextRandomObstacle(i));
        }

        this.generateInitialCoordinatesForObstacles();

    }

    /**
    Returns a random obstacle from our list of obstacles
    **/
    private Obstacle getNextRandomObstacle(int obstacleNumber) {

        int randomInt = randomGenerator.nextInt(TOTAL_NUMBER_OF_OBSTACLE_TYPES);

        switch (randomInt) {
            case 0:
                return new AsteroidObstacle(this, obstacleNumber);
            case 1:
                return new FireObstacle(this, obstacleNumber);
            case 2:
                return new ToxicGasObstacle(this, obstacleNumber);
            case 3:
                return new AlienObstacle(this, obstacleNumber);
            default:
                Gdx.app.log("applog", "Error: This should'nt happen");
                return null;
        }
    }

    /**
     * Generates initial co-ordinates while making sure that they are not impossible
     **/
    public void generateInitialCoordinatesForObstacles() {


        for (int i = 0; i < obstaclesList.size(); i++) {
            int[] coordinates;
            if(i == 0) {
                // This is done so that initially, at the start, the obstacles are off screen
                // Giving the user some time to get accustomed to the in-game physics
                coordinates = this.getCoordinatesForFirstObstacle();
                coordinates[0] += xMax;
            }
            else {
                coordinates = this.getCoordinatesForObstacle(i - 1);
            }

            obstaclesList.get(i).setCoordinates(coordinates[0], coordinates[1]);
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


    private int[] getCoordinatesForFirstObstacle() {

        int[] coordinates = new int[2];
        int y = generateRandomYCoordinate();
        int x = 0;
        coordinates[0] = x;
        coordinates[1] = y;
        return coordinates;
    }


    public int[] getCoordinatesForObstacle(int previousObstacleIndex) {

        int[] coordinates = new int[2];

        int y = generateRandomYCoordinate();

        int previousX;

        previousX = obstaclesList.get(previousObstacleIndex).getX();

        int from = previousX + this.minimumDistanceBetweenObstacles;
        int to = from + this.randomRegion;

        int x = randomGenerator.nextInt(Math.abs(to - from)) + from;

        coordinates[0] = x;
        coordinates[1] = y;

        return coordinates;
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
        // Update this to change where points are being generated
        this.randomRegion = this.xMax / 3;
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

    public int getFurthestObstacleIndex() {
        return furthestObstacleIndex;
    }

    public void setFurthestObstacleIndex(int furthestObstacleIndex) {
        this.furthestObstacleIndex = furthestObstacleIndex;
    }

    public ArrayList<Consumable> getInventoryList() {
        return this.inventoryList;
    }
}
