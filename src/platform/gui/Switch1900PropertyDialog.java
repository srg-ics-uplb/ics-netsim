package platform.gui;

import devices.interfaces.Interface;

import devices.switches.Switch1900;

import devices.switches.ports.SwitchPort;

import devices.switches.vlan.VLAN;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class Switch1900PropertyDialog extends CenterableDialog {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 9192022357512386052L;
	private Vector rows;
    private Vector columns;
    private SwitchPort[] switchPort;
    private final String[] columnNames = {
        "Switch Port #", "Switch Interface Name", "Connected Device",
        "Connected Interface", "VLAN Name", "VLAN #"
    };
    private JTable table;
    private final JScrollPane scrollPane;
    private DefaultTableModel tabModel;
    private final String nci = "No Connected Interface";
    private final String ncd = "No Connected Device";
    private final String defaultVLAN = "Default VLAN";
    private final JPanel panel;
    protected String deviceName = "";

    public Switch1900PropertyDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Switch1900 Properties", true);

        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new AbsoluteLayout());
        panel.setSize(800, 243);

        rows = new Vector();
        columns = new Vector();

        for (int i = 0; i < columnNames.length; i++) {
            columns.addElement((String) columnNames[i]);
        }

        tabModel = new DefaultTableModel();
        tabModel.setDataVector(rows, columns);

        table = new JTable(tabModel);
        scrollPane = new JScrollPane(table);

        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setEnabled(false);

        panel.add(scrollPane, new AbsoluteConstraints(0, 0, 800, 243));

        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    dispose();
                }
            });

        pack();
    }

    public void setSwitch(Switch1900 switch1900) {
        switchPort = switch1900.getPorts();
        rows.removeAllElements();

        for (int i = 0; i < switchPort.length; i++) {
            Vector data = new Vector();
            data.add(switchPort[i].getPortNumber());

            Interface interFace = switchPort[i].getPortInterface();
            data.add(interFace.getName());

            if (interFace.isOpened() == false) {
                data.add(interFace.getConnectedInterface().getDevice().getName());
                data.add(interFace.getConnectedInterface().getConnectedInterface().getName());
            } else {
                data.add(ncd);
                data.add(nci);
            }

            VLAN vlan = switchPort[i].getVLAN();

            if (vlan.getIndex() == 1) {
                data.add(defaultVLAN);
            } else {
                data.add(vlan.getName());
            }

            data.add(new Integer(vlan.getIndex()));
            addRow(data);
        }
    }

    public void addRow(Vector data) {
        rows.addElement(data);
    }
}
