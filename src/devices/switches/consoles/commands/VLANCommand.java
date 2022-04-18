package devices.switches.consoles.commands;

import devices.commands.Command;


public class VLANCommand {
    public final static Command VLAN = new Command("vlan", "Add, delete, or modify values associated with a single VLAN");
    public final static Command VLAN_1_1005 = new Command("<1-1005>", "ISL VLAN index");
    public final static Command VLAN_1_1005_NAME = new Command("name", "Ascii name of the VLAN");
    public final static Command EXIT = new Command("exit", "Apply changes, bump revision number, and exit mode");
    public final static Command VTP = new Command("vtp", "Perform VTP administrative functions");
    public final static Command HELP = new Command("help", "Description of the interactive help system");
    public final static Command ABORT = new Command("abort", "Exit mode without applying the changes");
    public final static Command APPLY = new Command("apply", "Apply current changes and bump revision number");
    public final static Command NAME = new Command("name", "");
}
