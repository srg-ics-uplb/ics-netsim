package devices.commands;

import java.io.Serializable;


public class Command implements Serializable {

	private static final long serialVersionUID = -6236371698131393631L;
	private final String name;
    private final String description;
    private Command[] arguments;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String toString() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setArguments(Command[] arguments) {
        this.arguments = arguments;
    }

    public Command[] getArguments() {
        return arguments;
    }

    public String getName() {
        return name;
    }
}
