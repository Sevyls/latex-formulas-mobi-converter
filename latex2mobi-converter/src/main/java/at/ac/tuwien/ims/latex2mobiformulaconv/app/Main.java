package at.ac.tuwien.ims.latex2mobiformulaconv.app;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.Converter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi.HtmlToMobiConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.latex2html.LatexToHtmlConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.utils.WorkingDirectoryResolver;
import org.apache.commons.cli.*;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

/**
 * The MIT License (MIT)
 * latex2mobi -- LaTeX Formulas to Mobi Converter
 * Copyright (c) 2014 Michael Auß
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 * <p/>
 * For Third Party Software Licenses read LICENSE file in base dir.
 *
 * @author mauss
 *         Created: 29.04.14 22:55
 */
public class Main {
    private static final String CONFIGURATION_FILENAME = "configuration.properties";

    // Utils
    private static Logger logger = Logger.getRootLogger();
    private static PropertiesConfiguration config;
    private static Options options;
    private static ApplicationContext applicationContext;

    // Flag options
    private static boolean replaceWithPictures = false;
    private static boolean debug = false;
    private static boolean exportMarkup = false;
    private static boolean noMobiConversion = false;

    // Value options
    private static String title = null;
    private static String filename = "LaTeX2Mobi";

    // Paths
    private static ArrayList<Path> inputPaths = new ArrayList<Path>();
    private static Path workingDirectory;
    private static Path outputPath;


    /**
     * Main application method, may exit on error.
     *
     * @param args standard posix command line arguments
     */
    public static void main(String[] args) {
        logger.debug("main() started.");
        // Init
        applicationContext = new ClassPathXmlApplicationContext("/application-context.xml");
        setupWorkingDirectory();
        initializeOptions();
        // Analyse options
        parseCli(args);
        logger.debug("CLI arguments parsed.");

        logger.debug("Working directory set up to: " + workingDirectory.toAbsolutePath().toString());


        logger.debug("Application context loaded.");

        loadConfiguration();
        logger.debug("Configuration loaded.");

        // Start conversion
        Converter converter;
        if (replaceWithPictures) {
            converter = (Converter) applicationContext.getBean("image-converter");
        } else {
            converter = (Converter) applicationContext.getBean("dom-converter");
        }

        converter.setWorkingDirectory(workingDirectory);

        converter.setInputPaths(inputPaths);
        converter.setReplaceWithPictures(replaceWithPictures);
        converter.setOutputPath(outputPath);
        converter.setFilename(filename);
        converter.setTitle(title);
        converter.setDebug(debug);
        converter.setExportMarkup(exportMarkup);
        converter.setNoMobiConversion(noMobiConversion);

        Path resultFile = converter.convert();

        logger.info("Result : " + resultFile.toAbsolutePath().toString());

        logger.debug("main() exit.");
    }

    private static void parseCli(String[] args) {
        CommandLineParser parser = new PosixParser();
        try {
            CommandLine cmd = parser.parse(options, args);

            // Show usage, ignore other parameters and quit
            if (cmd.hasOption('h')) {
                usage();
                logger.debug("Help called, main() exit.");
                System.exit(0);
            }

            if (cmd.hasOption('d')) {
                // Activate debug markup only - does not affect logging!
                debug = true;
            }
            
            if (cmd.hasOption('m')) {
                exportMarkup = true;
            }

            if (cmd.hasOption('t')) {
                title = cmd.getOptionValue('t');
            }

            if (cmd.hasOption('f')) {
                filename = cmd.getOptionValue('f');
            }

            if (cmd.hasOption('n')) {
                exportMarkup = true; // implicit markup export
                noMobiConversion = true;
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
                logger.error("You have to specify an inputPath file or directory!");
                usage();
                System.exit(1);
            }

            if (cmd.hasOption('o')) {
                String outputDirectory = cmd.getOptionValue('o');

                //outputPath = workingDirectory.resolve(outputDirectory);
                outputPath = Paths.get(outputDirectory).toAbsolutePath();
                logger.debug("Output path: " + outputPath.toAbsolutePath().toString());
                if (!Files.isDirectory(outputPath) && Files.isRegularFile(outputPath)) {
                    logger.error("Given output directory is a file! Exiting...");

                    System.exit(1);
                }

                if (!Files.exists(outputPath)) {
                    Files.createDirectory(outputPath);
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
            LatexToHtmlConverter latexToHtmlConverter = (LatexToHtmlConverter) applicationContext.getBean("latex2html-converter");
            if (cmd.hasOption(latexToHtmlConverter.getExecOption().getOpt())) {
                String execValue = cmd.getOptionValue(latexToHtmlConverter.getExecOption().getOpt());
                logger.info("LaTeX to HTML Executable argument was given: " + execValue);
                Path execPath = Paths.get(execValue);
                latexToHtmlConverter.setExecPath(execPath);
            }

            HtmlToMobiConverter htmlToMobiConverter = (HtmlToMobiConverter) applicationContext.getBean("html2mobi-converter");
            Option htmlToMobiOption = htmlToMobiConverter.getExecOption();
            if (cmd.hasOption(htmlToMobiOption.getOpt())) {
                String execValue = cmd.getOptionValue(htmlToMobiOption.getOpt());
                logger.info("HTML to Mobi Executable argument was given: " + execValue);
                try {
                    Path execPath = Paths.get(execValue);
                    htmlToMobiConverter.setExecPath(execPath);
                } catch (InvalidPathException e) {
                    logger.error("Invalid path given for --" + htmlToMobiOption.getLongOpt() + " <" + htmlToMobiOption.getArgName() + ">");
                    logger.error("I will try to use your system's PATH variable...");
                }
            }


        } catch (MissingOptionException m) {
            logger.error(m.getMessage());
            usage();
            System.exit(1);
        } catch (MissingArgumentException a) {
            logger.error(a.getMessage());
            usage();
            System.exit(1);
        } catch (ParseException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }
    }

    private static void setupWorkingDirectory() {
        workingDirectory = WorkingDirectoryResolver.getWorkingDirectory(Main.class);

        if (workingDirectory == null) {
            logger.error("Exiting...");
            System.exit(1);
        }
    }

    private static void usage() {
        HelpFormatter formatter = new HelpFormatter();

        // Windows cmd.exe mag mein scharfes ß nicht.
        // Grrr.... Schei? Encoding.
        String header = "LaTeX Formulas to Mobi Converter\n"
                + "(c) 2014-2015 by Michael Auss\n\n\n";
        String footer = "\n\n\nThis software is open source, hosted at GitHub:\n"
                + "https://github.com/Sevyls/latex-formulas-mobi-converter";
        formatter.printHelp("latex2mobi", header, options, footer);
    }

    private static void loadConfiguration() {
        Path defaultConfigPath = workingDirectory.resolve(CONFIGURATION_FILENAME);
        logger.debug("Searching for config in default filepath: " + defaultConfigPath.toAbsolutePath().toString());

        try {
            logger.debug("Loading configuration.");

            config = new PropertiesConfiguration(defaultConfigPath.toFile());

        } catch (ConfigurationException e) {
            logger.error(e.getMessage(), e);
            logger.error("Configuration could not be loaded. Exiting...");
            System.exit(-1);
        }

        if (!(defaultConfigPath.toFile().exists() && defaultConfigPath.toFile().isFile())) {
            try {
                logger.info("Creating empty config file in working directory...");
                config.save();
                logger.debug("Saved empty configuration: " + defaultConfigPath.toAbsolutePath().toString());
            } catch (ConfigurationException e) {
                logger.error("Default configuration file could not be saved!");
                logger.error(e.getMessage(), e);
            }
        }
    }

    private static void initializeOptions() {
        options = new Options();

        Option inputOption = new Option("i", "inputPaths", true, "inputPaths file");
        inputOption.setRequired(true);
        inputOption.setArgs(Option.UNLIMITED_VALUES);
        inputOption.setOptionalArg(false);
        inputOption.setValueSeparator(',');
        options.addOption(inputOption);

        options.addOption("f", "filename", true, "output filename");
        options.addOption("o", "output-dir", true, "output directory");
        options.addOption("m", "export-markup", false, "export html markup");
        options.addOption("n", "no-mobi", false, "no Mobi conversion, just markup, NOTE: makes -m implicit!");
        options.addOption("t", "title", true, "Document title");
        options.addOption("h", "help", false, "show this help");
        options.addOption("d", "debug", false, "show debug output in html markup");

        Option picturesOption = new Option("r", "replace latex formulas with pictures, override html");
        picturesOption.setLongOpt("replace-with-images");
        picturesOption.setArgs(0);
        picturesOption.setRequired(false);
        options.addOption(picturesOption);

        // implementation specific options
        options.addOption(((LatexToHtmlConverter) applicationContext.getBean("latex2html-converter")).getExecOption());
        options.addOption(((HtmlToMobiConverter) applicationContext.getBean("html2mobi-converter")).getExecOption());
    }
}
