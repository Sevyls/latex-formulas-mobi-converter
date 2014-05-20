package at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals;

import org.jdom2.Element;

/**
 * @author mauss
 *         Created: 20.05.14 23:43
 */
public class Symbol extends Literal {
    private Character value;

    @Override
    public Character getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (Character) value;
    }

    @Override
    public Element render() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
