package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

/**
 * @author Michael Auß
 *         Date: 15.09.14
 *         Time: 22:35
 */
public class Mover implements FormulaElement {
    // TODO Mover
    private FormulaElement base;
    private FormulaElement overscript;

    public FormulaElement getOverscript() {
        return overscript;
    }

    public void setOverscript(FormulaElement overscript) {
        this.overscript = overscript;
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
        span.setAttribute("class", "mover");

        span.addContent(base.render());
        span.addContent(overscript.render());

        // TODO mover
        return span;
    }
}
