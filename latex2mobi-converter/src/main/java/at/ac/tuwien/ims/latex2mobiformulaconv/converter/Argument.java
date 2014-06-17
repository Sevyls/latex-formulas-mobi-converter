package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

/**
 * @author mauss
 *         Created: 17.06.14 00:42
 */
public abstract class Argument {
    private String argument;

    public Argument(String argument) {
        this.argument = argument;
    }

    public String getArgument() {
        return argument;
    }
}
