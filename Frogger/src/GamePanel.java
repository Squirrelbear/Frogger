import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Frogger
 * Author: Peter Mitchell (2021)
 *
 * GamePanel class:
 * Manages the game objects with regular updates.
 */
public class GamePanel extends JPanel implements ActionListener {
    /**
     * Time between updates in ms.
     */
    public static final int TIME_INTERVAL = 20;
    /**
     * Height of the game panel.
     */
    public static final int PANEL_HEIGHT = 600;
    /**
     * Width of the game panel.
     */
    public static final int PANEL_WIDTH = 400;
    /**
     * Segment height used for all the spacing out math to make elements appear relative to eachother.
     */
    public static final int SEGMENT_HEIGHT = GamePanel.PANEL_HEIGHT/14;

    /**
     * The background elements that are all static.
     */
    private Background background;
    /**
     * Timer that triggers on the TIME_INTERVAL and causes updates.
     */
    private Timer gameTimer;
    /**
     * A list of all the objects that are moving either left or right as obstacles
     */
    private List<MovingObject> objectList;
    /**
     * The player object that controls its own lives, score, and frogs.
     */
    private Player player;

    /**
     * Creates all the elements ready to start the game and starts updates.
     */
    public GamePanel() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        background = new Background();
        objectList = new ArrayList<>();
        gameTimer = new Timer(TIME_INTERVAL, this);
        initMovingObjects();
        player = new Player(background, objectList);
        gameTimer.start();
    }

    /**
     * Draws the background elements, all the moving objects, the player elements,
     * and if the game has ended an overlay to indicate it.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    public void paint(Graphics g) {
        background.paint(g);
        objectList.forEach(obj -> obj.paint(g));
        player.paint(g);
        if(player.isGameEnded()) {
            drawGameOver(g);
        }
    }

    /**
     * Resets the player and moving objects back to defaults.
     */
    public void restart() {
        player.reset();
        initMovingObjects();
    }

    /**
     * Checks for escape to quit, R to restart, and otherwise passes off to the player for player input.
     *
     * @param keyCode The key that was pressed or released.
     * @param isPressed True indicates the key is pressed, false indicates it has been released.
     */
    public void handleInput(int keyCode, boolean isPressed) {
        if(keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(0);
        } else if(keyCode == KeyEvent.VK_R) {
            restart();
        } else {
            player.handleInput(keyCode, isPressed);
        }
        repaint();
    }

    /**
     * If the game hasn't ended the game elements will be updated.
     * Initially the player is updated, then all the objects. If the
     * player is inside one of the objects attached to them it will
     * be managed in the postUpdate() call to player.
     *
     * @param e Information about the event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!player.isGameEnded()) {
            player.update(TIME_INTERVAL);
            for (int i = 0; i < objectList.size(); i++) {
                objectList.get(i).update(TIME_INTERVAL);
                if (objectList.get(i).isExpired()) {
                    objectList.get(i).reset();
                }
            }
            player.postUpdate();
        }
        repaint();
    }

    /**
     * Clears all the moving objects and spawns new sets of the
     * logs, cars, and turtles.
     */
    private void initMovingObjects() {
        objectList.clear();
        initLogs();
        initCars();
        initTurtles();
    }

    /**
     * Spawns all the moving log elements.
     */
    private void initLogs() {
        int middleSegment = PANEL_WIDTH / SEGMENT_HEIGHT / 2+1;
        objectList.add(new Log(0, 1, Log.getRandomSegmentLength(), false));
        objectList.add(new Log(-middleSegment*SEGMENT_HEIGHT, 1, Log.getRandomSegmentLength(), false));
        objectList.add(new Log(-SEGMENT_HEIGHT, 3, Log.getRandomSegmentLength(), false));
        objectList.add(new Log(-(middleSegment+1)*SEGMENT_HEIGHT, 3, Log.getRandomSegmentLength(), false));
        objectList.add(new Log(0, 5, Log.getRandomSegmentLength(), false));
        objectList.add(new Log(-middleSegment*SEGMENT_HEIGHT, 5, Log.getRandomSegmentLength(), false));
    }

    /**
     * Spawns all the moving car elements.
     */
    private void initCars() {
        int middleSegment = PANEL_WIDTH / SEGMENT_HEIGHT / 2+1;
        objectList.add(new Car(0, 7, Car.getRandomSegmentLength(), false));
        objectList.add(new Car(-middleSegment*SEGMENT_HEIGHT, 7, Car.getRandomSegmentLength(), false));
        objectList.add(new Car(-SEGMENT_HEIGHT, 9, Car.getRandomSegmentLength(), false));
        objectList.add(new Car(-(middleSegment+1)*SEGMENT_HEIGHT, 9, Car.getRandomSegmentLength(), false));
        objectList.add(new Car(0, 11, Car.getRandomSegmentLength(), false));
        objectList.add(new Car(-middleSegment*SEGMENT_HEIGHT, 11, Car.getRandomSegmentLength(), false));

        objectList.add(new Car(0, 8, Car.getRandomSegmentLength(), true));
        objectList.add(new Car(middleSegment*SEGMENT_HEIGHT, 8, Car.getRandomSegmentLength(), true));
        objectList.add(new Car(SEGMENT_HEIGHT, 10, Car.getRandomSegmentLength(), true));
        objectList.add(new Car((middleSegment+1)*SEGMENT_HEIGHT, 10, Car.getRandomSegmentLength(), true));
    }

    /**
     * Spawns all the moving turtle elements.
     */
    private void initTurtles() {
        int middleSegment = PANEL_WIDTH / SEGMENT_HEIGHT / 2+1;
        objectList.add(new Turtle(0, 2, Turtle.getRandomSegmentLength(), true));
        objectList.add(new Turtle(middleSegment*SEGMENT_HEIGHT, 2, Turtle.getRandomSegmentLength(), true));
        objectList.add(new Turtle(SEGMENT_HEIGHT, 4, Turtle.getRandomSegmentLength(), true));
        objectList.add(new Turtle((middleSegment+1)*SEGMENT_HEIGHT, 4, Turtle.getRandomSegmentLength(), true));
    }

    /**
     * Draws a white background with black text to show the game over message.
     *
     * @param g Reference to the Graphics object for rendering.
     */
    private void drawGameOver(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0,PANEL_HEIGHT/2-20, PANEL_WIDTH, 40);
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        String gameOverMessage = "All Frogs Expended!";
        int strWidth = g.getFontMetrics().stringWidth(gameOverMessage);
        g.drawString(gameOverMessage, PANEL_WIDTH/2-strWidth/2, PANEL_HEIGHT/2+10);
    }
}
