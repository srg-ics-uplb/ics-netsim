package platform.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;

import java.io.IOException;

import javax.imageio.ImageIO;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;


public class NameDialog extends CenterableDialog implements ItemListener, ActionListener {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -6438823324382750139L;
	private final static String DEFAULT = "DEFAULT NAME";
    private final static String NAME = "NEW NAME";
    private JRadioButton defaultButton;
    private JRadioButton nameButton;
    private JTextField nameField;
    private JLabel labelField;
    private JButton applyButton;
    private JButton cancelButton;
    protected String deviceName = "";
    protected boolean defaultSelected = false;
    protected boolean emptyName = true;

    public NameDialog(MainFrame mainFrame) {
        super((JFrame) mainFrame, "Set Device Name", true);

        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new BorderLayout());
        panel.setSize(300, 200);

        try {
            ClassLoader loader = getClass().getClassLoader();
            BufferedImage image = ImageIO.read(loader.getResource("images/graphics/nameDialog.jpg"));

            panel.setBorder(new BackgroundImageBorder(image));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JLabel instruction = new JLabel();
        instruction.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(instruction, BorderLayout.NORTH);
        Utilities.wrapLabelText(instruction, "Enter hostname for this device or click 'Default' to use the default name");

        defaultButton = new JRadioButton("Use the default name.");
        defaultButton.setActionCommand(DEFAULT);
        defaultButton.setSelected(true);

        nameButton = new JRadioButton("Enter the device name");
        nameButton.setActionCommand(NAME);

        ButtonGroup group = new ButtonGroup();
        group.add(defaultButton);
        group.add(nameButton);
        defaultButton.setOpaque(false);
        nameButton.setOpaque(false);

        JPanel radioPanel = new JPanel();

        radioPanel.setOpaque(false);
        radioPanel.setLayout(new BorderLayout());
        radioPanel.add(defaultButton, BorderLayout.NORTH);
        radioPanel.add(nameButton, BorderLayout.CENTER);
        radioPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        nameField = new JTextField(15);
        nameField.setEditable(false);
        nameField.setBackground(Color.white);
        labelField = new JLabel("Device Name: ");

        JPanel nameFieldPanel = new JPanel();
        nameFieldPanel.setLayout(new FlowLayout());
        nameFieldPanel.add(labelField);
        nameFieldPanel.add(nameField);
        nameFieldPanel.setOpaque(false);
        radioPanel.add(nameFieldPanel, BorderLayout.SOUTH);

        defaultButton.addItemListener(this);
        nameButton.addItemListener(this);
        panel.add(radioPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        applyButton = new JButton("Apply");
        buttonPanel.add(applyButton);
        cancelButton = new JButton("Cancel");
        buttonPanel.add(cancelButton);

        buttonPanel.setOpaque(false);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        applyButton.addActionListener(this);
        cancelButton.addActionListener(this);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
    }

    public void itemStateChanged(ItemEvent ie) {
        Object source = ie.getItemSelectable();

        if (source == defaultButton) {
            if (nameField.isEditable()) {
                nameField.setText("");
                nameField.setEditable(false);
            }
        } else if (source == nameButton) {
            if (!nameField.isEditable()) {
                nameField.setEditable(true);
                nameField.requestFocus();
            }
        }
    }

    private void checkName() {
        if (nameButton.isSelected()) {
            defaultSelected = false;

            if (nameField.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Please enter a name to continue.", "Scalable Cisco IOS Simulator", JOptionPane.WARNING_MESSAGE);
            } else {
                deviceName = nameField.getText();
                emptyName = false;
                dispose();
            }
        } else if (defaultButton.isSelected()) {
            emptyName = false;
            defaultSelected = true;
            dispose();
        }
    }

    public void actionPerformed(ActionEvent ae) {
        Object source = ae.getSource();

        if (source == applyButton) {
            checkName();
        } else if (source == cancelButton) {
            emptyName = true;
            dispose();
        }
    }
}
