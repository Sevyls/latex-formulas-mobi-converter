package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.Formula;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.attributes.Unit;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.layout.*;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.scriptlimit.*;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.tablesmatrices.Mtable;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.tablesmatrices.Mtd;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.tablesmatrices.Mtr;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.token.*;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
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
 */

/**
 *
 *
 * Converts formulas from MathML to a HTML+CSS based representation
 *
 * For supported MathML see the SPECIFICATION.md
 *
 * Implements the FormulaConverter interface
 * @see at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.FormulaConverter
 *
 * @author Michael Auß
 *         Date: 17.08.14
 *         Time: 16:14
 */
public class DOMFormulaConverter extends FormulaConverter {
    private static Logger logger = Logger.getLogger(DOMFormulaConverter.class);

    @Override
    public Formula parse(int id, String latexFormula) {
        Formula formula = super.parseToMathML(id, latexFormula);

        Element html = new Element("div");
        html.setAttribute("class", "math");

        SAXBuilder builder = new SAXBuilder();


        try {
            Document mathml = builder.build(new StringReader(formula.getMathMl()));

            Element root = mathml.getRootElement();
            if (root.getChildren().isEmpty()) {
                return null;
            }

            Iterator<Element> it = root.getChildren().iterator();

            while (it.hasNext()) {
                Element cur = it.next();
                FormulaElement formulaElement = renderElement(cur);
                if (formulaElement != null) {
                    Element resultHtml = formulaElement.render(null, null);
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
        Element div = new Element("div");

        div.addContent(html);

        formula.setHtml(div);

        return formula;
    }


    /**
     * Recursive function to render all FormulaElements
     * Converts MathML to HTML
     *
     * @param cur the current processed MathML JDOM2 Element (a node or leaf inside MathML's DOM tree)
     * @return an object which implements the FormulaElement interface, so it can be rendered to HTML
     */
    private FormulaElement renderElement(Element cur) {

        String name = cur.getName();
        String mathvariant = null;
        FormulaElement output;

        // Based on the MathML tag a corresponding class will be chosen and output will be rendered
        switch (name.toLowerCase()) {

            // Superscripts
            case "msup":
                Msup msup = new Msup();
                msup.setBase(renderElement(cur.getChildren().get(0)));
                msup.setSuperscript(renderElement(cur.getChildren().get(1)));
                output = msup;
                break;

            // Subscripts
            case "msub":
                Msub msub = new Msub();
                msub.setBase(renderElement(cur.getChildren().get(0)));
                msub.setSubscript(renderElement(cur.getChildren().get(1)));
                output = msub;
                break;

            // Subscript-superscript Pairs
            case "msubsup":
                Msubsup msubsup = new Msubsup();
                msubsup.setBase(renderElement(cur.getChildren().get(0)));
                msubsup.setSubscript(renderElement(cur.getChildren().get(1)));
                msubsup.setSuperscript(renderElement(cur.getChildren().get(2)));
                output = msubsup;
                break;

            // Rows
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

            // operators
            case "mo":
                Mo mo;
                String operator = cur.getText();


                // find operator in dictionary
                mo = MathmlCharacterDictionary.findOperator(operator, cur.getAttributeValue("form", "infix"));

                if (mo == null) {
                    mo = new Mo();
                    mo.setValue(operator);

                    // Parse attributes
                    mo.setAccent(Boolean.parseBoolean(cur.getAttributeValue("accent", "false")));
                    mo.setSeparator(Boolean.parseBoolean(cur.getAttributeValue("separator", "false")));
                    mo.setFence(Boolean.parseBoolean(cur.getAttributeValue("fence", "false")));
                    mo.setMovablelimits(Boolean.parseBoolean(cur.getAttributeValue("movablelimits", "false")));
                    mo.setLargeop(Boolean.parseBoolean(cur.getAttributeValue("largeop", "false")));
                    mo.setStretchy(Boolean.parseBoolean(cur.getAttributeValue("stretchy", "false")));

                    mo.setLspace(Unit.parse(cur.getAttributeValue("lspace", "thickmathspace")));
                    mo.setRspace(Unit.parse(cur.getAttributeValue("rspace", "thickmathspace")));
                    mo.setMinsize(Unit.parse(cur.getAttributeValue("minsize")));
                    mo.setMaxsize(Unit.parse(cur.getAttributeValue("maxsize")));
                }
                output = mo;
                break;

            // numbers
            case "mn":
                Mn mn = new Mn();
                mn.setValue(cur.getText());
                output = mn;
                break;

            // identifiers
            case "mi":
                Mi mi = new Mi();
                mi.setValue(cur.getText());
                mathvariant = cur.getAttributeValue("mathvariant");
                if (mathvariant != null  && mathvariant.isEmpty() == false) {
                    mi.setMathvariant(mathvariant);
                    mathvariant = null;
                }
                output = mi;
                break;

            // fractions
            case "mfrac":
                Mfrac mfrac = new Mfrac();
                mfrac.setNumerator(renderElement(cur.getChildren().get(0)));
                mfrac.setDenominator(renderElement(cur.getChildren().get(1)));

                String linethickness = cur.getAttributeValue("linethickness");
                if (linethickness != null) {
                    mfrac.setLinethickness(linethickness);
                }

                output = mfrac;
                break;

            // Expression Inside Pair of Fences
            case "mfenced":
                Mfenced mfenced = new Mfenced();
                mfenced.setOpened(cur.getAttributeValue("open"));
                mfenced.setClosed(cur.getAttributeValue("close"));
                mfenced.setSeparators(cur.getAttributeValue("separators"));

                List<Element> children = cur.getChildren();
                if (children.isEmpty() == false) {
                    List<FormulaElement> renderedChildren = new ArrayList<>();
                    for (Element child : children) {
                        FormulaElement renderedChild = renderElement(child);
                        renderedChildren.add(renderedChild);
                    }
                    mfenced.setContent(renderedChildren);
                }
                output = mfenced;
                break;

            // Space
            case "mspace":
                Mspace mspace = new Mspace();

                // Parse attributes
                String widthAttribute = cur.getAttributeValue("width");
                if (widthAttribute != null) {
                    mspace.setWidth(Unit.parse(widthAttribute));
                }
                String heightAttribute = cur.getAttributeValue("height");
                if (heightAttribute != null) {
                    mspace.setHeight(Unit.parse(heightAttribute));
                }

                // linebreaks will be ignored for now

                output = mspace;
                break;

            // Square root
            case "msqrt":
                Mroot msqrt = new Mroot();
                msqrt.setBase(renderElement(cur.getChildren().get(0)));
                // no index
                output = msqrt;
                break;

            // Root
            case "mroot":
                Mroot mroot = new Mroot();
                mroot.setBase(renderElement(cur.getChildren().get(0)));
                mroot.setDegree(renderElement(cur.getChildren().get(1)));
                output = mroot;
                break;

            // String literal
            case "ms":
                Ms ms = new Ms();
                ms.setValue(cur.getText());
                output = ms;
                break;

            // Text
            case "mtext":
                Mtext mtext = new Mtext();
                mtext.setValue(cur.getText());
                output = mtext;
                break;

            // Style change
            case "mstyle":
                Mstyle mstyle = new Mstyle();

                mathvariant = cur.getAttributeValue("mathvariant");
                if (mathvariant != null && mathvariant.isEmpty() == false) {
                    mstyle.setStyle(mathvariant);
                    mathvariant = null;
                }

                Iterator<Element> mstyleIterator = cur.getChildren().iterator();
                while (mstyleIterator.hasNext()) {
                    mstyle.addBaseElement(renderElement(mstyleIterator.next()));
                }
                output = mstyle;
                break;

            // Overscript
            case "mover":
                Mover mover = new Mover();
                mover.setBase(renderElement(cur.getChildren().get(0)));
                mover.setOverscript(renderElement(cur.getChildren().get(1)));
                output = mover;
                break;

            // Underscript
            case "munder":
                Munder munder = new Munder();
                munder.setBase(renderElement(cur.getChildren().get(0)));
                munder.setUnderscript(renderElement(cur.getChildren().get(1)));
                output = munder;
                break;

            // Matrices & tables
            case "mtable":
                Mtable mtable = new Mtable();
                Iterator<Element> mRowIterator = cur.getChildren().iterator();
                while (mRowIterator.hasNext()) {
                    Mtr mtr = (Mtr) renderElement(mRowIterator.next());
                    mtable.getRows().add(mtr);
                }
                output = mtable;
                break;

            // Table rows
            case "mtr":
                Mtr mtr = new Mtr();
                Iterator<Element> mCellIterator = cur.getChildren().iterator();
                while (mCellIterator.hasNext()) {
                    Mtd mtd = (Mtd) renderElement(mCellIterator.next());
                    mtr.getTds().add(mtd);
                }
                output = mtr;
                break;

            // Table cells
            case "mtd":
                Mtd mtd = new Mtd();
                Mrow tdContent = new Mrow();
                Iterator<Element> mContentIterator = cur.getChildren().iterator();
                while (mContentIterator.hasNext()) {
                    tdContent.addElement(renderElement(mContentIterator.next()));
                }
                mtd.setContent(tdContent);
                output = mtd;
                break;
            default:
                logger.info("MathML conversion of element <" + cur.getName() + "> NOT YET IMPLEMENTED");
                output = null;
                break;

        }

        return output;
    }
}
