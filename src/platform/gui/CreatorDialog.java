package platform.gui;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.awt.Dimension;

import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class CreatorDialog extends CenterableDialog {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -4472237493776493729L;
	protected String deviceName = "";

    public CreatorDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Credits", true);
        this.setSize(new Dimension(550, 600));
        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new AbsoluteLayout());
        panel.setSize(550, 600);

        try {
            ClassLoader loader = getClass().getClassLoader();
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/creatorDialog.jpg"));

            panel.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel instruction = new JLabel();
        instruction.setFont(new Font("Arial", Font.BOLD, 12));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setSize(400, 50);
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.setOpaque(false);
        panel.add(buttonPanel, new AbsoluteConstraints(0, 200, 400, 50));

        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    dispose();
                }
            });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }
}
