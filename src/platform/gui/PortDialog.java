package platform.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import devices.Device;
import devices.hosts.Host;
import devices.interfaces.Interface;
import devices.routers.Router;
import devices.switches.Switch1900;
import devices.switches.Switch2950;



public class PortDialog extends CenterableDialog implements MouseListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3354622243460495145L;
	ClassLoader loader = getClass().getClassLoader();
    JPanel panel;
    BufferedImage background;
    
    public int interfaceCount=0, addOrRemove=0;
    Interface[] interfaceArray;
    
    JLabel[] eth = new JLabel[13];
    JLabel[] feth = new JLabel[13];
    JLabel[] se = new JLabel[6];
    
    

    public PortDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Choose a Port", true);
        this.setPreferredSize(new Dimension(500,300));
              
        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new GridLayout(6,9));
        panel.setSize(300, 200);
        panel.setBackground(new Color(10));

        for (int l =0; l<13; l++){
        	eth[l] = new JLabel("Eth0/" + l);     
        	feth[l] = new JLabel("Fast Eth0/" + l); 
        	
        	eth[l].setForeground(Color.RED);
        	feth[l].setForeground(Color.RED);
        	
        	panel.add(feth[l]);    	
            panel.add(eth[l]);
            
            eth[l].addMouseListener(this);
            feth[l].addMouseListener(this);          
            
        }
        
        for (int l =0; l<6; l++){
        	se[l] = new JLabel("Serial " + l);
        	se[l].setForeground(Color.RED);
        	panel.add(se[l]);
        	se[l].addMouseListener(this);  
   
        }

       hideAll();
        
        pack();
    }
    
    public void setPanelBackground(Device device){

    	if (device instanceof Router){
            try {
                BufferedImage image = ImageIO.read(loader.getResource("images/graphics/routerBG.jpg"));
                panel.setBorder(new BackgroundImageBorder(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}else if (device instanceof Switch1900){
    		panel.setPreferredSize(new Dimension(900, 130));
    		try {
                BufferedImage image = ImageIO.read(loader.getResource("images/graphics/switch1900BG.jpg"));
                panel.setBorder(new BackgroundImageBorder(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}else if (device instanceof Switch2950){
    		try {
                BufferedImage image = ImageIO.read(loader.getResource("images/graphics/switch2950BG.jpg"));
                panel.setBorder(new BackgroundImageBorder(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
    	}else if (device instanceof Host){
            try {
                BufferedImage image = ImageIO.read(loader.getResource("images/graphics/hostBG.jpg"));
                panel.setBorder(new BackgroundImageBorder(image));
            } catch (IOException e) {
                e.printStackTrace();
            }
    	};   	    	
    }
    
    public void setActiveHostInterface(Interface face, int index){
    	if (index>=-1){
    		interfaceArray[index] = face;
    		for (int l=0; l<12; l++){
    			if ((face.getName()).equalsIgnoreCase("Ethernet0/" + l)){
	    			eth[l].setVisible(true);
	    			eth[l].setToolTipText(Integer.toString(index));
    			}
    			if ((face.getName()).equalsIgnoreCase("FastEthernet0/" + l)){
	    			feth[l].setVisible(true);
	    			feth[l].setToolTipText(Integer.toString(index));
    			}
    		}
    		for (int l=0; l<6; l++){
    			if ((face.getName()).equalsIgnoreCase("Serial0/" + l)){
	    			se[l].setVisible(true);
	    			se[l].setToolTipText(Integer.toString(index));
    			}
    		}
    	}
    }
    
    public void setInterfaceArray(){
    	interfaceArray =new Interface[interfaceCount];
    }

    public void hideAll(){
    	 
    	 for(int l=0; l<13; l++){
    		 eth[l].setVisible(false);
    		 feth[l].setVisible(false);    		 
    	 }
    	
    	 for(int l=0; l<6; l++){
    		 se[l].setVisible(false);  
    	 }
    	
    }




	public void mouseClicked(MouseEvent ae) {
		JLabel source = (JLabel)ae.getSource();

		if (addOrRemove == 1 && interfaceCount >=0){
			MainFrame.DESIGNER_PANEL.showAddConnection(interfaceArray[Integer.parseInt(source.getToolTipText())]);
		} else if (addOrRemove == -1 && interfaceCount >=0){
			MainFrame.DESIGNER_PANEL.removeConnection(interfaceArray[Integer.parseInt(source.getToolTipText())]);
		}
		this.setVisible(false);
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
