package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

import org.apache.commons.cli.Option;
import org.jdom2.Document;

import java.io.File;

/**
 * @author mauss
 *         Created: 21.05.14 00:13
 *         // TODO Documentation
 */
public class AmazonHtmlToMobiConverter implements HtmlToMobiConverter {
    @Override
    public File convertToMobi(Document document) {
        return null;  // TODO convertToMobi()
    }


    @Override
    public Option getExecOption() {
        Option option = new Option("k", "kindlegen-exec", true, "Amazon KindleGen executable location");
        option.setArgs(1);
        return option;
    }
}
