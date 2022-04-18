package platform.gui;

import devices.addresses.IPAddress;
import devices.addresses.Doublet;

import devices.hosts.Host;
import devices.servers.Server;

import devices.interfaces.Interface;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;


public class HostConfigDialog extends CenterableDialog implements ActionListener, FocusListener{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 5867384171918735104L;
	private final JTextField ipAddressField;
    private final JTextField subnetMaskField;
    private final JTextField defaultGatewayField;
    private final JTextField DHCPStatusField;
    private final JPanel labelPanel;
    private final JPanel fieldPanel;
    private final JPanel panelMid;
    private final JPanel panelNorth;
    private final JPanel IPPanel;
    private final JPanel radioPanel;
    private final JLabel labelIpAddress;
    private final JLabel labelSubnetMask;
    private final JLabel labelDefaultGateway;
    private final JButton applyButton;
    private final JButton cancelButton;
    private final ButtonGroup IPRadioGroup;
    private final JRadioButton DHCPRadio;
    private final JRadioButton staticRadio;
    protected String deviceName = "";
    protected boolean defaultSelected = false;
    protected boolean emptyName = true;
    protected boolean isValidQuartet = false;
    private Host host;
    private MainFrame mainFrame;

    public HostConfigDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Configure Interface Ethernet0", true);
        this.mainFrame = mainFrame;
        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new BorderLayout());
        panel.setSize(230, 230);

        try {
            ClassLoader loader = getClass().getClassLoader();
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/configDialog.jpg"));
            panel.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel instruction = new JLabel();
        instruction.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(instruction, BorderLayout.NORTH);

        Utilities.wrapLabelText(instruction, "Configure IP Address, Subnet Mask and Default Gateway of Ethernet0 in this Host");

        panelMid = new JPanel();
        panelMid.setLayout(new BorderLayout());
        panelMid.setBorder(new EmptyBorder(10, 20, 0, 20));
        panelMid.setOpaque(false);

        panelNorth = new JPanel();
        panelNorth.setLayout(new FlowLayout());
        panelNorth.setBorder(new EmptyBorder(15, 50, 0, 5));
        panelNorth.add(instruction);
        panelNorth.setOpaque(false);

        radioPanel = new JPanel();
        radioPanel.setLayout(new BorderLayout());
        radioPanel.setBorder(new EmptyBorder(15, 50, 0, 5));
        radioPanel.setOpaque(false);
        
        IPRadioGroup = new ButtonGroup();
        DHCPRadio = new JRadioButton("DHCP");
        DHCPRadio.setOpaque(false);
        DHCPRadio.addActionListener(this);
        staticRadio = new JRadioButton("Static");
        staticRadio.setOpaque(false);
        staticRadio.addActionListener(this);
        staticRadio.setSelected(true);
        IPRadioGroup.add(DHCPRadio);
        IPRadioGroup.add(staticRadio);
        
        DHCPStatusField = new JTextField(20);
        DHCPStatusField.setEditable(false);
        
        radioPanel.add(DHCPStatusField, BorderLayout.EAST);
        radioPanel.add(DHCPRadio, BorderLayout.CENTER);
        radioPanel.add(staticRadio, BorderLayout.SOUTH);
        
        

        labelIpAddress = new JLabel("IP Address: ");
        labelSubnetMask = new JLabel("Subnet Mask: ");
        labelDefaultGateway = new JLabel("Default Gateway: ");

        ipAddressField = new JTextField(15);
        subnetMaskField = new JTextField(15);
        defaultGatewayField = new JTextField(15);

        IPPanel = new JPanel();
        IPPanel.setLayout(new BorderLayout());
        IPPanel.setBorder(new EmptyBorder(10, 20, 0, 20));
        
        labelPanel = new JPanel();
        labelPanel.setLayout(new BorderLayout());
        labelPanel.setOpaque(false);

        fieldPanel = new JPanel();
        fieldPanel.setLayout(new BorderLayout());
        fieldPanel.setOpaque(false);

        labelPanel.add(labelIpAddress, BorderLayout.NORTH);
        labelPanel.add(labelSubnetMask, BorderLayout.CENTER);
        labelPanel.add(labelDefaultGateway, BorderLayout.SOUTH);

        fieldPanel.add(ipAddressField, BorderLayout.NORTH);
        fieldPanel.add(subnetMaskField, BorderLayout.CENTER);
        fieldPanel.add(defaultGatewayField, BorderLayout.SOUTH);

        IPPanel.add(labelPanel, BorderLayout.WEST);
        IPPanel.add(fieldPanel, BorderLayout.EAST);
        IPPanel.setOpaque(false);

        panelMid.add(radioPanel, BorderLayout.NORTH);
        panelMid.add(IPPanel, BorderLayout.CENTER);
        
        panel.add(panelNorth, BorderLayout.NORTH);
        panel.add(panelMid, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        applyButton = new JButton("Apply");
        buttonPanel.add(applyButton);
        cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        panel.add(buttonPanel, BorderLayout.SOUTH);

        ipAddressField.addActionListener(this);
        ipAddressField.addFocusListener(this);
        applyButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();

        addWindowListener(new WindowAdapter() {
                public void windowActivated(WindowEvent we) {
                    Interface[] ethernet0 = host.getInterfaces();
                    if (host.getStaticIP()){
                    	staticRadio.setSelected(true);
                    	defaultGatewayField.setEditable(true);
                    	subnetMaskField.setEditable(true);
                    	ipAddressField.setEditable(true);
                    	DHCPStatusField.setText("");
                    }else{
                    	DHCPRadio.setSelected(true);
                    	defaultGatewayField.setEditable(false);
                    	subnetMaskField.setEditable(false);
                    	ipAddressField.setEditable(false);
                    }
                    ipAddressField.setText(ethernet0[0].getIPAddress().toString());
                    subnetMaskField.setText(ethernet0[0].getSubnetMask().toString());
                    defaultGatewayField.setText(ethernet0[0].getDefaultGateway().toString());
                }
            });
    }
    /*REL*/
    private void automateSubnet(){
        if (!IPAddress.isValidInputQuartet(ipAddressField.getText())){
        	//JOptionPane.showMessageDialog(this, "Please enter a valid quartet in the empty field.", "Scalable Cisco IOS Simulator", JOptionPane.WARNING_MESSAGE);
        	ipAddressField.setText("0.0.0.0");
        	ipAddressField.grabFocus();
       }else{
    	   int[] decimals = IPAddress.getQuartet(ipAddressField.getText());
    	   if (decimals[0] >=0 &&  decimals[0]<=127) subnetMaskField.setText("255.0.0.0");
    	   if (decimals[0] >=128 &&  decimals[0]<=191) subnetMaskField.setText("255.255.0.0");
    	   if (decimals[0] >=192 &&  decimals[0]<=223) subnetMaskField.setText("255.255.255.0");
    	   
       }
    	
    }
    /*-REL*/
    private void checkFields() {
        Interface[] ethernet0 = host.getInterfaces();

        if ((ipAddressField.getText().equals("") || subnetMaskField.getText().equals("") || defaultGatewayField.getText().equals(""))) {
            JOptionPane.showMessageDialog(this, "Please enter a valid quartet in the empty field.", "Scalable Cisco IOS Simulator", JOptionPane.WARNING_MESSAGE);

            return;
        }

        if (IPAddress.isValidInputQuartet(ipAddressField.getText())) {
            int[] decimals = IPAddress.getQuartet(ipAddressField.getText());
            ethernet0[0].setIPAddress(decimals);

            if (IPAddress.isValidInputQuartet(subnetMaskField.getText())) {
                decimals = IPAddress.getQuartet(subnetMaskField.getText());
                ethernet0[0].setSubnetMask(decimals);

                if (IPAddress.isValidInputQuartet(defaultGatewayField.getText())) {
                    decimals = IPAddress.getQuartet(defaultGatewayField.getText());
                    ethernet0[0].setDefaultGateway(decimals);
                    ethernet0[0].setState(Interface.UP);
                    
                    if (DHCPRadio.isSelected()){
                    	host.setStaticIP(false);
                    }else if (staticRadio.isSelected()){
                    	host.setStaticIP(true);
                    }
                    dispose();
                }
            }
        }
    }

    public void setHost(Host host) {
        this.host = host;
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();
        if (source == ipAddressField){
        	automateSubnet();
        }
        if (source == applyButton) {
            checkFields();
        } else if (source == cancelButton) {
            emptyName = true;
            dispose();
        }
        if (source == DHCPRadio){
        	Doublet DHCPReply = null;
        	Server s;
        	ipAddressField.setEditable(false);
        	subnetMaskField.setEditable(false);
        	defaultGatewayField.setEditable(false);
        	DHCPStatusField.setText("Request Sent. Waiting for reply...");
        	s = host.getConsole().findDHCPServer(host.getInterfaces()[0]);
        	if (s != null){
        		DHCPReply = host.requestDHCP(s, mainFrame.getPacketListFrame());
        		ipAddressField.setText(DHCPReply.getIPAddress());
        		defaultGatewayField.setText(DHCPReply.getDefaultGateway());
        		if (DHCPReply.getIPAddress().equals("0.0.0.0")){
        			DHCPStatusField.setText("No DHCP replies received.");
        		}else
        			DHCPStatusField.setText("DHCP Successful.");
        	}else{
        		DHCPStatusField.setText("No DHCP replies received.");
        	}
        		
        }
        if (source == staticRadio){        	
        	ipAddressField.setEditable(true);
        	subnetMaskField.setEditable(true);
        	defaultGatewayField.setEditable(true);
        	DHCPStatusField.setText("");
        }
    }
	public void focusGained(FocusEvent arg0) {
	}
	public void focusLost(FocusEvent ae) {
		Object source = ae.getSource();
        if ((source == ipAddressField) && (ipAddressField.isEditable())){
        	automateSubnet();
        }

	}
}
