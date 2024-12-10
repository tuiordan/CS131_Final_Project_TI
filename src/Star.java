import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * The Star class represents a collectible item (star) in the game world.
 * The star has a position, size, and an image associated with it.
 * It is initially active and can be drawn on the screen.
 * This class extends the GameObject class.
 */
public class Star extends GameObject{

    // A boolean that indicates if the star is active (collectible)
    private boolean isActive;

    /**
     * Constructs a new Star object at the specified position.
     * The star is stationary, with no velocity or acceleration.
     * The image of the star is loaded from a file, and it is active by default.
     * The size of the star is half the size of a tile (used in the game world).
     *
     * @param x The x-coordinate of the star's position.
     * @param y The y-coordinate of the star's position.
     */
    public Star(double x, double y) {
        this.vx = 0; // Horizontal velocity (star is stationary)
        this.vy = 0; // Vertical velocity (star is stationary)
        this.ay = 0; // Vertical acceleration (star is stationary)
        this.ax = 0; // Horizontal acceleration (star is stationary)
        this.x = x; // Set the x-coordinate of the star
        this.y = y; // Set the y-coordinate of the star
        this.w = Main.TILE_SIZE / 2; // Set the width to half the size of a tile
        this.h = Main.TILE_SIZE / 2; // Set the height to half the size of a tile
        this.image = new Image("star.png"); // Load the image of the star
        isActive = true; // The star is active by default (collectible)
    }//end constructor

    /**
     * Updates the state of the star. In this case, it calls the superclass's update method,
     *
     * @param deltaTime The time elapsed since the last update (used for physics calculations).
     */
    public void update(double deltaTime) {
        super.update(deltaTime); // Call the update method of the superclass
    }//end update
    
     /**
     * Draws the star on the canvas at its current position.
     * The star is drawn using the image and its current coordinates, with a level offset for scrolling effects.
     *
     * @param context The GraphicsContext used for drawing on the canvas.
     * @param lvlOffset The horizontal offset for the star's drawing position (useful for scrolling).
     */
    @Override
    public void draw(GraphicsContext context, double lvlOffset) {
        context.drawImage(image, x - lvlOffset, y, w, h); // Draw the star image at the current position
    }//end draw
    
    /**
     * Returns whether the star is currently active.
     *
     * @return True if the star is active, otherwise false.
     */
    public boolean isActive() {
        return isActive;
    }//end isActive

     /**
     * Sets the active state of the star.
     * If the star is set to inactive, it can no longer be collected.
     *
     * @param isActive True to activate the star, false to deactivate it.
     */
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }//end setActive
    
}//end class
