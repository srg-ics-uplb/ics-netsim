package devices.switches.consoles.commands;

import devices.commands.Command;


public class UserCommand {
    public final static Command ENABLE = new Command("enable", "Turn on privileged commands");
    public final static Command EXIT = new Command("exit", "Exit from the EXEC");
    public final static Command LOGOUT = new Command("logout", "Exit from the EXEC");
    public final static Command PING = new Command("ping", "Send echo messages");
    public final static Command TELNET = new Command("telnet", "Open a telnet connection");
    public final static Command SHOW = new Command("show", "Show running system information");
    public final static Command TRACERT = new Command("tracert", "Trace route to destination");
    public final static Command HELP = new Command("help", "Description of the interactive help system");
    public final static Command ACCESS_ENABLE = new Command("access-enable", "Create a temporary Access-List entry");
    public final static Command CONNECT = new Command("connect", "Open a terminal connection");
    public final static Command LOCK = new Command("lock", "Lock the terminal");
    public final static Command RCOMMAND = new Command("rcommand", "Run command on remote switch");
    public final static Command SYSTAT = new Command("systat", "Display information about terminal lines");
    public final static Command TUNNEL = new Command("tunnel", "Open a tunnel connection");
}
