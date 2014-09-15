package at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals;

import org.jdom2.Element;

/**
 * @author Michael Auß
 *         Created: 15.09.2014
 */
public class Mn extends Literal {
    private String value;

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }

    @Override
    public Element render() {
        Element mnSpan = new Element("span");
        mnSpan.setAttribute("class", "mn");
        mnSpan.addContent(value);

        return mnSpan;
    }
}
