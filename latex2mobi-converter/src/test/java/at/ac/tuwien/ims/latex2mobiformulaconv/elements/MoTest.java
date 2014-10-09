package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class MoTest {
    private static final char[] operators = new char[]{'+', '-', '*', '%', '=', '/', '&', '<', '>', ':'};

    private static Logger logger = Logger.getLogger(MoTest.class);

    private String operator;

    @Before
    public void setUp() throws Exception {
        operator = RandomStringUtils.random(1, operators);
        logger.debug("Operator: " + operator);
    }

    @Test
    public void testRender() throws Exception {
        Mo mo = new Mo();

        mo.setOperator(operator);

        Element result = mo.render();

        assertNotNull(result);
        assertEquals("span", result.getName());
        assertEquals("mo", result.getAttributeValue("class"));
        assertEquals(operator, result.getText());

    }
}