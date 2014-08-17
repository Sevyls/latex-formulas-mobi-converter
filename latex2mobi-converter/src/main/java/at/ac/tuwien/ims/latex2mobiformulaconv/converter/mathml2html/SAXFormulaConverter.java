package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Formula;
import org.jdom2.Element;
import org.jdom2.Text;

/**
 * @author Michael Auß
 *         Date: 17.08.14
 *         Time: 16:14
 */
public class SAXFormulaConverter extends FormulaConverter {
    @Override
    public Formula parse(int id, String latexFormula) {
        Formula formula = super.parseToMathML(id, latexFormula);

        // TODO convert formula with SAX Parser

        // Test output
        Element html = new Element("span");
        Text text = new Text("Formula #" + formula.getId());
        html.addContent(text);

        formula.setHtml(html);

        return formula;
    }
}
