package at.ac.tuwien.ims.latex2mobiformulaconv.app;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi.AmazonHtmlToMobiConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi.HtmlToMobiConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.latex.LatexToHtmlConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.latex.PandocLatexToHtmlConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.FormulaConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.ImageFormulaConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.SAXFormulaConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Formula;
import org.apache.commons.cli.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    private static Path outputPath;
    private static boolean replaceWithPictures = false;

    private static HtmlToMobiConverter htmlToMobiConverter;
    private static LatexToHtmlConverter latexToHtmlConverter;

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

        // Instantiate classes
        latexToHtmlConverter = new PandocLatexToHtmlConverter();
        htmlToMobiConverter = new AmazonHtmlToMobiConverter();

        initializeOptions();


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
                String outputDirectory = cmd.getOptionValue('o');

                outputPath = workingDirectory.resolve(outputDirectory);
                if (!Files.isDirectory(outputPath) && Files.isRegularFile(outputPath)) {
                    logger.error("Given output directory is a file! Exiting...");
                    logger.debug(outputPath.toAbsolutePath().toString());
                    System.exit(1);
                }

                if (!Files.isWritable(outputPath)) {
                    logger.error("Given output directory is not writable! Exiting...");
                    logger.debug(outputPath.toAbsolutePath().toString());
                    System.exit(1);
                }
                logger.debug("Output path: " + outputPath.toAbsolutePath().toString());

            } else {
                // Set default output directory if none is given
                outputPath = workingDirectory;
            }

            // If set, replace LaTeX Formulas with PNG images, created by
            if (cmd.hasOption("r")) {
                logger.debug("Picture Flag set");
                replaceWithPictures = true;
            }

            // Executable configuration
            if (cmd.hasOption(latexToHtmlConverter.getExecOption().getOpt())) {
                // TODO Pandoc executable handling
            }

            if (cmd.hasOption(htmlToMobiConverter.getExecOption().getOpt())) {
                // TODO KindleGen executable handling
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


        // TODO iterate over inputPaths
        org.jdom2.Document document = latexToHtmlConverter.convert(inputPaths.get(0).toFile());


        XMLOutputter xout = new XMLOutputter();
        logger.debug(xout.outputString(document));

        logger.info("Parsing Formulas from converted HTML...");
        Path tempDirPath = null;
        try {
            tempDirPath = Files.createTempDirectory("latex2mobi");
            logger.debug("Temporary directory created at: " + tempDirPath.toAbsolutePath().toString());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }

        FormulaConverter formulaConverter;
        Map<Integer, Formula> formulaMap = new HashMap<>();

        if (replaceWithPictures) {
            formulaConverter = new ImageFormulaConverter(tempDirPath);
        } else {
            formulaConverter = new SAXFormulaConverter();
        }

        Map<Integer, String> latexFormulas = formulaConverter.extractFormulas(document);
        Iterator<Integer> it = latexFormulas.keySet().iterator();
        while (it.hasNext()) {
            Integer id = it.next();
            String latexFormula = latexFormulas.get(id);

            Formula formula = formulaConverter.parse(id, latexFormula);

            if (formula != null) {
                formulaMap.put(id, formula);
            }
        }
        document = formulaConverter.replaceFormulas(document, formulaMap);

        File mobiFile = htmlToMobiConverter.convertToMobi(document, tempDirPath);

        Path resultFilepath = null;
        try {
            resultFilepath = Files.move(mobiFile.toPath(), outputPath.resolve(mobiFile.getName()));
            logger.debug("Mobi file moved to: " + resultFilepath.toAbsolutePath().toString());
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }

        logger.debug("main() exit.");
    }

    private static void usage() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("latex2mobi", options);
    }

    private static void initializeOptions() {
        options = new Options();

        Option inputOption = new Option("i", "inputPaths", true, "inputPaths file");
        inputOption.setRequired(true);
        inputOption.setArgs(Option.UNLIMITED_VALUES);
        inputOption.setValueSeparator(',');
        options.addOption(inputOption);

        options.addOption("o", "output-dir", true, "output directory");
        options.addOption("h", "help", false, "show this help");

        Option picturesOption = new Option("r", "replace-with-images");
        picturesOption.setArgs(0);
        picturesOption.setDescription("replace latex formulas with pictures, override html");
        picturesOption.setRequired(false);
        options.addOption(picturesOption);

        options.addOption(latexToHtmlConverter.getExecOption());
        options.addOption(htmlToMobiConverter.getExecOption());
    }
}
