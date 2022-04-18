package platform.gui;

import devices.packets.udp.*;
import devices.packets.tcp.*;


public class PacketGenerator {

	
	public static DHCPPacket createDHCPPacket(String srcMac, String dstMac, String srcIp, String dstIp, String sourcePort, String destPort, String yiaddr, String siaddr, String chaddr, String dgiaddr){
		return new DHCPPacket(srcMac, dstMac, srcIp, dstIp,	sourcePort, destPort, yiaddr, siaddr, chaddr, dgiaddr);
	}
	
	public static HTTPPacket createHTTPPacket(String srcMac, String dstMac, String srcIp, String dstIp, String sourcePort, String destPort, String seqNum, String ackNum, String HTTPData){
		return new HTTPPacket(srcMac, dstMac, srcIp, dstIp,	sourcePort, destPort, seqNum, ackNum, HTTPData);
	}
}
