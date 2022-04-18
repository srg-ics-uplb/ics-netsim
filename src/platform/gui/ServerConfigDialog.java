package platform.gui;

import devices.addresses.IPAddress;

import devices.servers.Server;

import devices.interfaces.Interface;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Color;
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
import javax.swing.ImageIcon;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.JScrollPane;


public class ServerConfigDialog extends CenterableDialog implements ActionListener, FocusListener{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 5867384171918735104L;
	
	
	
	private final JPanel panelMid;
    private final JPanel panelNorth;
    
    private final JPanel imagePanel;
    
    private final JPanel DHCPIPPanel;
    private final JPanel staticIPPanel;
    private final JPanel labelPanel;
    private final JPanel fieldPanel;
    private final JPanel IPPanel;
    private final JTextField ipAddressField;
    private final JTextField subnetMaskField;
    private final JTextField defaultGatewayField;
    private final JLabel labelIpAddress;
    private final JLabel labelSubnetMask;
    private final JLabel labelDefaultGateway;    
    
    private final JPanel DHCPPanel;
    private final JPanel DHCPSub1Panel;
    private final JPanel DHCPLabelPanel;
    private final JPanel DHCPFieldPanel;
    private final JCheckBox DHCPCheck;
    private final JLabel DHCPDefaultGatewayLabel;
    private final JLabel DHCPDNSServerLabel;
    private final JLabel DHCPStartIPAddressLabel;
    private final JLabel DHCPMaxUsersLabel;
    private final JTextField DHCPDefaultGatewayField;
	private final JTextField DHCPDNSServerField;
    private final JTextField DHCPStartIPAddressField;
    private final JTextField DHCPMaxUsersField;
    
    private final JPanel HTTPPanel;
    private final JPanel HTTPSub1Panel;
    private final JCheckBox HTTPCheck;
    private final JLabel HTTPPathLabel;
    private final JTextField HTTPPathField;
    private final JPanel HTTPHtmlPanel;
    private final JTextArea HTTPHtmlArea;
    private final JScrollPane HTTPHtmlScroll;
    
    private final JButton applyButton;
    private final JButton cancelButton;
    protected String deviceName = "";
    protected boolean defaultSelected = false;
    protected boolean emptyName = true;
    protected boolean isValidQuartet = false;
    private Server server;

    public ServerConfigDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Configure Interface Ethernet0", true);

        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new BorderLayout());
        panel.setSize(230, 230);
        panel.setBackground(Color.WHITE);
        
        JLabel instruction = new JLabel();
        instruction.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(instruction, BorderLayout.NORTH);

        Utilities.wrapLabelText(instruction, "Configure Services and IP components on this Server.");

        /*
         * Main panels North, Mid, and South
         */
        //North panel
        panelNorth = new JPanel();
        panelNorth.setLayout(new BorderLayout());
        panelNorth.setBorder(new EmptyBorder(15, 50, 0, 5));
        imagePanel = new JPanel();
        
        ClassLoader loader = getClass().getClassLoader();
        try{
        BufferedImage image = ImageIO.read(loader.getResource("images/graphics/configDialog.jpg"));
        ImageIcon imageTry = new ImageIcon(image, "basta");
    	JLabel img = new JLabel(imageTry);
    	panelNorth.add(img, BorderLayout.WEST);
        } catch (IOException e){}
 
        panelNorth.add(instruction, BorderLayout.CENTER);
        panelNorth.setOpaque(false);
        
        //Mid panel
        panelMid = new JPanel();
        panelMid.setLayout(new BorderLayout());
        panelMid.setOpaque(false);
        labelPanel = new JPanel();
        labelPanel.setLayout(new GridLayout(3,1));
        labelPanel.setOpaque(false);
        fieldPanel = new JPanel();
        fieldPanel.setLayout(new GridLayout(3,1));
        fieldPanel.setOpaque(false);

        //South panel
        JPanel buttonPanel = new JPanel();
        applyButton = new JButton("Apply");
        applyButton.addActionListener(this);
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        buttonPanel.add(applyButton);
        buttonPanel.add(cancelButton);
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(5, 0, 5, 0));
        


        /*
         * Middle Panel - IP address, Subnet Mask, Gateway Address
         */
        
        IPPanel = new JPanel();
        IPPanel.setLayout(new BorderLayout());
        IPPanel.setBorder(new EmptyBorder(10, 20, 0, 20));
        
        staticIPPanel = new JPanel();
        staticIPPanel.setLayout(new BorderLayout());
        staticIPPanel.setBorder(new EmptyBorder(10, 20, 0, 20));
        staticIPPanel.setOpaque(false);
        DHCPIPPanel = new JPanel();
        DHCPIPPanel.setOpaque(false);
        DHCPIPPanel.setLayout(new BorderLayout());
        DHCPIPPanel.setBorder(new EmptyBorder(10, 20, 0, 20));
        
        
        //
        //staticIPPanel
        
        //labelPanel items
        labelIpAddress = new JLabel("IP Address: ");
        labelSubnetMask = new JLabel("Subnet Mask: ");
        labelDefaultGateway = new JLabel("Default Gateway: ");
        labelPanel.add(labelIpAddress, BorderLayout.NORTH);
        labelPanel.add(labelSubnetMask, BorderLayout.CENTER);
        labelPanel.add(labelDefaultGateway, BorderLayout.SOUTH);
        
        //fieldPanel items
        ipAddressField = new JTextField(15);
        ipAddressField.addActionListener(this);
        ipAddressField.addFocusListener(this);
        subnetMaskField = new JTextField(15);
        defaultGatewayField = new JTextField(15);
		fieldPanel.add(ipAddressField, BorderLayout.NORTH);
        fieldPanel.add(subnetMaskField, BorderLayout.CENTER);
        fieldPanel.add(defaultGatewayField, BorderLayout.SOUTH);        
        
        //combine labelPanel and fieldPanel into staticIPPanel
        staticIPPanel.add(labelPanel, BorderLayout.WEST);
        staticIPPanel.add(fieldPanel, BorderLayout.EAST);

        IPPanel.setOpaque(false);
        IPPanel.add(DHCPIPPanel, BorderLayout.NORTH);
        IPPanel.add(staticIPPanel, BorderLayout.SOUTH);
        
        
        
        /*
         * DHCP Panel
         */
        DHCPPanel = new JPanel();
        DHCPPanel.setLayout(new BorderLayout());
        DHCPPanel.setBorder(new EmptyBorder(10, 20, 0, 20));
        DHCPPanel.setOpaque(false);
        
        DHCPCheck = new JCheckBox("DHCP Service", false);
        DHCPCheck.setOpaque(false);
        DHCPCheck.addActionListener(this);
        
        //put DHCPLabelPanel and DHCPFieldPanel in here later 
        DHCPSub1Panel = new JPanel();
        DHCPSub1Panel.setLayout(new BorderLayout());
        DHCPSub1Panel.setBorder(new EmptyBorder(10, 20, 0, 20));
        DHCPSub1Panel.setOpaque(false);
        
        //DHCPLabelPanel components
        DHCPLabelPanel = new JPanel();
        //DHCPLabelPanel.setLayout(new BoxLayout(DHCPLabelPanel, BoxLayout.Y_AXIS));
        DHCPLabelPanel.setLayout(new GridLayout(4,1));
        DHCPLabelPanel.setBorder(new EmptyBorder(10, 20, 0, 20));
        DHCPLabelPanel.setOpaque(false);
        
        DHCPDefaultGatewayLabel = new JLabel("Default Gateway: ");
        DHCPDNSServerLabel = new JLabel("DNS Server: ");        
        DHCPStartIPAddressLabel = new JLabel("Start IP Address:");
        DHCPMaxUsersLabel = new JLabel("Maximum number of users:");
        
        DHCPLabelPanel.add(DHCPDefaultGatewayLabel);
        DHCPLabelPanel.add(DHCPDNSServerLabel);   
        DHCPLabelPanel.add(DHCPStartIPAddressLabel);
        DHCPLabelPanel.add(DHCPMaxUsersLabel);
        
        
      	//DHCPFieldPanel components
        DHCPFieldPanel = new JPanel();
        //DHCPFieldPanel.setLayout(new BoxLayout(DHCPFieldPanel, BoxLayout.Y_AXIS));
        DHCPFieldPanel.setLayout(new GridLayout(4,1));
        DHCPFieldPanel.setBorder(new EmptyBorder(10, 20, 0, 20));
        DHCPFieldPanel.setOpaque(false);
        
        DHCPDefaultGatewayField = new JTextField("0.0.0.0", 15);
        DHCPDefaultGatewayField.addFocusListener(this);
        DHCPDefaultGatewayField.setEditable(false);
        
        DHCPDNSServerField = new JTextField("DHCP DNS not yet implemented.", 15);
        //DHCPDNSServerField.addFocusListener(this);
        DHCPDNSServerField.setEditable(false);
        
        DHCPStartIPAddressField = new JTextField("0.0.0.0", 15);
        DHCPStartIPAddressField.addFocusListener(this);
        DHCPStartIPAddressField.setEditable(false);
        
        DHCPMaxUsersField = new JTextField("0", 3);
        DHCPMaxUsersField.addFocusListener(this);
        DHCPMaxUsersField.setEditable(false);

        DHCPFieldPanel.add(DHCPDefaultGatewayField);
        DHCPFieldPanel.add(DHCPDNSServerField);
        DHCPFieldPanel.add(DHCPStartIPAddressField);
        DHCPFieldPanel.add(DHCPMaxUsersField);
        
        //combine DHCPLabelPanel and DHCPFieldPanel into DHCPSub1Panel
        DHCPSub1Panel.add(DHCPLabelPanel, BorderLayout.WEST);
        DHCPSub1Panel.add(DHCPFieldPanel, BorderLayout.EAST);
        DHCPSub1Panel.setOpaque(false);

        
        DHCPPanel.add(DHCPCheck, BorderLayout.NORTH);
        DHCPPanel.add(DHCPSub1Panel, BorderLayout.CENTER);

        
        /*
         * HTTP Panel
         */
        HTTPPanel = new JPanel();
        HTTPPanel.setLayout(new BorderLayout());
        HTTPPanel.setBorder(new EmptyBorder(10, 20, 0, 20));
        HTTPPanel.setOpaque(false);

        HTTPSub1Panel = new JPanel();
        HTTPSub1Panel.setLayout(new BorderLayout());
        HTTPSub1Panel.setBorder(new EmptyBorder(10, 20, 0, 20));
        HTTPSub1Panel.setOpaque(false);

        HTTPCheck = new JCheckBox("HTTP Service", false);
        HTTPCheck.setOpaque(false);
        HTTPCheck.addActionListener(this);

        HTTPPathLabel = new JLabel("Path on server:");
        HTTPPathField = new JTextField("/", 25);
        HTTPPathField.setEditable(false);
        
        HTTPSub1Panel.add(HTTPPathLabel, BorderLayout.WEST);
        HTTPSub1Panel.add(HTTPPathField, BorderLayout.EAST);

        HTTPHtmlPanel = new JPanel();
        HTTPHtmlPanel.setLayout(new BorderLayout());
        HTTPHtmlPanel.setBorder(new EmptyBorder(10, 20, 0, 20));
        HTTPHtmlPanel.setOpaque(false);

        HTTPHtmlArea = new JTextArea("<html> \n\t<head>\n\t\t<title>\n\t\t\tIt works!\n\t\t</title>\n\t</head>\n\t<body>\n\t\tIt works!\n\t</body>\n</html>", 15,40);
        HTTPHtmlArea.setBorder(new EtchedBorder(EtchedBorder.LOWERED));
        HTTPHtmlArea.setTabSize(4);
        HTTPHtmlArea.setEditable(false);
        HTTPHtmlArea.setOpaque(false);
        
        HTTPHtmlScroll = new JScrollPane(HTTPHtmlArea);

        
        HTTPHtmlPanel.add(HTTPHtmlScroll);

        HTTPPanel.add(HTTPCheck, BorderLayout.NORTH);
        HTTPPanel.add(HTTPSub1Panel, BorderLayout.CENTER);
        HTTPPanel.add(HTTPHtmlPanel, BorderLayout.SOUTH);      


        
        
        
        
        panelMid.add(IPPanel, BorderLayout.NORTH);
        panelMid.add(DHCPPanel, BorderLayout.CENTER);
        panelMid.add(HTTPPanel, BorderLayout.SOUTH);                
        
        panel.add(panelNorth, BorderLayout.NORTH);
        panel.add(panelMid, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();

        addWindowListener(new WindowAdapter() {
                public void windowActivated(WindowEvent we) {
                    Interface[] ethernet0 = server.getInterfaces();

                    ipAddressField.setText(ethernet0[0].getIPAddress().toString());
                    subnetMaskField.setText(ethernet0[0].getSubnetMask().toString());
                    defaultGatewayField.setText(ethernet0[0].getDefaultGateway().toString());
                    
                    DHCPCheck.setSelected(server.getDHCPStatus());
                    if (DHCPCheck.isSelected()){
                        DHCPDefaultGatewayField.setEditable(true);
                        //DHCP DNS is not yet implemented because there aren't any DNS Servers. YET.
                        //DHCPDNSServerField.setEditable(true);
                        DHCPStartIPAddressField.setEditable(true);
                        DHCPMaxUsersField.setEditable(true);
                	}else{
                		DHCPDefaultGatewayField.setEditable(false);
                        DHCPDNSServerField.setEditable(false);
                        DHCPStartIPAddressField.setEditable(false);
                        DHCPMaxUsersField.setEditable(false);
                	}
                
                    DHCPDefaultGatewayField.setText(server.getDefaultGateway());
                	//DHCPDNSServerField.setText(server.getDNSServer());
                    DHCPStartIPAddressField.setText(server.getStartIPAddress());
                    DHCPMaxUsersField.setText(String.valueOf(server.getMaximumUsers()));
                    
                    HTTPCheck.setSelected(server.HTTPStatus);
                    if (HTTPCheck.isSelected()){
                		HTTPPathField.setEditable(true);
                		HTTPHtmlArea.setEditable(true);
                		repaint();
                	}else{
                		HTTPPathField.setEditable(false);
                		HTTPHtmlArea.setEditable(false);
                	}
                    HTTPPathField.setText(server.HTTPPath);
                    HTTPHtmlArea.setText(server.HTTPResource);                    
                    
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
        Interface[] ethernet0 = server.getInterfaces();

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
                    if (IPAddress.isValidInputQuartet(DHCPDefaultGatewayField.getText())) {
                    	server.setDefaultGateway(DHCPDefaultGatewayField.getText());
                    	//Uncomment when DHCP DNS is implemented
                    	//if (IPAddress.isValidInputQuartet(DHCPDNSServerField.getText())) {
                    		//server.setDNSServer(DHCPDNSServerField.getText());
                    		if (IPAddress.isValidInputQuartet(DHCPStartIPAddressField.getText())) {
                    			server.setStartIPAddress(DHCPStartIPAddressField.getText());
                    			server.setHTTPStatus(HTTPCheck.isSelected());
                                server.setHTTPPath(HTTPPathField.getText());
                                server.setHTTPResource(HTTPHtmlArea.getText());
                                server.setDHCPStatus(DHCPCheck.isSelected());
                                server.setMaximumUsers(Integer.valueOf(DHCPMaxUsersField.getText()));
                                dispose();
                    		}
                    	//}
                    }
                }
            }
        }
        
        
    }

    public void setServer(Server server) {
        this.server = server;
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
        if (source == DHCPCheck){
        	if (DHCPCheck.isSelected()){
                DHCPDefaultGatewayField.setEditable(true);
                //DHCP DNS is not yet implemented because there aren't any DNS Servers. YET.
                //DHCPDNSServerField.setEditable(true);
                DHCPStartIPAddressField.setEditable(true);
                DHCPMaxUsersField.setEditable(true);
        	}else{
        		DHCPDefaultGatewayField.setEditable(false);
                DHCPDNSServerField.setEditable(false);
                DHCPStartIPAddressField.setEditable(false);
                DHCPMaxUsersField.setEditable(false);
        	}
        }
        if (source == HTTPCheck){
        	if (HTTPCheck.isSelected()){
        		HTTPPathField.setEditable(true);
        		HTTPHtmlArea.setEditable(true);
        		repaint();
        	}else{
        		HTTPPathField.setEditable(false);
        		HTTPHtmlArea.setEditable(false);
        	}
        }
    }
	public void focusGained(FocusEvent arg0) {}
	public void focusLost(FocusEvent ae) {
		Object source = ae.getSource();
        if ((source == ipAddressField) && (ipAddressField.isEditable())){
        	automateSubnet();
        }
        if ((source == DHCPDefaultGatewayField) && (DHCPDefaultGatewayField.isEditable())){
        	if(!IPAddress.isValidInputQuartet(DHCPDefaultGatewayField.getText())) DHCPDefaultGatewayField.setText("0.0.0.0"); 
        }
        if ((source == DHCPDNSServerField) && (DHCPDNSServerField.isEditable())){
        	if(!IPAddress.isValidInputQuartet(DHCPDNSServerField.getText())) DHCPDNSServerField.setText("0.0.0.0");
        }
        if ((source == DHCPStartIPAddressField) && (DHCPStartIPAddressField.isEditable())){
        	if(!IPAddress.isValidInputQuartet(DHCPStartIPAddressField.getText())) DHCPStartIPAddressField.setText("0.0.0.0");
        }
        if ((source == DHCPMaxUsersField) && (DHCPMaxUsersField.isEditable())){
        	String tokens[];
        	//The regex pattern "\\." actually only splits the String between the periods.
        	tokens = DHCPStartIPAddressField.getText().split("\\.");
        	int i = Integer.valueOf(DHCPMaxUsersField.getText());
        	if ((i <= 0) || ((i + Integer.valueOf(tokens[3])) > 255)){
        		JOptionPane.showMessageDialog(null, "Invalid number of maximum users.", "Error", JOptionPane.ERROR_MESSAGE);
        		DHCPMaxUsersField.setText("0");
        	}
        }
	}
}

