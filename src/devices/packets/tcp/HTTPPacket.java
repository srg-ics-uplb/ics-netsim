package devices.packets.tcp;

import devices.packets.TCPPacket;

public class HTTPPacket extends TCPPacket {

	public String HTTPData;
	
	public HTTPPacket(String srcMac, String dstMac, String srcIp, String dstIp,	String sourcePort, String destPort, String seqNum, String ackNum, String HTTPData) {
		super(srcMac, dstMac, srcIp, dstIp, sourcePort, destPort, seqNum, ackNum);
		this.HTTPData = HTTPData;
	}
	
	public String getHTTPData(){
		return HTTPData;
	}
	
}
