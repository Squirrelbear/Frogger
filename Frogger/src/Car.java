import java.awt.*;

/**
 * Frogger
 * Author: Peter Mitchell (2021)
 *
 * Car class:
 * Defines a car that can move left or right.
 * The car can appear with various lengths and will appear as more like a truck if the length is long enough.
 */
public class Car extends MovingObject {
    /**
     * All the colours that the cars can appear as.
     */
    private static final Color[] CAR_COLOURS = { Color.CYAN, new Color(27, 57, 167), Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN };
    /**
     * Distance moves during each update in pixels.
     */
    private static final int MOVE_DISTANCE = 3;
    /**
     * Time in ms between movements.
     */
    private static final int MOVE_DELAY = 40;
    /**
     * Number of segments. Used to determine the visual length.
     */
    private int unitWidth;

    /**
     * Initialises a car to move in the defined direction.
     *
     * @param offsetX Position to offset the X coordinate from default spawning.
     * @param yRow The row to set as a vertical position.
     * @param unitWidth Number of segments. Used to determine the length.
     * @param isMovingLeft When true the car will move left, otherwise it will move right.
     */
    public Car(int offsetX, int yRow, int unitWidth, boolean isMovingLeft) {
        super(new Position(offsetX+(isMovingLeft?1:-1)*unitWidth*GamePanel.SEGMENT_HEIGHT,
                        yRow*GamePanel.SEGMENT_HEIGHT+(GamePanel.SEGMENT_HEIGHT-MovingObject.OBJECT_HEIGHT)/2),
                unitWidth * GamePanel.SEGMENT_HEIGHT, CAR_COLOURS[(int)(Math.random()*CAR_COLOURS.length)],
                isMovingLeft, MOVE_DISTANCE, MOVE_DELAY);
        this.unitWidth = unitWidth;
    }

    /**
     * Draws a car if the segment count is less than 3, otherwise draws a truck.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(Color.WHITE);
        if(unitWidth < 3) {
            // Draw a car
            int windowFrontX = moveDirection.equals(Position.LEFT) ? position.x + 20 : position.x + width - 30;
            g.fillRect(windowFrontX, position.y+5, 10, height-10);
            int windowBackX = moveDirection.equals(Position.RIGHT) ? position.x + 10 : position.x + width - 15;
            g.fillRect(windowBackX, position.y+5, 5, height-10);
        } else {
            // Draw a truck
            int windowFrontX = moveDirection.equals(Position.LEFT) ? position.x + 10 : position.x + width - 20;
            g.fillRect(windowFrontX, position.y+5, 10, height-10);
            int wheelBackX = moveDirection.equals(Position.RIGHT) ? position.x + 10 : position.x + width - 20;
            g.setColor(new Color(52, 49, 49));
            g.fillRect(wheelBackX, position.y-3, 10, 6);
            g.fillRect(wheelBackX, position.y+height-3, 10, 6);
            g.fillRect(windowFrontX, position.y-3, 10, 3);
            g.fillRect(windowFrontX, position.y+height, 10, 3);
            int cabinBackX = moveDirection.equals(Position.LEFT) ? position.x + 25 : position.x + width - 35;
            g.drawLine(cabinBackX, position.y+1, cabinBackX, position.y+height-1);
        }
    }

    /**
     * Resets the car back to a default position with new random length and colour.
     */
    @Override
    public void reset() {
        super.reset();
        unitWidth = getRandomSegmentLength();
        width = unitWidth * GamePanel.SEGMENT_HEIGHT;
        position.x = moveDirection.equals(Position.RIGHT) ? -(getMaxLength() * GamePanel.SEGMENT_HEIGHT)
                :(GamePanel.PANEL_WIDTH/GamePanel.SEGMENT_HEIGHT+1)*(GamePanel.SEGMENT_HEIGHT);
        drawColour = CAR_COLOURS[(int)(Math.random()*CAR_COLOURS.length)];
    }

    /**
     * Gets a valid random number of segments for a car.
     *
     * @return A random number between 2 and 3.
     */
    public static int getRandomSegmentLength() {
        return (int)(Math.random()*2)+2;
    }

    /**
     * Gets the maximum number of segments.
     *
     * @return The maximum length of a car.
     */
    public static int getMaxLength() {
        return 3;
    }
}
