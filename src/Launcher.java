
// Basic Game class. Its allows an application to easily have multiple screens (more than one).
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.Game;

// stands for  Lightweight Java Game Library  Lightweight Java Game Library
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

/**
 * The main game driver class.
 *
 * The behaviour of Launcher:
 * - creates an instance of the game
 * - creates a new application with game instance and window settings as argument
 */
public class Launcher {
    public static void main (String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.addIcon("assets/icon.png", FileType.Internal);
        config.width = 800;
        config.height = 600;
        config.title = "Space Roadster";
        Game spaceRoadster = new RoadsterSpaceGame();
        LwjglApplication launcher = new LwjglApplication(spaceRoadster,config);

    }
}
