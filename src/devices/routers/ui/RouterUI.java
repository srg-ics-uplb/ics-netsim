package devices.routers.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import platform.gui.MainFrame;
import devices.Device;
import devices.interfaces.Interface;
import devices.routers.Router;
import devices.routers.Router2600;
import devices.ui.DeviceUI;


public class RouterUI extends DeviceUI implements ActionListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -6662000390599348503L;
	Router routerName;

    public RouterUI(Router router) {
        super(router);
        setPopupMenu(popupMenu);
        initializePopupMenu();
        routerName=router;
        console.addActionListener(this);
        delete.addActionListener(this);
        properties.addActionListener(this);
        addConnection.addActionListener(this);
        removeConnection.addActionListener(this);

    }

    public void initializePopupMenu() {
        popupMenu.add(console);
        popupMenu.addSeparator();
        popupMenu.add(addConnection);
        popupMenu.add(removeConnection);
        popupMenu.addSeparator();
        popupMenu.add(delete);
        popupMenu.addSeparator();
        popupMenu.add(properties);
    }

    public void setAddInterfaces() {
        MainFrame.ROUTER_2600_PORT_DIALOG.addOrRemove=1;
        MainFrame.ROUTER_2600_PORT_DIALOG.interfaceCount=0;
    	        addInterfaces = device.getOpenedInterfaces();
        if (addInterfaces.length >= 0){
        	MainFrame.ROUTER_2600_PORT_DIALOG.interfaceCount=addInterfaces.length;
        	MainFrame.ROUTER_2600_PORT_DIALOG.setInterfaceArray();
            for (int i = 0; i < addInterfaces.length; i++) {
            	MainFrame.ROUTER_2600_PORT_DIALOG.setActiveHostInterface(addInterfaces[i], i);

            }
            MainFrame.ROUTER_2600_PORT_DIALOG.setLights();
        } else MainFrame.ROUTER_2600_PORT_DIALOG.interfaceCount=-1;
        
    }

    public void setRemoveInterfaces() {
    	MainFrame.ROUTER_2600_PORT_DIALOG.addOrRemove=-1;
    	MainFrame.ROUTER_2600_PORT_DIALOG.interfaceCount=0;
    	
        removeInterfaces = device.getClosedInterfaces();
        if (removeInterfaces.length >= 0){
	       	System.out.println(removeInterfaces.length);
	        MainFrame.ROUTER_2600_PORT_DIALOG.interfaceCount=removeInterfaces.length;
	        MainFrame.ROUTER_2600_PORT_DIALOG.setInterfaceArray();
	        
	        for (int i = 0; i < removeInterfaces.length; i++) {
	        	MainFrame.ROUTER_2600_PORT_DIALOG.setActiveHostInterface(removeInterfaces[i], i);
	        }
	        MainFrame.ROUTER_2600_PORT_DIALOG.setLights();
        } else MainFrame.ROUTER_2600_PORT_DIALOG.interfaceCount = -1;
        
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source.equals(console)) {
            getDevice().getConsole().showCentered();
        } else if (source.equals(delete)) {
            Device device = getDevice();
            Interface[] interfaces = device.getClosedInterfaces();

            for (int i = 0; i < interfaces.length; i++) {
                MainFrame.DESIGNER_PANEL.removeConnection(interfaces[i]);
            }

            for (int i = 0; i < Device.DEVICES.size(); i++) {
                if (device == (Device) Device.DEVICES.get(i)) {
                    Device.DEVICES.remove(i);
                    MainFrame.DESIGNER_PANEL.repaint();

                    break;
                }
            }
        } else if (source.equals(properties)) {
            MainFrame.ROUTER_PROPERTY_DIALOG.setRouter((Router2600) getDevice());
            MainFrame.ROUTER_PROPERTY_DIALOG.showCentered();
        } else if (source.equals(addConnection)) {
        	MainFrame.ROUTER_2600_PORT_DIALOG.hideAll();
        	this.setAddInterfaces();
        	MainFrame.ROUTER_2600_PORT_DIALOG.showCentered();

        } else if (source.equals(removeConnection)) {
        	MainFrame.ROUTER_2600_PORT_DIALOG.hideAll();
            this.setRemoveInterfaces();
        	MainFrame.ROUTER_2600_PORT_DIALOG.showCentered();
        }
    }
}
