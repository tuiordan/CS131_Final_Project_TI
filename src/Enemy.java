import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;


/**
 * The Enemy class represents an enemy character in the game.
 * Enemies move towards the player's character (Stickman), jump when above the player,
 * and interact with platforms and level boundaries.
 */
public class Enemy extends GameObject{

    private final double WIDTH = 36; // Width of the enemy character in pixels
    private final double HEIGHT = 90; // Height of the enemy character in pixels
    private final double MAX_VELOCITY = 200; // Maximum speed limit for enemy movement
    private double maxVelocity; // Actual maximum speed (randomized for variation)
    private double levelWidth; // Width of the level in pixels
    private Stickman stickman; // Reference to the player's character for collision and movement logic
    private boolean hasJumped; // Tracks if the enemy is currently in a jump to prevent double jumping

     /**
     * Constructs an Enemy object.
     *
     * @param levelWidth The width of the level in pixels.
     * @param x          The initial x-coordinate of the enemy.
     * @param y          The initial y-coordinate of the enemy.
     * @param stickman   A reference to the Stickman character for collision detection and interaction.
     */
    public Enemy(Double levelWidth, double x, double y, Stickman stickman) {
        this.vx = 0; // Initial horizontal velocity
        this.vy = 0; // Initial vertical velocity
        this.x = x; // Initial x-position
        this.y = y; // Initial y-position
        this.ay = 1200; // Constant vertical acceleration (gravity)
        this.ax = 0; // Initial horizontal acceleration
        this.w = WIDTH; // Width of the enemy
        this.h = HEIGHT; // Height of the enemy
        this.image = new Image("enemyLeft.png"); // Initial image facing left
        this.levelWidth = levelWidth; // Level width for boundary checking
        this.stickman = stickman; // Reference to the Stickman character
        this.maxVelocity = MAX_VELOCITY * Math.random(); // Randomized maximum velocity for variety
        this.hasJumped = false; // Enemy starts grounded
    }//end constructor

    
     /**
     * Updates the enemy's position and velocity.
     *
     * @param deltaTime The time elapsed since the last update, used for smooth movement.
     */
    public void update(double deltaTime) {
        super.update(deltaTime); // Call to GameObject's update for basic physics
        movementEnemy(deltaTime); // Update enemy-specific movement behavior
    }//end update

    
    /**
     * Controls the enemy's movement behavior, including following the Stickman
     * and managing jumping logic.
     *
     * @param deltaTime The time elapsed since the last update, used for smooth movement.
     */
    public void movementEnemy(double deltaTime){

        // Move towards the Stickman
        if (this.getRight() < stickman.getLeft()) {
            ax = 1000; // Accelerate to the right
            image = new Image("enemyRight.png"); // Change image to face right
        } else if (stickman.getLeft() < this.getLeft()) {
            ax = -1000; // Accelerate to the left
            image = new Image("enemyLeft.png"); // Change image to face left
        }

        // Jump if below the Stickman
        if (this.getUp() > stickman.getDown() && !hasJumped) {
            vy = -650; // Set upward velocity for the jump
            hasJumped = true; // Prevent double jumps
        }

         // Enforce level boundaries
         if (y + h > Main.HEIGHT) { // Ground boundary
            y = Main.HEIGHT - h; // Snap to the ground
            hasJumped = false; // Reset jumping ability
        } else if (y < 0) { // Ceiling boundary
            y = 0; // Snap to the ceiling
        }

        if (x + w > levelWidth) { // Right level boundary
            x = levelWidth - w; // Snap to the right edge
        } else if (x < 0) { // Left level boundary
            x = 0; // Snap to the left edge
        }

        // Limit horizontal velocity
        if (vx > maxVelocity) {
            vx = maxVelocity;
        } else if (vx < -maxVelocity) {
            vx = -maxVelocity;
        }
    }//end movementEnemy

    /**
     * Handles collisions between the enemy and a platform.
     *
     * @param platform The platform to check for collision.
     */
    public void collision(Platform platform){
        if ((this.getDown() > platform.getUp() ) && 
            (this.getRight() > platform.getLeft()) && 
            (this.getLeft() < platform.getRight()) && 
            (this.getUp() < platform.getDown())) {

            //collision from above
            if (this.getDown() > platform.getUp() && 
               (this.getRight() > platform.getLeft()) && 
               (this.getLeft() < platform.getRight() - 0) && 
                this.getUp() < platform.getUp()){
                y = platform.getUp() - h; // Snap to the top of the platform
            }
            if  (this.getUp() < platform.getDown() && 
                (this.getRight() > platform.getLeft() + 0) && 
                (this.getLeft() < platform.getRight() - 0) && 
                 this.getDown() > platform.getDown()){
                if((platform.getDown() + h) < Main.HEIGHT){
                    y = platform.getDown();
                    vy = 0;
                }
            }            
            if (this.getRight() > platform.getLeft() && 
                this.getRight() < platform.getRight() && 
                this.getUp() < platform.getDown() && 
                this.getDown() > platform.getUp()){
                vx = 0;
                x = platform.getLeft() - w;
                
            }
            if (this.getLeft() < platform.getRight() && 
                this.getLeft() > platform.getLeft() && 
                this.getUp() < platform.getDown() && 
                this.getDown() > platform.getUp()){
                vx = 0;
                x = platform.getRight();            
            }
        }
    }//end collision  


    /**
     * Renders the enemy to the screen.
     *
     * @param context   The GraphicsContext used for drawing.
     * @param lvlOffset The horizontal offset for level scrolling.
     */
    @Override
    public void draw(GraphicsContext context, double lvlOffset) {
        context.drawImage(image, x - lvlOffset, y, WIDTH, HEIGHT);
    }//end draw    
}//end class
