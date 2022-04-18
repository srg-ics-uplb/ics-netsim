package devices.servers.consoles;

import devices.addresses.IPAddress;

import devices.commands.Command;

import devices.consoles.Console;

import devices.servers.Server;

import devices.servers.consoles.commands.ServerCommand;
import devices.servers.consoles.commands.UserCommand;

import platform.gui.MainFrame;

import java.util.StringTokenizer;


public class ServerConsole extends Console {
    

    /**
	 * 
	 */
	private static final long serialVersionUID = -837096981977289607L;

	public ServerConsole(Server server, MainFrame frame) {
        super(server, frame);
        removeKeyBindings();
    }

    public void initialize() {
        currentPrompt = "[user@localhost ~]$";
        currentMode = Console.USER_MODE;
        availableCommands = ServerCommand.USER_MODE_COMMANDS;
    }

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
            processUserCommand(command, tokens, position);
        }
    }

    private void processUserCommand(String input, StringTokenizer tokens, int position) {
        int cursorPosition = position;
        Command command = getFullCommand(input, availableCommands, cursorPosition);

        if (command != null) {
            if (command.equals(UserCommand.IFCONFIG)) {
                StringBuffer selectedInterface = new StringBuffer();
                int selectedInterfacePosition = getNextPosition(tokens, selectedInterface);
                cursorPosition += (input.length() + selectedInterfacePosition);

                if (selectedInterface.length() != 0) {
                    StringBuffer address = new StringBuffer();
                    int addressPosition = getNextPosition(tokens, address);
                    cursorPosition += (selectedInterface.length() + addressPosition);

                    if (address.length() != 0) {
                        if (isValidQuartet(address.toString(), cursorPosition)) {
                        }
                    } else {
                        showIncompleteCommandError();
                    }
                } else {
                    showIncompleteCommandError();
                }
            } else if (command.equals(UserCommand.PING)) {
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
}
