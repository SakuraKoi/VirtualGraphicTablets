package sakura.kooi.VirtualGraphicTablets.server.core.utils;

import lombok.CustomLog;

import java.awt.*;

@CustomLog
public class RobotUtils {
    private static Robot robot;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
          log.e("Failed initialize key simulation", e);
        }
    }

    public static void keyPress(int keyCode) {
        robot.keyPress(keyCode);
        robot.keyRelease(keyCode);
    }
}
