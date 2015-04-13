package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi.HtmlToMobiConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.latex2html.LatexToHtmlConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.FormulaConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.Formula;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/*
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
 */

/**
 *
 *
 * @author Michael Auß
 *         Date: 17.08.14
 *         Time: 17:35
 *
 * Converts LaTeX input to Mobi files with Pandoc & Amazon Kindlegen
 */
public class Converter {
    private static Logger logger = Logger.getLogger(Converter.class);
    private static final String MAIN_CSS_FILENAME = "main.css";
    private static final String FILE_EXTENSION = ".mobi";

    private LatexToHtmlConverter latexToHtmlConverter;
    private HtmlToMobiConverter htmlToMobiConverter;
    private FormulaConverter formulaConverter;

    private Path workingDirectory;

    /**
     *  ArrayList of input paths, only the first gets read
     */
    private ArrayList<Path> inputPaths;

    /**
     * if true, the formulas will get replaced with png pictures
     * else the will be represented with html
     */
    private boolean replaceWithPictures = false;
    private boolean debugMarkupOutput = false;

    private boolean exportMarkup = false;
    private boolean noMobiConversion = false;

    /**
     * the directory path where the result will be written to
     */
    private Path outputPath;

    /**
     * the filename of the result file, if it already exists, a number will automatically be added to this string
     */
    private String filename;

    private String title;

    public Path getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    public LatexToHtmlConverter getLatexToHtmlConverter() {
        return latexToHtmlConverter;
    }

    public void setLatexToHtmlConverter(LatexToHtmlConverter latexToHtmlConverter) {
        this.latexToHtmlConverter = latexToHtmlConverter;
    }

    public HtmlToMobiConverter getHtmlToMobiConverter() {
        return htmlToMobiConverter;
    }

    public void setHtmlToMobiConverter(HtmlToMobiConverter htmlToMobiConverter) {
        this.htmlToMobiConverter = htmlToMobiConverter;
    }

    public FormulaConverter getFormulaConverter() {
        return formulaConverter;
    }

    public void setFormulaConverter(FormulaConverter formulaConverter) {
        this.formulaConverter = formulaConverter;
    }

    public boolean isExportMarkup() {
        return exportMarkup;
    }

    public void setExportMarkup(boolean exportMarkup) {
        this.exportMarkup = exportMarkup;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isReplaceWithPictures() {
        return replaceWithPictures;
    }

    public void setReplaceWithPictures(boolean replaceWithPictures) {
        this.replaceWithPictures = replaceWithPictures;
    }

    public boolean isDebugMarkupOutput() {
        return debugMarkupOutput;
    }

    public void setDebugMarkupOutput(boolean debugMarkupOutput) {
        this.debugMarkupOutput = debugMarkupOutput;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(Path outputPath) {
        this.outputPath = outputPath;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public ArrayList<Path> getInputPaths() {
        return inputPaths;
    }

    public void setInputPaths(ArrayList<Path> inputPaths) {
        this.inputPaths = inputPaths;
    }

    public boolean isNoMobiConversion() {
        return noMobiConversion;
    }

    public void setNoMobiConversion(boolean noMobiConversion) {
        this.noMobiConversion = noMobiConversion;
    }

    /**
     * Converts a single input file from LaTeX to Mobi
     * @return Path of the resulting File
     */
    public Path convert() {
        // Currently only the first input path will be evaluated
        File inputFile = inputPaths.get(0).toFile();

        // set default title
        if (title == null) {
            title = inputFile.getName();
        }

        // Main Document conversion to HTML, without formulas
        logger.debug("Converting main document to HTML...");
        Document document = latexToHtmlConverter.convert(inputFile, title);

        logger.info("Parsing LaTeX formulas from resulting HTML...");


        Map<Integer, Formula> formulaMap = new HashMap<>();

        formulaConverter.setGenerateDebugMarkup(debugMarkupOutput);

        Map<Integer, String> latexFormulas = formulaConverter.extractFormulas(document);

        if (latexFormulas.isEmpty() == false) {
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
        }

        // Saving html file
        File htmlFile = saveHtmlFile(document);

        Path markupDir = null;
        if (exportMarkup || noMobiConversion) {
            markupDir = exportMarkup(htmlFile.toPath());
        }

        if (noMobiConversion) {
            return markupDir.toAbsolutePath();
        } else {
            // Convert to MOBI format
            logger.info("Converting completed HTML to MOBI format...");
            File mobiFile = htmlToMobiConverter.convertToMobi(htmlFile);

            // Save file
            Path resultFilepath = null;
            try {
                // Don't overwrite files
                Path outputFilepath;
                int i = 1;
                String replaceFilename = filename + FILE_EXTENSION;
                while (true) {
                    outputFilepath = outputPath.resolve(replaceFilename);
                    if (Files.exists(outputFilepath) == false) {
                        break;
                    }
                    replaceFilename = filename + " (" + i + ")" + FILE_EXTENSION;
                    i++;
                }

                resultFilepath = Files.move(mobiFile.toPath(), outputFilepath);
                logger.debug("Mobi file moved to: " + resultFilepath.toAbsolutePath().toString());
                return resultFilepath.toAbsolutePath();

            } catch (IOException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }
    }

    /**
     * Saves the html document to a file with .html extension
     * @param document JDOM Document representing the HTML
     * @return written HTML File object
     */
    private File saveHtmlFile(Document document) {
        Path tempFilepath = null;



            Path tempDirPath = formulaConverter.getTempDirPath();

            ClassLoader classLoader = getClass().getClassLoader();
            InputStream mainCssIs = classLoader.getResourceAsStream(MAIN_CSS_FILENAME);

            logger.debug("Copying main.css file to temp dir: " + tempDirPath.toAbsolutePath().toString());
            try {
                Files.copy(mainCssIs, tempDirPath.resolve(MAIN_CSS_FILENAME));
            } catch (FileAlreadyExistsException e) {
                // do nothing
            } catch (IOException e) {
                logger.error("could not copy main.css file to temp dir!");
            }

            tempFilepath = tempDirPath.resolve("latex2mobi.html");

            logger.debug("tempFile created at: " + tempFilepath.toAbsolutePath().toString());
        try {
            Files.write(tempFilepath, new XMLOutputter().outputString(document).getBytes(Charset.forName("UTF-8")));

            if (debugMarkupOutput) {
                logger.info("Debug markup will be generated.");
            }

        } catch (IOException e) {
            logger.error("Error writing HTML to temp dir!");
            logger.error(e.getMessage(), e);
            }

        return tempFilepath.toFile();
    }

    private Path exportMarkup(Path tempFilepath) {
        Path resultPath;
        if (outputPath != null) {
            resultPath = outputPath;
        } else {
            resultPath = workingDirectory;
        }

        Path markupDir = resultPath.resolve(title + "-markup");
        try {
            try {
                Files.createDirectory(markupDir);
            } catch (FileAlreadyExistsException e) {
                // do nothing
            }

            Path tempDirPath = tempFilepath.getParent();
            File tempDir = tempDirPath.toFile();

            // Copy all files from temp folder to the markup output folder
            String[] files = tempDir.list(FileFileFilter.FILE);
            for (int i = 0; i < files.length; i++) {
                Files.copy(tempDirPath.resolve(files[i]), markupDir.resolve(files[i]), StandardCopyOption.REPLACE_EXISTING);
            }

            logger.info("Exported markup to folder: " + markupDir.toAbsolutePath().toString());
        } catch (IOException e) {
            logger.error("Error saving markup files: " + e.getMessage(), e);
            return null;
        }
        return markupDir;
    }
}
