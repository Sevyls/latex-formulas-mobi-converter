package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.tablesmatrices;

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
 * Renders a MathML Mtr tag (Table rows)
 *
 * http://www.w3.org/TR/MathML2/chapter3.html#presm.mtr
 *
 * @author Michael Auß
 *         Date: 26.11.2014
 *         Time: 21:44
 */
public class Mtr implements FormulaElement {
    private List<Mtd> tds = new ArrayList<Mtd>();

    public List<Mtd> getTds() {
        return tds;
    }

    @Override
    public Element render(FormulaElement parent, List<FormulaElement> siblings) {
        Element tr = new Element("tr");
        for (int i = 0; i < tds.size(); i++) {
            tr.addContent(tds.get(i).render(null, null));
        }
        return tr;
    }
}