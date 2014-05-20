package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

import java.util.ArrayList;

/**
 * @author mauss
 *         Created: 20.05.14 23:31
 */
public class Group implements FormulaElement {
    private ArrayList<FormulaElement> elements;

    public Group() {
        elements = new ArrayList<FormulaElement>();
    }

    public ArrayList<FormulaElement> getElements() {
        return elements;
    }

    @Override
    public Element render() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
