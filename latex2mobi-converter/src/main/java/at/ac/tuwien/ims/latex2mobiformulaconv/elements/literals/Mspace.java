package at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.attributes.Unit;
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
 * @author Michael Auß
 *         Date: 15.09.14
 */
public class Mspace implements FormulaElement {
    // Default values from MathML2 3.2.7.2
    // http://www.w3.org/TR/MathML2/chapter3.html#presm.mspace
    private Unit width = new Unit(0.0, "em");
    private Unit height = new Unit(0.0, "ex");
    private Unit depth = new Unit(0.0, "ex");
    private String linebreak = "auto";

    public Unit getWidth() {
        return width;
    }

    public void setWidth(Unit width) {
        this.width = width;
    }

    public Unit getHeight() {
        return height;
    }

    public void setHeight(Unit height) {
        this.height = height;
    }

    public Unit getDepth() {
        return depth;
    }

    public void setDepth(Unit depth) {
        this.depth = depth;
    }

    public String getLinebreak() {
        return linebreak;
    }

    public void setLinebreak(String linebreak) {
        this.linebreak = linebreak;
    }

    @Override
    public Element render(FormulaElement parent, List<FormulaElement> siblings) {
        Element span = new Element("span");
        span.setAttribute("class", "mspace");

        String style = "width: " + width.toString() + "; height: " + height.toString() + ";";
        span.setAttribute("style", style);

        span.setText(" ");
        return span;
    }
}
