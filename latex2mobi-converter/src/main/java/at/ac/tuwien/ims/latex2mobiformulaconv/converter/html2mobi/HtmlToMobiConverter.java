package at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.ExecOption;
import org.jdom2.Document;

import java.io.File;
import java.nio.file.Path;

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
     * @param tempDirPath
     * @return
     */
    public File convertToMobi(Document document, Path tempDirPath);
}
