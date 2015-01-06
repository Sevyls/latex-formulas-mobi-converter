package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.token;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.attributes.Unit;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.layout.Mrow;
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
    private static Logger logger = Logger.getLogger(Mo.class);

    private static final String MATHML_OPERATOR_DICTIONARY_XML = "mathml-operator-dictionary.xml";
    private static final String MATHML_CHARACTERS_BY_NAME_XML = "mathml-characters-by-name.xml";
    private static final String MATHML_CHARACTERS_BY_UNICODE_XML = "mathml-characters-by-unicode.xml";

    private static HashMap<String, List<Mo>> dictionary = new HashMap<>();
    private static HashMap<String, String> mathmlEntityMapByName = new HashMap<>();
    private static HashMap<String, List<String>> mathmlEntityMapByUnicode = new HashMap<>();

    /**
     * Initializes MathML2 Operator dictionary & Character Entity mappings
     */
    static {
        // initialize Operator dictionary as described in MathML2 spec
        // http://www.w3.org/TR/MathML2/appendixf.html

        // Read from dictionary xml
        try {
            SAXBuilder builder = new SAXBuilder();

            Document dictionaryXml = builder.build(FormulaElement.class.getClassLoader().getResourceAsStream(MATHML_OPERATOR_DICTIONARY_XML));
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

                element.setLspace(Unit.parse(moElement.getAttributeValue("lspace", "thickmathspace")));
                element.setRspace(Unit.parse(moElement.getAttributeValue("rspace", "thickmathspace")));

                element.setMinsize(Unit.parse(moElement.getAttributeValue("minsize")));
                element.setMaxsize(Unit.parse(moElement.getAttributeValue("maxsize")));


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
            logger.error("Missing required system file " + MATHML_OPERATOR_DICTIONARY_XML);
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        logger.debug("Dictionary init");

        // MathML2 Character Entity Mapping by name
        // http://www.w3.org/TR/MathML2/byalpha.html
        try {
            SAXBuilder builder = new SAXBuilder();

            //Document characterXml = builder.build(WorkingDirectoryResolver.getWorkingDirectory(FormulaElement.class.getClassLoader()).resolve(MATHML_CHARACTERS_BY_NAME_XML).toFile());

            Document characterXml = builder.build(FormulaElement.class.getClassLoader().getResourceAsStream(MATHML_CHARACTERS_BY_NAME_XML));
            List<Element> characterElements = characterXml.getRootElement().getChildren();

            for (Element characterElement : characterElements) {
                String name = characterElement.getAttributeValue("name");
                String unicode = characterElement.getAttributeValue("unicode");

                // Decode Unicode value to character, note: currently only UTF-8 values
                char c = (char) Integer.parseInt(unicode.substring(1), 16);
                mathmlEntityMapByName.put(name, Character.toString(c));
            }
            logger.debug("Character Map by Name init");
        } catch (JDOMException e) {
            logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            logger.error("Missing required system file " + MATHML_CHARACTERS_BY_NAME_XML);
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }

        // MathML2 Character Entity Mapping by Unicode
        // http://www.w3.org/TR/MathML2/bycodes.html
        try {
            SAXBuilder builder = new SAXBuilder();

            Document characterXml = builder.build(FormulaElement.class.getClassLoader().getResourceAsStream(MATHML_CHARACTERS_BY_UNICODE_XML));
            List<Element> characterElements = characterXml.getRootElement().getChildren();

            for (Element characterElement : characterElements) {
                String unicode = characterElement.getAttributeValue("unicode");

                // Decode Unicode value to character, note: currently only UTF-8 values
                char c = (char) Integer.parseInt(unicode.substring(1), 16);

                List<Element> nameElements = characterElement.getChildren();
                if (nameElements.isEmpty() == false) {
                    List<String> names = new ArrayList<>();
                    for (Element nameTag : nameElements) {
                        names.add(nameTag.getText());
                    }
                    mathmlEntityMapByUnicode.put(Character.toString(c), names);
                }
            }
            logger.debug("Character Map by Unicode init");
        } catch (JDOMException e) {
            logger.error(e.getMessage(), e);
        } catch (FileNotFoundException e) {
            logger.error("Missing required system file " + MATHML_CHARACTERS_BY_UNICODE_XML);
            logger.error(e.getMessage(), e);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * Searches for an operator inside the MathML Operator dictionary
     * @param operator character which represents the operator
     * @param form MathML Operator form parameter, one of "prefix", "infix" (default) or "postfix"
     * @return Found MathML Operator object with its attributes OR null, when nothing could be found
     */
    public static Mo findInDictionary(String operator, String form) {
        if (form == null) {
            form = "infix";
        }
        String searchString = operator;
        if (dictionary.containsKey(searchString) == false && mathmlEntityMapByUnicode.containsKey(searchString)) {
            List<String> names = mathmlEntityMapByUnicode.get(searchString);

            for (String name : names) {
                if (dictionary.containsKey(name)) {
                    searchString = name;
                    break;
                }
                if (dictionary.containsKey("&" + name + ";")) {
                    searchString = "&" + name + ";";
                    break;
                }
            }
        }

        if (dictionary.containsKey(searchString)) {
            List<Mo> list = dictionary.get(searchString);
            if (list.size() == 1) {
                return list.get(0);
            }

            for (Mo mo : list) {
                if (mo.getForm().equals(form)) {
                    return mo;
                }
            }
        }

        return null;
    }

    private String operator;
    private String form = "infix";
    private boolean fence = false;
    private boolean stretchy = false;
    private boolean separator = false;
    private boolean largeop = false;
    private boolean movablelimits = false;
    private boolean accent = false;

    private Unit maxsize = new Unit(Double.POSITIVE_INFINITY, null);
    private Unit minsize = new Unit(1.0, null);

    public Unit getMaxsize() {
        return maxsize;
    }

    public void setMaxsize(Unit maxsize) {
        this.maxsize = maxsize;
    }

    public Unit getMinsize() {
        return minsize;
    }

    public void setMinsize(Unit minsize) {
        this.minsize = minsize;
    }

    private Unit lspace = Unit.parse("thickmathspace");
    private Unit rspace = Unit.parse("thickmathspace");


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

    public Unit getLspace() {
        return lspace;
    }

    public void setLspace(Unit lspace) {
        this.lspace = lspace;
    }

    public Unit getRspace() {
        return rspace;
    }

    public void setRspace(Unit rspace) {
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
    public String toString() {
        return "Mo{" +
                "operator='" + operator + '\'' +
                ", form='" + form + '\'' +
                ", fence=" + fence +
                ", stretchy=" + stretchy +
                ", separator=" + separator +
                ", largeop=" + largeop +
                ", movablelimits=" + movablelimits +
                ", accent=" + accent +
                ", maxsize=" + maxsize +
                ", minsize=" + minsize +
                ", lspace=" + lspace +
                ", rspace=" + rspace +
                '}';
    }

    @Override
    public Element render(FormulaElement parent, List<FormulaElement> siblings) {
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

        // spacing
        String css = "padding-left: " + lspace.toString() + "; padding-right: " + rspace.toString() + ";";
        moSpan.setAttribute("style", css);

        String output = operator;
        if (output.length() > 1) {
            String entityName = output.substring(1, output.length() - 1);
            if (mathmlEntityMapByName.containsKey(entityName)) {
                output = mathmlEntityMapByName.get(entityName);
            }
        }

        moSpan.addContent(output.trim());

        return moSpan;
    }
}
