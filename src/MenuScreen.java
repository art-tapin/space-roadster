import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputEvent.Type;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class MenuScreen extends BaseScreen {
    private Music soundtrack;
    private BaseActor menuScreenSecret;
    public void initialize() {

        BaseActor menuScreen = new BaseActor(0,0, mainStage);
        menuScreen.loadTexture("assets/menuBackground.png");
        menuScreen.setSize(800,600);

        menuScreenSecret = new BaseActor(0, 0, mainStage);
        menuScreenSecret.loadAnimationFromSheet("assets/musk.png",7, 10, 0.05f,true);
        menuScreenSecret.setSize(800, 600);
        menuScreenSecret.setVisible(false);

        TextButton startButton = new TextButton("START", BaseGame.textButtonStyle);
        startButton.setPosition(85,175);
        uiStage.addActor(startButton);
        startButton.addListener(
                (Event e) -> {
                    if (!(e instanceof InputEvent) || !((InputEvent)e).getType().equals(Type.touchDown))
                        return false;
                    soundtrack.stop();
                    RoadsterSpaceGame.setActiveScreen(new LevelScreen());
                    return false;
                }
        );

        addSelectDifficultyBox();

        soundtrack = Gdx.audio.newMusic(Gdx.files.internal("assets/musicAndSF/space_odyssey.mp3"));
        soundtrack.setVolume(1f);
        soundtrack.setLooping(true);
        soundtrack.play();
    }

    private void addSelectDifficultyBox() {
        Skin skin = new Skin(Gdx.files.internal("assets/skin/uiskin.json"));

        SelectBox<String> selectBox = new SelectBox<>(skin);

        String[] values = new String[]{"Low (15)", "Medium (23)", "Hard (40)", "Insane (69)"};
        selectBox.setItems(values);

        selectBox.setSize(120, 20);
        selectBox.setPosition(550, 50);
        uiStage.addActor(selectBox);

        Label areYouSure = new Label("ARE YOU SHURE?", skin);
        areYouSure.setPosition(100, 250);
        areYouSure.setVisible(false);
        uiStage.addActor(areYouSure);

        Label dontTryIt = new Label("Anakin,\n DONT'T TRY IT!", skin);
        dontTryIt.setPosition(410, 60);
        dontTryIt.setVisible(false);
        uiStage.addActor(dontTryIt);

        selectBox.addListener(new ChangeListener() {
            /**
             * @param actor The event target, which is the actor that emitted the change event.
             */
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                areYouSure.setVisible(false);
                dontTryIt.setVisible(false);
                menuScreenSecret.setVisible(false);

                switch (selectBox.getSelected()) {
                    case "Medium (23)":
                        LevelScreen.ASTEROIDS_TO_DESTROY = 23;
                        LevelScreen.SATELLITES_TO_DESTROY = 2;
                        break;
                    case "Hard (40)":
                        LevelScreen.ASTEROIDS_TO_DESTROY = 40;
                        LevelScreen.SATELLITES_TO_DESTROY = 4;
                        break;
                    case "Insane (69)":
                        LevelScreen.ASTEROIDS_TO_DESTROY = 69;
                        LevelScreen.SATELLITES_TO_DESTROY = 8;
                        menuScreenSecret.setVisible(true);
                        areYouSure.setVisible(true);
                        dontTryIt.setVisible(true);
                        break;
                    default:
                        LevelScreen.ASTEROIDS_TO_DESTROY = 15;
                        LevelScreen.SATELLITES_TO_DESTROY = 1;
                        break;
                }
            }
        });
    }

    public void update(float dt) {
        if (Gdx.input.isKeyPressed(Keys.ENTER)) {
            soundtrack.stop();
            RoadsterSpaceGame.setActiveScreen(new LevelScreen());
        }

        if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
            Gdx.app.exit();
        }
    }
}
