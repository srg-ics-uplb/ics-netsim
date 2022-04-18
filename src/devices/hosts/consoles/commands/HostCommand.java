package devices.hosts.consoles.commands;

import devices.commands.Command;


public class HostCommand {
    public final static Command[] USER_MODE_COMMANDS = {
        UserCommand.IFCONFIG, UserCommand.PING, UserCommand.TRACERT
    };
}
