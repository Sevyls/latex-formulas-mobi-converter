package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import org.apache.commons.cli.Option;
import org.jdom2.Document;

import java.io.File;
import java.nio.file.Path;

/**
 * @author mauss
 *         Created: 21.05.14 00:12
 */
public class CalibreHtmlToMobiConverter implements HtmlToMobiConverter {
    @Override
    public File convertToMobi(Document document, Path tempDirPath) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Option getExecOption() {
        // new Option("c", "calibre-exec", true, "Calibre executable location")
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
