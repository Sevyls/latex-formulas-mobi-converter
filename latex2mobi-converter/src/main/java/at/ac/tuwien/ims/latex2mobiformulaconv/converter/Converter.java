package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi.AmazonHtmlToMobiConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi.HtmlToMobiConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.latex2html.LatexToHtmlConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.latex2html.PandocLatexToHtmlConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.DOMFormulaConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.FormulaConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.ImageFormulaConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Formula;
import org.apache.log4j.Logger;
import org.jdom2.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
 * @author Michael Auß
 *         Date: 17.08.14
 *         Time: 17:35
 */
public class Converter {
    private static Logger logger = Logger.getLogger(Converter.class);

    // hard coded Implementation bindings
    private static LatexToHtmlConverter latexToHtmlConverter = new PandocLatexToHtmlConverter();
    private static HtmlToMobiConverter htmlToMobiConverter = new AmazonHtmlToMobiConverter();

    private Path workingDirectory;

    public Converter(Path workingDirectory) {
        this.workingDirectory = workingDirectory;
    }


    /**
     * Converts a single input file from LaTeX to Mobi
     *
     * @param inputPaths          ArrayList of input paths, only the first gets read
     * @param replaceWithPictures if true, the formulas will get replaced with png pictures,
     *                            else the will be represented with html
     * @param outputPath          the directory path where the result will be written to
     * @param filename            the filename of the result file, if it already exists, a number will automatically be added to this string
     * @return Path of the resulting File
     */
    public Path convert(ArrayList<Path> inputPaths, boolean replaceWithPictures, Path outputPath, String filename, String title, boolean debug) {
        // TODO iterate over inputPaths
        File inputFile = inputPaths.get(0).toFile();

        // set default title
        if (title == null) {
            title = inputFile.getName();
        }

        // Main Document conversion to HTML, without formulas
        logger.debug("Converting main document to HTML...");
        Document document = latexToHtmlConverter.convert(inputFile, title, workingDirectory);

        /*XMLOutputter xout = new XMLOutputter();
        logger.debug(xout.outputString(document));*/

        logger.info("Parsing LaTeX formulas from resulting HTML...");


        FormulaConverter formulaConverter;
        Map<Integer, Formula> formulaMap = new HashMap<>();

        if (replaceWithPictures) {
            formulaConverter = new ImageFormulaConverter();
        } else {
            formulaConverter = new DOMFormulaConverter(debug);
        }

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

        // Convert to MOBI format
        logger.info("Converting completed HTML to MOBI format...");
        File mobiFile = htmlToMobiConverter.convertToMobi(document);

        // Save file
        Path resultFilepath = null;
        try {
            // Don't overwrite files
            Path outputFilepath;
            int i = 1;
            String replaceFilename = filename + ".mobi";
            while (true) {
                outputFilepath = outputPath.resolve(replaceFilename);
                if (Files.exists(outputFilepath) == false) {
                    break;
                }
                replaceFilename = filename + " (" + i + ").mobi";
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
