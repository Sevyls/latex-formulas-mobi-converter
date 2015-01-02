package at.ac.tuwien.ims.latex2mobiformulaconv.tests.unit;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.Mfrac;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
public class MfracTest {
    private Mfrac mfrac;

    @Before
    public void setUp() throws Exception {
        mfrac = new Mfrac();

        mfrac.setNumerator(mock(FormulaElement.class));
        when(mfrac.getNumerator().render(null, null)).thenReturn(new Element("span"));
        mfrac.setDenominator(mock(FormulaElement.class));
        when(mfrac.getDenominator().render(null, null)).thenReturn(new Element("span"));
    }

    @Test
    public void testRender() throws Exception {
        Element result = mfrac.render(null, null);

        assertNotNull(result);
        assertEquals("span", result.getName());
        assertEquals("mfrac", result.getAttributeValue("class"));

        verify(mfrac.getNumerator()).render(null, null);
        assertEquals("numerator", result.getChildren("span").get(0).getAttributeValue("class"));
        verify(mfrac.getDenominator()).render(null, null);
        assertEquals("denominator", result.getChildren("span").get(1).getAttributeValue("class"));
    }
}