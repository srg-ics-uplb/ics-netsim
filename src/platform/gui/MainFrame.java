/*
 * Last revised: Jan 11, 2010 by Ludwig Tirazona <ljtirazona@gmail.com>
 */

package platform.gui;

import devices.Device;

import devices.hosts.Host;

import devices.hosts.consoles.HostConsole;

import devices.servers.Server;

import devices.servers.consoles.ServerConsole;

import devices.routers.Router;
import devices.routers.Router2600;

import devices.routers.consoles.Router2600Console;

import devices.switches.Switch;
import devices.switches.Switch1900;
import devices.switches.Switch2950;

import devices.switches.consoles.Switch1900Console;
import devices.switches.consoles.Switch2950Console;

import devices.ui.DeviceUI;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import javax.imageio.ImageIO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;


public class MainFrame extends JFrame implements ActionListener {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8550292220084056830L;

	//TutorialFrame tutorialFrame;
	PacketListFrame packetListFrame;
	
    public final static String ACCEPTED_FILENAME_EXTENSION = "vnt";

    /***************************************************************************
     * DIALOGS AND DESIGNER PANEL
     **************************************************************************/
    public static AddConnectionDialog ADD_CONNECTION_DIALOG;
    public static NameDialog NAME_DIALOG;
    public static DesignerPanel DESIGNER_PANEL;
    public static CreatorDialog CREATOR_DIALOG;
    public static HelpBrowser HELP_BROWSER;
    public static HostPortDialog HOST_PORT_DIALOG;
    public static ServerPortDialog SERVER_PORT_DIALOG;
    public static Router2600PortDialog ROUTER_2600_PORT_DIALOG;
    public static Switch1900PortDialog SWITCH_1900_PORT_DIALOG;
    public static Switch2950PortDialog SWITCH_2950_PORT_DIALOG;


    /***************************************************************************
     * HOST'S DIALOGS
     **************************************************************************/
    public static HostConfigurePortDialog HOST_CONFIGURE_PORT_DIALOG;
    public static HostConfigDialog HOST_CONFIG_DIALOG;
    public static HostPropertyDialog HOST_PROPERTY_DIALOG;
    public static HostPortScanDialog HOST_PORT_SCAN_DIALOG;
    public static HostPingDialog HOST_PING_DIALOG;
    public static HostHTTPRequestDialog HOST_HTTP_REQUEST_DIALOG;

    /***************************************************************************
     * SERVER'S DIALOGS
     **************************************************************************/
    public static ServerConfigurePortDialog SERVER_CONFIGURE_PORT_DIALOG;
    public static ServerConfigDialog SERVER_CONFIG_DIALOG;
    public static ServerPropertyDialog SERVER_PROPERTY_DIALOG;
    public static ServerPortScanDialog SERVER_PORT_SCAN_DIALOG;
    public static ServerPingDialog SERVER_PING_DIALOG;

    /**************************************************************************
     * SWITCHES' DIALOGS
     *************************************************************************/
    public static Switch1900PropertyDialog SWITCH1900_PROPERTY_DIALOG;
    public static Switch2950PropertyDialog SWITCH2950_PROPERTY_DIALOG;

    /**************************************************************************
     * ROUTER'S DIALOGS
     *************************************************************************/
    public static RouterPropertyDialog ROUTER_PROPERTY_DIALOG;

    /**************************************************************************
     * MISCELLANEOUS
     *************************************************************************/
    public static Point CENTER_POINT;
    private final JFileChooser fileChooser;
    private File currentFile = null;

    /***************************************************************************
     * DEVICES BUTTON ON TOOLBAR
     **************************************************************************/
    private final JButton router2600Button;
    private final JButton switch1900Button;
    private final JButton switch2950Button;
    private final JButton computerButton;
    private final JButton serverButton;
    private final JButton newButton;
    private final JButton openButton;
    private final JButton saveButton;
    private final JButton saveAsButton;
    private final JButton helpButton;
    private final JButton tutorialButton;
    private final JButton creatorButton;
    private final JButton tracerButton;

    public MainFrame() throws Exception {
        super();
        frameInit();
        setLayout(new BorderLayout());
        //tutorialFrame = new TutorialFrame(this);
        packetListFrame = new PacketListFrame(this);
        Container container = getContentPane();

        ADD_CONNECTION_DIALOG = new AddConnectionDialog(this);
        NAME_DIALOG = new NameDialog(this);
        HOST_PORT_DIALOG = new HostPortDialog(this);
        SERVER_PORT_DIALOG = new ServerPortDialog(this);
        ROUTER_2600_PORT_DIALOG = new Router2600PortDialog(this);
        SWITCH_1900_PORT_DIALOG = new Switch1900PortDialog(this);
        SWITCH_2950_PORT_DIALOG = new Switch2950PortDialog(this);
        
        
        

        HOST_CONFIGURE_PORT_DIALOG = new HostConfigurePortDialog(this);
        HOST_CONFIG_DIALOG = new HostConfigDialog(this);
        HOST_PROPERTY_DIALOG = new HostPropertyDialog(this);
        HOST_PING_DIALOG = new HostPingDialog(this);
        HOST_PORT_SCAN_DIALOG = new HostPortScanDialog(this);
        HOST_HTTP_REQUEST_DIALOG = new HostHTTPRequestDialog(this);
        
        SERVER_CONFIGURE_PORT_DIALOG = new ServerConfigurePortDialog(this);
        SERVER_CONFIG_DIALOG = new ServerConfigDialog(this);
        SERVER_PROPERTY_DIALOG = new ServerPropertyDialog(this);
        SERVER_PING_DIALOG = new ServerPingDialog(this);
        SERVER_PORT_SCAN_DIALOG = new ServerPortScanDialog(this);

        SWITCH1900_PROPERTY_DIALOG = new Switch1900PropertyDialog(this);
        SWITCH2950_PROPERTY_DIALOG = new Switch2950PropertyDialog(this);

        ROUTER_PROPERTY_DIALOG = new RouterPropertyDialog(this);

        /***********************************************************************
         * FILE CHOOSER - FOR OPENING AND SAVING .VNT FILES
         **********************************************************************/
        fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new TopologyFilter());
        fileChooser.setAcceptAllFileFilterUsed(false);
        fileChooser.setMultiSelectionEnabled(false);

        /***********************************************************************
         * DESIGNER PANEL
         **********************************************************************/
        DESIGNER_PANEL = new DesignerPanel(this);

        JScrollPane designerPane = new JScrollPane(DESIGNER_PANEL);
        designerPane.setPreferredSize(new Dimension(835, 550));

        /***********************************************************************
         * HELP, CREDITS
         **********************************************************************/
        HELP_BROWSER = new HelpBrowser(this);
        CREATOR_DIALOG = new CreatorDialog(this);
       

        /***********************************************************************
         * BUTTON DEVICES
         **********************************************************************/
        ClassLoader loader = getClass().getClassLoader();
        router2600Button = new JButton(new ImageIcon(loader.getResource("images/devices/router2600.gif")));
        router2600Button.setToolTipText("Router 2600");
        router2600Button.addActionListener(this);
        switch1900Button = new JButton(new ImageIcon(loader.getResource("images/devices/switch1900.gif")));
        switch1900Button.setToolTipText("Switch 1900");
        switch1900Button.addActionListener(this);
        switch2950Button = new JButton(new ImageIcon(loader.getResource("images/devices/switch2950.gif")));
        switch2950Button.setToolTipText("Switch 2950");
        switch2950Button.addActionListener(this);
        computerButton = new JButton(new ImageIcon(loader.getResource("images/devices/host.gif")));
        computerButton.setToolTipText("Host");
        computerButton.addActionListener(this);
        serverButton = new JButton(new ImageIcon(loader.getResource("images/devices/server.gif")));
        serverButton.setToolTipText("Server");
        serverButton.addActionListener(this);

        newButton = new JButton(new ImageIcon(loader.getResource("images/graphics/new.png")));
        newButton.addActionListener(this);
        openButton = new JButton(new ImageIcon(loader.getResource("images/graphics/open.png")));
        openButton.addActionListener(this);
        saveButton = new JButton(new ImageIcon(loader.getResource("images/graphics/save.png")));
        saveButton.addActionListener(this);
        saveAsButton = new JButton(new ImageIcon(loader.getResource("images/graphics/saveas.png")));
        saveAsButton.addActionListener(this);
        tutorialButton = new JButton(new ImageIcon(loader.getResource("images/graphics/tutorial.png")));
        tutorialButton.addActionListener(this);
        helpButton = new JButton(new ImageIcon(loader.getResource("images/graphics/help.png")));
        helpButton.addActionListener(this);
        creatorButton = new JButton(new ImageIcon(loader.getResource("images/graphics/creator.png")));
        creatorButton.addActionListener(this);
        tracerButton = new JButton(new ImageIcon(loader.getResource("images/graphics/tracer.png")));
        tracerButton.addActionListener(this);


        newButton.setOpaque(false);
        newButton.setToolTipText("Create New Network");
        openButton.setOpaque(false);
        openButton.setToolTipText("Open Saved Network");
        saveButton.setOpaque(false);
        saveButton.setToolTipText("Save Network");
        saveAsButton.setOpaque(false);
        saveAsButton.setToolTipText("Save Network As");
        tutorialButton.setOpaque(false);
        tutorialButton.setToolTipText("Tutorial");
        helpButton.setOpaque(false);
        helpButton.setToolTipText("Help");
        creatorButton.setOpaque(false);
        creatorButton.setToolTipText("Credits");        
        tracerButton.setOpaque(false);
        tracerButton.setToolTipText("Show/Hide Packet Watcher Window");
        /***********************************************************************
         * TOOLBAR - CONTAINS THE IMAGES OF DEVICES
         **********************************************************************/
        JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
        toolBar.setBorderPainted(false);
        toolBar.setMargin(new Insets(0, 0, 0, 0));
        toolBar.setFloatable(false);
        toolBar.add(computerButton);
        toolBar.add(serverButton);        
        toolBar.add(router2600Button);
        toolBar.add(switch1900Button);
        toolBar.add(switch2950Button);

        JToolBar topToolBar = new JToolBar();
        topToolBar.setBorderPainted(false);
        topToolBar.setMargin(new Insets(0, 0, 0, 0));
        topToolBar.setFloatable(false);
        topToolBar.add(newButton);
        topToolBar.add(openButton);
        topToolBar.add(saveButton);
        topToolBar.add(saveAsButton);
        topToolBar.addSeparator();
        topToolBar.add(tutorialButton);
        topToolBar.add(helpButton);
        topToolBar.add(creatorButton);
        topToolBar.add(tracerButton);
        topToolBar.setOpaque(false);

        JPanel banner = new JPanel();
        banner.setSize(900, 100);
        banner.setLayout(new AbsoluteLayout());
        banner.add(topToolBar, new AbsoluteConstraints(0, 60, 350, 40));

        try {
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/tempBanner.jpg"));
            banner.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        container.add(banner, BorderLayout.NORTH);
        container.add(toolBar, BorderLayout.WEST);
        container.add(designerPane, BorderLayout.CENTER);
        
        

        /***********************************************************************
         * SET FRAME PROPERTIES
         **********************************************************************/
        setTitle("ICS-NetSim: Scalable Cisco IOS Simulator for Virtual Networks");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        pack();
        setVisible(true);
        

        /***********************************************************************
         * SET THE LOCATION OF THE FRAME ON THE SCREEN
         **********************************************************************/
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = getSize().width;
        int h = getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        setLocation(x, y);

        CENTER_POINT = new Point(w / 2, h / 2);
    }

    public PacketListFrame getPacketListFrame(){
    	return packetListFrame;
    }
    
    /***************************************************************************
     * PERFORMS ACTIONS DEPENDING ON THE MENU ITEM OR DEVICE BUTTON CLICKED
     **************************************************************************/
    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == newButton) {
            if (currentFile != null) {
                if (Device.DEVICES.size() > 0) {
                    int response = JOptionPane.showConfirmDialog(this, "Save " + currentFile.getName() + "?", "Save?", JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        saveDevicesToFile(Device.getDevices(), currentFile);
                    }
                }

                currentFile = null;
                Device.DEVICES.removeAllElements();
                DESIGNER_PANEL.repaint();
            } else {
                if (Device.DEVICES.size() > 0) {
                    int confirm = JOptionPane.showConfirmDialog(this, "Save untitled?", "Save?", JOptionPane.YES_NO_OPTION);

                    if (confirm == JOptionPane.YES_OPTION) {
                        int returnValue = fileChooser.showSaveDialog(this);

                        if (returnValue == JFileChooser.APPROVE_OPTION) {
                            File file = fileChooser.getSelectedFile();

                            if (file.exists()) {
                                int response = JOptionPane.showConfirmDialog(this, "Overwrite " + file.getName() + "?", "Overwrite?", JOptionPane.YES_NO_OPTION);

                                if (response == JOptionPane.YES_OPTION) {
                                    saveDevicesToFile(Device.getDevices(), file);
                                }
                            } else {
                                saveDevicesToFile(Device.getDevices(), file);
                            }
                        }
                    }

                    Device.DEVICES.removeAllElements();
                    Device.resetDeviceCount();
                    DESIGNER_PANEL.repaint();
                }
            }
        } else if (source == openButton) {
            int returnValue = fileChooser.showOpenDialog(this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                if (file.exists()) {
                    loadDevices(file);
                    currentFile = file;
                }
            }
        } else if (source == saveButton) {
            if (currentFile != null) {
                saveDevicesToFile(Device.getDevices(), currentFile);
            } else {
                int returnValue = fileChooser.showSaveDialog(this);

                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    
                	File file = fileChooser.getSelectedFile();
                	/*REL*/
                	String ext=Utilities.getExtension(file);
                	if(ext==null) file= new File((file.toString()+ "." + ACCEPTED_FILENAME_EXTENSION));
                   	ext=Utilities.getExtension(file);
                   	/*-REL*/
                    if (file.exists()) {
                        int response = JOptionPane.showConfirmDialog(this, "Overwrite " + file.getName() + "?", "Overwrite?", JOptionPane.YES_NO_OPTION);

                        if (response == JOptionPane.YES_OPTION) {
                            saveDevicesToFile(Device.getDevices(), file);
                            currentFile = file;
                        }
                    } else {
                        saveDevicesToFile(Device.getDevices(), file);
                        currentFile = file;
                    }
                }
            }
        } else if (source == saveAsButton) {
            int returnValue = fileChooser.showSaveDialog(this);

            if (returnValue == JFileChooser.APPROVE_OPTION) {
                
            	File file = fileChooser.getSelectedFile();
            	/*REL*/
            	String ext=Utilities.getExtension(file);
            	if(ext==null) file= new File((file.toString()+ "." + ACCEPTED_FILENAME_EXTENSION));
               	ext=Utilities.getExtension(file);
               	/*-REL*/
                if (file.exists()) {
                    int response = JOptionPane.showConfirmDialog(this, "Overwrite " + file.getName() + "?", "Overwrite?", JOptionPane.YES_NO_OPTION);

                    if (response == JOptionPane.YES_OPTION) {
                        saveDevicesToFile(Device.getDevices(), file);
                        currentFile = file;
                    }
                } else {
                    saveDevicesToFile(Device.getDevices(), file);
                    currentFile = file;
                }
            }
        }else if (source == tutorialButton) {
        	//tutorialFrame.setVisible(true);
        	
        }else if (source == tracerButton) {
        	if (packetListFrame.isVisible() == false) packetListFrame.setVisible(true);
        	else packetListFrame.setVisible(false);
        }           
        else if (source == helpButton) {
            HELP_BROWSER.showCentered();
        } else if (source == creatorButton) {
            CREATOR_DIALOG.showCentered();
        } else if (source == router2600Button) {
            DESIGNER_PANEL.addDevice(Device.ROUTER2600);
        } else if (source == switch1900Button) {
            DESIGNER_PANEL.addDevice(Device.SWITCH1900);
        } else if (source == switch2950Button) {
            DESIGNER_PANEL.addDevice(Device.SWITCH2950);
        } else if (source == computerButton) {
            DESIGNER_PANEL.addDevice(Device.HOST);
        } else if (source == serverButton) {
            DESIGNER_PANEL.addDevice(Device.SERVER);
        }
    }

    /***************************************************************************
     * SAVE OBJECTS INTO FILE
     **************************************************************************/
    private void saveDevicesToFile(Device[] devices, File file) {
        try {
            FileOutputStream fileOut = new FileOutputStream(file);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(new Integer(devices.length));

            for (int i = 0; i < devices.length; i++) {
                objectOut.writeObject(devices[i]);
            }

            objectOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***************************************************************************
     * LOAD OBJECTS INTO THE DESIGNER
     **************************************************************************/
    public void loadDevices(File file) {
        try {
            FileInputStream fileIn = new FileInputStream(file);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            int objectsTotal = ((Integer) objectIn.readObject()).intValue();
            Device[] devices = new Device[objectsTotal];
            Image image = null;
            ClassLoader loader = getClass().getClassLoader();

            for (int i = 0; i < objectsTotal; i++) {
                devices[i] = (Device) objectIn.readObject();

                if (devices[i] instanceof Router) {
                    if (devices[i] instanceof Router2600) {
                        devices[i].setConsole(new Router2600Console((Router2600) devices[i], this));
                        image = (new ImageIcon(loader.getResource("images/devices/router2600.gif"))).getImage();
                    }
                } else if (devices[i] instanceof Switch) {
                    if (devices[i] instanceof Switch1900) {
                        devices[i].setConsole(new Switch1900Console((Switch1900) devices[i], this));
                        image = (new ImageIcon(loader.getResource("images/devices/switch1900.gif"))).getImage();
                    } else if (devices[i] instanceof Switch2950) {
                        devices[i].setConsole(new Switch2950Console((Switch2950) devices[i], this));
                        image = (new ImageIcon(loader.getResource("images/devices/switch2950.gif"))).getImage();
                    }
                } else if (devices[i] instanceof Host) {
                    devices[i].setConsole(new HostConsole((Host) devices[i], this));
                    image = (new ImageIcon(loader.getResource("images/devices/host.gif"))).getImage();
                } else if (devices[i] instanceof Server) {
                    devices[i].setConsole(new ServerConsole((Server) devices[i], this));
                    image = (new ImageIcon(loader.getResource("images/devices/server.gif"))).getImage();
                }

                devices[i].setImage(image);

                DeviceUI deviceUI = devices[i].getDeviceUI();
                deviceUI.setPopupMenu(new JPopupMenu());
                deviceUI.initializePopupMenu();
            }

            Device.load(devices);
            objectIn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***************************************************************************
     * TOPOLOGY FILTER - FILTERS THE DOCUMENTS WHEN OPENING .VNT FILES
     **************************************************************************/
    private class TopologyFilter extends FileFilter implements Serializable {
        

        /**
		 * 
		 */
		private static final long serialVersionUID = -8122970785622696078L;

		public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }

            String extension = Utilities.getExtension(f);

            if (extension != null) {
                if (extension.equals(ACCEPTED_FILENAME_EXTENSION)) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        public String getDescription() {
            return "All Visual Network Topologies";
        }
    }
}
