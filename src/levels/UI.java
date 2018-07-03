package levels;

import org.newdawn.slick.TrueTypeFont;

import javax.swing.*;
import java.awt.*;

public class UI {
    public static int displayOptions(String text) {
        int input = JOptionPane.showOptionDialog(null, text, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        return input;
    }
}
