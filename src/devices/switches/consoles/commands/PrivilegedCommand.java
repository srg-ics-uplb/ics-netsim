package devices.switches.consoles.commands;

import devices.commands.Command;


public class PrivilegedCommand {
    public final static Command CLEAR = new Command("clear", "Reset functions");
    public final static Command CONFIGURE = new Command("configure", "Enter configuration mode");
    public final static Command CONFIGURE_TERMINAL = new Command("terminal", "");
    public final static Command COPY = new Command("copy", "Copy configuration or firmware");
    public final static Command DELETE = new Command("delete", "Reset configuration");
    public final static Command DISABLE = new Command("disable", "Turn off privileged commands");
    public final static Command EXIT = new Command("exit", "Exit from the EXEC");
    public final static Command PING = new Command("ping", "Send echo messages");
    public final static Command RELOAD = new Command("reload", "Halt and perform warm start");
    public final static Command SHOW = new Command("show", "Show running system information");
    public final static Command SHOW_IP = new Command("ip", "Show IP information");
    public final static Command SHOW_RUNNING_CONFIG = new Command("running-config", "Current operating configuration");
    public final static Command SHOW_VLAN = new Command("vlan", "VTP VLAN status");
    public final static Command TRACERT = new Command("tracert", "Trace route to destination");
    public final static Command CLOCK = new Command("clock", "");
    public final static Command DEBUG = new Command("debug", "Debugging functions (see also 'undebug')");
    public final static Command UNDEBUG = new Command("undebug", "Disable debugging functions (see also 'debug')");
    public final static Command ERASE = new Command("erase", "Erase a filesystem");
    public final static Command VLAN = new Command("vlan", "Configure VLAN parameters");
    public final static Command VLAN_DATABASE = new Command("database", "Configure VLAN database");    
    public final static Command HELP = new Command("help", "Description of the interactive help system");
    public final static Command ARCHIVE = new Command("archive", "manage archive files");
    public final static Command CD = new Command("cd", "Change current directory");
    public final static Command CNS = new Command("cns", "CNS Subsystem");
    public final static Command CONNECT = new Command("connect", "Open a terminal connection");
    public final static Command DIR = new Command("dir", "List files on a filesystem");
    public final static Command DOT1X = new Command("dot1x", "IEEE 802.1X commands");
    public final static Command FORMAT = new Command("format", "Format a filesystem");
    public final static Command FSCK = new Command("fsck", "Fsck a filesystem");
    public final static Command MKDIR = new Command("mkdir", "Create new directory");
    public final static Command NAME_CONNECTION = new Command("name-connection", "Name an existing network connection");
    public final static Command PWD = new Command("pwd", "Display current working directory");
    public final static Command RENAME = new Command("rename", "Rename a file");
    public final static Command RMDIR = new Command("rmdir", "Remove existing directory");
    public final static Command RSH = new Command("rsh", "Execute a remote command");
    public final static Command SEND = new Command("send", "Send a message to other tty lines");
    public final static Command UDLD = new Command("udld", "UDLD Protocol Commands");
    public final static Command VERIFY = new Command("verify", "Verify a file");
    public final static Command VMPS = new Command("vmps", "VMPS actions");
    public final static Command WHERE = new Command("where", "List active connection");
    public final static Command TELNET = new Command("telnet", "Open a telnet connection");
}
