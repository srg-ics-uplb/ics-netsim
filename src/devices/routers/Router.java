package devices.routers;

import devices.Device;

import devices.addresses.IPAddress;
import devices.addresses.SubnetMask;

import devices.interfaces.Interface;

import devices.routers.accesslists.IPAccessList;

import devices.routers.routingprotocols.EIGRP;
import devices.routers.routingprotocols.IGRP;
import devices.routers.routingprotocols.RIP;
import devices.routers.routingprotocols.RoutingProtocol;

import devices.routers.routingtable.Entry;
import devices.routers.routingtable.RoutingTable;

import devices.switches.Switch;

import devices.switches.vlan.VLAN;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Vector;

import javax.swing.Timer;


public class Router extends Device implements ActionListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int routerCount;
    private final RIP rip = new RIP();
    private final IGRP igrp = new IGRP();
    private final EIGRP eigrp = new EIGRP();
    private String password = "";
    private String secret = "";
    private final RoutingTable routingTable = new RoutingTable();
    private final Vector IPAccessLists = new Vector();
    private RoutingProtocol currentRoutingProtocol;
    private final Vector networksAdvertisedByRIP = new Vector();
    private final Vector networksAdvertisedByIGRP = new Vector();
    private final Vector networksAdvertisedByEIGRP = new Vector();
    private Timer updateTimer = new Timer(30000, this);

    public Router(String name, Point location) {
        super(name, location);
        macAddress = "4000." + randomHexChar() + randomHexChar() + randomHexChar() + randomHexChar() + "." + randomHexChar() + randomHexChar() + randomHexChar() + randomHexChar() + "." + randomHexChar() + randomHexChar() + randomHexChar() + randomHexChar();
        routerCount++;
    }

    public void startUpdateTimer() {
        if (updateTimer.isRunning()) {
            updateTimer.setDelay(currentRoutingProtocol.getUpdateInterval());
            updateTimer.restart();
        } else {
            updateTimer.start();
        }
    }

    public void stopUpdateTimer() {
        if (updateTimer.isRunning()) {
            updateTimer.stop();
        }
    }

    public RIP getRIP() {
        return rip;
    }

    public IGRP getIGRP() {
        return igrp;
    }

    public EIGRP getEIGRP() {
        return eigrp;
    }

    public void actionPerformed(ActionEvent ae) {
        if (currentRoutingProtocol.equals(rip)) {
            String[] networks = getNetworksAdvertisedByRIP();

            for (int i = 0; i < networks.length; i++) {
                RoutingTable table = getRoutingTable();
                Entry[] entries = table.getEntries();

                for (int j = 0; j < entries.length; j++) {
                    if (IPAddress.getNetworkAddress(new IPAddress(networks[i]), new SubnetMask(IPAddress.getClassfulSubnet(networks[i]))).equals(IPAddress.getNetworkAddress(entries[j].getDestinationNetwork(), new SubnetMask(IPAddress.getClassfulSubnet(networks[i]))))) {
                        ripNetwork(entries[j].getDestinationNetwork().toString(), entries[j].getMask().toString());
                    }
                }
            }
        } else if (currentRoutingProtocol.equals(igrp)) {
            String[] networks = getNetworksAdvertisedByRIP();

            for (int i = 0; i < networks.length; i++) {
                RoutingTable table = getRoutingTable();
                Entry[] entries = table.getEntries();

                for (int j = 0; j < entries.length; j++) {
                    if (IPAddress.getNetworkAddress(new IPAddress(networks[i]), new SubnetMask(IPAddress.getClassfulSubnet(networks[i]))).equals(IPAddress.getNetworkAddress(entries[j].getDestinationNetwork(), new SubnetMask(IPAddress.getClassfulSubnet(networks[i]))))) {
                        igrpNetwork(entries[j].getDestinationNetwork().toString(), entries[j].getMask().toString());
                    }
                }
            }
        } else if (currentRoutingProtocol.equals(eigrp)) {
        }
    }

    /**************************************************************************
     * ROUTING PROTOCOLS
     **************************************************************************/
    public void ripNetwork(String network, String mask) {
        Router router = this;
        Interface[] interfaces = router.getInterfaces();

        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].getState().equals(Interface.UP)) {
                propagateRIPNetwork(interfaces[i], network, mask, 0);
            }
        }
    }

    public void propagateRIPNetwork(Interface deviceInterface, String network, String mask, int hopCount) {
        String deviceNetworkAddress = IPAddress.getNetworkAddress(deviceInterface.getIPAddress(), deviceInterface.getSubnetMask());
        Interface connectedInterface = deviceInterface.getConnectedInterface();

        if (connectedInterface != null) {
            Device connectedDevice = connectedInterface.getDevice();

            if (connectedDevice instanceof Switch) {
                Interface[] interfaces = connectedDevice.getClosedInterfaces();
                VLAN vlan = connectedInterface.getSwitchPort().getVLAN();

                for (int i = 0; i < interfaces.length; i++) {
                    if (interfaces[i].getSwitchPort().getVLAN() == vlan) {
                        Interface otherInterface = interfaces[i].getConnectedInterface();

                        if (otherInterface.getState().equals(Interface.UP)) {
                            if (IPAddress.getNetworkAddress(otherInterface.getIPAddress(), deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                                Device device = otherInterface.getDevice();

                                if (device instanceof Router) {
                                    Router router = (Router) device;

                                    if (router.getCurrentRoutingProtocol() instanceof RIP) {
                                        RoutingTable table = router.getRoutingTable();

                                        if (!table.entryExists(network, mask, deviceInterface.getIPAddress().toString()) && !table.entryDirectlyConnected(network, mask)) {
                                            Entry entry = new Entry(network, mask, deviceInterface.getIPAddress().toString(), hopCount);
                                            table.addEntry(entry);

                                            Interface[] routerInterfaces = device.getClosedInterfaces();

                                            for (int j = 0;
                                                    j < routerInterfaces.length;
                                                    j++) {
                                                if (!routerInterfaces[j].equals(otherInterface) && routerInterfaces[j].getState().equals(Interface.UP)) {
                                                    propagateRIPNetwork(routerInterfaces[j], network, mask, hopCount + 1);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (connectedDevice instanceof Router) {
                Router router = (Router) connectedDevice;

                if (connectedInterface.getState().equals(Interface.UP) && (router.getCurrentRoutingProtocol() instanceof RIP)) {
                    IPAddress connectedAddress = connectedInterface.getIPAddress();

                    if (IPAddress.getNetworkAddress(connectedAddress, deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                        RoutingTable table = ((Router) connectedDevice).getRoutingTable();

                        if (!table.entryExists(network, mask, deviceInterface.getIPAddress().toString()) && !table.entryDirectlyConnected(network, mask)) {
                            Entry entry = new Entry(network, mask, deviceInterface.getIPAddress().toString(), hopCount);
                            table.addEntry(entry);

                            Interface[] routerInterfaces = connectedDevice.getClosedInterfaces();

                            for (int j = 0; j < routerInterfaces.length; j++) {
                                if (!routerInterfaces[j].equals(connectedInterface) && routerInterfaces[j].getState().equals(Interface.UP)) {
                                    propagateRIPNetwork(routerInterfaces[j], network, mask, hopCount + 1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void igrpNetwork(String network, String mask) {
        Router router = this;
        Interface[] interfaces = router.getInterfaces();

        for (int i = 0; i < interfaces.length; i++) {
            if (interfaces[i].getState().equals(Interface.UP)) {
                propagateIGRPNetwork(interfaces[i], network, mask, 0, router.getIGRP().getASNumber());
            }
        }
    }

    public void propagateIGRPNetwork(Interface deviceInterface, String network, String mask, int hopCount, int ASNumber) {
        String deviceNetworkAddress = IPAddress.getNetworkAddress(deviceInterface.getIPAddress(), deviceInterface.getSubnetMask());
        Interface connectedInterface = deviceInterface.getConnectedInterface();

        if (connectedInterface != null) {
            Device connectedDevice = connectedInterface.getDevice();

            if (connectedDevice instanceof Switch) {
                Interface[] interfaces = connectedDevice.getClosedInterfaces();
                VLAN vlan = connectedInterface.getSwitchPort().getVLAN();

                for (int i = 0; i < interfaces.length; i++) {
                    if (interfaces[i].getSwitchPort().getVLAN() == vlan) {
                        Interface otherInterface = interfaces[i].getConnectedInterface();

                        if (otherInterface.getState().equals(Interface.UP)) {
                            if (IPAddress.getNetworkAddress(otherInterface.getIPAddress(), deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                                Device device = otherInterface.getDevice();

                                if (device instanceof Router) {
                                    Router router = (Router) device;

                                    if (router.getCurrentRoutingProtocol() instanceof IGRP) {
                                        IGRP igrp = (IGRP) router.getCurrentRoutingProtocol();

                                        if (igrp.getASNumber() == ASNumber) {
                                            RoutingTable table = router.getRoutingTable();

                                            if (!table.entryExists(network, mask, deviceInterface.getIPAddress().toString()) && !table.entryDirectlyConnected(network, mask)) {
                                                Entry entry = new Entry(network, mask, deviceInterface.getIPAddress().toString(), hopCount);
                                                table.addEntry(entry);

                                                Interface[] routerInterfaces = device.getClosedInterfaces();

                                                for (int j = 0;
                                                        j < routerInterfaces.length;
                                                        j++) {
                                                    if (!routerInterfaces[j].equals(otherInterface) && routerInterfaces[j].getState().equals(Interface.UP)) {
                                                        propagateIGRPNetwork(routerInterfaces[j], network, mask, hopCount + 1, ASNumber);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (connectedDevice instanceof Router) {
                Router router = (Router) connectedDevice;

                if (connectedInterface.getState().equals(Interface.UP) && (router.getCurrentRoutingProtocol() instanceof IGRP)) {
                    IGRP igrp = (IGRP) router.getCurrentRoutingProtocol();
                    System.out.println(router);
                    System.out.println(igrp.getASNumber());
                    System.out.println("asnumber:" + ASNumber);
                    if (igrp.getASNumber() == ASNumber) {
                        IPAddress connectedAddress = connectedInterface.getIPAddress();

                        if (IPAddress.getNetworkAddress(connectedAddress, deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                            RoutingTable table = ((Router) connectedDevice).getRoutingTable();

                            if (!table.entryExists(network, mask, deviceInterface.getIPAddress().toString()) && !table.entryDirectlyConnected(network, mask)) {
                                Entry entry = new Entry(network, mask, deviceInterface.getIPAddress().toString(), hopCount);
                                table.addEntry(entry);

                                Interface[] routerInterfaces = connectedDevice.getClosedInterfaces();

                                for (int j = 0; j < routerInterfaces.length;
                                        j++) {
                                    if (!routerInterfaces[j].equals(connectedInterface) && routerInterfaces[j].getState().equals(Interface.UP)) {
                                        propagateIGRPNetwork(routerInterfaces[j], network, mask, hopCount + 1, ASNumber);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void addRIPNetwork(String network) {
        networksAdvertisedByRIP.add(network);
    }

    public void addIGRPNetwork(String network) {
        networksAdvertisedByIGRP.add(network);
    }

    public void addEIGRPNetwork(String network) {
        networksAdvertisedByEIGRP.add(network);
    }

    public String[] getNetworksAdvertisedByRIP() {
        String[] networks = new String[networksAdvertisedByRIP.size()];

        for (int i = 0; i < networks.length; i++) {
            networks[i] = networksAdvertisedByRIP.get(i).toString();
        }

        return networks;
    }

    public String[] getNetworksAdvertisedByIGRP() {
        String[] networks = new String[networksAdvertisedByIGRP.size()];

        for (int i = 0; i < networks.length; i++) {
            networks[i] = networksAdvertisedByIGRP.get(i).toString();
        }

        return networks;
    }

    public String[] getNetworksAdvertisedByEIGRP() {
        String[] networks = new String[networksAdvertisedByEIGRP.size()];

        for (int i = 0; i < networks.length; i++) {
            networks[i] = networksAdvertisedByEIGRP.get(i).toString();
        }

        return networks;
    }

    public void setCurrentRoutingProtocol(RoutingProtocol protocol) {
        this.currentRoutingProtocol = protocol;
    }

    public RoutingProtocol getCurrentRoutingProtocol() {
        return currentRoutingProtocol;
    }

    public RoutingTable getRoutingTable() {
        return routingTable;
    }

    public void addIPAccessList(IPAccessList accessList) {
        IPAccessLists.add(accessList);
    }

    public IPAccessList[] getIPAccessLists() {
        IPAccessList[] accessLists = new IPAccessList[IPAccessLists.size()];

        for (int i = 0; i < accessLists.length; i++) {
            accessLists[i] = (IPAccessList) IPAccessLists.get(i);
        }

        return accessLists;
    }

    public IPAccessList getIPAccessList(int index) {
        IPAccessList accessList = null;

        for (int i = 0; i < IPAccessLists.size(); i++) {
            IPAccessList temp = (IPAccessList) IPAccessLists.get(i);

            if (temp.getIndex() == index) {
                accessList = temp;
            }
        }

        return accessList;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getPassword() {
        return password;
    }

    public String getSecret() {
        return secret;
    }

    public boolean isPasswordEnabled() {
        if (password.equals("")) {
            return false;
        }

        return true;
    }

    public boolean isSecretEnabled() {
        if (secret.equals("")) {
            return false;
        }

        return true;
    }
}
