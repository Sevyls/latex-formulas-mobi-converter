package at.ac.tuwien.ims.latex2mobiformulaconv.tests.unit;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.layout.Mfenced;
import org.apache.commons.lang.RandomStringUtils;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
public class MfencedTest extends FormulaElementTest {
    private Mfenced mfenced;
    private String opening;
    private String closing;
    private String separators;


    @Before
    public void setUp() throws Exception {
        mfenced = new Mfenced();

        formulaElement = mfenced;
    }

    @Test
    public void testRenderEmptyContent() throws Exception {
        // TODO
    }

    @Test
    public void testRenderSingleContentElement() throws Exception {
        // TODO
    }

    @Test
    public void testRenderContentList() throws Exception {
        // TODO
    }

    @Test
    public void testRenderContentListWithSeparators() throws Exception {
        int count = new Random().nextInt(31) + 2;
        logger.debug("Content list length: " + count);
        separators = RandomStringUtils.randomAscii(count);

        List<FormulaElement> list = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            FormulaElement mockedFormulaElement = mock(FormulaElement.class);
            when(mockedFormulaElement.render(or(any(FormulaElement.class), isNull(FormulaElement.class)),
                    or(anyListOf(FormulaElement.class), isNull(List.class)))).thenReturn(new Element("span"));
            list.add(mockedFormulaElement);
        }

        mfenced.setContent(list);

        Element result = mfenced.render(possibleParent, null);



        for (FormulaElement mockedElement : list) {
            verify(mockedElement).render(or(eq(mfenced), isNull(FormulaElement.class)), or(anyListOf(FormulaElement.class), isNull(List.class)));
        }
        // TODO
    }
}