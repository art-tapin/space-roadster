import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class WarningMessage extends BaseActor {
    public WarningMessage(float x, float y, Stage s) {
        super(x, y, s);

        loadTexture("assets/warning.png");

        Action action1 = Actions.scaleTo(1.2f, 1.15f, 0.2f);
        Action action2 = Actions.scaleTo(0.8f, 0.8f, 0.2f);
        Action pulse = Actions.sequence(action1, action2);

        addAction( Actions.forever(pulse) );
    }
}
