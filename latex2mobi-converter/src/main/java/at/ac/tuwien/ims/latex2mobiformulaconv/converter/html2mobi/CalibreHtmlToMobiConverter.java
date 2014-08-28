package at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi;

import org.apache.commons.cli.Option;
import org.jdom2.Document;

import java.io.File;

/**
 * @author mauss
 *         Created: 21.05.14 00:12
 */
public class CalibreHtmlToMobiConverter implements HtmlToMobiConverter {
    @Override
    public File convertToMobi(Document document) {
        // TODO implement
        return null;
    }

    @Override
    public Option getExecOption() {
        // new Option("c", "calibre-exec", true, "Calibre executable location")
        // TODO implement
        return null;
    }
}
