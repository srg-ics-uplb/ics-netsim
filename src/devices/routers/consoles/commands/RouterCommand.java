package devices.routers.consoles.commands;

import devices.commands.Command;


public class RouterCommand {
    public final static Command AUTO_SUMMARY = new Command("auto-summary", "Enable automatic network number summarization");
    public final static Command DEFAULT_INFORMATION = new Command("default-information", "Control distribution of default information");
    public final static Command DEFAULT_METRIC = new Command("default-metric", "Set metric of redistributed routes");
    public final static Command DISTANCE = new Command("distance", "Control distribution of default information");
    public final static Command DISTRIBUTE_LIST = new Command("distribute-list", "Filter networks in routing updates");
    public final static Command EXIT = new Command("exit", "Exit from routing protocol configuration mode");
    public final static Command NETWORK = new Command("network", "Enable routing on an IP network");
    public final static Command PASSIVE_INTERFACE = new Command("passive-interface", "Suppress routing updates on an interface");
    public final static Command REDISTRIBUTE = new Command("redistribute", "Redistribute information from another routing protocol");
    public final static Command VERSION = new Command("version", "Set routing protocol version");
}
