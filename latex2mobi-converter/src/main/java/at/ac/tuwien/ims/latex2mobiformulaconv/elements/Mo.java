package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

/**
 * @author Michael Auß
 *         Created: 15.09.2014
 */
public class Mo implements FormulaElement {
    private String operator;

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public Element render() {
        Element moSpan = new Element("span");
        moSpan.setAttribute("class", "mo");
        moSpan.addContent(operator);
        return moSpan;
    }
}
