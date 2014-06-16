package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Formula;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mauss
 *         Date: 08.06.14
 */
public class FormulaConverterImpl implements FormulaConverter {
    private static Logger logger = Logger.getLogger(FormulaConverterImpl.class);

    public static final String FORMULA_ID_PREFIX = "formula_";
    private static XPathFactory xPathFactory = XPathFactory.instance();
    private static XPathExpression<Element> xpath = xPathFactory.compile("//*[@class='LaTeX']", Filters.element());


    @Override
    public Formula parse(String latexFormula) {
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
