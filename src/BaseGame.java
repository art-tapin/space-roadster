import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.graphics.Texture;

/**
 * The BaseGame instance is created when game is launched
 * It manages the screen(s) that appear during the game
 */
public abstract class BaseGame extends Game {

    /**
     *  Stores reference to game; used when calling 'setActiveScreen' method.
     */
    private static BaseGame game;

    /**
     * Stores text style of labels.
     */
    public static LabelStyle labelStyle;

    /**
     * Stores text style of buttons.
     */
    public static TextButtonStyle textButtonStyle;

    public BaseGame() {
        game = this; // a libGDX convention to work with screen's states
    }

    /**
     *  Called when game is initialized,
     *  after Gdx.input and other objects have been initialized.
     */
    @Override
    public void create() {
        textButtonStyle = new TextButtonStyle();
        Texture buttonTex = new Texture(Gdx.files.internal("assets/test_button.png"));
        NinePatch buttonPatch = new NinePatch(buttonTex, 24,24,24,24);
        textButtonStyle.up = new NinePatchDrawable(buttonPatch);
        textButtonStyle.font = new BitmapFont(Gdx.files.internal("assets/newfont.fnt"));

        labelStyle = new LabelStyle();
        labelStyle.font = new BitmapFont(Gdx.files.internal("assets/newfont.fnt"));

        // prepare for multiple classes/stages/actors
        // to receive users input
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        Gdx.input.setInputProcessor(inputMultiplexer);
    }

    /**
     *  Used to switch screens while game is running.
     *  Method is static to simplify usage.
     */
    public static void setActiveScreen(BaseScreen s) {
        game.setScreen(s);
    }
}
