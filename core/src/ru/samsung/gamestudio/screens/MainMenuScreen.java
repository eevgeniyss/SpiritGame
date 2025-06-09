package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import ru.samsung.gamestudio.SpiritGame;

public class MainMenuScreen implements Screen {

    final SpiritGame game;
    SpriteBatch batch;

    Texture background;
    Texture startButtonTexture;
    Texture settingsButtonTexture;
    Texture exitButtonTexture;

    Rectangle startButtonRect;
    Rectangle settingsButtonRect;
    Rectangle exitButtonRect;

    float scale = 1.0f;

    public MainMenuScreen(final SpiritGame game) {
        this.game = game;
        this.batch = game.batch;

        background = new Texture("background.png");
        startButtonTexture = new Texture("start.png");
        settingsButtonTexture = new Texture("settings.png");
        exitButtonTexture = new Texture("exit.png");

        int buttonWidth = (int)(startButtonTexture.getWidth() * scale);
        int buttonHeight = (int)(startButtonTexture.getHeight() * scale);
        int spacing = 40;

        int totalWidth = buttonWidth * 3 + spacing * 2;
        int startX = (Gdx.graphics.getWidth() - totalWidth) / 2;
        int y = (Gdx.graphics.getHeight() - buttonHeight) / 2;

        startButtonRect = new Rectangle(startX, y, buttonWidth, buttonHeight);
        settingsButtonRect = new Rectangle(startX + buttonWidth + spacing, y, buttonWidth, buttonHeight);
        exitButtonRect = new Rectangle(startX + 2 * (buttonWidth + spacing), y, buttonWidth, buttonHeight);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                int flippedY = Gdx.graphics.getHeight() - screenY;

                if (startButtonRect.contains(screenX, flippedY)) {
                    onStartPressed();
                    return true;
                } else if (settingsButtonRect.contains(screenX, flippedY)) {
                    onSettingsPressed();
                    return true;
                } else if (exitButtonRect.contains(screenX, flippedY)) {
                    onExitPressed();
                    return true;
                }
                return false;
            }
        });
    }

    private void onStartPressed() {
        System.out.println("Старт нажат");
        game.setScreen(new GameScreen(game));
    }

    private void onSettingsPressed() {
        System.out.println("Настройки нажаты");
        game.setScreen(new SettingsScreen(game));
    }

    private void onExitPressed() {
        System.out.println("Выход нажат");
        Gdx.app.exit();
    }

    @Override public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        batch.draw(startButtonTexture, startButtonRect.x, startButtonRect.y, startButtonRect.width, startButtonRect.height);
        batch.draw(settingsButtonTexture, settingsButtonRect.x, settingsButtonRect.y, settingsButtonRect.width, settingsButtonRect.height);
        batch.draw(exitButtonTexture, exitButtonRect.x, exitButtonRect.y, exitButtonRect.width, exitButtonRect.height);

        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        startButtonTexture.dispose();
        settingsButtonTexture.dispose();
        exitButtonTexture.dispose();
    }
}
