package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi.AmazonHtmlToMobiConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi.HtmlToMobiConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.latex.LatexToHtmlConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.latex.PandocLatexToHtmlConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.FormulaConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.ImageFormulaConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.SAXFormulaConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Formula;
import org.apache.log4j.Logger;
import org.jdom2.output.XMLOutputter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Michael Auß
 *         Date: 17.08.14
 *         Time: 17:35
 */
public class Converter {
    private static Logger logger = Logger.getLogger(Converter.class);
    private static LatexToHtmlConverter latexToHtmlConverter = new PandocLatexToHtmlConverter();
    private static HtmlToMobiConverter htmlToMobiConverter = new AmazonHtmlToMobiConverter();

    public void convert(ArrayList<Path> inputPaths, boolean replaceWithPictures, Path outputPath) {
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
    }

}
