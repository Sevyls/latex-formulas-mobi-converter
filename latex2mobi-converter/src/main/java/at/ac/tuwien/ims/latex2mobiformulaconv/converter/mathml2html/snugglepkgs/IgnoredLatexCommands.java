package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.snugglepkgs;

import uk.ac.ed.ph.snuggletex.SnugglePackage;

/**
 * @author Michael Auß
 *         Date: 02.01.2015
 *         Time: 15:53
 */
public class IgnoredLatexCommands implements SnugglePackageProvider {
    @Override
    public SnugglePackage provide() {
        // TODO
        SnugglePackage setlengthOperator = new SnugglePackage("setlength");
        // \setlength\arraycolsep{0.1em}
        return null;
    }
}
