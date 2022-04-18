package platform.gui;
import devices.packets.*;

public class PacketListUnit {
	
	public String lastDevice;
	public String atDevice;
	public String type;
	public EthernetIPPacket packet;
	
	public PacketListUnit(String lastDevice, String atDevice, String type, EthernetIPPacket packet){
		this.lastDevice = lastDevice;
		this.atDevice = atDevice;
		this.type = type;
		this.packet = packet;
	}
	
	public void setLastDevice(String lastDevice){
		this.lastDevice = lastDevice;
	}
	
	public String getLastDevice(){
		return lastDevice;
	}
	
	public void setAtDevice(String atDevice){
		this.atDevice = atDevice;
	}
	
	public String getAtDevice(){
		return atDevice;
	}
	
	public void setType(String type){
		this.type = type;
	}
	
	public String getType(){
		return type;
	}
	
	public void setPacket(EthernetIPPacket packet){
		this.packet = packet;
	}
	
	public EthernetIPPacket getPacket(){
		return packet;
	}
}
