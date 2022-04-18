package platform;

public class Splasher {
    public static void main(String[] args) {
        SplashWindow.splash(Splasher.class.getClassLoader().getResource("images/graphics/splash.jpg"));
        SplashWindow.invokeMain("platform.Main", args);
        SplashWindow.disposeSplash();
    }
}
