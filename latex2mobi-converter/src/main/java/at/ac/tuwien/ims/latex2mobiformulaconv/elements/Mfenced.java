package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

/**
 * @author Michael Auß
 *         Created: 15.09.2014
 */
public class Mfenced implements FormulaElement {
    private String opened;
    private String closed;
    // todo separator
    private FormulaElement content;

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    @Override
    public Element render() {
        Element fencedSpan = new Element("span");
        fencedSpan.setAttribute("class", "mfenced");

        fencedSpan.addContent(opened);
        fencedSpan.addContent(content.render());
        fencedSpan.addContent(closed);

        return fencedSpan;
    }
}
