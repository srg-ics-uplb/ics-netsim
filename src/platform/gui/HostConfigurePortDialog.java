package platform.gui;

import devices.hosts.Host;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class HostConfigurePortDialog extends CenterableDialog implements ActionListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -7563775785366758382L;
	private Host host;
    private final JPanel panel;
    private final JCheckBox ftp = new JCheckBox();
    private final JCheckBox ssh = new JCheckBox();
    private final JCheckBox telnet = new JCheckBox();
    private final JCheckBox doom = new JCheckBox();
    private final JCheckBox dns = new JCheckBox();
    private final JCheckBox pop3 = new JCheckBox();
    private final JCheckBox[] portCheckBoxes = {
        ftp, ssh, telnet, doom, dns, pop3
    };
    private final JButton applyButton;
    private final JButton cancelButton;
    private final JLabel instruction;
    protected String deviceName = "";
    protected boolean defaultSelected = false;
    protected boolean emptyName = true;
    protected boolean isValidQuartet = false;

    public HostConfigurePortDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Configure Host Ports", true);

        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new AbsoluteLayout());
        panel.setSize(400, 300);

        try {
            ClassLoader loader = getClass().getClassLoader();
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/configDialog.jpg"));
            panel.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < portCheckBoxes.length; i++) {
            portCheckBoxes[i].setOpaque(false);
        }

        instruction = new JLabel();
        instruction.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(instruction, new AbsoluteConstraints(60, 20, 150, 20));

        Utilities.wrapLabelText(instruction, "Select the Active Ports");

        panel.add(ftp, new AbsoluteConstraints(30, 60, 75, 30));
        panel.add(ssh, new AbsoluteConstraints(105, 60, 75, 30));

        panel.add(telnet, new AbsoluteConstraints(30, 105, 75, 30));
        panel.add(doom, new AbsoluteConstraints(105, 105, 75, 30));

        panel.add(dns, new AbsoluteConstraints(30, 150, 75, 30));
        panel.add(pop3, new AbsoluteConstraints(105, 150, 75, 30));

        JPanel buttonPanel = new JPanel();
        applyButton = new JButton("Apply");
        buttonPanel.add(applyButton);
        cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        buttonPanel.setOpaque(false);
        panel.add(buttonPanel, new AbsoluteConstraints(0, 190, 200, 40));

        applyButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();

        addWindowListener(new WindowAdapter() {
                public void windowActivated(WindowEvent we) {
                    for (int i = 0; i < portCheckBoxes.length; i++) {
                        if (host.getPorts()[i].portOpened) {
                            portCheckBoxes[i].setSelected(false);
                        } else {
                            portCheckBoxes[i].setSelected(true);
                        }
                    }
                }
            });
    }

    public void setHost(Host host) {
        this.host = host;

        for (int i = 0; i < portCheckBoxes.length; i++) {
            portCheckBoxes[i].setText(host.getPorts()[i].getPortName());
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == applyButton) {
            for (int i = 0; i < portCheckBoxes.length; i++) {
                if (portCheckBoxes[i].isSelected()) {
                    host.getPorts()[i].setPortOpened(false);
                } else {
                    host.getPorts()[i].setPortOpened(true);
                }
            }

            dispose();
        } else if (source == cancelButton) {
            dispose();
        }
    }
}
