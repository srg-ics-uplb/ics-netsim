package devices.routers;

import devices.interfaces.FastEthernet;
import devices.interfaces.Interface;
import devices.interfaces.Serial;

import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;


public class Router2600 extends Router {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8001794335287189646L;
	private final FastEthernet fastEthernet0 = new FastEthernet(this, "FastEthernet0/0", "Fa0/0");
    private final FastEthernet fastEthernet1 = new FastEthernet(this, "FastEthernet0/1", "Fa0/1");
    private final Serial serial0 = new Serial(this, "Serial0", "s0");
    private final Serial serial1 = new Serial(this, "Serial1", "s1");
    private final Interface[] interfaces = {
        fastEthernet0, fastEthernet1, serial0, serial1
    };

    public Router2600(String name, Point location) {
        super(name, location);

        ClassLoader loader = getClass().getClassLoader();
        image = (new ImageIcon(loader.getResource("images/devices/router2600.gif"))).getImage();
        setInterfaces(interfaces);
    }

    public Image getImage() {
        return image;
    }
}
