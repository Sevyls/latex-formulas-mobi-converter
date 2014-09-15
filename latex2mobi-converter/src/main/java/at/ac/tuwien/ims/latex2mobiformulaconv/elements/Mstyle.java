package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

/**
 * @author Michael Auß
 *         Date: 15.09.20
 */
public class Mstyle implements FormulaElement {


    @Override
    public Element render() {
        Element span = new Element("span");
        span.setAttribute("class", "mstyle");

        // TODO set style classes

        return span;
    }
}
