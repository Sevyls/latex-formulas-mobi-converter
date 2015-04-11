package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.layout;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;
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
 *
 *
 * Renders a MathML mfrag tag
 *
 * See http://www.w3.org/TR/MathML2/chapter3.html#presm.mfrac
 *
 * @author Michael Auß
 *         Created: 20.05.14 23:39
 */
public class Mfrac implements FormulaElement {
    private FormulaElement numerator;
    private FormulaElement denominator;
    private String linethickness = "1";

    public String getLinethickness() {
        return linethickness;
    }

    public void setLinethickness(String linethickness) {
        this.linethickness = linethickness;
    }

    public FormulaElement getNumerator() {
        return numerator;
    }

    public void setNumerator(FormulaElement numerator) {
        this.numerator = numerator;
    }

    public FormulaElement getDenominator() {
        return denominator;
    }

    public void setDenominator(FormulaElement denominator) {
        this.denominator = denominator;
    }

    @Override
    public Element render(FormulaElement parent, List<FormulaElement> siblings) {
        Element fraction = new Element("span");
        fraction.setAttribute("class", "mfrac");

        Element numeratorSpan = new Element("span");
        numeratorSpan.setAttribute("class", "numerator");

        List<FormulaElement> siblingsList = new ArrayList<>();
        siblingsList.add(numerator);
        siblingsList.add(denominator);

        numeratorSpan.addContent(numerator.render(this, siblingsList));

        Element denominatorSpan = new Element("span");
        denominatorSpan.setAttribute("class", "denominator");

        // style for linethickness

        //  medium (same as 1), thin (thinner than 1, otherwise up to the renderer), or thick (thicker than 1, otherwise up to the renderer).
        switch (linethickness) {
            case "medium":
                linethickness = "1";
                break;
            case "thin":
                linethickness = "0.75";
                break;
            case "thick":
                linethickness = "2";
                break;
            default:
                try {
                    Double.valueOf(linethickness);
                } catch (NumberFormatException e) {
                    linethickness = "1";
                }
        }


        denominatorSpan.setAttribute("style", "border-top: " + linethickness + "px solid black;");

        denominatorSpan.addContent(this.denominator.render(this, siblingsList));

        fraction.addContent(numeratorSpan);
        fraction.addContent(denominatorSpan);

        return fraction;
    }
}
