import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;


public class Ufo extends BaseActor {

    private Sound ufoSound;

    public Ufo(float x, float y, Stage s) {
        super(x, y, s);

        loadTexture("assets/ufo.png");

        setBoundaryPolygon(16);

        setSpeed(290);
        setMaxSpeed(290);
        setDeceleration(0);
        setMotionAngle(0);

        ufoSound = Gdx.audio.newSound(Gdx.files.internal("assets/musicAndSF/ufoSound.mp3"));
    }

    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);
        wrapAroundWorld();
    }

    public void playSound() {
        ufoSound.play();
    }
}
