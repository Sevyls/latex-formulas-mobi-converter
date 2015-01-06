package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements;

import org.jdom2.Element;

import java.util.List;

/**
 * The MIT License (MIT)
 * latex2mobi -- LaTeX Formulas to Mobi Converter
 * Copyright (c) 2014 Michael Auß
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 *
 * For Third Party Software Licenses read LICENSE file in base dir.
 *
 *
 * @author Michael Auß
 *         Created: 20.05.14 23:29
 *
 * A part of a formula which can be rendered to HTML
 */
public interface FormulaElement {

    /**
     * Renders this FormulaElement to HTML.
     * May call render() on sub-elements for recursive rendering.
     *
     * @return A JDOM Element which represents this FormulaElement + sub-elements (i.e. in HTML)
     * @param parent the parent FormulaElement, use it for context only
     * @param siblings list of sibling elements including this FormulaElement, use it for context only
     */
    public Element render(FormulaElement parent, List<FormulaElement> siblings);
}
