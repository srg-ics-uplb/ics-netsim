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



public class Router2600PortDialog extends CenterableDialog implements MouseListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8873641869853281099L;
	ClassLoader loader = getClass().getClassLoader();
    JPanel panel;
    BufferedImage background;
    
    public int interfaceCount=0, addOrRemove=0;
    Interface[] interfaceArray;
    
    JLabel[] feth = new JLabel[2];
    JLabel[] fethLights = new JLabel[2];
    
    

    public Router2600PortDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Choose a Port", true);
        
        this.setPreferredSize(new Dimension(601,280));
              
        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new GridLayout(15,15));
        panel.setSize(601, 270);
        panel.setBackground(new Color(10));

        try {
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/router2600.png"));
            panel.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }
        

        for (int l =0; l<2; l++){
        	feth[l] = new JLabel("FastEthernet0/" + l); 
        	feth[l].setForeground(Color.RED);
            feth[l].addMouseListener(this);
            
            fethLights[l] = new JLabel(new ImageIcon(loader.getResource("images/graphics/lights_off2.png")));
            
        }

        for (int j=0; j<37; j++){        	
        	panel.add(new JLabel("    "));
        }
    	panel.add(feth[0]);
    	panel.add(feth[1]);
        for (int j=0; j<23; j++){        	
        	panel.add(new JLabel("    "));
        }
        panel.add(fethLights[0]);
    	panel.add(fethLights[1]);
        for (int j=0; j<7; j++){        	
        	panel.add(new JLabel("    "));
        }
        
       hideAll();
        
        pack();
    }
        
    public void setActiveHostInterface(Interface face, int index){
    	if (index>=-1){
    		interfaceArray[index] = face;
    		for (int l=0; l<2; l++){
    			if ((face.getName()).equalsIgnoreCase("FastEthernet0/" + l)){
	    			feth[l].setVisible(true);
	    			feth[l].setToolTipText(Integer.toString(index));
    			}
    		}
    	}
    }
    
    public void setLights(){
		for (int i=0; i<2; i++){
	    	if(addOrRemove==1) {
				if(feth[i].isVisible()){
					fethLights[i].setIcon(new ImageIcon(loader.getResource("images/graphics/lights_off2.png")));
				} else{
					fethLights[i].setIcon(new ImageIcon(loader.getResource("images/graphics/lights_on2.png")));
				}	        			
			}
			else if(addOrRemove==-1){
				if(feth[i].isVisible()){
					fethLights[i].setIcon(new ImageIcon(loader.getResource("images/graphics/lights_on2.png")));
				} else{
					fethLights[i].setIcon(new ImageIcon(loader.getResource("images/graphics/lights_off2.png")));
				}
			}
		}
    }
    
    public void setInterfaceArray(){
    	interfaceArray =new Interface[interfaceCount];
    }

    public void hideAll(){
    	 
    	 for(int l=0; l<2; l++){
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
