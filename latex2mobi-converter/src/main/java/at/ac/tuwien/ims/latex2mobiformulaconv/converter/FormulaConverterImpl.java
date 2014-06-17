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
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.xml.sax.SAXException;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mauss
 *         Date: 08.06.14
 */
public class FormulaConverterImpl implements FormulaConverter {
    private static Logger logger = Logger.getLogger(FormulaConverterImpl.class);
    private static SnuggleEngine engine = new SnuggleEngine();

    public static final String FORMULA_ID_PREFIX = "formula_";
    private static XPathFactory xPathFactory = XPathFactory.instance();
    private static XPathExpression<Element> xpath = xPathFactory.compile("//*[@class='LaTeX']", Filters.element());


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
            logger.debug("Snuggle: " + xmlString);
            try {
                final org.w3c.dom.Document doc = MathMLParserSupport.parseString(xmlString);

                final File outFile = new File("test1.png");
                final MutableLayoutContext params = new LayoutContextImpl(
                        LayoutContextImpl.getDefaultLayoutContext());
                params.setParameter(Parameter.MATHSIZE, 25f);
                Converter.getInstance().convert(doc, outFile, "image/png", params);
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

    @Override
    public Map<Integer, Formula> extractFormulas(Document document) {
        Map<Integer, Formula> formulas = new HashMap<>();

        // TODO implement
        List<Element> foundElements = xpath.evaluate(document);
        int id = 0;
        for (Element element : foundElements) {

            element.setAttribute("id", FORMULA_ID_PREFIX + id);

            Formula formula = parse(element.getValue());
            formulas.put(id, formula);
            id++;
        }

        return formulas;
    }
}
