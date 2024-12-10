import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * An abstract class that defines the physics and behavior of game objects.
 * 
 * Game objects in the game world have properties such as position, velocity, 
 * acceleration, size, and an associated image. This class provides methods 
 * for updating the physics of the object and retrieving its bounding box dimensions.
 */
public abstract class GameObject {
    protected double x, y; // The x and y positions of the object
    protected double vx, vy; // The velocities along the x and y axes
    protected double ax, ay; // The accelerations along the x and y axes
    protected double w, h; // The width and height of the object
    protected Image image; // The image representing the object

    /**
     * Draws the game object on the screen.
     * 
     * @param context   the GraphicsContext used to draw the object
     * @param lvlOffset the horizontal offset of the level, used for scrolling
     */
    public abstract void draw(GraphicsContext context, double lvlOffset);

    /**
     * Updates the physics of the game object.
     * 
     * This method calculates the new velocity and position of the object
     * based on its acceleration and the elapsed time.
     * 
     * @param deltaTime the time elapsed since the last update, in seconds
     */
    protected void updatePhysics(double deltaTime) {
        vx += deltaTime * ax; // Update velocity in the x direction
        vy += deltaTime * ay; // Update velocity in the y direction
        x += deltaTime * vx;  // Update position in the x direction
        y += deltaTime * vy;  // Update position in the y direction
    }//end updatePhysics

    /**
     * Updates the state of the game object.
     * 
     * This method is intended to be called once per frame and delegates 
     * physics updates to the `updatePhysics` method.
     * 
     * @param deltaTime the time elapsed since the last update, in seconds
     */
    public void update(double deltaTime) {
        updatePhysics(deltaTime);
    }//end update

    /**
     * Gets the top (up) position of the game object.
     * 
     * @return the y-coordinate of the top of the object
     */
    public double getUp() {
        return y;
    }//end getUp

    /**
     * Gets the bottom (down) position of the game object.
     * 
     * @return the y-coordinate of the bottom of the object
     */
    public double getDown() {
        return y + h;
    }//end getDown

    /**
     * Gets the left position of the game object.
     * 
     * @return the x-coordinate of the left side of the object
     */
    public double getLeft() {
        return x;
    }//end getLeft

    /**
     * Gets the right position of the game object.
     * 
     * @return the x-coordinate of the right side of the object
     */
    public double getRight() {
        return x + w;
    }//end getRight

    /**
     * Sets the x-coordinate of the game object's position.
     * 
     * @param x the new x-coordinate
     */
    public void setX(double x) {
        this.x = x;
    }//end setX

    /**
     * Sets the y-coordinate of the game object's position.
     * 
     * @param y the new y-coordinate
     */
    public void setY(double y) {
        this.y = y;
    }//end setY

}//end class
