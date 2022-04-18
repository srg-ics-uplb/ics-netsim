package devices.switches.vlan;

import java.io.Serializable;


public class VLAN implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1097154419117392671L;
	private final int index;
    private String name;
    private String status = "Enabled";
    private String type;
    private String[] ports;
    private int SAID = 100001;
    private int MTU = 1500;
    private int parent = 0;
    private int ringNo = 1;
    private int bridgeNo = 1;
    private String stp = "Unkn";
    private int trans1 = 0;
    private int trans2 = 0;

    public VLAN(int index) {
        this.index = index;
    }

    public VLAN(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public int getBridgeNo() {
        return bridgeNo;
    }

    public void setBridgeNo(int bridgeNo) {
        this.bridgeNo = bridgeNo;
    }

    public int getIndex() {
        return index;
    }

    public int getMTU() {
        return MTU;
    }

    public void setMTU(int mtu) {
        MTU = mtu;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParent() {
        return parent;
    }

    public void setParent(int parent) {
        this.parent = parent;
    }

    public String[] getPorts() {
        return ports;
    }

    public void setPorts(String[] ports) {
        this.ports = ports;
    }

    public int getRingNo() {
        return ringNo;
    }

    public void setRingNo(int ringNo) {
        this.ringNo = ringNo;
    }

    public int getSAID() {
        return SAID;
    }

    public void setSAID(int said) {
        SAID = said;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStp() {
        return stp;
    }

    public void setStp(String stp) {
        this.stp = stp;
    }

    public int getTrans1() {
        return trans1;
    }

    public void setTrans1(int trans1) {
        this.trans1 = trans1;
    }

    public int getTrans2() {
        return trans2;
    }

    public void setTrans2(int trans2) {
        this.trans2 = trans2;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
