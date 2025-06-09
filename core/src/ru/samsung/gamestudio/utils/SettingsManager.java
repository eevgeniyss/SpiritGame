package ru.samsung.gamestudio.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import ru.samsung.gamestudio.utils.SettingsManager;


public class SettingsManager {

    private static final String PREFS_NAME = "game_settings";
    private static final Preferences prefs = Gdx.app.getPreferences(PREFS_NAME);

    public static void saveSettings(boolean sound, boolean music, int difficulty, int language) {
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putInteger("difficulty", difficulty);
        prefs.putInteger("language", language);
        prefs.flush();
    }

    public static boolean getSound() {
        return prefs.getBoolean("sound", true);
    }

    public static void setSound(boolean value) {
        prefs.putBoolean("sound", value);
        prefs.flush();
    }

    public static boolean getMusic() {
        return prefs.getBoolean("music", true);
    }

    public static void setMusic(boolean value) {
        prefs.putBoolean("music", value);
        prefs.flush();
    }

    public static int getDifficulty() {
        return prefs.getInteger("difficulty", 0);
    }

    public static void setDifficulty(int value) {
        prefs.putInteger("difficulty", value);
        prefs.flush();
    }

    public static int getLanguage() {
        return prefs.getInteger("language", 0);
    }

    public static void setLanguage(int value) {
        prefs.putInteger("language", value);
        prefs.flush();
    }
}
