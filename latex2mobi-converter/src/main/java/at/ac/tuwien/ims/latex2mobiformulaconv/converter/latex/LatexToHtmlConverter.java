package at.ac.tuwien.ims.latex2mobiformulaconv.converter.latex;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.ExecOption;
import org.jdom2.Document;

import java.io.File;
import java.nio.file.Path;

/**
 * @author mauss
 *         Created: 21.05.14 00:08
 *         // TODO Documentation
 */
public interface LatexToHtmlConverter extends ExecOption {
    /**
     * // TODO Documentation
     *
     * @return
     */
    public Document convert(File tex, String title, final Path workingDirectory);
}
