package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

/**
 * @author Michael Auß
 *         Date: 17.09.2014
 */
public class Msubsup implements FormulaElement {
    private FormulaElement base;
    private FormulaElement subscript;
    private FormulaElement superscript;

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

    public FormulaElement getSuperscript() {
        return superscript;
    }

    public void setSuperscript(FormulaElement superscript) {
        this.superscript = superscript;
    }

    @Override
    public Element render() {
        Element msubsupSpan = new Element("span");
        msubsupSpan.setAttribute("class", "msubsup");

        // Add base content
        msubsupSpan.addContent(base.render());

        // Add subscript content
        Element sup = new Element("sup");
        sup.addContent(superscript.render());
        msubsupSpan.addContent(sup);

        // Add superscript content
        Element sub = new Element("sub");
        sub.addContent(subscript.render());
        msubsupSpan.addContent(sub);

        return msubsupSpan;
    }
}
