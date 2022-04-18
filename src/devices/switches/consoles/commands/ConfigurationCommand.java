package devices.switches.consoles.commands;

import devices.commands.Command;


public class ConfigurationCommand {
    public final static Command BANNER = new Command("banner", "Define a login banner");
    public final static Command CDP = new Command("cdp", "Global CDP configuration subcommands");
    public final static Command ENABLE = new Command("enable", "Modify enable password parameters");
    public final static Command ENABLE_PASSWORD = new Command("password", "Assign privileged password");
    public final static Command ENABLE_PASSWORD_LEVEL = new Command("level", "Set exec level password");
    public final static Command ENABLE_PASSWORD_LEVEL_15 = new Command("15", "level number");
    public final static Command ENABLE_PASSWORD_LEVEL_15_WORD = new Command("word", "The UNENCRYPTED (clear text) 'enable' password");
    public final static Command ENABLE_SECRET = new Command("secret", "Assign privileged password");
    public final static Command ENABLE_SECRET_LEVEL = new Command("level", "Set exec level password");
    public final static Command ENABLE_SECRET_LEVEL_15 = new Command("15", "level number");
    public final static Command ENABLE_SECRET_LEVEL_15_WORD = new Command("word", "The UNENCRYPTED (clear text) 'enable' password");
    public final static Command END = new Command("end", "Exit from configure mode");
    public final static Command EXIT = new Command("exit", "Exit from configure mode");
    public final static Command HOSTNAME = new Command("hostname", "Set system's network name");
    public final static Command INTERFACE = new Command("interface", "Select an interface to configure");
    public final static Command INTERFACE_ETHERNET = new Command("ethernet", "IEEE 802.3");
    public final static Command IP = new Command("ip", "Global IP configuration subcommands");
    public final static Command IP_ADDRESS = new Command("address", "Set the IP address of the device");
    public final static Command IP_ADDRESS_ABCD = new Command("a.b.c.d", "IP Address");
    public final static Command IP_ADDRESS_ABCD_ABCD = new Command("a.b.c.d", "IP subnet mask");
    public final static Command IP_DEFAULT_GATEWAY = new Command("default-gateway", "Set the default gateway of the device");
    public final static Command IP_DEFAULT_GATEWAY_ABCD = new Command("a.b.c.d", "Default gateway");
    public final static Command IP_DOMAIN_NAME = new Command("domain-name", "Set the domain name of the device");
    public final static Command IP_NAME_SERVER = new Command("name-server", "Configure a name server for the device");
    public final static Command LINE = new Command("line", "Configure a terminal line");
    public final static Command MAC_ADDRESS_TABLE = new Command("mac-address-table", "Configure the mac address table");
    public final static Command RIP = new Command("rip", "Routing information protocol configuration");
    public final static Command SERVICE = new Command("service", "Configuration Command");
    public final static Command SPANTREE = new Command("spantree", "Spanning tree subsystem");
    public final static Command SWITCHING_MODE = new Command("switching-mode", "Sets the switching mode");
    public final static Command UPLINK_FAST = new Command("uplink-fast", "Enable Uplink fast");
    public final static Command VLAN = new Command("vlan", "VLAN configuration");
    public final static Command VLAN_1_1001 = new Command("<1-1001>", "ISL VLAN index");
    public final static Command VLAN_1_1001_ETHERNET = new Command("ethernet", "ethernet");
    public final static Command VLAN_1_1001_FDDI = new Command("fddi", "fddi");
    public final static Command VLAN_1_1001_MTU = new Command("mtu", "VLAN MTU");
    public final static Command VLAN_1_1001_NAME = new Command("name", "Set VLAN name");
    public final static Command VLAN_1_1001_NAME_WORD = new Command("word", "VLAN name");
    public final static Command VLAN_1_1001_SDE = new Command("sde", "IEE 801.10 Said");
    public final static Command VLAN_1_1001_STATE = new Command("state", "VLAN state");
    public final static Command VLAN_SERVER = new Command("server", "vlan membership server");
    public final static Command VLAN_MEMBERSHIP = new Command("vlan-membership", "VLAN membership server configuration");
    public final static Command VTP = new Command("vtp", "Global VTP configuration commands");
}
