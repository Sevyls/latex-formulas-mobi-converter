package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

/**
 * @author Michael Auß
 *         Created: 15.09.2014
 */
public class Msup implements FormulaElement {
    public static final char COMMAND = '_';

    public FormulaElement getBase() {
        return base;
    }

    public void setBase(FormulaElement base) {
        this.base = base;
    }

    public FormulaElement getSuperscript() {
        return superscript;
    }

    public void setSuperscript(FormulaElement superscript) {
        this.superscript = superscript;
    }

    private FormulaElement base;
    private FormulaElement superscript;

    @Override
    public Element render() {
        Element msuperSpan = new Element("span");
        msuperSpan.setAttribute("class", "msup");

        // Add base content
        if (base != null) {
            Element baseElement = base.render();
            msuperSpan.addContent(baseElement);
        }

        // Add superscript content
        Element sub = new Element("sup");
        sub.addContent(superscript.render());
        msuperSpan.addContent(sub);

        return msuperSpan;
    }


}
