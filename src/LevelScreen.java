import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class LevelScreen extends BaseScreen {

    private Roadster roadster;

    private boolean gameOver;

    public static int ASTEROIDS_TO_DESTROY = 15;
    public static int SATELLITES_TO_DESTROY = 1;

    private int amount;
    private float rockTimer;
    private float rockSpawnInterval;
    private boolean asteroidSpawnFlag;

    private Label asteroidLabel;
    private int asteroidScore;

    private boolean satelliteSpawnFlag;
    private float satelliteTimer;
    private float satelliteSpawnInterval;
    private int satelliteScore;
    private int satelliteSpawned;
    private Label satelliteLabel;

    private Ufo ufo;
    private float ufoTimer;
    int ufoSpawnInterval;
    private boolean ufoSpawnFlag;
    private int ufoAndWarningMessageYCoord;
    private WarningMessage warningMessage;

    private Music ambientmusic;
    private float audioVolume;
    private Music elonMuskSpeech;
    private Music NOICE;

    BaseActor earth;
    BaseActor muskWinningFace;
    BaseActor muskFailing;

    public void initialize() {
        BaseActor space = new BaseActor(0, 0, mainStage);
        space.loadAnimationFromSheet("assets/space.png", 1, 6, 0.8f, true);
        space.setSize(800, 600);
        BaseActor.setWorldBounds(space);

        muskWinningFace = new BaseActor(100, 80, mainStage);
        muskWinningFace.loadTexture("assets/muskWinningFace.png");
        muskWinningFace.setVisible(false);

        muskFailing = new BaseActor(100, 80, mainStage);
        muskFailing.loadTexture("assets/muskFailing.png");
        muskFailing.setVisible(false);

        // Implementation of a "live background" --> rotation earth object
        earth = new BaseActor(100, 80, mainStage);
        earth.loadAnimationFromSheet("assets/earth.png", 6, 8, 2.2f, true);

        ButtonStyle buttonStyle = new ButtonStyle();
        ButtonStyle buttonStyle2 = new ButtonStyle();

        Texture buttonTex = new Texture(Gdx.files.internal("assets/restart.png"));
        TextureRegion buttonRegion = new TextureRegion(buttonTex);
        buttonStyle.up = new TextureRegionDrawable(buttonRegion);
        Button restartButton = new Button(buttonStyle);
        restartButton.setPosition(760, 560);
        uiStage.addActor(restartButton);

        Texture buttonTex2 = new Texture(Gdx.files.internal("assets/speaker.png"));
        TextureRegion buttonRegion2 = new TextureRegion(buttonTex2);
        buttonStyle2.up = new TextureRegionDrawable(buttonRegion2);
        Button muteButton = new Button(buttonStyle2);
        muteButton.setPosition(720, 560);
        uiStage.addActor(muteButton);

        restartButton.addListener(
                (Event e) -> {
                    if (!isTouchDownEvent(e))
                        return false;

                    ambientmusic.dispose();
                    elonMuskSpeech.stop();

                    ASTEROIDS_TO_DESTROY = 15;
                    SATELLITES_TO_DESTROY = 1;

                    RoadsterSpaceGame.setActiveScreen(new MenuScreen());
                    return false;
                }
        );

        muteButton.addListener(
                (Event e) -> {
                    if (!isTouchDownEvent(e))
                        return false;
                    audioVolume = 0.05f - audioVolume;
                    ambientmusic.setVolume(audioVolume);
                    return true;
                }
        );

        audioVolume = 0.05f;
        ambientmusic = Gdx.audio.newMusic(Gdx.files.internal("assets/musicAndSF/Sun_Araw_Deep_Cover.mp3"));
        ambientmusic.setLooping(true);
        ambientmusic.setVolume(audioVolume);
        ambientmusic.play();

        elonMuskSpeech = Gdx.audio.newMusic(Gdx.files.internal("assets/musicAndSF/ElonMuskIncredibleSpeach.mp3"));
        elonMuskSpeech.setVolume(0.6f);
        elonMuskSpeech.setLooping(true);
        elonMuskSpeech.stop();

        NOICE = Gdx.audio.newMusic(Gdx.files.internal("assets/musicAndSF/Nice.mp3"));
        NOICE.setVolume(1f);
        NOICE.setLooping(false);
        NOICE.stop();

        rockTimer = 0;
        rockSpawnInterval = 2;
        amount = 0;
        asteroidSpawnFlag = true;

        satelliteTimer = 0;
        satelliteSpawnInterval = 8;
        satelliteSpawnFlag = false;
        satelliteScore = 0;

        asteroidLabel = new Label(" x" + (ASTEROIDS_TO_DESTROY - asteroidScore), BaseGame.labelStyle);
        asteroidLabel.setPosition(45, 570);
        uiStage.addActor(asteroidLabel);
        BaseActor asteroidIcon = new BaseActor(0, 550, uiStage);
        asteroidIcon.loadTexture("assets/asteroid.png");
        asteroidIcon.setScale(0.5f);

        satelliteLabel = new Label(" x" + (SATELLITES_TO_DESTROY - satelliteScore), BaseGame.labelStyle);
        satelliteLabel.setPosition(210, 570);
        uiStage.addActor(satelliteLabel);
        BaseActor satelliteIcon = new BaseActor(110, 550, uiStage);
        satelliteIcon.loadTexture("assets/satellite.png");
        satelliteIcon.setScale(0.5f);

        ufoSpawnFlag = false;
        ufoSpawnInterval = 2;

        ufoAndWarningMessageYCoord = MathUtils.random(50, 400);

        roadster = new Roadster(400, 300, mainStage);

        new Asteroid(600, 500, mainStage);
        new Asteroid(600, 300, mainStage);
        new Asteroid(400, 100, mainStage);
        new Asteroid(200, 300, mainStage);

        gameOver = false;
    }

    @Override
    public void update(float dt) {
        // here the behaviour of the collisions, effects and ui elements will be implemented
        for (BaseActor asteroidActor : BaseActor.getList(mainStage, "Asteroid")) {
            if (asteroidActor.overlaps(roadster)) {
                if (roadster.shieldPower <= 0) {
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor(roadster);
                    boom.playBOOM();
                    roadster.remove();
                    roadster.setPosition(-1000, -1000);

                    turnOnLoseMessage();
                } else {
                    roadster.shieldPower -= 34;
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor(asteroidActor);
                    boom.playBOOM();
                    asteroidActor.remove();
                    asteroidScore++;
                }
            }

            if (ufo != null && asteroidActor.overlaps(ufo)) {
                Explosion boom = new Explosion(0, 0, mainStage);
                boom.centerAtActor(asteroidActor);
                boom.playBOOM();
                asteroidActor.remove();
                asteroidScore++;
            }


            // implementation of shooting mechanics
            for (BaseActor laserActor : BaseActor.getList(mainStage, "Laser")) {
                if (laserActor.overlaps(asteroidActor)) {
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor(asteroidActor);
                    boom.playBOOM();
                    laserActor.remove();
                    asteroidActor.remove();
                    asteroidScore++;
                }

                if(ufo != null && ufo.overlaps(laserActor)) {
                    laserActor.remove();
                }
            }
        }

        for (BaseActor satelliteActor : BaseActor.getList(mainStage, "Satellite")) {
            Satellite satellite = (Satellite) satelliteActor;

            if (satelliteActor.overlaps(roadster)) {
                if (roadster.shieldPower <= 0) {
                    Explosion boom = new Explosion(0, 0, mainStage);
                    boom.centerAtActor(roadster);
                    boom.playSatelliteBOOM();
                    roadster.remove();
                    roadster.setPosition(-1000, -1000);

                    turnOnLoseMessage();
                } else {
                    roadster.shieldPower -= 34;
                    satellite.takeHP();
                    if (satellite.getHP() == 0) {
                        Explosion boom = new Explosion(0, 0, mainStage);
                        boom.centerAtActor(satelliteActor);
                        boom.playSatelliteBOOM();
                        satelliteActor.remove();
                        satelliteScore++;
                    }
                }
            }

            // implementation of shooting mechanics
            for (BaseActor laserActor : BaseActor.getList(mainStage, "Laser")) {
                if (laserActor.overlaps(satelliteActor)) {
                    satellite.takeHP();
                    laserActor.remove();
                    if (satellite.getHP() == 0) {
                        Explosion boom = new Explosion(0, 0, mainStage);
                        boom.centerAtActor(satelliteActor);
                        boom.playSatelliteBOOM();
                        satelliteActor.remove();
                        satelliteScore++;
                    }
                }
            }
            setColorOf(satellite);
        }

        if (ufo != null && ufo.overlaps(roadster)) {
            Explosion boom = new Explosion(0, 0, mainStage);
            boom.centerAtActor(roadster);
            boom.playBOOM();
            roadster.remove();
            roadster.setPosition(-1000, -1000);

            turnOnLoseMessage();
        }

        rockTimer += dt;
        if (rockTimer > rockSpawnInterval) {
            spawnRock(asteroidSpawnFlag);
            rockSpawnInterval *= 1.2f;
            rockTimer = 0;
        }

        if (asteroidScore > ASTEROIDS_TO_DESTROY) {
            asteroidLabel.setText(" x0");
        } else {
            asteroidLabel.setText(" x" + (ASTEROIDS_TO_DESTROY - asteroidScore));
        }
        if (satelliteScore > SATELLITES_TO_DESTROY) {
            satelliteLabel.setText(" x0");
        } else {
            satelliteLabel.setText(" x" + (SATELLITES_TO_DESTROY - satelliteScore));
        }

        if (asteroidScore == ASTEROIDS_TO_DESTROY / 3) {
            ufoSpawnFlag = true;
            satelliteSpawnFlag = true;
        }

        if (ufoSpawnFlag) {
            ufoTimer += dt;
            if (ufoTimer > ufoSpawnInterval && ufo == null) {
                spawnUfo();
            }
            if (warningMessage == null) {
                showWorningMessage();
            }
        }

        if (ufo != null && ufo.getX() > 790) { // the right side of the screen
            ufo.setVisible(false);
            ufo.setPosition(-10000, -10000);
            ufo.remove();
        }


        satelliteTimer += dt;
        if (satelliteTimer > satelliteSpawnInterval && satelliteScore != SATELLITES_TO_DESTROY) {
            spawnSatellite(satelliteSpawnFlag);
            satelliteTimer = 0;
        }

        if (satelliteSpawned == SATELLITES_TO_DESTROY) {
            satelliteSpawnFlag = false;
        }

        // clear gameBoard
        if (!gameOver && asteroidScore >= ASTEROIDS_TO_DESTROY && satelliteScore >= SATELLITES_TO_DESTROY) {
            asteroidSpawnFlag = false;
            for (BaseActor asteroidActor : BaseActor.getList(mainStage, "Asteroid")) {
                Explosion boom = new Explosion(0, 0, mainStage);
                boom.centerAtActor(asteroidActor);
                asteroidActor.remove();
            }
            for (BaseActor satelliteActor : BaseActor.getList(mainStage, "Satellite")) {
                Explosion boom = new Explosion(0, 0, mainStage);
                boom.centerAtActor(satelliteActor);
                satelliteActor.remove();
            }

            BaseActor messageWin = new BaseActor(0, 0, uiStage);
            messageWin.loadTexture("assets/win_message.png");
            messageWin.centerAtPosition(400, 400);
            messageWin.setOpacity(0);
            messageWin.addAction(Actions.fadeIn(1));

            stopSX();

            earth.addAction(Actions.fadeOut(3));

            muskWinningFace.setVisible(true);
            muskWinningFace.setOpacity(0);
            muskWinningFace.addAction(Actions.fadeIn(3));

            ambientmusic.stop();
            NOICE.play();

            gameOver = true;
        }
    }

    private void turnOnLoseMessage() {
        BaseActor messageLose = new BaseActor(0, 0, uiStage);
        messageLose.loadTexture("assets/lose_message.png");
        messageLose.centerAtPosition(400, 250);
        messageLose.setOpacity(0);
        messageLose.addAction(Actions.fadeIn(1));

        asteroidSpawnFlag = false;
        gameOver = true;

        earth.addAction(Actions.fadeOut(2));

        muskFailing.setVisible(true);
        muskFailing.setOpacity(0);
        muskFailing.addAction(Actions.fadeIn(4));

        ambientmusic.stop();
        elonMuskSpeech.play();
    }

    private void stopSX() {
        roadster.stopSF();
    }

    private void showWorningMessage() {
        warningMessage = new WarningMessage(50, ufoAndWarningMessageYCoord, uiStage);
    }

    private void spawnUfo() {
        ufo = new Ufo(-150, ufoAndWarningMessageYCoord, mainStage);
        ufo.playSound();
        ufoSpawnFlag = false;
        warningMessage.setVisible(false);
    }

    private void setColorOf(Satellite satellite) {
        switch (satellite.getHP()) {
            case 2: {
                satellite.setColor(Color.TAN);
                break;
            }
            case 1: {
                satellite.setColor(Color.SALMON);
                break;
            }
        }
    }

    private void spawnSatellite(boolean spawnFlag) {
        if (spawnFlag) {
            new Satellite(MathUtils.random(100, 500), -60, mainStage);
            satelliteSpawned++;
        }
    }

    void spawnRock(boolean flag) {
        if (flag) {
            for (int i = 0; i < amount; i++) {
                new Asteroid(800, MathUtils.random(100, 500), mainStage);
                new Asteroid(-65, MathUtils.random(100, 500), mainStage); // -65 == width of asteroid
            }
            amount++;
        }
    }

    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.SPACE) {
            roadster.shoot();
        }

        if (keycode == Input.Keys.X) {
            roadster.warp();
        }

        return false;
    }
}

