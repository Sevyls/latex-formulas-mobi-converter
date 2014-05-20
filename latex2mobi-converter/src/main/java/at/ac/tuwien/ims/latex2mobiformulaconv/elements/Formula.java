package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

import java.util.ArrayList;

/**
 * @author mauss
 *         Created: 20.05.14 23:30
 */
public class Formula {
    private ArrayList<Line> lines;

    public Formula() {
        lines = new ArrayList<Line>();
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public Element render() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
