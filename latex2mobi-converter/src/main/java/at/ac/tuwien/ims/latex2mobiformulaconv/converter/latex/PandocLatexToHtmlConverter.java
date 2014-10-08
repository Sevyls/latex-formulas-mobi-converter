package at.ac.tuwien.ims.latex2mobiformulaconv.converter.latex;

import org.apache.commons.cli.Option;
import org.apache.commons.exec.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author Michael Auß
 *         Created: 21.05.14 00:06
 */
public class PandocLatexToHtmlConverter implements LatexToHtmlConverter {
    private static Logger logger = Logger.getLogger(PandocLatexToHtmlConverter.class);

    private String includeCss(Path workingDirectory) {
        // Load main css file
        File mainCss = workingDirectory.resolve("main.css").toFile();

        String css = "";
        try {
            css = FileUtils.readFileToString(mainCss, Charset.forName("UTF-8"));
        } catch (IOException e) {
            logger.error("Error reading main.css file\n" + e.getMessage(), e);
        }

        // TODO compile all css files to single string

        return "<style>\n" + css + "\n</style>";
    }

    @Override
    public Document convert(File tex, String title, final Path workingDirectory) {
        logger.debug("Start convert() with file " + tex.toPath().toAbsolutePath().toString() + ", title: " + title);

        // TODO read pandoc path from running config
        CommandLine cmdLine = new CommandLine("pandoc");

        cmdLine.addArgument("--from=latex");
        cmdLine.addArgument("--to=html5");
        cmdLine.addArgument("--asciimathml");

        cmdLine.addArgument("${file}");

        HashMap<String, Path> map = new HashMap<String, Path>();
        map.put("file", Paths.get(tex.toURI()));

        cmdLine.setSubstitutionMap(map);

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
        Executor executor = new DefaultExecutor();
        executor.setExitValue(1);
        executor.setWatchdog(watchdog);
        StringWriter writer = new StringWriter();
        WriterOutputStream writerOutputStream = new WriterOutputStream(writer, Charset.forName("UTF-8"));

        ExecuteStreamHandler pandocStreamHandler = new PumpStreamHandler(writerOutputStream, System.err);
        executor.setStreamHandler(pandocStreamHandler);

        logger.debug("Launching pandoc:");
        logger.debug(cmdLine.toString());

        try {
            executor.execute(cmdLine, resultHandler);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }

        try {
            resultHandler.waitFor();
            int exitValue = resultHandler.getExitValue();

            logger.debug("Pandoc execution's exit value: " + exitValue);
            ExecuteException executeException = resultHandler.getException();
            if (executeException != null && executeException.getCause() != null) {

                String exceptionKlass = executeException.getCause().getClass().getCanonicalName();
                String exceptionMessage = executeException.getCause().getMessage();
                if (exceptionMessage.contains("Cannot run program \"pandoc\"")) {
                    logger.error("Pandoc could not be found! Exiting...");
                    logger.error("Please make sure that pandoc is installed on your system and available in the PATH variable");
                    logger.debug(executeException);
                    System.exit(1);
                }
                logger.debug(exceptionKlass + ": " + exceptionMessage);
            }

        } catch (InterruptedException e) {
            logger.error("pandoc conversion thread got interrupted, exiting...");
            logger.error(e.getMessage(), e);
            System.exit(1);
        }

        // add html document structure to output
        String htmlOutput = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                // set title
                "<title>" + title + "</title>\n" +
                // include css
                includeCss(workingDirectory) +
                "</head>\n" +
                "<body>";
        try {
            htmlOutput += writer.getBuffer().toString();
            writer.close();

        } catch (IOException e) {
            logger.error("Error reading html result from StringBuffer...");
            logger.error(e.getMessage(), e);
            System.exit(1);
        }


        htmlOutput += "</body>\n" +
                "</html>";

        logger.debug("html-output: " + htmlOutput);

        // output loading as JDOM Document
        SAXBuilder sax = new SAXBuilder();
        Document document = null;
        try {
            document = sax.build(new StringReader(htmlOutput));
        } catch (JDOMException e) {
            logger.error("JDOM Parsing error");
            logger.error(e.getMessage(), e);
            System.exit(1);
        } catch (IOException e) {
            logger.error("Error reading from String...");
            logger.error(e.getMessage(), e);
            System.exit(1);
        }
        return document;
    }

    @Override
    public Option getExecOption() {
        Option pandocOption = new Option("p", "pandoc-exec", true, "pandoc executable location");
        pandocOption.setArgs(1);
        return pandocOption;
    }
}
