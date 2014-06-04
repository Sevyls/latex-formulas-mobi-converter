package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import org.apache.commons.exec.*;
import org.apache.log4j.Logger;
import org.jdom2.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
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
        // TODO read pandoc path from running config
        CommandLine cmdLine = new CommandLine("pandoc");

        cmdLine.addArgument("--from=latex");
        cmdLine.addArgument("--to=html");

        // TODO set output file
        cmdLine.addArgument("--output=formulas.html");
        cmdLine.addArgument("${file}");
        HashMap map = new HashMap();
        try {
            // TODO Input file handling
            map.put("file", Paths.get(this.getClass().getClassLoader().getResource("formulas.tex").toURI()).toAbsolutePath());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }
        cmdLine.setSubstitutionMap(map);

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000);
        Executor executor = new DefaultExecutor();
        executor.setExitValue(1);
        executor.setWatchdog(watchdog);

        ExecuteStreamHandler pandocStreamHandler = new ExecuteStreamHandler() {
            @Override
            public void setProcessInputStream(OutputStream outputStream) throws IOException {
                //TODO pandoc input stream
            }

            @Override
            public void setProcessErrorStream(InputStream inputStream) throws IOException {
                // TODO monitor error stream
                // TODO Error handling
            }

            @Override
            public void setProcessOutputStream(InputStream inputStream) throws IOException {
                // TODO monitor output stream
                // TODO Error handling?
            }

            @Override
            public void start() throws IOException {
                // TODO ?
            }

            @Override
            public void stop() throws IOException {
                // TODO ?
            }
        };
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

        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            // TODO Exception handling
        }
        return null;
    }
}
