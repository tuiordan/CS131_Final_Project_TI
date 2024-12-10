import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * The Platform class represents a static object in the game world that serves as a surface 
 * for other objects (like the player) to interact with, such as jumping or standing.
 * It extends the GameObject class and includes properties for position, size, and color.
 */
public class Platform extends GameObject {

    protected Color color;  // Color of the platform
    protected Rectangle rectangle; // The Rectangle object that defines the visual dimensions of the platform

    /**
     * Constructs a new Platform object with the specified position and size.
     * The platform is initially stationary (no velocity or acceleration).
     * The platform is black by default.
     *
     * @param x The x-coordinate of the platform's position.
     * @param y The y-coordinate of the platform's position.
     * @param width The width of the platform.
     * @param height The height of the platform.
     */
    public Platform(double x, double y, double width, double height){
        this.vx = 0; // Horizontal velocity (platform is stationary)
        this.vy = 0; // Vertical velocity (platform is stationary)
        this.ay = 0; // Vertical acceleration (platform is stationary)
        this.ax = 0; // Horizontal acceleration (platform is stationary)
        this.x = x; // Set the x-coordinate of the platform
        this.y = y; // Set the y-coordinate of the platform
        this.w = width; // Set the width of the platform
        this.h = height; // Set the height of the platform
        this.color = Color.BLACK; // Set the default color of the platform
        rectangle = new Rectangle(w, h); // Initialize the rectangle to represent the platform        
    }//end constructor

    /**
     * Updates the platform state. In this case, the platform does not move,
     * so this method calls the superclass update but does not modify the platform's properties.
     * 
     * @param deltaTime The time elapsed since the last update (used for physics calculations).
     */
    public void update(double deltaTime) {
        super.update(deltaTime);
    }//end update

     /**
     * Draws the platform onto the canvas at its current position and dimensions.
     * The platform is drawn as a filled rectangle.
     * The position is offset by the level offset to handle scrolling or parallax effects.
     *
     * @param context The GraphicsContext used for drawing on the canvas.
     * @param lvlOffset The horizontal offset for the platform's drawing position.
     */
    @Override
    public void draw(GraphicsContext context, double lvlOffset) {
        context.setFill(color); // Set the color of the platform
        context.fillRect(x - lvlOffset, y, w, h); // Draw the platform as a rectangle, with a level offset for scrolling
    }//end draw
    
}//end class
