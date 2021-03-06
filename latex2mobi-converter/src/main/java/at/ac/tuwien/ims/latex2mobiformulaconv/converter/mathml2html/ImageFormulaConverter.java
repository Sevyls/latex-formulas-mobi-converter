package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.elements.Formula;
import net.sourceforge.jeuclid.MathMLParserSupport;
import net.sourceforge.jeuclid.MutableLayoutContext;
import net.sourceforge.jeuclid.context.LayoutContextImpl;
import net.sourceforge.jeuclid.context.Parameter;
import org.apache.log4j.Logger;
import org.jdom2.Element;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

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
 *         Date: 08.06.14
 */
public class ImageFormulaConverter extends FormulaConverter {
    private static final Logger logger = Logger.getLogger(ImageFormulaConverter.class);

    /**
     * Takes a single LaTeX formula and converts it to an image in PNG format
     *
     * @param id           formula's number inside the document
     * @param latexFormula LaTeX formula
     * @return Formula object with resulting image file path set
     */
    @Override
    public Formula parse(int id, String latexFormula) {
        Formula formula = super.parseToMathML(id, latexFormula);
        logger.debug("parse() entered...");
        if (formula.isInvalid() == false) {
            try {
                final org.w3c.dom.Document doc = MathMLParserSupport.parseString(formula.getMathMl());

                final File outFile = Files.createTempFile(tempDirPath, "formula", ".png").toFile();

                formula.setImageFilePath(outFile.toPath());

                // Generate Html Image tag
                Element imageTag = new Element("img");
                imageTag.setAttribute("src", formula.getImageFilePath().getFileName().toString());
                imageTag.setAttribute("alt", FORMULA_ID_PREFIX + formula.getId());
                formula.setHtml(imageTag);

                final MutableLayoutContext params = new LayoutContextImpl(
                        LayoutContextImpl.getDefaultLayoutContext());
                params.setParameter(Parameter.MATHSIZE, 25f);

                // convert to image
                net.sourceforge.jeuclid.converter.Converter imageConverter = net.sourceforge.jeuclid.converter.Converter.getInstance();
                imageConverter.convert(doc, outFile, "image/png", params);
                logger.debug("Image file created at: " + outFile.toPath().toAbsolutePath().toString());
            } catch (SAXException e1) {
                logger.error(e1.getMessage(), e1);
            } catch (ParserConfigurationException e2) {
                logger.error(e2.getMessage(), e2);
            } catch (IOException e3) {
                logger.error(e3.getMessage(), e3);
            }
        } else {
            formula.setHtml(renderInvalidFormulaSource(formula));
        }
        return formula;
    }
}
