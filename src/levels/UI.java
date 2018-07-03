package levels;

import javax.swing.*;
import java.awt.*;

public class UI {
    public static int displayOptions(String text) {
        JFrame frame = new JFrame();
        int input = JOptionPane.showOptionDialog(frame, text, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        return input;
    }
}
