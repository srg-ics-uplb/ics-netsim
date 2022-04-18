package platform.gui;

import devices.interfaces.Interface;

import devices.routers.Router2600;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


public class RouterPropertyDialog extends CenterableDialog {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8776458550504191702L;
	private Vector rows;
    private Vector columns;
    private final String[] columnNames = {
        "Router Interface", "IP Address", "Subnet Mask", "Connected Device",
        "Connected Device Interface"
    };
    private JTable table;
    private final JScrollPane scrollPane;
    private DefaultTableModel tabModel;
    private final String nci = "No Connected Interface";
    private final String ncd = "No Connected Device";
    private final JPanel panel;
    private Interface[] interFaces;
    protected String deviceName = "";

    public RouterPropertyDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Router Properties", true);

        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new AbsoluteLayout());
        panel.setSize(800, 83);

        rows = new Vector();
        columns = new Vector();

        for (int i = 0; i < columnNames.length; i++) {
            columns.addElement((String) columnNames[i]);
        }

        tabModel = new DefaultTableModel();
        tabModel.setDataVector(rows, columns);

        table = new JTable(tabModel);
        scrollPane = new JScrollPane(table); // ScrollPane

        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setEnabled(false);

        panel.add(scrollPane, new AbsoluteConstraints(0, 0, 800, 83));

        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    dispose();
                }
            });

        pack();
    }

    public void setRouter(Router2600 router) {
        interFaces = router.getInterfaces();
        rows.removeAllElements();

        for (int i = 0; i < interFaces.length; i++) {
            Vector data = new Vector();
            data.add(interFaces[i].getName());
            data.add(interFaces[i].getIPAddress().toString());
            data.add(interFaces[i].getSubnetMask().toString());

            if (interFaces[i].isOpened() == false) {
                data.add(interFaces[i].getConnectedInterface().getDevice().getName());
                data.add(interFaces[i].getConnectedInterface().getName());
            } else {
                data.add(ncd);
                data.add(nci);
            }

            addRow(data);
        }
    }

    public void addRow(Vector data) {
        rows.addElement(data);
    }
}
