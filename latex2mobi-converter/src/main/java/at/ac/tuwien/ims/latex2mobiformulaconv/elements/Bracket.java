package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

/**
 * @author mauss
 *         Created: 20.05.14 23:34
 */
public class Bracket implements FormulaElement {
    private Group group;

    public Bracket() {
        group = new Group();
    }

    public Group getGroup() {
        return group;
    }

    @Override
    public Element render() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
