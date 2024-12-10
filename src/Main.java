import java.io.FileNotFoundException;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * This is the main driver class of the JavaFX program. 
 * It manages two scenes: the home page and the game scene.
 * The game features an animated stickman escape scenario.
 */
public class Main extends Application {
    
    //Constants for window dimensions and tile size
    public static final double WIDTH = 1280, HEIGHT = 720, TILE_SIZE = 60; 
    
    private Stage stage;         //Primary stage of the application
    private double deltaTime;    //Time elapsed between frames for AnimationTimer
    Game game = new Game();      //Instance of the Game class   
    private double time;         //Timer for transitioning back to the home scene

    
     /**
     * The main entry point of the application.
     * 
     * @param args command-line arguments (not used)
     */
    public static void main(String args[]){
        launch(args);
    }//end Main

   /**
     * Initializes the primary stage of the application.
     * This method launches the JavaFX application.
     * 
     * @param primaryStage the main stage of the application
     * @throws FileNotFoundException if required resources (images) are not found
     */

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException{
        // Set the home scene initially
        this.stage = primaryStage;
        Scene scene = sceneIntro();  
        primaryStage.setScene(scene);
        
        // Window title
        primaryStage.setTitle("Final Project");  
        
        // Set application icon
        Image icon = new Image("stickman.png");  
        primaryStage.getIcons().add(icon);
        
        // Prevent window resizing
        primaryStage.setResizable(false);  
        
        primaryStage.show();
    }//end start

    /**
     * Creates and returns the home scene, which acts as the main menu.
     * 
     * @return the home scene
     * @throws FileNotFoundException if the background image is not found
     */
    private Scene sceneIntro() throws FileNotFoundException{

       // Main VBox layout for the home screen
       VBox root = new VBox();

       // Create the scene
       Scene scene = new Scene(root, WIDTH, HEIGHT, Color.BEIGE);

       // Background image for the home scene
       var img = new Image("imageMain.png");   
       BackgroundImage backgroundImage = new BackgroundImage(img, null, null, null, null);  

       // Title text for the home screen
       var title = new Text("STICKMAN ESCAPE");
       title.setFont(new Font(50));

       // Buttons layout
       var buttons = new VBox();
       buttons.setAlignment(Pos.BASELINE_CENTER);
       buttons.setSpacing(5);
       var btnPlay = new Button("Play!");
       buttons.getChildren().add(btnPlay);

       // Add elements to the root container
       root.getChildren().addAll(title, buttons);
       root.setAlignment(Pos.CENTER);
       root.setBackground(new Background(backgroundImage));


        // Action handler for the Play button to start the game
        btnPlay.setOnAction((e) -> {
            game = new Game();  // Reset game
            stage.setScene(sceneGame());  // Switch to the game scene
        });

        // Handle ESCAPE key press to close the game
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                Platform.exit();  // Exit the application
            }
        });
        return scene;
    }//end sceneIntro
    
    /**
     * Creates and returns the game scene where gameplay occurs.
     * 
     * @return the game scene
     */
    private Scene sceneGame() {
        //Scene
        var root = new Pane();
        var scene = new Scene(root, WIDTH, HEIGHT);        
        var canvas = new Canvas( game.getLevelWidth(), HEIGHT);
        
        var lives = new VBox();
        lives.setAlignment(Pos.TOP_CENTER);
        lives.setSpacing(14);
        lives.setPadding(new Insets(10));

        root.getChildren().addAll(canvas, lives);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));

        //animation timer and canvas
        var context = canvas.getGraphicsContext2D();
        var timer = new AnimationTimer() {
            long lastTime = System.nanoTime();
            
            @Override
            public void handle(long now) {
                
                deltaTime = (now - lastTime) * 1e-9;
                
                // Updates the game logic
                game.update(deltaTime);

                // Clears and redraws the canvas
                context.setFill(Color.LIGHTGREY);
                context.fillRect(0, 0, WIDTH, HEIGHT);
                game.draw(context);

                // Transitions back to the home scene if the game is finished
                if (game.isFinished()) {
                    time += deltaTime;
                    if (time >= 3) {
                        stop();
                        try {
                            stage.setScene(sceneIntro());
                            //Stops the handle
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        time = 0;
                    }
                }                
                lastTime = now;
            }
        };
        
        timer.start();

        // Handle key press events
        scene.setOnKeyPressed((e) -> {
            returnIntro(e);  // Check if ESCAPE is pressed to return to home scene
            // Stop timer if ESCAPE is pressed
            if (returnIntro(e)) {
                timer.stop();
            } else {
                Input.setKeyPressed(e.getCode(), true);  // Register key press for gameplay
            }
        });

        // Handles key release events
        scene.setOnKeyReleased(event -> {
            Input.setKeyPressed(event.getCode(), false); //Register key release
        });

        return scene;
    }//end sceneGame

    /**
     * Returns to the home scene when the ESCAPE key is pressed.
     * 
     * @param event the key event triggered by the user
     * @return true if the ESCAPE key is pressed; otherwise false
     */
    public boolean returnIntro(KeyEvent event) {
        boolean returnIntroNeeded;
        if (event.getCode() == KeyCode.ESCAPE) {
            try {
                stage.setScene(sceneIntro()); // Switch to home screen
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            returnIntroNeeded = true;
        } else {
            returnIntroNeeded = false;
        }
        return returnIntroNeeded;
    }//end retunrIntro
    
}//end class
