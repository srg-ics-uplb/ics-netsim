package devices.packets;

import devices.packets.EthernetIPPacket;

public class TCPPacket extends EthernetIPPacket {

	public String sourcePort;
	public String destPort;
	public String seqNum;
	public String ackNum;
	public String checksum;
	
	public TCPPacket(String srcMac, String dstMac, String srcIp, String dstIp, String sourcePort, String destPort, String seqNum, String ackNum) {
		super(srcMac, dstMac, srcIp, dstIp);
		this.sourcePort = sourcePort;
		this.destPort = destPort;
		this.seqNum = seqNum;
		this.ackNum = ackNum;
		this.checksum = "0x0";
	}

	public String getSourcePort(){
		return sourcePort;
	}
	public String getDestPort(){
		return destPort;
	}
	public String getSeqNum(){
		return seqNum;
	}
	public String getAckNum(){
		return ackNum;
	}
	public String getChecksum(){
		return checksum;
	}
}
