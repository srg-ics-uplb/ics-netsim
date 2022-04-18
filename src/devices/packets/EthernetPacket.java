package devices.packets;

public class EthernetPacket {

	public String preamble;
	public String sourceMAC;
	public String destMAC;
	public String type;
	public String fcs;
	
	public EthernetPacket(String srcMac, String dstMac){
		preamble = "101010...1011";
		destMAC = dstMac;
		sourceMAC = srcMac;
		type = "0x800";
		fcs = "0x0";
	}
	
	public String getSrcMAC(){
		return sourceMAC;
	}
	public String getDestMAC(){
		return destMAC;
	}
	public String getType(){
		return type;
	}
	public String getFcs(){
		return fcs;
	}
}
