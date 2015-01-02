package at.ac.tuwien.ims.latex2mobiformulaconv.tests.unit;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Mrow;
import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
 */
public class MrowTest {
    private static Logger logger = Logger.getLogger(MrowTest.class);
    private static final String[] subElements = new String[]{"mo", "mover", "munder", "msup", "mi", "mfrac"};

    private Mrow mrow;
    private int count;


    @Before
    public void setUp() throws Exception {


    }

    @Test
    public void testRenderSingleElement() throws Exception {
        count = 1;
        mockElements();
        Element result = mrow.render(null, null);

        assertNotNull(result);
        assertEquals("span", result.getName());
        // Should not render any mrow markup
        assertNotEquals("mrow", result.getAttributeValue("class"));

        FormulaElement singleChild = mrow.getList().get(0);

        // Single child should get rendered
        verify(singleChild).render(any(FormulaElement.class), anyList());

    }

    @Test
    public void testRenderRandomCountOfMin2Elements() throws Exception {
        count = RandomUtils.nextInt(100) + 2;  // Minimum of 2 elements
        logger.debug("Random count: " + count);
        mockElements();



        Element result = mrow.render(null, null);

        assertNotNull(result);
        assertEquals("span", result.getName());
        assertEquals("mrow", result.getAttributeValue("class"));


        Iterator<FormulaElement> iterator = mrow.getList().iterator();
        while (iterator.hasNext()) {
            verify(iterator.next()).render(mrow, mrow.getList());
        }
    }

    private void mockElements() {
        mrow = new Mrow();
        for (int i = 0; i < count; i++) {
            // mock subelement
            FormulaElement formulaElement = mock(FormulaElement.class);

            // mock result of subelement
            Element span = new Element("span");
            span.setAttribute("class", subElements[RandomUtils.nextInt(subElements.length - 1)]);
            when(formulaElement.render(null, null)).thenReturn(span);

            mrow.addElement(formulaElement);
        }
    }
}