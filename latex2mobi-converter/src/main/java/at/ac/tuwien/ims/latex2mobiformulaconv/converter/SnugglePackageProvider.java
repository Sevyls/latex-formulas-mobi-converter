package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import uk.ac.ed.ph.snuggletex.SnugglePackage;

/**
 * @author mauss
 *         Created: 13.07.14 17:01
 */
public interface SnugglePackageProvider {

    /**
     * Provide a SnugglePackage implementation for certain missing LaTeX formula objects
     *
     * @return SnugglePackage which can be added to the Runtime's Snuggle Engine
     */
    public SnugglePackage provide();

}
