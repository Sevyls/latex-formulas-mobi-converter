package at.ac.tuwien.ims.latex2mobiformulaconv.app;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

/**
 * @author mauss
 *         Created: 29.04.14 22:55
 */
public class Main {
    private static final String CONFIGURATION_FILENAME = "configuration.properties";
    private static Logger logger = Logger.getRootLogger();
    private static Configuration config;

    public static void main(String[] args) {
        logger.debug("main() started.");

        try {
            logger.debug("Loading configuration.");
            config = new PropertiesConfiguration(CONFIGURATION_FILENAME);
        } catch (ConfigurationException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }

        // TODO Apache Commons CLI - configuration
        // TODO Apache Commons CLI - parsing


        logger.debug("main() exit.");
    }
}
