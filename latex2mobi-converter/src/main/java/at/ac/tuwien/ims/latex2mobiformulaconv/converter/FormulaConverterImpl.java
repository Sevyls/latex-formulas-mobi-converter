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

    private static XPathFactory xPathFactory = XPathFactory.instance();
    private static XPathExpression<Element> xpath = xPathFactory.compile("//*[@class='math']", Filters.element());


    @Override
    public Formula parse(String latexFormula) {
        // TODO implement
        return null;
    }

    @Override
    public Map<Integer, String> parseFormulas(Document document) {
        Map<Integer, String> formulas = new HashMap<>();

        // TODO implement
        List<Element> foundElements = xpath.evaluate(document);
        for (Element element : foundElements) {
            logger.debug(element.getValue());
        }

        return formulas;
    }
}
