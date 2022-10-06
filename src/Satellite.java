import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Satellite extends BaseActor{
    private int health;

    public Satellite(float x, float y, Stage s) {
        super(x, y, s);

        loadTexture("assets/satellite.png");

        health = 3;

        float random = MathUtils.random(30);

        addAction(Actions.forever(Actions.rotateBy(30 + random, 4)));

        setSpeed(70 + random);
        setMaxSpeed(70 + random);
        setDeceleration(0);

        setMotionAngle(MathUtils.random(360));
    }

    public void takeHP() {
        health--;
    }

    public int getHP() {
        return health;
    }

    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);
        wrapAroundWorld();
    }
}
