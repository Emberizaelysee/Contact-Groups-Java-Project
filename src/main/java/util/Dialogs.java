package util;

import java.awt.GraphicsEnvironment;
import javax.swing.JOptionPane;

/**
 * Thin wrapper around JOptionPane so that non-UI code (controllers, FileManager) can be
 * unit-tested without popping real modal dialogs. Disabled by default in headless
 * environments (e.g. CI without a display); tests can also disable it explicitly via
 * {@link #setEnabled(boolean)} regardless of the environment's headless state.
 */
public final class Dialogs {

    private static volatile boolean enabled = !GraphicsEnvironment.isHeadless();

    private Dialogs() {
    }

    public static void setEnabled(boolean value) {
        enabled = value;
    }

    public static void error(String message, String title) {
        if (enabled) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void info(String message, String title) {
        if (enabled) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
