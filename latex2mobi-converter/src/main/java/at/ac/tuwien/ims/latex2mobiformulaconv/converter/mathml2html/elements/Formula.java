package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements;

import org.jdom2.Element;

import java.nio.file.Path;

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
 *
 *
 * Main model for a single formula found in a LaTeX input document
 *
 * Holds all relevant information for source and destination representation
 * after successful conversion.
 *
 * @author Michael Auß
 *         Created: 20.05.14 23:30
 */
public class Formula {
    /**
     * The id is the sequential position on the document
     */
    private int id;

    /**
     * The LaTeX source for this formula
     */
    private String latexCode;

    /**
     * The file path for the image representation of this formula
     * (only available if output should be images instead of markup)
     *
     * @see at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.ImageFormulaConverter
     */
    private Path imageFilePath;

    /**
     * The markup representation of this formula
     * default representation of a formula after conversion
     * @see at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.DOMFormulaConverter
     */
    private Element html;

    /**
     * The intermediate step to converting a formula is its MathML representation
     * SnuggleTeX generates this MathML
     */
    private String mathMl;

    public Element getHtml() {
        return html;
    }

    public void setHtml(Element html) {
        this.html = html;
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
    }

    public int getId() {
        return id;
    }
}
