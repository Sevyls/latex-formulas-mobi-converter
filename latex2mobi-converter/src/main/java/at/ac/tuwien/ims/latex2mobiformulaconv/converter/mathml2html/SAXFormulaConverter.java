package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Formula;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.jdom2.Text;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.StringReader;

/**
 * @author Michael Auß
 *         Date: 17.08.14
 *         Time: 16:14
 */
public class SAXFormulaConverter extends FormulaConverter {
    private static Logger logger = Logger.getLogger(SAXFormulaConverter.class);

    @Override
    public Formula parse(int id, String latexFormula) {
        Formula formula = super.parseToMathML(id, latexFormula);

        // TODO convert formula with SAX Parser

        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader parser = factory.createXMLStreamReader(new StringReader(formula.getMathMl()));

            StringBuilder spacer = new StringBuilder();

            while (parser.hasNext()) {
                System.out.println("Event: " + parser.getEventType());

                switch (parser.getEventType()) {
                    case XMLStreamConstants.START_DOCUMENT:
                        System.out.println("START_DOCUMENT: " + parser.getVersion());
                        break;

                    case XMLStreamConstants.END_DOCUMENT:
                        System.out.println("END_DOCUMENT: ");
                        parser.close();
                        break;

                    case XMLStreamConstants.NAMESPACE:
                        System.out.println("NAMESPACE: " + parser.getNamespaceURI());
                        break;

                    case XMLStreamConstants.START_ELEMENT:
                        spacer.append("  ");
                        System.out.println(spacer + "START_ELEMENT: " + parser.getLocalName());

                        // Der Event XMLStreamConstants.ATTRIBUTE wird nicht geliefert!
                        for (int i = 0; i < parser.getAttributeCount(); i++)
                            System.out.println(spacer + "  Attribut: "
                                    + parser.getAttributeLocalName(i)
                                    + " Wert: " + parser.getAttributeValue(i));
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        if (!parser.isWhiteSpace())
                            System.out.println(spacer + "  CHARACTERS: " + parser.getText());
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        System.out.println(spacer + "END_ELEMENT: " + parser.getLocalName());
                        spacer.delete((spacer.length() - 2), spacer.length());
                        break;

                    default:
                        break;
                }
                parser.next();
            }
        } catch (XMLStreamException e) {
            logger.error(e.getMessage(), e);
        }


        // Test output
        Element html = new Element("span");
        Text text = new Text("Formula #" + formula.getId());
        html.addContent(text);

        formula.setHtml(html);

        return formula;
    }
}
