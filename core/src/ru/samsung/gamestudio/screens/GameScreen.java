package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.Array;
import ru.samsung.gamestudio.SpiritGame;
import ru.samsung.gamestudio.utils.SettingsManager;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.audio.Music;

public class GameScreen implements Screen {
    final SpiritGame game;

    private static final float BASE_SPEED = 300f;
    private static final float SPEED_INCREMENT = 5f;
    private static final float MAX_SPEED = 800f;

    private float currentSpeed = BASE_SPEED;

    private static final float GROUND_Y = 30f;
    private static final float GRAVITY = -800f;
    private static final float JUMP_FORCE = 900f;

    private static final float JUMP_DURATION = 1.8f;

    private static final float DOG_INSET_X = 20f;
    private static final float DOG_INSET_Y = 15f;

    private static final float CACTUS_SCALE_X = 1f;
    private static final float CACTUS_SCALE_Y = 1f;
    private static final float CACTUS_INSET_X = 10f;
    private static final float CACTUS_INSET_Y = 15f;

    // Аудио
    private Sound jumpSound, hitSound;
    private Music backgroundMusic;
    private boolean soundEnabled, musicEnabled;

    // Отрисовка
    private SpriteBatch batch;
    private Texture background, cloudTexture, runSheet, deathSheet;
    private Texture[] jumpTextures;
    private BitmapFont scoreFont, font;

    // Анимации
    private Animation<TextureRegion> runAnimation, jumpAnimation;
    private TextureRegion[] deathFrames;

    // Собака
    private Rectangle dogRect;
    private float dogY = GROUND_Y;
    private float dogVelocity = 0;
    private boolean isJumping = false;
    private boolean isGameOver = false;
    private float jumpTime = 0f;
    private float stateTime = 0f;

    // Объекты
    private float[] bgX = new float[3];
    private final Array<Cactus> cactuses = new Array<>();
    private final Array<Cloud> clouds = new Array<>();

    // Таймеры
    private float cactusSpawnTimer = 0f, cactusSpawnInterval = 3.5f;
    private float cloudSpawnTimer = 0f, cloudSpawnInterval = 10f + MathUtils.random(5f);
    private float scoreTimer = 0f;
    private int score = 0;

    public GameScreen(final SpiritGame game) {
        this.game = game;
        this.batch = game.batch;

        soundEnabled = SettingsManager.getSound();
        musicEnabled = SettingsManager.getMusic();

        initAudio();
        loadTextures();
        initAnimations();
        initFonts();
        initBackground();
        initDog();
        initInput();
    }

    private void initAudio() {
        jumpSound = Gdx.audio.newSound(Gdx.files.internal("jump.wav"));
        hitSound = Gdx.audio.newSound(Gdx.files.internal("hit.wav"));
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.4f);
        if (musicEnabled) backgroundMusic.play();
    }

    private void loadTextures() {
        background = new Texture("gamePoleWhite.png");
        cloudTexture = new Texture("oblako.png");
        runSheet = new Texture("dog_run.png");
        deathSheet = new Texture("dog_dead.png");
    }

    private void initAnimations() {
        runAnimation = new Animation<>(0.1f, splitSheet(runSheet, 6));
        jumpAnimation = loadJumpAnimation();
        deathFrames = reverse(splitSheet(deathSheet, 7));
    }

    private void initFonts() {
        scoreFont = new BitmapFont();
        scoreFont.getData().setScale(2f);
        scoreFont.setColor(Color.BLACK);

        font = new BitmapFont();
        font.getData().setScale(3f);
        font.setColor(Color.RED);
    }

    private void initBackground() {
        for (int i = 0; i < bgX.length; i++) {
            bgX[i] = i * background.getWidth();
        }
    }

    private void initDog() {
        float dogWidth = runSheet.getWidth() / 6f;
        float dogHeight = runSheet.getHeight();
        dogRect = new Rectangle(150 + DOG_INSET_X, dogY + DOG_INSET_Y,
                dogWidth - 2 * DOG_INSET_X, dogHeight - DOG_INSET_Y);
    }

    private void initInput() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override public boolean touchDown(int x, int y, int pointer, int button) {
                jump(); return true;
            }

            @Override public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.SPACE) { jump(); return true; }
                return false;
            }
        });
    }

    private Animation<TextureRegion> loadJumpAnimation() {
        int frameCount = 5;
        jumpTextures = new Texture[frameCount];
        TextureRegion[] frames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            jumpTextures[i] = new Texture("dog_jump" + (i + 1) + ".png");
            frames[i] = new TextureRegion(jumpTextures[i]);
        }
        return new Animation<>(0.1f, frames);
    }

    private TextureRegion[] splitSheet(Texture sheet, int count) {
        int w = sheet.getWidth() / count;
        int h = sheet.getHeight();
        TextureRegion[] frames = new TextureRegion[count];
        for (int i = 0; i < count; i++) frames[i] = new TextureRegion(sheet, i * w, 0, w, h);
        return frames;
    }

    private TextureRegion[] reverse(TextureRegion[] arr) {
        TextureRegion[] rev = new TextureRegion[arr.length];
        for (int i = 0; i < arr.length; i++) rev[i] = arr[arr.length - 1 - i];
        return rev;
    }

    private void jump() {
        if (!isJumping) {
            dogVelocity = JUMP_FORCE;
            isJumping = true;
            jumpTime = 0f;
            if (soundEnabled) jumpSound.play(0.7f);
        }
    }

    private void spawnCactus() {
        Texture cactusTex = new Texture("cact" + MathUtils.random(1, 3) + ".png");
        float drawW = cactusTex.getWidth() * CACTUS_SCALE_X;
        float drawH = cactusTex.getHeight() * CACTUS_SCALE_Y;
        float drawX = Gdx.graphics.getWidth();
        float drawY = GROUND_Y;

        Rectangle hitbox = new Rectangle(drawX + CACTUS_INSET_X, drawY + CACTUS_INSET_Y,
                drawW - 2 * CACTUS_INSET_X, drawH - CACTUS_INSET_Y);

        cactuses.add(new Cactus(hitbox, cactusTex, drawX, drawY, drawW, drawH));
        cactusSpawnInterval = 3.5f + MathUtils.random(1.5f);
    }

    private void spawnCloud() {
        clouds.add(new Cloud(Gdx.graphics.getWidth(), MathUtils.random(600, 750), MathUtils.random(30, 80)));
        cloudSpawnInterval = 10f + MathUtils.random(5f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.96f, 0.96f, 0.96f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateGameState(delta);
        renderGame();
    }

    private void updateGameState(float delta) {
        if (isGameOver) return;

        stateTime += delta;
        scoreTimer += delta;

        if (scoreTimer >= 1f) {
            score++;
            scoreTimer = 0f;
            currentSpeed = Math.min(currentSpeed + SPEED_INCREMENT, MAX_SPEED);
        }

        // Движение собаки
        dogVelocity += GRAVITY * delta;
        dogY += dogVelocity * delta;

        if (dogY <= GROUND_Y) {
            dogY = GROUND_Y;
            dogVelocity = 0;
            isJumping = false;
        } else jumpTime += delta;

        dogRect.y = dogY + DOG_INSET_Y;

        // Фон
        for (int i = 0; i < bgX.length; i++) {
            bgX[i] -= currentSpeed * delta;
            if (bgX[i] + background.getWidth() <= 0) {
                float maxX = Math.max(Math.max(bgX[0], bgX[1]), bgX[2]);
                bgX[i] = maxX + background.getWidth() - 2;
            }
        }

        // Кактусы
        cactusSpawnTimer += delta;
        if (cactusSpawnTimer >= cactusSpawnInterval) {
            spawnCactus(); cactusSpawnTimer = 0;
        }

        for (int i = 0; i < cactuses.size; i++) {
            Cactus c = cactuses.get(i);
            c.rect.x -= currentSpeed * delta;
            c.drawX -= currentSpeed * delta;

            if (dogRect.overlaps(c.rect)) {
                if (soundEnabled) hitSound.play();
                backgroundMusic.stop();
                game.setScreen(new GameOverScreen(game, score));
                isGameOver = true;
                return;
            }

            if (c.rect.x + c.rect.width < 0) {
                cactuses.removeIndex(i); i--;
            }
        }

        // Облака
        cloudSpawnTimer += delta;
        if (cloudSpawnTimer >= cloudSpawnInterval) {
            spawnCloud(); cloudSpawnTimer = 0;
        }

        for (int i = 0; i < clouds.size; i++) {
            Cloud cloud = clouds.get(i);
            cloud.x -= cloud.speed * delta;
            if (cloud.x + cloudTexture.getWidth() < 0) {
                clouds.removeIndex(i); i--;
            }
        }
    }

    private void renderGame() {
        batch.begin();

        // Отрисовка
        for (float x : bgX) batch.draw(background, x, 0);
        for (Cloud cloud : clouds) batch.draw(cloudTexture, cloud.x, cloud.y);
        for (Cactus cactus : cactuses)
            batch.draw(cactus.texture, cactus.drawX, cactus.drawY, cactus.drawWidth, cactus.drawHeight);

        // Собака
        TextureRegion currentFrame;
        if (isGameOver) {
            int idx = Math.min((int)(stateTime / 0.25f), deathFrames.length - 1);
            currentFrame = deathFrames[idx];
        } else if (isJumping) {
            float phase = MathUtils.clamp(jumpTime / JUMP_DURATION, 0f, 0.999f);
            int idx = (int)(phase * jumpAnimation.getKeyFrames().length);
            currentFrame = jumpAnimation.getKeyFrames()[idx];
        } else {
            currentFrame = runAnimation.getKeyFrame(stateTime, true);
        }

        batch.draw(currentFrame,
                dogRect.x - DOG_INSET_X, dogRect.y - DOG_INSET_Y,
                dogRect.width + 2 * DOG_INSET_X, dogRect.height + DOG_INSET_Y);

        // Очки
        scoreFont.draw(batch, "Score: " + score, 20, Gdx.graphics.getHeight() - 20);
        batch.end();
    }

    // Inner classes
    private static class Cactus {
        Rectangle rect;
        Texture texture;
        float drawX, drawY, drawWidth, drawHeight;

        Cactus(Rectangle rect, Texture texture, float drawX, float drawY, float drawWidth, float drawHeight) {
            this.rect = rect; this.texture = texture;
            this.drawX = drawX; this.drawY = drawY;
            this.drawWidth = drawWidth; this.drawHeight = drawHeight;
        }
    }

    private static class Cloud {
        float x, y, speed;
        Cloud(float x, float y, float speed) {
            this.x = x; this.y = y; this.speed = speed;
        }
    }

    @Override public void dispose() {
        scoreFont.dispose();
        font.dispose();
        jumpSound.dispose();
        hitSound.dispose();
        backgroundMusic.dispose();
        background.dispose();
        cloudTexture.dispose();
        runSheet.dispose();
        deathSheet.dispose();
        for (Texture t : jumpTextures) t.dispose();
        for (Cactus c : cactuses) c.texture.dispose();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
}
