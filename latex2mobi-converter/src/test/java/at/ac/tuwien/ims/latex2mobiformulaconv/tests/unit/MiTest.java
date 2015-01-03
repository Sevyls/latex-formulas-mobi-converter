package at.ac.tuwien.ims.latex2mobiformulaconv.tests.unit;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.FormulaElement;
import at.ac.tuwien.ims.latex2mobiformulaconv.elements.literals.Mi;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;

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
public class MiTest {
    private Mi mi;
    private String randomText;

    private static Logger logger = Logger.getLogger(MiTest.class);

    @Before
    public void setUp() throws Exception {
        mi = new Mi();
        randomText = RandomStringUtils.randomAscii(new Random().nextInt(32));
        logger.debug("RandomText: " + randomText);
        mi.setValue(randomText);
    }

    @Test
    public void testRender() throws Exception {
        FormulaElement possibleParent = mock(FormulaElement.class);

        Element result = mi.render(or(eq(possibleParent), isNull(FormulaElement.class)), or(anyListOf(FormulaElement.class), isNull(List.class)));

        assertNotNull(result);

        assertEquals("span", result.getName());
        assertEquals("mi", result.getAttributeValue("class"));
        assertEquals(randomText, result.getText());
        assertTrue(result.getChildren().isEmpty());

    }
}