package platform.gui;

import devices.Device;

import devices.interfaces.Ethernet;
import devices.interfaces.Interface;
import devices.interfaces.Serial;

import devices.ui.DeviceUI;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class AddConnectionDialog extends CenterableDialog implements ActionListener, ListSelectionListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 4723072740751771038L;
	private Device selectedDevice;
    private Interface selectedInterface;
    private JButton applyButton;
    private JButton cancelButton;
    private final JList deviceList = new JList();
    private final JList interfaceList = new JList();
    private final Vector deviceVector = new Vector();
    private final Vector interfaceVector = new Vector();
    private Interface deviceInterface;

    public AddConnectionDialog(MainFrame mainFrame) {
        super(mainFrame, "Add Connection", true);

        JPanel panel = new JPanel(new BorderLayout());
        getContentPane().add(panel);
        panel.setSize(400, 400);

        JLabel instruction = new JLabel();
        instruction.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(instruction, BorderLayout.NORTH);
        Utilities.wrapLabelText(instruction, "Select a device and an interface to connect to.");

        deviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        deviceList.setFixedCellWidth(150);
        deviceList.setFixedCellHeight(15);
        deviceList.addListSelectionListener(this);

        interfaceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        interfaceList.setFixedCellWidth(150);
        interfaceList.setFixedCellHeight(15);
        interfaceList.addListSelectionListener(this);

        JScrollPane deviceScroller = new JScrollPane(deviceList);
        JScrollPane interfaceScroller = new JScrollPane(interfaceList);
        JPanel listPanel1 = new JPanel(new BorderLayout());
        listPanel1.add(deviceScroller, BorderLayout.WEST);

        JPanel listPanel2 = new JPanel();
        listPanel2.add(interfaceScroller, BorderLayout.EAST);

        JPanel listsPanel = new JPanel();
        listsPanel.add(listPanel1);
        listsPanel.add(listPanel2);

        panel.add(listsPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        applyButton = new JButton("Apply");
        buttonPanel.add(applyButton);
        cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);

        JPanel formattedButtonPanel = new JPanel(new BorderLayout());
        formattedButtonPanel.add(buttonPanel, BorderLayout.EAST);
        panel.add(formattedButtonPanel, BorderLayout.SOUTH);

        applyButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(380, 230);
    }

    public void setInterface(Interface deviceInterface) {
        this.deviceInterface = deviceInterface;
    }

    public void setDeviceList(Device[] devices) {
        selectedDevice = deviceInterface.getDevice();

        for (int i = 0; i < devices.length; i++) {
            Device device = devices[i];

            if (device != selectedDevice) {
                Interface[] interfaces = device.getOpenedInterfaces();
                int interfaceCount = 0;

                if (deviceInterface instanceof Ethernet) {
                    for (int j = 0; j < interfaces.length; j++) {
                        if (interfaces[j] instanceof Ethernet) {
                            interfaceCount++;
                        }
                    }
                } else if (deviceInterface instanceof Serial) {
                    for (int j = 0; j < interfaces.length; j++) {
                        if (interfaces[j] instanceof Serial) {
                            interfaceCount++;
                        }
                    }
                }

                if (interfaceCount > 0) {
                    deviceVector.add(device);
                }
            }
        }

        deviceList.setListData(deviceVector);
    }

    public void setInterfaceList(Device device) {
        interfaceVector.removeAllElements();

        Interface[] interfaces = device.getOpenedInterfaces();

        for (int i = 0; i < interfaces.length; i++) {
            if ((deviceInterface instanceof Ethernet) && (interfaces[i] instanceof Ethernet)) {
                interfaceVector.addElement(interfaces[i]);
            } else if ((deviceInterface instanceof Serial) && (interfaces[i] instanceof Serial)) {
                interfaceVector.addElement(interfaces[i]);
            }
        }

        interfaceList.setListData(interfaceVector);
    }

    public void valueChanged(ListSelectionEvent lse) {
        Object source = lse.getSource();

        if (source == deviceList) {
            int selectedIndex = deviceList.getSelectedIndex();

            if (selectedIndex >= 0) {
                setInterfaceList((Device) deviceVector.get(selectedIndex));
            }
        } else if (source == interfaceList) {
            int selectedIndex = interfaceList.getSelectedIndex();

            if (selectedIndex >= 0) {
                selectedInterface = (Interface) interfaceVector.get(selectedIndex);
            }
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == applyButton) {
            if (selectedInterface != null) {
                deviceInterface.setConnectedInterface(selectedInterface);
                selectedInterface.setConnectedInterface(deviceInterface);

                Device device = deviceInterface.getDevice();
                DeviceUI deviceUI = device.getDeviceUI();
                deviceUI.setInterfaces();

                device = selectedInterface.getDevice();
                deviceUI = device.getDeviceUI();
                deviceUI.setInterfaces();

                deviceVector.removeAllElements();
                interfaceVector.removeAllElements();
                selectedInterface = null;
                dispose();
            }
        } else if (source == cancelButton) {
            deviceVector.removeAllElements();
            interfaceVector.removeAllElements();
            selectedInterface = null;
            dispose();
        }
    }
}
