package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

/**
 * @author mauss
 *         Created: 20.05.14 23:39
 */
public class Mfrac implements FormulaElement {
    private FormulaElement numerator;
    private FormulaElement denominator;

    public FormulaElement getNumerator() {
        return numerator;
    }

    public void setNumerator(FormulaElement numerator) {
        this.numerator = numerator;
    }

    public FormulaElement getDenominator() {
        return denominator;
    }

    public void setDenominator(FormulaElement denominator) {
        this.denominator = denominator;
    }

    @Override
    public Element render() {
        Element fraction = new Element("span");
        fraction.setAttribute("class", "mfrac");

        Element numerator = new Element("span");
        numerator.setAttribute("class", "numerator");
        numerator.addContent(this.numerator.render());

        Element denominator = new Element("span");
        denominator.setAttribute("class", "denominator");
        denominator.addContent(this.denominator.render());

        fraction.addContent(numerator);
        fraction.addContent(denominator);

        return fraction;
    }
}
