import javafx.scene.input.KeyCode;
import java.util.HashMap;

/**
 * A utility class for managing keyboard input in a game.
 * 
 * The `Input` class tracks the state of keyboard keys, allowing other parts of
 * the game to inquire whether specific keys are currently pressed or released.
 * It uses a HashMap to store the state of each key, where the key is 
 * a KeyCode and the value is a boolean indicating whether the key 
 * is pressed.
 */
public class Input {
    
    // A map to store the current pressed state of keys, where KeyCode is the key
    // and a boolean represents whether the key is pressed (true) or released (false).
    private static HashMap<KeyCode, Boolean> keys = new HashMap<>(); 
    
    /**
     * Checks if a specific key is currently pressed.
     * 
     * @param code the KeyCode of the key to check
     * @return true if the key is pressed, false otherwise
     */
    public static boolean isKeyPressed(KeyCode code) {
        // Returns the pressed state of the key or false if the key isn't in the map
        return keys.getOrDefault(code, false);
    }//end isKeyPressed

    /**
     * Updates the pressed state of a specific key.
     * 
     * This method should be called whenever a key event is detected to
     * update the key's current state in the map.
     * 
     * @param code      the KeyCode of the key to update
     * @param isPressed true if the key is pressed, false if released
     */
    public static void setKeyPressed(KeyCode code, boolean isPressed) {
        keys.put(code, isPressed);
    }//end setKeyPressed
}//end class