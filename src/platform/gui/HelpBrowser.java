package platform.gui;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.io.IOException;

import java.net.URL;

import java.util.Stack;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;


public class HelpBrowser extends CenterableDialog implements ActionListener, HyperlinkListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 285329516307572544L;
	private JEditorPane editorPane = null;
    private final JPanel panel;
    private final JButton back;
    private final JButton forward;
    private final JButton home;
    private Stack backStack = new Stack();
    private Stack forwardStack = new Stack();
    private String previous = "";
    private final String homepage;

    public HelpBrowser(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Help Browser", true);

        ClassLoader loader = getClass().getClassLoader();

        panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new AbsoluteLayout());
        panel.setSize(640, 520);
        this.setResizable(false);

        back = new JButton(new ImageIcon(loader.getResource("images/graphics/back.png")));
        back.addActionListener(this);

        forward = new JButton(new ImageIcon(loader.getResource("images/graphics/forward.png")));
        forward.addActionListener(this);

        home = new JButton(new ImageIcon(loader.getResource("images/graphics/home.png")));
        home.addActionListener(this);

        JToolBar topToolBar = new JToolBar();
        topToolBar.setBorderPainted(false);
        topToolBar.setMargin(new Insets(0, 0, 0, 0));
        topToolBar.setFloatable(false);
        topToolBar.add(back);
        topToolBar.add(forward);
        topToolBar.add(home);

        //		if file is to be loaded 
        //		homepage = "file:///C|/index.html";
        //		else
        //		homepage = "http://www.google.com";
        homepage = loader.getResource("html/index.html").toString();

        try {
            editorPane = new JEditorPane(homepage);
        } catch (IOException ie) {
            ie.printStackTrace();
        }

        editorPane.setEditable(false);
        editorPane.addHyperlinkListener(this);

        back.addActionListener(this);
        forward.addActionListener(this);
        home.addActionListener(this);

        previous = homepage;

        panel.add(topToolBar, new AbsoluteConstraints(0, 0, 640, 40));
        panel.add(new JScrollPane(editorPane), new AbsoluteConstraints(0, 40, 640, 480));

        this.setResizable(true);

        this.addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    dispose();
                }
            });

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
    }

    public void setPage() {
    }

    public void actionPerformed(ActionEvent ae) {
        try {
            if (ae.getSource() == back) {
                if (!backStack.empty()) {
                    String str = backStack.pop().toString();
                    editorPane.setPage(new URL(str));
                    forwardStack.push(previous);
                    previous = str;
                }
            } else if (ae.getSource() == forward) {
                if (!forwardStack.empty()) {
                    String str = forwardStack.pop().toString();
                    editorPane.setPage(new URL(str));
                    backStack.push(previous);
                    previous = str;
                }
            } else if (ae.getSource() == home) {
                if (!forwardStack.empty()) {
                    String str = forwardStack.pop().toString();
                    editorPane.setPage(new URL(str));
                    backStack.push(previous);
                    previous = str;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            try {
                //JOptionPane.showMessageDialog(this, e.getURL());
                backStack.push(previous);
                editorPane.setPage(e.getURL());

                //				address.setText(e.getURL().toString());
                //				previous = address.getText().toString();
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
