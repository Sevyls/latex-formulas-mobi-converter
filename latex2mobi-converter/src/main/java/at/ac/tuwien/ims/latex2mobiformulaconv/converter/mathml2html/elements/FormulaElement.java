package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements;

import org.jdom2.Element;

import java.util.List;

/*
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
 */

/**
 * @author Michael Auß
 *         Created: 20.05.14 23:29
 *         <p/>
 *         A part of a formula which can be rendered to HTML
 *         <p/>
 *         Note: This is not used when converting to images
 */
public interface FormulaElement {

    /**
     * Renders this FormulaElement to HTML.
     * May call render() on sub-elements for recursive rendering.
     * <p/>
     * Some cases require to take different actions in rendering depending on
     * this elements parents or siblings.
     * <p/>
     * Implementing classes SHOULD always have a way to fallback if parent or siblings are NULL
     * <p/>
     * NEVER call render() on the parent or siblings, endless loop could be the result.
     *
     * @param parent   the parent FormulaElement
     * @param siblings list of sibling elements including this FormulaElement
     * @return A JDOM Element which represents this FormulaElement + sub-elements (i.e. in HTML)
     */
    Element render(FormulaElement parent, List<FormulaElement> siblings);
}
