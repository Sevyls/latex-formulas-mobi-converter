package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import org.apache.commons.cli.Option;

/**
 * @author mauss
 *         Created: 06.06.14 22:48
 *         <p/>
 *         Creating an Apache Commons CLI Option for a runtime-depending executable
 */
public interface ExecOption {
    /**
     * Gets the configuration for the CLI Option for the executable
     *
     * @return the configured Option object
     */
    public Option getExecOption();

}
