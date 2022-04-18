package platform.gui;

import java.awt.*;

import javax.swing.*;


public class CenterableDialog extends JDialog {
    
	private static final long serialVersionUID = -3961535271116817487L;

	public CenterableDialog(JFrame parent, String title, boolean modal) {
        super(parent, title, modal);
        setResizable(false);
    }

    public void showCentered() {
        JFrame parent = (JFrame) getParent();
        Dimension dim = parent.getSize();
        Point loc = parent.getLocationOnScreen();

        Dimension size = getSize();

        loc.x += ((dim.width - size.width) / 2);
        loc.y += ((dim.height - size.height) / 2);

        if (loc.x < 0) {
            loc.x = 0;
        }

        if (loc.y < 0) {
            loc.y = 0;
        }

        Dimension screen = getToolkit().getScreenSize();

        if (size.width > screen.width) {
            size.width = screen.width;
        }

        if (size.height > screen.height) {
            size.height = screen.height;
        }

        if ((loc.x + size.width) > screen.width) {
            loc.x = screen.width - size.width;
        }

        if ((loc.y + size.height) > screen.height) {
            loc.y = screen.height - size.height;
        }

        setBounds(loc.x, loc.y, size.width, size.height);
        setVisible(true);
    }
}
