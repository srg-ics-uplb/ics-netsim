package devices.switches;

import devices.interfaces.Ethernet;
import devices.interfaces.FastEthernet;
import devices.interfaces.Interface;

import devices.switches.ports.SwitchPort;

import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;


public class Switch1900 extends Switch {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -5986055914229007144L;
	private final Ethernet ethernet1 = new Ethernet(this, "Ethernet0/1", "ethernet1");
    private final SwitchPort port1 = new SwitchPort(ethernet1, "1");
    private final Ethernet ethernet2 = new Ethernet(this, "Ethernet0/2", "ethernet2");
    private final SwitchPort port2 = new SwitchPort(ethernet2, "2");
    private final Ethernet ethernet3 = new Ethernet(this, "Ethernet0/3", "ethernet3");
    private final SwitchPort port3 = new SwitchPort(ethernet3, "3");
    private final Ethernet ethernet4 = new Ethernet(this, "Ethernet0/4", "ethernet4");
    private final SwitchPort port4 = new SwitchPort(ethernet4, "4");
    private final Ethernet ethernet5 = new Ethernet(this, "Ethernet0/5", "ethernet5");
    private final SwitchPort port5 = new SwitchPort(ethernet5, "5");
    private final Ethernet ethernet6 = new Ethernet(this, "Ethernet0/6", "ethernet6");
    private final SwitchPort port6 = new SwitchPort(ethernet6, "6");
    private final Ethernet ethernet7 = new Ethernet(this, "Ethernet0/7", "ethernet7");
    private final SwitchPort port7 = new SwitchPort(ethernet7, "7");
    private final Ethernet ethernet8 = new Ethernet(this, "Ethernet0/8", "ethernet8");
    private final SwitchPort port8 = new SwitchPort(ethernet8, "8");
    private final Ethernet ethernet9 = new Ethernet(this, "Ethernet0/9", "ethernet9");
    private final SwitchPort port9 = new SwitchPort(ethernet9, "9");
    private final Ethernet ethernet10 = new Ethernet(this, "Ethernet0/10", "ethernet10");
    private final SwitchPort port10 = new SwitchPort(ethernet10, "10");
    private final Ethernet ethernet11 = new Ethernet(this, "Ethernet0/11", "ethernet11");
    private final SwitchPort port11 = new SwitchPort(ethernet11, "11");
    private final Ethernet ethernet12 = new Ethernet(this, "Ethernet0/12", "ethernet12");
    private final SwitchPort port12 = new SwitchPort(ethernet12, "12");
    private final FastEthernet fastEthernet1 = new FastEthernet(this, "FastEthernet0/1", "Fa0/1");
    private final SwitchPort portA = new SwitchPort(fastEthernet1, "A");
    private final FastEthernet fastEthernet2 = new FastEthernet(this, "FastEthernet0/2", "Fa0/2");
    private final SwitchPort portB = new SwitchPort(fastEthernet2, "B");
    private final Interface[] interfaces = {
        ethernet1, ethernet2, ethernet3, ethernet4, ethernet5, ethernet6,
        ethernet7, ethernet8, ethernet9, ethernet10, ethernet11, ethernet12,
        fastEthernet1, fastEthernet2
    };
    private final SwitchPort[] ports = {
        port1, port2, port3, port4, port5, port6, port7, port8, port9, port10,
        port11, port12, portA, portB
    };

    public Switch1900(String name, Point location) {
        super(name, location);

        ClassLoader loader = getClass().getClassLoader();
        image = (new ImageIcon(loader.getResource("images/devices/switch1900.gif"))).getImage();
        super.setInterfaces(interfaces);
        setPorts(ports);
    }

    public Image getImage() {
        return image;
    }
}
