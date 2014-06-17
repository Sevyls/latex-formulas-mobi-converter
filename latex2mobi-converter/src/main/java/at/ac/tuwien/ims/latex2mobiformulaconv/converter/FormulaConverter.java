package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Formula;
import org.jdom2.Document;

import java.util.Map;

/**
 * @author mauss
 *         Date: 08.06.14
 */
public interface FormulaConverter {

    /**
     * Parse latex formula code to entities, which afterward can be rendered to html
     *
     * @param latexFormula
     * @return Parsed Formula root object, tree-like representation of formula for further html rendering
     */
    public Formula parse(String latexFormula);

    /**
     * Parses an JDOM HTML Document for formula entries, sets an id to refer to it in the future.
     *
     * @param document JDOM HTML Document to parse
     * @return Map of formulas, keys: given id, values: corresponding latex formula code from the document
     */
    public Map<Integer, String> extractFormulas(Document document);
}
