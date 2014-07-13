package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Formula;
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
public abstract class FormulaConverter {
    public static final String FORMULA_ID_PREFIX = "formula_";
    private static XPathFactory xPathFactory = XPathFactory.instance();
    protected static XPathExpression<Element> xpath = xPathFactory.compile("//*[@class='LaTeX']", Filters.element());

    /**
     * Parse latex formula code to entities, which afterward can be rendered to html
     *
     * @param latexFormula
     * @return Parsed Formula root object, tree-like representation of formula for further html rendering
     */
    public abstract Formula parse(String latexFormula);

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


}
