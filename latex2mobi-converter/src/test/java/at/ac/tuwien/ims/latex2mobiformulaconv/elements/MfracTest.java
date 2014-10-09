package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class MfracTest {
    private Mfrac mfrac;

    @Before
    public void setUp() throws Exception {
        mfrac = new Mfrac();

        mfrac.setNumerator(mock(FormulaElement.class));
        when(mfrac.getNumerator().render()).thenReturn(new Element("span"));
        mfrac.setDenominator(mock(FormulaElement.class));
        when(mfrac.getDenominator().render()).thenReturn(new Element("span"));
    }

    @Test
    public void testRender() throws Exception {
        Element result = mfrac.render();

        assertNotNull(result);
        assertEquals("span", result.getName());
        assertEquals("mfrac", result.getAttributeValue("class"));

        verify(mfrac.getNumerator()).render();
        assertEquals("numerator", result.getChildren("span").get(0).getAttributeValue("class"));
        verify(mfrac.getDenominator()).render();
        assertEquals("denominator", result.getChildren("span").get(1).getAttributeValue("class"));
    }
}