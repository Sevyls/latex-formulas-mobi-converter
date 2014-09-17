package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Auß
 *         Date: 15.09.20
 */
public class Mstyle implements FormulaElement {
    private List<FormulaElement> base = new ArrayList<>();
    private String style;


    public List<FormulaElement> getBase() {
        return base;
    }

    public void setBase(List<FormulaElement> base) {
        this.base = base;
    }

    public void addBaseElement(FormulaElement element) {
        this.base.add(element);
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    @Override
    public Element render() {
        Element span = new Element("span");

        // set style classes
        String cssClass = "mstyle";
        if (style != null && style.isEmpty() == false) {
            cssClass += " mathvariant-" + style;
        }

        span.setAttribute("class", cssClass);

        if (base.isEmpty() == false) {
            for (FormulaElement e : base) {
                span.addContent(e.render());
            }
        }
        return span;
    }
}
