package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

import java.util.List;

/**
 * @author Michael Auß
 *         Date: 26.11.2014
 *         Time: 22:19
 */
public class Mtd implements FormulaElement {
    private FormulaElement content = new Mrow();

    public void setContent(FormulaElement content) {
        this.content = content;
    }

    @Override
    public Element render(FormulaElement parent, List<FormulaElement> siblings) {
        Element td = new Element("td");

        td.addContent(content.render(null, null));

        return td;
    }
}
