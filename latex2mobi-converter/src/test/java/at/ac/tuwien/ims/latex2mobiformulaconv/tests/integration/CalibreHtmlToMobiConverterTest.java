package at.ac.tuwien.ims.latex2mobiformulaconv.tests.integration;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi.HtmlToMobiConverter;
import junit.framework.TestCase;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

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
 * Integration Test for
 * @see at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi.CalibreHtmlToMobiConverter
 *
 * @author Michael Auß
 *  Created on 09.04.15.
 */
public class CalibreHtmlToMobiConverterTest extends TestCase {
    private static Logger logger = Logger.getLogger(CalibreHtmlToMobiConverterTest.class);

    private ApplicationContext applicationContext;
    private HtmlToMobiConverter calibreConverter;
    private Path inputFile;
    private File mobi;

    public void setUp() throws Exception {
        logger.debug("setUp()...");
        this.applicationContext = new ClassPathXmlApplicationContext("/application-context.xml");
        this.calibreConverter = (HtmlToMobiConverter) applicationContext.getBean("calibre-html2mobi-converter");
        this.inputFile = applicationContext.getResource("html+css/latex2mobi.html").getFile().toPath();
        logger.debug("inputFile = " + this.inputFile.toAbsolutePath().toString());
        logger.debug("inputFile exists? " + Files.exists(inputFile));
    }

    public void testConvertToMobi() throws Exception {
        logger.debug("testConvertToMobi");

        mobi = this.calibreConverter.convertToMobi(inputFile.toFile());
        assertNotNull(mobi);
        logger.debug("Output file created at " + mobi.toPath().toAbsolutePath().toString());
        assertTrue(Files.exists(mobi.toPath()));
        assertTrue(Files.size(mobi.toPath()) > 0L);

    }

    public void tearDown() throws Exception {
        Files.deleteIfExists(mobi.toPath());
    }
}