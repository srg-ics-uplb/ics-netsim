package devices.routers.consoles.commands;

import devices.commands.Command;


public class Router2600Command {
    public final static Command[] USER_MODE_COMMANDS = {
        UserCommand.ACCESS_ENABLE, UserCommand.ACCESS_PROFILE,
        UserCommand.CONNECT, UserCommand.ENABLE, UserCommand.EXIT,
        UserCommand.DISABLE, UserCommand.DISCONNECT, UserCommand.HELP,
        UserCommand.LOCK, UserCommand.LOGIN, UserCommand.LOGOUT,
        UserCommand.MRINFO, UserCommand.MSTAT, UserCommand.MTRACE,
        UserCommand.NAME_CONNECTION, UserCommand.PAD, UserCommand.PING,
        UserCommand.PPP, UserCommand.RESUME, UserCommand.RLOGIN,
        UserCommand.SHOW, UserCommand.SLIP, UserCommand.SYSTAT,
        UserCommand.TERMINAL, UserCommand.TRACERT, UserCommand.TUNNEL,
        UserCommand.UDPTN, UserCommand.X28, UserCommand.X3
    };
    public final static Command[] PRIVILEGED_MODE_COMMANDS = {
        PrivilegedCommand.ACCESS_TEMPLATE, PrivilegedCommand.ALPS,
        PrivilegedCommand.ARCHIVE, PrivilegedCommand.BFE, PrivilegedCommand.CD,
        PrivilegedCommand.CLEAR, PrivilegedCommand.CLOCK,
        PrivilegedCommand.CONFIGURE, PrivilegedCommand.COPY,
        PrivilegedCommand.DEBUG, PrivilegedCommand.DELETE, PrivilegedCommand.DIR,
        PrivilegedCommand.DISABLE, PrivilegedCommand.DISCONNECT,
        PrivilegedCommand.ELOG, PrivilegedCommand.ERASE, PrivilegedCommand.EXIT,
        PrivilegedCommand.LOGOUT, PrivilegedCommand.MORE, PrivilegedCommand.MRM,
        PrivilegedCommand.NCIA, PrivilegedCommand.PING, PrivilegedCommand.PWD,
        PrivilegedCommand.RELOAD, PrivilegedCommand.RESTART,
        PrivilegedCommand.RESUME, PrivilegedCommand.RSH, PrivilegedCommand.SDLC,
        PrivilegedCommand.SEND, PrivilegedCommand.SETUP, PrivilegedCommand.SHOW,
        PrivilegedCommand.START_CHAT, PrivilegedCommand.TELNET,
        PrivilegedCommand.UNDEBUG, PrivilegedCommand.VERIFY,
        PrivilegedCommand.WRITE
    };
    public final static Command[] CONFIGURATION_MODE_COMMANDS = {
        ConfigurationCommand.AAA, ConfigurationCommand.ACCESS_LIST,
        ConfigurationCommand.ARP, ConfigurationCommand.BANNER,
        ConfigurationCommand.BOOT, ConfigurationCommand.CDP,
        ConfigurationCommand.CLASS_MAP, ConfigurationCommand.CONFIG_REGISTER,
        ConfigurationCommand.CONTROLLER, ConfigurationCommand.CRYPTO,
        ConfigurationCommand.DIALER_LIST, ConfigurationCommand.ENABLE,
        ConfigurationCommand.END, ConfigurationCommand.EXIT,
        ConfigurationCommand.FRAME_RELAY, ConfigurationCommand.HELP,
        ConfigurationCommand.HOSTNAME, ConfigurationCommand.INTERFACE,
        ConfigurationCommand.IP, ConfigurationCommand.ISDN,
        ConfigurationCommand.LINE, ConfigurationCommand.LOGGING,
        ConfigurationCommand.MAP_CLASS, ConfigurationCommand.MAP_LIST,
        ConfigurationCommand.MULTILINK, ConfigurationCommand.NO,
        ConfigurationCommand.NTP, ConfigurationCommand.POLICY_MAP,
        ConfigurationCommand.PRIORITY_LIST, ConfigurationCommand.PRIVILEGE,
        ConfigurationCommand.RLOGIN, ConfigurationCommand.RMON,
        ConfigurationCommand.ROUTE_MAP, ConfigurationCommand.ROUTER,
        ConfigurationCommand.SERVICE, ConfigurationCommand.TACACS_SERVER,
        ConfigurationCommand.TFTP_SERVER, ConfigurationCommand.USERNAME
    };
    public final static Command[] INTERFACE_MODE_COMMANDS = {
        InterfaceCommand.BACKUP, InterfaceCommand.CDP, InterfaceCommand.CRYPTO,
        InterfaceCommand.CUSTOM_QUEUE_LIST, InterfaceCommand.DESCRIPTION,
        InterfaceCommand.ENCAPSULATION, InterfaceCommand.EXIT,
        InterfaceCommand.INTERFACE, InterfaceCommand.IP, InterfaceCommand.IPX,
        InterfaceCommand.ISIS, InterfaceCommand.NO, InterfaceCommand.SHUTDOWN
    };
    public final static Command[] ROUTER_MODE_COMMANDS = {
        RouterCommand.AUTO_SUMMARY, RouterCommand.DEFAULT_INFORMATION,
        RouterCommand.DEFAULT_METRIC, RouterCommand.DISTANCE,
        RouterCommand.DISTRIBUTE_LIST, RouterCommand.EXIT,
        RouterCommand.PASSIVE_INTERFACE, RouterCommand.NETWORK,
        RouterCommand.REDISTRIBUTE, RouterCommand.VERSION
    };

    /***************************************************************************
     * ARGUMENTS
     **************************************************************************/
    public final static Command[] CONFIGURATION_ACCESS_LIST_ARGUMENTS = {
        ConfigurationCommand.ACCESS_LIST_1_99,
        ConfigurationCommand.ACCESS_LIST_100_199
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_1_99_ARGUMENTS = {
        ConfigurationCommand.ACCESS_LIST_1_99_DENY,
        ConfigurationCommand.ACCESS_LIST_1_99_PERMIT
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_1_99_DENY_ARGUMENTS = {
        ConfigurationCommand.ACCESS_LIST_1_99_DENY_ANY,
        ConfigurationCommand.ACCESS_LIST_1_99_DENY_ABCD
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_1_99_DENY_ANY_ARGUMENTS = {
        ConfigurationCommand.ACCESS_LIST_1_99_DENY_ANY_ABCD
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_1_99_DENY_ABCD_ARGUMENTS = {
        ConfigurationCommand.ACCESS_LIST_1_99_DENY_ABCD_ABCD
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_1_99_PERMIT_ARGUMENTS = {
        ConfigurationCommand.ACCESS_LIST_1_99_PERMIT_ANY,
        ConfigurationCommand.ACCESS_LIST_1_99_PERMIT_ABCD
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_1_99_PERMIT_ANY_ARGUMENTS = {
        ConfigurationCommand.ACCESS_LIST_1_99_PERMIT_ANY_ABCD
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_1_99_PERMIT_ABCD_ARGUMENTS = {
        ConfigurationCommand.ACCESS_LIST_1_99_PERMIT_ABCD_ABCD
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_100_199_ARGUMENTS = {
        ConfigurationCommand.ACCESS_LIST_100_199_DENY,
        ConfigurationCommand.ACCESS_LIST_100_199_PERMIT
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_100_199_DENY_ARGUMENTS = {
        ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP,
        ConfigurationCommand.ACCESS_LIST_100_199_DENY_ICMP,
        ConfigurationCommand.ACCESS_LIST_100_199_DENY_IGRP,
        ConfigurationCommand.ACCESS_LIST_100_199_DENY_IP,
        ConfigurationCommand.ACCESS_LIST_100_199_DENY_OSPF,
        ConfigurationCommand.ACCESS_LIST_100_199_DENY_TCP,
        ConfigurationCommand.ACCESS_LIST_100_199_DENY_UDP
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_100_199_DENY_EIGRP_ARGUMENTS = {
    	ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP_ABCD,
    	ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP_ANY
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_100_199_DENY_EIGRP_ABCD_ARGUMENTS = {	
    	ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP_ABCD_ABCD,
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_100_199_DENY_EIGRP_ANY_ARGUMENTS = {
    	ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP_ANY_ABCD,
    	ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP_ANY_ANY
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_100_199_DENY_EIGRP_ANY_ANY_ARGUMENTS = {
    	ConfigurationCommand.ACCESS_LIST_100_199_DENY_EIGRP_ANY_ANY_LOG
    };
    public final static Command[] CONFIGURATION_ACCESS_LIST_100_199_PERMIT_ARGUMENTS = {
        ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_EIGRP,
        ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_ICMP,
        ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_IGRP,
        ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_IP,
        ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_OSPF,
        ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_TCP,
        ConfigurationCommand.ACCESS_LIST_100_199_PERMIT_UDP
    };
    public final static Command[] CONFIGURATION_ENABLE_ARGUMENTS = {
        ConfigurationCommand.ENABLE_PASSWORD, ConfigurationCommand.ENABLE_SECRET
    };
    public final static Command[] CONFIGURATION_NO_ARGUMENTS = CONFIGURATION_MODE_COMMANDS;
    public final static Command[] CONFIGURATION_IP_ARGUMENTS = {
        ConfigurationCommand.IP_ACCESS_LIST, ConfigurationCommand.IP_CLASSLESS,
        ConfigurationCommand.IP_DHCP, ConfigurationCommand.IP_DOMAIN_LOOKUP,
        ConfigurationCommand.IP_HOST, ConfigurationCommand.IP_HTTP,
        ConfigurationCommand.IP_NAT, ConfigurationCommand.IP_ROUTE
    };
    public final static Command[] CONFIGURATION_IP_ROUTE_ARGUMENTS = {
        ConfigurationCommand.IP_ROUTE_ABCD
    };
    public final static Command[] CONFIGURATION_IP_ROUTE_ABCD_ARGUMENTS = {
        ConfigurationCommand.IP_ROUTE_ABCD_ABCD
    };
    public final static Command[] CONFIGURATION_IP_ROUTE_ABCD_ABCD_ARGUMENTS = {
        ConfigurationCommand.IP_ROUTE_ABCD_ABCD_ABCD,
        ConfigurationCommand.IP_ROUTE_ABCD_ABCD_DIALER,
        ConfigurationCommand.IP_ROUTE_ABCD_ABCD_SERIAL
    };
    public final static Command[] CONFIGURATION_ROUTER_ARGUMENTS = {
        ConfigurationCommand.ROUTER_BGP, ConfigurationCommand.ROUTER_EIGRP,
        ConfigurationCommand.ROUTER_IGRP, ConfigurationCommand.ROUTER_ISIS,
        ConfigurationCommand.ROUTER_OSPF, ConfigurationCommand.ROUTER_RIP
    };
    public final static Command[] INTERFACE_IP_ARGUMENTS = {
        InterfaceCommand.IP_ACCESS_GROUP, InterfaceCommand.IP_ADDRESS,
        InterfaceCommand.IP_NAT, InterfaceCommand.IP_NETWORK,
        InterfaceCommand.IP_OSPF, InterfaceCommand.IP_POLICY,
        InterfaceCommand.IP_ROUTER, InterfaceCommand.IP_SUMMARY_ADDRESS
    };
    public final static Command[] INTERFACE_IP_ACCESS_GROUP_ARGUMENTS = {
        InterfaceCommand.IP_ACCESS_GROUP_1_199
    };
    public final static Command[] INTERFACE_IP_ACCESS_GROUP_1_199_ARGUMENTS = {
        InterfaceCommand.IP_ACCESS_GROUP_1_199_IN,
        InterfaceCommand.IP_ACCESS_GROUP_1_199_OUT
    };
    public final static Command[] INTERFACE_IP_ADDRESS_ARGUMENTS = {
        InterfaceCommand.IP_ADDRESS_ABCD
    };
    public final static Command[] INTERFACE_IP_ADDRESS_ABCD_ARGUMENTS = {
        InterfaceCommand.IP_ADDRESS_ABCD_ABCD
    };
    public final static Command[] INTERFACE_NO_ARGUMENTS = {
        InterfaceCommand.NO_SHUTDOWN
    };
    public final static Command[] PRIVILEGED_CONFIGURE_ARGUMENTS = {
        PrivilegedCommand.CONFIGURE_MEMORY, PrivilegedCommand.CONFIGURE_NETWORK,
        PrivilegedCommand.CONFIGURE_TERMINAL
    };
    public final static Command[] PRIVILEGED_SHOW_ARGUMENTS = {
        PrivilegedCommand.SHOW_ACCESS_LISTS, PrivilegedCommand.SHOW_ARP,
        PrivilegedCommand.SHOW_CDP, PrivilegedCommand.SHOW_CLASS_MAP,
        PrivilegedCommand.SHOW_CLNS, PrivilegedCommand.SHOW_CLOCK,
        PrivilegedCommand.SHOW_COMPRESS, PrivilegedCommand.SHOW_CONFIGURATION,
        PrivilegedCommand.SHOW_CONTROLLERS, PrivilegedCommand.SHOW_CRYPTO,
        PrivilegedCommand.SHOW_DIALER, PrivilegedCommand.SHOW_FLASH,
        PrivilegedCommand.SHOW_FRAME_RELAY, PrivilegedCommand.SHOW_HISTORY,
        PrivilegedCommand.SHOW_HOSTS, PrivilegedCommand.SHOW_INTERFACES,
        PrivilegedCommand.SHOW_IP, PrivilegedCommand.SHOW_ISDN,
        PrivilegedCommand.SHOW_ISIS, PrivilegedCommand.SHOW_NTP,
        PrivilegedCommand.SHOW_POLICY_MAP, PrivilegedCommand.SHOW_PROTOCOLS,
        PrivilegedCommand.SHOW_QUEUEING, PrivilegedCommand.SHOW_RUNNING_CONFIG,
        PrivilegedCommand.SHOW_SESSIONS, PrivilegedCommand.SHOW_STARTUP_CONFIG,
        PrivilegedCommand.SHOW_TERMINAL, PrivilegedCommand.SHOW_USERS,
        PrivilegedCommand.SHOW_VERSION
    };
    public final static Command[] PRIVILEGED_SHOW_IP_ARGUMENTS = {
        PrivilegedCommand.SHOW_IP_ARP, PrivilegedCommand.SHOW_IP_BGP,
        PrivilegedCommand.SHOW_IP_DHCP, PrivilegedCommand.SHOW_IP_EIGRP,
        PrivilegedCommand.SHOW_IP_INTERFACE, PrivilegedCommand.SHOW_IP_NAT,
        PrivilegedCommand.SHOW_IP_OSPF, PrivilegedCommand.SHOW_IP_PROTOCOLS,
        PrivilegedCommand.SHOW_IP_ROUTE
    };
    public final static Command[] PRIVILEGED_SHOW_IP_INTERFACE_ARGUMENTS = {
        PrivilegedCommand.SHOW_IP_INTERFACE_BRIEF,
        PrivilegedCommand.SHOW_IP_INTERFACE_ETHERNET,
        PrivilegedCommand.SHOW_IP_INTERFACE_FASTETHERNET,
        PrivilegedCommand.SHOW_IP_INTERFACE_LOOPBACK,
        PrivilegedCommand.SHOW_IP_INTERFACE_SERIAL
    };

    static {
        ConfigurationCommand.ACCESS_LIST.setArguments(CONFIGURATION_ACCESS_LIST_ARGUMENTS);
        ConfigurationCommand.ACCESS_LIST_1_99.setArguments(CONFIGURATION_ACCESS_LIST_1_99_ARGUMENTS);
        ConfigurationCommand.ACCESS_LIST_1_99_DENY.setArguments(CONFIGURATION_ACCESS_LIST_1_99_DENY_ARGUMENTS);
        ConfigurationCommand.ACCESS_LIST_1_99_DENY_ANY.setArguments(CONFIGURATION_ACCESS_LIST_1_99_DENY_ANY_ARGUMENTS);
        ConfigurationCommand.ACCESS_LIST_1_99_DENY_ABCD.setArguments(CONFIGURATION_ACCESS_LIST_1_99_DENY_ABCD_ARGUMENTS);
        ConfigurationCommand.ACCESS_LIST_1_99_PERMIT.setArguments(CONFIGURATION_ACCESS_LIST_1_99_PERMIT_ARGUMENTS);
        ConfigurationCommand.ACCESS_LIST_1_99_PERMIT_ANY.setArguments(CONFIGURATION_ACCESS_LIST_1_99_PERMIT_ANY_ARGUMENTS);
        ConfigurationCommand.ACCESS_LIST_1_99_PERMIT_ABCD.setArguments(CONFIGURATION_ACCESS_LIST_1_99_PERMIT_ABCD_ARGUMENTS);
        ConfigurationCommand.ACCESS_LIST_100_199.setArguments(CONFIGURATION_ACCESS_LIST_100_199_ARGUMENTS);
        ConfigurationCommand.ACCESS_LIST_100_199_DENY.setArguments(CONFIGURATION_ACCESS_LIST_100_199_DENY_ARGUMENTS);
        ConfigurationCommand.ACCESS_LIST_100_199_PERMIT.setArguments(CONFIGURATION_ACCESS_LIST_100_199_PERMIT_ARGUMENTS);
        ConfigurationCommand.ENABLE.setArguments(CONFIGURATION_ENABLE_ARGUMENTS);
        ConfigurationCommand.IP.setArguments(CONFIGURATION_IP_ARGUMENTS);
        ConfigurationCommand.IP_ROUTE.setArguments(CONFIGURATION_IP_ROUTE_ARGUMENTS);
        ConfigurationCommand.IP_ROUTE_ABCD.setArguments(CONFIGURATION_IP_ROUTE_ABCD_ARGUMENTS);
        ConfigurationCommand.IP_ROUTE_ABCD_ABCD.setArguments(CONFIGURATION_IP_ROUTE_ABCD_ABCD_ARGUMENTS);
        ConfigurationCommand.NO.setArguments(CONFIGURATION_NO_ARGUMENTS);
        ConfigurationCommand.ROUTER.setArguments(CONFIGURATION_ROUTER_ARGUMENTS);
        InterfaceCommand.IP.setArguments(INTERFACE_IP_ARGUMENTS);
        InterfaceCommand.IP_ACCESS_GROUP.setArguments(INTERFACE_IP_ACCESS_GROUP_ARGUMENTS);
        InterfaceCommand.IP_ACCESS_GROUP_1_199.setArguments(INTERFACE_IP_ACCESS_GROUP_1_199_ARGUMENTS);
        InterfaceCommand.IP_ADDRESS.setArguments(INTERFACE_IP_ADDRESS_ARGUMENTS);
        InterfaceCommand.IP_ADDRESS_ABCD.setArguments(INTERFACE_IP_ADDRESS_ABCD_ARGUMENTS);
        InterfaceCommand.NO.setArguments(INTERFACE_NO_ARGUMENTS);
        PrivilegedCommand.CONFIGURE.setArguments(PRIVILEGED_CONFIGURE_ARGUMENTS);
        PrivilegedCommand.SHOW.setArguments(PRIVILEGED_SHOW_ARGUMENTS);
        PrivilegedCommand.SHOW_IP.setArguments(PRIVILEGED_SHOW_IP_ARGUMENTS);
        PrivilegedCommand.SHOW_IP_INTERFACE.setArguments(PRIVILEGED_SHOW_IP_INTERFACE_ARGUMENTS);
    }
}
