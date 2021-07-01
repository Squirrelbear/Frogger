import org.w3c.dom.css.Rect;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Frogger
 * Author: Peter Mitchell (2021)
 *
 * Player class:
 * Manages the collection of frogs and is responsible for showing the lives/score.
 */
public class Player {
    /**
     * Status of the keys for left/right/up to determine if movement should happen during updates.
     */
    private boolean keyLeftIsPressed, keyRightIsPressed, keyUpIsPressed;

    /**
     * The magnitude of movement translation for left/right movement.
     */
    private final int moveRate = 5;

    /**
     * Remaining lives that the player has.
     */
    private int lives;
    /**
     * The current frog that the player can control with movement.
     */
    private Frog currentFrog;
    /**
     * A reference to the background to query regions and the lilies.
     */
    private Background background;
    /**
     * A reference to all the moving objects to check collisions.
     */
    private List<MovingObject> movingObjects;
    /**
     * The object that has been attached to as a safe object.
     */
    private MovingObject attachedObject;
    /**
     * Position of the attached object to use for relative movement on the object.
     */
    private Position objectPosition;
    /**
     * All frogs that the player controls. Including the current frog and any that have already been used for score.
     */
    private List<Frog> frogs;
    /**
     * Frogs shown in the bottom right to represent the lives.
     */
    private List<Frog> livesVisual;
    /**
     * When true the player has run out of lives and has no current frog they are moving.
     */
    private boolean gameEnded;
    /**
     * The current score of the player awarded for making frogs reach the lilies.
     */
    private int score;

    /**
     * Sets up the player ready with all their lives and a frog ready to move.
     *
     * @param background A reference to the background to query regions and the lilies.
     * @param movingObjects A reference to all the moving objects to check collisions.
     */
    public Player(Background background, List<MovingObject> movingObjects) {
        this.background = background;
        this.movingObjects = movingObjects;
        frogs = new ArrayList<>();
        livesVisual = new ArrayList<>();
        reset();
    }

    /**
     * Resets all the state variables for the lives, score, input, and spawns a new frog.
     */
    public void reset() {
        frogs.clear();
        keyUpIsPressed = keyLeftIsPressed = keyRightIsPressed = false;
        attachedObject = null;
        lives = 4;
        score = 0;
        livesVisual.clear();
        Position startLivesPosition = new Position(background.getScoreZone().position);
        livesVisual.add(new Frog(new Position(startLivesPosition), GamePanel.SEGMENT_HEIGHT, GamePanel.SEGMENT_HEIGHT));
        startLivesPosition.x += GamePanel.SEGMENT_HEIGHT;
        livesVisual.add(new Frog(new Position(startLivesPosition), GamePanel.SEGMENT_HEIGHT, GamePanel.SEGMENT_HEIGHT));
        startLivesPosition.x += GamePanel.SEGMENT_HEIGHT;
        livesVisual.add(new Frog(new Position(startLivesPosition), GamePanel.SEGMENT_HEIGHT, GamePanel.SEGMENT_HEIGHT));
        startLivesPosition.x += GamePanel.SEGMENT_HEIGHT;
        livesVisual.add(new Frog(new Position(startLivesPosition), GamePanel.SEGMENT_HEIGHT, GamePanel.SEGMENT_HEIGHT));
        spawnFrog();
        gameEnded = false;
    }

    /**
     * Moves the frog if the up/left/right arrow keys were pressed since last update.
     * Checks for any object that should be attached to and tracks its position ready to update
     * in postUpdate().
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        if(currentFrog == null || currentFrog.position.y <= background.getEndZone().position.y) {
            keyUpIsPressed = keyLeftIsPressed = keyRightIsPressed = false;
            attachedObject = null;
            return;
        }

        if(keyUpIsPressed) {
            keyUpIsPressed = false;
            currentFrog.position.y -= GamePanel.SEGMENT_HEIGHT;
            attachedObject = findCollidedMovingObject(15);
            // Only attach to an object if it is safe.
            if(attachedObject != null && !attachedObject.isSafe()) attachedObject = null;
        }
        if(keyLeftIsPressed) {
            moveWithinBounds(new Position(-moveRate,0), GamePanel.PANEL_WIDTH-currentFrog.width, GamePanel.PANEL_HEIGHT);
        }
        if(keyRightIsPressed) {
            moveWithinBounds(new Position(moveRate,0), GamePanel.PANEL_WIDTH-currentFrog.width, GamePanel.PANEL_HEIGHT);
        }
        if(attachedObject != null) {
            // Check if the frog has moved along to another safe object or off the current safe object
            MovingObject checkedAttachedObject = findCollidedMovingObject(15);
            if(checkedAttachedObject != attachedObject) {
                attachedObject = checkedAttachedObject;
            }
            if(attachedObject != null) {
                objectPosition = new Position(attachedObject.position);
            }
        }
    }

    /**
     * Updates any movement based on an attached object. Then checks if the frog has died,
     * or reached a lilly. Then spawns a new frog unless the lives have run out.
     */
    public void postUpdate() {
        if(currentFrog == null) return;

        // Move object to keep it in the same relative position on the attached object.
        if(attachedObject != null) {
            Position offset = new Position(attachedObject.position);
            offset.subtract(objectPosition);
            moveWithinBounds(offset, GamePanel.PANEL_WIDTH-currentFrog.width, GamePanel.PANEL_HEIGHT);
        }

        // Test for the frog being somewhere they should be removed from (hit a car, in water, or at the end)
        boolean spawnAnotherFrog = false;
        MovingObject collidedObj = findCollidedMovingObject(3);
        Rectangle collidedLily = getCollidedLily(40);
        if((collidedObj != null && !collidedObj.isSafe())
        || (collidedObj == null && isFrogInWaterArea())
        || (currentFrog.position.y == 0)) {
            spawnAnotherFrog = true;
            if(collidedLily == null || isCollidingWithAnotherFrog()) {
                // Frog died... oops
                frogs.remove(frogs.size() - 1);
            } else {
                score += 100;
            }
            currentFrog = null;
        }
        if(spawnAnotherFrog && lives > 0) {
            spawnFrog();
        } else if(lives == 0 && currentFrog == null) {
            gameEnded = true;
        }
    }

    /**
     * Pressing/Releasing left/right updates their state, and pressing up will trigger an up.
     * Input is delayed till next update.
     *
     * @param keyCode The key that was interacted with.
     * @param isPressed The state whether it be pressed (true) or released (false).
     */
    public void handleInput(int keyCode, boolean isPressed) {
        if(keyCode == KeyEvent.VK_LEFT) {
            keyLeftIsPressed = isPressed;
        } else if(keyCode == KeyEvent.VK_RIGHT) {
            keyRightIsPressed = isPressed;
        } else if(keyCode == KeyEvent.VK_UP && isPressed) {
            keyUpIsPressed = true;
        }
    }

    /**
     * Draws all the frogs, all the lives, and the score.
     *
     * @param g Reference to the graphics object for rendering.
     */
    public void paint(Graphics g) {
        frogs.forEach(f -> f.paint(g));
        livesVisual.forEach(f -> f.paint(g));
        drawScore(g);
    }

    /**
     * The game ends when the player has run out of lives and they do not have a current frog.
     *
     * @return True if the game has ended and the player can't do anything.
     */
    public boolean isGameEnded() {
        return gameEnded;
    }

    /**
     * Moves based on the translationVector, but clamps the movement within the bounds of the play space.
     *
     * @param translationVector Added to position to calculate the new position.
     */
    private void moveWithinBounds(Position translationVector, int maxX, int maxY) {
        int newX = currentFrog.position.x+translationVector.x;
        int newY = currentFrog.position.y+translationVector.y;
        if(newX < 0) newX = 0;
        else if(newX > maxX) newX = maxX;
        if(newY < 0) newY = 0;
        else if(newY > maxY) newY = maxY;
        currentFrog.position.setPosition(newX, newY);
    }

    /**
     *
     *
     * @param minimumPercent Percent from 0 to 100.
     * @return Null or the object that is intersected with covering a minimumPercent of coverage.
     */
    private MovingObject findCollidedMovingObject(double minimumPercent) {
        if(currentFrog == null) return null;

        for(MovingObject movingObject : movingObjects) {
            if(currentFrog.isIntersecting(movingObject)) {
                if(currentFrog.getOverlapPercent(movingObject) >= minimumPercent) {
                    return movingObject;
                }
            }
        }
        return null;
    }

    /**
     * Spawns a frog by subtracting a life and adding the new frog in the spawn position.
     */
    private void spawnFrog() {
        lives--;
        livesVisual.remove(livesVisual.size()-1);
        Frog newFrog = new Frog(new Position((GamePanel.PANEL_WIDTH/GamePanel.SEGMENT_HEIGHT /2)*GamePanel.SEGMENT_HEIGHT, 12*GamePanel.SEGMENT_HEIGHT),
                GamePanel.SEGMENT_HEIGHT, GamePanel.SEGMENT_HEIGHT);
        frogs.add(newFrog);
        currentFrog = newFrog;
    }

    /**
     * Checks if the frog's y position is inside the water area.
     *
     * @return True if the frog is in the water area.
     */
    private boolean isFrogInWaterArea() {
        return (currentFrog.position.y >= background.getWaterZone().position.y &&
                currentFrog.position.y <= background.getWaterZone().position.y+background.getWaterZone().height-5);
    }

    /**
     * Checks if there is a lily collided with that meets the minimum percent of coverage.
     *
     * @param minimumCollision Percent from 0 to 100.
     * @return Null or a lily matching the requirements.
     */
    private Rectangle getCollidedLily(double minimumCollision) {
        for(Rectangle lily : background.getLilies()) {
            if(lily.getOverlapPercent(currentFrog) >= minimumCollision) {
                return lily;
            }
        }
        return null;
    }

    /**
     * Tests if the current frog is intersecting with any other active frog.
     *
     * @return True if the current frog is intersecting with any other frog.
     */
    private boolean isCollidingWithAnotherFrog() {
        for(Frog f : frogs) {
            if(f != currentFrog && f.isIntersecting(currentFrog)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Draws the score in the bottom left corner.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawScore(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String scoreStr = score + " :Score";
        int strWidth = g.getFontMetrics().stringWidth(scoreStr);
        g.drawString(scoreStr, GamePanel.PANEL_WIDTH-strWidth-15, background.getScoreZone().position.y+30);
    }
}
