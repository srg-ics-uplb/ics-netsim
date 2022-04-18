package devices;

public class Packet {
	public String lastDevice;
	public String atDevice;
	public String type;
	public String bits;
	
	public Packet(String ld, String ad, String t, String b){
		setLastDevice(ld);
		setAtDevice(ad);
		setType(t);
		setBits(b);		
	}	
	
	public String getLastDevice(){
		return lastDevice;
	}
	public String getAtDevice(){
		return atDevice;
	}
	public String getType(){
		return type;
	}
	public String getBits(){
		return bits;
	}
	
	public void setLastDevice(String ld){
		lastDevice = ld;
	}
	public void setAtDevice(String ad){
		atDevice = ad;
	}
	public void setType(String t){
		type = t;
	}
	public void setBits(String b){
		bits = b;
	}
	
}
