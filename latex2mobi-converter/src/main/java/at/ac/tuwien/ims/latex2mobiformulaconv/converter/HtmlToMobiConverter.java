package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import org.jdom2.Document;

import java.io.File;

/**
 * @author mauss
 *         Created: 21.05.14 00:11
 *         // TODO Documentation
 */
public interface HtmlToMobiConverter extends ExecOption {
    /**
     * // TODO Documentation
     *
     * @param document
     * @return
     */
    public File convertToMobi(Document document);
}
