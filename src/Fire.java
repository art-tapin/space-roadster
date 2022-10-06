import com.badlogic.gdx.scenes.scene2d.Stage;

public class Fire extends BaseActor {
    public Fire(float x, float y, Stage s) {
        super(x,y,s);
        loadAnimationFromSheet("assets/fire.png", 6, 6, 0.06f, true);
    }
}
