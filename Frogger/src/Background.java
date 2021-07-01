import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Frogger
 * Author: Peter Mitchell (2021)
 *
 * Background class:
 * Defines the background elements with regions that can be retrieved for other uses.
 */
public class Background {
    /**
     * The zone at the top of the screen where the frog will stop.
     */
    private Rectangle endZone;
    /**
     * The middle safe zone where nothing can hit the frog.
     */
    private Rectangle middleZone;
    /**
     * The start zone where the frogs spawn.
     */
    private Rectangle startZone;
    /**
     * The region where cars are going across as hazards.
     */
    private Rectangle roadZone;
    /**
     * The water region where the frog must be on an object to not die.
     */
    private Rectangle waterZone;
    /**
     * The region at the bottom where the score and lives are shown.
     */
    private Rectangle scoreZone;
    /**
     * The lilies that are all in the endZone.
     */
    private List<Rectangle> lilies;

    /**
     * Generates all the partitioning of the sections for the game.
     */
    public Background() {
        endZone = new Rectangle(new Position(0,0),GamePanel.PANEL_WIDTH, GamePanel.SEGMENT_HEIGHT);
        middleZone = new Rectangle(new Position(0,6*GamePanel.SEGMENT_HEIGHT),GamePanel.PANEL_WIDTH,GamePanel.SEGMENT_HEIGHT);
        startZone = new Rectangle(new Position(0,12*GamePanel.SEGMENT_HEIGHT),GamePanel.PANEL_WIDTH,GamePanel.SEGMENT_HEIGHT);
        roadZone = new Rectangle(new Position(0,7*GamePanel.SEGMENT_HEIGHT),GamePanel.PANEL_WIDTH,5*GamePanel.SEGMENT_HEIGHT);
        waterZone = new Rectangle(new Position(0,GamePanel.SEGMENT_HEIGHT),GamePanel.PANEL_WIDTH,5*GamePanel.SEGMENT_HEIGHT);
        scoreZone = new Rectangle(new Position(0,13*GamePanel.SEGMENT_HEIGHT), GamePanel.PANEL_WIDTH, GamePanel.PANEL_HEIGHT-GamePanel.SEGMENT_HEIGHT*12);

        lilies = new ArrayList<>();
        for(int i = 2; i < 9; i+=2) {
            lilies.add(new Rectangle(endZone.position.x+i*(GamePanel.SEGMENT_HEIGHT), endZone.position.y, GamePanel.SEGMENT_HEIGHT, GamePanel.SEGMENT_HEIGHT));
        }
    }

    /**
     * Draws all the background elements.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        g.setColor(new Color(108, 186, 88));
        g.fillRect(middleZone.position.x,middleZone.position.y,middleZone.width,middleZone.height);
        g.fillRect(startZone.position.x,startZone.position.y,startZone.width,startZone.height);

        g.setColor(Color.BLACK);
        g.fillRect(roadZone.position.x, roadZone.position.y, roadZone.width, roadZone.height);
        g.fillRect(scoreZone.position.x, scoreZone.position.y, scoreZone.width, scoreZone.height);
        g.setColor(Color.WHITE);
        for(int y = roadZone.position.y+GamePanel.SEGMENT_HEIGHT; y < startZone.position.y; y+= GamePanel.SEGMENT_HEIGHT) {
            for(int x = 0; x < GamePanel.PANEL_WIDTH; x+= 20) {
                g.fillRect(x,y,10,4);
            }
        }

        g.setColor(Color.CYAN);
        g.fillRect(waterZone.position.x, waterZone.position.y, waterZone.width, waterZone.height);
        g.fillRect(endZone.position.x,endZone.position.y,endZone.width,endZone.height);

        g.setColor(new Color(108, 186, 88));
        for(Rectangle lily : lilies) {
            g.fillArc(lily.position.x, lily.position.y, lily.width, lily.height, 180, 330);
        }
    }

    /**
     * Gets a reference to the water region.
     *
     * @return A reference to the region where the water is located.
     */
    public Rectangle getWaterZone() {
        return waterZone;
    }

    /**
     * Gets the end zone region.
     *
     * @return A reference to the region where the end is located.
     */
    public Rectangle getEndZone() {
        return endZone;
    }

    /**
     * Gets a list of the lilies for collision checking.
     *
     * @return A list of all the lilies.
     */
    public List<Rectangle> getLilies() {
        return lilies;
    }

    /**
     * Gets the score area.
     *
     * @return A reference to the region where the score is located.
     */
    public Rectangle getScoreZone() {
        return scoreZone;
    }
}
