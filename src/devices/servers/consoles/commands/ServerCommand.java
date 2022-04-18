package devices.servers.consoles.commands;

import devices.commands.Command;


public class ServerCommand {
    public final static Command[] USER_MODE_COMMANDS = {
        UserCommand.IFCONFIG, UserCommand.PING, UserCommand.TRACERT
    };
}
