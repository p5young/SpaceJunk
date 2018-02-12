package com.spacejunk.game.menus;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.spacejunk.game.SpaceJunk;
import com.spacejunk.game.consumables.Consumable;

import java.util.ArrayList;

/**
 * Created by vidxyz on 2/9/18.
 */

public class ConsumablesMenu {

    // Max items you can hold at once in your inventory
    private static final int MAX_CONSUMABLES = 4;

    private Texture[] inventoryElementTextures;
    private Texture inventoryListTexture;

    private SpaceJunk currentGame;

    public ConsumablesMenu(SpaceJunk currentGame) {

        this.currentGame = currentGame;

        inventoryElementTextures = new Texture[MAX_CONSUMABLES];

        // Use these textures to modify what's shown on the inventrory list
        inventoryElementTextures[0] = new Texture("heart.png");
        inventoryElementTextures[0] = new Texture("heart.png");
        inventoryElementTextures[0] = new Texture("heart.png");
        inventoryElementTextures[0] = new Texture("heart.png");

        inventoryListTexture = new Texture("inventory_list.png");
    }

    public void render(SpriteBatch canvas) {

        renderInventoryList(canvas);
        renderInventoryElements(canvas);
    }

    private void renderInventoryList(SpriteBatch canvas) {

        canvas.setColor(1, 1, 1, 0.5f);
        canvas.draw(inventoryListTexture, currentGame.getxMax() - inventoryListTexture.getWidth(), 0);
        canvas.setColor(1, 1, 1, 1);

//        Sprite sprite = new Sprite(inventoryListTexture);
//        sprite.setAlpha(0.5f);
//        canvas.draw(sprite);
    }

    private void renderInventoryElements(SpriteBatch canvas) {
        ArrayList<Consumable> inventoryList = this.currentGame.getLevel().getInventoryList();

        for(int i = 0; i < MAX_CONSUMABLES; i++) {
            if(inventoryList.get(i) != null) {
                canvas.draw(inventoryList.get(i).getTexture(),
                        (this.currentGame.getxMax() - ((i + 1) * inventoryListTexture.getWidth() / 4) + (12 - (i * 2))),
                        inventoryListTexture.getHeight() / 7);
            }
        }
    }
}
