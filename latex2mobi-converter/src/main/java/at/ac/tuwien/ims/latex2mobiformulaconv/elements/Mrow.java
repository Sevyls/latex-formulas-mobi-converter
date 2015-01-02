package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.apache.log4j.Logger;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Iterator;
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
 *         Created: 20.05.14 23:30
 */
public class Mrow implements FormulaElement {
    private static Logger logger = Logger.getLogger(Mrow.class);

    List<FormulaElement> list = new ArrayList<>();

    public void addElement(FormulaElement element) {
        list.add(element);
    }

    public List<FormulaElement> getList() {
        return list;
    }

    @Override
    public Element render(FormulaElement parent, List<FormulaElement> siblings) {
        if (list.size() == 1) {
            // TODO check attributes of mrow

            // MathML2 3.3.1.2.1 mrow of one argument
            // http://www.w3.org/TR/MathML2/chapter3.html#presm.mrow
            // MathML renderers are required to treat an mrow element containing exactly one argument as equivalent
            // in all ways to the single argument occurring alone,
            // provided there are no attributes on the mrow element's start tag.
            return list.get(0).render(null, null);
        }

        Element span = new Element("span");
        span.setAttribute("class", "mrow");

        Iterator<FormulaElement> iterator = list.iterator();
        while (iterator.hasNext()) {
            FormulaElement cur = iterator.next();
            Element html = cur.render(this, list);
            if (html != null) {
                span.addContent(html);
            } else {
                logger.debug("HTML is NULL: " + cur.getClass().toString());
            }
        }
        return span;
    }
}
