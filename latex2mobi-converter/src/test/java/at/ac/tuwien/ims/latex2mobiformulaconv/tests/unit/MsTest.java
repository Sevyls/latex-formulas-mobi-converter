package at.ac.tuwien.ims.latex2mobiformulaconv.tests.unit;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.token.Ms;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
 *         Date: 02.01.2015
 *         Time: 10:39
 */
public class MsTest extends FormulaElementTest {
    private static final Logger logger = Logger.getLogger(MsTest.class);
    private Ms ms;
    private String randomString;
    private String whitespace = "   \t";

    @Before
    public void setUp() throws Exception {
        ms = new Ms();
        randomString = RandomStringUtils.randomAscii(new Random().nextInt(32) + 1).trim();
        logger.debug("Random string:\n" + randomString);

        ms.setValue(whitespace + randomString + whitespace);
        formulaElement = ms;
    }

    @Test
    public void testDetails() throws Exception {
        Element result = ms.render(possibleParent, null);

        String resultText = result.getText();
        logger.debug("Result Text:\n" + resultText);

        // MathML: By default, string literals are displayed surrounded by double quotes.
        assertTrue(resultText.charAt(0) == '"');
        assertTrue(resultText.charAt(resultText.length() - 1) == '"');

        // The original string should be contained within the result string
        assertTrue(resultText.contains(randomString));

        // Leading + Trailing Whitespace should be trimmed
        // Currently whitespace inside the string is ignored
        assertEquals(randomString, resultText.substring(1, resultText.length() - 1));
    }
}
