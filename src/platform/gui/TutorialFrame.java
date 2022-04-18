package platform.gui;
import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;


public class TutorialFrame extends JFrame implements ActionListener{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -164901047236413817L;
    ClassLoader loader = getClass().getClassLoader();

    int counter=0;
    String[] category;
    String[] html;
    String[] filename;

    JPanel mainPanel = new JPanel();
    String defaultPage="";
    File f;
    JEditorPane editorPane = new JEditorPane();
    JPanel listPane = new JPanel();
    MainFrame mainWindow;
    
    public TutorialFrame(MainFrame mainFrame) throws Exception {
        super();
        frameInit();
        setLayout(new BorderLayout());
        this.setMaximumSize(new Dimension(900,600));
        this.setPreferredSize(new Dimension(900,600));
        Container container = getContentPane();
        mainWindow = mainFrame;

        try{
           f = new File("tutorials/list.txt");
           Scanner fileScanner = new Scanner(f);            
            while (fileScanner.hasNextLine()){
            	fileScanner.nextLine();
            	fileScanner.nextLine();
            	fileScanner.nextLine();
            	fileScanner.nextLine();
            	counter++;
            } 
            fileScanner.close();
        }catch (Exception ex1){
        	
        }  
                   
            category = new String[counter];
            html=new String[counter];
            filename=new String[counter];
            int counter2=0;
           
            
         try{
             f = new File("tutorials/list.txt");
             Scanner fileScanner = new Scanner(f);      
            do{
             category[counter2]=fileScanner.nextLine();
             html[counter2]=fileScanner.nextLine();
             filename[counter2]=fileScanner.nextLine();
             fileScanner.nextLine();
             counter2++;
            }while (counter2<counter);
            

       	
        }catch (Exception e) {
            e.printStackTrace();
        }


        f = new File("tutorials/html_files/index.html");
        defaultPage = f.getPath();
        
        try {
            editorPane.setPage(f.toURL());
        } catch (IOException ie) {
            ie.printStackTrace();
        }


        listPane.setLayout(new GridLayout(50,1));

        for (int j=0; j<counter; j++){
        	JButton dummy = new JButton (category[j]);
        	dummy.addActionListener(this);
        	listPane.add(dummy);
        	
        }

        
        
        editorPane.setEditable(false);
        JToolBar topToolBar = new JToolBar();
        topToolBar.setBorderPainted(false);
        topToolBar.setMargin(new Insets(0, 0, 0, 0));
        topToolBar.setFloatable(false);

        topToolBar.setOpaque(false);

        JPanel banner = new JPanel();
        banner.setSize(900, 100);
        banner.setLayout(new AbsoluteLayout());
        banner.add(topToolBar, new AbsoluteConstraints(0, 60, 320, 40));

        try {
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/tempBanner.jpg"));
            banner.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JScrollPane listScroll = new JScrollPane(listPane);
        listScroll.setPreferredSize(new Dimension(300,450));
        mainPanel.add(listScroll);
      
        JScrollPane mainScroll = new JScrollPane(editorPane);
        mainScroll.setPreferredSize(new Dimension(550,450));
        mainPanel.add(mainScroll);
 
        container.add(banner, BorderLayout.NORTH);
        container.add(mainPanel, BorderLayout.CENTER);
     

        /***********************************************************************
         * SET FRAME PROPERTIES
         **********************************************************************/
        setTitle("Scalable Cisco IOS Simulator Tutorial Browser");
        
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


	public void actionPerformed(ActionEvent ae) {
		Object source = ae.getSource();
		
		if (source instanceof JButton){
			JButton dummyButton = (JButton) source;
			int marker=-1;
			for (int x=0; x<counter; x++){
				if (category[x].equals(dummyButton.getText())) marker=x;
			}
			int response = JOptionPane.showConfirmDialog(this, "Loading a tutorial setup will disregard any changes you made in the current network topology.\n\nDo you want to continue anyway?", "Load tutorial file?", JOptionPane.YES_NO_OPTION);

            if (response == JOptionPane.YES_OPTION) {
				try {
					f=new File(html[marker]);
					editorPane.setPage(f.toURL());
					mainWindow.loadDevices(new File(filename[marker]));
				} catch (Exception e) {
					JOptionPane.showMessageDialog(this, "One or more of the associated files were not found! Please see the tutorial list.");
					e.printStackTrace();
				}
		 
            }
		}
		
	}







}
