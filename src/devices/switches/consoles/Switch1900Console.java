package devices.switches.consoles;

import devices.addresses.IPAddress;

import devices.commands.Command;

import devices.interfaces.FastEthernet;
import devices.interfaces.Interface;

import devices.switches.Switch1900;

import devices.switches.consoles.commands.ConfigurationCommand;
import devices.switches.consoles.commands.InterfaceCommand;
import devices.switches.consoles.commands.PrivilegedCommand;
import devices.switches.consoles.commands.Switch1900Command;
import devices.switches.consoles.commands.UserCommand;

import devices.switches.vlan.VLAN;

import platform.gui.MainFrame;

import java.util.StringTokenizer;


public class Switch1900Console extends SwitchConsole {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 7278951351450736158L;
	protected final static String INITIAL_MODE_PROMPT = "\tCLI session with the switch is open.\n\tTo end the CLI session, enter [Exit].\n";
    private Switch1900 switch1900;

    public Switch1900Console(Switch1900 switch1900, MainFrame frame) {
        super(switch1900, frame);
        this.switch1900 = switch1900;
    }

    public void setMode(int mode) {
        currentMode = mode;

        if (currentMode == USER_MODE) {
            currentPrompt = switch1900.getName() + USER_MODE_PROMPT;
            availableCommands = Switch1900Command.USER_MODE_COMMANDS;
        } else if (currentMode == PRIVILEGED_MODE) {
            currentPrompt = switch1900.getName() + PRIVILEGED_MODE_PROMPT;
            availableCommands = Switch1900Command.PRIVILEGED_MODE_COMMANDS;
        } else if (currentMode == CONFIGURATION_MODE) {
            currentPrompt = switch1900.getName() + CONFIGURATION_MODE_PROMPT;
            availableCommands = Switch1900Command.CONFIGURATION_MODE_COMMANDS;
        } else if (currentMode == PASSWORD_MODE) {
            currentPrompt = PASSWORD_MODE_PROMPT;
            availableCommands = null;
        } else if (currentMode == INTERFACE_MODE) {
            currentPrompt = switch1900.getName() + INTERFACE_MODE_PROMPT;
            if (currentInterface instanceof FastEthernet) {
                availableCommands = Switch1900Command.INTERFACE_MODE_FASTETHERNET_COMMANDS;
            } else {
                availableCommands = Switch1900Command.INTERFACE_MODE_COMMANDS;
            }
        } else if (currentMode == INITIAL_MODE) {
            textArea.setText("");
            currentPrompt = INITIAL_MODE_PROMPT;
            availableCommands = null;
        }
    }

    /***************************************************************************
     * PROCESSES THE INPUT FROM THE USER... USUALLY TRIGGERED WHEN THE USER
     * PRESSES ENTER
     **************************************************************************/
    public void processInput(String input) {
        StringTokenizer tokens = new StringTokenizer(input, " ", true);
        int position = 0;
        String command = null;

        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();

            if (!token.equals(" ")) {
                command = token;

                break;
            }

            position++;
        }

        if (command != null) {
            if (currentMode == USER_MODE) {
                processUserCommand(command, tokens, position);
            } else if (currentMode == PRIVILEGED_MODE) {
                processPrivilegedCommand(command, tokens, position);
            } else if (currentMode == CONFIGURATION_MODE) {
                processConfigurationCommand(command, tokens, position);
            } else if (currentMode == INTERFACE_MODE) {
                processInterfaceCommand(command, tokens, position);
            } else if (currentMode == PASSWORD_MODE) {
                processPasswordCommand(command, tokens, position);
            }
        } else {
            if (currentMode == PASSWORD_MODE) {
                processPasswordCommand("", tokens, position);
            }
        }
    }

    private void processUserCommand(String input, StringTokenizer tokens, int position) {
        int cursorPosition = position;
        Command command = getFullCommand(input, availableCommands, cursorPosition);

        if (command != null) {
            if (command.equals(UserCommand.ENABLE)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() == 0) {
                    if (switch1900.isPasswordEnabled() || switch1900.isSecretEnabled()) {
                        setMode(PASSWORD_MODE);
                    } else {
                        setMode(PRIVILEGED_MODE);
                    }
                } else {
                    showInvalidInputError(cursorPosition);
                }
            } else if (command.equals(UserCommand.EXIT)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() == 0) {
                    setMode(INITIAL_MODE);
                } else {
                    showInvalidInputError(cursorPosition);
                }
            
            }else if (command.equals(UserCommand.PING)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    if (isValidQuartet(arg.toString(), cursorPosition)) {
                        StringBuffer extras = new StringBuffer();
                        int extrasPosition = getNextPosition(tokens, extras);
                        cursorPosition += (arg.length() + extrasPosition);

                        if (extras.length() == 0) {
                            IPAddress destination = new IPAddress(arg.toString());

                            if (deviceReachable(getDevice(), destination)) {
                                showReply(destination);
                            } else {
                                showRequestTimedOut(destination);
                            }
                        } else {
                            showInvalidInputError(cursorPosition);
                        }
                    }
                } else {
                    showIncompleteCommandError();
                }
            }else if (command.equals(UserCommand.TRACERT)){

                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    if (isValidQuartet(arg.toString(), cursorPosition)) {
                        StringBuffer extras = new StringBuffer();
                        int extrasPosition = getNextPosition(tokens, extras);
                        cursorPosition += (arg.length() + extrasPosition);

                        if (extras.length() == 0) {
                            IPAddress destination = new IPAddress(arg.toString());

                            if (deviceTraceable(getDevice(), destination)) {
                                showTraceReply(destination,1);
                            } else {
                            	showTraceReply(destination,2);
                            }
                        } else {
                            showInvalidInputError(cursorPosition);
                        }
                    }
                } else {
                    showIncompleteCommandError();
                }
            	
            	
            }
        }
    }

    private void processPrivilegedCommand(String input, StringTokenizer tokens, int position) {
        int cursorPosition = position;
        Command command = getFullCommand(input, availableCommands, cursorPosition);

        if (command != null) {
            if (command.equals(PrivilegedCommand.CONFIGURE)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Switch1900Command.PRIVILEGED_CONFIGURE_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(PrivilegedCommand.CONFIGURE_TERMINAL)) {
                            StringBuffer extras = new StringBuffer();
                            int extrasPosition = getNextPosition(tokens, extras);
                            cursorPosition = arg.length() + extrasPosition;

                            if (extras.length() != 0) {
                                showInvalidInputError(cursorPosition);
                            } else {
                                setMode(CONFIGURATION_MODE);
                            }
                        }
                    }
                } else {
                    showIncompleteCommandError();
                }
            } else if (command.equals(PrivilegedCommand.DISABLE)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() == 0) {
                    setMode(USER_MODE);
                } else {
                    showInvalidInputError(cursorPosition);
                }
            } else if (command.equals(PrivilegedCommand.EXIT)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() == 0) {
                    setMode(INITIAL_MODE);
                } else {
                    showInvalidInputError(cursorPosition);
                }
            } else if (command.equals(PrivilegedCommand.SHOW)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Switch1900Command.PRIVILEGED_SHOW_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(PrivilegedCommand.SHOW_IP)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() == 0) {
                                showIP();
                            } else {
                                showInvalidInputError(cursorPosition);
                            }
                        } else if (argument.equals(PrivilegedCommand.SHOW_RUNNING_CONFIG)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() == 0) {
                                showRunningConfig();
                            } else {
                                showInvalidInputError(cursorPosition);
                            }
                        }
                    }
                } else {
                    showIncompleteCommandError();
                }
            }
        }
    }

    private void processConfigurationCommand(String input, StringTokenizer tokens, int position) {
        int cursorPosition = position;
        Command command = getFullCommand(input, availableCommands, cursorPosition);

        if (command != null) {
            if (command.equals(ConfigurationCommand.ENABLE)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Switch1900Command.CONFIGURATION_ENABLE_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(ConfigurationCommand.ENABLE_PASSWORD)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() != 0) {
                                Command argument2 = getFullCommand(arg2.toString(), Switch1900Command.CONFIGURATION_ENABLE_PASSWORD_ARGUMENTS, cursorPosition);

                                if (argument2 != null) {
                                    StringBuffer arg3 = new StringBuffer();
                                    int arg3Position = getNextPosition(tokens, arg3);
                                    cursorPosition += (arg2.length() + arg3Position);

                                    if (arg3.length() != 0) {
                                        Command argument3 = getFullCommand(arg3.toString(), Switch1900Command.CONFIGURATION_ENABLE_PASSWORD_LEVEL_ARGUMENTS, cursorPosition);

                                        if (argument3 != null) {
                                            StringBuffer password = new StringBuffer();
                                            int passwordPosition = getNextPosition(tokens, password);
                                            cursorPosition += (arg3.length() + passwordPosition);

                                            if (password.length() != 0) {
                                                StringBuffer extras = new StringBuffer();
                                                int extrasPosition = getNextPosition(tokens, extras);
                                                cursorPosition += (password.length() + extrasPosition);

                                                if (extrasPosition == 0) {
                                                    switch1900.setPassword(password.toString());
                                                } else {
                                                    showInvalidInputError(cursorPosition);
                                                }
                                            } else {
                                                showIncompleteCommandError();
                                            }
                                        }
                                    } else {
                                        showIncompleteCommandError();
                                    }
                                }
                            } else {
                                showIncompleteCommandError();
                            }
                        }

                        if (argument.equals(ConfigurationCommand.ENABLE_SECRET)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() != 0) {
                                Command argument2 = getFullCommand(arg2.toString(), Switch1900Command.CONFIGURATION_ENABLE_SECRET_ARGUMENTS, cursorPosition);

                                if (argument2 != null) {
                                    StringBuffer arg3 = new StringBuffer();
                                    int arg3Position = getNextPosition(tokens, arg3);
                                    cursorPosition += (arg2.length() + arg3Position);

                                    if (arg3.length() != 0) {
                                        Command argument3 = getFullCommand(arg3.toString(), Switch1900Command.CONFIGURATION_ENABLE_SECRET_LEVEL_ARGUMENTS, cursorPosition);

                                        if (argument3 != null) {
                                            StringBuffer secret = new StringBuffer();
                                            int secretPosition = getNextPosition(tokens, secret);
                                            cursorPosition += (arg3.length() + secretPosition);

                                            if (secret.length() != 0) {
                                                StringBuffer extras = new StringBuffer();
                                                int extrasPosition = getNextPosition(tokens, extras);
                                                cursorPosition += (secret.length() + extrasPosition);

                                                if (extrasPosition == 0) {
                                                    switch1900.setSecret(secret.toString());
                                                } else {
                                                    showInvalidInputError(cursorPosition);
                                                }
                                            } else {
                                                showIncompleteCommandError();
                                            }
                                        }
                                    } else {
                                        showIncompleteCommandError();
                                    }
                                }
                            } else {
                                showIncompleteCommandError();
                            }
                        }
                    }
                } else {
                    showIncompleteCommandError();
                }
            } else if (command.equals(ConfigurationCommand.EXIT)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() == 0) {
                    setMode(PRIVILEGED_MODE);
                } else {
                    showInvalidInputError(cursorPosition);
                }
            } else if (command.equals(ConfigurationCommand.HOSTNAME)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    StringBuffer extras = new StringBuffer();
                    int extrasPosition = getNextPosition(tokens, extras);
                    cursorPosition += (arg.length() + extrasPosition);

                    if (extras.length() != 0) {
                        showInvalidInputError(position + input.length() + argPosition + arg.length() + extrasPosition);
                    } else {
                        switch1900.setName(arg.toString());
                        setMode(currentMode);
                    }
                } else {
                    showIncompleteCommandError();
                }
            } else if (command.equals(ConfigurationCommand.IP)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Switch1900Command.CONFIGURATION_IP_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(ConfigurationCommand.IP_ADDRESS)) {
                            StringBuffer ipAddress = new StringBuffer();
                            int ipAddressPosition = getNextPosition(tokens, ipAddress);
                            cursorPosition += (arg.length() + ipAddressPosition);

                            if (ipAddress.length() != 0) {
                                if (isValidQuartet(ipAddress.toString(), cursorPosition)) {
                                    StringBuffer subnet = new StringBuffer();
                                    int subnetPosition = getNextPosition(tokens, subnet);
                                    cursorPosition += (ipAddress.length() + subnetPosition);

                                    if (subnet.length() != 0) {
                                        if (isValidQuartet(subnet.toString(), cursorPosition)) {
                                            StringBuffer extras = new StringBuffer();
                                            int extrasPosition = getNextPosition(tokens, extras);
                                            cursorPosition += (subnet.length() + extrasPosition);

                                            if (extras.length() == 0) {
                                                int[] ipAddressDecimals = IPAddress.getQuartet(ipAddress.toString());
                                                int[] subnetDecimals = IPAddress.getQuartet(subnet.toString());
                                                switch1900.setIPAddress(ipAddressDecimals);
                                                switch1900.setSubnetMask(subnetDecimals);
                                            } else {
                                                showInvalidInputError(cursorPosition);
                                            }
                                        }
                                    } else {
                                        showIncompleteCommandError();
                                    }
                                }
                            } else {
                                showIncompleteCommandError();
                            }
                        } else if (argument.equals(ConfigurationCommand.IP_DEFAULT_GATEWAY)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() != 0) {
                                if (isValidQuartet(arg2.toString(), cursorPosition)) {
                                    StringBuffer extras = new StringBuffer();
                                    int extrasPosition = getNextPosition(tokens, extras);
                                    cursorPosition += (arg2.length() + extrasPosition);

                                    if (extras.length() == 0) {
                                        switch1900.setDefaultGateway(arg2.toString());
                                    } else {
                                        showInvalidInputError(cursorPosition);
                                    }
                                }
                            } else {
                                showIncompleteCommandError();
                            }
                        }
                    }
                } else {
                    showIncompleteCommandError();
                }
            } else if (command.equals(ConfigurationCommand.INTERFACE)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Interface selectedInterface = getInterfaceFromName(arg.toString(), cursorPosition);

                    if (selectedInterface != null) {
                        StringBuffer extras = new StringBuffer();
                        int extrasPosition = getNextPosition(tokens, extras);
                        cursorPosition += (arg.length() + extrasPosition);

                        if (extras.length() != 0) {
                            showInvalidInputError(cursorPosition);
                        } else {
                            setCurrentInterface(selectedInterface);
                            setMode(INTERFACE_MODE);
                        }
                    }
                } else {
                    showIncompleteCommandError();
                }
            } else if (command.equals(ConfigurationCommand.VLAN)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = null;

                    if (!Character.isDigit(arg.charAt(0))) {
                        argument = getFullCommand(arg.toString(), Switch1900Command.CONFIGURATION_VLAN_ARGUMENTS, cursorPosition);
                    }

                    if (argument != null) {
                    } else {
                        if (isInteger(arg.toString(), cursorPosition)) {
                            int vlanIndex = Integer.parseInt(arg.toString());

                            if (vlanIndex < 1) {
                                showInvalidInputError(cursorPosition);
                            } else if (vlanIndex > 1001) {
                                showInvalidInputError(cursorPosition + 3);
                            } else {
                                StringBuffer arg2 = new StringBuffer();
                                int arg2Position = getNextPosition(tokens, arg2);
                                cursorPosition += (arg.length() + arg2Position);

                                if (arg2.length() != 0) {
                                    Command argument2 = getFullCommand(arg2.toString(), Switch1900Command.CONFIGURATION_VLAN_1_1001_ARGUMENTS, cursorPosition);

                                    if (argument2 != null) {
                                        if (argument2.equals(ConfigurationCommand.VLAN_1_1001_NAME)) {
                                            StringBuffer name = new StringBuffer();
                                            int namePosition = getNextPosition(tokens, name);
                                            cursorPosition += (arg2.length() + namePosition);

                                            if (name.length() != 0) {
                                                StringBuffer extras = new StringBuffer();
                                                int extrasPosition = getNextPosition(tokens, extras);
                                                cursorPosition += (name.length() + extrasPosition);

                                                if (extras.length() == 0) {
                                                    VLAN[] vlans = switch1900.getVLANS();
                                                    boolean vlanAlreadyExists = false;

                                                    for (int i = 0;
                                                            i < vlans.length;
                                                            i++) {
                                                        if (vlans[i].getIndex() == vlanIndex) {
                                                            vlans[i].setName(name.toString());
                                                            vlanAlreadyExists = true;
                                                        }
                                                    }

                                                    if (!vlanAlreadyExists) {
                                                        switch1900.addVLAN(new VLAN(vlanIndex, name.toString()));
                                                    }
                                                } else {
                                                    showInvalidInputError(cursorPosition);
                                                }
                                            } else {
                                                showIncompleteCommandError();
                                            }
                                        }
                                    }
                                } else {
                                    showIncompleteCommandError();
                                }
                            }
                        }
                    }
                } else {
                    showIncompleteCommandError();
                }
            }
        }
    }

    private void processInterfaceCommand(String input, StringTokenizer tokens, int position) {
        int cursorPosition = position;
        Command command = getFullCommand(input, availableCommands, cursorPosition);

        if (command != null) {
        	if (command.equals(InterfaceCommand.TRUNK)){
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += input.length() + argPosition;                
                if (arg.length() != 0){
                	Command argument = getFullCommand(arg.toString(), Switch1900Command.INTERFACE_TRUNK_ARGUMENTS, cursorPosition);
                	if (argument.equals(InterfaceCommand.TRUNK_OFF)){
                		StringBuffer arg2 = new StringBuffer();
                		int arg2Position = getNextPosition(tokens, arg2);
                		cursorPosition += arg.length() + arg2Position;
                		if (arg2.length() == 0){
                			((FastEthernet)currentInterface).setTrunked(false);                			
                		}else{
                			showInvalidInputError(cursorPosition);
                		}
                	}else if (argument.equals(InterfaceCommand.TRUNK_ON)){
                		StringBuffer arg2 = new StringBuffer();
                		int arg2Position = getNextPosition(tokens, arg2);
                		cursorPosition += arg.length() + arg2Position;
                		if (arg2.length() == 0){
                			((FastEthernet)currentInterface).setTrunked(true);                			
                		}else{
                			showInvalidInputError(cursorPosition);
                		}
                	}
                }else{
                	showIncompleteCommandError();
                }                
        	}else if (command.equals(InterfaceCommand.VLAN_MEMBERSHIP)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += input.length() + argPosition;

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Switch1900Command.INTERFACE_VLAN_MEMBERSHIP_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(InterfaceCommand.VLAN_MEMBERSHIP_STATIC)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() != 0) {
                                if (isInteger(arg2.toString(), cursorPosition)) {
                                    int vlanIndex = Integer.parseInt(arg2.toString());

                                    if (vlanIndex < 1) {
                                        showInvalidInputError(cursorPosition);
                                    } else if (vlanIndex > 105) {
                                        showInvalidInputError(cursorPosition + 2);
                                    } else {
                                        StringBuffer extras = new StringBuffer();
                                        int extrasPosition = getNextPosition(tokens, extras);
                                        cursorPosition += (arg2.length() + extrasPosition);

                                        if (extras.length() == 0) {
                                            VLAN[] vlans = switch1900.getVLANS();
                                            VLAN portVLAN = null;

                                            for (int i = 0; i < vlans.length;
                                                    i++) {
                                                int index = vlans[i].getIndex();

                                                if (index == vlanIndex) {
                                                    portVLAN = vlans[i];
                                                    currentInterface.getSwitchPort().setVLAN(portVLAN);
                                                }
                                            }

                                            if (portVLAN == null) {
                                                showInvalidVLAN(vlanIndex);
                                            }
                                        } else {
                                            showInvalidInputError(cursorPosition);
                                        }
                                    }
                                }
                            } else {
                                showIncompleteCommandError();
                            }
                        }
                    }
                } else {
                    showIncompleteCommandError();
                }
            }
        }
    }

    private void processPasswordCommand(String input, StringTokenizer tokens, int position) {
        if (switch1900.isSecretEnabled()) {
            if (input.equals(switch1900.getSecret())) {
                setMode(PRIVILEGED_MODE);
            } else {
                passwordTries++;
            }
        } else if (switch1900.isPasswordEnabled()) {
            if (input.equals(switch1900.getPassword())) {
                setMode(PRIVILEGED_MODE);
            } else {
                passwordTries++;
            }
        }

        if (passwordTries == 3) {
            setMode(USER_MODE);
            passwordTries = 0;
        }
    }

    private void showIP() {
        textArea.append("\n");
        textArea.append("IP Address: ");

        if (!switch1900.getIPAddress().toString().equals("0.0.0.0")) {
            textArea.append("" + switch1900.getIPAddress().toString());
        }

        textArea.append("\n");
        textArea.append("Subnet Mask: ");
        //      TODO     if (!switch1900.getSubnetMask().toString().equals("0.0.0.0")){
        //      TODO     	textArea.append("" + switch1900.getIPAddress().toString());
        //      TODO     }
        textArea.append("\n");
        textArea.append("Default Gateway: " + switch1900.getDefaultGateway() + "\n");
        textArea.append("Management VLAN: 1\n");
        textArea.append("Domain Name:\n");
        textArea.append("Name server 1: 0.0.0.0\n");
        textArea.append("Name server 2: 0.0.0.0\n");
        textArea.append("HTTP server: Enabled\n");
        textArea.append("HTTP port: 80\n");
        textArea.append("RIP: Enabled\n");
    }

    private void showRunningConfig() {
        Interface[] interfaces = switch1900.getInterfaces();
        textArea.append("\n");
        textArea.append("Building configuration...\n");
        textArea.append("Current configuration:\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("hostname " + "\"" + switch1900.getName() + "\"\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");

        // TODO   	if ((!switch1900.getIPAddress().toString().equalsIgnoreCase("0.0.0.0"))||(!switch1900.getSubnetMask().toString().equalsIgnoreCase("0.0.0.0"))){
        //    	 TODO  		textArea.append("ip address " + switch1900.getIPAddress().toString() + " " + switch1900.getSubnetMask().toString() + "\n");	
        //    	 TODO 	}
        if (!switch1900.getDefaultGateway().equals("")) {
            textArea.append("ip default-gateway " + switch1900.getDefaultGateway() + "\n");
        }

        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");

        if (switch1900.getSecret().length() != 0) {
            textArea.append("enable secret " + switch1900.getSecret().length() + " <insert encrypted password here!>\n");
        }

        textArea.append("!\n");

        for (int i = 0; i < interfaces.length; i++) {
            //    		 TODO   		textArea.append("interface " + interfaces[i].getName() + "\n");
            textArea.append("\n");
            textArea.append("!\n");

            //    		 TODO   		if (i>=interfaces.length-2){
            //    		 TODO  			textArea.append("!\n");
            //    		 TODO   		}
        }

        textArea.append("line console\n");
        textArea.append("!\n");
        textArea.append("End\n");
    }
}
