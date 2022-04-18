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



public class Switch2950PortDialog extends CenterableDialog implements MouseListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -738461041925788097L;
	ClassLoader loader = getClass().getClassLoader();
    JPanel panel;
    BufferedImage background;
    
    public int interfaceCount=0, addOrRemove=0;
    Interface[] interfaceArray;
    
    JLabel[] feth = new JLabel[12];
    JLabel[] fethLights = new JLabel[12];
    
    

    public Switch2950PortDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Choose a Port", true);
        
        this.setPreferredSize(new Dimension(910,180));
        
        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new GridLayout(3,20));
        panel.setSize(600, 250);
        panel.setBackground(new Color(10));

        try {
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/switch2950.png"));
            panel.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        


        for (int l =0; l<12; l++){
        	feth[l] = new JLabel("Fast0/" + l); 
        	feth[l].setForeground(Color.RED);
            feth[l].addMouseListener(this);
            fethLights[l] = new JLabel(new ImageIcon(loader.getResource("images/graphics/lights_off.png")));
                       
        }

        for (int j=0; j<21; j++){        	
        	panel.add(new JLabel(""));
        }
        for (int l =0; l<12; l++){
        	panel.add(feth[l]);   
        }
        for (int j=0; j<6; j++){        	
        	panel.add(new JLabel(""));
        }
        for (int l =0; l<12; l++){
        	panel.add(fethLights[l]);   
        }
        for (int j=0; j<3; j++){        	
        	panel.add(new JLabel(""));
        }
        
       hideAll();
        
        pack();
    }
        
    public void setActiveHostInterface(Interface face, int index){
    	if (index>=-1){
    		interfaceArray[index] = face;
    		for (int l=0; l<12; l++){

    			if ((face.getName()).equalsIgnoreCase("FastEthernet0/" + (l+1))){
	    			feth[l].setVisible(true);
	    			feth[l].setToolTipText(Integer.toString(index));
    			}
    		}
    	}
    }
    
    public void setLights(){
		for (int i=0; i<12; i++){
	    	if(addOrRemove==1) {
				if(feth[i].isVisible()){
					fethLights[i].setIcon(new ImageIcon(loader.getResource("images/graphics/lights_off.png")));
				} else{
					fethLights[i].setIcon(new ImageIcon(loader.getResource("images/graphics/lights_on.png")));
				}	        			
			}
			else if(addOrRemove==-1){
				if(feth[i].isVisible()){
					fethLights[i].setIcon(new ImageIcon(loader.getResource("images/graphics/lights_on.png")));
				} else{
					fethLights[i].setIcon(new ImageIcon(loader.getResource("images/graphics/lights_off.png")));
				}
			}
		}
    }
    
    public void setInterfaceArray(){
    	interfaceArray =new Interface[interfaceCount];
    }

    public void hideAll(){

    	 for(int l=0; l<12; l++){
    		 feth[l].setVisible(false);    		 
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
