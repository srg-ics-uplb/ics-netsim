package devices.routers.consoles.commands;

import devices.commands.Command;


public class UserCommand {
    public final static Command ACCESS_PROFILE = new Command("access-profile", "Apply user-profile to interface");
    public final static Command ACCESS_ENABLE = new Command("access-enable", "Create a temporary Access-List entry");
    public final static Command CONNECT = new Command("connect", "Open a terminal connection");
    public final static Command DISABLE = new Command("disable", "Turn off privileged commands");
    public final static Command DISCONNECT = new Command("disconnect", "Disconnect an existing network connection");
    public final static Command ENABLE = new Command("enable", "Turn on privileged commands");
    public final static Command EXIT = new Command("exit", "Exit from the EXEC");
    public final static Command HELP = new Command("help", "Description of the interactive help system");
    public final static Command LOCK = new Command("lock", "Lock the terminal");
    public final static Command LOGIN = new Command("login", "Log in as a particular user");
    public final static Command LOGOUT = new Command("logout", "Exit from the EXEC");
    public final static Command MRINFO = new Command("mrinfo", "Request neighbor and version information from a multicast router");
    public final static Command MSTAT = new Command("mstat", "Show statistics after multiple multicast tracerts");
    public final static Command MTRACE = new Command("mtrace", "Trace reverse multicast path from destination to source");
    public final static Command NAME_CONNECTION = new Command("name-connection", "Name and existing network connection");
    public final static Command PAD = new Command("pad", "Open a X.29 PAD connection");
    public final static Command PING = new Command("ping", "Send echo messages");
    public final static Command PPP = new Command("ppp", "Start IETF Point-to-Point Protocol (PPP)");
    public final static Command RESUME = new Command("resume", "Resume an active network connection");
    public final static Command RLOGIN = new Command("rlogin", "Open an rlogin connection");
    public final static Command SHOW = new Command("show", "Show running system information");
    public final static Command SLIP = new Command("slip", "Start a Serial-line IP (SLIP)");
    public final static Command SYSTAT = new Command("systat", "Display information about terminal lines");
    public final static Command TERMINAL = new Command("terminal", "Set terminal line parameters");
    public final static Command TRACERT = new Command("traceri", "Trace route to destination");
    public final static Command TUNNEL = new Command("tunnel", "Open a tunnel connection");
    public final static Command UDPTN = new Command("udptn", "Open an updtn connection");
    public final static Command X28 = new Command("x28", "Become an X.28 PAD");
    public final static Command X3 = new Command("x3", "Set X.3 parameters on PAD");
}
