package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.token;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.MathmlCharacterDictionary;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;
import org.jdom2.Element;

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
 * Renders a MathML Mi tag
 *
 * http://www.w3.org/TR/MathML2/chapter3.html#presm.mi
 *
 * @author Michael Auß
 *         Date: 15.09.2014
 */
public class Mi extends Token {
    private String value;
    private String mathvariant;

    public String getMathvariant() {
        return mathvariant;
    }

    public void setMathvariant(String mathvariant) {
        this.mathvariant = mathvariant;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }

    @Override
    public Element render(FormulaElement parent, List<FormulaElement> siblings) {
        Element span = new Element("span");
        span.setAttribute("class", "mi");
        if (mathvariant != null) {
            switch(mathvariant) {
                case "script":
                    lookup(this.value + "scr");
                    break;
                case "double-struck":
                    lookup(this.value.toUpperCase() + "opf");
                    break;
                case "fraktur":
                    lookup(this.value + "fr");
                    break;
            }
        }
        span.setText(value.trim());
        return span;
    }

    private void lookup(String name) {
        if (MathmlCharacterDictionary.entityMapByName.containsKey(name)) {
            this.value = MathmlCharacterDictionary.entityMapByName.get(name);
        }
    }
}
