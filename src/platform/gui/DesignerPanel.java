package platform.gui;

import devices.Device;

import devices.hosts.Host;
import devices.hosts.consoles.HostConsole;
import devices.hosts.ui.HostUI;

import devices.servers.Server;
import devices.servers.consoles.ServerConsole;
import devices.servers.ui.ServerUI;


import devices.interfaces.Interface;

import devices.routers.Router;
import devices.routers.Router2600;
import devices.routers.consoles.Router2600Console;
import devices.routers.ui.RouterUI;

import devices.switches.Switch;
import devices.switches.Switch1900;
import devices.switches.Switch2950;
import devices.switches.consoles.Switch1900Console;
import devices.switches.consoles.Switch2950Console;
import devices.switches.ui.SwitchUI;

import devices.ui.DeviceUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;


public class DesignerPanel extends JPanel implements FocusListener {
    
	private static final long serialVersionUID = -8863195200701376334L;
	private JPopupMenu popupMenu;
    private MainFrame mainFrame;
    private Device deviceDrag;

    public DesignerPanel(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;

        addMouseListener(new MyMouseListener(this));
        addMouseMotionListener(new MyMouseMotionListener(this));
        addFocusListener(this);

        setComponentPopupMenu(popupMenu);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setOpaque(true);
        setPreferredSize(new Dimension(10000, 10000));
        setVisible(true);
    }

    public void addDevice(String deviceType) {
        MainFrame.NAME_DIALOG.showCentered();

        if (!MainFrame.NAME_DIALOG.emptyName) {
            String deviceName = "";

            if (MainFrame.NAME_DIALOG.defaultSelected) {
                if (deviceType.equals(Device.ROUTER2600)) {
                    deviceName = "Router" + (Router.routerCount + 1);
                } else if (deviceType.equals(Device.SWITCH1900) || deviceType.equals(Device.SWITCH2950) || deviceType.equals(Device.SWITCH3550)) {
                    deviceName = "Switch" + (Switch.switchCount + 1);
                } else if (deviceType.equals(Device.HOST)) {
                    deviceName = "Host" + (Host.hostCount + 1);
                }else if (deviceType.equals(Device.SERVER)) {
                    deviceName = "Server" + (Server.serverCount + 1);
                }
            } else {
                deviceName = MainFrame.NAME_DIALOG.deviceName;
            }

            Point dropPoint = SwingUtilities.convertPoint(mainFrame, MainFrame.CENTER_POINT, this);

            if (deviceType.equals(Device.ROUTER2600)) {
                Router2600 router2600 = new Router2600(deviceName, dropPoint);
                router2600.setConsole(new Router2600Console(router2600, mainFrame));
                router2600.setDeviceUI(new RouterUI(router2600));
            } else if (deviceType.equals(Device.SWITCH1900)) {
                Switch1900 switch1900 = new Switch1900(deviceName, dropPoint);
                switch1900.setConsole(new Switch1900Console(switch1900, mainFrame));
                switch1900.setDeviceUI(new SwitchUI(switch1900));
            } else if (deviceType.equals(Device.SWITCH2950)) {
                Switch2950 switch2950 = new Switch2950(deviceName, dropPoint);
                switch2950.setConsole(new Switch2950Console(switch2950, mainFrame));
                switch2950.setDeviceUI(new SwitchUI(switch2950));
            } else if (deviceType.equals(Device.HOST)) {
                Host host = new Host(deviceName, dropPoint);
                host.setConsole(new HostConsole(host, mainFrame));
                host.setDeviceUI(new HostUI(host));
            }else if (deviceType.equals(Device.SERVER)) {
                Server server = new Server(deviceName, dropPoint);
                server.setConsole(new ServerConsole(server, mainFrame));
                server.setDeviceUI(new ServerUI(server));
            }

            repaint();
        }
    }

    public void showAddConnection(Interface interfaceObj) {
        MainFrame.ADD_CONNECTION_DIALOG.setInterface(interfaceObj);
        MainFrame.ADD_CONNECTION_DIALOG.setDeviceList(Device.getDevices());
        MainFrame.ADD_CONNECTION_DIALOG.showCentered();
        repaint();
    }

    public void removeConnection(Interface interfaceObj) {
        Interface connectedInterface = interfaceObj.getConnectedInterface();
        connectedInterface.setConnectedInterface(null);
        interfaceObj.setConnectedInterface(null);

        Device device = connectedInterface.getDevice();
        DeviceUI deviceUI = device.getDeviceUI();
        deviceUI.setInterfaces();
        device = interfaceObj.getDevice();
        deviceUI = device.getDeviceUI();
        deviceUI.setInterfaces();
        repaint();
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.WHITE);
        g2D.fillRect(0, 0, getWidth(), getHeight());

        Image image = null;
        Device[] devices = Device.getDevices();

        for (int i = 0; i < devices.length; i++) {
            Device device = devices[i];
            Point location = device.getLocation();
            image = device.getImage();

            g2D.setColor(Color.BLUE);

            Interface[] closedInterfaces = device.getClosedInterfaces();

            for (int j = 0; j < closedInterfaces.length; j++) {
                Interface connectedInterface = closedInterfaces[j].getConnectedInterface();
                Device connectedDevice = connectedInterface.getDevice();
                Image connectedImage = connectedDevice.getImage();
                Point destination = connectedDevice.getLocation();
                g2D.drawLine(location.x + (image.getWidth(this) / 2), location.y + (image.getHeight(this) / 2), destination.x + (connectedImage.getWidth(this) / 2), destination.y + (connectedImage.getHeight(this) / 2));
            }
        }

        for (int i = 0; i < devices.length; i++) {
            Device device = devices[i];
            Point location = device.getLocation();
            image = device.getImage();

            g2D.drawImage(image, location.x, location.y, this);
            g2D.setColor(Color.BLACK);

            FontMetrics fontMetrics = g2D.getFontMetrics();
            g2D.drawString(device.getName(), (location.x + (image.getWidth(this) / 2)) - (fontMetrics.stringWidth(device.getName()) / 2), location.y + image.getHeight(this) + fontMetrics.getHeight());
        }
    }

    public void focusGained(FocusEvent fe) {
        repaint();
    }

    public void focusLost(FocusEvent fe) {
    }

    private class MyMouseListener extends MouseAdapter {
        private JComponent component;

        public MyMouseListener(JComponent component) {
            this.component = component;
        }

        public void mouseClicked(MouseEvent me) {
            if (me.getButton() == MouseEvent.BUTTON3) {
                Point mousePoint = me.getPoint();
                Device[] devices = Device.getDevices();

                for (int i = 0; i < devices.length; i++) {
                    Device device = devices[i];

                    if (device.contains(mousePoint, device, component)) {
                        DeviceUI deviceUI = device.getDeviceUI();
                        popupMenu = deviceUI.getPopupMenu();
                        popupMenu.show(component, me.getX(), me.getY());
                    }
                }
            }
        }

        public void mousePressed(MouseEvent me) {
            Point mousePoint = me.getPoint();
            Device[] devices = Device.getDevices();

            for (int i = 0; i < devices.length; i++) {
                Device device = devices[i];

                if (device.contains(mousePoint, device, component) && (me.getButton() == MouseEvent.BUTTON1)) {
                    deviceDrag = device;
                }
            }
        }

        public void mouseReleased(MouseEvent me) {
            if (me.getButton() == MouseEvent.BUTTON1) {
                deviceDrag = null;
            }
        }
    }

    private class MyMouseMotionListener extends MouseMotionAdapter {
        private JComponent component;

        public MyMouseMotionListener(JComponent component) {
            this.component = component;
        }

        public void mouseDragged(MouseEvent me) {
            if (deviceDrag != null) {
                if (component.contains(me.getPoint())) {
                    deviceDrag.setLocation(me.getPoint());
                    component.repaint();
                }
            }
        }
    }
}
