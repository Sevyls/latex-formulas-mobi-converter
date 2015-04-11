package at.ac.tuwien.ims.latex2mobiformulaconv.tests.unit;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.tablesmatrices.Mtable;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.tablesmatrices.Mtr;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
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
public class MtableTest extends FormulaElementTest {
    private Mtable mtable;
    private ArrayList<Mtr> rows;
    private int count;

    @Before
    public void setUp() throws Exception {
        mtable = new Mtable();
        count = new Random().nextInt(31) + 2;
        logger.debug("Content list length: " + count);

        rows = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Mtr mockedFormulaElement = mock(Mtr.class);
            when(mockedFormulaElement.render(
                    or(any(Mtable.class), isNull(Mtable.class)),
                    or(anyListOf(Mtr.class), isNull(List.class))))
                    .thenReturn(new Element("tr"));
            rows.add(mockedFormulaElement);
        }

        mtable.getRows().addAll(rows);

        formulaElement = mtable;
    }


    @Test
    public void testDetails() throws Exception {
        Element result = mtable.render(possibleParent, null);
        for (FormulaElement mockedElement : rows) {
            verify(mockedElement).render(or(eq(mtable), isNull(FormulaElement.class)), or(anyListOf(Mtr.class), isNull(List.class)));
        }

        assertNotNull(result.getChild("table"));
        assertEquals(0, result.getChildren().indexOf(result.getChild("table")));
        assertEquals(count, result.getChild("table").getChildren("tr").size());
    }
}