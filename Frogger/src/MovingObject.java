import java.awt.*;

/**
 * Frogger
 * Author: Peter Mitchell (2021)
 *
 * MovingObject class:
 * Defines a generic moving object that can move left or right.
 */
public class MovingObject extends Rectangle {
    /**
     * Height of moving objects.
     */
    public static final int OBJECT_HEIGHT = GamePanel.SEGMENT_HEIGHT * 4 / 5;

    /**
     * A unit vector representing the direction of motion.
     */
    protected Position moveDirection;
    /**
     * Timer to delay movements.
     */
    private ActionTimer moveTimer;
    /**
     * Distance to move each time the timer triggers.
     */
    private int moveDistance;
    /**
     * The colour to draw the object with.
     */
    protected Color drawColour;
    /**
     * When true the object is ready to be reset because it has moved off screen.
     */
    private boolean isExpired;
    /**
     * A safe object can be walked on in the water area and can be attached to by the player.
     */
    protected boolean isSafe;

    /**
     * @param position The position to place the object at.
     * @param width The width of the object.
     * @param drawColour The colour to draw the object with.
     * @param isMovingLeft When true the object will always move left, otherwise right.
     * @param moveDistance The distance to move during each movement in pixels.
     * @param moveDelay The delay in movements in ms.
     */
    public MovingObject(Position position, int width, Color drawColour, boolean isMovingLeft, int moveDistance, int moveDelay) {
        super(position, width, OBJECT_HEIGHT);
        this.moveDirection = isMovingLeft ? Position.LEFT : Position.RIGHT;
        moveTimer = new ActionTimer(moveDelay);
        this.moveDistance = moveDistance;
        this.drawColour = drawColour;
        isExpired = false;
        isSafe = false;
    }

    /**
     * Updates the movement timer. If it triggers the object is moved by their defined amount.
     * And then checks are made to determine if the object has moved off screen and needs to be reset.
     *
     * @param deltaTime Time since last update.
     */
    public void update(int deltaTime) {
        moveTimer.update(deltaTime);
        if(moveTimer.isTriggered()) {
            moveTimer.reset();
            Position translation = new Position(moveDirection);
            translation.multiply(moveDistance);
            position.add(translation);

            // Check if the object is ready to be reset from moving off screen
            if((moveDirection.equals(Position.LEFT) && position.x < -width)
                || (moveDirection.equals(Position.RIGHT) && position.x > GamePanel.PANEL_WIDTH)) {
                isExpired = true;
            }
        }
    }

    /**
     * Draws a rectangle based on the defined properties of the object.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        g.setColor(drawColour);
        g.fillRect(position.x, position.y, width, height);
    }

    /**
     * Resets the object setting it back to not expired.
     */
    public void reset() {
        isExpired = false;
    }

    /**
     * True when the object has moved off screen and no longer serves a purpose in
     * its current state.
     *
     * @return True if the object requires resetting.
     */
    public boolean isExpired() {
        return isExpired;
    }

    /**
     * Allows the player to check if the object can be walked on to ride.
     *
     * @return True if the object can be walked on.
     */
    public boolean isSafe() {
        return isSafe;
    }
}
