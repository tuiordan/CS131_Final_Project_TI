import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Represents a Lava object in the game, extending the Platform class.
 * This class defines a red platform that acts as a danger in the game world.
 */
public class Lava extends Platform {

    /**
     * Constructs a Lava object with the specified position and size.
     * The color of the lava is set to red by default.
     *
     * @param x      the x-coordinate of the lava.
     * @param y      the y-coordinate of the lava.
     * @param width  the width of the lava platform.
     * @param height the height of the lava platform.
     */
    public Lava(double x, double y, double width, double height){
        super(x, y, width, height); // Call the parent class constructor.
        this.color = Color.RED;    // Set the color of the lava to red.
    }//end constructor

    /**
     * Updates the lava's state based on the time elapsed.
     * For now, this simply invokes the parent class's update logic.
     *
     * @param deltaTime the time elapsed since the last update, in seconds.
     */
    public void update(double deltaTime) {
        super.update(deltaTime);
    }//end update


    /**
     * Draws the Lava object on the screen using the specified graphics context.
     * The lava is rendered as a filled rectangle with its specified position,
     * size, and red color.
     *
     * @param context   the GraphicsContext used for rendering the lava.
     * @param lvlOffset the offset to apply for scrolling levels.
     */
    @Override
    public void draw(GraphicsContext context, double lvlOffset) {
        context.setFill(color);
        context.fillRect(x - lvlOffset, y, w, h);
    }//end draw

}//end class
