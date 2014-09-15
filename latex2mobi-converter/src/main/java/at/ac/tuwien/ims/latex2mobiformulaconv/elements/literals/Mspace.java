package at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.FormulaElement;
import org.jdom2.Element;

/**
 * @author Michael Auß
 *         Date: 15.09.14
 */
public class Mspace implements FormulaElement {
    @Override
    public Element render() {
        Element span = new Element("span");
        span.setAttribute("class", "mspace");
        span.setText(" ");
        return span;
    }
}
