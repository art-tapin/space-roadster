import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class MuskShield extends BaseActor
{
    public MuskShield(float x, float y, Stage s)
    {
        super(x,y,s);

        this.setOpacity(0.7f);

        Action action1 = Actions.scaleTo(1.05f, 1.05f, 1);
        Action action2 = Actions.scaleTo(0.95f, 0.95f, 1);
        Action pulse = Actions.sequence(action1, action2);

        addAction( Actions.forever(pulse) );
    }
}