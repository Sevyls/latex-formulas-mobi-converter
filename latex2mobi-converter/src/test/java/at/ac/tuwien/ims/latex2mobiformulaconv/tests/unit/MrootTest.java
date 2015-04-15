package at.ac.tuwien.ims.latex2mobiformulaconv.tests.unit;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.layout.Mroot;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.*;

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
public class MrootTest extends FormulaElementTest {
    private static final Logger logger = Logger.getLogger(MrootTest.class);

    private Mroot mroot;
    private FormulaElement base;
    private FormulaElement degree;


    @Before
    public void setUp() throws Exception {
        logger.debug("enter setUp()...");

        mroot = new Mroot();

        // mock base
        base = mock(FormulaElement.class);

        // mock result of base
        Element baseSpan = new Element("span");
        when(base.render(or(eq(mroot), isNull(FormulaElement.class)),
                or(anyListOf(FormulaElement.class), isNull(List.class)))).thenReturn(baseSpan);

        mroot.setBase(base);

        // mock degree
        degree = mock(FormulaElement.class);

        // mock result of degree
        Element degreeSpan = new Element("span");
        when(degree.render(or(eq(mroot), isNull(FormulaElement.class)),
                or(anyListOf(FormulaElement.class), isNull(List.class)))).thenReturn(degreeSpan);

        mroot.setDegree(degree);

        formulaElement = mroot;
    }

    @Test
    public void testRenderRoot() {
        logger.debug("enter testRenderRoot()...");

        Element result = mroot.render(possibleParent, null);

        assertNotNull(result);
        assertEquals("span", result.getName());
        assertEquals("mroot", result.getAttributeValue("class"));

        assertEquals("mroot-degree", result.getChildren("span").get(0).getAttributeValue("class"));

        Element symbol = result.getChildren("span").get(1);
        assertEquals("mroot-symbol", symbol.getAttributeValue("class"));
        assertEquals("√", symbol.getChild("span").getText());

        assertEquals("mroot-topbar", result.getChildren("span").get(2).getAttributeValue("class"));
        assertEquals("mroot-base", result.getChildren("span").get(2).getChild("span").getAttributeValue("class"));

        verify(base).render(or(eq(mroot), isNull(FormulaElement.class)), or(anyListOf(FormulaElement.class), isNull(List.class)));
        verify(degree).render(or(eq(mroot), isNull(FormulaElement.class)), or(anyListOf(FormulaElement.class), isNull(List.class)));
    }

    @Test
    public void testRenderSqrt() {
        logger.debug("enter testRenderRoot()...");
        mroot.setDegree(null);
        Element result = mroot.render(possibleParent, null);

        assertNotNull(result);
        assertEquals("span", result.getName());
        assertEquals("mroot", result.getAttributeValue("class"));


        Element symbol = result.getChildren("span").get(0);
        assertEquals("mroot-symbol", symbol.getAttributeValue("class"));
        assertEquals("√", symbol.getChild("span").getText());

        assertEquals("mroot-topbar", result.getChildren("span").get(1).getAttributeValue("class"));
        assertEquals("mroot-base", result.getChildren("span").get(1).getChild("span").getAttributeValue("class"));

        verify(base).render(or(eq(mroot), isNull(FormulaElement.class)), or(anyListOf(FormulaElement.class), isNull(List.class)));
    }
}