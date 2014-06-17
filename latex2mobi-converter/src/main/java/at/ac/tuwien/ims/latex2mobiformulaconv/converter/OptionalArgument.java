package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

/**
 * @author mauss
 *         Created: 17.06.14 00:41
 */
public class OptionalArgument extends Argument {
    public static final char START_DELIMITER = '[';
    public static final char END_DELIMITER = ']';

    public OptionalArgument(String argument) {
        super(argument);
    }


}
