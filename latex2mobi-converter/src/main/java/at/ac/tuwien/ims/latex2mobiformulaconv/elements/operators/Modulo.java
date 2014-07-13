package at.ac.tuwien.ims.latex2mobiformulaconv.elements.operators;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.SnugglePackageProvider;
import org.jdom2.Element;
import uk.ac.ed.ph.snuggletex.SnugglePackage;
import uk.ac.ed.ph.snuggletex.definitions.TextFlowContext;
import uk.ac.ed.ph.snuggletex.dombuilding.InterpretableSimpleMathHandler;
import uk.ac.ed.ph.snuggletex.semantics.MathOperatorInterpretation;

import static uk.ac.ed.ph.snuggletex.definitions.Globals.ALL_MODES;

/**
 * @author mauss
 *         Created: 13.07.14 16:57
 */
public class Modulo extends Operator implements SnugglePackageProvider {

    @Override
    public Element render() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public SnugglePackage provide() {

        SnugglePackage modOperator = new SnugglePackage("mod");
        MathOperatorInterpretation bmodInterpretation = new MathOperatorInterpretation("mod");
        modOperator.addSimpleCommand("bmod", ALL_MODES, bmodInterpretation, new InterpretableSimpleMathHandler(), TextFlowContext.ALLOW_INLINE);

        return modOperator;
    }
}
