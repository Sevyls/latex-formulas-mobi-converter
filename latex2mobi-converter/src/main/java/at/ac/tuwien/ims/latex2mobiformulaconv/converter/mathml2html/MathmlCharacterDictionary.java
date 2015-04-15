package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.attributes.Unit;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.token.Mo;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.token.Token;
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
 * @author Michael Auß
 *         Created: 01.04.2015
 */
public class MathmlCharacterDictionary {
    // Directory maps
    public static final HashMap<String, List<Token>> operatorDictionary = new HashMap<>();
    public static final HashMap<String, String> entityMapByName = new HashMap<>();
    public static final HashMap<String, List<String>> entityMapByUnicode = new HashMap<>();
    private static final Logger logger = Logger.getLogger(MathmlCharacterDictionary.class);
    // Resource filenames
    private static final String MATHML_OPERATOR_DICTIONARY_XML = "mathml-operator-dictionary.xml";
    private static final String MATHML_CHARACTERS_BY_NAME_XML = "mathml-characters-by-name.xml";
    private static final String MATHML_CHARACTERS_BY_UNICODE_XML = "mathml-characters-by-unicode.xml";

    /**
     * Initializes MathML2 Operator operatorDictionary & Character Entity mappings
     */
    static {
        // initialize Operator operatorDictionary as described in MathML2 spec
        // http://www.w3.org/TR/MathML2/appendixf.html

        // Read from operatorDictionary xml
        try {
            SAXBuilder builder = new SAXBuilder();

            Document dictionaryXml = builder.build(FormulaElement.class.getClassLoader().getResourceAsStream(MATHML_OPERATOR_DICTIONARY_XML));
            List<Element> operatorElements = dictionaryXml.getRootElement().getChildren();
            logger.debug("Found " + operatorElements.size() + " operators from operatorDictionary.");

            for (Element moElement : operatorElements) {
                Mo element = new Mo();
                element.setValue(StringEscapeUtils.unescapeXml(moElement.getAttributeValue("value")));
                element.setForm(moElement.getAttributeValue("form"));

                // read attributes
                element.setAccent(Boolean.parseBoolean(moElement.getAttributeValue("accent", "false")));
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
                if (operatorDictionary.containsKey(element.getValue())) {
                    operatorDictionary.get(element.getValue()).add(element);
                } else {
                    List<Token> list = new ArrayList<>();
                    list.add(element);
                    operatorDictionary.put(element.getValue(), list);
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

        logger.debug("Operator Dictionary init");

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

                // Deprecated: Decode Unicode value to character, note: currently only UTF-8 values
                //char c = (char) Integer.parseInt(unicode.substring(1), 16);
                //entityMapByName.put(name, Character.toString(c));

                // Decode Unicode to String representation, full support for UTF-8, UTF-16 still not supported
                String output = new String(Character.toChars(Integer.parseInt(unicode.substring(1), 16)));
                entityMapByName.put(name, output);
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
                    entityMapByUnicode.put(Character.toString(c), names);
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
     *
     * @param operator character which represents the operator
     * @param form     MathML Operator form parameter, one of "prefix", "infix" (default) or "postfix"
     * @return Found MathML Operator object with its attributes OR null, when nothing could be found
     */
    public static Mo findOperator(String operator, String form) {
        if (form == null) {
            form = "infix";
        }
        String searchString = operator;

        if (MathmlCharacterDictionary.operatorDictionary.containsKey(searchString) == false && MathmlCharacterDictionary.entityMapByUnicode.containsKey(searchString)) {
            List<String> names = MathmlCharacterDictionary.entityMapByUnicode.get(searchString);

            for (String name : names) {
                if (MathmlCharacterDictionary.operatorDictionary.containsKey(name)) {
                    searchString = name;
                    break;
                }
                if (MathmlCharacterDictionary.operatorDictionary.containsKey("&" + name + ";")) {
                    searchString = "&" + name + ";";
                    break;
                }
            }
        }

        if (MathmlCharacterDictionary.operatorDictionary.containsKey(searchString)) {
            List<Token> list = MathmlCharacterDictionary.operatorDictionary.get(searchString);
            if (list.size() == 1) {
                return (Mo) list.get(0);
            }

            for (Token mo : list) {
                if (((Mo) mo).getForm().equals(form)) {
                    return (Mo) mo;
                }
            }
        }

        return null;
    }

    public static String decodeEntity(String operatorEntity) {
        if (operatorEntity != null && operatorEntity.length() > 1) {

            // check if just the name was given
            if (MathmlCharacterDictionary.entityMapByName.containsKey(operatorEntity)) {
                return MathmlCharacterDictionary.entityMapByName.get(operatorEntity);
            }

            // check if it was a complete entity (i.e. "&OverBrace;")
            String entityName = operatorEntity.substring(1, operatorEntity.length() - 1);

            if (MathmlCharacterDictionary.entityMapByName.containsKey(entityName)) {
                return MathmlCharacterDictionary.entityMapByName.get(entityName);
            }
        }
        return null;
    }
}
