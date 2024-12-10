import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * The Game class is responsible for managing the core mechanics of the game,
 * including updating the game state, handling collisions, and rendering all
 * game elements to the screen.
 */
public class Game{
    private double levelWidth; // Width of the current level in pixels
    private double time; //time between considered collisions
    private double starTime; //Time elapsed since the last star refresh
    private boolean collision; // True if a collision that reduces life occurred
    private int numLives; // Number of lives remaining for the player
    private int score; //Player's score, increased by collecting stars
    private boolean finished; //True if the game is over
    
    
    private Platform[] listPlatforms; //Array of platforms
    private Lava[] listLava; //Array of lava object
    private Star[] listStar; //Array of Star object
    private Enemy[] listEnemies; //Array of Enemy objects 
    private Stickman stickman; //Stickman character controlled by the player
    private int enemyCount; // Number of enemies in the level
    private int starCount; // Number of stars in the level
    
    private double xLvlOffset; // Horizontal offset for camera scrolling
    private double leftBorder; // Left boundary for the camera
    private double rightBorder; // Right boundary for the camera
    private double maxLvlOffsetX; // Maximum horizontal offset for the level
    
    /**
     * Default constructor for the Game class.
     * Initializes all game elements, including the level layout, platforms,
     * enemies, stars, and lava.
     */
    public Game(){
        finished = false;
        levelWidth = Levels.LEVEL_1[0].length() * Main.TILE_SIZE;
        stickman = new Stickman(levelWidth);
        leftBorder = 480;
        rightBorder = Main.WIDTH - leftBorder;
        maxLvlOffsetX = levelWidth - Main.WIDTH;
        numLives = 5;
        listPlatforms = new Platform[Levels.LEVEL_1[0].length() * 12];
        listLava = new Lava[Levels.LEVEL_1[0].length() * 12];
        listEnemies = new Enemy[10];
        listStar = new Star[10];
        score = 0;
        initializeLevel(); 
    }//end empty argument constructor
    
    
    /**
     * Updates the game state. This method is called at regular intervals and
     * updates all game objects, handles collisions, and checks for game-over conditions.
     *
     * @param deltaTime The time elapsed since the last update, used for smooth animations.
     */
    public void update(double deltaTime) {
        time += deltaTime; //timer for collisions
        starTime += deltaTime; //timer for stars refresh
        stickman.update(deltaTime); //updates the position of the stickman

        //updates all the enemies
        for(int i = 0; i < listEnemies.length; i++){
            if(listEnemies[i] != null){
                listEnemies[i].update(deltaTime);                
            }
        }
        //check for collisions with platforms for stickman and enemies
        checkCollisions();

        //check for collisions with lava and enemies to lose a life
        checkCollisionLostLife(listEnemies);
        checkCollisionLostLife(listLava);
        

        //check for collision with stars to get points
        checkCollisionGetPoints();

        //sets up the camera effect that follows the player
        checkProximityBorder();
       
        /// Refresh stars every 15 seconds
        if (starTime >= 15){
            int starIndex;

            for (int i = 0; i < 3; i++) {
                //Creates a random int from 0 to the number of stars 3 times
                starIndex = (int) (Math.random() * (starCount - 1));
                
                if(listStar[starIndex] != null && listStar[starIndex].isActive() == false){
                    listStar[starIndex].setActive(true);
                }                
            }
            starTime = 0;            

        }

        // Reduce lives if a collision occurred
        if(collision && time > 2){
            numLives -= 1;
            time = 0;
        }
        else{
            collision = false;
        }

        //ends the game is there are no more lives
        if(numLives == 0){
            finished = true;
        }
    }//end update
    
     /**
     * Checks for collisions between the stickman and stars. If a collision occurs,
     * the star is deactivated, and the score is increased.
     */
    public void checkCollisionGetPoints(){
        for(int i = 0; i < listStar.length; i++){
            if(listStar[i] != null && listStar[i].isActive() == true){   
                //checks for a collision                 
                if((stickman.getDown() > listStar[i].getUp() ) && (stickman.getRight() > listStar[i].getLeft()) && (stickman.getLeft() < listStar[i].getRight()) && (stickman.getUp() < listStar[i].getDown())){
                    //removes the star
                    listStar[i].setActive(false);
                    
                    //adds 10 points
                    score += 10;                      
                }
            }
        }
    }//end checkCollisionGetPoints
    
    /**
     * Checks for collisions between the stickman and enemies or lava. If a collision
     * is detected, a life is lost.
     * 
     * implementation of polymorphism
     */
    public void checkCollisionLostLife(GameObject[] list){
        for(int i = 0; i < list.length; i++){
            if(list[i] != null){                    
                if((stickman.getDown() > list[i].getUp() ) && (stickman.getRight() > list[i].getLeft()) && (stickman.getLeft() < list[i].getRight()) && (stickman.getUp() < list[i].getDown())){
                    collision = true;
                    break;                      
                }
            }
        }
    }//end checkCollisionLostLife


    /**
     * Checks for collisions between platforms and game characters
     * (stickman and enemies).
     */
    public void checkCollisions(){
        for(int i = 0; i < listPlatforms.length; i++){
            if(listPlatforms[i] != null){
                stickman.collision(listPlatforms[i]);                    
            }
        }
        for(int i = 0; i < listEnemies.length; i++){
            for(int j = 0; j < listPlatforms.length; j++){
                if(listPlatforms[j] != null && listEnemies[i] != null){                   
                    listEnemies[i].collision(listPlatforms[j]); 
                }                
            }
        }
    }//end checkCollisions
    
     /**
     * Adjusts the camera offset to keep the stickman in view.
     */
    public void checkProximityBorder(){
        double stickmanX = stickman.getLeft();
        double diff = stickmanX - xLvlOffset;
        
        if(diff > rightBorder){
            xLvlOffset += diff - rightBorder;
        }
        else if (diff < leftBorder){
            xLvlOffset += diff - leftBorder;
        }
        
        if(xLvlOffset > maxLvlOffsetX){
            xLvlOffset = maxLvlOffsetX;
        }
        else if(xLvlOffset < 0){
            xLvlOffset = 0;
        }
    }//end checkProximityBorder
    

    
   /**
     * Draws all game elements to the screen, including platforms, stickman,
     * enemies, stars, and the UI elements (score, lives, and game-over text).
     *
     * @param context The JavaFX GraphicsContext for rendering.
     */
    public void draw(GraphicsContext context){
        //draw the stickman
        stickman.draw(context, xLvlOffset);

        //draw the platforms
        for(int i = 0; i < listPlatforms.length; i++){
            if(listPlatforms[i] != null){
                listPlatforms[i].draw(context, xLvlOffset);  
                              
            }
            if(listLava[i] != null){
                listLava[i].draw(context, xLvlOffset);
            }
        }

        //draw the enemies
        for(int i = 0; i < listEnemies.length; i++){
            if(listEnemies[i] != null){
                listEnemies[i].draw(context, xLvlOffset);                
            }
        }

        //draw the stars
        for(int i = 0; i < listStar.length; i++){
            if(listStar[i] != null && listStar[i].isActive() == true){
                listStar[i].draw(context, xLvlOffset);                
            }
        }

        //draw the end text
        if (finished) {
            String textEnd = "GAME OVER";
            context.setFill(Color.RED);
            context.setFont(new Font(60));
            context.fillText(textEnd, Main.WIDTH * 0.4, Main.HEIGHT / 2);
        }

        //draws the score
        context.setFill(Color.BLACK);
        context.setFont(new Font(30));
        context.fillText("Score: " + String.valueOf(score), Main.WIDTH * 0.1, Main.HEIGHT * 0.1);

        //draws the lives
        var heart = new Image("heart.png");
        for (int i = 0; i < numLives; i++) {
            context.drawImage(heart, Main.WIDTH * 0.8 + 50 * i, Main.HEIGHT * 0.1 - 40, 40, 40);
        }

        
    }//end draw
    
    /**
     * Initializes the level by parsing the level layout and creating platforms,
     * enemies, lava, and stars based on the layout data.
     */
    public void initializeLevel(){
        int count = 0;
        enemyCount = 0;
        starCount = 0;
        for(int i = 0; i < Levels.LEVEL_1.length; i++){
            String line = Levels.LEVEL_1[i];
            for(int j = 0; j < line.length(); j++){
                
                if(line.charAt(j) == '1'){
                    listPlatforms[count] = new Platform(j * Main.TILE_SIZE, i * Main.TILE_SIZE, Main.TILE_SIZE, Main.TILE_SIZE);
                }
                if(line.charAt(j) == '2'){
                    listEnemies[enemyCount] = new Enemy(levelWidth, j * Main.TILE_SIZE, i * Main.TILE_SIZE, stickman);
                    enemyCount++;
                }
                if(line.charAt(j) == '3'){
                    listLava[count] = new Lava(j * Main.TILE_SIZE, i * Main.TILE_SIZE, Main.TILE_SIZE, Main.TILE_SIZE);
                }
                if(line.charAt(j) == '4'){
                    listStar[starCount] = new Star(j * Main.TILE_SIZE + 15, i * Main.TILE_SIZE);
                    starCount++;
                }
                count += 1;
                
            }
        }
              
    }//end initializeLevel


    // Getters and Setters
    public double getLevelWidth() {
        return levelWidth;
    }//end getLevelWidth
    
    public void setLevelWidth(double levelWidth) {
        this.levelWidth = levelWidth;
    }//end setLevelWidth

    public boolean isFinished() {
        return finished;
    }//end isFinished
    
}//end class