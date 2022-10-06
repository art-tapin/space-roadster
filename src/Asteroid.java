import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.math.MathUtils;

public class Asteroid extends BaseActor {
    public Asteroid(float x, float y, Stage s) {
        super(x, y, s);

        loadTexture("assets/asteroid.png");

        float random = MathUtils.random(30);

        addAction(Actions.forever(Actions.rotateBy(30 + random, 1)));

        setSpeed(50 + random);
        setMaxSpeed(50 + random);
        setDeceleration(0);

        setMotionAngle(MathUtils.random(360));
    }

    public void act(float dt) {
        super.act(dt);
        applyPhysics(dt);
        wrapAroundWorld();
    }
}
