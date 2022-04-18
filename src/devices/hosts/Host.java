package devices.hosts;

import devices.addresses.Doublet;
import devices.addresses.IPAddress;

import devices.Device;

import devices.servers.Server;

import devices.hosts.ports.Port;

import devices.protocols.HTTPClient;
import devices.protocols.DHCPClient;

import devices.interfaces.Ethernet;
import devices.interfaces.Interface;

import platform.gui.PacketGenerator;
import platform.gui.PacketListUnit;
import platform.gui.PacketListFrame;

import java.awt.Image;
import java.awt.Point;

import java.util.StringTokenizer;

import javax.swing.ImageIcon;


/*
 * TODO: Replace deprecated class StringTokenizer. Use String.split() instead
 */

public class Host extends Device implements DHCPClient, HTTPClient{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4847421041298460601L;
	
    public static int hostCount;
    public String[] portDetails = {
        "21|FTP|File Transfer Protocol", "22|SSH|Secure Shell",
        "23|Telnet|Telnet Info", "666|Doom|Doom Information",
        "53|DNS|DNS Information", "110|POP3|POP3 E-mail"
    };
    private Port[] ports;
    private final Ethernet ethernet0 = new Ethernet(this, "Ethernet0/0", "ethernet0");
    private final Interface[] interfaces = { ethernet0 };
    public boolean staticIP;

    public Host(String name, Point location) {
        super(name, location);
        hostCount++;
        staticIP = true;
        macAddress = "2000." + randomHexChar() + randomHexChar() + randomHexChar() + randomHexChar() + "." + randomHexChar() + randomHexChar() + randomHexChar() + randomHexChar() + "." + randomHexChar() + randomHexChar() + randomHexChar() + randomHexChar();
        ClassLoader loader = getClass().getClassLoader();
        image = (new ImageIcon(loader.getResource("images/devices/host.gif"))).getImage();
        setInterfaces(interfaces);
        initPorts();
    }

    public String requestHTTP(Server server, String hostQuery, PacketListFrame packetListFrame){
    	String remoteReply;
    	
    	if (packetListFrame.isVisible()){
    		packetListFrame.addPacket(new PacketListUnit(this.getName(), server.getName(), "HTTP", PacketGenerator.createHTTPPacket(this.getMacAddress(), server.getMacAddress(), this.getInterfaces()[0].getIPAddress().toString(), server.getInterfaces()[0].getIPAddress().toString(), "1035", "80", "1", "1", hostQuery)));
    	}
    	
    	remoteReply = server.HTTPPort(this, hostQuery, packetListFrame);
    	
    	return remoteReply;
    }
    
    public Doublet requestDHCP(Server server, PacketListFrame packetListFrame){
    	Doublet DHCPAns = new Doublet("0.0.0.0", "0.0.0.0");
    	String request = null;
    	String intermediateMsg = null;
    	String tokens[] = null;
    	int DHCPState = 0; //0 for DISCOVER, 1 for REQUEST, 2 for Done
    	boolean transactionDone = false;

    	while (!transactionDone){
    		switch (DHCPState){
    			case 0: {
    				request = "DHCPDISCOVER";
    				
    		    	if (packetListFrame.isVisible()){
    		    		packetListFrame.addPacket(new PacketListUnit(this.getName(), server.getName(), "DHCP", PacketGenerator.createDHCPPacket(this.getMacAddress(), "FFFF.FFFF.FFFF.FFFF" , this.getInterfaces()[0].getIPAddress().toString(), IPAddress.BROADCAST, "68", "67", "0.0.0.0", "0.0.0.0", this.getMacAddress(), "")));
    		    	}
    	        	intermediateMsg = server.DHCPPort(this, request, packetListFrame);
    	        	
    	        	if (intermediateMsg.equals("")){
    	        		transactionDone = true;
    	        	}else{
    	        		tokens = intermediateMsg.split(" ");
    	        		if (tokens[0].equals("DHCPOFFER")) DHCPState = 1;
    	        	}
    	        	break;
    			}
    			case 1: {    				
    				request = "DHCPREQUEST ";
    				for (int x = 1 ; x < (tokens.length-1); x++){
    					request += tokens[x] + " ";
    				}
    				//avoid trailing whitespace
    				request += tokens[tokens.length-1];
    				
    				if (packetListFrame.isVisible()){
    		    		packetListFrame.addPacket(new PacketListUnit(this.getName(), server.getName(), "DHCP", PacketGenerator.createDHCPPacket(this.getMacAddress(), "FFFF.FFFF.FFFF.FFFF" , this.getInterfaces()[0].getIPAddress().toString(), IPAddress.BROADCAST, "68", "67", tokens[2], tokens[4], this.getMacAddress(), tokens[6])));
    		    	}
    				intermediateMsg = server.DHCPPort(this, request, packetListFrame);
    				
    				if (intermediateMsg.equals("")){
    					transactionDone = true;
    				}else{
    					tokens = intermediateMsg.split(" ");
    	        		if (tokens[0].equals("DHCPACK")) DHCPState = 2;
    				}
    				break;
    			}
    			case 2:{
    				for (int x = 0; x < tokens.length; x++){
    					if (tokens[x].equals("yiaddr:")){
    						DHCPAns.setIPAddress(tokens[x+1]);
    					}
    					if (tokens[x].equals("dgiaddr:")){
    						DHCPAns.setDefaultGateway(tokens[x+1]);
    					}
    				}
    				transactionDone = true;
    				break;
    			}
    				
    		}//end switch
    	}//end while
    	return DHCPAns;
    }
    public void initPorts() {
        ports = new Port[portDetails.length];

        for (int i = 0; i < portDetails.length; i++) {
            ports[i] = new Port();

            StringTokenizer token = new StringTokenizer(portDetails[i], "|");
            ports[i].setPortNumber(Integer.parseInt(token.nextToken()));
            ports[i].setPortName(token.nextToken());
            ports[i].setPortDescription(token.nextToken());
        }
    }

    public Image getImage() {
        return image;
    }

    public Port[] getPorts() {
        return ports;
    }

    public int getNumberOfClosedPorts() {
        int num = 0;

        for (int i = 0; i < ports.length; i++) {
            if (!ports[i].isOpened()) {
                num++;
            }
        }

        return num;
    }

    public int getNumberOfOpenedPorts() {
        int num = 0;

        for (int i = 0; i < ports.length; i++) {
            if (ports[i].isOpened()) {
                num++;
            }
        }

        return num;
    }
    
    public void setStaticIP(boolean b){
    	staticIP = b;
    }
    public boolean getStaticIP(){
    	return staticIP;
    }
}
