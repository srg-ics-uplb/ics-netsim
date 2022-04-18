package devices.servers;

import devices.Device;
import devices.addresses.IPAddress;

import devices.protocols.HTTPServer;
import devices.protocols.DHCPServer;

import devices.servers.ports.Port;

import devices.interfaces.Ethernet;
import devices.interfaces.Interface;

import platform.gui.PacketGenerator;
import platform.gui.PacketListUnit;
import platform.gui.PacketListFrame;

import java.awt.Image;
import java.awt.Point;

import java.util.StringTokenizer;

import javax.swing.ImageIcon;




public class Server extends Device implements HTTPServer, DHCPServer{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4847421041298460601L;
	
    public static int serverCount;
    public String[] portDetails = {
        "21|FTP|File Transfer Protocol", "22|SSH|Secure Shell",
        "23|Telnet|Telnet Info", "666|Doom|Doom Information",
        "53|DNS|DNS Information", "110|POP3|POP3 E-mail"
    };
    private Port[] ports;
    private final Ethernet ethernet0 = new Ethernet(this, "Ethernet0/0", "ethernet0");
    private final Interface[] interfaces = { ethernet0 };
    
    public boolean HTTPStatus;
	public String HTTPPath;
	public String HTTPResource;
	public static String HTTPNotFound = "<html>\n\t<head>\n\t\t<title>\n\t\t\tObject not found!\n\t\t</title>\n\t</head>\n\t<body>\n\t\tObject not found!\n\t</body>\n</html>";
	
	public boolean DHCPStatus;
	public String defaultGateway;
	public String DNSServer;
	public String startIPAddress;
	public int maximumUsers;
	public int numOfUsers;

    public Server(String name, Point location) {
        super(name, location);
        serverCount++;

        HTTPStatus = false;
        HTTPPath = new String("/");
        HTTPResource = "<html> \n\t<head>\n\t\t<title>\n\t\t\tIt works!\n\t\t</title>\n\t</head>\n\t<body>\n\t\tIt works!\n\t</body>\n</html>";
        
        DHCPStatus = false;
    	defaultGateway = "0.0.0.0";
    	DNSServer = "0.0.0.0";
    	startIPAddress = "0.0.0.0";
    	maximumUsers = 0;
        numOfUsers = 0;
        macAddress = "1000." + randomHexChar() + randomHexChar() + randomHexChar() + randomHexChar() + "." + randomHexChar() + randomHexChar() + randomHexChar() + randomHexChar() + "." + randomHexChar() + randomHexChar() + randomHexChar() + randomHexChar();
    	
        ClassLoader loader = getClass().getClassLoader();
        image = (new ImageIcon(loader.getResource("images/devices/server.gif"))).getImage();
        setInterfaces(interfaces);
        initPorts();
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

    //This implementation of the DHCP protocol assumes that only one Host at a time will be transacted with by the server.
    public String DHCPPort(Device host, String remoteQuery, PacketListFrame packetListFrame){
    	String response = "";
    	String tokens[];
    	if (DHCPStatus){
    		tokens = remoteQuery.split(" ");
    		if (tokens.length > 0){
    			if (tokens[0].equals("DHCPDISCOVER")){
    				//if we can offer an address
    				if (numOfUsers < maximumUsers){
    					if (packetListFrame.isVisible()){
        		    		packetListFrame.addPacket(new PacketListUnit(this.getName(), host.getName(), "DHCP", PacketGenerator.createDHCPPacket(this.getMacAddress(), "FFFF.FFFF.FFFF.FFFF" , this.getInterfaces()[0].getIPAddress().toString(), IPAddress.BROADCAST, "67", "68", nextIPAdd(), this.getInterfaces()[0].getIPAddress().toString(), host.getMacAddress(), defaultGateway)));
        		    	}
    					response = "DHCPOFFER yiaddr: " + nextIPAdd() + " siaddr: " + this.getInterfaces()[0].getIPAddress() + " dgiaddr: " + defaultGateway;
    				}
    			}else if (tokens[0].equals("DHCPREQUEST")){
    				//remoteQuery should be "DHCPREQUEST yiaddr: <IP offerred> siaddr: <address of this Server> dgiaddr: <defaul gateway set by server>
    				if(tokens[1].equals("yiaddr:")){
    					//if IP requested is available
    					if (tokens[2].equals(nextIPAdd())){
    						if (packetListFrame.isVisible()){
            		    		packetListFrame.addPacket(new PacketListUnit(this.getName(), host.getName(), "DHCP", PacketGenerator.createDHCPPacket(this.getMacAddress(), host.getMacAddress() , this.getInterfaces()[0].getIPAddress().toString(), IPAddress.BROADCAST, "67", "68", nextIPAdd(), this.getInterfaces()[0].getIPAddress().toString(), host.getMacAddress(), defaultGateway)));
            		    	}    						
    						response =  "DHCPACK yiaddr: " + nextIPAdd() + " siaddr: " + this.getInterfaces()[0].getIPAddress() + " dgiaddr: " + defaultGateway;
    						numOfUsers++;
    					//if IP requested is not available
    					}else{
    						response = "DHCPNACK siaddr: " + this.getInterfaces()[0].getIPAddress();
    					}
    				}
    			}
    			
    		}
    	}    	
    	return response;
    }
    
    public String nextIPAdd(){
    	String nextAdd = null;
    	String addressOctets[] = startIPAddress.split("\\.");
    	addressOctets[3] = String.valueOf( numOfUsers + Integer.valueOf(addressOctets[3]) );
    	nextAdd = addressOctets[0] + "." + addressOctets[1] + "." + addressOctets[2] + "." + addressOctets[3];
    	return nextAdd;
    }
    public String HTTPPort(Device host, String remoteQuery, PacketListFrame packetListFrame){
    	String response = "";
    	String tokens[];
    	
    	if (HTTPStatus){
    		response += "HTTP/1.1 ";
    		tokens = remoteQuery.split(" ");
    		if (tokens.length > 0){
    			if (tokens[0].equals("GET")){    				
    				if (tokens[1].equals(HTTPPath)){
    					response += "200 OK \n" + HTTPResource;
    				}else{
    					response += "404 Not Found \n" + HTTPNotFound;
    				}
    				if (packetListFrame.isVisible()){
    		    		packetListFrame.addPacket(new PacketListUnit(this.getName(), host.getName(), "HTTP", PacketGenerator.createHTTPPacket(this.getMacAddress(), host.getMacAddress(), this.getInterfaces()[0].getIPAddress().toString(), host.getInterfaces()[0].getIPAddress().toString(), "80", "1035", "1", "1", response)));
    		    	}
    			}
    			
    		}
    	}
    	return response;
    }
    
	
	/*
	 * Only Getters and Setters follow.
	 */
	
	public String getHTTPPath() {		
		return HTTPPath;
	}
	public String getHTTPResource() {
		return HTTPResource;
	}
	public boolean getHTTPStatus() {
		return HTTPStatus;
	}
	public void setHTTPPath(String newpath) {
		HTTPPath = newpath;
		
	}
	public void setHTTPResource(String r) {
		HTTPResource = r;
		
	}
	public void setHTTPStatus(boolean b) {		
		HTTPStatus = b;
	}
	public boolean getDHCPStatus() {
		return DHCPStatus;
	}
	public String getDNSServer() {
		return DNSServer;
	}
	public String getDefaultGateway() {
		return defaultGateway;
	}
	public int getMaximumUsers() {
		return maximumUsers;
	}
	public String getStartIPAddress() {
		return startIPAddress;
	}
	public void setDHCPStatus(boolean b) {
		DHCPStatus = b;
	}
	public void setDNSServer(String s) {
		DNSServer = s;
	}
	public void setDefaultGateway(String s) {
		defaultGateway = s;
	}
	public void setMaximumUsers(int i) {
		maximumUsers = i;
	}
	public void setStartIPAddress(String s) {
		startIPAddress = s;
	}
}
