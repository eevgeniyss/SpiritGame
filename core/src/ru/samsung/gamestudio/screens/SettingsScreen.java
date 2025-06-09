package ru.samsung.gamestudio.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import ru.samsung.gamestudio.SpiritGame;
import ru.samsung.gamestudio.utils.SettingsManager;

public class SettingsScreen implements Screen {
    final SpiritGame game;
    SpriteBatch batch;

    Texture background;
    Texture menuPanel;
    Texture checkboxOn;
    Texture checkboxOff;

    boolean soundEnabled;
    boolean musicEnabled;
    int difficulty;
    int language;

    Rectangle soundBoxRect, musicBoxRect;
    Rectangle difficultyEasyRect, difficultyMediumRect, difficultyHardRect;
    Rectangle langRuRect, langEnRect;
    Rectangle saveRect, cancelRect;

    float scale = 0.7f;
    int panelWidth, panelHeight, panelX, panelY;

    public SettingsScreen(final SpiritGame game) {
        this.game = game;
        this.batch = game.batch;

        background = new Texture("backsettingsMenu.png");
        menuPanel = new Texture("menuplashka.png");
        checkboxOn = new Texture("activity.png");
        checkboxOff = new Texture("standart.png");

        panelWidth = (int) (menuPanel.getWidth() * scale);
        panelHeight = (int) (menuPanel.getHeight() * scale);
        panelX = (Gdx.graphics.getWidth() - panelWidth) / 2;
        panelY = (Gdx.graphics.getHeight() - panelHeight) / 2;

        float cbSize = checkboxOn.getWidth() * scale;

        soundEnabled = SettingsManager.getSound();
        musicEnabled = SettingsManager.getMusic();
        difficulty = SettingsManager.getDifficulty();
        language = SettingsManager.getLanguage();

        soundBoxRect = new Rectangle(panelX + 40 * scale, panelY + panelHeight - 290 * scale, cbSize, cbSize);
        musicBoxRect = new Rectangle(panelX + 40 * scale, panelY + panelHeight - 390 * scale, cbSize, cbSize);

        difficultyEasyRect = new Rectangle(panelX + 40 * scale, panelY + panelHeight - 600 * scale, cbSize, cbSize);
        difficultyMediumRect = new Rectangle(panelX + 40 * scale, panelY + panelHeight - 700 * scale, cbSize, cbSize);
        difficultyHardRect = new Rectangle(panelX + 40 * scale, panelY + panelHeight - 800 * scale, cbSize, cbSize);

        langRuRect = new Rectangle(panelX + 40 * scale, panelY + panelHeight - 1000 * scale, cbSize, cbSize);
        langEnRect = new Rectangle(panelX + 40 * scale, panelY + panelHeight - 1100 * scale, cbSize, cbSize);

        saveRect = new Rectangle(panelX + 280 * scale, panelY + 50 * scale, 140 * scale, 50 * scale);
        cancelRect = new Rectangle(panelX + panelWidth - 420 * scale, panelY + 50 * scale, 140 * scale, 50 * scale);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                int flippedY = Gdx.graphics.getHeight() - screenY;

                if (soundBoxRect.contains(screenX, flippedY)) {
                    soundEnabled = !soundEnabled;
                    return true;
                }
                if (musicBoxRect.contains(screenX, flippedY)) {
                    musicEnabled = !musicEnabled;
                    return true;
                }
                if (difficultyEasyRect.contains(screenX, flippedY)) {
                    difficulty = 0;
                    return true;
                }
                if (difficultyMediumRect.contains(screenX, flippedY)) {
                    difficulty = 1;
                    return true;
                }
                if (difficultyHardRect.contains(screenX, flippedY)) {
                    difficulty = 2;
                    return true;
                }
                if (langRuRect.contains(screenX, flippedY)) {
                    language = 0;
                    return true;
                }
                if (langEnRect.contains(screenX, flippedY)) {
                    language = 1;
                    return true;
                }
                if (saveRect.contains(screenX, flippedY)) {
                    SettingsManager.saveSettings(soundEnabled, musicEnabled, difficulty, language);
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                if (cancelRect.contains(screenX, flippedY)) {
                    game.setScreen(new MainMenuScreen(game));
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(menuPanel, panelX, panelY, panelWidth, panelHeight);

        batch.draw(soundEnabled ? checkboxOn : checkboxOff, soundBoxRect.x, soundBoxRect.y, soundBoxRect.width, soundBoxRect.height);
        batch.draw(musicEnabled ? checkboxOn : checkboxOff, musicBoxRect.x, musicBoxRect.y, musicBoxRect.width, musicBoxRect.height);

        batch.draw(difficulty == 0 ? checkboxOn : checkboxOff, difficultyEasyRect.x, difficultyEasyRect.y, difficultyEasyRect.width, difficultyEasyRect.height);
        batch.draw(difficulty == 1 ? checkboxOn : checkboxOff, difficultyMediumRect.x, difficultyMediumRect.y, difficultyMediumRect.width, difficultyMediumRect.height);
        batch.draw(difficulty == 2 ? checkboxOn : checkboxOff, difficultyHardRect.x, difficultyHardRect.y, difficultyHardRect.width, difficultyHardRect.height);

        batch.draw(language == 0 ? checkboxOn : checkboxOff, langRuRect.x, langRuRect.y, langRuRect.width, langRuRect.height);
        batch.draw(language == 1 ? checkboxOn : checkboxOff, langEnRect.x, langEnRect.y, langEnRect.width, langEnRect.height);

        batch.end();
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        background.dispose();
        menuPanel.dispose();
        checkboxOn.dispose();
        checkboxOff.dispose();
    }
}
