package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

/**
 * @author Michael Auß
 *         Created: 15.09.2014
 */
public class Msub implements FormulaElement {
    public static final char COMMAND = '_';

    public FormulaElement getBase() {
        return base;
    }

    public void setBase(FormulaElement base) {
        this.base = base;
    }

    public FormulaElement getSubscript() {
        return subscript;
    }

    public void setSubscript(FormulaElement subscript) {
        this.subscript = subscript;
    }

    private FormulaElement base;
    private FormulaElement subscript;

    @Override
    public Element render() {
        Element msubSpan = new Element("span");
        msubSpan.setAttribute("class", "msub");

        // Add base content
        msubSpan.addContent(base.render());

        // Add subscript content
        Element sub = new Element("sub");
        sub.addContent(subscript.render());
        msubSpan.addContent(sub);

        return msubSpan;
    }


}
