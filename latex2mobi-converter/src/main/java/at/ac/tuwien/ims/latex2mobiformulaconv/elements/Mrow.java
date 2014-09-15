package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author mauss
 *         Created: 20.05.14 23:30
 */
public class Mrow implements FormulaElement {
    List<FormulaElement> list = new ArrayList<>();

    public void addElement(FormulaElement element) {
        list.add(element);
    }

    @Override
    public Element render() {
        Element span = new Element("span");
        Iterator<FormulaElement> iterator = list.iterator();
        while (iterator.hasNext()) {
            FormulaElement cur = iterator.next();
            span.addContent(cur.render());
        }
        return span;
    }
}
