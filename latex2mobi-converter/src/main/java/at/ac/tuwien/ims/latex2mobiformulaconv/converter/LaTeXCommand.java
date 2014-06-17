package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import java.util.List;

/**
 * @author mauss
 *         Created: 17.06.14 00:37
 */
public abstract class LaTeXCommand {
    public static final char START_COMMAND = '\\';

    protected String command;
    protected List<OptionalArgument> optionalArgumentList;
    protected List<MandatoryArgument> mandatoryArgumentList;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public List<OptionalArgument> getOptionalArgumentList() {
        return optionalArgumentList;
    }

    public void setOptionalArgumentList(List<OptionalArgument> optionalArgumentList) {
        this.optionalArgumentList = optionalArgumentList;
    }

    public List<MandatoryArgument> getMandatoryArgumentList() {
        return mandatoryArgumentList;
    }

    public void setMandatoryArgumentList(List<MandatoryArgument> mandatoryArgumentList) {
        this.mandatoryArgumentList = mandatoryArgumentList;
    }
}
