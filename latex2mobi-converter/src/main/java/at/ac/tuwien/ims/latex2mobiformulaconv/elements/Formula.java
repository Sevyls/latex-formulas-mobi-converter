package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

import java.util.ArrayList;

/**
 * @author mauss
 *         Created: 20.05.14 23:30
 */
public class Formula {

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        SINGLE_LINE,
        MULTI_LINE
    }

    private ArrayList<Line> lines;
    private String latexCode;
    private Type type;

    public String getLatexCode() {
        return latexCode;
    }

    public void setLatexCode(String latexCode) {
        this.latexCode = latexCode;
    }

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
