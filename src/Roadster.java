import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.lwjgl.audio.Mp3;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Roadster extends BaseActor {
    private Fire fire;

    private MuskShield shieldFull;
    private MuskShield shieldMedium;
    private MuskShield shieldLow;
    public int shieldPower;

    private Sound laserShootSound;
    private Sound thrustersSound;

    public Roadster(float x, float y, Stage s) {
        super(x, y, s);

        loadTexture("assets/roadster.png");
        setBoundaryPolygon(4);
        this.scaleBy(0.3f);

        setAcceleration(200);
        setMaxSpeed(100);
        setDeceleration(10);

        fire = new Fire(0, 0, s);
        addActor(fire);
        fire.setPosition(-fire.getWidth(), getHeight() / 2 - fire.getHeight() / 2);

        shieldFull = new MuskShield(0, 0, s);
        shieldFull.loadTexture("assets/maskFace1.png");

        shieldMedium = new MuskShield(0, 0, s);
        shieldMedium.loadTexture("assets/maskFace2.png");

        shieldLow = new MuskShield(0, 0, s);
        shieldLow.loadTexture("assets/maskFace3.png");

        addActor(shieldFull);
        shieldFull.centerAtPosition(getWidth() / 2, getHeight() / 2);
        shieldFull.setVisible(true);

        addActor(shieldMedium);
        shieldMedium.centerAtPosition(getWidth() / 2, getHeight() / 2);
        shieldMedium.setVisible(false);

        addActor(shieldLow);
        shieldLow.centerAtPosition(getWidth() / 2, getHeight() / 2);
        shieldLow.setVisible(false);

        shieldPower = 100;

        laserShootSound = Gdx.audio.newSound(Gdx.files.internal("assets/musicAndSF/laserSF.mp3"));

        thrustersSound = Gdx.audio.newSound(Gdx.files.internal("assets/musicAndSF/thrustersSF.mp3"));
        thrustersSound.play();
        thrustersSound.loop();
        thrustersSound.pause();
    }

    public void shoot() {
        if (getStage() == null)
            return ;

        if(BaseActor.getList(this.getStage(), "Laser").size()==3)
            return ;
        else
            laserShootSound.play();

        Laser laser = new Laser(0,0, this.getStage());
        laser.centerAtActor(this);
        laser.setRotation(this.getRotation());
        laser.setMotionAngle(this.getRotation());

    }

    public void warp() {
        if (getStage() == null)
            return;

        Warp warp1 = new Warp(0,0, this.getStage());
        warp1.centerAtActor(this);
        setPosition(MathUtils.random(800), MathUtils.random(600));
        Warp warp2 = new Warp(0,0, this.getStage());
        warp2.centerAtActor(this);
    }

    public void act(float dt) {
        super.act( dt );

        float degreesPerSecond = 120; //Rotation by degrees per second
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            rotateBy(degreesPerSecond * dt);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            rotateBy(-degreesPerSecond * dt);
        }

        //Acceleration of roadster
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
        {
            accelerateAtAngle(getRotation());
            fire.setVisible(true);
            thrustersSound.resume();
        }
        else {
            fire.setVisible(false);
            thrustersSound.pause();
        }

        applyPhysics(dt);

        wrapAroundWorld();

        switch (shieldPower) {
            case 66: {
                shieldFull.remove();
                shieldMedium.setVisible(true);
                break;
            }
            case 32: {
                shieldMedium.remove();
                shieldLow.setVisible(true);
                break;
            }
            case -2: {
                shieldLow.remove();
            }
        }
    }

    public void stopSF() {
        laserShootSound.stop();
        thrustersSound.stop();
    }
}
