package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import org.jdom2.Document;

import java.io.File;

/**
 * @author mauss
 *         Created: 21.05.14 00:08
 */
public interface LatexToHtmlConverter {
    public Document convert(File tex);
}
