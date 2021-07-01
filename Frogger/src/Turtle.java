import java.awt.*;

/**
 * Frogger
 * Author: Peter Mitchell (2021)
 *
 * Turtle class:
 * Represents a simple turtle that can have multiple turtles moving left or right.
 */
public class Turtle extends MovingObject {
    /**
     * The distance to move during each update in pixels.
     */
    private static final int MOVE_DISTANCE = GamePanel.SEGMENT_HEIGHT;
    /**
     * The time in ms to delay between moves.
     */
    private static final int MOVE_DELAY = 1000;
    /**
     * The number of segments. Used to determine how many turtles are drawn.
     */
    private int unitWidth;
    /**
     * The colour used to outline the turtles.
     */
    private final Color outlineColour = new Color(30, 78, 18);

    /**
     * Initialises a turtle to move in the defined direction. Turtles are safe to travel on.
     *
     * @param offsetX Position to offset the X coordinate from default spawning.
     * @param yRow The row to set as a vertical position.
     * @param unitWidth Number of segments. Used to determine the length.
     * @param isMovingLeft When true the car will move left, otherwise it will move right.
     */
    public Turtle(int offsetX, int yRow, int unitWidth, boolean isMovingLeft) {
        super(new Position(offsetX-unitWidth*GamePanel.SEGMENT_HEIGHT,
                        yRow*GamePanel.SEGMENT_HEIGHT+(GamePanel.SEGMENT_HEIGHT-MovingObject.OBJECT_HEIGHT)/2),
                unitWidth * GamePanel.SEGMENT_HEIGHT, new Color(64, 146, 35),
                isMovingLeft, MOVE_DISTANCE, MOVE_DELAY);
        this.unitWidth = unitWidth;
        isSafe = true;
    }

    /**
     * Draws a turtle for every unitWidth.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    @Override
    public void paint(Graphics g) {
        for(int i = 0; i < unitWidth; i++) {
            // Draw head
            g.setColor(drawColour);
            g.fillOval(position.x+i*GamePanel.SEGMENT_HEIGHT-4, position.y + height/2-4, 8, 8);
            g.setColor(outlineColour);
            g.drawOval(position.x+i*GamePanel.SEGMENT_HEIGHT-4, position.y + height/2-4, 8, 8);
            // Draw body
            g.setColor(drawColour);
            g.fillOval(position.x+i*GamePanel.SEGMENT_HEIGHT+2, position.y, GamePanel.SEGMENT_HEIGHT-4, height);
            g.setColor(outlineColour);
            g.drawOval(position.x+i*GamePanel.SEGMENT_HEIGHT+2, position.y, GamePanel.SEGMENT_HEIGHT-4, height);
            // Draw eyes
            g.setColor(Color.BLACK);
            g.fillOval(position.x+i*GamePanel.SEGMENT_HEIGHT-2, position.y + height/2-4, 2, 2);
            g.fillOval(position.x+i*GamePanel.SEGMENT_HEIGHT-2, position.y + height/2, 2, 2);
        }
    }

    /**
     * Resets by generating a new unitWidth and then updating position/width correctly.
     */
    @Override
    public void reset() {
        super.reset();
        unitWidth = getRandomSegmentLength();
        width = unitWidth * GamePanel.SEGMENT_HEIGHT;
        position.x = moveDirection.equals(Position.RIGHT) ? -(getMaxLength() * GamePanel.SEGMENT_HEIGHT)
                :(GamePanel.PANEL_WIDTH/GamePanel.SEGMENT_HEIGHT+1)*(GamePanel.SEGMENT_HEIGHT);
    }

    /**
     * Gets a valid random number of segments for a turtle.
     *
     * @return A random number between 1 and 3.
     */
    public static int getRandomSegmentLength() {
        return (int)(Math.random()*3+1);
    }

    /**
     * Gets the maximum number of segments.
     *
     * @return The maximum length of a turtle.
     */
    public static int getMaxLength() {
        return 3;
    }
}
