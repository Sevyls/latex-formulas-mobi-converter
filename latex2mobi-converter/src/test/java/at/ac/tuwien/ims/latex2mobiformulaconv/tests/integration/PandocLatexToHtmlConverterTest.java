package at.ac.tuwien.ims.latex2mobiformulaconv.tests.integration;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.latex2html.PandocLatexToHtmlConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.tests.utils.TestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.filter.Filters;
import org.jdom2.xpath.XPathExpression;
import org.jdom2.xpath.XPathFactory;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import static org.junit.Assert.*;

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
 *         Date: 18.08.14
 *         Time: 21:22
 */
public class PandocLatexToHtmlConverterTest {
    private static Logger logger = Logger.getLogger(PandocLatexToHtmlConverterTest.class);

    private PandocLatexToHtmlConverter converter;
    private File inputFile;
    private Path workingDirectory;
    private String title;
    private String mainCss;

    @Before
    public void setUp() throws Exception {
        converter = new PandocLatexToHtmlConverter();

        workingDirectory = TestUtils.getWorkingDirectory(this.getClass());
        logger.debug("Working directory: " + workingDirectory.toAbsolutePath().toString());
        inputFile = workingDirectory.resolve("formulas.tex").toFile();

        mainCss = FileUtils.readFileToString(workingDirectory.resolve("main.css").toFile(), "UTF-8");
        // ignore UNIX / Windows new line
        mainCss = mainCss.trim().replace("\n", "").replace("\r", "");

        title = RandomStringUtils.randomAlphanumeric(64);
        logger.debug("Random title: " + title);
    }

    @Test
    public void testConvertLatexToHtml() throws Exception {
        Document result = converter.convert(inputFile, title, workingDirectory);
        assertNotNull(result);

        Element root = result.getRootElement();
        assertEquals("html", root.getName());

        List<Element> children = root.getChildren();
        assertFalse(children.isEmpty());

        Element head = children.get(0);
        assertEquals("head", head.getName());

        // Check for title
        Element title = head.getChild("title");
        assertNotNull(title);
        assertEquals(this.title, title.getText());

        // Check CSS
        Element style = head.getChild("style");
        assertNotNull(style);

        // ignore UNIX / Windows new line
        String styleText = style.getText().trim().replace("\n", "").replace("\r", "");

        assertFalse(styleText.isEmpty());
        assertEquals(mainCss, styleText);


        Element body = children.get(1);
        assertEquals("body", body.getName());

        List<Element> bodyElements = body.getChildren();
        assertFalse(bodyElements.isEmpty());

        // check for spans with LaTeX class
        String query = "//*[@class= 'LaTeX']";
        XPathExpression<Element> xpe = XPathFactory.instance().compile(query, Filters.element());
        List<Element> latexFormulas = xpe.evaluate(result);

        assertFalse(latexFormulas.isEmpty());

        for (Element formula : latexFormulas) {
            assertEquals("span", formula.getName());
            String latex = formula.getText();
            assertNotEquals("", latex);
        }
    }
}
