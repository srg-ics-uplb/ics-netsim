package devices.switches.consoles;

import java.util.StringTokenizer;

import platform.gui.MainFrame;
import devices.addresses.IPAddress;
import devices.commands.Command;
import devices.interfaces.FastEthernet;
import devices.interfaces.Interface;
import devices.switches.Switch2950;
import devices.switches.consoles.commands.ConfigurationCommand;
import devices.switches.consoles.commands.InterfaceCommand;
import devices.switches.consoles.commands.PrivilegedCommand;
import devices.switches.consoles.commands.Switch2950Command;
import devices.switches.consoles.commands.UserCommand;
import devices.switches.consoles.commands.VLANCommand;
import devices.switches.vlan.VLAN;


public class Switch2950Console extends SwitchConsole {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1934852879921967839L;
	protected final static String INITIAL_MODE_PROMPT = "\n\n\nSwitch con0 is now available\n\n\nPress RETURN to get started.\n\n\n";
    private Switch2950 switch2950;
        
    public Switch2950Console(Switch2950 switch2950, MainFrame frame) {
        super(switch2950, frame);
        this.switch2950 = switch2950;
    }

    public void setMode(int mode) {
        currentMode = mode;

        if (currentMode == USER_MODE) {
            currentPrompt = switch2950.getName() + USER_MODE_PROMPT;
            availableCommands = Switch2950Command.USER_MODE_COMMANDS;
        } else if (currentMode == PRIVILEGED_MODE) {
            currentPrompt = switch2950.getName() + PRIVILEGED_MODE_PROMPT;
            availableCommands = Switch2950Command.PRIVILEGED_MODE_COMMANDS;
        } else if (currentMode == CONFIGURATION_MODE) {
            currentPrompt = switch2950.getName() + CONFIGURATION_MODE_PROMPT;
            availableCommands = Switch2950Command.CONFIGURATION_MODE_COMMANDS;
        } else if (currentMode == PASSWORD_MODE) {
            currentPrompt = PASSWORD_MODE_PROMPT;
            availableCommands = null;
        } else if (currentMode == INTERFACE_MODE) {
            currentPrompt = switch2950.getName() + INTERFACE_MODE_PROMPT;
            availableCommands = Switch2950Command.INTERFACE_MODE_COMMANDS;
        } else if (currentMode == INITIAL_MODE) {
            textArea.setText("");
            currentPrompt = INITIAL_MODE_PROMPT;
            availableCommands = null;
        } else if (currentMode == VLAN_MODE) {
            currentPrompt = switch2950.getName() + VLAN_MODE_PROMPT;
            availableCommands = Switch2950Command.VLAN_MODE_COMMANDS;
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
            } else if (currentMode == VLAN_MODE) {
                processVLANCommand(command, tokens, position);
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
                    if (switch2950.isPasswordEnabled() || switch2950.isSecretEnabled()) {
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
            }            }else if (command.equals(UserCommand.PING)) {
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

    

    private void processPrivilegedCommand(String input, StringTokenizer tokens, int position) {
    	int cursorPosition = position;
        Command command = getFullCommand(input, availableCommands, cursorPosition);

        if (command != null) {
            if (command.equals(PrivilegedCommand.CONFIGURE)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Switch2950Command.PRIVILEGED_CONFIGURE_ARGUMENTS, cursorPosition);

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
            } else if (command.equals(PrivilegedCommand.VLAN)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = getFullCommand(arg.toString(), Switch2950Command.PRIVILEGED_VLAN_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(PrivilegedCommand.VLAN_DATABASE)) {
                            StringBuffer extras = new StringBuffer();
                            int extrasPosition = getNextPosition(tokens, extras);
                            cursorPosition = arg.length() + extrasPosition;

                            if (extras.length() != 0) {
                                showInvalidInputError(cursorPosition);
                            } else {
                                setMode(VLAN_MODE);
                            }
                        }
                    }
                } else {
                    showIncompleteCommandError();
                }
            } 
            else if (command.equals(PrivilegedCommand.DISABLE)) {
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
                    Command argument = getFullCommand(arg.toString(), Switch2950Command.PRIVILEGED_SHOW_ARGUMENTS, cursorPosition);

                    if (argument != null) {
                        if (argument.equals(PrivilegedCommand.SHOW_IP)) {
                        	 StringBuffer arg2 = new StringBuffer();
                             int arg2Position = getNextPosition(tokens, arg2);
                             cursorPosition += (arg.length() + arg2Position);

                             if (arg2.length() != 0) {  
                                 showInvalidInputError(cursorPosition);
                             } else {
//                                 showIP();
                             }
                        }else if (argument.equals(PrivilegedCommand.SHOW_RUNNING_CONFIG)) {
                       	 StringBuffer arg2 = new StringBuffer();
                         int arg2Position = getNextPosition(tokens, arg2);
                         cursorPosition += (arg.length() + arg2Position);

                         if (arg2.length() != 0) {  
                             showInvalidInputError(cursorPosition);
                         } else {
                             showRunningConfig();
                          }
                        }else if (argument.equals(PrivilegedCommand.VLAN)) {
                          	 StringBuffer arg2 = new StringBuffer();
                             int arg2Position = getNextPosition(tokens, arg2);
                             cursorPosition += (arg.length() + arg2Position);

                             if (arg2.length() != 0) {  
                                 showInvalidInputError(cursorPosition);
                             } else {
                                 showVLAN();
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
                     Command argument = getFullCommand(arg.toString(), Switch2950Command.CONFIGURATION_ENABLE_ARGUMENTS, cursorPosition);

                     if (argument != null) {
                         if (argument.equals(ConfigurationCommand.ENABLE_PASSWORD)) {
                             StringBuffer arg2 = new StringBuffer();
                             int arg2Position = getNextPosition(tokens, arg2);
                             cursorPosition += (arg.length() + arg2Position);

                             if (arg2.length() != 0) {
                                 Command argument2 = getFullCommand(arg2.toString(), Switch2950Command.CONFIGURATION_ENABLE_PASSWORD_ARGUMENTS, cursorPosition);

                                 if (argument2 != null) {
                                     StringBuffer arg3 = new StringBuffer();
                                     int arg3Position = getNextPosition(tokens, arg3);
                                     cursorPosition += (arg2.length() + arg3Position);

                                     if (arg3.length() != 0) {
                                         Command argument3 = getFullCommand(arg3.toString(), Switch2950Command.CONFIGURATION_ENABLE_PASSWORD_LEVEL_ARGUMENTS, cursorPosition);

                                         if (argument3 != null) {
                                             StringBuffer password = new StringBuffer();
                                             int passwordPosition = getNextPosition(tokens, password);
                                             cursorPosition += (arg3.length() + passwordPosition);

                                             if (password.length() != 0) {
                                                 StringBuffer extras = new StringBuffer();
                                                 int extrasPosition = getNextPosition(tokens, extras);
                                                 cursorPosition += (password.length() + extrasPosition);

                                                 if (extrasPosition == 0) {
                                                     switch2950.setPassword(password.toString());
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
                                 Command argument2 = getFullCommand(arg2.toString(), Switch2950Command.CONFIGURATION_ENABLE_SECRET_ARGUMENTS, cursorPosition);

                                 if (argument2 != null) {
                                     StringBuffer arg3 = new StringBuffer();
                                     int arg3Position = getNextPosition(tokens, arg3);
                                     cursorPosition += (arg2.length() + arg3Position);

                                     if (arg3.length() != 0) {
                                         Command argument3 = getFullCommand(arg3.toString(), Switch2950Command.CONFIGURATION_ENABLE_SECRET_LEVEL_ARGUMENTS, cursorPosition);

                                         if (argument3 != null) {
                                             StringBuffer secret = new StringBuffer();
                                             int secretPosition = getNextPosition(tokens, secret);
                                             cursorPosition += (arg3.length() + secretPosition);

                                             if (secret.length() != 0) {
                                                 StringBuffer extras = new StringBuffer();
                                                 int extrasPosition = getNextPosition(tokens, extras);
                                                 cursorPosition += (secret.length() + extrasPosition);

                                                 if (extrasPosition == 0) {
                                                     switch2950.setSecret(secret.toString());
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
                         switch2950.setName(arg.toString());
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
                     Command argument = getFullCommand(arg.toString(), Switch2950Command.CONFIGURATION_IP_ARGUMENTS, cursorPosition);

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
                                                 switch2950.setIPAddress(ipAddressDecimals);
                                                 switch2950.setSubnetMask(subnetDecimals);
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
                                         switch2950.setDefaultGateway(arg2.toString());
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
             }
         }
    }

    private void processInterfaceCommand(String input, StringTokenizer tokens, int position) {
    	 int cursorPosition = position;
         Command command = getFullCommand(input, availableCommands, cursorPosition);

         if (command != null) {
             if (command.equals(InterfaceCommand.SWITCHPORT)) {
                 StringBuffer arg = new StringBuffer();
                 int argPosition = getNextPosition(tokens, arg);
                 cursorPosition += (input.length() + argPosition);

                 if (arg.length() != 0) {
                     Command argument = getFullCommand(arg.toString(), Switch2950Command.INTERFACE_SWITCHPORT_ARGUMENTS, cursorPosition);
                    
                     if (argument != null) {
                         if (argument.equals(InterfaceCommand.SWITCHPORT_ACCESS)) {
                             StringBuffer arg2 = new StringBuffer();
                             int arg2Position = getNextPosition(tokens, arg2);
                             cursorPosition += (arg2.length() + arg2Position);
                             
                             if (arg2.length() != 0) {
                            	 
                            	 argument = getFullCommand(arg2.toString(), Switch2950Command.INTERFACE_SWITCHPORT_ACCESS_ARGUMENTS, cursorPosition);
                            	    
                            	 if (argument != null) {
                            		  
                            		 if (argument.equals(InterfaceCommand.SWITCHPORT_ACCESS_VLAN)) {
                            			 
                            			 StringBuffer arg3 = new StringBuffer();
                                         int arg3Position = getNextPosition(tokens, arg3);
                                         cursorPosition += (arg3.length() + arg3Position);
                                         
                                         if (arg3.length() != 0) {
                                        	     
                                        	     if (isInteger(arg3.toString(), cursorPosition)) {
                                                 int vlanIndex = Integer.parseInt(arg3.toString());

                                                 if (vlanIndex < 1) {
                                                     showInvalidInputError(cursorPosition);
                                                 } else if (vlanIndex > 1005) {
                                                     showInvalidInputError(cursorPosition + 3);
                                                 } else {
                                                     StringBuffer extras = new StringBuffer();
                                                     int extrasPosition = getNextPosition(tokens, extras);
                                                     cursorPosition += (arg3.length() + extrasPosition);

                                                     if (extras.length() == 0) {
                                                         VLAN[] vlans = switch2950.getVLANS();
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
                         } else if (argument.equals(InterfaceCommand.SWITCHPORT_MODE)){
                        	 StringBuffer arg2 = new StringBuffer();
                        	 int arg2Position = getNextPosition(tokens, arg2);
                        	 cursorPosition += arg2.length() + arg2Position;
                        	 if (arg2.length() != 0){
                        		 Command argument2 = getFullCommand(arg2.toString(), Switch2950Command.INTERFACE_SWITCHPORT_MODE_ARGUMENTS, cursorPosition);
                        		 if (argument2.equals(InterfaceCommand.SWITCHPORT_MODE_ACCESS)){
                        			 StringBuffer arg3 = new StringBuffer();
                        			 int arg3Position = getNextPosition(tokens, arg3);
                        			 cursorPosition += arg2.length() + arg3Position;
                        			 if (arg3.length() == 0){
                        				 ((FastEthernet)currentInterface).setTrunked(false);
                        			 }else{
                        				 showInvalidInputError(cursorPosition);
                        			 }
                        		 } else if (argument2.equals(InterfaceCommand.SWITCHPORT_MODE_TRUNK)){
                        			 StringBuffer arg3 = new StringBuffer();
                        			 int arg3Position = getNextPosition(tokens, arg3);
                        			 cursorPosition += arg2.length() + arg3Position;
                        			 if (arg3.length() == 0){
                        				 ((FastEthernet)currentInterface).setTrunked(true);
                        			 }else{
                        				 showInvalidInputError(cursorPosition);
                        			 }
                        		 }
                        	 }else{
                        		 showIncompleteCommandError();
                        	 }
                         }
                     }
                 } else {
                     showIncompleteCommandError();
                 }
             } else if (command.equals(InterfaceCommand.DESCRIPTION)) {


             }
         }

    }

    private void processVLANCommand(String input, StringTokenizer tokens, int position) {
   	    int cursorPosition = position;
        Command command = getFullCommand(input, availableCommands, cursorPosition);
       
        if (command != null) {
        	
        	if (command.equals(VLANCommand.EXIT)) {
                StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() == 0) {
                    setMode(PRIVILEGED_MODE);
                } else {
                    showInvalidInputError(cursorPosition);
                }
            } else if (command.equals(VLANCommand.VLAN)) {
            	StringBuffer arg = new StringBuffer();
                int argPosition = getNextPosition(tokens, arg);
                cursorPosition += (input.length() + argPosition);

                if (arg.length() != 0) {
                    Command argument = null;

                    if (!Character.isDigit(arg.charAt(0))) {
                        argument = getFullCommand(arg.toString(), Switch2950Command.VLAN_VLAN_ARGUMENTS, cursorPosition);
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
                                    Command argument2 = getFullCommand(arg2.toString(), Switch2950Command.VLAN_VLAN_1_1005_ARGUMENTS, cursorPosition);

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
                                                    VLAN[] vlans = switch2950.getVLANS();
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
                                                        switch2950.addVLAN(new VLAN(vlanIndex, name.toString()));
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
    
    private void processPasswordCommand(String input, StringTokenizer tokens, int position) {
    	if (switch2950.isSecretEnabled()) {
            if (input.equals(switch2950.getSecret())) {
                setMode(PRIVILEGED_MODE);
            } else {
                passwordTries++;
            }
        } else if (switch2950.isPasswordEnabled()) {
            if (input.equals(switch2950.getPassword())) {
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
    
    private void showRunningConfig() {
    	Interface[] interfaces = switch2950.getInterfaces();
    	textArea.append("!\n");
    	textArea.append("Version 12.1\n");
    	textArea.append("service timestamps debug uptime\n");
    	textArea.append("service timestamps log uptime\n");
    	textArea.append("no service password-encryption\n");
    	textArea.append("!\n");
    	textArea.append("hostname " + switch2950.getName() + "\n"  );
    	textArea.append("ip name-server " + switch2950.getIPAddress().toString() + "\n");
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
    	textArea.append("spanning-tree extend system-id\n");
    	textArea.append("!\n");
    	
    	for (int i=0;i<interfaces.length;i++){
    		textArea.append("interface " + interfaces[i].getName() + "\n");
    		textArea.append(" backup interface\n");
    		textArea.append(" no fair-queue\n");
    		textArea.append("!\n");
    	}
    	textArea.append(" vtp domain bigdomain!\n");
    	
    	VLAN[] vlan = switch2950.getVLANS();
    	
    	for (int i=0;i<vlan.length;i++){
    		textArea.append("interface Vlan " + vlan[i].getIndex() + "\n");
    		textArea.append(" no ip address\n");
    		textArea.append(" no ip route-cache\n");
    		textArea.append(" shutdown\n");
    		vlan[i].getPorts();
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
    	textArea.append("line vty 0 15\n");
    	textArea.append("!\n");
    	textArea.append("no scheduler allocate\n");
    	textArea.append("end\n");
    	
    }
    
    private void showVLAN() {
    	VLAN[] vlans = switch2950.getVLANS();
    	textArea.append("\nVLAN \tName\t\t\t\tStatus\tPorts\t\t\t\t\t\n");
    	textArea.append("------- ------------------------------- ------  ----------------------\n");
    	
        Interface[] interfaces = switch2950.getInterfaces();
        
    	for (int i=0;i<vlans.length;i++){
    		textArea.append(vlans[i].getIndex() + "\t");
    		if ((vlans[i].getName().equalsIgnoreCase(""))||(vlans[i].getName()==null)){
    			textArea.append("Default\t\t\t\t" + vlans[i].getStatus() + "\t"); 
    		}
    		else {
    			textArea.append(vlans[i].getName() + "\t\t\t\t" + vlans[i].getStatus() + "\t");
    		}
    		for (int j=0;j<interfaces.length;j++){
    			if (vlans[i].getIndex() == interfaces[j].getSwitchPort().getVLAN().getIndex()){
    				textArea.append(interfaces[j].getName());
    			}
    		}
    		textArea.append("\n");
    	}

//    	
//    	VLAN[] vlan = switch2950.getVLANS();
//    	
//    	for (int i=0;i<vlan.length;i++){
//    		textArea.append("interface Vlan " + vlan[i].getIndex() + "\n");
//    		textArea.append(" no ip address\n");
//    		textArea.append(" no ip route-cache\n");
//    		textArea.append(" shutdown\n");
//    	}
//    	textArea.append("!\n");
//    	textArea.append("ip classless\n");
//    	textArea.append("no ip http server\n");
//    	textArea.append("!\n");
//    	textArea.append("!\n");
//    	textArea.append("!\n");
//    	textArea.append("line con 0\n");
//    	textArea.append(" transport input none\n");
//    	textArea.append("line aux 0\n");
//    	textArea.append("line vty 0 15\n");
//    	textArea.append("!\n");
//    	textArea.append("no scheduler allocate\n");
//    	textArea.append("end\n");
    	
    }    
}
