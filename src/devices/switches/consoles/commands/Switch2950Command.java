package devices.switches.consoles.commands;

import devices.commands.Command;

import devices.switches.consoles.commands.ConfigurationCommand;
import devices.switches.consoles.commands.InterfaceCommand;
import devices.switches.consoles.commands.PrivilegedCommand;
import devices.switches.consoles.commands.UserCommand;


public class Switch2950Command {
    public final static Command[] USER_MODE_COMMANDS = {
        UserCommand.ENABLE, UserCommand.EXIT, UserCommand.LOGOUT,
        UserCommand.PING, UserCommand.TELNET, UserCommand.SHOW,
        UserCommand.TRACERT, UserCommand.HELP, UserCommand.ACCESS_ENABLE,
        UserCommand.CONNECT, UserCommand.LOCK, UserCommand.RCOMMAND,
        UserCommand.SYSTAT, UserCommand.TUNNEL
    };
    public final static Command[] PRIVILEGED_MODE_COMMANDS = {
        PrivilegedCommand.CLEAR, PrivilegedCommand.CONFIGURE,
        PrivilegedCommand.COPY, PrivilegedCommand.DELETE,
        PrivilegedCommand.DISABLE, PrivilegedCommand.RELOAD,
        PrivilegedCommand.CLOCK, PrivilegedCommand.DEBUG,
        PrivilegedCommand.UNDEBUG, PrivilegedCommand.ERASE,
        PrivilegedCommand.VLAN, PrivilegedCommand.HELP,
        PrivilegedCommand.ARCHIVE, PrivilegedCommand.CD, PrivilegedCommand.CNS,
        PrivilegedCommand.CONNECT, PrivilegedCommand.DIR,
        PrivilegedCommand.DOT1X, PrivilegedCommand.FORMAT,
        PrivilegedCommand.FSCK, PrivilegedCommand.MKDIR,
        PrivilegedCommand.NAME_CONNECTION, PrivilegedCommand.PWD,
        PrivilegedCommand.RENAME, PrivilegedCommand.RMDIR,
        PrivilegedCommand.EXIT, PrivilegedCommand.PING, PrivilegedCommand.RELOAD,
        PrivilegedCommand.SHOW, PrivilegedCommand.TRACERT
    };
    public final static Command[] CONFIGURATION_MODE_COMMANDS = {
        ConfigurationCommand.BANNER, ConfigurationCommand.CDP,
        ConfigurationCommand.ENABLE, ConfigurationCommand.END,
        ConfigurationCommand.EXIT, ConfigurationCommand.HOSTNAME,
        ConfigurationCommand.INTERFACE, ConfigurationCommand.IP,
        ConfigurationCommand.LINE, ConfigurationCommand.MAC_ADDRESS_TABLE,
        ConfigurationCommand.RIP, ConfigurationCommand.SPANTREE,
        ConfigurationCommand.SWITCHING_MODE, ConfigurationCommand.UPLINK_FAST,
        ConfigurationCommand.VLAN, ConfigurationCommand.VLAN_MEMBERSHIP,
        ConfigurationCommand.VTP
    };
    public final static Command[] INTERFACE_MODE_COMMANDS = {
    	InterfaceCommand.DUPLEX, InterfaceCommand.DESCRIPTION, 
    	InterfaceCommand.EXIT, InterfaceCommand.HELP, 
    	InterfaceCommand.IP, InterfaceCommand.SHUTDOWN,
    	InterfaceCommand.SPEED,InterfaceCommand.SWITCHPORT,    
    };
    public final static Command[] VLAN_MODE_COMMANDS = {
        VLANCommand.VLAN, VLANCommand.ABORT, VLANCommand.APPLY, VLANCommand.EXIT,
        VLANCommand.HELP, VLANCommand.VTP
    };
    public final static Command[] CONFIGURATION_ENABLE_ARGUMENTS = {
        ConfigurationCommand.ENABLE_PASSWORD, ConfigurationCommand.ENABLE_SECRET
    };
    public final static Command[] CONFIGURATION_ENABLE_PASSWORD_ARGUMENTS = {
        ConfigurationCommand.ENABLE_PASSWORD_LEVEL
    };
    public final static Command[] CONFIGURATION_ENABLE_PASSWORD_LEVEL_ARGUMENTS = {
        ConfigurationCommand.ENABLE_SECRET_LEVEL_15
    };
    public final static Command[] CONFIGURATION_ENABLE_PASSWORD_LEVEL_15_ARGUMENTS = {
        ConfigurationCommand.ENABLE_PASSWORD_LEVEL_15_WORD
    };
    public final static Command[] CONFIGURATION_ENABLE_SECRET_ARGUMENTS = {
        ConfigurationCommand.ENABLE_PASSWORD_LEVEL
    };
    public final static Command[] CONFIGURATION_ENABLE_SECRET_LEVEL_ARGUMENTS = {
        ConfigurationCommand.ENABLE_SECRET_LEVEL_15
    };
    public final static Command[] CONFIGURATION_ENABLE_SECRET_LEVEL_15_ARGUMENTS = {
        ConfigurationCommand.ENABLE_PASSWORD_LEVEL_15_WORD
    };
    public final static Command[] CONFIGURATION_IP_ARGUMENTS = {
        ConfigurationCommand.IP_ADDRESS, ConfigurationCommand.IP_DEFAULT_GATEWAY,
        ConfigurationCommand.IP_DOMAIN_NAME, ConfigurationCommand.IP_NAME_SERVER
    };
    public final static Command[] CONFIGURATION_IP_ADDRESS_ARGUMENTS = {
        ConfigurationCommand.IP_ADDRESS_ABCD
    };
    public final static Command[] CONFIGURATION_IP_ADDRESS_ABCD_ARGUMENTS = {
        ConfigurationCommand.IP_ADDRESS_ABCD_ABCD
    };
    public final static Command[] CONFIGURATION_IP_DEFAULT_GATEWAY_ARGUMENTS = {
        ConfigurationCommand.IP_DEFAULT_GATEWAY_ABCD
    };
    public final static Command[] CONFIGURATION_INTERFACE_ARGUMENTS = {
        ConfigurationCommand.INTERFACE_ETHERNET
    };
    public final static Command[] INTERFACE_SWITCHPORT_ARGUMENTS = {
        InterfaceCommand.SWITCHPORT_PORTSECURITY, InterfaceCommand.SWITCHPORT_ACCESS,
        InterfaceCommand.SWITCHPORT_MODE, InterfaceCommand.SWITCHPORT_TRUNK
    };
    public final static Command[] INTERFACE_SWITCHPORT_ACCESS_ARGUMENTS = {
        InterfaceCommand.SWITCHPORT_ACCESS_VLAN
    };
    public final static Command[] INTERFACE_SWITCHPORT_MODE_ARGUMENTS = {
    	InterfaceCommand.SWITCHPORT_MODE_ACCESS, InterfaceCommand.SWITCHPORT_MODE_DYNAMIC,
    	InterfaceCommand.SWITCHPORT_MODE_TRUNK
    };
    public final static Command[] PRIVILEGED_CONFIGURE_ARGUMENTS = {
        PrivilegedCommand.CONFIGURE_TERMINAL
    };
    public final static Command[] PRIVILEGED_SHOW_ARGUMENTS = {
        PrivilegedCommand.SHOW_IP, PrivilegedCommand.SHOW_RUNNING_CONFIG,
        PrivilegedCommand.VLAN
    };
    public final static Command[] PRIVILEGED_VLAN_ARGUMENTS = {
    	PrivilegedCommand.VLAN_DATABASE
    };
    public final static Command[] VLAN_VLAN_ARGUMENTS = {
    	VLANCommand.VLAN_1_1005
    };
    public final static Command[] VLAN_VLAN_1_1005_ARGUMENTS = {
    	VLANCommand.VLAN_1_1005_NAME
    };
    static {
        ConfigurationCommand.ENABLE.setArguments(CONFIGURATION_ENABLE_ARGUMENTS);
        ConfigurationCommand.ENABLE_PASSWORD.setArguments(CONFIGURATION_ENABLE_PASSWORD_ARGUMENTS);
        ConfigurationCommand.ENABLE_PASSWORD_LEVEL.setArguments(CONFIGURATION_ENABLE_PASSWORD_LEVEL_ARGUMENTS);
        ConfigurationCommand.ENABLE_PASSWORD_LEVEL_15.setArguments(CONFIGURATION_ENABLE_PASSWORD_LEVEL_15_ARGUMENTS);
        ConfigurationCommand.ENABLE_SECRET.setArguments(CONFIGURATION_ENABLE_SECRET_ARGUMENTS);
        ConfigurationCommand.ENABLE_SECRET_LEVEL.setArguments(CONFIGURATION_ENABLE_SECRET_LEVEL_ARGUMENTS);
        ConfigurationCommand.ENABLE_SECRET_LEVEL_15.setArguments(CONFIGURATION_ENABLE_SECRET_LEVEL_15_ARGUMENTS);
        ConfigurationCommand.IP.setArguments(CONFIGURATION_IP_ARGUMENTS);
        ConfigurationCommand.IP_ADDRESS.setArguments(CONFIGURATION_IP_ADDRESS_ARGUMENTS);
        ConfigurationCommand.IP_ADDRESS_ABCD.setArguments(CONFIGURATION_IP_ADDRESS_ABCD_ARGUMENTS);
        ConfigurationCommand.IP_DEFAULT_GATEWAY.setArguments(CONFIGURATION_IP_DEFAULT_GATEWAY_ARGUMENTS);
        ConfigurationCommand.INTERFACE.setArguments(CONFIGURATION_INTERFACE_ARGUMENTS);
        InterfaceCommand.SWITCHPORT.setArguments(INTERFACE_SWITCHPORT_ARGUMENTS);
        InterfaceCommand.SWITCHPORT_ACCESS.setArguments(INTERFACE_SWITCHPORT_ACCESS_ARGUMENTS);
        InterfaceCommand.SWITCHPORT_MODE.setArguments(INTERFACE_SWITCHPORT_MODE_ARGUMENTS);
        PrivilegedCommand.CONFIGURE.setArguments(PRIVILEGED_CONFIGURE_ARGUMENTS);
        PrivilegedCommand.VLAN.setArguments(PRIVILEGED_VLAN_ARGUMENTS);
        VLANCommand.VLAN.setArguments(VLAN_VLAN_ARGUMENTS);
        VLANCommand.VLAN_1_1005.setArguments(VLAN_VLAN_1_1005_ARGUMENTS);
    }

    public static void loadCommands() {
    }
}
