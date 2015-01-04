package at.ac.tuwien.ims.latex2mobiformulaconv.tests.unit;

import at.ac.tuwien.ims.latex2mobiformulaconv.elements.FormulaElement;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.junit.After;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.AdditionalMatchers.or;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyListOf;
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
 *         Date: 03.01.2015
 *         Time: 21:56
 *
 * Provides a generic logger and a parent Element
 * Verifies behaviour that all implementing classes should respect.
 */
public abstract class FormulaElementTest {
    protected FormulaElement formulaElement;

    protected FormulaElement possibleParent = mock(FormulaElement.class);
    protected static Logger logger = Logger.getLogger(FormulaElementTest.class);

    @Test
    public void testRender() {
        logger.debug("Generic render test...");
        Element result = formulaElement.render(possibleParent, null);
        assertNotNull(result);
        String resultTagName = result.getName();

        // Ensure that no other elements as div or span Tags are used
        assertTrue(resultTagName == "div" || resultTagName == "span");

        // Ensure main element has name of corresponding MathML Object for Styling
        String className = formulaElement.getClass().getSimpleName().toLowerCase();
        logger.debug("Class Name: " + className);
        assertTrue(result.getAttributeValue("class").contains(className));

        // Ensure content
        assertTrue(result.getChildren().isEmpty() == false || result.getText().isEmpty() == false);
    }

    /**
     * Verifies that an implementing FormulaElement class should not call render
     * on its parent FormulaElement. Loop render calls would never stop because
     * of the recursive rendering algorithm.
     */
    @After
    public void verifyParentHasNotBeenRenderedAgain() {
        logger.debug("Verify parent has not been rendered again");
        verify(this.possibleParent, never()).render(or(any(FormulaElement.class), isNull(FormulaElement.class)), or(anyListOf(FormulaElement.class), isNull(List.class)));
    }
}
