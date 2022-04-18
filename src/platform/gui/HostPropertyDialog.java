package platform.gui;

import devices.hosts.Host;

import devices.interfaces.Interface;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class HostPropertyDialog extends CenterableDialog implements ActionListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 2213004090863690861L;
	private Host host;
    private final JPanel panel;
    private final JButton closeButton;
    private final JLabel instruction;
    private final JLabel hostName = new JLabel("Host Name: ");
    private final JLabel interFace = new JLabel("Interface: ");
    private final JLabel ipAddress = new JLabel("IP Address: ");
    private final JLabel subnetMask = new JLabel("Subnet Mask: ");
    private final JLabel defaultGateway = new JLabel("Default Gateway: ");
    private JLabel hostName2 = new JLabel();
    private JLabel interFace2 = new JLabel();
    private JLabel ipAddress2 = new JLabel();
    private JLabel subnetMask2 = new JLabel();
    private JLabel defaultGateway2 = new JLabel();
    protected String deviceName = "";
    protected boolean defaultSelected = false;
    protected boolean emptyName = true;
    protected boolean isValidQuartet = false;

    public HostPropertyDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Host Properties", true);

        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new AbsoluteLayout());
        panel.setSize(400, 350);

        try {
            ClassLoader loader = getClass().getClassLoader();
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/configDialog.jpg"));
            panel.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        instruction = new JLabel();
        instruction.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(instruction, new AbsoluteConstraints(60, 20, 150, 20));

        Utilities.wrapLabelText(instruction, "HOST PROPERTIES");

        panel.add(hostName, new AbsoluteConstraints(40, 60, 75, 25));
        panel.add(interFace, new AbsoluteConstraints(50, 85, 75, 25));
        panel.add(ipAddress, new AbsoluteConstraints(40, 110, 100, 25));
        panel.add(subnetMask, new AbsoluteConstraints(28, 135, 100, 25));
        panel.add(defaultGateway, new AbsoluteConstraints(12, 160, 100, 25));

        panel.add(hostName2, new AbsoluteConstraints(120, 60, 75, 25));
        panel.add(interFace2, new AbsoluteConstraints(120, 85, 75, 25));
        panel.add(ipAddress2, new AbsoluteConstraints(120, 110, 100, 25));
        panel.add(subnetMask2, new AbsoluteConstraints(120, 135, 100, 25));
        panel.add(defaultGateway2, new AbsoluteConstraints(120, 160, 100, 25));

        JPanel buttonPanel = new JPanel();
        closeButton = new JButton("Close");
        buttonPanel.add(closeButton);
        buttonPanel.setOpaque(false);
        panel.add(buttonPanel, new AbsoluteConstraints(0, 190, 200, 40));

        closeButton.addActionListener(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
    }

    public void setHost(Host host) {
        this.host = host;
        loadInfo();
    }

    public void loadInfo() {
        Interface[] ethernet0 = host.getInterfaces();

        hostName2.setText(host.getName());
        interFace2.setText(ethernet0[0].getName());
        ipAddress2.setText(ethernet0[0].getIPAddress().toString());
        subnetMask2.setText(ethernet0[0].getSubnetMask().toString());
        defaultGateway2.setText(ethernet0[0].getDefaultGateway().toString());
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == closeButton) {
            dispose();
        }
    }
}
