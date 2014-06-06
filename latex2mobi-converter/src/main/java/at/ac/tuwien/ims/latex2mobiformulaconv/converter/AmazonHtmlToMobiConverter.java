package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import org.apache.commons.cli.Option;
import org.apache.commons.exec.*;
import org.apache.commons.io.output.WriterOutputStream;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author mauss
 *         Created: 21.05.14 00:13
 *         // TODO Documentation
 */
public class AmazonHtmlToMobiConverter implements HtmlToMobiConverter {
    private static Logger logger = Logger.getLogger(AmazonHtmlToMobiConverter.class);

    @Override
    public File convertToMobi(Document document) {
        logger.debug("Enter convertToMobi()...");

        if (document == null) {
            logger.error("Document is null, aborting...");
            System.exit(1);
        }

        // Save document to temporary file
        Path tempFilepath = null;
        try {
            tempFilepath = Files.createTempFile("latex2mobi", ".html");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }

        try {
            Files.write(tempFilepath, new XMLOutputter().outputString(document).getBytes(Charset.forName("UTF-8")));
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }


        // TODO  read kindlegen path from running config
        CommandLine cmdLine = new CommandLine("kindlegen");
        cmdLine.addArgument("\"" + tempFilepath.toAbsolutePath().toString() + "\"");
        cmdLine.addArgument("-c0");
        cmdLine.addArgument("-locale en");

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
        Executor executor = new DefaultExecutor();
        executor.setExitValue(1);
        executor.setWatchdog(watchdog);
        StringWriter writer = new StringWriter();
        WriterOutputStream writerOutputStream = new WriterOutputStream(writer, Charset.forName("UTF-8"));

        ExecuteStreamHandler kindlegenStreamHandler = new PumpStreamHandler(writerOutputStream, System.err);
        executor.setStreamHandler(kindlegenStreamHandler);

        logger.debug("Launching kindlegen:");
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
            // TODO evaluate exitValue
            logger.debug("Kindlegen execution's exit value: " + exitValue);
            ExecuteException executeException = resultHandler.getException();
            if (executeException != null) {
                // TODO Handle ExecuteException IOException -- "Das System kann die angegebene Datei nicht finden"
                logger.debug(executeException);
            }

        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }
        String output = "";
        try {
            output += writer.getBuffer().toString();
            writer.close();

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }

        // TODO evaluate Kindlegen output
        logger.debug("Kindlegen output: \n" + output);

        // TODO move kindlegen mobi file to designated output file / directory
        // TODO return File
        return null;
    }


    @Override
    public Option getExecOption() {
        Option option = new Option("k", "kindlegen-exec", true, "Amazon KindleGen executable location");
        option.setArgs(1);
        return option;
    }
}
