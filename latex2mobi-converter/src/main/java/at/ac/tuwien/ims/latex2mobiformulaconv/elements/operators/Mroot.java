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

        Element mrootSpan = new Element("span");
        mrootSpan.setAttribute("class", "mroot");

        // index
        if (index != null) {
            Element indexSpan = new Element("span");
            indexSpan.setAttribute("class", "mroot-index");

            indexSpan.addContent(index.render());

            mrootSpan.addContent(indexSpan);
        }

        // Root symbol
        Element mrootSymbol = new Element("span");
        mrootSymbol.setAttribute("class", "mroot-symbol");
        Mtext sqrtSymbol = new Mtext();
        sqrtSymbol.setValue("√");
        mrootSymbol.addContent(sqrtSymbol.render());
        mrootSpan.addContent(mrootSymbol);


        Element mrootTopbar = new Element("span");
        mrootTopbar.setAttribute("class", "mroot-topbar");

        // base
        Element baseSpan = new Element("span");
        baseSpan.setAttribute("class", "mroot-base");
        baseSpan.addContent(base.render());
        mrootTopbar.addContent(baseSpan);

        mrootSpan.addContent(mrootTopbar);


        return mrootSpan;
    }
}
