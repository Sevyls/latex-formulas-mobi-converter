package at.ac.tuwien.ims.latex2mobiformulaconv.tests.unit;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.attributes.Unit;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.token.Mspace;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
 * @author Michael Auß
 */
public class MspaceTest extends FormulaElementTest {
    private static final Logger logger = Logger.getLogger(MspaceTest.class);

    private Mspace mspace;

    @Before
    public void setUp() throws Exception {
        logger.debug("enter setUp()...");

        mspace = new Mspace();

        formulaElement = mspace;
    }

    @Test
    public void testRenderDefault() {
        logger.debug("enter testRenderDefault()...");

        Element result = mspace.render(possibleParent, null);

        assertNotNull(result);
        assertEquals("span", result.getName());
        assertEquals("mspace", result.getAttributeValue("class"));
        assertEquals(" ", result.getText());
        assertEquals("width: 0.0em; height: 0.0ex;", result.getAttributeValue("style"));
    }

    @Test
    public void testRenderCustomWidthAndHeight() throws Exception {
        logger.debug("enter testRenderCustomWidthAndHeight()...");
        final Double width = 1.2;
        final Double height = 20.2;

        // instantiate Unit manually
        mspace.setWidth(new Unit(width, "em"));

        // parse Unit
        mspace.setHeight(Unit.parse(height.toString() + "px"));

        Element result = mspace.render(possibleParent, null);

        assertNotNull(result);
        assertEquals("span", result.getName());
        assertEquals("mspace", result.getAttributeValue("class"));
        assertEquals(" ", result.getText());

        assertEquals("width: " + width.toString() + "em; height: " + height.toString() + "px;", result.getAttributeValue("style"));

    }
}