package com.spacejunk.game.levels;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.spacejunk.game.GameScreen;
import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.constants.GameConstants;
import com.spacejunk.game.consumables.Consumable;
import com.spacejunk.game.consumables.FireSuitConsumable;
import com.spacejunk.game.consumables.GasMaskConsumable;
import com.spacejunk.game.consumables.InvisibilityConsumable;
import com.spacejunk.game.consumables.SpaceHammerConsumable;
import com.spacejunk.game.obstacles.Obstacle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * Created by vidxyz on 2/8/18.
 */

public class Level {

    private ArrayList<Obstacle> obstaclesList;
    private ArrayList<Consumable> consumablesList;

    // Following two fields are for inventory management
    private ArrayList<Consumable> inventoryObjects;
    private Set<Consumable.CONSUMABLES> inventory;
    private Consumable.CONSUMABLES equippedConsumable;


    // Co-ordinates for platform boundaries
    int topPlatformY;
    int middlePlatformY;
    int bottomPlatformY;

    // Co-ordinates for boundaries
    int xMax;
    int yMax;

    // Velocity
    int velocity = GameConstants.VELOCITY;
    int minimumDistanceBetweenObstacles;

    int maxLives = GameConstants.MAX_LIVES;

    private double scoringRate = GameConstants.SCORING_RATE;

    private SpaceJunk currentGame;
    private LevelGenerator levelGenerator;

    // used in renderObstacles() to delay next group's generation
    private int chunkWidth;

    public Level(SpaceJunk currentGame) {

        Gdx.app.log("applog", "Level constructor called here");

        this.currentGame = currentGame;
        this.obstaclesList = new ArrayList<Obstacle>();
        this.consumablesList = new ArrayList<Consumable>();

        this.levelGenerator = new LevelGenerator(this);

        /* initialize the map with the obstacles
        *  we are kind of hard coding what the consumables are at first
        */
        this.inventory = new HashSet<Consumable.CONSUMABLES>();

        this.equippedConsumable = Consumable.CONSUMABLES.UNEQUIPPED;

        this.inventoryObjects = new ArrayList<Consumable>();
        this.inventoryObjects.add(new SpaceHammerConsumable(this));
        this.inventoryObjects.add(new FireSuitConsumable(this));
        this.inventoryObjects.add(new GasMaskConsumable(this));
        this.inventoryObjects.add(new InvisibilityConsumable(this));

        this.currentGame = currentGame;
        this.minimumDistanceBetweenObstacles = (int)currentGame.getCharacter().getCharacterShape().getWidth() + 100;
        Gdx.app.log("applog", "minimum distance between obstacles is " + minimumDistanceBetweenObstacles);
    }

    /**
    This function generates a group of obstacles
     Returns: an integer to be used as a delay counter before next group is generated
                a wide group will return a large number
    **/
    public void generateObstacles() {
        chunkWidth = levelGenerator.generateObstacles();
        Gdx.app.log("applog", "SETTING CHUNKWIDTH: " + chunkWidth);
    }


    /**
     * Renders the obstacles on screen, while constantly updating positions throughout
     **/
    public void renderObstacles(SpriteBatch canvas, ShapeRenderer shapeRenderer, boolean toMove) {

        // make new chunk of obstacles
        if(toMove && (chunkWidth -= GameConstants.VELOCITY) <= 0) {
            generateObstacles();
        }

        // move everything left
        for (Obstacle o : obstaclesList) {
            if(toMove) {
                o.moveLeft();
            }
            canvas.draw(o.getTexture(), o.getX(), o.getY());

            if(GameScreen.DEBUG) {
                shapeRenderer.rect(o.getObstacleShape().getX(), o.getObstacleShape().getY(),
                        o.getObstacleShape().getWidth(), o.getObstacleShape().getHeight());
            }
        }
        for (Consumable c : consumablesList) {
            if(toMove) {
                c.moveLeft();
            }
            canvas.draw(c.getTexture(), c.getX(), c.getY());

            if(GameScreen.DEBUG) {
                Color col = shapeRenderer.getColor(); // store color
                shapeRenderer.setColor(Color.YELLOW); // make color blue
                shapeRenderer.rect(c.getConsumableShape().getX(), c.getConsumableShape().getY(),
                        c.getConsumableShape().getWidth(), c.getConsumableShape().getHeight());
                shapeRenderer.setColor(col);          // restore color
            }
        }

        // draw platform lines if DEBUG is on
        if (GameScreen.DEBUG) {
            Color c = shapeRenderer.getColor(); // store color
            shapeRenderer.setColor(Color.BLUE); // make color blue
            shapeRenderer.rect(0f, (float)this.topPlatformY, (float)xMax, 5f);
            shapeRenderer.rect(0f, (float)this.middlePlatformY, (float)xMax, 5f);
            shapeRenderer.rect(0f, (float)this.bottomPlatformY, (float)xMax, 5f);
            shapeRenderer.setColor(c);          // restore color
        }

        // delete anything that's moved off the left side of the screen
        for (int i = obstaclesList.size() - 1 ; i >= 0 ; --i) {
            Obstacle o = obstaclesList.get(i);
            if(o.getX() < -o.getTexture().getWidth()) {
                obstaclesList.remove(i);
                //Gdx.app.log("applog", "removed obstacle " + i);
            }
        }
        for (int i = consumablesList.size() - 1 ; i >= 0 ; --i) {
            Consumable c = consumablesList.get(i);
            if(c.getX() < -c.getTexture().getWidth()) {
                consumablesList.remove(i);
                //Gdx.app.log("applog", "removed consumable " + i);
            }
        }
    }

    // I made the obstacle hitboxes 75% of normal size because
    // corners collide while textures weren't touching
    public void updateObstacleShapeCoordinates() {
        for (Obstacle o : obstaclesList) {
            o.getObstacleShape().set(o.getX(), o.getY(),
                    o.getTexture().getWidth(), o.getTexture().getHeight());
        }
        for (Consumable c : consumablesList) {
            c.getConsumableShape().set(c.getX(), c.getY(),
                    c.getTexture().getWidth(), c.getTexture().getHeight());
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

    public Set<Consumable.CONSUMABLES> getInventory() {
        return inventory;
    }

    public Consumable.CONSUMABLES getEquippedConsumable() {
        return equippedConsumable;
    }

    public void setEquippedConsumable(Consumable.CONSUMABLES consumable) {
        this.equippedConsumable = consumable;
    }

    public ArrayList<Consumable> getInventoryObjects() {
        return inventoryObjects;
    }

    public ArrayList<Obstacle> getObstaclesList() {
        return obstaclesList;
    }

    public ArrayList<Consumable> getConsumablesList() {
        return consumablesList;
    }

    public int getMinimumDistanceBetweenObstacles() {
        return minimumDistanceBetweenObstacles;
    }

    public int getTopPlatformY() {
        return topPlatformY;
    }

    public int getMiddlePlatformY() {
        return middlePlatformY;
    }

    public int getBottomPlatformY() {
        return bottomPlatformY;
    }

    public LevelGenerator getLevelGenerator() {
        return levelGenerator;
    }

    public SpaceJunk getCurrentGame() { return currentGame; }

}
