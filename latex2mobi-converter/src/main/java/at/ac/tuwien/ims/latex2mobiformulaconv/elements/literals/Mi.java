package at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.FormulaElement;
import org.jdom2.Element;

/**
 * @author Michael Auß
 *         Date: 15.09.2014
 */
public class Mi implements FormulaElement {
    private String literal;

    public String getLiteral() {
        return literal;
    }

    public void setLiteral(String literal) {
        this.literal = literal;
    }

    @Override
    public Element render() {
        Element span = new Element("span");
        span.setAttribute("class", "mi");
        span.setText(literal);
        return span;
    }
}
