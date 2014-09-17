package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.apache.log4j.Logger;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author mauss
 *         Created: 20.05.14 23:30
 */
public class Mrow implements FormulaElement {
    private static Logger logger = Logger.getLogger(Mrow.class);

    List<FormulaElement> list = new ArrayList<>();

    public void addElement(FormulaElement element) {
        list.add(element);
    }

    @Override
    public Element render() {
        Element span = new Element("span");
        span.setAttribute("class", "mrow");
        Iterator<FormulaElement> iterator = list.iterator();
        while (iterator.hasNext()) {
            FormulaElement cur = iterator.next();
            Element html = cur.render();
            if (html != null) {
                span.addContent(html);
            } else {
                logger.debug("HTML is NULL: " + cur.getClass().toString());
            }
        }
        return span;
    }
}
