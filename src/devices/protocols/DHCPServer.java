package devices.protocols;

import java.lang.String;

import platform.gui.PacketListFrame;
import devices.Device;

public interface DHCPServer {
	
	public String DHCPPort(Device d, String remoteQuery, PacketListFrame packetListFrame);
	public boolean getDHCPStatus();
	public void setDHCPStatus(boolean b);
	public String getDefaultGateway();
	public void setDefaultGateway(String s);
	public String getDNSServer();
	public void setDNSServer(String s);
	public String getStartIPAddress();
	public void setStartIPAddress(String s);
	public int getMaximumUsers();
	public void setMaximumUsers(int i);
}
