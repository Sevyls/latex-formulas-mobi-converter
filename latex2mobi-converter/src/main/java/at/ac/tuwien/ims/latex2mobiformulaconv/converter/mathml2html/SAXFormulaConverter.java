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
import java.util.Stack;

/**
 * @author Michael Auß
 *         Date: 17.08.14
 *         Time: 16:14
 */
public class SAXFormulaConverter extends FormulaConverter {
    private boolean debug = false;
    private static Logger logger = Logger.getLogger(SAXFormulaConverter.class);

    public SAXFormulaConverter(boolean debug) {
        this.debug = debug;
    }

    @Override
    public Formula parse(int id, String latexFormula) {
        Formula formula = super.parseToMathML(id, latexFormula);

        // TODO convert formula with SAX Parser

        XMLInputFactory factory = XMLInputFactory.newInstance();
        Element html = new Element("span");
        html.setAttribute("class", "math");

        try {
            XMLStreamReader parser = factory.createXMLStreamReader(new StringReader(formula.getMathMl()));

            StringBuilder spacer = new StringBuilder();


            Stack<Element> elementStack = new Stack<>();
            Element cur = null;
            while (parser.hasNext()) {
                //logger.debug("Event: " + parser.getEventType());

                switch (parser.getEventType()) {
                    case XMLStreamConstants.START_DOCUMENT:
                        logger.debug("START_DOCUMENT: " + parser.getVersion());
                        break;

                    case XMLStreamConstants.END_DOCUMENT:
                        logger.debug("END_DOCUMENT");
                        parser.close();
                        break;

                    /*case XMLStreamConstants.NAMESPACE:
                        System.out.println("NAMESPACE: " + parser.getNamespaceURI());
                        break;*/

                    case XMLStreamConstants.START_ELEMENT:
                        spacer.append("  ");

                        String name = parser.getLocalName();
                        logger.debug(spacer + "START_ELEMENT: " + name);


                        // ignore root node
                        if (name == "math") {
                            parser.next();
                            continue;
                        }

                        cur = new Element("span");
                        cur.setAttribute("class", name);
                        elementStack.push(cur);

                        switch (name) {

                            default:
                                logger.debug("Not yet implemented: " + name);
                                break;
                        }

                        html.addContent(cur);

                        // Der Event XMLStreamConstants.ATTRIBUTE wird nicht geliefert!
                        for (int i = 0; i < parser.getAttributeCount(); i++)
                            logger.debug(spacer + "  Attribut: "
                                    + parser.getAttributeLocalName(i)
                                    + " Wert: " + parser.getAttributeValue(i));
                        break;

                    case XMLStreamConstants.CHARACTERS:
                        if (!parser.isWhiteSpace()) {
                            cur = elementStack.peek();
                            Text text = new Text(parser.getText());
                            cur.addContent(text);

                            logger.debug(spacer + "  CHARACTERS: " + parser.getText());
                        } else {
                            if (elementStack.isEmpty() == false) {
                                cur = elementStack.peek();
                                Text text = new Text(" ");
                                html.addContent(text);
                            }
                        }
                        break;

                    case XMLStreamConstants.END_ELEMENT:
                        name = parser.getLocalName();
                        logger.debug(spacer + "START_ELEMENT: " + name);

                        // ignore root node
                        if (name == "math") {
                            parser.next();
                            continue;
                        }

                        elementStack.pop();
                        cur = null;

                        logger.debug(spacer + "END_ELEMENT: " + parser.getLocalName());
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
        Element span = new Element("span");


        // Generate debug output (image, latex + mathml code)
        if (debug) {
            // Index
            Element index = new Element("span");
            Text text = new Text("Formula #" + formula.getId());
            index.addContent(text);
            span.addContent(index);

            Element br = new Element("br");
            span.addContent(br);

            // LaTeX
            Element latex = new Element("code");
            Text latexText = new Text(formula.getLatexCode());
            latex.addContent(latexText);
            span.addContent(latex);

            // Image
            Element image = new Element("code");
            ImageFormulaConverter imageFormulaConverter = new ImageFormulaConverter();
            Formula imageFormula = imageFormulaConverter.parse(formula.getId(), formula.getLatexCode());
            image.addContent(imageFormula.getHtml());
            span.addContent(image);

            // MathML
            Element mathml = new Element("code");
            Text mathmlText = new Text(formula.getMathMl());
            mathml.addContent(mathmlText);
            span.addContent(mathml);
        }

        span.addContent(html);


        formula.setHtml(span);

        return formula;
    }
}
