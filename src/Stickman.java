import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;

/**
 * The Stickman class represents the player character in the game.
 * It handles the movement, jumping, and collision detection of the stickman,
 * as well as drawing the stickman on the screen using different images for animations.
 */
public class Stickman extends GameObject {

    private final double WIDTH = 36; // Width of the stickman
    private final double HEIGHT = 90; // Height of the stickman
    private final double MAX_VELOCITY = 300; // Maximum speed the stickman can move
    private boolean hasJumped = false; // Indicaties if the stickman has jumped
    private double time = 0; // Time variable for managing animation frame changes
    private double levelWidth; // Width of the game level (used for screen boundary checks)
    private int collisionStatus; // Tracks the type of collision with platforms (1 = left, 2 = right)

    /**
     * Constructs a new Stickman object with the specified level width.
     * The stickman is initialized with default position, velocity, and acceleration values.
     * A stickman image is also loaded.
     *
     * @param levelWidth The width of the game level, used for boundary checks.
     */
    public Stickman(Double levelWidth) {
        this.vx = 0; // Initial horizontal velocity
        this.vy = 0; // Initial vertical velocity
        this.x = 180; // Initial x-coordinate of the stickman
        this.y = 360; // Initial y-coordinate of the stickman
        this.ay = 1200; // Vertical acceleration due to gravity
        this.ax = 0; // No horizontal acceleration by default
        this.w = WIDTH; // Set width of the stickman
        this.h = HEIGHT; // Set height of the stickman
        this.image = new Image("stickman.png"); // Load the default image for the stickman
        this.levelWidth = levelWidth; // Set the level width for boundary checks
    }//end constructor

     /**
     * Updates the state of the stickman by applying movement and physics updates.
     * 
     * @param deltaTime The time elapsed since the last update, used for physics calculations.
     */
    public void update(double deltaTime) {
        super.update(deltaTime); // Call the parent update method
        movementPlayer(deltaTime); // Handle player movement
    }//end update

    /**
     * Draws the stickman on the screen at the current position.
     * 
     * @param context The GraphicsContext used to draw the stickman.
     * @param lvlOffset The level offset used for scrolling effects.
     */
    public void draw(GraphicsContext context, double lvlOffset) {
        context.drawImage(image, x - lvlOffset, y, WIDTH, HEIGHT); // Draw the stickman at the current position
    }//end draw

    /**
     * Handles the movement of the stickman, including horizontal movement and jumping.
     * The stickman can move left, right, or stop, and can jump when the UP key is pressed.
     * 
     * @param deltaTime The time elapsed since the last update, used to apply movement and speed changes.
     */
    public void movementPlayer(double deltaTime){

        boolean left = Input.isKeyPressed(KeyCode.LEFT); // Check if the LEFT key is pressed
        boolean right = Input.isKeyPressed(KeyCode.RIGHT); // Check if the RIGHT key is pressed

        // Handle left movement
        if (left && collisionStatus != 2) {
            time += deltaTime; // Increment the time for animation
            ax = -1000; // Apply leftward horizontal acceleration
            if (time > 0.0) {
                image = new Image("stickmanRunLeft1.png"); // Set animation frame 1
            }
            if (time >= 0.1) {
                image = new Image("stickmanRunLeft2.png"); // Set animation frame 2
            }
            if (time >= 0.2) {
                image = new Image("stickmanRunLeft3.png"); // Set animation frame 3
            }
            if (time >= 0.3) {
                image = new Image("stickmanRunLeft4.png"); // Set animation frame 4
            }
            if (time >= 0.4) {
                time = 0; // Reset time for animation loop
            }
        }

        // Handle right movement
        else if (right && collisionStatus != 1) {
            time += deltaTime; // Increment the time for animation
            ax = 1000; // Apply rightward horizontal acceleration
            
            if (time > 0.0) {
                image = new Image("stickmanRunRight1.png"); // Set animation frame 1
            }
            if (time >= 0.1) {
                image = new Image("stickmanRunRight2.png"); // Set animation frame 2
            }
            if (time >= 0.2) {
                image = new Image("stickmanRunRight3.png"); // Set animation frame 3
            }
            if (time >= 0.3) {
                image = new Image("stickmanRunRight4.png"); // Set animation frame 4
            }
            if (time >= 0.4) {
                time = 0; // Reset time for animation loop
            }
        }

        //slows the stickman down when no keys are pressed
        else {        
            ax = 0; //no horizontal acceleration
            int signVelocity;
            int newSignVelocity;
            if(vx > 0){
                signVelocity = 1;
            }
            else {
                signVelocity = -1;
            }
            double slowingSpeedX = -signVelocity * 500;
            vx += deltaTime * slowingSpeedX;
            if(vx > 0){
                newSignVelocity = 1;
            }
            else {
                newSignVelocity = -1;
            }

            if (newSignVelocity != signVelocity) {
                vx = 0; //stops when the direction changes
            }        
        }

        // If no horizontal movement, set to idle image
        if (vx == 0) {
            image = new Image("stickman.png");
        }

        // Cap the velocity to the maximum speed limit
        if (vx > MAX_VELOCITY) {
            vx = MAX_VELOCITY;
        } else if (vx < -MAX_VELOCITY) {
            vx = -MAX_VELOCITY;
        }
        
        //handle jumping
        boolean jump = Input.isKeyPressed(KeyCode.UP); // Check if the UP key is pressed
        if (jump && !hasJumped) { // If jump is pressed and the stickman is not already in the air
            vy = -750; // Set upward velocity for the jump
            hasJumped = true; // Mark the stickman as having jumped
        }

        // Prevent the stickman from going out of bounds vertically
        if (y + h > Main.HEIGHT) {
            y = Main.HEIGHT - h; // Stickman hits the bottom of the screen
            hasJumped = false; // Reset jump status
        } else if (y < 0) {
            y = 0; // Prevent stickman from moving above the screen
            vy = 0; // Stop upward velocity
        }

        // Prevent the stickman from going out of bounds horizontally
        if (x + w > levelWidth) {
            x = levelWidth - w; // Prevent the stickman from going beyond the level width
        } else if (x < 0) {
            x = 0; // Prevent the stickman from moving beyond the left boundary
        }
    }//end movementPlayer

    /**
     * Handles collision detection with a platform.
     * The stickman interacts with the platform, adjusting its position and velocity
     * when a collision is detected.
     * 
     * @param platform The platform object with which the stickman might collide.
     */
    public void collision(Platform platform){

        if((this.getDown() > platform.getUp() ) && (this.getRight() > platform.getLeft()) && (this.getLeft() < platform.getRight()) && (this.getUp() < platform.getDown())){ 
            if(hasJumped){
                if(this.getDown() > platform.getUp() && this.getRight() > (platform.getLeft() + 2) && (this.getLeft() < platform.getRight() - 2) && this.getUp() < platform.getUp()){
                    y = platform.getUp() - h;  
                    hasJumped = false;           
                }
                if(this.getUp() < platform.getDown() && (this.getRight() > platform.getLeft() + 2) && (this.getLeft() < platform.getRight() - 2) && this.getDown() > platform.getDown()){
                    if((platform.getDown() + h) < Main.HEIGHT){
                        y = platform.getDown();
                        vy = 0;
                    }
                }            
                if(this.getRight() > platform.getLeft() && this.getRight() < platform.getRight() && this.getUp() < platform.getDown() && this.getDown() > platform.getUp()){
                    vx = 0;
                    x = platform.getLeft() - w;
                    collisionStatus = 1;
                    
                }
                if(this.getLeft() < platform.getRight() && this.getLeft() > platform.getLeft() && this.getUp() < platform.getDown() && this.getDown() > platform.getUp()){
                    vx = 0;
                    x = platform.getRight();  
                    collisionStatus = 2;              
                } 
            }
            if(this.getDown() > platform.getUp() && this.getRight() > (platform.getLeft() + 0) && (this.getLeft() < platform.getRight() - 0) && this.getUp() < platform.getUp()){
                y = platform.getUp() - h;  
                hasJumped = false;           
            }
            if(this.getUp() < platform.getDown() && (this.getRight() > platform.getLeft() + 0) && (this.getLeft() < platform.getRight() - 0) && this.getDown() > platform.getDown()){
                if((platform.getDown() + h) < Main.HEIGHT){
                    y = platform.getDown();
                    vy = 0;
                }
            }            
            if(this.getRight() > platform.getLeft() && this.getRight() < platform.getRight() && this.getUp() < platform.getDown() && this.getDown() > platform.getUp()){
                vx = 0;
                x = platform.getLeft() - w;
                collisionStatus = 1;
                
            }
            if(this.getLeft() < platform.getRight() && this.getLeft() > platform.getLeft() && this.getUp() < platform.getDown() && this.getDown() > platform.getUp()){
                vx = 0;
                x = platform.getRight();  
                collisionStatus = 2;              
            }
        }
        collisionStatus = 0;
        
    }

    

    public double getUp() {
        return y;
    }

    public double getDown() {
        return y + h;
    }

    public double getLeft() {
        return x;
    }

    public double getRight() {
        return x + w;
    }
    
}
