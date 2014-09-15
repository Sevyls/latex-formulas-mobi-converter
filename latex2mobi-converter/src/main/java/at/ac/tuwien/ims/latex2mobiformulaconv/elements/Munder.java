package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

/**
 * @author Michael Auß
 *         Date: 15.09.14
 *         Time: 22:35
 */
public class Munder implements FormulaElement {
    // TODO Munder
    private FormulaElement base;
    private FormulaElement underscript;

    public FormulaElement getUnderscript() {
        return underscript;
    }

    public void setUnderscript(FormulaElement underscript) {
        this.underscript = underscript;
    }

    public FormulaElement getBase() {
        return base;
    }

    public void setBase(FormulaElement base) {
        this.base = base;
    }

    @Override
    public Element render() {
        Element span = new Element("span");
        span.setAttribute("class", "munder");

        span.addContent(base.render());
        span.addContent(underscript.render());

        // TODO munder
        return span;
    }
}
