package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.token;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;

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
 * Represents a Token as defined in MathML 2.0
 * http://www.w3.org/TR/MathML2/chapter3.html#presm.tokel
 *
 * @author Michael Auß
 *         Created: 20.05.14 23:39
 */
public abstract class Token implements FormulaElement {
    public abstract Object getValue();

    public abstract void setValue(Object value);
}
