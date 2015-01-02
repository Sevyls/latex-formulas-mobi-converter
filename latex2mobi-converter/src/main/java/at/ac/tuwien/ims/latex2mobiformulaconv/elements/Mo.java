package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals.Mspace;
import at.ac.tuwien.ims.latex2mobiformulaconv.utils.WorkingDirectoryResolver;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
 * @author Michael Auß
 *         Created: 15.09.2014
 */
public class Mo implements FormulaElement {
    private static final String FILENAME = "mathml-operator-dictionary.xml";
    private static Logger logger = Logger.getLogger(Mo.class);
    private static HashMap<String, List<Mo>> dictionary = new HashMap<>();



    static {
        // initialize Operator dictionary as described in MathML2 spec
        // http://www.w3.org/TR/MathML2/appendixf.html

        // Read from dictionary xml
        try {
            SAXBuilder builder = new SAXBuilder();
            Document dictionaryXml = builder.build(WorkingDirectoryResolver.getWorkingDirectory(Mo.class).resolve(FILENAME).toFile());
            List<Element> operatorElements = dictionaryXml.getRootElement().getChildren();
            logger.debug("Found " + operatorElements.size() + " operators from dictionary.");

            for (Element moElement : operatorElements) {
                Mo element = new Mo();
                element.setOperator(StringEscapeUtils.unescapeXml(moElement.getAttributeValue("value")));
                element.setForm(moElement.getAttributeValue("form"));

                // read attributes
                element.setAccent(Boolean.parseBoolean(moElement.getAttributeValue("accent","false")));
                element.setMovablelimits(Boolean.parseBoolean(moElement.getAttributeValue("movablelimits", "false")));
                element.setStretchy(Boolean.parseBoolean(moElement.getAttributeValue("stretchy", "false")));
                element.setLargeop(Boolean.parseBoolean(moElement.getAttributeValue("largeop", "false")));
                element.setSeparator(Boolean.parseBoolean(moElement.getAttributeValue("separator", "false")));
                element.setFence(Boolean.parseBoolean(moElement.getAttributeValue("fence", "false")));

                element.setLspace(moElement.getAttributeValue("lspace"));
                element.setRspace(moElement.getAttributeValue("rspace"));



                // keep lists of all forms of operators
                if (dictionary.containsKey(element.getOperator())) {
                    dictionary.get(element.getOperator()).add(element);
                } else {
                    List<Mo> list = new ArrayList<>();
                    list.add(element);
                    dictionary.put(element.getOperator(), list);
                }
            }

        } catch (JDOMException e) {
            logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            logger.error("Missing required system file " + FILENAME);
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        logger.debug("Dictionary init");
    }

    private String operator;
    private String form = "infix";
    private boolean fence = false;
    private boolean stretchy = false;
    private boolean separator = false;
    private boolean largeop = false;
    private boolean movablelimits = false;
    private boolean accent = false;

    private String lspace = null;
    private String rspace = null;

    public boolean isFence() {
        return fence;
    }

    public void setFence(boolean fence) {
        this.fence = fence;
    }

    public boolean isStretchy() {
        return stretchy;
    }

    public void setStretchy(boolean stretchy) {
        this.stretchy = stretchy;
    }

    public boolean isSeparator() {
        return separator;
    }

    public void setSeparator(boolean separator) {
        this.separator = separator;
    }

    public boolean isLargeop() {
        return largeop;
    }

    public void setLargeop(boolean largeop) {
        this.largeop = largeop;
    }

    public boolean isMovablelimits() {
        return movablelimits;
    }

    public void setMovablelimits(boolean movablelimits) {
        this.movablelimits = movablelimits;
    }

    public boolean isAccent() {
        return accent;
    }

    public void setAccent(boolean accent) {
        this.accent = accent;
    }

    public String getLspace() {
        return lspace;
    }

    public void setLspace(String lspace) {
        this.lspace = lspace;
    }

    public String getRspace() {
        return rspace;
    }

    public void setRspace(String rspace) {
        this.rspace = rspace;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    @Override
    public Element render(FormulaElement parent, List<FormulaElement> siblings) {
        if (dictionary.containsKey(operator) == false) {
            logger.debug("Operator not found in dictionary: " + operator);
        }

        // Mrow default form behaviour according to W3C MathML2
        // 3.2.5.7.2 Default value of the form attribute
        // http://www.w3.org/TR/MathML2/chapter3.html#presm.formdefval
        if (parent != null && parent.getClass() == Mrow.class && siblings != null && siblings.size() > 1) {
            List<FormulaElement> siblingsWithoutMspace = new ArrayList<>();
            for (FormulaElement e : siblings) {
                if (e.getClass() != Mspace.class) {
                    siblingsWithoutMspace.add(e);
                }
            }

            Integer position = siblingsWithoutMspace.indexOf(this);

            if (this.separator) {
                this.form = "infix";
            }

            if (this.fence) {
                // TODO
            }

            if (position == 0) {
                this.form = "prefix";
            } else if (position == siblingsWithoutMspace.size() - 1) {
                this.form = "postfix";
            }


        }

        // Render operator
        Element moSpan = new Element("span");
        moSpan.setAttribute("class", "mo");


        // Spacing depends on form attribute
        String text;
        switch (this.form) {
            case "prefix":
                text = " " + operator;
                break;
            case "postfix":
                text = operator + " ";
                break;
            case "infix":
            default:
                text = " " + operator + " ";
                break;
        }
        moSpan.addContent(text);


        return moSpan;
    }
}
