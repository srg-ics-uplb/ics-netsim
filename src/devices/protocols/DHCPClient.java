package devices.protocols;

import platform.gui.PacketListFrame;
import devices.addresses.Doublet;
import devices.servers.Server;

public interface DHCPClient {
	
	public Doublet requestDHCP(Server s, PacketListFrame packetListFrame);
}
