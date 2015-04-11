package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.layout;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.token.Mo;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.token.Mtext;
import org.jdom2.Element;

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
 * Rendering for mroot & msqrt MathML Elements
 *
 * See http://www.w3.org/TR/MathML2/chapter3.html#presm.mroot
 *
 * @author Michael Auß
 *         Created: 21.05.14 00:03
 *
 */
public class Mroot extends Mo {
    private FormulaElement base;
    private FormulaElement index = null;

    public FormulaElement getIndex() {
        return index;
    }

    public void setIndex(FormulaElement index) {
        this.index = index;
    }

    public FormulaElement getBase() {
        return base;
    }

    public void setBase(FormulaElement base) {
        this.base = base;
    }

    @Override
    public Element render(FormulaElement parent, List<FormulaElement> siblings) {

        Element mrootSpan = new Element("span");
        mrootSpan.setAttribute("class", "mroot");

        // TODO rename index to correct name "degree"
        if (index != null) {
            Element indexSpan = new Element("span");
            indexSpan.setAttribute("class", "mroot-index");

            indexSpan.addContent(index.render(null, null));

            mrootSpan.addContent(indexSpan);
        }

        // Root symbol
        Element mrootSymbol = new Element("span");
        mrootSymbol.setAttribute("class", "mroot-symbol");
        Mtext sqrtSymbol = new Mtext();
        sqrtSymbol.setValue("√");
        mrootSymbol.addContent(sqrtSymbol.render(null, null));
        mrootSpan.addContent(mrootSymbol);


        Element mrootTopbar = new Element("span");
        mrootTopbar.setAttribute("class", "mroot-topbar");

        // base
        Element baseSpan = new Element("span");
        baseSpan.setAttribute("class", "mroot-base");
        baseSpan.addContent(base.render(null, null));
        mrootTopbar.addContent(baseSpan);

        mrootSpan.addContent(mrootTopbar);


        return mrootSpan;
    }
}
