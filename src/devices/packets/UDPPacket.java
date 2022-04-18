package devices.packets;

import devices.packets.EthernetIPPacket;

public class UDPPacket extends EthernetIPPacket {

	public String sourcePort;
	public String destPort;
	public String length;
	public String checksum;
	
	public UDPPacket(String srcMac, String dstMac, String srcIp, String dstIp, String sourcePort, String destPort){
		super(srcMac, dstMac, srcIp, dstIp);
		this.sourcePort = sourcePort;
		this.destPort = destPort;
		length = "0x30";
		checksum = "0x0";
	}
	
	public String getSourcePort(){
		return sourcePort;
	}
	public String getDestPort(){
		return destPort;
	}
	public String getLength(){
		return length;
	}
	public String getChecksum(){
		return checksum;
	}
}
