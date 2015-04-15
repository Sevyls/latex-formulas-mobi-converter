package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.token;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.MathmlCharacterDictionary;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.attributes.Unit;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.layout.Mrow;
import org.apache.log4j.Logger;
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
 * Renders a MathML Mo tag
 *
 * http://www.w3.org/TR/MathML2/chapter3.html#presm.mo
 *
 * @author Michael Auß
 *         Created: 15.09.2014
 */
public class Mo extends Token {
    private static Logger logger = Logger.getLogger(Mo.class);

    private String value;

    // MathML Mo attributes with default values as described in
    // http://www.w3.org/TR/MathML2/chapter3.html#id.3.2.5.2
    private String form = "infix";
    private boolean fence = false;
    private boolean stretchy = false;
    private boolean separator = false;
    private boolean largeop = false;
    private boolean movablelimits = false;
    private boolean accent = false;

    private Unit maxsize = new Unit(Double.POSITIVE_INFINITY, null);
    private Unit minsize = new Unit(1.0, null);

    private Unit lspace = Unit.parse("thickmathspace");
    private Unit rspace = Unit.parse("thickmathspace");

    public Unit getMaxsize() {
        return maxsize;
    }

    public void setMaxsize(Unit maxsize) {
        this.maxsize = maxsize;
    }

    public Unit getMinsize() {
        return minsize;
    }

    public void setMinsize(Unit minsize) {
        this.minsize = minsize;
    }

    public boolean isFence() {
        return fence;
    }

    public void setFence(boolean fence) {
        this.fence = fence;
    }

    public boolean isStretchy() {
        return stretchy;
    }

    public void setStretchy(boolean stretchy) {
        this.stretchy = stretchy;
    }

    public boolean isSeparator() {
        return separator;
    }

    public void setSeparator(boolean separator) {
        this.separator = separator;
    }

    public boolean isLargeop() {
        return largeop;
    }

    public void setLargeop(boolean largeop) {
        this.largeop = largeop;
    }

    public boolean isMovablelimits() {
        return movablelimits;
    }

    public void setMovablelimits(boolean movablelimits) {
        this.movablelimits = movablelimits;
    }

    public boolean isAccent() {
        return accent;
    }

    public void setAccent(boolean accent) {
        this.accent = accent;
    }

    public Unit getLspace() {
        return lspace;
    }

    public void setLspace(Unit lspace) {
        this.lspace = lspace;
    }

    public Unit getRspace() {
        return rspace;
    }

    public void setRspace(Unit rspace) {
        this.rspace = rspace;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(Object value) {
        this.value = (String) value;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    @Override
    public String toString() {
        return "Mo{" +
                "value='" + value + '\'' +
                ", form='" + form + '\'' +
                ", fence=" + fence +
                ", stretchy=" + stretchy +
                ", separator=" + separator +
                ", largeop=" + largeop +
                ", movablelimits=" + movablelimits +
                ", accent=" + accent +
                ", maxsize=" + maxsize +
                ", minsize=" + minsize +
                ", lspace=" + lspace +
                ", rspace=" + rspace +
                '}';
    }

    @Override
    public Element render(FormulaElement parent, List<FormulaElement> siblings) {
        // Mrow default form behaviour according to W3C MathML2
        // 3.2.5.7.2 Default value of the form attribute
        // http://www.w3.org/TR/MathML2/chapter3.html#presm.formdefval
        if (parent != null && parent.getClass() == Mrow.class && siblings != null && siblings.size() > 1) {
            List<FormulaElement> siblingsWithoutMspace = new ArrayList<>();
            for (FormulaElement e : siblings) {
                if (e.getClass() != Mspace.class) {
                    siblingsWithoutMspace.add(e);
                }
            }

            Integer position = siblingsWithoutMspace.indexOf(this);

            if (this.separator) {
                this.form = "infix";
            }

            if (position == 0) {
                this.form = "prefix";
            } else if (position == siblingsWithoutMspace.size() - 1) {
                this.form = "postfix";
            }


        }

        // Render operator
        Element moSpan = new Element("span");
        moSpan.setAttribute("class", "mo");

        // spacing
        String css = "padding-left: " + lspace.toString() + "; padding-right: " + rspace.toString() + ";";
        moSpan.setAttribute("style", css);

        String output = MathmlCharacterDictionary.decodeEntity(value);
        if (output != null) {
            value = output;
        }

        moSpan.addContent(value.trim());

        return moSpan;
    }
}
