package at.ac.tuwien.ims.latex2mobiformulaconv.tests.unit;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.FormulaConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.Formula;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;

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
public class FormulaConverterTest {
    private FormulaConverter formulaConverter;

    @Before
    public void setUp() throws Exception {
        // Mock abstract class to implementing class for testing its non-abstract methods
        formulaConverter = Mockito.mock(FormulaConverter.class, Mockito.CALLS_REAL_METHODS);
    }

    @Test
    public void testParseToMathML() throws Exception {
        final String correctLatexSource = "$A_{m,n} =\n" +
                "\\begin{pmatrix}\n" +
                "a_{1,1} & a_{1,2} & \\cdots & a_{1,n} \\\\\n" +
                "a_{2,1} & a_{2,2} & \\cdots & a_{2,n} \\\\\n" +
                "\\vdots  & \\vdots  & \\ddots & \\vdots  \\\\\n" +
                "a_{m,1} & a_{m,2} & \\cdots & a_{m,n}\n" +
                "\\end{pmatrix}$";

        Formula result = formulaConverter.parseToMathML(1, correctLatexSource);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertFalse(result.isInvalid());

        assertEquals(correctLatexSource, result.getLatexCode());

        assertNotNull(result.getMathMl());
        assertFalse(result.getMathMl().isEmpty());

        // TODO mathml xml schema validation?
    }

    @Test
    public void testExtractFormulas() throws Exception {
        // TODO
    }

    @Test
    public void testReplaceFormulas() throws Exception {
        // TODO
    }

    @Test
    public void testRenderInvalidFormulaSource() throws Exception {
        // TODO
    }
}