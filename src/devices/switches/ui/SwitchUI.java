package devices.switches.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import platform.gui.MainFrame;
import devices.Device;
import devices.interfaces.Interface;
import devices.switches.Switch;
import devices.switches.Switch1900;
import devices.switches.Switch2950;
import devices.switches.consoles.SwitchConsole;
import devices.ui.DeviceUI;


public class SwitchUI extends DeviceUI implements ActionListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -7440495397697859176L;
	Switch switchName;

    public SwitchUI(Switch switchDevice) {
        super(switchDevice);
        setPopupMenu(popupMenu);
        initializePopupMenu();
        switchName = switchDevice;
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
    	if (getDevice() instanceof Switch1900){
	        MainFrame.SWITCH_1900_PORT_DIALOG.addOrRemove=1;
	        MainFrame.SWITCH_1900_PORT_DIALOG.interfaceCount=0;

	        addInterfaces = device.getOpenedInterfaces();
	        if (addInterfaces.length >= 0){
	        	MainFrame.SWITCH_1900_PORT_DIALOG.interfaceCount=addInterfaces.length;
	        	MainFrame.SWITCH_1900_PORT_DIALOG.setInterfaceArray();
	            for (int i = 0; i < addInterfaces.length; i++) {
	            	MainFrame.SWITCH_1900_PORT_DIALOG.setActiveHostInterface(addInterfaces[i], i);
	
	            }
	            MainFrame.SWITCH_1900_PORT_DIALOG.setLights();
	        } else MainFrame.SWITCH_1900_PORT_DIALOG.interfaceCount=-1;
    	}else if (getDevice() instanceof Switch2950){
	        MainFrame.SWITCH_2950_PORT_DIALOG.addOrRemove=1;
	        MainFrame.SWITCH_2950_PORT_DIALOG.interfaceCount=0;

	        addInterfaces = device.getOpenedInterfaces();
	        if (addInterfaces.length >= 0){
	        	MainFrame.SWITCH_2950_PORT_DIALOG.interfaceCount=addInterfaces.length;
	        	MainFrame.SWITCH_2950_PORT_DIALOG.setInterfaceArray();
	            for (int i = 0; i < addInterfaces.length; i++) {
	            	MainFrame.SWITCH_2950_PORT_DIALOG.setActiveHostInterface(addInterfaces[i], i);
	
	            }
	            
	            MainFrame.SWITCH_2950_PORT_DIALOG.setLights();
	        } else MainFrame.SWITCH_2950_PORT_DIALOG.interfaceCount=-1;
    	}
        
    }

    public void setRemoveInterfaces() {
    	if (getDevice() instanceof Switch1900){
	    	MainFrame.SWITCH_1900_PORT_DIALOG.addOrRemove=-1;
	    	MainFrame.SWITCH_1900_PORT_DIALOG.interfaceCount=0;
	    
	        removeInterfaces = device.getClosedInterfaces();
	        if (removeInterfaces.length >= 0){
		       	//System.out.println(removeInterfaces.length);
		        MainFrame.SWITCH_1900_PORT_DIALOG.interfaceCount=removeInterfaces.length;
		        MainFrame.SWITCH_1900_PORT_DIALOG.setInterfaceArray();
		        
		        for (int i = 0; i < removeInterfaces.length; i++) {
		        	MainFrame.SWITCH_1900_PORT_DIALOG.setActiveHostInterface(removeInterfaces[i], i);
		        }
		        MainFrame.SWITCH_1900_PORT_DIALOG.setLights();
	        } else MainFrame.SWITCH_1900_PORT_DIALOG.interfaceCount = -1;
    	}else if (getDevice() instanceof Switch2950){
	    	MainFrame.SWITCH_2950_PORT_DIALOG.addOrRemove=-1;
	    	MainFrame.SWITCH_2950_PORT_DIALOG.interfaceCount=0;
	    	
	        removeInterfaces = device.getClosedInterfaces();
	        if (removeInterfaces.length >= 0){
		       	//System.out.println(removeInterfaces.length);
		        MainFrame.SWITCH_2950_PORT_DIALOG.interfaceCount=removeInterfaces.length;
		        MainFrame.SWITCH_2950_PORT_DIALOG.setInterfaceArray();
		        
		        for (int i = 0; i < removeInterfaces.length; i++) {
		        	MainFrame.SWITCH_2950_PORT_DIALOG.setActiveHostInterface(removeInterfaces[i], i);
		        }
		        MainFrame.SWITCH_2950_PORT_DIALOG.setLights();
	        } else MainFrame.SWITCH_1900_PORT_DIALOG.interfaceCount = -1;
    		
    	}
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source.equals(console)) {
            SwitchConsole switchConsole = (SwitchConsole) getDevice().getConsole();
            switchConsole.showCentered();
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
            if (getDevice() instanceof Switch1900) {
                MainFrame.SWITCH1900_PROPERTY_DIALOG.setSwitch((Switch1900) getDevice());
                MainFrame.SWITCH1900_PROPERTY_DIALOG.showCentered();
            } else if (getDevice() instanceof Switch2950) {
                MainFrame.SWITCH2950_PROPERTY_DIALOG.setSwitch((Switch2950) getDevice());
                MainFrame.SWITCH2950_PROPERTY_DIALOG.showCentered();
            }
        } else if (source.equals(addConnection)) {
        	if (getDevice() instanceof Switch1900) {
	        	MainFrame.SWITCH_1900_PORT_DIALOG.hideAll();
	        	this.setAddInterfaces();
	        	MainFrame.SWITCH_1900_PORT_DIALOG.showCentered();
        	}else if (getDevice() instanceof Switch2950) {	
        		MainFrame.SWITCH_2950_PORT_DIALOG.hideAll();
	        	this.setAddInterfaces();
	        	MainFrame.SWITCH_2950_PORT_DIALOG.showCentered();
        	}

        } else if (source.equals(removeConnection)) {
        	if (getDevice() instanceof Switch1900) {
	        	MainFrame.SWITCH_1900_PORT_DIALOG.hideAll();
	        	this.setRemoveInterfaces();
	        	MainFrame.SWITCH_1900_PORT_DIALOG.showCentered();
        	}else if (getDevice() instanceof Switch2950) {	
        		MainFrame.SWITCH_2950_PORT_DIALOG.hideAll();
	        	this.setRemoveInterfaces();
	        	MainFrame.SWITCH_2950_PORT_DIALOG.showCentered();
        	}
        }
    }
}
