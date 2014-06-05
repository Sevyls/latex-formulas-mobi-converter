package at.ac.tuwien.ims.latex2mobiformulaconv.app;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.LatexToHtmlConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.PandocLatexToHtmlConverter;
import org.apache.commons.cli.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;

import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * @author mauss
 *         Created: 29.04.14 22:55
 */
public class Main {
    private static final String CONFIGURATION_FILENAME = "configuration.properties";
    private static Logger logger = Logger.getRootLogger();
    private static PropertiesConfiguration config;
    private static Options options;
    private static ArrayList<Path> inputPaths = new ArrayList<Path>();

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

        Option inputOption = new Option("i", "inputPaths", true, "inputPaths file");
        inputOption.setRequired(true);
        inputOption.setArgs(Option.UNLIMITED_VALUES);
        inputOption.setValueSeparator(',');
        options.addOption(inputOption);

        options.addOption("o", "output", true, "output file/directory");

        Option pandocOption = new Option("p", "pandoc-exec", true, "pandoc executable location");
        pandocOption.setArgs(1);
        options.addOption(pandocOption);

        options.addOption("k", "kindlegen-exec", true, "Amazon KindleGen executable location");
        options.addOption("c", "calibre-exec", true, "Calibre executable location");

        options.addOption("h", "help", false, "show this help");

        CommandLineParser parser = new PosixParser();
        try {

            CommandLine cmd = parser.parse(options, args);


            if (cmd.hasOption('h')) {
                // Show usage, ignore other parameters and quit
                usage();
                logger.debug("Help called, main() exit.");
                System.exit(0);
            }

            if (cmd.hasOption('i')) {
                String[] inputValues = cmd.getOptionValues('i');
                for (String inputValue : inputValues) {
                    Path inputPath = Paths.get(inputValue);

                    if (Files.exists(inputPath)) {
                        logger.debug("Input file/directory found: " + inputPath.toAbsolutePath().toString());
                        inputPaths.add(inputPath);
                    } else {
                        logger.error("Input file/directory could not be found!");
                        logger.error(inputPath.toAbsolutePath().toString());
                        System.exit(1);
                    }
                }
            } else {
                logger.error("You have to specify an inputPaths file or directory!");
                usage();
                System.exit(1);

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


        } catch (MissingOptionException m) {
            logger.warn(m.getMessage());
            usage();
            System.exit(1);
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

        // TODO iterate over inputPaths
        latexToHtmlConverter.convert(inputPaths.get(0).toFile());

        logger.debug("main() exit.");
    }

    private static void usage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("latex2mobi", options);
    }
}
