package levels;

import javax.swing.*;

public class UI {
    private JFrame frame = new JFrame();

    public int displayOptions(String text) {
        int input = JOptionPane.showOptionDialog(frame, text, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, null);
        return input;
    }
}
