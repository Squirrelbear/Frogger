import java.awt.*;

/**
 * Frogger
 * Author: Peter Mitchell (2021)
 *
 * Log class:
 * Defines a log that can move left or right.
 */
public class Log extends MovingObject {
    /**
     * The different colour used for the end at the direction the log is travelling.
     */
    private final Color endColour = new Color(94, 63, 24);
    /**
     * The distance moved during each move in pixels.
     */
    private static final int MOVE_DISTANCE = GamePanel.SEGMENT_HEIGHT;
    /**
     * The delay between moves in ms.
     */
    private static final int MOVE_DELAY = 1200;

    /**
     * Initialises a log to move in the defined direction. Logs are safe to travel on.
     *
     * @param offsetX Position to offset the X coordinate from default spawning.
     * @param yRow The row to set as a vertical position.
     * @param unitWidth Number of segments. Used to determine the length.
     * @param isMovingLeft When true the car will move left, otherwise it will move right.
     */
    public Log(int offsetX, int yRow, int unitWidth, boolean isMovingLeft) {
        super(new Position(offsetX-unitWidth*GamePanel.SEGMENT_HEIGHT,
                        yRow*GamePanel.SEGMENT_HEIGHT+(GamePanel.SEGMENT_HEIGHT-MovingObject.OBJECT_HEIGHT)/2),
                    unitWidth * GamePanel.SEGMENT_HEIGHT, new Color(135, 91, 35),
                            isMovingLeft, MOVE_DISTANCE, MOVE_DELAY);
        isSafe = true;
    }

    /**
     * Draws a log consisting of the log body with an oval at each end. The oval at the end where it is moving
     * toward is a different colour to the rest.
     *
     * @param g Reference to the Graphics Object for rendering.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(moveDirection.equals(Position.LEFT) ? endColour : drawColour);
        g.fillOval(position.x - 5, position.y, 10, height);
        g.setColor(moveDirection.equals(Position.LEFT) ? drawColour : endColour);
        g.fillOval(position.x + width - 5, position.y, 10, height);
    }

    /**
     * Resets the log by selecting a new random length and resetting the position.
     */
    @Override
    public void reset() {
        super.reset();
        width = getRandomSegmentLength() * GamePanel.SEGMENT_HEIGHT;
        position.x = moveDirection.equals(Position.RIGHT) ? -(getMaxLength() * GamePanel.SEGMENT_HEIGHT)
                                :(GamePanel.PANEL_WIDTH/GamePanel.SEGMENT_HEIGHT+1)*(GamePanel.SEGMENT_HEIGHT);
    }

    /**
     * Gets a valid random length for a log.
     *
     * @return A number between 1 and 4
     */
    public static int getRandomSegmentLength() {
        return (int)(Math.random()*4+1);
    }

    /**
     * Gets the maximum length a log can be.
     *
     * @return The maximum length a log can be.
     */
    public static int getMaxLength() {
        return 5;
    }
}
