package at.ac.tuwien.ims.latex2mobiformulaconv.elements;

import org.jdom2.Element;

import java.nio.file.Path;
import java.util.ArrayList;

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
 *         Created: 20.05.14 23:30
 */
public class Formula {
    private int id;
    private ArrayList<Mrow> mrows;
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
        mrows = new ArrayList<Mrow>();
    }

    public int getId() {
        return id;
    }

    public ArrayList<Mrow> getMrows() {
        return mrows;
    }

    public Element render() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
