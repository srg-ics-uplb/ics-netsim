package devices.protocols;


import devices.Device;
import platform.gui.PacketListFrame;

import java.lang.String;

public interface HTTPServer {
	
	public String HTTPPort(Device d, String remoteQuery, PacketListFrame packetListFrame);
	public boolean getHTTPStatus();
	public void setHTTPStatus(boolean b);
	public String getHTTPPath();
	public void setHTTPPath(String newpath);
	//public void getHTTPPacket(Packet p); // Actually implement this someday, maybe.
	public void setHTTPResource(String r);
	public String getHTTPResource();
}


