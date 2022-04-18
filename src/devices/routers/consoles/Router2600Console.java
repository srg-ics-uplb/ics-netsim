package devices.routers.consoles;

import devices.addresses.IPAddress;

import devices.commands.Command;

import devices.interfaces.Interface;

import devices.routers.Router2600;

import devices.routers.accesslists.ExtendedIPAccessList;
import devices.routers.accesslists.ExtendedIPPermission;
import devices.routers.accesslists.IPAccessList;
import devices.routers.accesslists.StandardIPAccessList;
import devices.routers.accesslists.StandardIPPermission;

import devices.routers.consoles.commands.ConfigurationCommand;
import devices.routers.consoles.commands.InterfaceCommand;
import devices.routers.consoles.commands.PrivilegedCommand;
import devices.routers.consoles.commands.Router2600Command;
import devices.routers.consoles.commands.RouterCommand;
import devices.routers.consoles.commands.UserCommand;

import devices.routers.routingprotocols.IGRP;
import devices.routers.routingprotocols.RoutingProtocol;

import devices.routers.routingtable.Entry;
import devices.routers.routingtable.RoutingTable;

import platform.gui.MainFrame;

import java.util.Calendar;
import java.util.StringTokenizer;


public class Router2600Console extends RouterConsole {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 8371734100652504979L;
	private Router2600 router2600;

    public Router2600Console(Router2600 router2600, MainFrame frame) {
        super(router2600, frame);
        this.router2600 = router2600;
    }

    /***************************************************************************
     * VARIABLE SETTERS / UPDATE
     **************************************************************************/
    public void setMode(int mode) {
        currentMode = mode;

        if (currentMode == USER_MODE) {
            currentPrompt = router2600.getName() + USER_MODE_PROMPT;
            availableCommands = Router2600Command.USER_MODE_COMMANDS;
        } else if (currentMode == PRIVILEGED_MODE) {
            currentPrompt = router2600.getName() + PRIVILEGED_MODE_PROMPT;
            availableCommands = Router2600Command.PRIVILEGED_MODE_COMMANDS;
        } else if (currentMode == CONFIGURATION_MODE) {
            currentPrompt = router2600.getName() + CONFIGURATION_MODE_PROMPT;
            availableCommands = Router2600Command.CONFIGURATION_MODE_COMMANDS;
        } else if (currentMode == PASSWORD_MODE) {
            currentPrompt = PASSWORD_MODE_PROMPT;
            availableCommands = null;
        } else if (currentMode == INTERFACE_MODE) {
            currentPrompt = router2600.getName() + INTERFACE_MODE_PROMPT;
            availableCommands = Router2600Command.INTERFACE_MODE_COMMANDS;
        } else if (currentMode == INITIAL_MODE) {
            textArea.setText("");
            currentPrompt = INITIAL_MODE_PROMPT;
            availableCommands = null;
        } else if (currentMode == ROUTER_MODE) {
            currentPrompt = router2600.getName() + ROUTER_MODE_PROMPT;
            availableCommands = Router2600Command.ROUTER_MODE_COMMANDS;
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
            } else if (currentMode == ROUTER_MODE) {
                processRouterCommand(command, tokens, position);
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
                    if (router2600.isPasswordEnabled() || router2600.isSecretEnabled()) {
                        setMode(PASSWORD_MODE);
                    } else {
                        setMode(PRIVILEGED_MODE);
                    }
                } else {
                    showInvalidInputError(cursorPosition);
                }
            } else if (command.equals(UserCommand.EXIT) || command.equals(UserCommand.LOGOUT)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() == 0) {
                    setMode(INITIAL_MODE);
                } else {
                    showInvalidInputError(cursorPosition);
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
                    Command argument = getFullCommand(arg.toString(), Router2600Command.PRIVILEGED_CONFIGURE_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        StringBuffer extras = new StringBuffer();
                        int extrasPosition = getNextPosition(tokens, extras);
                        cursorPosition = arg.length() + extrasPosition;

                        if (argument.equals(PrivilegedCommand.CONFIGURE_TERMINAL)) {
                            if (extras.length() != 0) {
                                showInvalidInputError(cursorPosition);
                            } else {
                                setMode(CONFIGURATION_MODE);
                            }
                        }
                    }
                } else {
                    textArea.append("\nConfiguring from terminal, memory, or network [terminal]?");
                    textArea.append("\nEnter configuration commands, one per line.  End with CNTL/Z.");
                    setMode(CONFIGURATION_MODE);
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
            } else if (command.equals(PrivilegedCommand.EXIT) || command.equals(PrivilegedCommand.LOGOUT)) {
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
                    Command argument = getFullCommand(arg.toString(), Router2600Command.PRIVILEGED_SHOW_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(PrivilegedCommand.SHOW_IP)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() != 0) {
                                Command argument2 = getFullCommand(arg2.toString(), Router2600Command.PRIVILEGED_SHOW_IP_ARGUMENTS, cursorPosition);

                                if (argument2 != null) {
                                    if (argument2.equals(PrivilegedCommand.SHOW_IP_INTERFACE)) {
                                        StringBuffer arg3 = new StringBuffer();
                                        int arg3Position = getNextPosition(tokens, arg3);
                                        cursorPosition += (arg2.length() + arg3Position);

                                        if (arg3.length() != 0) {
                                        	if((isInterface(arg3.toString(), cursorPosition))){
                                                    Interface selectedInterface = getInterfaceFromName(arg3.toString(), cursorPosition);

                                                    if (selectedInterface != null) {
                                                        StringBuffer extras = new StringBuffer();
                                                        int extrasPosition = getNextPosition(tokens, extras);
                                                        cursorPosition += (arg3.toString().length() + extrasPosition);

                                                        if (extras.length() != 0) {
                                                            showInvalidInputError(cursorPosition);
                                                        } else {
                                                            showIPInterfaceArg(selectedInterface);
                                                        }
                                                    } else {
                                                    showIncompleteCommandError();
                                                    }
                                        	}else{Command argument3 = getFullCommand(arg3.toString(), Router2600Command.PRIVILEGED_SHOW_IP_INTERFACE_ARGUMENTS, cursorPosition);
	                                            if (argument3 != null) {	                                            	
	                                            	if (argument3.equals(PrivilegedCommand.SHOW_IP_INTERFACE_BRIEF)) {
	                                                    StringBuffer extras = new StringBuffer();
	                                                    int extrasPosition = getNextPosition(tokens, extras);
	                                                    cursorPosition += (arg3.length() + extrasPosition);
	
	                                                    if (extras.length() == 0) {
	                                                        showIPInterfaceBrief();
	                                                    } else {
	                                                    	showInvalidInputError(cursorPosition);
	                                                    }
	                                                                                          	
	                                            	}else {
	                                                    showInvalidInputError(cursorPosition);
	                                                }
	                                                
	                                            }
                                        	}
                                        } else {
                                            showIPInterface();
                                        }
                                    } else if (argument2.equals(PrivilegedCommand.SHOW_IP_ROUTE)) {
                                        StringBuffer arg3 = new StringBuffer();
                                        int arg3Position = getNextPosition(tokens, arg3);
                                        cursorPosition += (arg2.length() + arg3Position);

                                        if (arg3.length() == 0) {
                                            RoutingTable table = router2600.getRoutingTable();
                                            Entry[] entries = table.getEntries();
                                            //System.out.println("Showing Routing Table:");
                                            textArea.append("\n");
                                            textArea.append(router2600.toString());
                                            textArea.append("\nDestination - Netmask - Gateway\n");
                                            for (int i = 0; i < entries.length; i++) {
                                                textArea.append(entries[i].getDestinationNetwork().toString() + "  " + entries[i].getMask().toString() + "  " + entries[i].getNextHopAddress().toString() + "\n");
                                            }
                                        } else {
                                            showInvalidInputError(cursorPosition);
                                        }
                                    }
                                }
                            } else {
                                showIncompleteCommandError();
                            }
                        } else if (argument.equals(PrivilegedCommand.SHOW_INTERFACES)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() == 0) {
                                showInterfaces();
                            } else {
                                    Interface selectedInterface = getInterfaceFromName(arg2.toString(), cursorPosition);

                                    if (selectedInterface != null) {
                                        StringBuffer extras = new StringBuffer();
                                        int extrasPosition = getNextPosition(tokens, extras);
                                        cursorPosition += (arg2.length() + extrasPosition);

                                        if (extras.length() != 0) {
                                            showInvalidInputError(cursorPosition);
                                        } else {
                                            showInterfacesArg(selectedInterface);
                                        }
                                    } else {
                                    showIncompleteCommandError();
                                }
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
            if (command.equals(ConfigurationCommand.ACCESS_LIST)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    if (isInteger(arg.toString(), cursorPosition)) {
                        int accessListNumber = Integer.parseInt(arg.toString());

                        if ((accessListNumber == 0) || (arg.toString().charAt(0) > '1')) {
                            showInvalidInputError(cursorPosition);
                        }

                        if ((accessListNumber >= 1) && (accessListNumber <= 99)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() != 0) {
                                Command argument2 = getFullCommand(arg2.toString(), Router2600Command.CONFIGURATION_ACCESS_LIST_1_99_ARGUMENTS, cursorPosition);

                                if (argument2 != null) {
                                    if (argument2.equals(ConfigurationCommand.ACCESS_LIST_1_99_DENY)) {
                                        StringBuffer address = new StringBuffer();
                                        int addressPosition = getNextPosition(tokens, address);
                                        cursorPosition += (arg2.length() + addressPosition);

                                        if (address.length() != 0) {
                                            if (address.toString().equals(ConfigurationCommand.ACCESS_LIST_1_99_DENY_ANY.getName())) {
                                                StringBuffer extras = new StringBuffer();
                                                int extrasPosition = getNextPosition(tokens, extras);
                                                cursorPosition += (address.length() + extrasPosition);

                                                if (extras.length() == 0) {
                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                    boolean accessListFound = false;

                                                    for (int i = 0;
                                                            i < accessLists.length;
                                                            i++) {
                                                        int index = accessLists[i].getIndex();

                                                        if (index == accessListNumber) {
                                                            accessListFound = true;
                                                            accessLists[i].addIPPermission(new StandardIPPermission(StandardIPPermission.DENY, "0.0.0.0", "255.255.255.255"));
                                                        }
                                                    }

                                                    if (!accessListFound) {
                                                        StandardIPAccessList accessList = new StandardIPAccessList(accessListNumber);
                                                        accessList.addIPPermission(new StandardIPPermission(StandardIPPermission.DENY, "0.0.0.0", "255.255.255.255"));
                                                        router2600.addIPAccessList(accessList);
                                                    }
                                                } else {
                                                    showInvalidInputError(cursorPosition);
                                                }
                                            } else if (isValidQuartet(address.toString(), cursorPosition)) {
                                                StringBuffer wildcard = new StringBuffer();
                                                int wildcardPosition = getNextPosition(tokens, wildcard);
                                                cursorPosition += (address.length() + wildcardPosition);

                                                if (wildcard.length() != 0) {
                                                    if (isValidQuartet(wildcard.toString(), cursorPosition)) {
                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                        boolean accessListFound = false;

                                                        for (int i = 0;
                                                                i < accessLists.length;
                                                                i++) {
                                                            int index = accessLists[i].getIndex();

                                                            if (index == accessListNumber) {
                                                                accessListFound = true;
                                                                accessLists[i].addIPPermission(new StandardIPPermission(StandardIPPermission.DENY, address.toString(), wildcard.toString()));
                                                            }
                                                        }

                                                        if (!accessListFound) {
                                                            StandardIPAccessList accessList = new StandardIPAccessList(accessListNumber);
                                                            accessList.addIPPermission(new StandardIPPermission(StandardIPPermission.DENY, address.toString(), wildcard.toString()));
                                                            router2600.addIPAccessList(accessList);
                                                        }
                                                    }
                                                } else {
                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                    boolean accessListFound = false;

                                                    for (int i = 0;
                                                            i < accessLists.length;
                                                            i++) {
                                                        int index = accessLists[i].getIndex();

                                                        if (index == accessListNumber) {
                                                            accessListFound = true;
                                                            accessLists[i].addIPPermission(new StandardIPPermission(StandardIPPermission.DENY, address.toString(), "0.0.0.0"));
                                                        }
                                                    }

                                                    if (!accessListFound) {
                                                        StandardIPAccessList accessList = new StandardIPAccessList(accessListNumber);
                                                        accessList.addIPPermission(new StandardIPPermission(StandardIPPermission.DENY, address.toString(), "0.0.0.0"));
                                                        router2600.addIPAccessList(accessList);
                                                    }
                                                }
                                            }
                                        } else {
                                            showIncompleteCommandError();
                                        }
                                    } else if (argument2.equals(ConfigurationCommand.ACCESS_LIST_1_99_PERMIT)) {
                                        StringBuffer address = new StringBuffer();
                                        int addressPosition = getNextPosition(tokens, address);
                                        cursorPosition += (arg2.length() + addressPosition);

                                        if (address.length() != 0) {
                                            if (address.toString().equals(ConfigurationCommand.ACCESS_LIST_1_99_PERMIT_ANY.getName())) {
                                                StringBuffer extras = new StringBuffer();
                                                int extrasPosition = getNextPosition(tokens, extras);
                                                cursorPosition += (address.length() + extrasPosition);

                                                if (extras.length() == 0) {
                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                    boolean accessListFound = false;

                                                    for (int i = 0;
                                                            i < accessLists.length;
                                                            i++) {
                                                        int index = accessLists[i].getIndex();

                                                        if (index == accessListNumber) {
                                                            accessListFound = true;
                                                            accessLists[i].addIPPermission(new StandardIPPermission(StandardIPPermission.PERMIT, "0.0.0.0", "255.255.255.255"));
                                                        }
                                                    }

                                                    if (!accessListFound) {
                                                        StandardIPAccessList accessList = new StandardIPAccessList(accessListNumber);
                                                        accessList.addIPPermission(new StandardIPPermission(StandardIPPermission.PERMIT, "0.0.0.0", "255.255.255.255"));
                                                        router2600.addIPAccessList(accessList);
                                                    }
                                                } else {
                                                    showInvalidInputError(cursorPosition);
                                                }
                                            }

                                            if (isValidQuartet(address.toString(), cursorPosition)) {
                                                StringBuffer wildcard = new StringBuffer();
                                                int wildcardPosition = getNextPosition(tokens, wildcard);
                                                cursorPosition += (address.length() + wildcardPosition);

                                                if (wildcard.length() != 0) {
                                                    if (isValidQuartet(wildcard.toString(), cursorPosition)) {
                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                        boolean accessListFound = false;

                                                        for (int i = 0;
                                                                i < accessLists.length;
                                                                i++) {
                                                            int index = accessLists[i].getIndex();

                                                            if (index == accessListNumber) {
                                                                accessListFound = true;
                                                                accessLists[i].addIPPermission(new StandardIPPermission(StandardIPPermission.PERMIT, address.toString(), wildcard.toString()));
                                                            }
                                                        }

                                                        if (!accessListFound) {
                                                            StandardIPAccessList accessList = new StandardIPAccessList(accessListNumber);
                                                            accessList.addIPPermission(new StandardIPPermission(StandardIPPermission.PERMIT, address.toString(), wildcard.toString()));
                                                            router2600.addIPAccessList(accessList);
                                                        }
                                                    }
                                                } else {
                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                    boolean accessListFound = false;

                                                    for (int i = 0;
                                                            i < accessLists.length;
                                                            i++) {
                                                        int index = accessLists[i].getIndex();

                                                        if (index == accessListNumber) {
                                                            accessListFound = true;
                                                            accessLists[i].addIPPermission(new StandardIPPermission(StandardIPPermission.PERMIT, address.toString(), "0.0.0.0"));
                                                        }
                                                    }

                                                    if (!accessListFound) {
                                                        StandardIPAccessList accessList = new StandardIPAccessList(accessListNumber);
                                                        accessList.addIPPermission(new StandardIPPermission(StandardIPPermission.PERMIT, address.toString(), "0.0.0.0"));
                                                        router2600.addIPAccessList(accessList);
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
                        } else if ((accessListNumber >= 100) && (accessListNumber <= 199)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            /** TODO: EXTENDED ACCESS LIST */
                            if (arg2.length() != 0) {
                                Command argument2 = getFullCommand(arg2.toString(), Router2600Command.CONFIGURATION_ACCESS_LIST_100_199_ARGUMENTS, cursorPosition);

                                if (argument2 != null) {
                                    if (argument2.equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY)) {
                                        StringBuffer arg3 = new StringBuffer();
                                        int arg3Position = getNextPosition(tokens, arg3);
                                        cursorPosition += (arg2.length() + arg3Position);

                                        if (arg3.length() != 0) {
                                            Command argument3 = getFullCommand(arg3.toString(), Router2600Command.CONFIGURATION_ACCESS_LIST_100_199_DENY_ARGUMENTS, cursorPosition);

                                            if (argument3 != null) {
                                                if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() == 0) {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    } else {
                                                                        showInvalidInputError(cursorPosition);
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
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
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            } else {
                                                                                showInvalidInputError(cursorPosition);
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() == 0) {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        }
                                                    } else {
                                                        showIncompleteCommandError();
                                                    }
                                                } else if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_ICMP)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_ICMP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_ICMP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() == 0) {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.ICMP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.ICMP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    } else {
                                                                        showInvalidInputError(cursorPosition);
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.ICMP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.ICMP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
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
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_ICMP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.ICMP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.ICMP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            } else {
                                                                                showInvalidInputError(cursorPosition);
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() == 0) {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.ICMP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.ICMP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        }
                                                    } else {
                                                        showIncompleteCommandError();
                                                    }
                                                } else if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_IGRP)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_IGRP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_IGRP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() == 0) {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IGRP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IGRP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    } else {
                                                                        showInvalidInputError(cursorPosition);
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IGRP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IGRP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
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
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_IGRP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IGRP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IGRP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            } else {
                                                                                showInvalidInputError(cursorPosition);
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() == 0) {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IGRP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IGRP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        }
                                                    } else {
                                                        showIncompleteCommandError();
                                                    }
                                                } else if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_IP)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_IP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_IP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() == 0) {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    } else {
                                                                        showInvalidInputError(cursorPosition);
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
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
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_IP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            } else {
                                                                                showInvalidInputError(cursorPosition);
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() == 0) {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.IP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        }
                                                    } else {
                                                        showIncompleteCommandError();
                                                    }
                                                } else if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_OSPF)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() == 0) {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    } else {
                                                                        showInvalidInputError(cursorPosition);
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
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
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            } else {
                                                                                showInvalidInputError(cursorPosition);
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() == 0) {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        }
                                                    } else {
                                                        showIncompleteCommandError();
                                                    }
                                                } else if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_TCP)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_TCP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_TCP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (arg6.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_TCP_ANY_ANY_EQ.getName())) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition = arg6.length() + arg7Position;

                                                                            if (arg7.length() != 0) {
                                                                                if (isInteger(arg7.toString(), cursorPosition)) {
                                                                                    StringBuffer arg8 = new StringBuffer();
                                                                                    int arg8Position = getNextPosition(tokens, arg8);
                                                                                    cursorPosition = arg7.length() + arg8Position;

                                                                                    if (arg8.length() == 0) {
                                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                        boolean accessListFound = false;

                                                                                        for (int i = 0;
                                                                                                i < accessLists.length;
                                                                                                i++) {
                                                                                            int index = accessLists[i].getIndex();

                                                                                            if (index == accessListNumber) {
                                                                                                accessListFound = true;
                                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255", Integer.parseInt(arg7.toString())));
                                                                                            }
                                                                                        }

                                                                                        if (!accessListFound) {
                                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255", Integer.parseInt(arg7.toString())));
                                                                                            router2600.addIPAccessList(accessList);
                                                                                        }
                                                                                    } else {
                                                                                        showInvalidInputError(cursorPosition);
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                showIncompleteCommandError();
                                                                            }
                                                                        } else {
                                                                            showInvalidInputError(cursorPosition);
                                                                        }
                                                                    } else {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() != 0) {
                                                                                if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_TCP_ANY_ABCD_ABCD_EQ.getName())) {
                                                                                    StringBuffer arg8 = new StringBuffer();
                                                                                    int arg8Position = getNextPosition(tokens, arg8);
                                                                                    cursorPosition += (arg7.length() + arg8Position);

                                                                                    if (arg8.length() != 0) {
                                                                                        if (isInteger(arg8.toString(), cursorPosition)) {
                                                                                            StringBuffer arg9 = new StringBuffer();
                                                                                            int arg9Position = getNextPosition(tokens, arg9);
                                                                                            cursorPosition += (arg8.length() + arg9Position);

                                                                                            if (arg9.length() == 0) {
                                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                                boolean accessListFound = false;

                                                                                                for (int i = 0;
                                                                                                        i < accessLists.length;
                                                                                                        i++) {
                                                                                                    int index = accessLists[i].getIndex();

                                                                                                    if (index == accessListNumber) {
                                                                                                        accessListFound = true;
                                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString(), Integer.parseInt(arg8.toString())));
                                                                                                    }
                                                                                                }

                                                                                                if (!accessListFound) {
                                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString(), Integer.parseInt(arg8.toString())));
                                                                                                    router2600.addIPAccessList(accessList);
                                                                                                }
                                                                                            } else {
                                                                                                showInvalidInputError(cursorPosition);
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        showIncompleteCommandError();
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            } else {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            }
                                                                        }
                                                                    } else {
                                                                        showIncompleteCommandError();
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_TCP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() != 0) {
                                                                                if (arg8.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_TCP_ABCD_ABCD_ANY_EQ.getName())) {
                                                                                    StringBuffer arg9 = new StringBuffer();
                                                                                    int arg9Position = getNextPosition(tokens, arg9);
                                                                                    cursorPosition += (arg8.length() + arg9Position);

                                                                                    if (arg9.length() != 0) {
                                                                                        if (isInteger(arg9.toString(), cursorPosition)) {
                                                                                            StringBuffer arg10 = new StringBuffer();
                                                                                            int arg10Position = getNextPosition(tokens, arg10);
                                                                                            cursorPosition += (arg9.length() + arg10Position);

                                                                                            if (arg10.length() == 0) {
                                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                                boolean accessListFound = false;

                                                                                                for (int i = 0;
                                                                                                        i < accessLists.length;
                                                                                                        i++) {
                                                                                                    int index = accessLists[i].getIndex();

                                                                                                    if (index == accessListNumber) {
                                                                                                        accessListFound = true;
                                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255", Integer.parseInt(arg9.toString())));
                                                                                                    }
                                                                                                }

                                                                                                if (!accessListFound) {
                                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255", Integer.parseInt(arg9.toString())));
                                                                                                    router2600.addIPAccessList(accessList);
                                                                                                }
                                                                                            } else {
                                                                                                showInvalidInputError(cursorPosition);
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        showIncompleteCommandError();
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() != 0) {
                                                                                    if (arg9.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_TCP_ABCD_ABCD_ABCD_ABCD_EQ.getName())) {
                                                                                        StringBuffer arg10 = new StringBuffer();
                                                                                        int arg10Position = getNextPosition(tokens, arg10);
                                                                                        cursorPosition += (arg9.length() + arg10Position);

                                                                                        if (arg10.length() != 0) {
                                                                                        	if (isInteger(arg10.toString(), cursorPosition)){
                                                                                        		StringBuffer arg11 = new StringBuffer();
                                                                                        		int arg11Position = getNextPosition(tokens, arg11);
                                                                                        		cursorPosition += arg10.length() + arg11Position;
                                                                                        		if (arg11.length() == 0){
                                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                                    boolean accessListFound = false;

                                                                                                    for (int i = 0;
                                                                                                            i < accessLists.length;
                                                                                                            i++) {
                                                                                                        int index = accessLists[i].getIndex();

                                                                                                        if (index == accessListNumber) {
                                                                                                            accessListFound = true;
                                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString(), Integer.parseInt(arg10.toString())));
                                                                                                        }
                                                                                                    }

                                                                                                    if (!accessListFound) {
                                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString(), Integer.parseInt(arg10.toString())));
                                                                                                        router2600.addIPAccessList(accessList);
                                                                                                    }
                                                                                        		}else{
                                                                                        			showInvalidInputError(cursorPosition);
                                                                                        		}
                                                                                        	}
                                                                                        } else {
                                                                                            showIncompleteCommandError();
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        }
                                                    } else {
                                                        showIncompleteCommandError();
                                                    }
                                                } else if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_UDP)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_UDP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_UDP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() == 0) {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.UDP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.UDP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    } else {
                                                                        showInvalidInputError(cursorPosition);
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.UDP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.UDP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
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
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_DENY_UDP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.UDP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.UDP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            } else {
                                                                                showInvalidInputError(cursorPosition);
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() == 0) {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.UDP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.UDP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            }
                                                                        }
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
                                    } else if (argument2.equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT)) {
                                        StringBuffer arg3 = new StringBuffer();
                                        int arg3Position = getNextPosition(tokens, arg3);
                                        cursorPosition += (arg2.length() + arg3Position);

                                        if (arg3.length() != 0) {
                                            Command argument3 = getFullCommand(arg3.toString(), Router2600Command.CONFIGURATION_ACCESS_LIST_100_199_PERMIT_ARGUMENTS, cursorPosition);

                                            if (argument3 != null) {
                                                if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_EIGRP)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_EIGRP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_EIGRP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() == 0) {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    } else {
                                                                        showInvalidInputError(cursorPosition);
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
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
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_EIGRP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            } else {
                                                                                showInvalidInputError(cursorPosition);
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() == 0) {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        }
                                                    } else {
                                                        showIncompleteCommandError();
                                                    }
                                                } else if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_ICMP)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_ICMP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_ICMP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() == 0) {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.ICMP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.ICMP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    } else {
                                                                        showInvalidInputError(cursorPosition);
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.ICMP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.ICMP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
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
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_ICMP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.ICMP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.ICMP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            } else {
                                                                                showInvalidInputError(cursorPosition);
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() == 0) {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.ICMP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.ICMP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        }
                                                    } else {
                                                        showIncompleteCommandError();
                                                    }
                                                } else if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_IGRP)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_IGRP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_IGRP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() == 0) {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IGRP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IGRP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    } else {
                                                                        showInvalidInputError(cursorPosition);
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IGRP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IGRP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
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
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_IGRP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IGRP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IGRP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            } else {
                                                                                showInvalidInputError(cursorPosition);
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() == 0) {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IGRP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IGRP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        }
                                                    } else {
                                                        showIncompleteCommandError();
                                                    }
                                                } else if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_IP)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_IP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_IP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() == 0) {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    } else {
                                                                        showInvalidInputError(cursorPosition);
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
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
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_IP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            } else {
                                                                                showInvalidInputError(cursorPosition);
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() == 0) {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.IP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        }
                                                    } else {
                                                        showIncompleteCommandError();
                                                    }
                                                } else if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_OSPF)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_EIGRP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_EIGRP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() == 0) {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    } else {
                                                                        showInvalidInputError(cursorPosition);
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
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
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_EIGRP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            } else {
                                                                                showInvalidInputError(cursorPosition);
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() == 0) {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.EIGRP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        }
                                                    } else {
                                                        showIncompleteCommandError();
                                                    }
                                                } else if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_TCP)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_TCP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_TCP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (arg6.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_TCP_ANY_ANY_EQ.getName())) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition = arg6.length() + arg7Position;

                                                                            if (arg7.length() != 0) {
                                                                                if (isInteger(arg7.toString(), cursorPosition)) {
                                                                                    StringBuffer arg8 = new StringBuffer();
                                                                                    int arg8Position = getNextPosition(tokens, arg8);
                                                                                    cursorPosition = arg7.length() + arg8Position;

                                                                                    if (arg8.length() == 0) {
                                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                        boolean accessListFound = false;

                                                                                        for (int i = 0;
                                                                                                i < accessLists.length;
                                                                                                i++) {
                                                                                            int index = accessLists[i].getIndex();

                                                                                            if (index == accessListNumber) {
                                                                                                accessListFound = true;
                                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255", Integer.parseInt(arg7.toString())));
                                                                                            }
                                                                                        }

                                                                                        if (!accessListFound) {
                                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255", Integer.parseInt(arg7.toString())));
                                                                                            router2600.addIPAccessList(accessList);
                                                                                        }
                                                                                    } else {
                                                                                        showInvalidInputError(cursorPosition);
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                showIncompleteCommandError();
                                                                            }
                                                                        } else {
                                                                            showInvalidInputError(cursorPosition);
                                                                        }
                                                                    } else {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() != 0) {
                                                                                if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_TCP_ANY_ABCD_ABCD_EQ.getName())) {
                                                                                    StringBuffer arg8 = new StringBuffer();
                                                                                    int arg8Position = getNextPosition(tokens, arg8);
                                                                                    cursorPosition += (arg7.length() + arg8Position);

                                                                                    if (arg8.length() != 0) {
                                                                                        if (isInteger(arg8.toString(), cursorPosition)) {
                                                                                            StringBuffer arg9 = new StringBuffer();
                                                                                            int arg9Position = getNextPosition(tokens, arg9);
                                                                                            cursorPosition += (arg8.length() + arg9Position);

                                                                                            if (arg9.length() == 0) {
                                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                                boolean accessListFound = false;

                                                                                                for (int i = 0;
                                                                                                        i < accessLists.length;
                                                                                                        i++) {
                                                                                                    int index = accessLists[i].getIndex();

                                                                                                    if (index == accessListNumber) {
                                                                                                        accessListFound = true;
                                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString(), Integer.parseInt(arg8.toString())));
                                                                                                    }
                                                                                                }

                                                                                                if (!accessListFound) {
                                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString(), Integer.parseInt(arg8.toString())));
                                                                                                    router2600.addIPAccessList(accessList);
                                                                                                }
                                                                                            } else {
                                                                                                showInvalidInputError(cursorPosition);
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        showIncompleteCommandError();
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            } else {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            }
                                                                        }
                                                                    } else {
                                                                        showIncompleteCommandError();
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_TCP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() != 0) {
                                                                                if (arg8.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_TCP_ABCD_ABCD_ANY_EQ.getName())) {
                                                                                    StringBuffer arg9 = new StringBuffer();
                                                                                    int arg9Position = getNextPosition(tokens, arg9);
                                                                                    cursorPosition += (arg8.length() + arg9Position);

                                                                                    if (arg9.length() != 0) {
                                                                                        if (isInteger(arg9.toString(), cursorPosition)) {
                                                                                            StringBuffer arg10 = new StringBuffer();
                                                                                            int arg10Position = getNextPosition(tokens, arg10);
                                                                                            cursorPosition += (arg9.length() + arg10Position);

                                                                                            if (arg10.length() == 0) {
                                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                                boolean accessListFound = false;

                                                                                                for (int i = 0;
                                                                                                        i < accessLists.length;
                                                                                                        i++) {
                                                                                                    int index = accessLists[i].getIndex();

                                                                                                    if (index == accessListNumber) {
                                                                                                        accessListFound = true;
                                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255", Integer.parseInt(arg9.toString())));
                                                                                                    }
                                                                                                }

                                                                                                if (!accessListFound) {
                                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255", Integer.parseInt(arg9.toString())));
                                                                                                    router2600.addIPAccessList(accessList);
                                                                                                }
                                                                                            } else {
                                                                                                showInvalidInputError(cursorPosition);
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        showIncompleteCommandError();
                                                                                    }
                                                                                }
                                                                            } else {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() != 0) {
                                                                                    if (arg9.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_TCP_ABCD_ABCD_ABCD_ABCD_EQ.getName())) {
                                                                                        StringBuffer arg10 = new StringBuffer();
                                                                                        int arg10Position = getNextPosition(tokens, arg10);
                                                                                        cursorPosition += (arg9.length() + arg10Position);

                                                                                        if (arg10.length() != 0) {
                                                                                        	if (isInteger(arg10.toString(), cursorPosition)){
                                                                                        		StringBuffer arg11 = new StringBuffer();
                                                                                        		int arg11Position = getNextPosition(tokens, arg11);
                                                                                        		cursorPosition += arg10.length() + arg11Position;
                                                                                        		if (arg11.length() == 0){
                                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                                    boolean accessListFound = false;

                                                                                                    for (int i = 0;
                                                                                                            i < accessLists.length;
                                                                                                            i++) {
                                                                                                        int index = accessLists[i].getIndex();

                                                                                                        if (index == accessListNumber) {
                                                                                                            accessListFound = true;
                                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString(), Integer.parseInt(arg10.toString())));
                                                                                                        }
                                                                                                    }

                                                                                                    if (!accessListFound) {
                                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString(), Integer.parseInt(arg10.toString())));
                                                                                                        router2600.addIPAccessList(accessList);
                                                                                                    }
                                                                                        		}else{
                                                                                        			showInvalidInputError(cursorPosition);
                                                                                        		}
                                                                                        	}
                                                                                        } else {
                                                                                            showIncompleteCommandError();
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.DENY, ExtendedIPPermission.TCP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                showIncompleteCommandError();
                                                            }
                                                        }
                                                    } else {
                                                        showIncompleteCommandError();
                                                    }
                                                } else if (argument3.equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_UDP)) {
                                                    StringBuffer arg4 = new StringBuffer();
                                                    int arg4Position = getNextPosition(tokens, arg4);
                                                    cursorPosition += (arg3.length() + arg4Position);

                                                    if (arg4.length() != 0) {
                                                        if (arg4.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_UDP_ANY.getName())) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition += (arg4.length() + arg5Position);

                                                            if (arg5.length() != 0) {
                                                                if (arg5.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_UDP_ANY_ANY.getName())) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() == 0) {
                                                                        IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                        boolean accessListFound = false;

                                                                        for (int i = 0;
                                                                                i < accessLists.length;
                                                                                i++) {
                                                                            int index = accessLists[i].getIndex();

                                                                            if (index == accessListNumber) {
                                                                                accessListFound = true;
                                                                                accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.UDP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            }
                                                                        }

                                                                        if (!accessListFound) {
                                                                            ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                            accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.UDP, "0.0.0.0", "255.255.255.255", "0.0.0.0", "255.255.255.255"));
                                                                            router2600.addIPAccessList(accessList);
                                                                        }
                                                                    } else {
                                                                        showInvalidInputError(cursorPosition);
                                                                    }
                                                                } else if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg6);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (arg6.length() != 0) {
                                                                        if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                            StringBuffer arg7 = new StringBuffer();
                                                                            int arg7Position = getNextPosition(tokens, arg7);
                                                                            cursorPosition += (arg6.length() + arg7Position);

                                                                            if (arg7.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.UDP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.UDP, "0.0.0.0", "255.255.255.255", arg5.toString(), arg6.toString()));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
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
                                                        } else if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                            StringBuffer arg5 = new StringBuffer();
                                                            int arg5Position = getNextPosition(tokens, arg5);
                                                            cursorPosition = arg4.length() + arg5Position;

                                                            if (arg5.length() != 0) {
                                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                                    StringBuffer arg6 = new StringBuffer();
                                                                    int arg6Position = getNextPosition(tokens, arg);
                                                                    cursorPosition += (arg5.length() + arg6Position);

                                                                    if (isValidQuartet(arg6.toString(), cursorPosition)) {
                                                                        StringBuffer arg7 = new StringBuffer();
                                                                        int arg7Position = getNextPosition(tokens, arg7);
                                                                        cursorPosition += (arg6.length() + arg7Position);

                                                                        if (arg7.toString().equals(ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_UDP_ABCD_ABCD_ANY.getName())) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (arg8.length() == 0) {
                                                                                IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                boolean accessListFound = false;

                                                                                for (int i = 0;
                                                                                        i < accessLists.length;
                                                                                        i++) {
                                                                                    int index = accessLists[i].getIndex();

                                                                                    if (index == accessListNumber) {
                                                                                        accessListFound = true;
                                                                                        accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.UDP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    }
                                                                                }

                                                                                if (!accessListFound) {
                                                                                    ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                    accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.UDP, arg5.toString(), arg6.toString(), "0.0.0.0", "255.255.255.255"));
                                                                                    router2600.addIPAccessList(accessList);
                                                                                }
                                                                            } else {
                                                                                showInvalidInputError(cursorPosition);
                                                                            }
                                                                        } else if (isValidQuartet(arg7.toString(), cursorPosition)) {
                                                                            StringBuffer arg8 = new StringBuffer();
                                                                            int arg8Position = getNextPosition(tokens, arg8);
                                                                            cursorPosition += (arg7.length() + arg8Position);

                                                                            if (isValidQuartet(arg8.toString(), cursorPosition)) {
                                                                                StringBuffer arg9 = new StringBuffer();
                                                                                int arg9Position = getNextPosition(tokens, arg9);
                                                                                cursorPosition += (arg8.length() + arg9Position);

                                                                                if (arg9.length() == 0) {
                                                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();
                                                                                    boolean accessListFound = false;

                                                                                    for (int i = 0;
                                                                                            i < accessLists.length;
                                                                                            i++) {
                                                                                        int index = accessLists[i].getIndex();

                                                                                        if (index == accessListNumber) {
                                                                                            accessListFound = true;
                                                                                            accessLists[i].addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.UDP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        }
                                                                                    }

                                                                                    if (!accessListFound) {
                                                                                        ExtendedIPAccessList accessList = new ExtendedIPAccessList(accessListNumber);
                                                                                        accessList.addIPPermission(new ExtendedIPPermission(ExtendedIPPermission.PERMIT, ExtendedIPPermission.UDP, arg5.toString(), arg6.toString(), arg7.toString(), arg8.toString()));
                                                                                        router2600.addIPAccessList(accessList);
                                                                                    }
                                                                                } else {
                                                                                    showInvalidInputError(cursorPosition);
                                                                                }
                                                                            }
                                                                        }
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
            } else if (command.equals(ConfigurationCommand.ENABLE)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Router2600Command.CONFIGURATION_ENABLE_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(ConfigurationCommand.ENABLE_PASSWORD)) {
                            StringBuffer password = new StringBuffer();
                            int passwordPosition = getNextPosition(tokens, password);
                            cursorPosition += (arg.length() + passwordPosition);

                            if (password.length() != 0) {
                                StringBuffer extras = new StringBuffer();
                                int extrasPosition = getNextPosition(tokens, extras);
                                cursorPosition += (password.length() + extrasPosition);

                                if (extras.length() != 0) {
                                    showInvalidInputError(cursorPosition);
                                } else {
                                    router2600.setPassword(password.toString());
                                }
                            } else {
                                showIncompleteCommandError();
                            }
                        } else if (argument.equals(ConfigurationCommand.ENABLE_SECRET)) {
                            StringBuffer secret = new StringBuffer();
                            int secretPosition = getNextPosition(tokens, secret);
                            cursorPosition += (arg.length() + secretPosition);

                            if (secret.length() != 0) {
                                StringBuffer extras = new StringBuffer();
                                int extrasPosition = getNextPosition(tokens, extras);
                                cursorPosition += (secret.length() + extrasPosition);

                                if (extras.length() != 0) {
                                    showInvalidInputError(cursorPosition);
                                } else {
                                    router2600.setSecret(secret.toString());
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
                        router2600.setName(arg.toString());
                        setMode(currentMode);
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
            } else if (command.equals(ConfigurationCommand.IP)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Router2600Command.CONFIGURATION_IP_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(ConfigurationCommand.IP_ROUTE)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() != 0) {
                                if (isValidQuartet(arg2.toString(), cursorPosition)) {
                                    StringBuffer arg3 = new StringBuffer();
                                    int arg3Position = getNextPosition(tokens, arg3);
                                    cursorPosition += (arg2.length() + arg3Position);

                                    if (arg3.length() != 0) {
                                        if (isValidQuartet(arg3.toString(), cursorPosition)) {
                                            StringBuffer arg4 = new StringBuffer();
                                            int arg4Position = getNextPosition(tokens, arg4);
                                            cursorPosition += (arg3.length() + arg4Position);

                                            if (arg4.length() != 0) {
                                                if (Character.isDigit(arg4.charAt(0))) {
                                                    if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                        StringBuffer arg5 = new StringBuffer();
                                                        int arg5Position = getNextPosition(tokens, arg5);
                                                        cursorPosition += (arg4.length() + arg5Position);

                                                        if (arg5.length() == 0) {
                                                            RoutingTable table = router2600.getRoutingTable();
                                                            Entry entry = new Entry(arg2.toString(), arg3.toString(), arg4.toString(), Entry.STATIC_HOP_COUNT);
                                                            table.addEntry(entry);
                                                        } else {
                                                        }
                                                    }
                                                } else {
                                                    Command argument4 = getFullCommand(arg4.toString(), Router2600Command.CONFIGURATION_IP_ROUTE_ABCD_ABCD_ARGUMENTS, cursorPosition);

                                                    if (argument4 != null) {
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
                            } else {
                                showIncompleteCommandError();
                            }
                        }
                    }
                } else {
                    showIncompleteCommandError();
                }
            } else if (command.equals(ConfigurationCommand.NO)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Router2600Command.CONFIGURATION_MODE_COMMANDS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(ConfigurationCommand.IP)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() != 0) {
                                Command argument2 = getFullCommand(arg2.toString(), Router2600Command.CONFIGURATION_IP_ARGUMENTS, cursorPosition);

                                if (argument2 != null) {
                                    if (argument2.equals(ConfigurationCommand.IP_ROUTE)) {
                                        StringBuffer arg3 = new StringBuffer();
                                        int arg3Position = getNextPosition(tokens, arg3);
                                        cursorPosition += (arg2.length() + arg3Position);

                                        if (isValidQuartet(arg3.toString(), cursorPosition)) {
                                            String networkAddress = arg3.toString();
                                            StringBuffer arg4 = new StringBuffer();
                                            int arg4Position = getNextPosition(tokens, arg4);
                                            cursorPosition += (arg3.length() + arg4Position);

                                            if (isValidQuartet(arg4.toString(), cursorPosition)) {
                                                String subnetMask = arg4.toString();
                                                StringBuffer arg5 = new StringBuffer();
                                                int arg5Position = getNextPosition(tokens, arg5);
                                                cursorPosition += (arg4.length() + arg5Position);

                                                if (isValidQuartet(arg5.toString(), cursorPosition)) {
                                                    String gateway = arg5.toString();
                                                    RoutingTable table = router2600.getRoutingTable();
                                                    table.deleteEntry(networkAddress, subnetMask, gateway);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                showIncompleteCommandError();
                            }
                        }
                    } else {
                        showIncompleteCommandError();
                    }
                } else {
                    showIncompleteCommandError();
                }
            } else if (command.equals(ConfigurationCommand.ROUTER)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Router2600Command.CONFIGURATION_ROUTER_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(ConfigurationCommand.ROUTER_RIP)) {
                            StringBuffer extras = new StringBuffer();
                            int extrasPosition = getNextPosition(tokens, extras);
                            cursorPosition += (arg.length() + extrasPosition);

                            if (extras.length() == 0) {
                                setMode(ROUTER_MODE);

                                RoutingProtocol protocol = router2600.getCurrentRoutingProtocol();

                                if (protocol != null) {
                                    if (protocol.getAdministrativeDistance() > router2600.getRIP().getAdministrativeDistance()) {
                                        router2600.setCurrentRoutingProtocol(router2600.getRIP());
                                        router2600.startUpdateTimer();
                                    }
                                } else {
                                    router2600.setCurrentRoutingProtocol(router2600.getRIP());
                                    router2600.startUpdateTimer();
                                }
                            } else {
                                showInvalidInputError(cursorPosition);
                            }
                        } else if (argument.equals(ConfigurationCommand.ROUTER_IGRP)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() != 0) {
                                if (isInteger(arg2.toString(), cursorPosition)) {
                                    StringBuffer extras = new StringBuffer();
                                    int extrasPosition = getNextPosition(tokens, extras);
                                    cursorPosition += (arg2.length() + extrasPosition);

                                    if (extras.length() == 0) {
                                        setMode(ROUTER_MODE);

                                        RoutingProtocol protocol = router2600.getCurrentRoutingProtocol();

                                        if (protocol != null) {
                                            if (protocol.getAdministrativeDistance() > router2600.getIGRP().getAdministrativeDistance()) {
                                                IGRP igrp = router2600.getIGRP();
                                                igrp.setASNumber(Integer.parseInt(arg2.toString()));
                                                router2600.setCurrentRoutingProtocol(router2600.getIGRP());
                                                router2600.startUpdateTimer();
                                            }
                                        } else {
                                            IGRP igrp = router2600.getIGRP();
                                            igrp.setASNumber(Integer.parseInt(arg2.toString()));
                                            router2600.setCurrentRoutingProtocol(router2600.getIGRP());
                                            router2600.startUpdateTimer();
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
            if (command.equals(InterfaceCommand.INTERFACE)) {
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
            } else if (command.equals(InterfaceCommand.IP)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Router2600Command.INTERFACE_IP_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(InterfaceCommand.IP_ADDRESS)) {
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
                                                currentInterface.setIPAddress(ipAddressDecimals);
                                                currentInterface.setSubnetMask(subnetDecimals);

                                                RoutingTable table = router2600.getRoutingTable();
                                                Entry[] tableEntries = table.getEntries();

                                                for (int i = 0;
                                                        i < tableEntries.length;
                                                        i++) {
                                                    if (tableEntries[i].getRouterInterface() != null) {
                                                        if ((tableEntries[i].getRouterInterface() == currentInterface) && tableEntries[i].getConnectionType().equals(Entry.DIRECTLY_CONNECTED)) {
                                                            table.deleteEntry(tableEntries[i]);
                                                        }
                                                    }
                                                }

                                                Entry entry = new Entry(IPAddress.getNetworkAddress(currentInterface.getIPAddress(), currentInterface.getSubnetMask()), currentInterface.getSubnetMask().toString(), "0.0.0.0", Entry.DIRECTLY_CONNECTED_HOP_COUNT);
                                                entry.setRouterInterface(currentInterface);
                                                entry.setConnectionType(Entry.DIRECTLY_CONNECTED);
                                                table.addEntry(entry);
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
                        } else if (argument.equals(InterfaceCommand.IP_ACCESS_GROUP)) {
                            StringBuffer arg2 = new StringBuffer();
                            int arg2Position = getNextPosition(tokens, arg2);
                            cursorPosition += (arg.length() + arg2Position);

                            if (arg2.length() != 0) {
                                if (isInteger(arg2.toString(), cursorPosition)) {
                                    int accessListNumber = Integer.parseInt(arg2.toString());

                                    if ((accessListNumber == 0) || (arg2.charAt(0) > '1')) {
                                        showInvalidInputError(cursorPosition);
                                    } else if ((accessListNumber >= 1) && (accessListNumber <= 99)) {
                                        StringBuffer arg3 = new StringBuffer();
                                        int arg3Position = getNextPosition(tokens, arg3);
                                        cursorPosition += (arg2.length() + arg3Position);

                                        if (arg3.length() != 0) {
                                            Command direction = getFullCommand(arg3.toString(), Router2600Command.INTERFACE_IP_ACCESS_GROUP_1_199_ARGUMENTS, cursorPosition);

                                            if (direction.equals(InterfaceCommand.IP_ACCESS_GROUP_1_199_IN)) {
                                                StringBuffer extras = new StringBuffer();
                                                int extrasPosition = getNextPosition(tokens, extras);
                                                cursorPosition += (arg3.length() + extrasPosition);

                                                if (extras.length() == 0) {
                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();

                                                    for (int i = 0;
                                                            i < accessLists.length;
                                                            i++) {
                                                        int index = accessLists[i].getIndex();

                                                        if (index == accessListNumber) {
                                                            currentInterface.setIPAccessListIn(accessLists[i]);
                                                        }
                                                    }
                                                } else {
                                                    showInvalidInputError(cursorPosition);
                                                }
                                            } else if (direction.equals(InterfaceCommand.IP_ACCESS_GROUP_1_199_OUT)) {
                                                StringBuffer extras = new StringBuffer();
                                                int extrasPosition = getNextPosition(tokens, extras);
                                                cursorPosition += (arg3.length() + extrasPosition);

                                                if (extras.length() == 0) {
                                                    IPAccessList[] accessLists = router2600.getIPAccessLists();

                                                    for (int i = 0;
                                                            i < accessLists.length;
                                                            i++) {
                                                        int index = accessLists[i].getIndex();

                                                        if (index == accessListNumber) {
                                                            currentInterface.setIPAccessListOut(accessLists[i]);
                                                        }
                                                    }
                                                } else {
                                                    showInvalidInputError(cursorPosition);
                                                }
                                            }
                                        } else {
                                            showIncompleteCommandError();
                                        }
                                    } else if ((accessListNumber >= 100) && (accessListNumber <= 199)) {
                                    } else {
                                        showInvalidInputError(cursorPosition + 3);
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
            } else if (command.equals(InterfaceCommand.SHUTDOWN)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() == 0) {
                    if (currentInterface.getState().equals(Interface.UP)) {
                        currentInterface.setState(Interface.ADMINISTRATIVELY_DOWN);
                        currentInterface.setProtocolStatus(Interface.DOWN);
                        showInterfaceState(Interface.ADMINISTRATIVELY_DOWN);
                    }
                } else {
                    showInvalidInputError(cursorPosition);
                }
            } else if (command.equals(InterfaceCommand.NO)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Router2600Command.INTERFACE_NO_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(InterfaceCommand.NO_SHUTDOWN)) {
                            StringBuffer extras = new StringBuffer();
                            int extrasPosition = getNextPosition(tokens, extras);
                            cursorPosition += (arg.length() + extrasPosition);

                            if (extras.length() == 0) {
                                if (currentInterface.getState().equals(Interface.ADMINISTRATIVELY_DOWN)) {
                                    currentInterface.setState(Interface.UP);
                                    currentInterface.setProtocolStatus(Interface.UP);
                                    showInterfaceState(Interface.UP);
                                }
                            } else {
                                showInvalidInputError(cursorPosition);
                            }
                        }
                    }
                } else {
                    showIncompleteCommandError();
                }
            } else if (command.equals(InterfaceCommand.EXIT)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() == 0) {
                    setMode(CONFIGURATION_MODE);
                } else {
                    showInvalidInputError(cursorPosition);
                }
            }
        }
    }

    private void processPasswordCommand(String input, StringTokenizer tokens, int position) {
        if (router2600.isSecretEnabled()) {
            if (input.equals(router2600.getSecret())) {
                setMode(PRIVILEGED_MODE);
            } else {
                passwordTries++;
            }
        } else if (router2600.isPasswordEnabled()) {
            if (input.equals(router2600.getPassword())) {
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

    private void processRouterCommand(String input, StringTokenizer tokens, int position) {
        int cursorPosition = position;
        Command command = getFullCommand(input, availableCommands, cursorPosition);

        if (command != null) {
            if (command.equals(RouterCommand.NETWORK)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    if (isValidQuartet(arg.toString(), cursorPosition)) {
                        router2600.addRIPNetwork(arg.toString());
                    }
                } else {
                    showIncompleteCommandError();
                }
            }
            if (command.equals(RouterCommand.EXIT )) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() == 0) {
                    setMode(CONFIGURATION_MODE);
                } else {
                    showIncompleteCommandError();
                }
            }
            
            
        }
    }

    /***************************************************************************
     * OTHER SHOW METHODS
     **************************************************************************/
    private void showInterfaceState(String state) {
        Calendar calendar = Calendar.getInstance();
        String time = "" + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);

        if (state.equals(Interface.UP)) {
            textArea.append("\n" + time + " %LINK-3-UPDOWN:  Interface " + currentInterface.toString() + ", changed state to up");
            textArea.append("\n" + time + " %LINEPROTO-5-UPDOWN: Line protocol on Interface " + currentInterface.toString() + ", changed state to up");
        } else if (state.equals(Interface.ADMINISTRATIVELY_DOWN)) {
            textArea.append("\n" + time + " %LINK-3-UPDOWN:  Interface " + currentInterface.toString() + ", changed state to administratively down");
            textArea.append("\n" + time + " %LINEPROTO-5-UPDOWN: Line protocol on Interface " + currentInterface.toString() + ", changed state to down");
        }

        textArea.append("\n");
    }

    private void showIPInterfaceBrief() {
        Interface[] interfaces = router2600.getInterfaces();
        String[] header = {
            "Interface", "IP Address", "OK?", "Method", "Status", "Protocol"
        };

        for (int i = 0; i < (interfaces.length + 1); i++) {
            textArea.append("\n");

            for (int j = 0; j < 6; j++) {
                String output = "";

                if (i == 0) {
                    output = header[j];
                } else {
                    switch (j) {
                    case 0:
                        output = interfaces[i - 1].getName();

                        break;

                    case 1:
                        output = interfaces[i - 1].getIPAddress().toString();

                        if (output.equals("")) {
                            output = "unassigned";
                        }

                        break;

                    case 2:
                        output = "YES";

                        break;

                    case 3:
                        output = "unset";

                        break;

                    case 4:
                        output = interfaces[i - 1].getState();

                        break;

                    case 5:
                        output = interfaces[i - 1].getProtocoStatus();

                        break;

                    default:
                    }
                }

                textArea.append(output);

                int column = 0;

                switch (j) {
                case 0:
                    column = IP_ADDRESS_COLUMN;

                    break;

                case 1:
                    column = INTERFACE_OK_COLUMN;

                    break;

                case 2:
                    column = INTERFACE_METHOD_COLUMN;

                    break;

                case 3:
                    column = INTERFACE_STATUS_COLUMN;

                    break;

                case 4:
                    column = PROTOCOL_STATUS_COLUMN;

                    break;

                default:
                }

                for (int k = output.length(); k < column; k++) {
                    textArea.append(" ");
                }
            }
        }
    }
    
    private void showIPInterface() {
        Interface[] interfaces = router2600.getInterfaces();

        for (int i = 0; i < interfaces.length; i++) {
            textArea.append("\n" + interfaces[i].getName() + " is " + interfaces[i].getState() + ", line protocol is " + interfaces[i].getProtocoStatus()+ "\n");
        }
    }

    private void showIPInterfaceArg(Interface inter) {
        Interface[] interfaces = router2600.getInterfaces();

        for (int i = 0; i < interfaces.length; i++) {
        	if (interfaces[i].getName().equals(inter.getName())){
            textArea.append("\n" + interfaces[i].getName() + " is " + interfaces[i].getState() + ", line protocol is " + interfaces[i].getProtocoStatus()+ "\n");
        	}
       }
    }

    private void showInterfaces() {
        Interface[] interfaces = router2600.getInterfaces();

        for (int i = 0; i < interfaces.length; i++) {
            textArea.append("\n" + interfaces[i].getName() + " is " + interfaces[i].getState() + ", line protocol is " + interfaces[i].getProtocoStatus());
            textArea.append(interfaces[i].getDescription());
        }
    }

    private void showInterfacesArg(Interface inter) {
        Interface[] interfaces = router2600.getInterfaces();

        for (int i = 0; i < interfaces.length; i++) {
        	
        	if (interfaces[i].getName().equals(inter.getName())){
            textArea.append("\n" + interfaces[i].getName() + " is " + interfaces[i].getState() + ", line protocol is " + interfaces[i].getProtocoStatus());
            textArea.append(interfaces[i].getDescription());
        	}
        }
    }
    
    private void showRunningConfig() {
        Interface[] interfaces = router2600.getInterfaces();
        textArea.append("Building COnfiguration...\n");
        textArea.append("\n");
        textArea.append("!\n");
        textArea.append("Version 1.0\n");
        textArea.append("service timestamps debug uptime\n");
        textArea.append("service timestamps log uptime\n");
        textArea.append("no service password-encryption\n");
        textArea.append("!\n");
        textArea.append("hostname " + router2600.getName() + "\n");
        textArea.append("enable secret " + router2600.getSecret().length() + " <insert encrypted password here!>\n");
        textArea.append("enable password " + router2600.getPassword() + "\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("ip subnet-zero\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");

        for (int i = 0; i < interfaces.length; i++) {
            textArea.append("interface " + interfaces[i].getName() + "\n");

            String state = interfaces[i].getState();

            if ((state.equalsIgnoreCase("administratively down")) || (state.equalsIgnoreCase("down"))) {
                textArea.append(" no ip address\n");
                textArea.append(" no ip directed-broadcast\n");
                textArea.append(" backup interface\n");
                textArea.append(" shutdown\n");
            } else {
                textArea.append(" ip address " + interfaces[i].getIPAddress().toString() + " " + interfaces[i].getSubnetMask().toString() + "\n");
                textArea.append(" no ip directed-broadcast\n");
                textArea.append(" bandwidth 10000\n");
                textArea.append(" backup interface\n");
                textArea.append(" no fair-queue\n");
            }

            textArea.append("!\n");
        }

        textArea.append("!\n");
        textArea.append("ip classless\n");
        textArea.append("no ip http server\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("!\n");
        textArea.append("line con 0\n");
        textArea.append(" transport input none\n");
        textArea.append("line aux 0\n");
        textArea.append("line vty 0 4\n");
        textArea.append("!\n");
        textArea.append("no scheduler allocate\n");
        textArea.append("end\n");
        textArea.append("\n");
    }
}
