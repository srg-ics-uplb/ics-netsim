package platform.gui;

import devices.addresses.IPAddress;

import devices.consoles.Console;

import devices.hosts.Host;

import devices.hosts.ports.Port;

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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;


public class HostPortScanDialog extends CenterableDialog implements ActionListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 7824477693566440959L;
	private final JTextField ipAddressField;
    private Host host;
    private final JPanel panel;
    private final JPanel panelNorth;
    private final JTextArea textArea;
    private final JLabel labelIpAddress;
    private final JLabel labelConsole;
    private final JButton portScan;
    private final JButton clearButton;
    private final JButton cancelButton;
    protected String deviceName = "";
    protected boolean defaultSelected = false;
    protected boolean emptyName = true;
    protected boolean isValidQuartet = false;

    public HostPortScanDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "System Port Scan", true);

        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new AbsoluteLayout());
        panel.setSize(700, 520);

        try {
            ClassLoader loader = getClass().getClassLoader();
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/PortScanDialog.jpg"));
            panel.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        labelIpAddress = new JLabel("IP Address: ");
        labelIpAddress.setFont(new Font("Arial", Font.BOLD, 12));
        ipAddressField = new JTextField(15);
        ipAddressField.setBackground(Color.white);
        portScan = new JButton("Port Scan");

        panelNorth = new JPanel();
        panelNorth.setOpaque(false);
        panelNorth.setLayout(new FlowLayout());
        panelNorth.add(labelIpAddress);
        panelNorth.add(ipAddressField);
        panelNorth.add(portScan);

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
        portScan.addActionListener(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void clearFields() {
        ipAddressField.setText("");
        textArea.setText("");
    }

    public void displayPorts(int[] ipAddressDecimals, Port[] ports) {
        textArea.append("PORT\t STATE\t SERVICE\n");

        for (int i = 0; i < ports.length; i++) {
            if (!ports[i].isOpened()) {
                textArea.append("" + ports[i].portNumber + "\t open\t" + ports[i].portName + "\n");
            }
        }
    }

    public void portScan(String ipAddress) {
        Console console = host.getConsole();

        if (console.deviceReachable(host, new IPAddress(ipAddress))) {
            if (console.getDestinationInterface().getDevice() instanceof Host) {
                Host destinationHost = (Host) console.getDestinationInterface().getDevice();
                Port[] ports = destinationHost.getPorts();
                double seconds = 0.0005;
                textArea.append("Interesting ports on " + ipAddress + "\n");
                textArea.append("(The " + destinationHost.getNumberOfClosedPorts() + " ports scanned but not shown below are in state: closed)\n");
                displayPorts(IPAddress.getQuartet(ipAddress), ports);
                seconds = destinationHost.getNumberOfClosedPorts() * seconds;
                textArea.append("\nNmap finished: 1 IP address (1 host up) scanned in " + seconds + " seconds");
                textArea.append("\n\n");
            } else {
                JOptionPane.showMessageDialog(this, "The IP Address was found was not of a host device.", "Error!!!", JOptionPane.ERROR_MESSAGE);
                clearFields();
            }
        } else {
            JOptionPane.showMessageDialog(this, "The IP Address was not found.", "Error!!!", JOptionPane.ERROR_MESSAGE);
            clearFields();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == clearButton) {
            clearFields();
        } else if (source == portScan) {
            String ipAdd = ipAddressField.getText();
            boolean validIP = IPAddress.isValidInputQuartet(ipAdd);

            if (validIP) {
                portScan(ipAdd);
            } else {
                ipAddressField.setText("");
            }
        } else if (source == cancelButton) {
            emptyName = true;
            dispose();
        }
    }
}
