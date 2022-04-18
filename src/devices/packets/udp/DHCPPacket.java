package devices.packets.udp;

import devices.packets.UDPPacket;

public class DHCPPacket extends UDPPacket {

	public String op;
	public String xid;
	public String secs;
	public String ciaddr;
	public String yiaddr;
	public String siaddr;
	public String chaddr;
	public String giaddr;
	
	//Optional DHCP field, for default gateway setting
	public String dgiaddr;
	
	public DHCPPacket(String srcMac, String dstMac, String srcIp, String dstIp,	String sourcePort, String destPort, String yiaddr, String siaddr, String chaddr, String dgiaddr) {
		super(srcMac, dstMac, srcIp, dstIp, sourcePort, destPort);
		op = "0x5";
		this.ciaddr = "0.0.0.0";
		this.yiaddr = yiaddr; 
		this.siaddr = siaddr;
		this.giaddr = "0.0.0.0";
		this.chaddr = chaddr;
		
		this.dgiaddr = dgiaddr;
	}
	
	public String getOp(){
		return op;
	}
	public String getCiaddr(){
		return ciaddr;
	}
	public String getYiaddr(){
		return yiaddr;		
	}
	public String getSiaddr(){
		return siaddr;
	}
	public String getGiaddr(){
		return giaddr;
	}
	public String getChaddr(){
		return chaddr;
	}
	public String getDgiaddr(){
		return dgiaddr;
	}
}
