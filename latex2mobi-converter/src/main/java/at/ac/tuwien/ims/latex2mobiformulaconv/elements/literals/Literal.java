package at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.FormulaElement;

/**
 * @author mauss
 *         Created: 20.05.14 23:39
 */
public abstract class Literal implements FormulaElement {
    public abstract Object getValue();

    public abstract void setValue(Object value);
}
