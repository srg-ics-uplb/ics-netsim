package platform.gui;

import devices.addresses.IPAddress;
import devices.Device;
import devices.servers.Server;
import devices.switches.Switch;

import devices.consoles.Console;

import devices.hosts.Host;
import devices.interfaces.Interface;

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

/*
 * Derived from HostPingDialog
 */

public class HostHTTPRequestDialog extends CenterableDialog implements ActionListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 6492977244433768386L;
	private final JTextField URIField;
    private Host host;
    private final JPanel panel;
    private final JPanel panelNorth;
    private final JTextArea textArea;
    private final JLabel URILabel;
    private final JLabel labelConsole;
    private final JButton send;
    private final JButton clearButton;
    private final JButton cancelButton;
    protected String deviceName = "";
    protected boolean defaultSelected = false;
    protected boolean isValidQuartet = false;
    public MainFrame mainFrame;

    public HostHTTPRequestDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "HTTP GETter", true);
        this.mainFrame = mainFrame;
        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new AbsoluteLayout());
        panel.setSize(700, 520);
/*
        try {
            ClassLoader loader = getClass().getClassLoader();
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/pingDialog.jpg"));
            panel.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }Server
*/
        
        URILabel = new JLabel("URI:  http://");
        URILabel.setFont(new Font("Arial", Font.BOLD, 12));
        URIField = new JTextField(15);
        URIField.setBackground(Color.white);
        send= new JButton("Send");

        panelNorth = new JPanel();
        panelNorth.setOpaque(false);
        panelNorth.setLayout(new FlowLayout());
        panelNorth.add(URILabel);
        panelNorth.add(URIField);
        panelNorth.add(send);

        panel.add(panelNorth, new AbsoluteConstraints(70, 15, 350, 40));

        labelConsole = new JLabel("Result:");
        textArea = new JTextArea();
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(textArea);

        panel.add(labelConsole, new AbsoluteConstraints(15, 80, 120, 20));
        panel.add(scrollPane, new AbsoluteConstraints(15, 105, 480, 300));

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
        send.addActionListener(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void clearFields() {
        //URIField.setText("");
        textArea.setText("");
    }   

    public void showQueryTimedOut(IPAddress address) {
        String add = address.toString();
        textArea.append("\nServer not found\nCannot find server at " + add + "\n\n");
    }

    public void queryHTTP(String ipAddress, String path) {
    	Device returnedDevice;
    	Server returnedServer;
        Console console = host.getConsole();
        IPAddress destination = new IPAddress(ipAddress);
        String hostQuery = "";
        String remoteReply = "";
        IPAddress hostAddress;

        hostQuery = "GET " + path + " HTTP/1.1";
        
        //retrieve remote Device denoted by IP, proceed if Device is Server
        Interface deviceInterface = (host.getInterfaces()[0]);
        hostAddress = deviceInterface.getIPAddress();
        returnedDevice = console.inLANGetDevice(deviceInterface, destination);
        if (returnedDevice instanceof Server){
        	returnedServer = (Server) returnedDevice;
        	remoteReply = host.requestHTTP(returnedServer, hostQuery, mainFrame.getPacketListFrame());
        	if (remoteReply.equals("")){ textArea.append("Unable to Connect.\nCannot establish a connection to " + ipAddress + ".\n\n");}
        	else {
        		textArea.append("REPLY FROM " + ipAddress + ":\n\n" );
        		textArea.append(remoteReply + "\n\n");
        		textArea.append("END REPLY.\n\n");
        	}
        }else showQueryTimedOut(destination);
    }

	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		boolean validIP = false;
		boolean validPath = false;
		String ipAddress = "";
		String path = "";
		
        if (source == clearButton) {
            clearFields();
        } else if (source == send) {
        	//check for URI validity
        	String tokens[] = URIField.getText().trim().split("/");
        	if (tokens.length > 0){
        		ipAddress = tokens[0];
        		validIP = IPAddress.isValidInputQuartet(ipAddress);
        		//create path for resource on server
        		for (int x = 1; x < tokens.length; x++){path = path +  "/" +  tokens[x];}
        		//if path is blank or just a "/"
        		if (path.equals("")){path = "/";}
        		validPath = !path.contains(" ");
        		if (!validPath) {JOptionPane.showMessageDialog(null,"Invalid Path specified.", "URI Error", JOptionPane.ERROR_MESSAGE);}
        	}
            if (validIP && validPath) {queryHTTP(ipAddress, path);}
        } else if (source == cancelButton) {dispose();}
    }
}
