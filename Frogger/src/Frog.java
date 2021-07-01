import java.awt.*;

/**
 * Frogger
 * Author: Peter Mitchell (2021)
 *
 * Frog class:
 * Defines a Frog visual element. Used for both representing lives,
 * and also for representing the Frog elements controlled by the player.
 */
public class Frog extends Rectangle {
    /**
     * Defines a basic Frog that is ready to draw.
     *
     * @param position Position to place the Frog.
     * @param width Width of the Frog.
     * @param height Height of the Frog.
     */
    public Frog(Position position, int width, int height) {
        super(position, width, height);
    }

    /**
     * Draws the Frog by drawing all the arms/legs/body, and eyes.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        // Bounding box
        //g.setColor(Color.BLACK);
        //g.drawRect(position.x, position.y, width, height);
        // arms
        g.setColor(new Color(74, 177, 50));
        g.fillRect(position.x+5, position.y+10, 10, 4);
        g.fillRect(position.x, position.y+6, 4, 4);
        g.fillRect(position.x+width-15, position.y+10, 10, 4);
        g.fillRect(position.x+width-4, position.y+6, 4, 4);
        g.setColor(new Color(21, 64, 12));
        g.drawRect(position.x+5, position.y+10, 10, 4);
        g.drawRect(position.x, position.y+6, 4, 4);
        g.drawRect(position.x+width-15, position.y+10, 10, 4);
        g.drawRect(position.x+width-4, position.y+6, 4, 4);

        // legs
        g.setColor(new Color(21, 64, 12));
        g.drawRect(position.x+5, position.y+height-10, 10, 4);
        g.drawRect(position.x+5, position.y+height-10, 4, 9);
        g.drawRect(position.x+width-17, position.y+height-10, 10, 4);
        g.drawRect(position.x+width-7, position.y+height-10, 4, 9);
        g.setColor(new Color(74, 177, 50));
        g.fillRect(position.x+6, position.y+height-9, 10, 3);
        g.fillRect(position.x+6, position.y+height-9, 3, 8);
        g.fillRect(position.x+width-17, position.y+height-9, 10, 3);
        g.fillRect(position.x+width-6, position.y+height-9, 3, 8);

        // Body
        g.setColor(new Color(74, 177, 50));
        g.fillOval(position.x+width/4+1, position.y, width/2, height);
        g.setColor(new Color(21, 64, 12));
        g.drawOval(position.x+width/4+1, position.y, width/2, height);

        // eyes
        g.setColor(Color.BLACK);
        g.fillOval(position.x + width/2 - 6, position.y + height/2-10, 5, 5);
        g.fillOval(position.x + width/2 + 2, position.y + height/2-10, 5, 5);
    }
}
