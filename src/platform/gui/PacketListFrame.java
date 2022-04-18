package platform.gui;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import platform.gui.PacketListUnit;
import devices.packets.*;
import devices.packets.tcp.*;
import devices.packets.udp.*;


public class PacketListFrame extends JFrame implements ActionListener, MouseListener{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -164901047236413817L;
    ClassLoader loader = getClass().getClassLoader();

    JPanel mainPanel;
    DefaultTableModel model;
    JTable listTable;
    MainFrame mainWindow;
    JButton clearButton = new JButton("Clear List");
    JButton hideButton = new JButton("Hide");
    public PacketView pViewTemp;
    public ArrayList packets;
    int order;
    int tempInt;
    
    public PacketListFrame(MainFrame mainFrame) throws Exception {
        super();
        frameInit();
        setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(450,330));
        this.setPreferredSize(new Dimension(450,330));
        try{
        	this.setAlwaysOnTop(true);
        }catch(Exception e){}
        Container container = getContentPane();
        mainWindow = mainFrame;
        order = 1;
        
        packets = new ArrayList();
        packets.clear();
        
        mainPanel = new JPanel();
        mainPanel.setLayout(new AbsoluteLayout());
        
        /*
         * Create JTable to display packets' info 
         */
        model = new DefaultTableModel(){
        	public boolean isCellEditable(int row, int column){
        		return false;
        	}
        };
        model.addColumn("Order");
        model.addColumn("From");
        model.addColumn("To");
        model.addColumn("Type");
        listTable = new JTable(model);
        listTable.setAutoCreateRowSorter(true);
        listTable.addMouseListener(this);
        
        JScrollPane listScroll = new JScrollPane(listTable);
        listScroll.setPreferredSize(new Dimension(450,265));
        listScroll.addMouseListener(this);
        clearButton.addActionListener(this);
        hideButton.addActionListener(this);
        
        mainPanel.add(listScroll, new AbsoluteConstraints(0,0,440,265));
        mainPanel.add(hideButton, new AbsoluteConstraints(3,267,115,30));
        mainPanel.add(clearButton, new AbsoluteConstraints(322,267,115,30));
        container.add(mainPanel, BorderLayout.CENTER);
        
        /***********************************************************************
         * SET FRAME PROPERTIES
         **********************************************************************/
        setTitle("Packet Watcher");
        
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        setResizable(false);
        setVisible(false);
        pack();

        /***********************************************************************
         * SET THE LOCATION OF THE FRAME ON THE SCREEN
         **********************************************************************/
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        int w = getSize().width;
        int h = getSize().height;
        int x = (dim.width - w) / 2;
        int y = (dim.height - h) / 2;
        setLocation(x, y);


    }

    public void addPacket(PacketListUnit p){
    	packets.add(p);
    	model.addRow(new Object[] { order, p.getLastDevice(), p.getAtDevice(), p.getType()});
    	 order++;
    	
    }
    
    public void clearList(){
    	while (model.getRowCount() > 0){
    		tempInt = model.getRowCount();
    		model.removeRow(tempInt-1);
    	}
    	if (!packets.isEmpty()){
    		packets.clear();
    	}
    	order = 1;
    	validate();
    }

	public void actionPerformed(ActionEvent ae) {
		
		Object source = ae.getSource();
		
		if (source == hideButton){
			this.setVisible(false);
		}
		else if (source == clearButton){
			clearList();
		}
		
	}


	public void mouseClicked(MouseEvent arg0) {
		Object source = arg0.getSource();
		if (source instanceof JTable){
			Integer i = (Integer) listTable.getValueAt(listTable.getSelectedRow(), 0);
			
			String tempString = "[ Packet " + Integer.toString(i) + " ]  [ From: " + listTable.getValueAt(listTable.getSelectedRow(), 1) + " ][ To: " + listTable.getValueAt(listTable.getSelectedRow(), 2) + " ]";
			
			pViewTemp = new PacketView(tempString, ((PacketListUnit) packets.get(i-1)).getPacket());			
			pViewTemp.setVisible(true);
			//generate Packet view from packets[i]; show JFrame with Packet view in it		
		}
		
		
	}

	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
