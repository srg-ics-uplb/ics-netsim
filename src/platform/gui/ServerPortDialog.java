package platform.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import devices.interfaces.Interface;



public class ServerPortDialog extends CenterableDialog implements MouseListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 8826603527415474025L;
	ClassLoader loader = getClass().getClassLoader();
    JPanel panel;
    BufferedImage background;
    JLabel lights = new JLabel(new ImageIcon(loader.getResource("images/graphics/back.png")));
    
    
    public int interfaceCount=0, addOrRemove=0;
    Interface[] interfaceArray;
    
    JLabel eth = new JLabel();

    
    

    public ServerPortDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Choose a Port", true);
        this.setPreferredSize(new Dimension(350,140));
              
        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new GridLayout(2,7));
        panel.setSize(600, 250);
        panel.setBackground(new Color(10));

        try {
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/server.png"));
            panel.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        

    	eth = new JLabel("Eth0/0"); 
    	eth.setForeground(Color.RED);
        eth.addMouseListener(this);   



        for (int j=0; j<2; j++){        	
        	panel.add(new JLabel(""));
        }
        
        panel.add(eth);
               
        for (int j=0; j<6; j++){        	
        	panel.add(new JLabel(""));
        }        
        panel.add(lights);
        
        for (int j=0; j<3; j++){        	
        	panel.add(new JLabel(""));
        }
        
       hideAll();
        
        pack();
    }


    public void setActiveServerInterface(Interface face, int index){
    	if (index>=-1){
    		interfaceArray[index] = face;
    		for (int l=0; l<12; l++){
    			if ((face.getName()).equalsIgnoreCase("Ethernet0/" + l)){
	    			eth.setVisible(true);
	    			eth.setToolTipText(Integer.toString(index));
    			}
    		}
    	}
    }
    
    public void setLights(){
		if(addOrRemove==1) {
			if(eth.isVisible()){
				lights.setIcon(new ImageIcon(loader.getResource("images/graphics/lights_off2.png")));
			} else{
				lights.setIcon(new ImageIcon(loader.getResource("images/graphics/lights_on2.png")));
			}	        			
		}
		else if(addOrRemove==-1){
			if(eth.isVisible()){
				lights.setIcon(new ImageIcon(loader.getResource("images/graphics/lights_on2.png")));
			} else{
				lights.setIcon(new ImageIcon(loader.getResource("images/graphics/lights_off2.png")));
			}
		}
    	
    }
    
    
    public void setInterfaceArray(){
    	interfaceArray =new Interface[interfaceCount];
    }

    public void hideAll(){
    		 eth.setVisible(false);    		 
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
