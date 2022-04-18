package devices.switches.consoles;

import devices.consoles.Console;

import devices.switches.Switch;

import platform.gui.MainFrame;


public class SwitchConsole extends Console {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = -7938913914711753790L;

	public SwitchConsole(Switch switchDevice, MainFrame frame) {
        super(switchDevice, frame);
    }

    public void setMode(int mode) {
        if (this instanceof Switch1900Console) {
            ((Switch1900Console) this).setMode(mode);
        } else if (this instanceof Switch2950Console) {
            ((Switch2950Console) this).setMode(mode);
        }
    }

    public void processInput(String input) {
        if (this instanceof Switch1900Console) {
            ((Switch1900Console) this).processInput(input);
        } else if (this instanceof Switch2950Console) {
            ((Switch2950Console) this).processInput(input);
        }
    }

    protected void showInvalidVLAN(int index) {
        textArea.append("\nError: Invalid VLAN " + index);
    }
}
