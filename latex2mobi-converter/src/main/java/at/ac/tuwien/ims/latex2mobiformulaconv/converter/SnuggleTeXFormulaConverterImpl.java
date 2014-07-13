package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Formula;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import net.sourceforge.jeuclid.converter.Converter;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.xml.sax.SAXException;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnugglePackage;
import uk.ac.ed.ph.snuggletex.SnuggleSession;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author mauss
 *         Date: 08.06.14
 */
public class SnuggleTeXFormulaConverterImpl extends FormulaConverter {
    private static Logger logger = Logger.getLogger(SnuggleTeXFormulaConverterImpl.class);
    private static SnuggleEngine engine = new SnuggleEngine();

    private Path tempDirPath;

    public SnuggleTeXFormulaConverterImpl(Path tempDirPath) {
        this.tempDirPath = tempDirPath;
    }

    static {
        // Add special SnuggleTeX configuration for certain Elements
        for (SnugglePackage p : SnugglePackageRegistry.getPackages()) {
            engine.addPackage(p);
        }
    }

    @Override
    public Formula parse(String latexFormula) {
        logger.debug("parse() entered...");

        // TODO implement
        // Check if multiline
        // Eine Leerzeile oder ein Doppelbackslash \\ bewirken einen neuen Absatz.
        Formula formula = new Formula();

        formula.setLatexCode(latexFormula);

        if (latexFormula.matches("(?m)^\\s+$") || latexFormula.contains("\\\\")) {
            formula.setType(Formula.Type.MULTI_LINE);
        } else {
            formula.setType(Formula.Type.SINGLE_LINE);
        }
        logger.debug(formula.getType());
        logger.debug(latexFormula);


        SnuggleInput input = new SnuggleInput(latexFormula);
        try {

            SnuggleSession session = engine.createSession();
            session.parseInput(input);
            String xmlString = session.buildXMLString();
            logger.debug("MathML: " + xmlString);

            // TODO ignore empty formulas

            formula.setMathMl(xmlString);
            try {
                final org.w3c.dom.Document doc = MathMLParserSupport.parseString(xmlString);

                final File outFile = Files.createTempFile(this.tempDirPath, "formula", ".png").toFile();

                formula.setImageFilePath(outFile.toPath());

                final MutableLayoutContext params = new LayoutContextImpl(
                        LayoutContextImpl.getDefaultLayoutContext());
                params.setParameter(Parameter.MATHSIZE, 25f);
                Converter.getInstance().convert(doc, outFile, "image/png", params);
                logger.debug("Image file created at: " + outFile.toPath().toAbsolutePath().toString());
            } catch (SAXException e1) {
                logger.error(e1.getMessage(), e1);
            } catch (ParserConfigurationException e2) {
                logger.error(e2.getMessage(), e2);
            }


        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        return formula;
    }

    public Document replaceFormulasWithImages(Document document, Map<Integer, Path> imagePaths) {
        List<Element> foundElements = xpath.evaluate(document);
        Map<String, Element> elementMap = new HashMap<>();

        for (Element element : foundElements) {
            elementMap.put(element.getAttribute("id").getValue(), element);
        }

        Iterator<Integer> pathIterator = imagePaths.keySet().iterator();

        while (pathIterator.hasNext()) {
            Integer id = pathIterator.next();

            Element element = elementMap.get(FORMULA_ID_PREFIX + id);
            Path imagePath = imagePaths.get(id);

            element.removeAttribute("class");
            element.removeContent();
            Element imageTag = new Element("img");
            imageTag.setAttribute("src", this.tempDirPath.relativize(imagePath).toString());
            imageTag.setAttribute("alt", FORMULA_ID_PREFIX + id);
            element.addContent(imageTag);
        }


        return document;
    }
}
