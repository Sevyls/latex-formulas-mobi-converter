package at.ac.tuwien.ims.latex2mobiformulaconv.app;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.LatexToHtmlConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.PandocLatexToHtmlConverter;
import org.apache.commons.cli.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author mauss
 *         Created: 29.04.14 22:55
 */
public class Main {
    private static final String CONFIGURATION_FILENAME = "configuration.properties";
    private static Logger logger = Logger.getRootLogger();
    private static PropertiesConfiguration config;
    private static Options options;

    public static void main(String[] args) {
        logger.debug("main() started.");
        Path workingDirectory = null;
        try {
            workingDirectory = Paths.get(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (workingDirectory.toFile().isFile()) {
                workingDirectory = workingDirectory.getParent();
            }
            logger.debug("Working Directory: " + workingDirectory);
        } catch (URISyntaxException e) {
            logger.error("Working directory could not be resolved!");
            logger.error(e.getMessage(), e);
            logger.error("Exiting...");
            System.exit(1);
        }

        options = new Options();

        options.addOption("i", "input", true, "input file");
        options.addOption("o", "output", true, "output file/directory");
        options.addOption("p", "pandoc-exec", true, "pandoc executable location");
        options.addOption("k", "kindlegen-exec", true, "Amazon KindleGen executable location");
        options.addOption("c", "calibre-exec", true, "Calibre executable location");
        options.addOption("h", "help", false, "show this help");

        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            if (cmd.hasOption('h')) {
                // Show usage, ignore other parameters and quit
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("latex2mobi", options);
                logger.debug("Help called, main() exit.");
                System.exit(0);
            }

            if (cmd.hasOption('i')) {
                // TODO Input File handling
            }

            if (cmd.hasOption('o')) {
                // TODO Output File/directory handling
            }

            if (cmd.hasOption('p')) {
                // TODO Pandoc executable handling
            }

            if (cmd.hasOption('k')) {
                // TODO KindleGen executable handling
            }

            if (cmd.hasOption('c')) {
                // TODO Calibre executable handling
            }


        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }

        Path defaultConfigPath = workingDirectory.resolve(CONFIGURATION_FILENAME);
        logger.debug("Searching for config in default filepath: " + defaultConfigPath.toAbsolutePath().toString());

        try {
            logger.debug("Loading configuration.");

            config = new PropertiesConfiguration(defaultConfigPath.toFile());

        } catch (ConfigurationException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }

        if (!(defaultConfigPath.toFile().exists() && defaultConfigPath.toFile().isFile())) {
            try {
                logger.debug("Creating empty config file in working directory...");
                config.save();
                logger.debug("Saved empty configuration: " + defaultConfigPath.toAbsolutePath().toString());
            } catch (ConfigurationException e) {
                logger.error(e.getMessage(), e);
                // TODO Exception handling
            }
        }


        LatexToHtmlConverter latexToHtmlConverter = new PandocLatexToHtmlConverter();

        // TODO input file --> convert call
        latexToHtmlConverter.convert(null);

        logger.debug("main() exit.");
    }
}
