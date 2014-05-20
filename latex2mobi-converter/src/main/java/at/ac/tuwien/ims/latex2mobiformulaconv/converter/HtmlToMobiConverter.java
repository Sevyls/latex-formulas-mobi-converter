package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import org.jdom2.Document;

import java.io.File;

/**
 * @author mauss
 *         Created: 21.05.14 00:11
 */
public interface HtmlToMobiConverter {
    public File convertToMobi(Document document);
}
