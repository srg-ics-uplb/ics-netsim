package devices.packets;

public class EthernetIPPacket extends EthernetPacket {

	public String dscp;
	public String id;
	public String fragOffset;
	public String ttl;
	public String pro;
	public String sourceIp;
	public String destIp;
	public String opt;
	
	public EthernetIPPacket(String srcMac, String dstMac, String srcIp, String dstIp){
		super(srcMac, dstMac);
		dscp = "0x0";
		id = "0x0";
		fragOffset = "0x0";
		ttl = "128";
		pro = "0x11";
		sourceIp = srcIp;
		destIp = dstIp;
		opt = "0x0";
	}
	
	public String getDscp(){
		return dscp;
	}
	public String getId(){
		return id;
	}
	public String getFragOffset(){
		return fragOffset;
	}
	public String getTtl(){
		return ttl;
	}
	public String getPro(){
		return pro;
	}
	public String getSourceIp(){
		return sourceIp;
	}
	public String getDestIp(){
		return destIp;
	}
	public String getOpt(){
		return opt;
	}
}
