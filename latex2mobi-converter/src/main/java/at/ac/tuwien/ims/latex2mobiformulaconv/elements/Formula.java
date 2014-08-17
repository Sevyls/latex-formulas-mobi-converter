package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * @author mauss
 *         Created: 20.05.14 23:30
 */
public class Formula {
    private int id;
    private ArrayList<Line> lines;
    private String latexCode;
    private Type type;
    private Path imageFilePath;
    private Element html;
    private String mathMl;

    public Element getHtml() {
        return html;
    }

    public void setHtml(Element html) {
        this.html = html;
    }

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

    public Path getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(Path imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getMathMl() {
        return mathMl;
    }

    public void setMathMl(String mathMl) {
        this.mathMl = mathMl;
    }

    public String getLatexCode() {
        return latexCode;
    }

    public void setLatexCode(String latexCode) {
        this.latexCode = latexCode;
    }

    public Formula(int id) {
        this.id = id;
        lines = new ArrayList<Line>();
    }

    public int getId() {
        return id;
    }

    public ArrayList<Line> getLines() {
        return lines;
    }

    public Element render() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
