package at.ac.tuwien.ims.latex2mobiformulaconv.tests.unit;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.layout.Mstyle;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Matchers.any;
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
public class MstyleTest extends FormulaElementTest {
    private static final Logger logger = Logger.getLogger(MstyleTest.class);
    private final String STYLE = "test";
    private Mstyle mstyle;
    private FormulaElement base;

    @Before
    public void setUp() throws Exception {
        mstyle = new Mstyle();
        base = mock(FormulaElement.class);
        when(base.render(or(any(FormulaElement.class), isNull(FormulaElement.class)), anyListOf(FormulaElement.class))).thenReturn(new Element("span"));
        List<FormulaElement> list = new ArrayList<>();
        list.add(base);

        mstyle.setBase(list);
        mstyle.setStyle(STYLE);

        formulaElement = mstyle;
    }


    @Test
    public void testDetails() throws Exception {
        Element result = mstyle.render(possibleParent, null);

        assertNotNull(result);
        assertEquals("span", result.getName());

        assertEquals("mstyle mathvariant-test", result.getAttributeValue("class"));

        verify(base).render(or(eq(mstyle), isNull(FormulaElement.class)), or(anyListOf(FormulaElement.class), isNull(List.class)));
        verify(mstyle.getBase().get(0)).render(isNull(FormulaElement.class), or(anyListOf(FormulaElement.class), isNull(List.class)));
    }
}