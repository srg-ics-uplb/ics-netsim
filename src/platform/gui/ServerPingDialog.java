package platform.gui;

import devices.addresses.IPAddress;

import devices.consoles.Console;

import devices.servers.Server;

import java.awt.Color;
import java.awt.FlowLayout;
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
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class ServerPingDialog extends CenterableDialog implements ActionListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 6492966244433768386L;
	private final JTextField ipAddressField;
    private Server server;
    private final JPanel panel;
    private final JPanel panelNorth;
    private final JTextArea textArea;
    private final JLabel labelIpAddress;
    private final JLabel labelConsole;
    private final JButton ping;
    private final JButton clearButton;
    private final JButton cancelButton;
    protected String deviceName = "";
    protected boolean defaultSelected = false;
    protected boolean isValidQuartet = false;

    public ServerPingDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Ping Command", true);

        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new AbsoluteLayout());
        panel.setSize(700, 520);

        try {
            ClassLoader loader = getClass().getClassLoader();
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/pingDialog.jpg"));
            panel.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        labelIpAddress = new JLabel("IP Address: ");
        labelIpAddress.setFont(new Font("Arial", Font.BOLD, 12));
        ipAddressField = new JTextField(15);
        ipAddressField.setBackground(Color.white);
        ping = new JButton("Ping");

        panelNorth = new JPanel();
        panelNorth.setOpaque(false);
        panelNorth.setLayout(new FlowLayout());
        panelNorth.add(labelIpAddress);
        panelNorth.add(ipAddressField);
        panelNorth.add(ping);

        panel.add(panelNorth, new AbsoluteConstraints(70, 70, 350, 40));

        labelConsole = new JLabel("Console Output");
        textArea = new JTextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        panel.add(labelConsole, new AbsoluteConstraints(15, 105, 120, 20));
        panel.add(scrollPane, new AbsoluteConstraints(15, 130, 480, 300));

        JPanel buttonPanel = new JPanel();
        clearButton = new JButton("Clear All");
        buttonPanel.add(clearButton);
        cancelButton = new JButton("Exit");
        buttonPanel.add(cancelButton);
        buttonPanel.setOpaque(false);
        buttonPanel.setSize(600, 30);
        panel.add(buttonPanel, new AbsoluteConstraints(0, 430, 510, 35));

        clearButton.addActionListener(this);
        cancelButton.addActionListener(this);
        ping.addActionListener(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
    }

    public void setServer(Server server) {
        this.server = server;
    }

    public void clearFields() {
        ipAddressField.setText("");
        textArea.setText("");
    }

    public void showReply(IPAddress address) {
        String add = address.toString();
        textArea.append("\nPinging " + add + " with 32 bytes of data:\n");

        for (int i = 0; i < 5; i++) {
            textArea.append("\nReply from " + add + ": bytes=32 time=60ms TTL=241");
        }

        textArea.append("\nPing statistics for " + add + ":\n\tPackets: Sent = 5, Received = 5, Lost = 0 (0% loss),\nApproximate round trip times in milli-seconds:\n\tMinimum = 50ms, Maximum =  60ms, Average =  55ms\n");
    }

    public void showRequestTimedOut(IPAddress address) {
        String add = address.toString();
        textArea.append("\nPinging " + add + " with 32 bytes of data:\n");

        for (int i = 0; i < 5; i++) {
            textArea.append("\nRequest timed out.");
        }

        textArea.append("\nPing statistics for " + add + ":\n\tPackets: Sent = 5, Received = 0, Lost = 5 (100% loss),\nApproximate round trip times in milli-seconds:\n\tMinimum = 0ms, Maximum =  0ms, Average =  0ms\n");
    }

    public void ping(String ipAddress) {
        Console console = server.getConsole();
        IPAddress destination = new IPAddress(ipAddress);

        if (console.deviceReachable(server, destination)) {
            showReply(destination);
        } else {
            showRequestTimedOut(destination);
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == clearButton) {
            clearFields();
        } else if (source == ping) {
            String ipAddress = ipAddressField.getText();
            boolean validIP = IPAddress.isValidInputQuartet(ipAddress);

            if (validIP) {
                ping(ipAddress);
            } else {
                ipAddressField.setText("");
            }
        } else if (source == cancelButton) {
            dispose();
        }
    }
}
