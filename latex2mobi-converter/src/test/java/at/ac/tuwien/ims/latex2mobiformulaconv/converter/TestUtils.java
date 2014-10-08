package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import org.apache.log4j.Logger;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Michael Auß
 *         Date: 08.10.2014
 *         Time: 21:07
 */
public class TestUtils {
    private static Logger logger = Logger.getLogger(TestUtils.class);

    /**
     * Returns the Working Directory of a given Test class
     *
     * @param clazz The class which will be searched upon
     * @return Java NIO Path Object for the Working Directory
     */
    public static Path getWorkingDirectory(Class clazz) {
        Path workingDirectory = null;

        try {
            workingDirectory = Paths.get(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
            logger.debug("Working directory for Class "
                    + clazz.getCanonicalName() + ": "
                    + workingDirectory.toAbsolutePath().toString());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }

        return workingDirectory;
    }
}
