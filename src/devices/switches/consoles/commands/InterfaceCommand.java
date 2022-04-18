package devices.switches.consoles.commands;

import devices.commands.Command;


public class InterfaceCommand {
    public final static Command CDP = new Command("cdp", "CDP interface subcommands");
    public final static Command DESCRIPTION = new Command("description", "Interface specific description");
    public final static Command DUPLEX = new Command("duplex", "Configure duplex operation");
    public final static Command EXIT = new Command("exit", "Exit from interface configuration mode");
    public final static Command HELP = new Command("help", "Description of the interactive help system");
    public final static Command IP = new Command("ip", "Interface Internet Protocol config commands");
    public final static Command SPEED = new Command("speed", "Configure speed operation");
    public final static Command PORT = new Command("port", "Perform switch port configuration");
    public final static Command SHUTDOWN = new Command("shutdown", "Shutdown the selected interface");    
    public final static Command SPANTREE = new Command("spantree", "Spanning tree subsystem");    
    public final static Command SWITCHPORT = new Command("switchport", "Set switching mode characteristics");
    public final static Command SWITCHPORT_PORTSECURITY = new Command("port-security", "Security Related Command");
    public final static Command SWITCHPORT_ACCESS = new Command("access", "Set access mode characteristics of the interface");
    public final static Command SWITCHPORT_MODE = new Command("mode", "Set trunking mode of the interface");
    public final static Command SWITCHPORT_MODE_ACCESS = new Command("access", "Set trunking mode to ACCESS unconditionally");
    public final static Command SWITCHPORT_MODE_DYNAMIC = new Command("dynamic", "Set trunking mode to dynamically negotiate access or trunk mode");
    public final static Command SWITCHPORT_MODE_TRUNK = new Command("trunk", "Set the trunking mode to TRUNK unconditionally");       
    public final static Command SWITCHPORT_TRUNK = new Command("trunk", "Set trunking characteristics of the interface");
    public final static Command SWITCHPORT_ACCESS_VLAN = new Command("vlan", "Set VLAN when interface is in access mode");    
    public final static Command TRUNK = new Command("trunk", "Set DISL trunk state");    
    public final static Command TRUNK_AUTO = new Command("auto", "Set DISL state to auto");
    public final static Command TRUNK_DESIRABLE = new Command("desirable", "Set DISL state to desirable");
    public final static Command TRUNK_NONEGOTIATE = new Command("nonegotiate", "Set DISL state to nonegotiate");
    public final static Command TRUNK_OFF = new Command("off", "Set DISL state to off");
    public final static Command TRUNK_ON = new Command("on", "Set DISL state to on");     
    public final static Command TRUNK_VLAN = new Command("trunk-vlan", "Trunk VLANs");      
    public final static Command VLAN_MEMBERSHIP = new Command("vlan-membership", "VLAN membership configuration");
    public final static Command VLAN_MEMBERSHIP_DYNAMIC = new Command("dynamic", "Set VLAN membership type as dynamic");
    public final static Command VLAN_MEMBERSHIP_STATIC = new Command("static", "Set VLAN membership type as static");
    public final static Command VLAN_MEMBERSHIP_STATIC_1_105 = new Command("<1-105>", "ISL VLAN index");  
    public final static Command VTP = new Command("vtp", "Global VTP configuration commands");
}
