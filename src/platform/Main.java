package platform;

import platform.gui.MainFrame;


import java.lang.reflect.InvocationTargetException;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


public class Main {
    MainFrame mainFrame;


    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.getStackTrace();
        }

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        new Main().launch();
    }

    public void launch() {
        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        try {
							mainFrame = new MainFrame();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
                        
                    }
                });
        } catch (InterruptedException e) {
        } catch (InvocationTargetException e) {
            InternalError error = new InternalError();
            error.initCause(e);
            throw error;
        }
    }
}
