package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.layout;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.MathmlCharacterDictionary;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.tablesmatrices.Mtable;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.token.Mo;
import org.jdom2.Element;

import java.util.ArrayList;
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
 * Renders a MathML Mfenced tag
 * <p/>
 * http://www.w3.org/TR/MathML2/chapter3.html#presm.mfenced
 *
 * @author Michael Auß
 *         Created: 15.09.2014
 */
public class Mfenced implements FormulaElement {
    private static final String SEPARATOR = ",";

    private String opened;
    private String closed;
    private List<FormulaElement> content;
    private String separators;

    public Mfenced() {
        // default values
        this.opened = "(";
        this.closed = ")";
        this.separators = SEPARATOR;
        this.content = new ArrayList<>();
    }

    public String getSeparators() {
        return separators;
    }

    public void setSeparators(String separators) {
        if (separators != null) {
            this.separators = separators;
        } else {
            this.separators = SEPARATOR;
        }
    }

    public List<FormulaElement> getContent() {
        return content;
    }

    public void setContent(List<FormulaElement> content) {
        this.content = content;
    }

    public String getOpened() {
        return opened;
    }

    public void setOpened(String opened) {
        this.opened = opened;
    }

    public String getClosed() {
        return closed;
    }

    public void setClosed(String closed) {
        this.closed = closed;
    }

    @Override
    public Element render(FormulaElement parent, List<FormulaElement> siblings) {
        Element fencedSpan = new Element("span");
        fencedSpan.setAttribute("class", "mfenced");

        // Opening fence
        Element openFenceSpan = new Element("span");
        openFenceSpan.setAttribute("class", "mfenced-open");
        openFenceSpan.setText(opened);
        fencedSpan.addContent(openFenceSpan);


        // Content
        if (content.isEmpty() == false) {

            // if this is a fenced table or matrix, just render the content element
            // pass information about the fence via parent reference
            if (content.size() == 1 &&
                    content.get(0) instanceof Mtable) {
                Mtable fencedTableOrMatrix = (Mtable) content.get(0);
                return fencedTableOrMatrix.render(this, null);
            }


            String tempSeparators = separators.replaceAll(" ", "");

            for (int i = 0; i < content.size(); i++) {
                FormulaElement element = content.get(i);

                Element contentSpan = new Element("span");
                contentSpan.setAttribute("class", "mfenced-content");
                contentSpan.addContent(element.render(null, null));
                fencedSpan.addContent(contentSpan);

                // Separators
                if (content.size() > 1) {
                    Mo separatorElement = new Mo();
                    separatorElement.setSeparator(true);

                    String separator = SEPARATOR;
                    if (tempSeparators.length() == 1) {
                        separator = tempSeparators;
                    } else if (i < tempSeparators.length()) {
                        separator = Character.toString(tempSeparators.charAt(i));

                        // Entity lookup
                        if (separator.length() > 1) {
                            String entityName = separator.substring(1, separator.length() - 1);
                            if (MathmlCharacterDictionary.entityMapByName.containsKey(entityName)) {
                                separator = MathmlCharacterDictionary.entityMapByName.get(entityName);
                            }
                        }
                    }

                    separatorElement.setValue(separator);

                    Element mo = separatorElement.render(this, null);
                    mo.setAttribute("class", mo.getAttributeValue("class") + " mfenced-separator");
                    fencedSpan.addContent(mo);
                }
            }
        }

        // Closing fence
        Element closeFenceSpan = new Element("span");
        closeFenceSpan.setAttribute("class", "mfenced-close");
        closeFenceSpan.setText(closed);
        fencedSpan.addContent(closeFenceSpan);

        return fencedSpan;
    }
}
