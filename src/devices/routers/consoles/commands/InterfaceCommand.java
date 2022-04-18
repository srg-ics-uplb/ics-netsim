package devices.routers.consoles.commands;

import devices.commands.Command;


public class InterfaceCommand {
    public final static Command BACKUP = new Command("backup", "Modify backup parameters");
    public final static Command CDP = new Command("cdp", "CDP interface subcommands");
    public final static Command CRYPTO = new Command("crypto", "Encryption/Decryption commands");
    public final static Command CUSTOM_QUEUE_LIST = new Command("custom-queue-list", "Assign a custom queue list to an interface");
    public final static Command DESCRIPTION = new Command("description", "Interface specific description");
    public final static Command DUPLEX = new Command("duplex", "Configure duplex operation");
    public final static Command ENCAPSULATION = new Command("encapsulation", "Set encapsulation type for an interface");
    public final static Command EXIT = new Command("exit", "Exit from interface configuration mode");
    public final static Command HELP = new Command("help", "Description of the interactive help system");
    public final static Command INTERFACE = new Command("interface", "Select an interface to configure");
    public final static Command IP = new Command("ip", "Interface Internet Protocol config commands");
    public final static Command IP_ACCESS_GROUP = new Command("access-group", "Specify access control for packets");
    public final static Command IP_ACCESS_GROUP_1_199 = new Command("<1-199>", "IP access list (standard or extended)");
    public final static Command IP_ACCESS_GROUP_1_199_IN = new Command("in", "inbound packets");
    public final static Command IP_ACCESS_GROUP_1_199_OUT = new Command("out", "outbound packets");
    public final static Command IP_ADDRESS = new Command("address", "Set the IP address of the device");
    public final static Command IP_ADDRESS_ABCD = new Command("a.b.c.d", "IP Address");
    public final static Command IP_ADDRESS_ABCD_ABCD = new Command("a.b.c.d", "IP subnet mask");
    public final static Command IP_NAT = new Command("nat", "NAT interface commands");
    public final static Command IP_NETWORK = new Command("network", "Assign an IPX network & enable IPX routing");
    public final static Command IP_OSPF = new Command("ospf", "OSPF interface commands");
    public final static Command IP_POLICY = new Command("policy", "Enable policy routing");
    public final static Command IP_ROUTER = new Command("router", "IP router interface commands");
    public final static Command IP_SUMMARY_ADDRESS = new Command("summary-address", "Perform address summarization");
    public final static Command IPX = new Command("ipx", "Novell/IPX interface subcommands");
    public final static Command ISIS = new Command("isis", "IS-IS commands");
    public final static Command NO = new Command("no", "Negate a command or set its defaults");
    public final static Command NO_SHUTDOWN = new Command("shutdown", "Shutdown the selected interface");
    public final static Command PORT = new Command("port", "Perform switch port configuration");
    public final static Command SHUTDOWN = new Command("shutdown", "Shutdown the selected interface");
    public final static Command SPANTREE = new Command("spantree", "Spanning tree subsystem");
    public final static Command VLAN_MEMBERSHIP = new Command("vlan-membership", "VLAN membership configuration");
    public final static Command VLAN_MEMBERSHIP_DYNAMIC = new Command("dynamic", "Set VLAN membership type as dynamic");
    public final static Command VLAN_MEMBERSHIP_STATIC = new Command("static", "Set VLAN membership type as static");
    public final static Command VLAN_MEMBERSHIP_STATIC_1_105 = new Command("<1-105>", "ISL VLAN index");
}
