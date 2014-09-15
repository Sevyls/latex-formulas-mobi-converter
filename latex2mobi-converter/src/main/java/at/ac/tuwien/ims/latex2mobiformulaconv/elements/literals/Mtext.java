package at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals;

import org.jdom2.Element;

/**
 * @author mauss
 *         Created: 20.05.14 23:41
 */
public class Mtext extends Literal {
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
        Element span = new Element("span");
        span.setAttribute("class", "mspan");
        span.setText(value);
        return span;
    }
}
