package devices.servers.consoles.commands;

import devices.commands.Command;


public class UserCommand {
    public final static Command IFCONFIG = new Command("ifconfig", "");
    public final static Command PING = new Command("ping", "Send echo messages");
    public final static Command TRACERT = new Command("tracert", "trace");
}
