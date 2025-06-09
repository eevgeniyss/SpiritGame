package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.*;
import ru.samsung.gamestudio.SpiritGame;

public class GameOverScreen implements Screen {
    private final SpiritGame game;
    private final SpriteBatch batch;
    private final OrthographicCamera camera;

    private Texture endMenuTexture;
    private BitmapFont font;

    private int finalScore;

    public GameOverScreen(final SpiritGame game, int finalScore) {
        this.game = game;
        this.finalScore = finalScore;
        this.batch = game.batch;
        this.camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        endMenuTexture = new Texture("endmenu.png");

        font = new BitmapFont();
        font.getData().setScale(3f);
        font.setColor(Color.WHITE);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector3 touch = new Vector3(screenX, screenY, 0);
                camera.unproject(touch);

                if (touch.x < Gdx.graphics.getWidth() / 2f) {
                    game.setScreen(new GameScreen(game)); // Левая часть — рестарт
                } else {
                    game.setScreen(new MainMenuScreen(game)); // Правая часть — меню
                }

                return true;
            }

            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.R) {
                    game.setScreen(new GameScreen(game));
                } else if (keycode == Input.Keys.ESCAPE || keycode == Input.Keys.M) {
                    game.setScreen(new MainMenuScreen(game));
                }
                return true;
            }
        });
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Растянуть картинку на весь экран
        batch.draw(endMenuTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Вывод счёта
        font.draw(batch, "SCORE: " + finalScore, 50, Gdx.graphics.getHeight() - 50);

        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        endMenuTexture.dispose();
        font.dispose();
    }
}
