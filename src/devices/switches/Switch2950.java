package devices.switches;

import devices.interfaces.FastEthernet;
import devices.interfaces.Interface;

import devices.switches.ports.SwitchPort;

import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;


public class Switch2950 extends Switch {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -4687820612182413151L;
	private final FastEthernet fastEthernet1 = new FastEthernet(this, "FastEthernet0/1", "Fa0/1");
    private final SwitchPort port1 = new SwitchPort(fastEthernet1, "1");
    private final FastEthernet fastEthernet2 = new FastEthernet(this, "FastEthernet0/2", "Fa0/2");
    private final SwitchPort port2 = new SwitchPort(fastEthernet2, "2");
    private final FastEthernet fastEthernet3 = new FastEthernet(this, "FastEthernet0/3", "Fa0/3");
    private final SwitchPort port3 = new SwitchPort(fastEthernet3, "3");
    private final FastEthernet fastEthernet4 = new FastEthernet(this, "FastEthernet0/4", "Fa0/4");
    private final SwitchPort port4 = new SwitchPort(fastEthernet4, "4");
    private final FastEthernet fastEthernet5 = new FastEthernet(this, "FastEthernet0/5", "Fa0/5");
    private final SwitchPort port5 = new SwitchPort(fastEthernet5, "5");
    private final FastEthernet fastEthernet6 = new FastEthernet(this, "FastEthernet0/6", "Fa0/6");
    private final SwitchPort port6 = new SwitchPort(fastEthernet6, "6");
    private final FastEthernet fastEthernet7 = new FastEthernet(this, "FastEthernet0/7", "Fa0/7");
    private final SwitchPort port7 = new SwitchPort(fastEthernet7, "7");
    private final FastEthernet fastEthernet8 = new FastEthernet(this, "FastEthernet0/8", "Fa0/8");
    private final SwitchPort port8 = new SwitchPort(fastEthernet8, "8");
    private final FastEthernet fastEthernet9 = new FastEthernet(this, "FastEthernet0/9", "Fa0/9");
    private final SwitchPort port9 = new SwitchPort(fastEthernet9, "9");
    private final FastEthernet fastEthernet10 = new FastEthernet(this, "FastEthernet0/10", "Fa0/10");
    private final SwitchPort port10 = new SwitchPort(fastEthernet10, "10");
    private final FastEthernet fastEthernet11 = new FastEthernet(this, "FastEthernet0/11", "Fa0/11");
    private final SwitchPort port11 = new SwitchPort(fastEthernet11, "11");
    private final FastEthernet fastEthernet12 = new FastEthernet(this, "FastEthernet0/12", "Fa0/12");
    private final SwitchPort port12 = new SwitchPort(fastEthernet12, "12");
    private final Interface[] interfaces = {
        fastEthernet1, fastEthernet2, fastEthernet3, fastEthernet4,
        fastEthernet5, fastEthernet6, fastEthernet7, fastEthernet8,
        fastEthernet9, fastEthernet10, fastEthernet11, fastEthernet12
    };
    private final SwitchPort[] ports = {
        port1, port2, port3, port4, port5, port6, port7, port8, port9, port10,
        port11, port12
    };

    public Switch2950(String name, Point location) {
        super(name, location);

        ClassLoader loader = getClass().getClassLoader();
        image = (new ImageIcon(loader.getResource("images/devices/switch2950.gif"))).getImage();
        super.setInterfaces(interfaces);
        setPorts(ports);
    }

    public Image getImage() {
        return image;
    }
}
