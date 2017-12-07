public abstract class Settings {
    private static float SCREEN_WIDTH;
    private static float SCREEN_HEIGHT;
    private static boolean FULLSCREEN;
    private static float MUSIC_VOLUME;


    public static float getMusicVolume() {
        return MUSIC_VOLUME;
    }

    public static void setMusicVolume(float musicVolume) {
        MUSIC_VOLUME = musicVolume;
    }

    public static float getScreenWidth() {
        return SCREEN_WIDTH;
    }

    public static void setScreenWidth(int screenWidth) {
        SCREEN_WIDTH = screenWidth;
    }

    public static float getScreenHeight() {
        return SCREEN_HEIGHT;
    }

    public static void setScreenHeight(int screenHeight) {
        SCREEN_HEIGHT = screenHeight;
    }

    public static boolean isFULLSCREEN() {
        return FULLSCREEN;
    }

    public static void setFULLSCREEN(boolean FULLSCREEN) {
        Settings.FULLSCREEN = FULLSCREEN;
    }
}
