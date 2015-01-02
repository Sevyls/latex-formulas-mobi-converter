package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Auß
 *         Date: 26.11.2014
 *         Time: 21:44
 */
public class Mtr implements FormulaElement {
    private List<Mtd> tds = new ArrayList<Mtd>();

    public List<Mtd> getTds() {
        return tds;
    }

    @Override
    public Element render(FormulaElement parent, List<FormulaElement> siblings) {
        Element tr = new Element("tr");
        for (int i = 0; i < tds.size(); i++) {
            tr.addContent(tds.get(i).render(null, null));
        }
        return tr;
    }
}