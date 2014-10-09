package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class MrowTest {
    private static Logger logger = Logger.getLogger(MrowTest.class);
    private static final String[] subElements = new String[]{"mo", "mover", "munder", "msup", "mi", "mfrac"};

    private Mrow mrow;
    private int count;


    @Before
    public void setUp() throws Exception {
        count = RandomUtils.nextInt(100);
        logger.debug("Random count: " + count);
        mrow = new Mrow();

        for (int i = 0; i < count; i++) {
            // mock subelement
            FormulaElement formulaElement = mock(FormulaElement.class);

            // mock result of subelement
            Element span = new Element("span");
            span.setAttribute("class", subElements[RandomUtils.nextInt(subElements.length - 1)]);
            when(formulaElement.render()).thenReturn(span);

            mrow.addElement(formulaElement);
        }

    }

    @Test
    public void testRender() throws Exception {
        Element result = mrow.render();

        assertNotNull(result);
        assertEquals("span", result.getName());
        assertEquals("mrow", result.getAttributeValue("class"));


        Iterator<FormulaElement> iterator = mrow.list.iterator();
        while (iterator.hasNext()) {
            verify(iterator.next()).render();
        }
    }
}