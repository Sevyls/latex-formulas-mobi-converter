package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Formula;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import uk.ac.ed.ph.snuggletex.SnuggleEngine;
import uk.ac.ed.ph.snuggletex.SnuggleInput;
import uk.ac.ed.ph.snuggletex.SnuggleSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author mauss
 *         Date: 08.06.14
 */
public abstract class FormulaConverter {
    public static final String FORMULA_ID_PREFIX = "formula_";
    private static XPathFactory xPathFactory = XPathFactory.instance();
    protected static XPathExpression<Element> xpath = xPathFactory.compile("//*[@class='LaTeX']", Filters.element());
    private static Logger logger = Logger.getLogger(FormulaConverter.class);
    protected static SnuggleEngine engine = new SnuggleEngine();

    /**
     * Parse latex formula code to entities, which afterward can be rendered to html
     *
     * @param latexFormula
     * @return Parsed Formula root object, tree-like representation of formula for further html rendering
     */
    public abstract Formula parse(int id, String latexFormula);


    public Formula parseToMathML(int id, String latexFormula) {
        Formula formula = new Formula(id);

        formula.setLatexCode(latexFormula);

        // Check if multiline
        // Eine Leerzeile oder ein Doppelbackslash \\ bewirken einen neuen Absatz.
        if (latexFormula.matches("(?m)^\\s+$") || latexFormula.contains("\\\\")) {
            formula.setType(Formula.Type.MULTI_LINE);
        } else {
            formula.setType(Formula.Type.SINGLE_LINE);
        }
        logger.debug(formula.getType());
        logger.debug(latexFormula);

        // Parse MathML formula and convert to png file
        SnuggleInput input = new SnuggleInput(latexFormula);
        try {

            SnuggleSession session = engine.createSession();
            session.parseInput(input);
            String xmlString = session.buildXMLString();
            logger.debug("MathML: " + xmlString);

            // TODO ignore empty formulas

            formula.setMathMl(xmlString);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return formula;
    }

    /**
     * Parses an JDOM HTML Document for formula entries, sets an id to refer to it in the future.
     *
     * @param document JDOM HTML Document to parse
     * @return Map of formulas, keys: given id, values: corresponding latex formula code from the document
     */
    public Map<Integer, String> extractFormulas(Document document) {
        Map<Integer, String> formulas = new HashMap<>();


        List<Element> foundElements = xpath.evaluate(document);
        int id = 0;
        for (Element element : foundElements) {

            element.setAttribute("id", FORMULA_ID_PREFIX + id);


            formulas.put(id, element.getValue());
            id++;
        }

        return formulas;
    }

    public Document replaceFormulas(Document doc, Map<Integer, Formula> formulaMap) {
        List<Element> foundElements = xpath.evaluate(doc);
        Map<String, Element> elementMap = new HashMap<>();

        for (Element element : foundElements) {
            elementMap.put(element.getAttribute("id").getValue(), element);
        }

        Iterator<Integer> formulaIterator = formulaMap.keySet().iterator();

        while (formulaIterator.hasNext()) {
            Integer id = formulaIterator.next();

            Element element = elementMap.get(FORMULA_ID_PREFIX + id);
            Formula formula = formulaMap.get(id);

            element.removeAttribute("class");
            element.removeContent();

            element.addContent(formula.getHtml());
        }
        return doc;
    }


}
