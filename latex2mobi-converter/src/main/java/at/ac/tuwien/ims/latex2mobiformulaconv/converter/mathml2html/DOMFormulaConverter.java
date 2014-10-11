package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.*;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals.Mi;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals.Mn;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals.Mspace;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals.Mtext;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.operators.Mroot;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Text;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

/**
 * @author Michael Auß
 *         Date: 17.08.14
 *         Time: 16:14
 */
public class DOMFormulaConverter extends FormulaConverter {
    private boolean debug = false;
    private static Logger logger = Logger.getLogger(DOMFormulaConverter.class);

    public DOMFormulaConverter(boolean debug) {
        this.debug = debug;
    }

    @Override
    public Formula parse(int id, String latexFormula) {
        Formula formula = super.parseToMathML(id, latexFormula);

        Element html = new Element("span");
        html.setAttribute("class", "math");

        SAXBuilder builder = new SAXBuilder();


        try {
            Document mathml = builder.build(new StringReader(formula.getMathMl()));

            Element root = mathml.getRootElement();
            Iterator<Element> it = root.getChildren().iterator();

            while (it.hasNext()) {
                Element cur = it.next();
                FormulaElement formulaElement = renderElement(cur);
                if (formulaElement != null) {
                    Element resultHtml = formulaElement.render();
                    if (resultHtml != null) {
                        html.addContent(resultHtml);
                    } else {
                        logger.debug("HTML is NULL: " + cur.getName());
                    }
                }
            }

        } catch (JDOMException e) {
            logger.error("Error parsing generated MathML:");
            logger.error(formula.getMathMl());
            logger.error(e.getMessage(), e);

        } catch (IOException e) {
            logger.error("Error reading generated MathML:");
            logger.error(formula.getMathMl());
            logger.error(e.getMessage(), e);
        }
        // Test output
        Element span = new Element("span");


        // Generate debug output (image, latex + mathml code)
        if (debug) {
            // Msub
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


    /**
     * Recursive function to render all FormulaElements
     *
     * @param cur the current processed MathML JDOM2 Element (a node or leaf inside MathML's DOM tree)
     * @return an object which implements the FormulaElement interface, so it can be rendered to HTML
     */
    private FormulaElement renderElement(Element cur) {

        String name = cur.getName();
        FormulaElement output;

        // Based on the MathML tag a corresponding class will be chosen and output will be rendered
        switch (name.toLowerCase()) {
            case "msup":
                Msup msup = new Msup();
                msup.setBase(renderElement(cur.getChildren().get(0)));
                msup.setSuperscript(renderElement(cur.getChildren().get(1)));
                output = msup;
                break;
            case "msub":
                Msub msub = new Msub();
                msub.setBase(renderElement(cur.getChildren().get(0)));
                msub.setSubscript(renderElement(cur.getChildren().get(1)));
                output = msub;
                break;
            case "msubsup":
                Msubsup msubsup = new Msubsup();
                msubsup.setBase(renderElement(cur.getChildren().get(0)));
                msubsup.setSubscript(renderElement(cur.getChildren().get(1)));
                msubsup.setSuperscript(renderElement(cur.getChildren().get(2)));
                output = msubsup;
                break;
            case "mrow":
                Mrow mrow = new Mrow();
                Iterator<Element> iterator = cur.getChildren().iterator();

                while (iterator.hasNext()) {
                    Element element = iterator.next();
                    FormulaElement rowElement = renderElement(element);
                    mrow.addElement(rowElement);
                }

                output = mrow;
                break;
            case "mo":
                Mo mo = new Mo();
                mo.setOperator(cur.getText());
                output = mo;
                break;
            case "mn":
                Mn mn = new Mn();
                mn.setValue(cur.getText());
                output = mn;
                break;
            case "mi":
                Mi mi = new Mi();
                mi.setLiteral(cur.getText());
                output = mi;
                break;
            case "mfrac":
                Mfrac mfrac = new Mfrac();
                mfrac.setNumerator(renderElement(cur.getChildren().get(0)));
                mfrac.setDenominator(renderElement(cur.getChildren().get(1)));
                output = mfrac;
                break;
            case "mfenced":
                Mfenced mfenced = new Mfenced();
                mfenced.setOpened(cur.getAttributeValue("open"));
                mfenced.setClosed(cur.getAttributeValue("close"));
                if (cur.getChildren().isEmpty() == false) {
                    mfenced.setContent(renderElement(cur.getChildren().get(0)));
                }
                output = mfenced;
                break;
            case "mspace":
                Mspace mspace = new Mspace();
                output = new Mspace();
                break;
            case "msqrt":
                Mroot msqrt = new Mroot();
                msqrt.setBase(renderElement(cur.getChildren().get(0)));
                Mn square = new Mn();

                msqrt.setIndex(square);
                output = msqrt;
                break;
            case "mroot":
                Mroot mroot = new Mroot();
                mroot.setBase(renderElement(cur.getChildren().get(0)));
                mroot.setIndex(renderElement(cur.getChildren().get(1)));
                output = mroot;
                break;
            case "mtext":
                Mtext mtext = new Mtext();
                mtext.setValue(cur.getText());
                output = mtext;
                break;
            case "mstyle":
                Mstyle mstyle = new Mstyle();

                String mathvariant = cur.getAttributeValue("mathvariant");
                if (mathvariant != null && mathvariant.isEmpty() == false) {
                    mstyle.setStyle(mathvariant);
                }

                Iterator<Element> mstyleIterator = cur.getChildren().iterator();
                while (mstyleIterator.hasNext()) {
                    mstyle.addBaseElement(renderElement(mstyleIterator.next()));
                }
                output = mstyle;
                break;
            case "mover":
                Mover mover = new Mover();
                mover.setBase(renderElement(cur.getChildren().get(0)));
                mover.setOverscript(renderElement(cur.getChildren().get(1)));
                output = mover;
                break;
            case "munder":
                Munder munder = new Munder();
                munder.setBase(renderElement(cur.getChildren().get(0)));
                munder.setUnderscript(renderElement(cur.getChildren().get(1)));
                output = munder;
                break;
            default:
                logger.info("MathML conversion of element <" + cur.getName() + "> NOT YET IMPLEMENTED");
                output = null;
                break;

        }

        return output;
    }
}
