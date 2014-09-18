package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

/**
 * @author mauss
 *         Created: 20.05.14 23:29
 */

/**
 *
 */
public interface FormulaElement {

    /**
     * Renders this FormulaElement to HTML.
     * May call render() on sub-elements for recursive rendering.
     *
     * @return A JDOM Element which represents this FormulaElement + sub-elements (i.e. in HTML)
     */
    public Element render();
}
