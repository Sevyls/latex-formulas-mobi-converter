package at.ac.tuwien.ims.latex2mobiformulaconv.converter.latex2html;

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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;

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

 * An implementation of the LaTeX to HTML Converter interface using Pandoc
 * Pandoc will be called with an input file be
 * and the results will be parsed from its output file.
 *
 * @author Michael Auß
 *         Created: 21.05.14 00:06
 */
public class PandocLatexToHtmlConverter implements LatexToHtmlConverter {
    private static Logger logger = Logger.getLogger(PandocLatexToHtmlConverter.class);

    private Path execPath = null;

    @Override
    public Document convert(File tex, String title) {
        logger.debug("Start convert() with file " + tex.toPath().toAbsolutePath().toString() + ", title: " + title);

        CommandLine cmdLine;
        if (execPath != null) {
            // Run the configured pandoc executable
            logger.info("Pandoc will be run from: " + execPath.toString());
            cmdLine = new CommandLine(execPath.toFile());
        } else {
            // Run in system PATH environment
            logger.info("Pandoc will be run within the PATH variable.");
            cmdLine = new CommandLine("pandoc");
        }

        cmdLine.addArgument("--from=latex");
        cmdLine.addArgument("--to=html5");
        cmdLine.addArgument("--asciimathml"); // With this option, pandoc does not render latex formulas

        cmdLine.addArgument("${file}");

        HashMap<String, Path> map = new HashMap<String, Path>();
        map.put("file", Paths.get(tex.toURI()));

        cmdLine.setSubstitutionMap(map);

        DefaultExecuteResultHandler resultHandler = new DefaultExecuteResultHandler();

        ExecuteWatchdog watchdog = new ExecuteWatchdog(60 * 1000); // max execution time 1 minute
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
            logger.error("Pandoc's execution failed, exiting...");
            logger.error(e.getMessage(), e);
            System.exit(-1);
        }

        try {
            resultHandler.waitFor();
            int exitValue = resultHandler.getExitValue();

            logger.debug("Pandoc execution's exit value: " + exitValue);
            ExecuteException executeException = resultHandler.getException();
            if (executeException != null && executeException.getCause() != null) {

                String exceptionKlass = executeException.getCause().getClass().getCanonicalName();
                String exceptionMessage = executeException.getCause().getMessage();

                if (exceptionKlass.endsWith("IOException") || exceptionMessage.contains("Cannot run program \"pandoc\"")) {
                    logger.error("Pandoc could not be found! Exiting...");
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
        // TODO pandoc's html output depends on the operating system
        // TODO pandoc on Mac OS X returns valid html

        // pandoc on Windows returns no document markup (html, head, body)
        // therefore we have to use a template
        String htmlOutput = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                // set title
                "<title>" + title + "</title>\n" +
                // include css
                "<link rel=\"stylesheet\" type=\"text/css\" href=\"main.css\"></link>\n" +
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

        //logger.debug("html-output: " + htmlOutput);

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

    @Override
    public void setExecPath(Path execPath) {
        this.execPath = execPath;
    }
}
