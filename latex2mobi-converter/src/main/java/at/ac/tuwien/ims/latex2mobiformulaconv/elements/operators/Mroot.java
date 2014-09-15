package at.ac.tuwien.ims.latex2mobiformulaconv.elements.operators;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Mo;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals.Mtext;
import org.jdom2.Element;

/**
 * @author mauss
 *         Created: 21.05.14 00:03
 */
public class Mroot extends Mo {
    private FormulaElement base;
    private FormulaElement index;

    public FormulaElement getIndex() {
        return index;
    }

    public void setIndex(FormulaElement index) {
        this.index = index;
    }

    public FormulaElement getBase() {
        return base;
    }

    public void setBase(FormulaElement base) {
        this.base = base;
    }

    @Override
    public Element render() {
        // TODO mroot
        Element span = new Element("span");
        span.setAttribute("class", "mroot");
        span.addContent(index.render());
        Mtext sqrtSymbol = new Mtext();
        sqrtSymbol.setValue("√");
        span.addContent(sqrtSymbol.render());
        span.addContent(base.render());
        return span;
    }
}
