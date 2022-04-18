package devices.switches.consoles.commands;

import devices.commands.Command;

import devices.switches.consoles.commands.ConfigurationCommand;
import devices.switches.consoles.commands.InterfaceCommand;
import devices.switches.consoles.commands.PrivilegedCommand;
import devices.switches.consoles.commands.UserCommand;


public class Switch1900Command {
    public final static Command[] USER_MODE_COMMANDS = {
        UserCommand.ENABLE, UserCommand.EXIT, UserCommand.HELP, UserCommand.PING,
        UserCommand.SHOW, UserCommand.TRACERT
    };
    public final static Command[] PRIVILEGED_MODE_COMMANDS = {
        PrivilegedCommand.CLEAR, PrivilegedCommand.CONFIGURE,
        PrivilegedCommand.COPY, PrivilegedCommand.DELETE,
        PrivilegedCommand.DISABLE, PrivilegedCommand.EXIT,
        PrivilegedCommand.PING, PrivilegedCommand.RELOAD, PrivilegedCommand.SHOW,
        PrivilegedCommand.TRACERT
    };
    public final static Command[] CONFIGURATION_MODE_COMMANDS = {
        ConfigurationCommand.BANNER, ConfigurationCommand.CDP,
        ConfigurationCommand.ENABLE, ConfigurationCommand.END,
        ConfigurationCommand.EXIT, ConfigurationCommand.HOSTNAME,
        ConfigurationCommand.INTERFACE, ConfigurationCommand.IP,
        ConfigurationCommand.LINE, ConfigurationCommand.MAC_ADDRESS_TABLE,
        ConfigurationCommand.RIP, ConfigurationCommand.SERVICE,
        ConfigurationCommand.SPANTREE, ConfigurationCommand.SWITCHING_MODE,
        ConfigurationCommand.UPLINK_FAST, ConfigurationCommand.VLAN,
        ConfigurationCommand.VLAN_MEMBERSHIP, ConfigurationCommand.VTP
    };
    public final static Command[] INTERFACE_MODE_COMMANDS = {
        InterfaceCommand.CDP, InterfaceCommand.DESCRIPTION,
        InterfaceCommand.DUPLEX, InterfaceCommand.EXIT, InterfaceCommand.PORT,
        InterfaceCommand.SHUTDOWN, InterfaceCommand.SPANTREE,
        InterfaceCommand.VLAN_MEMBERSHIP
    };
    public final static Command[] INTERFACE_MODE_FASTETHERNET_COMMANDS = {
        InterfaceCommand.CDP, InterfaceCommand.DESCRIPTION,
        InterfaceCommand.DUPLEX, InterfaceCommand.EXIT, InterfaceCommand.PORT,
        InterfaceCommand.SHUTDOWN, InterfaceCommand.SPANTREE,
        InterfaceCommand.TRUNK, InterfaceCommand.TRUNK_VLAN,
        InterfaceCommand.VLAN_MEMBERSHIP, InterfaceCommand.VTP
    };
    
    /**************************************************************************
     * COMMAND ARGUMENTS
     **************************************************************************/    
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
    public final static Command[] CONFIGURATION_VLAN_ARGUMENTS = {
        ConfigurationCommand.VLAN_1_1001, ConfigurationCommand.VLAN_SERVER
    };
    public final static Command[] CONFIGURATION_VLAN_1_1001_ARGUMENTS = {
        ConfigurationCommand.VLAN_1_1001_ETHERNET,
        ConfigurationCommand.VLAN_1_1001_FDDI,
        ConfigurationCommand.VLAN_1_1001_MTU,
        ConfigurationCommand.VLAN_1_1001_NAME,
        ConfigurationCommand.VLAN_1_1001_SDE,
        ConfigurationCommand.VLAN_1_1001_STATE
    };
    public final static Command[] CONFIGURATION_VLAN_1_1001_NAME_ARGUMENTS = {
        ConfigurationCommand.VLAN_1_1001_NAME_WORD
    };
    public final static Command[] INTERFACE_VLAN_MEMBERSHIP_ARGUMENTS = {
        InterfaceCommand.VLAN_MEMBERSHIP_DYNAMIC,
        InterfaceCommand.VLAN_MEMBERSHIP_STATIC
    };
    public final static Command[] INTERFACE_VLAN_MEMBERSHIP_STATIC_ARGUMENTS = {
        InterfaceCommand.VLAN_MEMBERSHIP_STATIC_1_105
    };
    public final static Command[] INTERFACE_TRUNK_ARGUMENTS = {
    	InterfaceCommand.TRUNK_AUTO, InterfaceCommand.TRUNK_DESIRABLE,
    	InterfaceCommand.TRUNK_NONEGOTIATE, InterfaceCommand.TRUNK_OFF,
    	InterfaceCommand.TRUNK_ON
    };
    public final static Command[] PRIVILEGED_CONFIGURE_ARGUMENTS = {
        PrivilegedCommand.CONFIGURE_TERMINAL
    };
    public final static Command[] PRIVILEGED_SHOW_ARGUMENTS = {
        PrivilegedCommand.SHOW_IP, PrivilegedCommand.SHOW_RUNNING_CONFIG
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
        ConfigurationCommand.VLAN.setArguments(CONFIGURATION_VLAN_ARGUMENTS);
        ConfigurationCommand.VLAN_1_1001.setArguments(CONFIGURATION_VLAN_1_1001_ARGUMENTS);
        ConfigurationCommand.VLAN_1_1001_NAME.setArguments(CONFIGURATION_VLAN_1_1001_NAME_ARGUMENTS);
        InterfaceCommand.TRUNK.setArguments(INTERFACE_TRUNK_ARGUMENTS);
        InterfaceCommand.VLAN_MEMBERSHIP.setArguments(INTERFACE_VLAN_MEMBERSHIP_ARGUMENTS);
        InterfaceCommand.VLAN_MEMBERSHIP_STATIC.setArguments(INTERFACE_VLAN_MEMBERSHIP_ARGUMENTS);
        PrivilegedCommand.CONFIGURE.setArguments(PRIVILEGED_CONFIGURE_ARGUMENTS);
        PrivilegedCommand.SHOW.setArguments(PRIVILEGED_SHOW_ARGUMENTS);
    }
}
