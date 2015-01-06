package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.Formula;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.snugglepkgs.SnugglePackageRegistry;
import org.apache.log4j.Logger;
import org.jdom2.Content;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.filter.Filters;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import uk.ac.ed.ph.snuggletex.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The MIT License (MIT)
 * latex2mobi -- LaTeX Formulas to Mobi Converter
 * Copyright (c) 2014 Michael Auß
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 * <p/>
 * <p/>
 * For Third Party Software Licenses read LICENSE file in base dir.
 *
 * @author mauss
 *         Date: 08.06.14
 */
public abstract class FormulaConverter {
    public static final String FORMULA_ID_PREFIX = "formula_";
    private static XPathFactory xPathFactory = XPathFactory.instance();
    protected static XPathExpression<Element> xpath = xPathFactory.compile("//*[@class='LaTeX']", Filters.element());
    private static Logger logger = Logger.getLogger(FormulaConverter.class);
    protected static SnuggleEngine engine = new SnuggleEngine();

    static {
        // Add special SnuggleTeX configuration for certain Elements
        for (SnugglePackage p : SnugglePackageRegistry.getPackages()) {
            engine.addPackage(p);
        }

        XMLStringOutputOptions xmlStringOutputOptions = new XMLStringOutputOptions();
        xmlStringOutputOptions.setEncoding("UTF-8");
        xmlStringOutputOptions.setIndenting(true);
        engine.setDefaultXMLStringOutputOptions(xmlStringOutputOptions);
    }

    private boolean debug = false;

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    /**
     * Parse latex formula code to entities, which afterward can be rendered to html
     *
     * @param latexFormula
     * @return Parsed Formula root object, tree-like representation of formula for further html rendering
     */
    public abstract Formula parse(int id, String latexFormula);

    /**
     * Parses a latex formula to MathML with SnuggleTeX
     *
     * @param id           the formula's index
     * @param latexFormula the latex formula string
     * @return formula object with set id, latex and mathml parameters
     */
    public Formula parseToMathML(int id, String latexFormula) {
        Formula formula = new Formula(id);

        formula.setLatexCode(latexFormula);

        // Parse MathML formula and convert to png file
        SnuggleInput input = new SnuggleInput(latexFormula);
        try {

            SnuggleSession session = engine.createSession();
            session.parseInput(input);
            String xmlString = session.buildXMLString();

            // TODO ignore empty formulas

            formula.setMathMl(xmlString);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return formula;
    }

    /**
     * Parses a JDOM HTML Document for formula entries, sets an id to refer to it in the future.
     *
     * @param document JDOM HTML Document to parse
     * @return Map of formulas, keys: given id, values: corresponding latex formula code from the document
     */
    public Map<Integer, String> extractFormulas(Document document) {
        Map<Integer, String> formulas = new HashMap<>();

        List<Element> foundElements = xpath.evaluate(document);
        if (foundElements.size() > 0) {
            int id = 0;
            for (Element element : foundElements) {
                formulas.put(id, element.getValue());

                // mark formula number
                element.setAttribute("id", FORMULA_ID_PREFIX + id);
                id++;
            }
        }

        return formulas;
    }

    /**
     * Replaces all formulas with the html representation of the mapped formula objects
     *
     * @param doc        JDOM Document where to replace the formulas
     * @param formulaMap Map of the indexed Formula Objects
     * @return JDOM Document with replaced formulas
     */
    public Document replaceFormulas(Document doc, Map<Integer, Formula> formulaMap) {
        List<Element> foundElements = xpath.evaluate(doc);

        if (foundElements.size() > 0) {
            Map<String, Element> elementMap = new HashMap<>();

            for (Element element : foundElements) {
                elementMap.put(element.getAttribute("id").getValue(), element);
            }

            // Replace all found formulas
            Iterator<Integer> formulaIterator = formulaMap.keySet().iterator();
            while (formulaIterator.hasNext()) {
                Integer id = formulaIterator.next();

                Element element = elementMap.get(FORMULA_ID_PREFIX + id);
                Formula formula = formulaMap.get(id);

                element.removeAttribute("class");
                element.removeContent();
                element.setName("div");

                Element div = (Element) element.getParent();
                div.setName("div");
                div.setAttribute("class", "formula");

                // Potentially there's text inside the paragraph...
                List<Text> texts = div.getContent(Filters.textOnly());
                if (texts.isEmpty() == false) {
                    String textString = "";
                    for (Text text : texts) {
                        textString += text.getText();
                    }
                    Element textSpan = new Element("span");
                    textSpan.setAttribute("class", "text");
                    textSpan.setText(textString);
                    div.addContent(textSpan);

                    List<Content> content = div.getContent();
                    content.removeAll(texts);
                }


                if (debug) {
                    div.setAttribute("style", "border: 1px solid black;");

                    Element h4 = new Element("h4");
                    h4.setText("DEBUG - Formula #" + formula.getId());
                    div.addContent(h4);

                    Element latexPre = new Element("pre");
                    latexPre.setAttribute("class", "debug-latex");
                    latexPre.setText(formula.getLatexCode());
                    div.addContent(latexPre);

                    Element mathmlPre = new Element("pre");
                    mathmlPre.setAttribute("class", "debug-mathml");
                    mathmlPre.setText(formula.getMathMl());
                    div.addContent(mathmlPre);

                    Element htmlPre = new Element("pre");
                    htmlPre.setAttribute("class", "debug-html");
                    XMLOutputter xmlOutputter = new XMLOutputter();
                    xmlOutputter.setFormat(Format.getRawFormat());
                    htmlPre.setText(xmlOutputter.outputString(formula.getHtml()));

                    div.addContent(htmlPre);

                }

                element.addContent(formula.getHtml());
            }
        }
        return doc;
    }
}
