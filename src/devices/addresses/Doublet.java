package devices.addresses;

public class Doublet extends Address {

	private static final long serialVersionUID = 2595333784070770438L;
	public String IPAdd;
	public String defaultGateway;
	
	public Doublet(String add, String defGateway) {
		IPAdd = add;
		defaultGateway = defGateway; 
	}
	
	public String getIPAddress(){
		return IPAdd;
	}
	
	public String getDefaultGateway(){
		return defaultGateway;
	}
	
	public void setIPAddress(String newAdd){
		IPAdd = newAdd;
	}
	
	public void setDefaultGateway(String newGateway){
		defaultGateway = newGateway;
	}
}
