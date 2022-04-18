package devices.consoles;

import devices.Device;

import devices.addresses.IPAddress;
import devices.addresses.SubnetMask;

import devices.commands.Command;

import devices.hosts.Host;
import devices.servers.Server;

import devices.hosts.consoles.HostConsole;
import devices.servers.consoles.ServerConsole;

import devices.interfaces.FastEthernet;
import devices.interfaces.Interface;

import devices.routers.Router;

import devices.routers.accesslists.ExtendedIPAccessList;
import devices.routers.accesslists.ExtendedIPPermission;
import devices.routers.accesslists.IPAccessList;
import devices.routers.accesslists.IPPermission;
import devices.routers.accesslists.StandardIPAccessList;
import devices.routers.accesslists.StandardIPPermission;
import devices.routers.accesslists.Wildcard;

import devices.routers.consoles.RouterConsole;

import devices.routers.routingtable.Entry;
import devices.routers.routingtable.RoutingTable;

import devices.switches.Switch;

import devices.switches.consoles.SwitchConsole;

import devices.switches.vlan.VLAN;

import platform.gui.CenterableDialog;
import platform.gui.MainFrame;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;


public class Console extends CenterableDialog implements CaretListener {

	private static final long serialVersionUID = -492311851212566004L;
	/***************************************************************************
     * MODES - USER MODE, PRIVILEGED MODE, ETC.
     **************************************************************************/
    protected final static int INITIAL_MODE = 0;
    protected final static int USER_MODE = 1;
    protected final static int PRIVILEGED_MODE = 2;
    protected final static int CONFIGURATION_MODE = 3;
    protected final static int PASSWORD_MODE = 4;
    protected final static int INTERFACE_MODE = 5;
    protected final static int ROUTER_MODE = 6;
    protected final static int VLAN_MODE = 6;

    /***************************************************************************
     * DIFFERENT PROMPTS FOR DIFFERENT MODES
     ***************************************************************************/
    protected final static String USER_MODE_PROMPT = ">";
    protected final static String PRIVILEGED_MODE_PROMPT = "#";
    protected final static String CONFIGURATION_MODE_PROMPT = "(config)#";
    protected final static String PASSWORD_MODE_PROMPT = "Password:";
    protected final static String INTERFACE_MODE_PROMPT = "(config-if)#";
    protected final static String ROUTER_MODE_PROMPT = "(config-router)#";
    protected final static String VLAN_MODE_PROMPT = "(vlan)#";

    /***************************************************************************
     * ERRORS
     ***************************************************************************/
    private final static String AMBIGUOUS_COMMAND_ERROR = "% Ambiguous command: ";
    private final static String COMMAND_DOES_NOT_EXIST_ERROR = "% Invalid input detected at '^' marker.";
    private final static String INCOMPLETE_COMMAND_ERROR = "% Incomplete command.";

    /***************************************************************************
     * FORMATTING CONSTANTS
     **************************************************************************/
    protected final static int DESCRIPTION_COLUMN = 40;
    protected final static int IP_ADDRESS_COLUMN = 22;
    protected final static int INTERFACE_OK_COLUMN = 16;
    protected final static int INTERFACE_METHOD_COLUMN = 6;
    protected final static int INTERFACE_STATUS_COLUMN = 10;
    protected final static int PROTOCOL_STATUS_COLUMN = 27;
    private Device device;
    protected final JTextArea textArea = new JTextArea();
    protected String currentPrompt = "";

    /***************************************************************************
     * ESSENTIAL VARIABLES IN CONSOLE
     **************************************************************************/
    protected final Stack backInputs = new Stack();
    protected final Stack forwardInputs = new Stack();
    protected int currentCaretPosition = 0;
    protected int currentMode;
    protected Command[] availableCommands;
    protected int passwordTries = 0;
    protected final StringBuffer passwordInput = new StringBuffer();
    protected Interface currentInterface;

    /***************************************************************************
     * TROUBLESHOOTING TOOLS - PING, PORTSCAN (NEEDED VARIABLES)
     **************************************************************************/
    private boolean reachable = false;
    private Interface destinationInterface = null;
    private boolean traceable = false;
    private String routeOfTrace="", prevRoutePoint="";
    private int hops=0;

    /**************************************************************************
     * CONSTRUCTOR
     **************************************************************************/
    public Console(Device device, MainFrame frame) {
        super(frame, device.toString() + " Console", true);
        this.device = device;

        Container container = getContentPane();
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(700, 700));
        container.add(scrollPane);
        textArea.setMargin(new Insets(5, 5, 5, 5));
        textArea.setBackground(Color.BLACK);
        textArea.setForeground(Color.WHITE);
        textArea.setCaretColor(Color.WHITE);
        textArea.setFont(new Font("Lucida Console", 1, 12));
        textArea.addCaretListener(this);
        addKeyBindings();
        initializeListeners();
        setMode(INITIAL_MODE);
        showPrompt();
        setSize(800, 600);
    }

    public Device getDevice() {
        return device;
    }

    protected void setCurrentInterface(Interface currentInterface) {
        this.currentInterface = currentInterface;
    }

    private void setMode(int mode) {
        if (this instanceof RouterConsole) {
            ((RouterConsole) this).setMode(mode);
        } else if (this instanceof SwitchConsole) {
            ((SwitchConsole) this).setMode(mode);
        } else if (this instanceof HostConsole) {
            ((HostConsole) this).initialize();
        } else if (this instanceof ServerConsole) {
            ((ServerConsole) this).initialize();
        }
    }

    /***************************************************************************
     * HANDLES EVENTS THAT USE KEY STROKES - CTRL Z, SHIFT /, ETC.
     **************************************************************************/
    private void addKeyBindings() {
        InputMap inputMap = textArea.getInputMap();
        ActionMap actionMap = textArea.getActionMap();

        Action question = new AbstractAction() {
            

            public void actionPerformed(ActionEvent ae) {
                if ((currentMode != INITIAL_MODE) && (currentMode != PASSWORD_MODE)) {
                    String input = getInput();
                    textArea.append("?");
                    input = input.substring(0, input.length());

                    if (input.equals("")) {
                        showCommands(availableCommands);
                    } else {
                        showPossibleCommands(input, availableCommands, input);
                    }

                    SwingUtilities.invokeLater(new Runnable() {
                            public void run() {
                                String contents = textArea.getText();

                                if (contents.endsWith("?")) {
                                    textArea.setText(contents.substring(0, contents.length() - 1));
                                }
                            }
                        });
                }
            }
        };

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, InputEvent.SHIFT_MASK), "QUESTION");
        actionMap.put("QUESTION", question);

        Action ctrlz = new AbstractAction() {
            

            public void actionPerformed(ActionEvent ae) {
                if ((currentMode == CONFIGURATION_MODE) || (currentMode == INTERFACE_MODE) || (currentMode == ROUTER_MODE)) {
                    setMode(PRIVILEGED_MODE);
                    showPrompt();
                }
            }
        };

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK), "CTRLZ");
        actionMap.put("CTRLZ", ctrlz);
    }

    protected void removeKeyBindings() {
        textArea.getInputMap().clear();
        textArea.getActionMap().clear();
    }

    /***************************************************************************
     * REMOVES UNNECESSARY MOUSE EVENTS - THROUGH REVERTING THE CARET TO ITS
     * PREVIOUS POSITION;
     *
     * HANDLES KEY EVENTS - MOSTLY ACTION KEYS LIKE ENTER, TAB, UP, LEFT, RIGHT,
     * DOWN, ETC.
     **************************************************************************/
    private void initializeListeners() {
        textArea.addMouseListener(new MouseAdapter() {
                public void mousePressed(MouseEvent me) {
                    if (currentMode != INITIAL_MODE) {
                        int validCaretPosition = getValidCaretPosition();

                        if (textArea.getCaretPosition() < validCaretPosition) {
                            textArea.setCaretPosition(currentCaretPosition);
                        }
                    } else {
                        textArea.setCaretPosition(currentCaretPosition);
                    }
                }
            });

        textArea.addMouseMotionListener(new MouseMotionAdapter() {
                public void mouseDragged(MouseEvent me) {
                    textArea.setCaretPosition(currentCaretPosition);
                }
            });

        textArea.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent ke) {
                    if (currentMode == INITIAL_MODE) {
                        ke.consume();
                    }
                }

                public void keyPressed(KeyEvent ke) {
                    if (currentMode == INITIAL_MODE) {
                        if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                            setMode(USER_MODE);
                            showPrompt();
                            textArea.setCaretPosition(textArea.toString().length());
                            ke.consume();
                        } else {
                            ke.consume();
                        }
                    } else {
                        switch (ke.getKeyCode()) {
                        case KeyEvent.VK_ENTER:

                            String input = getInput();

                            if (currentMode != PASSWORD_MODE) {
                                while (!forwardInputs.empty()) {
                                    backInputs.push(forwardInputs.pop());
                                }

                                backInputs.push(input);
                            } else {
                                input = passwordInput.toString();
                                passwordInput.delete(0, passwordInput.length());
                            }

                            processInput(input);
                            textArea.selectAll();
                            textArea.setCaretPosition(textArea.getSelectionEnd());
                            textArea.select(textArea.getCaretPosition(), textArea.getCaretPosition());
                            ke.consume();

                            break;

                        case KeyEvent.VK_TAB:
                            input = getInput();
                            showWholeCommand(input, availableCommands);
                            ke.consume();

                            break;

                        case KeyEvent.VK_UP:

                            if (!backInputs.empty()) {
                                String command = (String) backInputs.pop();
                                forwardInputs.push(command);
                                overwriteCurrentInput(command);
                            }

                            ke.consume();

                            break;

                        case KeyEvent.VK_DOWN:

                            if (!forwardInputs.empty()) {
                                String command = (String) forwardInputs.pop();
                                backInputs.add(command);
                                overwriteCurrentInput(command);
                            }

                            ke.consume();

                            break;

                        case KeyEvent.VK_LEFT:
                        case KeyEvent.VK_BACK_SPACE:

                            int offset = -1;

                            try {
                                offset = textArea.getLineStartOffset(textArea.getLineCount() - 1);
                            } catch (BadLocationException ble) {
                            }

                            int caretPosition = textArea.getCaretPosition();

                            if (!((caretPosition - (offset + currentPrompt.length())) > 0)) {
                                ke.consume();
                            }

                            break;

                        default:

                            if (currentMode == PASSWORD_MODE) {
                                passwordInput.append(ke.getKeyChar());
                                SwingUtilities.invokeLater(new Runnable() {
                                        public void run() {
                                            overwriteCurrentInput("");
                                        }
                                    });
                            }
                        }
                    }
                }
            });

        addWindowListener(new WindowAdapter() {
                public void windowClosing(WindowEvent we) {
                    setMode(INITIAL_MODE);
                    textArea.setText("");
                    showPrompt();
                }
            });
    }

    /***************************************************************************
     * CURRENT_CARET_POSITION IS USEFUL FOR REVERTING TO THE PREVIOUS CARET
     * POSITION... USED IN MOUSE EVENTS
     **************************************************************************/
    public void caretUpdate(CaretEvent ce) {
        currentCaretPosition = textArea.getCaretPosition();
    }

    /***************************************************************************
     * SHOW METHODS
     **************************************************************************/
    protected void showPrompt() {
        textArea.append("\n" + currentPrompt);
    }

    protected void showPossibleCommands(String inputCommand, Command[] availableCommands, String originalInput) {
        while (inputCommand.indexOf(" ") == 0) {
            inputCommand = inputCommand.substring(1);
        }

        StringTokenizer tokens = new StringTokenizer(inputCommand, " ");
        String lowerCaseInputCommand = "";

        if (tokens.hasMoreTokens()) {
            lowerCaseInputCommand = tokens.nextToken().toLowerCase();
        }

        Vector commands = new Vector();
        copyObjectsToVector(availableCommands, commands);

        if (isInteger(lowerCaseInputCommand)) {
            int num = Integer.parseInt(lowerCaseInputCommand);

            for (int i = 0; i < commands.size(); i++) {
                String name = ((Command) commands.get(i)).getName();

                if (!(name.startsWith("<") && name.endsWith(">"))) {
                    continue;
                }

                StringTokenizer numbers = new StringTokenizer(name.trim(), "<->");

                if (numbers.hasMoreTokens()) {
                    int lowerBound = Integer.parseInt(numbers.nextToken());
                    int upperBound = Integer.parseInt(numbers.nextToken());

                    if (!((num >= lowerBound) && (num <= upperBound))) {
                        commands.remove(i);
                        i--;
                    }
                } else {
                    commands.remove(i);
                    i--;
                }
            }
        } else {
            for (int i = 0; i < lowerCaseInputCommand.length(); i++) {
                char character = lowerCaseInputCommand.charAt(i);

                for (int j = 0; j < commands.size(); j++) {
                    Command command = (Command) commands.get(j);
                    String name = command.toString();

                    if (name.length() > i) {
                        char compareChar = name.charAt(i);

                        if (character != compareChar) {
                            commands.remove(j--);
                        }
                    } else {
                        commands.remove(j--);
                    }

                    if (commands.size() == 0) {
                        showPrompt();
                        textArea.append(originalInput);
                    }
                }
            }
        }

        if (commands.size() > 0) {
            Command command = (Command) commands.get(0);

            for (int i = 0; i < commands.size(); i++) {
                Command temp = (Command) commands.get(i);

                if (temp.toString().equals(lowerCaseInputCommand)) {
                    command = temp;
                }
            }

            String input = inputCommand.substring(lowerCaseInputCommand.length());

            if (input.trim().equals("")) {
                if (inputCommand.endsWith(" ")) {
                    Command[] arguments = command.getArguments();

                    if (arguments != null) {
                        showCommands(arguments);
                    } else {
                        showPrompt();
                    }

                    textArea.append(originalInput);
                } else {
                    textArea.append("\n");

                    for (int i = 0; i < commands.size(); i++) {
                        command = (Command) commands.get(i);

                        if (i == commands.size()) {
                            textArea.append(command.toString());
                        } else {
                            textArea.append(command.toString() + "\t\t\t");
                        }
                    }

                    showPrompt();
                    textArea.append(originalInput);
                }
            } else {
                Command[] arguments = command.getArguments();

                if (arguments != null) {
                    showPossibleCommands(input, arguments, originalInput);
                } else {
                    showPrompt();
                    textArea.append(originalInput);
                }
            }
        }
    }

    protected void showWholeCommand(String inputCommand, Command[] availableCommands) {
        while (inputCommand.indexOf(" ") == 0) {
            inputCommand = inputCommand.substring(1);
        }

        StringTokenizer tokens = new StringTokenizer(inputCommand, " ");
        String lowerCaseInputCommand = "";

        if (tokens.hasMoreTokens()) {
            lowerCaseInputCommand = tokens.nextToken().toLowerCase();
        }

        Vector commands = new Vector();
        copyObjectsToVector(availableCommands, commands);

        if (isInteger(lowerCaseInputCommand)) {
            int num = Integer.parseInt(lowerCaseInputCommand);

            for (int i = 0; i < commands.size(); i++) {
                String name = ((Command) commands.get(i)).getName();

                if (!(name.startsWith("<") && name.endsWith(">"))) {
                    continue;
                }

                StringTokenizer numbers = new StringTokenizer(name.trim(), "<->");

                if (numbers.hasMoreTokens()) {
                    int lowerBound = Integer.parseInt(numbers.nextToken());
                    int upperBound = Integer.parseInt(numbers.nextToken());

                    if (!((num >= lowerBound) && (num <= upperBound))) {
                        commands.remove(i);
                        i--;
                    }
                } else {
                    commands.remove(i);
                    i--;
                }
            }
        } else {
            for (int i = 0; i < lowerCaseInputCommand.length(); i++) {
                char character = lowerCaseInputCommand.charAt(i);

                for (int j = 0; j < commands.size(); j++) {
                    Command command = (Command) commands.get(j);
                    String name = command.toString();

                    if (name.length() > i) {
                        char compareChar = name.charAt(i);

                        if (character != compareChar) {
                            commands.remove(j--);
                        }
                    } else {
                        commands.remove(j--);
                    }
                }
            }
        }

        if (commands.size() > 0) {
            Command command = (Command) commands.get(0);

            for (int i = 0; i < commands.size(); i++) {
                Command temp = (Command) commands.get(i);

                if (temp.toString().equals(lowerCaseInputCommand)) {
                    command = temp;
                }
            }

            String input = inputCommand.substring(lowerCaseInputCommand.length());

            if (input.trim().equals("")) {
                String contents = textArea.getText();
                contents = contents.substring(0, contents.length() - inputCommand.length());
                textArea.setText(contents);
                textArea.append(command.toString());
            } else {
                Command[] arguments = command.getArguments();

                if (arguments != null) {
                    showWholeCommand(input, arguments);
                }
            }
        }
    }

    protected void showCommands(Command[] commands) {
        for (int i = 0; i < commands.length; i++) {
            textArea.append("\n");
            textArea.append(commands[i].toString());

            for (int j = commands[i].toString().length();
                    j < DESCRIPTION_COLUMN; j++) {
                textArea.append(" ");
            }

            textArea.append(commands[i].getDescription());
        }

        showPrompt();
    }

    /***************************************************************************
     * COPY BETWEEN COLLECTIONS/ARRAYS
     **************************************************************************/
    protected void copyObjectsToVector(Object[] objects, Vector commandsVector) {
        for (int i = 0; i < objects.length; i++) {
            commandsVector.add(objects[i]);
        }
    }

    protected void copyCommandsToArray(Vector vector, Command[] cmd) {
        for (int i = 0; i < vector.size(); i++) {
            Command command = (Command) vector.get(i);
            cmd[i] = command;
        }
    }

    /***************************************************************************
     * RETURNS THE OFFSET OF THE START OF VALID CARET POSITIONS IN THE TEXTAREA
     **************************************************************************/
    protected int getValidCaretPosition() {
        int lineCount = textArea.getLineCount();
        int offset = -1;

        try {
            offset = textArea.getLineStartOffset(lineCount - 1);
        } catch (BadLocationException ble) {
        }

        int validCaretPosition = offset + currentPrompt.length();

        return validCaretPosition;
    }

    protected void overwriteCurrentInput(String str) {
        int position = getValidCaretPosition();
        String contents = textArea.getText().substring(0, position);
        textArea.setText(contents);
        textArea.append(str);
    }

    /***************************************************************************
     * COMMAND PROCESSING ESSENTIALS
     **************************************************************************/
    private void processInput(String input) {
        if (this instanceof RouterConsole) {
            ((RouterConsole) this).processInput(input);
        } else if (this instanceof SwitchConsole) {
            ((SwitchConsole) this).processInput(input);
        } else if (this instanceof HostConsole) {
            ((HostConsole) this).processInput(input);
        } else if (this instanceof ServerConsole) {
            ((ServerConsole) this).processInput(input);
        }

        showPrompt();
    }

    protected Command getFullCommand(String inputCommand, Command[] availableCommands, int position) {
        String lowerCaseInputCommand = inputCommand.toLowerCase();
        Command returnCommand = null;
        Vector commands = new Vector();
        copyObjectsToVector(availableCommands, commands);

        for (int i = 0; i < lowerCaseInputCommand.length(); i++) {
            char character = lowerCaseInputCommand.charAt(i);

            for (int j = 0; j < commands.size(); j++) {
                Command command = (Command) commands.get(j);
                String name = command.toString();

                if (name.length() > i) {
                    char compareChar = name.charAt(i);

                    if (character != compareChar) {
                        commands.remove(j--);
                    }
                } else {
                    commands.remove(j--);
                }

                if (commands.size() == 0) {
                    showInvalidInputError(i + position);
                }
            }
        }

        if (commands.size() > 1) {
            for (int i = 0; i < commands.size(); i++) {
                Command temp = (Command) commands.get(i);

                if (temp.toString().equals(lowerCaseInputCommand)) {
                    returnCommand = temp;
                }
            }

            if (returnCommand == null) {
                showAmbiguityError(inputCommand);
            }
        } else if (commands.size() == 1) {
            returnCommand = (Command) commands.get(0);
        }

        return returnCommand;
    }

    protected Command getFullCommand(String inputCommand, Command[] availableCommands) {
        String lowerCaseInputCommand = inputCommand.toLowerCase();
        Command returnCommand = null;
        Vector commands = new Vector();
        copyObjectsToVector(availableCommands, commands);

        for (int i = 0; i < lowerCaseInputCommand.length(); i++) {
            char character = lowerCaseInputCommand.charAt(i);

            for (int j = 0; j < commands.size(); j++) {
                Command command = (Command) commands.get(j);
                String name = command.toString();

                if (name.length() > i) {
                    char compareChar = name.charAt(i);

                    if (character != compareChar) {
                        commands.remove(j--);
                    }
                } else {
                    commands.remove(j--);
                }
            }
        }

        if (commands.size() > 1) {
            for (int i = 0; i < commands.size(); i++) {
                Command temp = (Command) commands.get(i);

                if (temp.toString().equals(lowerCaseInputCommand)) {
                    returnCommand = temp;
                }
            }
        }

        return returnCommand;
    }

    protected int getNextPosition(StringTokenizer tokens, StringBuffer strBuff) {
        int position = 0;

        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();

            if (!token.equals(" ")) {
                strBuff.append(token);

                break;
            }

            position++;
        }

        return position;
    }

    protected String getInput() {
        int lastLineIndex = textArea.getLineCount() - 1;
        String input = "";

        try {
            int startOffset = textArea.getLineStartOffset(lastLineIndex);
            int length = textArea.getLineEndOffset(lastLineIndex) - startOffset;
            String lastLine = textArea.getText(textArea.getLineStartOffset(lastLineIndex), length);
            input = lastLine.substring(currentPrompt.length());
        } catch (BadLocationException ble) {
            ble.printStackTrace();
        }

        return input;
    }

    protected Interface getInterfaceFromName(String name, int position) {
        Interface selectedInterface = null;
        Interface[] interfaces = device.getInterfaces();
        Vector interfacesVector = new Vector();
        copyObjectsToVector(interfaces, interfacesVector);

        int digitIndex = getFirstIndexOfADigit(name);

        String argInterfaceType = null;

        if (digitIndex >= 0) {
            argInterfaceType = name.substring(0, digitIndex).toLowerCase();
        } else {
            argInterfaceType = name.toLowerCase();
        }

        boolean nameError = false;

        for (int i = 0; i < argInterfaceType.length(); i++) {
            char character = argInterfaceType.charAt(i);

            for (int j = 0; j < interfacesVector.size(); j++) {
                Interface compareInterface = (Interface) interfacesVector.get(j);
                String interfaceName = compareInterface.getName();
                int index = getFirstIndexOfADigit(interfaceName);

                if (index >= 0) {
                    String interfaceType = interfaceName.substring(0, index).toLowerCase();

                    if (interfaceType.length() > i) {
                        char compareChar = interfaceType.charAt(i);

                        if (character != compareChar) {
                            interfacesVector.remove(j--);
                        }
                    } else {
                        interfacesVector.remove(j--);
                    }

                    if (interfacesVector.size() == 0) {
                        showInvalidInputError(i + position);
                        nameError = true;
                    }
                }
            }
        }

        if (digitIndex >= 0) {
            String argInterfaceNumber = name.substring(digitIndex).toLowerCase();

            for (int i = 0; i < argInterfaceNumber.length(); i++) {
                char character = argInterfaceNumber.charAt(i);

                for (int j = 0; j < interfacesVector.size(); j++) {
                    Interface compareInterface = (Interface) interfacesVector.get(j);
                    String interfaceName = compareInterface.getName();
                    int index = getFirstIndexOfADigit(interfaceName);

                    if (index >= 0) {
                        String interfaceNumber = interfaceName.substring(index).toLowerCase();

                        if (interfaceNumber.length() > i) {
                            char compareChar = interfaceNumber.charAt(i);

                            if (character != compareChar) {
                                interfacesVector.remove(j--);
                            }
                        } else {
                            interfacesVector.remove(j--);
                        }

                        if (interfacesVector.size() == 0) {
                            showInvalidInputError(i + position + argInterfaceType.length());
                        }
                    }
                }
            }

            if (interfacesVector.size() >= 1) {
                for (int i = 0; i < interfacesVector.size(); i++) {
                    Interface temp = (Interface) interfacesVector.get(i);

                    if (temp.getName().endsWith(argInterfaceNumber)) {
                        selectedInterface = temp;
                    }
                }

                if (selectedInterface == null) {
                    showIncompleteCommandError();
                }
            } else if (interfacesVector.size() > 1) {
                showIncompleteCommandError();
            }
        } else if (!nameError) {
            showIncompleteCommandError();
        }

        return selectedInterface;
    }

    protected boolean isInterface(String name, int position) {
        Interface selectedInterface = null;
        Interface[] interfaces = device.getInterfaces();
        Vector interfacesVector = new Vector();
        copyObjectsToVector(interfaces, interfacesVector);

        int digitIndex = getFirstIndexOfADigit(name);

        String argInterfaceType = null;

        if (digitIndex >= 0) {
            argInterfaceType = name.substring(0, digitIndex).toLowerCase();
        } else {
            argInterfaceType = name.toLowerCase();
        }

        boolean nameError = false;

        for (int i = 0; i < argInterfaceType.length(); i++) {
            char character = argInterfaceType.charAt(i);

            for (int j = 0; j < interfacesVector.size(); j++) {
                Interface compareInterface = (Interface) interfacesVector.get(j);
                String interfaceName = compareInterface.getName();
                int index = getFirstIndexOfADigit(interfaceName);

                if (index >= 0) {
                    String interfaceType = interfaceName.substring(0, index).toLowerCase();

                    if (interfaceType.length() > i) {
                        char compareChar = interfaceType.charAt(i);

                        if (character != compareChar) {
                            interfacesVector.remove(j--);
                        }
                    } else {
                        interfacesVector.remove(j--);
                    }

                    if (interfacesVector.size() == 0) {
                       // showInvalidInputError(i + position);
                        nameError = true;
                    }
                }
            }
        }

        if (digitIndex >= 0) {
            String argInterfaceNumber = name.substring(digitIndex).toLowerCase();

            for (int i = 0; i < argInterfaceNumber.length(); i++) {
                char character = argInterfaceNumber.charAt(i);

                for (int j = 0; j < interfacesVector.size(); j++) {
                    Interface compareInterface = (Interface) interfacesVector.get(j);
                    String interfaceName = compareInterface.getName();
                    int index = getFirstIndexOfADigit(interfaceName);

                    if (index >= 0) {
                        String interfaceNumber = interfaceName.substring(index).toLowerCase();

                        if (interfaceNumber.length() > i) {
                            char compareChar = interfaceNumber.charAt(i);

                            if (character != compareChar) {
                                interfacesVector.remove(j--);
                            }
                        } else {
                            interfacesVector.remove(j--);
                        }

                        if (interfacesVector.size() == 0) {
                            //showInvalidInputError(i + position + argInterfaceType.length());
                        }
                    }
                }
            }

            if (interfacesVector.size() >= 1) {
                for (int i = 0; i < interfacesVector.size(); i++) {
                    Interface temp = (Interface) interfacesVector.get(i);

                    if (temp.getName().endsWith(argInterfaceNumber)) {
                        selectedInterface = temp;
                    }
                }

                if (selectedInterface == null) {
                    //showIncompleteCommandError();
                }
            } else if (interfacesVector.size() > 1) {
                //showIncompleteCommandError();
            }
        } else if (!nameError) {
            //showIncompleteCommandError();
        }
        if (selectedInterface!=null) return true; else return false; 
    }
       
    
    
    
    private int getFirstIndexOfADigit(String str) {
        int index = -1;

        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                index = i;

                break;
            }
        }

        return index;
    }

    /***************************************************************************
     * MISCELLANEOUS
     **************************************************************************/
    protected boolean isInteger(String arg, int position) {
        boolean isInteger = true;

        for (int i = 0; i < arg.length(); i++) {
            char ch = arg.charAt(i);

            if (!Character.isDigit(ch)) {
                showInvalidInputError(position + i);
                isInteger = false;

                break;
            }
        }

        return isInteger;
    }

    protected boolean isInteger(String arg) {
        boolean isInteger = true;

        for (int i = 0; i < arg.length(); i++) {
            char ch = arg.charAt(i);

            if (!Character.isDigit(ch)) {
                isInteger = false;

                break;
            }
        }

        return isInteger;
    }

    protected boolean isValidQuartet(String str, int position) {
        boolean isValidQuartet = true;
        int octetCounter = 0;
        boolean error = false;
        StringBuffer decimalBuffer = new StringBuffer();

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (octetCounter == 4) {
                showInvalidInputError((position + i) - 1);
                error = true;
            } else if (ch == '.') {
                if (decimalBuffer.length() > 0) {
                    decimalBuffer = decimalBuffer.delete(0, decimalBuffer.length());
                    octetCounter++;
                } else {
                    showInvalidInputError(position + i);
                    error = true;
                }
            } else {
                if (Character.isDigit(ch)) {
                    decimalBuffer.append(ch);

                    if ((decimalBuffer.length() == 3) || (i == (str.length() - 1))) {
                        int decimal = Integer.parseInt(decimalBuffer.toString());

                        if (!((decimal >= 0) && (decimal <= 255))) {
                            showInvalidInputError(position + i);
                            error = true;
                        } else {
                            if (i == (str.length() - 1)) {
                                octetCounter++;
                            }
                        }
                    } else if (decimalBuffer.length() > 3) {
                        showInvalidInputError(position + i);
                        error = true;
                    }
                } else {
                    showInvalidInputError(position + i);
                    error = true;
                }
            }

            if (error) {
                isValidQuartet = false;

                break;
            }
        }

        if (isValidQuartet) {
            if (octetCounter != 4) {
                showInvalidInputError(position + str.length());
                isValidQuartet = false;
            }
        }

        return isValidQuartet;
    }

    /***************************************************************************
     * METHODS THAT SHOW THE ERROR
     **************************************************************************/
    protected void showAmbiguityError(String command) {
        command = "\"" + command + "\"";
        textArea.append("\n" + AMBIGUOUS_COMMAND_ERROR + command);
    }

    protected void showInvalidInputError(int column) {
        textArea.append("\n");

        for (int i = 0; i < currentPrompt.length(); i++) {
            textArea.append(" ");
        }

        for (int i = 0; i < column; i++) {
            textArea.append(" ");
        }

        textArea.append("^");
        textArea.append("\n" + COMMAND_DOES_NOT_EXIST_ERROR);
    }

    protected void showIncompleteCommandError() {
        textArea.append("\n" + INCOMPLETE_COMMAND_ERROR);
    }

    /***************************************************************************
     * GETTERS / SETTERS
     **************************************************************************/
    public Command[] getAvailableCommands() {
        return availableCommands;
    }

    public void setAvailableCommands(Command[] availableCommands) {
        this.availableCommands = availableCommands;
    }

    public Stack getBackInputs() {
        return backInputs;
    }

    public int getCurrentCaretPosition() {
        return currentCaretPosition;
    }

    public void setCurrentCaretPosition(int currentCaretPosition) {
        this.currentCaretPosition = currentCaretPosition;
    }

    /***************************************************************************
     * TROUBLESHOOTING TOOLS
     ***************************************************************************/
    public boolean deviceReachable(Device device, IPAddress destination) {
        ping(device, destination);
        return reachable;
    }

    public void ping(Device device, IPAddress destination) {
        destinationInterface = null;

        if ((device instanceof Host) || (device instanceof Server) || (device instanceof Switch))  {
            Interface deviceInterface = (device.getInterfaces()[0]);

            if (deviceInterface.getState().equals(Interface.UP)) {
                IPAddress address = deviceInterface.getIPAddress();

                if (destination.toString().equals(IPAddress.LOOPBACK) || destination.toString().equals(IPAddress.BROADCAST) || destination.toString().equals(address.toString())) {
                    reachable = true;
                    destinationInterface = deviceInterface;
                } else if (inLAN(deviceInterface, destination)) {
                    reachable = true;
                } else if (inLAN(deviceInterface, deviceInterface.getDefaultGateway())) {
                    Interface gateway = getGateway(deviceInterface, deviceInterface.getDefaultGateway());

                    if (gateway.getDevice() instanceof Router) {
                        forward(deviceInterface, gateway, destination);
                    } else {
                        reachable = false;
                    }
                } else {
                    reachable = false;
                }
            } else {
                reachable = false;
            }
        }
    }
        
    public boolean deviceTraceable(Device device, IPAddress destination) {
        routeOfTrace="";
        prevRoutePoint="";
        hops=0;
    	tracer(device, destination);
        return traceable;
    }

    public void tracer(Device device, IPAddress destination) {
        destinationInterface = null;

        if ((device instanceof Host) || (device instanceof Server) || (device instanceof Switch)) {
            Interface deviceInterface = (device.getInterfaces()[0]);

            if (deviceInterface.getState().equals(Interface.UP)) {
                IPAddress address = deviceInterface.getIPAddress();

                if (destination.toString().equals(IPAddress.LOOPBACK) || destination.toString().equals(IPAddress.BROADCAST) || destination.toString().equals(address.toString())) {
             	    routeUpdater(destination, device);
                	reachable = true;
                    destinationInterface = deviceInterface;
                } else if (inLAN(deviceInterface, destination)) {
                    reachable = true;
                } else if (inLAN(deviceInterface, deviceInterface.getDefaultGateway())) {
                    Interface gateway = getGateway(deviceInterface, deviceInterface.getDefaultGateway());

                    if (gateway.getDevice() instanceof Router) {
                        forward(deviceInterface, gateway, destination);
                    } else {
                        reachable = false;
                    }
                } else {
                    reachable = false;
                }
            } else {
                reachable = false;
            }
        }
        traceable=reachable;
    }
    
    public void routeUpdater(IPAddress routePoint, Device routeDevice){
    	String add=routePoint.toString();
    	if (!add.equals("0.0.0.0") && !add.equals(prevRoutePoint)){
    		hops++;
    		routeOfTrace= routeOfTrace + "\n" + hops + "	<1 ms    <1 ms    <1 ms	  " + routePoint;    		
    		prevRoutePoint = add;
    	}    	
    }
       
    public boolean portReachable(Device device, IPAddress destination, int portNumber){
    	ping(device, destination, portNumber);
    	return reachable;
    }

    public void ping(Device device, IPAddress destination, int portNumber) {
        destinationInterface = null;

        if ((device instanceof Host) || (device instanceof Server) || (device instanceof Switch)) {
            Interface deviceInterface = (device.getInterfaces()[0]);

            if (deviceInterface.getState().equals(Interface.UP)) {
                IPAddress address = deviceInterface.getIPAddress();

                if (destination.toString().equals(IPAddress.LOOPBACK) || destination.toString().equals(IPAddress.BROADCAST) || destination.toString().equals(address.toString())) {
                    reachable = true;
                    destinationInterface = deviceInterface;
                } else if (inLAN(deviceInterface, destination)) {
                    reachable = true;
                } else if (inLAN(deviceInterface, deviceInterface.getDefaultGateway())) {
                    Interface gateway = getGateway(deviceInterface, deviceInterface.getDefaultGateway());

                    if (gateway.getDevice() instanceof Router) {
                        forward(deviceInterface, gateway, destination, portNumber);
                    } else {
                        reachable = false;
                    }
                } else {
                    reachable = false;
                }
            } else {
                reachable = false;
            }
        }
    }    

    public void forward(Interface sourceInterface, Interface entryInterface, IPAddress destination) {
        reachable = false;

        Device currentDevice = entryInterface.getDevice();

        if (currentDevice instanceof Router) {
            boolean permitted = true;
            IPAccessList accessList = entryInterface.getIPAccessListIn();

            if (accessList != null) {
                permitted = false;

                IPPermission[] permissions = accessList.getIPPermissions();

                if (accessList instanceof StandardIPAccessList) {
                    boolean stop = false;

                    for (int i = 0; i < permissions.length; i++) {
                        StandardIPPermission standardIPPermission = (StandardIPPermission) permissions[i];
                        IPAddress address = standardIPPermission.getAddress();
                        Wildcard wildcard = standardIPPermission.getWildcard();

                        if (IPAccessList.getMaskedAddress(address, wildcard).equals(IPAccessList.getMaskedAddress(sourceInterface.getIPAddress(), wildcard))) {
                            if (standardIPPermission.getPermission() == IPPermission.PERMIT) {
                                permitted = true;
                                stop = true;
                            } else if (standardIPPermission.getPermission() == IPPermission.DENY) {
                                permitted = false;
                                stop = true;
                            }
                        }

                        if (stop) {
                            break;
                        }
                    }
                } else if (accessList instanceof ExtendedIPAccessList) {
                    boolean stop = false;

                    for (int i = 0; i < permissions.length; i++) {
                        ExtendedIPPermission extendedIPPermission = (ExtendedIPPermission) permissions[i];
                        IPAddress permissionSource = extendedIPPermission.getSource();
                        Wildcard sourceWildcard = extendedIPPermission.getSourceWildcard();
                        IPAddress permissionDestination = extendedIPPermission.getDestination();
                        Wildcard destinationWildcard = extendedIPPermission.getDestinationWildcard();

                        if (IPAccessList.getMaskedAddress(permissionSource, sourceWildcard).equals(IPAccessList.getMaskedAddress(sourceInterface.getIPAddress(), sourceWildcard))) {
                            if (IPAccessList.getMaskedAddress(permissionDestination, destinationWildcard).equals(IPAccessList.getMaskedAddress(destination, destinationWildcard))) {
                            	/** TODO: */
                                if (extendedIPPermission.getPermission() == IPPermission.PERMIT) {
                                    permitted = true;
                                    stop = true;
                                } else if (extendedIPPermission.getPermission() == IPPermission.DENY) {
                                    permitted = false;
                                    stop = true;
                                }
                            }
                        }

                        if (stop) {
                            break;
                        }
                    }
                }
            }

            if (permitted) {
            	//System.out.println(destination.toString() + " " + sourceInterface.toString() + " " + entryInterface.getSubnetMask().toString());
                String destinationNetworkAddress = IPAddress.getNetworkAddress(destination, sourceInterface.getSubnetMask());
                Router router = (Router) currentDevice;
                RoutingTable table = router.getRoutingTable();
                Entry[] entries = table.getEntries();
                

                for (int i = 0; i < entries.length; i++) {
                    IPAddress network = entries[i].getDestinationNetwork();
                    SubnetMask mask = entries[i].getMask();
                    String entryDestinationNetwork = IPAddress.getNetworkAddress(network, mask);
                    IPAddress nextHopAddress = entries[i].getNextHopAddress();
                    Interface routerInterface = entries[i].getRouterInterface();

                    if (routerInterface == null) {
                        Interface[] interfaces = router.getClosedInterfaces();

                        for (int j = 0; j < interfaces.length; j++) {
                            IPAddress interfaceAddress = interfaces[j].getIPAddress();
                            SubnetMask interfaceMask = interfaces[j].getSubnetMask();

                            if (IPAddress.getNetworkAddress(interfaceAddress, interfaceMask).equals(IPAddress.getNetworkAddress(nextHopAddress, interfaceMask))) {
                                routerInterface = interfaces[j];
                                entries[i].setRouterInterface(routerInterface);
                            }
                        }
                    }

                    if ((routerInterface != null) && routerInterface.getState().equals(Interface.UP)) {
                        accessList = routerInterface.getIPAccessListOut();

                        if (accessList != null) {
                            permitted = false;

                            IPPermission[] permissions = accessList.getIPPermissions();

                            if (accessList instanceof StandardIPAccessList) {
                                boolean stop = false;

                                for (int j = 0; j < permissions.length; j++) {
                                    StandardIPPermission standardIPPermission = (StandardIPPermission) permissions[j];
                                    IPAddress address = standardIPPermission.getAddress();
                                    Wildcard wildcard = standardIPPermission.getWildcard();

                                    if (IPAccessList.getMaskedAddress(address, wildcard).equals(IPAccessList.getMaskedAddress(sourceInterface.getIPAddress(), wildcard))) {
                                        if (standardIPPermission.getPermission() == IPPermission.PERMIT) {
                                            permitted = true;
                                            stop = true;
                                        } else if (standardIPPermission.getPermission() == IPPermission.DENY) {
                                            permitted = false;
                                            stop = true;
                                        }
                                    }

                                    if (stop) {
                                        break;
                                    }
                                }
                            }
                        }

                        if (permitted) {
                        	//System.out.println("swabe!! " + (String)entryDestinationNetwork + " " + (String)destinationNetworkAddress );
                            
                        	if ((entryDestinationNetwork.equals(destinationNetworkAddress) && !nextHopAddress.toString().equals("0.0.0.0")) || (network.toString().equals("0.0.0.0") && mask.toString().equals("0.0.0.0"))) {
                                //System.out.println("Swabe Mucho!!");
                            	if (inLAN(routerInterface, nextHopAddress)) {
                                    Interface gateway = getGateway(routerInterface, nextHopAddress);

                                    if (gateway.getDevice() instanceof Router) {
                                        forward(sourceInterface, gateway, destination);
                                    }
                                }
                            } else if (entryDestinationNetwork.equals(destinationNetworkAddress) && nextHopAddress.toString().equals("0.0.0.0")) {
                                if (inLAN(routerInterface, destination)) {
                                  	reachable = true;
                                }
                            }
                        	
                        	if (reachable == false){
                        		String destinationNetworkAddress2 = IPAddress.getNetworkAddress(destination, mask);
                            	//System.out.println("swabe!! " + (String)entryDestinationNetwork + " " + (String)destinationNetworkAddress2 );
                               	if ((entryDestinationNetwork.equals(destinationNetworkAddress2) && !nextHopAddress.toString().equals("0.0.0.0")) || (network.toString().equals("0.0.0.0") && mask.toString().equals("0.0.0.0"))) {
                                  //  System.out.println("Swabe Mucho!!");
                                	if (inLAN(routerInterface, nextHopAddress)) {
                                        Interface gateway = getGateway(routerInterface, nextHopAddress);

                                        if (gateway.getDevice() instanceof Router) {
                                            forward(sourceInterface, gateway, destination);
                                        }
                                    }
                                } else if (entryDestinationNetwork.equals(destinationNetworkAddress2) && nextHopAddress.toString().equals("0.0.0.0")) {
                                    if (inLAN(routerInterface, destination)) {
                                      	reachable = true;
                                    }
                                }
                        		
                        	}
                        	
                        }
                    }
                }
            }
        }
    }
    
    public void forward(Interface sourceInterface, Interface entryInterface, IPAddress destination, int portNumber) {
        reachable = false;

        Device currentDevice = entryInterface.getDevice();

        if (currentDevice instanceof Router) {
            boolean permitted = true;
            IPAccessList accessList = entryInterface.getIPAccessListIn();

            if (accessList != null) {
                permitted = false;

                IPPermission[] permissions = accessList.getIPPermissions();

                if (accessList instanceof StandardIPAccessList) {
                    boolean stop = false;

                    for (int i = 0; i < permissions.length; i++) {
                        StandardIPPermission standardIPPermission = (StandardIPPermission) permissions[i];
                        IPAddress address = standardIPPermission.getAddress();
                        Wildcard wildcard = standardIPPermission.getWildcard();

                        if (IPAccessList.getMaskedAddress(address, wildcard).equals(IPAccessList.getMaskedAddress(sourceInterface.getIPAddress(), wildcard))) {
                            if (standardIPPermission.getPermission() == IPPermission.PERMIT) {
                                permitted = true;
                                stop = true;
                            } else if (standardIPPermission.getPermission() == IPPermission.DENY) {
                                permitted = false;
                                stop = true;
                            }
                        }

                        if (stop) {
                            break;
                        }
                    }
                } else if (accessList instanceof ExtendedIPAccessList) {
                    boolean stop = false;

                    for (int i = 0; i < permissions.length; i++) {
                        ExtendedIPPermission extendedIPPermission = (ExtendedIPPermission) permissions[i];
                        IPAddress permissionSource = extendedIPPermission.getSource();
                        Wildcard sourceWildcard = extendedIPPermission.getSourceWildcard();
                        IPAddress permissionDestination = extendedIPPermission.getDestination();
                        Wildcard destinationWildcard = extendedIPPermission.getDestinationWildcard();

                        if (IPAccessList.getMaskedAddress(permissionSource, sourceWildcard).equals(IPAccessList.getMaskedAddress(sourceInterface.getIPAddress(), sourceWildcard))) {
                            if (IPAccessList.getMaskedAddress(permissionDestination, destinationWildcard).equals(IPAccessList.getMaskedAddress(destination, destinationWildcard))) {
                            	/** TODO: */
                                if (extendedIPPermission.getPermission() == IPPermission.PERMIT) {
                                    permitted = true;
                                    stop = true;
                                } else if (extendedIPPermission.getPermission() == IPPermission.DENY) {
                                    permitted = false;
                                    stop = true;
                                }
                            }
                        }

                        if (stop) {
                            break;
                        }
                    }
                }
            }

            if (permitted) {
                String destinationNetworkAddress = IPAddress.getNetworkAddress(destination, sourceInterface.getSubnetMask());
                Router router = (Router) currentDevice;
                RoutingTable table = router.getRoutingTable();
                Entry[] entries = table.getEntries();

                for (int i = 0; i < entries.length; i++) {
                    IPAddress network = entries[i].getDestinationNetwork();
                    SubnetMask mask = entries[i].getMask();
                    String entryDestinationNetwork = IPAddress.getNetworkAddress(network, mask);
                    IPAddress nextHopAddress = entries[i].getNextHopAddress();
                    Interface routerInterface = entries[i].getRouterInterface();

                    if (routerInterface == null) {
                        Interface[] interfaces = router.getClosedInterfaces();

                        for (int j = 0; j < interfaces.length; j++) {
                            IPAddress interfaceAddress = interfaces[j].getIPAddress();
                            SubnetMask interfaceMask = interfaces[j].getSubnetMask();

                            if (IPAddress.getNetworkAddress(interfaceAddress, interfaceMask).equals(IPAddress.getNetworkAddress(nextHopAddress, interfaceMask))) {
                                routerInterface = interfaces[j];
                                entries[i].setRouterInterface(routerInterface);
                            }
                        }
                    }

                    if ((routerInterface != null) && routerInterface.getState().equals(Interface.UP)) {
                        accessList = routerInterface.getIPAccessListOut();

                        if (accessList != null) {
                            permitted = false;

                            IPPermission[] permissions = accessList.getIPPermissions();

                            if (accessList instanceof StandardIPAccessList) {
                                boolean stop = false;

                                for (int j = 0; j < permissions.length; j++) {
                                    StandardIPPermission standardIPPermission = (StandardIPPermission) permissions[j];
                                    IPAddress address = standardIPPermission.getAddress();
                                    Wildcard wildcard = standardIPPermission.getWildcard();

                                    if (IPAccessList.getMaskedAddress(address, wildcard).equals(IPAccessList.getMaskedAddress(sourceInterface.getIPAddress(), wildcard))) {
                                        if (standardIPPermission.getPermission() == IPPermission.PERMIT) {
                                            permitted = true;
                                            stop = true;
                                        } else if (standardIPPermission.getPermission() == IPPermission.DENY) {
                                            permitted = false;
                                            stop = true;
                                        }
                                    }

                                    if (stop) {
                                        break;
                                    }
                                }
                            }
                        }

                        if (permitted) {
                            if ((entryDestinationNetwork.equals(destinationNetworkAddress) && !nextHopAddress.toString().equals("0.0.0.0")) || (network.toString().equals("0.0.0.0") && mask.toString().equals("0.0.0.0"))) {
                                if (inLAN(routerInterface, nextHopAddress)) {
                                    Interface gateway = getGateway(routerInterface, nextHopAddress);

                                    if (gateway.getDevice() instanceof Router) {
                                        forward(sourceInterface, gateway, destination);
                                    }
                                }
                            } else if (entryDestinationNetwork.equals(destinationNetworkAddress) && nextHopAddress.toString().equals("0.0.0.0")) {
                                if (inLAN(routerInterface, destination)) {
                                    reachable = true;
                                }
                            }
                        }
                        
                    	if (reachable == false){
                    		String destinationNetworkAddress2 = IPAddress.getNetworkAddress(destination, mask);
                        	//System.out.println("swabe 'to!! " + (String)entryDestinationNetwork + " " + (String)destinationNetworkAddress2 );
                           	if ((entryDestinationNetwork.equals(destinationNetworkAddress2) && !nextHopAddress.toString().equals("0.0.0.0")) || (network.toString().equals("0.0.0.0") && mask.toString().equals("0.0.0.0"))) {
                              //System.out.println("Swabe Mucho 'to!!");
                            	if (inLAN(routerInterface, nextHopAddress)) {
                                    Interface gateway = getGateway(routerInterface, nextHopAddress);

                                    if (gateway.getDevice() instanceof Router) {
                                        forward(sourceInterface, gateway, destination);
                                    }
                                }
                            } else if (entryDestinationNetwork.equals(destinationNetworkAddress2) && nextHopAddress.toString().equals("0.0.0.0")) {
                                if (inLAN(routerInterface, destination)) {
                                  	reachable = true;
                                }
                            }
                    		
                    	}
                    }
                }
            }
        }
    }
/*REL*/    
public boolean Tracert(){
    	//textArea.append("works for me!");
    	return true;
    }
/*-REL*/
    public boolean inLAN(Interface deviceInterface, IPAddress destination) {
        boolean inLAN = false;
        String deviceNetworkAddress = IPAddress.getNetworkAddress(deviceInterface.getIPAddress(), deviceInterface.getSubnetMask());
        Interface connectedInterface = deviceInterface.getConnectedInterface();

        if (deviceInterface.getIPAddress().toString().equals(destination.toString())) {
      	    routeUpdater(deviceInterface.getIPAddress(), deviceInterface.getConnectedInterface().getDevice());
        	inLAN = true;
            destinationInterface = deviceInterface;
        } else if (connectedInterface != null) {
            Device connectedDevice = connectedInterface.getDevice();

            if ((connectedDevice instanceof Host) || (connectedDevice instanceof Server)) {
                if (connectedInterface.getState().equals(Interface.UP)) {
                    IPAddress connectedAddress = connectedInterface.getIPAddress();

                    if (IPAddress.getNetworkAddress(connectedAddress, deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                        if (connectedAddress.toString().equals(destination.toString())) {
                     	    routeUpdater(connectedAddress, deviceInterface.getConnectedInterface().getDevice());
                        	inLAN = true;
                            destinationInterface = connectedInterface;
                        }
                    }
                }
            } else if (connectedDevice instanceof Switch) {
                Interface[] interfaces = connectedDevice.getClosedInterfaces();
                VLAN vlan = connectedInterface.getSwitchPort().getVLAN();

                for (int i = 0; i < interfaces.length; i++) {
                    if ((interfaces[i].getSwitchPort().getVLAN().getIndex() == vlan.getIndex()) && !interfaces[i].equals(connectedInterface)) {
                        Interface otherInterface = interfaces[i].getConnectedInterface();

                        if (otherInterface.getState().equals(Interface.UP)) {
                            if (IPAddress.getNetworkAddress(otherInterface.getIPAddress(), deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                                if (otherInterface.getIPAddress().toString().equals(destination.toString())) {
                             	    routeUpdater(otherInterface.getIPAddress(), otherInterface.getDevice());
                                	inLAN = true;
                                    destinationInterface = otherInterface;
                                }
                            } else if (otherInterface.getDevice() instanceof Switch) {
                                Interface[] switchInterfaces = otherInterface.getDevice().getClosedInterfaces();
                               
                                /*REL*/
                               for (int o=0; o<switchInterfaces.length; o++){
                                   if (inLAN(switchInterfaces[o], destination) && !switchInterfaces[o].equals(otherInterface)) {
                                	   inLAN = true;

                                       break;
                                   }
                               }
                                /*-REL*/
                                
                                if (interfaces[i] instanceof FastEthernet && otherInterface instanceof FastEthernet) {
                                    if (((FastEthernet) interfaces[i]).isTrunked() && ((FastEthernet) otherInterface).isTrunked()) {
                                        for (int j = 0;
                                                j < switchInterfaces.length;
                                                j++) {
                                            if (inLAN(switchInterfaces[j], destination) && !switchInterfaces[j].equals(otherInterface)) {
                                                inLAN = true;

                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (connectedDevice instanceof Router) {
                if (connectedInterface.getState().equals(Interface.UP)) {
                    IPAddress connectedAddress = connectedInterface.getIPAddress();

                    if (IPAddress.getNetworkAddress(connectedAddress, deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                        if (connectedAddress.toString().equals(destination.toString())) {
                            routeUpdater(connectedAddress, connectedDevice);
                        	inLAN = true;
                            destinationInterface = connectedInterface;
                        }
                    }
                }
            }
        }

        return inLAN;
    }
    
    public Device inLANGetDevice(Interface deviceInterface, IPAddress destination) {
        Device inLAN = null;
        Device deviceTemp = null;
        String deviceNetworkAddress = IPAddress.getNetworkAddress(deviceInterface.getIPAddress(), deviceInterface.getSubnetMask());
        Interface connectedInterface = deviceInterface.getConnectedInterface();

            
        // if interface of host is connected to another interface  
        if (connectedInterface != null) {
            Device connectedDevice = connectedInterface.getDevice();

            //if connected to a Host or a Server
            if ((connectedDevice instanceof Host) || (connectedDevice instanceof Server)) {
                if (connectedInterface.getState().equals(Interface.UP)) {
                    IPAddress connectedAddress = connectedInterface.getIPAddress();

                    if (IPAddress.getNetworkAddress(connectedAddress, deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                        if (connectedAddress.toString().equals(destination.toString())) {
                     	    routeUpdater(connectedAddress, deviceInterface.getConnectedInterface().getDevice());
                        	inLAN = connectedDevice;
                            destinationInterface = connectedInterface;
                        }
                    }
                }
            //if connected to a Switch
            } else if (connectedDevice instanceof Switch) {
                Interface[] interfaces = connectedDevice.getClosedInterfaces();
                VLAN vlan = connectedInterface.getSwitchPort().getVLAN();

                for (int i = 0; i < interfaces.length; i++) {
                    if ((interfaces[i].getSwitchPort().getVLAN().getIndex() == vlan.getIndex()) && !interfaces[i].equals(connectedInterface)) {
                        Interface otherInterface = interfaces[i].getConnectedInterface();

                        if (otherInterface.getState().equals(Interface.UP)) {
                        	
                            if (IPAddress.getNetworkAddress(otherInterface.getIPAddress(), deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                                if (otherInterface.getIPAddress().toString().equals(destination.toString())) {
                             	    routeUpdater(otherInterface.getIPAddress(), otherInterface.getDevice());
                                	inLAN = otherInterface.getDevice();
                                    destinationInterface = otherInterface;
                                }
                            } 
                            else if (otherInterface.getDevice() instanceof Switch) {
                            	Interface[] switchInterfaces = otherInterface.getDevice().getClosedInterfaces();
                            	for (int o=0; o<switchInterfaces.length; o++){
                            		deviceTemp = inLANGetDevice(switchInterfaces[o], destination);
                                   if ( (deviceTemp != null) && !switchInterfaces[o].equals(otherInterface)) {
                                	   inLAN = deviceTemp;
                                       break;
                                   }
                            	}
                                if (interfaces[i] instanceof FastEthernet && otherInterface instanceof FastEthernet) {
                                    if (((FastEthernet) interfaces[i]).isTrunked() && ((FastEthernet) otherInterface).isTrunked()) {
                                        for (int j = 0;
                                                j < switchInterfaces.length;
                                                j++) {
                                        	deviceTemp = inLANGetDevice(switchInterfaces[j], destination);
                                            if ( !(deviceTemp.equals(null)) && !switchInterfaces[j].equals(otherInterface)) {
                                                inLAN = otherInterface.getDevice();
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            //if connected to a Router
            } else if (connectedDevice instanceof Router) {
                if (connectedInterface.getState().equals(Interface.UP)) {
                    IPAddress connectedAddress = connectedInterface.getIPAddress();

                    if (IPAddress.getNetworkAddress(connectedAddress, deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                        if (connectedAddress.toString().equals(destination.toString())) {
                            routeUpdater(connectedAddress, connectedDevice);
                        	inLAN = connectedInterface.getDevice();
                            destinationInterface = connectedInterface;
                        }
                    }
                }
            }
        }

        return inLAN;
    }

    public boolean isDHCPServer(Server s){
    	if (s.getDHCPStatus()){
    		return true;
    	}else return false;
    }
    	

    public Server findDHCPServer(Interface deviceInterface) {
    	Server serverTemp = null;
        Device deviceTemp = null;
        Device connectedDevice;
        Interface connectedInterface = deviceInterface.getConnectedInterface();
            
        
        // if interface of host is connected to another interface  
        if (connectedInterface != null) {
            connectedDevice = connectedInterface.getDevice();

            //if connected to a Server, Hosts don't matter, we are only looking for devices with DHCP
            if ((connectedDevice instanceof Server)) {
                if (connectedInterface.getState().equals(Interface.UP)) {
                	serverTemp = (Server) connectedDevice;
                	if (isDHCPServer(serverTemp)){
                		return serverTemp;
                	}
                }
            //if connected to a Switch
            } else if (connectedDevice instanceof Switch) {
                Interface[] interfaces = connectedDevice.getClosedInterfaces();
                VLAN vlan = connectedInterface.getSwitchPort().getVLAN();

                for (int i = 0; i < interfaces.length; i++) {
                    if ((interfaces[i].getSwitchPort().getVLAN().getIndex() == vlan.getIndex()) && !interfaces[i].equals(connectedInterface)) {
                        Interface otherInterface = interfaces[i].getConnectedInterface();

                        if (otherInterface.getState().equals(Interface.UP)) {
                        	if ((otherInterface.getDevice() instanceof Server)) {
                               serverTemp = (Server) otherInterface.getDevice();
                               if (isDHCPServer(serverTemp)){
                            	   return serverTemp;
                               }
                               
                        	}
                            else if (otherInterface.getDevice() instanceof Switch) {
                            	Interface[] switchInterfaces = otherInterface.getDevice().getClosedInterfaces();
                            	for (int o=0; o<switchInterfaces.length; o++){
                            		serverTemp = findDHCPServer(switchInterfaces[o]);
                                   if ( (serverTemp != null) && !switchInterfaces[o].equals(otherInterface)) {
                                	   return serverTemp;
                                   }
                            	}
                                if (interfaces[i] instanceof FastEthernet && otherInterface instanceof FastEthernet) {
                                    if (((FastEthernet) interfaces[i]).isTrunked() && ((FastEthernet) otherInterface).isTrunked()) {
                                        for (int j = 0;
                                                j < switchInterfaces.length;
                                                j++) {
                                        	deviceTemp = findDHCPServer(switchInterfaces[j]);
                                            if ( !(deviceTemp.equals(null)) && !switchInterfaces[j].equals(otherInterface)) {
                                                deviceTemp = otherInterface.getDevice();
                                                return serverTemp;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } 
    	}
        return serverTemp;
    }

        

    public Interface getGateway(Interface deviceInterface, IPAddress destination) {
        Interface gateway = null;
        String deviceNetworkAddress = IPAddress.getNetworkAddress(deviceInterface.getIPAddress(), deviceInterface.getSubnetMask());
        Interface connectedInterface = deviceInterface.getConnectedInterface();

        if (deviceInterface.getIPAddress().toString().equals(destination.toString())) {
            gateway = deviceInterface;
            destinationInterface = deviceInterface;
        } else if (connectedInterface != null) {
            Device connectedDevice = connectedInterface.getDevice();

            if ((connectedDevice instanceof Host) || (connectedDevice instanceof Server)) {
                if (connectedInterface.getState().equals(Interface.UP)) {
                    IPAddress connectedAddress = connectedInterface.getIPAddress();

                    if (IPAddress.getNetworkAddress(connectedAddress, deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                        if (connectedAddress.toString().equals(destination.toString())) {
                            gateway = connectedInterface;
                            destinationInterface = connectedInterface;
                        }
                    }
                }
            } else if (connectedDevice instanceof Switch) {
                Interface[] interfaces = connectedDevice.getClosedInterfaces();
                VLAN vlan = connectedInterface.getSwitchPort().getVLAN();

                for (int i = 0; i < interfaces.length; i++) {
                    if ((interfaces[i].getSwitchPort().getVLAN().getIndex() == vlan.getIndex()) && !interfaces[i].equals(connectedInterface)) {
                        Interface otherInterface = interfaces[i].getConnectedInterface();

                        if (otherInterface.getState().equals(Interface.UP)) {
                            if (IPAddress.getNetworkAddress(otherInterface.getIPAddress(), deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                                if (otherInterface.getIPAddress().toString().equals(destination.toString())) {
                                    gateway = otherInterface;
                                    destinationInterface = otherInterface;
                                }
                            } else if (otherInterface.getDevice() instanceof Switch) {
                                Interface[] switchInterfaces = otherInterface.getDevice().getClosedInterfaces();

                                for (int j = 0; j < switchInterfaces.length;
                                        j++) {
                                    if (((gateway = getGateway(switchInterfaces[j], destination)) != null) && !switchInterfaces[j].equals(otherInterface)) {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            } else if (connectedDevice instanceof Router) {
                if (connectedInterface.getState().equals(Interface.UP)) {
                    IPAddress connectedAddress = connectedInterface.getIPAddress();

                    if (IPAddress.getNetworkAddress(connectedAddress, deviceInterface.getSubnetMask()).equals(deviceNetworkAddress)) {
                        if (connectedAddress.toString().equals(destination.toString())) {
                            gateway = connectedInterface;
                            destinationInterface = connectedInterface;
                        }
                    }
                }
            }
        }

        return gateway;
    }

    public Interface getDestinationInterface() {
        return destinationInterface;
    }

    public void showReply(IPAddress address) {
        String add = address.toString();
        textArea.append("\nPinging " + add + " with 32 bytes of data:\n");

        for (int i = 0; i < 5; i++) {
            textArea.append("\nReply from " + add + ": bytes=32 time=60ms TTL=241");
        }

        textArea.append("\nPing statistics for " + add + ":\n\tPackets: Sent = 5, Received = 5, Lost = 0 (0% loss),\nApproximate round trip times in milli-seconds:\n\tMinimum = 50ms, Maximum =  60ms, Average =  55ms\n");
    }


    public void showTraceReply(IPAddress address, int marker) {
       // String add = address.toString();
    	if (marker==1)textArea.append("\n" + routeOfTrace + "\n");
    	if (marker==2)textArea.append("\nDestination Host Unreachable.\n");
  }

 
    public void showRequestTimedOut(IPAddress address) {
        String add = address.toString();
        textArea.append("\nPinging " + add + " with 32 bytes of data:\n");

        for (int i = 0; i < 5; i++) {
            textArea.append("\nRequest timed out.");
        }

        textArea.append("\nPing statistics for " + add + ":\n\tPackets: Sent = 5, Received = 0, Lost = 5 (100% loss),\nApproximate round trip times in milli-seconds:\n\tMinimum = 0ms, Maximum =  0ms, Average =  0ms\n");
    }
}

