package devices.routers.consoles;

import devices.consoles.Console;

import devices.routers.Router;

import platform.gui.MainFrame;


public class RouterConsole extends Console {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -3040133668493373999L;
	protected final static String INITIAL_MODE_PROMPT = "\n\n\nRouter con0 is now available\n\n\nPress RETURN to get started.\n\n\n";

    /***************************************************************************
     * DEVICES AND INTERFACES - TO BE CONFIGURED
     **************************************************************************/
    public RouterConsole(Router router, MainFrame frame) {
        super(router, frame);
    }

    public void setMode(int mode) {
        if (this instanceof Router2600Console) {
            ((Router2600Console) this).setMode(mode);
        }
    }

    public void processInput(String input) {
        if (this instanceof Router2600Console) {
            ((Router2600Console) this).processInput(input);
        }
    }
}
