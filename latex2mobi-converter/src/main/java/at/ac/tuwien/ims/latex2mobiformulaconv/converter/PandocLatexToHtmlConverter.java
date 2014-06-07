package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import org.apache.commons.cli.Option;
import org.apache.commons.exec.*;
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
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * @author mauss
 *         Created: 21.05.14 00:06
 */
public class PandocLatexToHtmlConverter implements LatexToHtmlConverter {
    private static Logger logger = Logger.getLogger(PandocLatexToHtmlConverter.class);

    @Override
    public Document convert(File tex) {
        logger.debug("Start convert() with file " + tex.toPath().toAbsolutePath().toString());

        // TODO read pandoc path from running config
        CommandLine cmdLine = new CommandLine("pandoc");

        cmdLine.addArgument("--from=latex");
        cmdLine.addArgument("--to=html5");
        cmdLine.addArgument("${file}");

        HashMap map = new HashMap();
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
                if (exceptionMessage.contains("Cannot run program \"pandoc\"")
                        && exceptionMessage.contains("No such file or directory")) {
                    logger.error("Pandoc could not be found! Exiting...");
                    logger.debug(executeException);
                    System.exit(1);
                }
                logger.debug(exceptionKlass + ": " + exceptionMessage);
            }

        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }
        // add html document structure to output
        String htmlOutput = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>";
        try {
            htmlOutput += writer.getBuffer().toString();
            writer.close();

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }


        htmlOutput += "</body>\n" +
                "</html>";

        logger.debug("html-output: " + htmlOutput);

        // TODO output loading as JDOM Document
        SAXBuilder sax = new SAXBuilder();
        Document document = null;
        try {
            document = sax.build(new StringReader(htmlOutput));
        } catch (JDOMException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
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
