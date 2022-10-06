import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Explosion extends BaseActor {
    Sound explosionSF;
    Sound satelliteExplosionSF;

    public Explosion(float x, float y, Stage s) {
        super(x, y, s);
        loadAnimationFromSheet("assets/explosion.png", 8, 10,0.01f, true);

        explosionSF = Gdx.audio.newSound(Gdx.files.internal("assets/musicAndSF/explosionSF.mp3"));
        satelliteExplosionSF = Gdx.audio.newSound(Gdx.files.internal("assets/musicAndSF/satelliteExplosionSF.mp3"));
    }

    public void act(float dt) {
        super.act(dt);

        if (isAnimationFinished()) {
            remove();
        }
    }

    public void playBOOM() {
        explosionSF.play();
    }

    public void stopBOOM() {
        explosionSF.stop();
    }

    public void playSatelliteBOOM() {
        satelliteExplosionSF.play();
    }

    public  void stopSatelliteBOOM() {
        satelliteExplosionSF.stop();
    }
}
