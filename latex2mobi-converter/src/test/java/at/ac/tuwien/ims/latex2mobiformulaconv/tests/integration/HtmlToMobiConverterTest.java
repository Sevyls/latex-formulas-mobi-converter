package at.ac.tuwien.ims.latex2mobiformulaconv.tests.integration;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi.HtmlToMobiConverter;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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
 * <p/>

 * Abstract Integration Test for an HtmlToMobiConverter Implementation
 * @see at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi.HtmlToMobiConverter
 *
 * @author Michael Auß
 *         Created on 10.04.15.
 */
public abstract class HtmlToMobiConverterTest {
    private static Logger logger = Logger.getLogger(HtmlToMobiConverterTest.class);

    private ApplicationContext applicationContext;
    private HtmlToMobiConverter htmlToMobiConverter;
    private Path inputFile;
    private File mobi;
    protected String bean;

    @Before
    public void setUp() throws Exception {
        logger.debug("setUp()...");
        this.applicationContext = new ClassPathXmlApplicationContext("/application-context.xml");
        this.htmlToMobiConverter = (HtmlToMobiConverter) applicationContext.getBean(bean);
        this.inputFile = applicationContext.getResource("html+css/latex2mobi.html").getFile().toPath();
        logger.debug("inputFile = " + this.inputFile.toAbsolutePath().toString());
        logger.debug("inputFile exists? " + Files.exists(inputFile));
    }

    @Test
    public void testConvertToMobi() throws Exception {
        logger.debug("testConvertToMobi");

        mobi = this.htmlToMobiConverter.convertToMobi(inputFile.toFile());
        assertNotNull(mobi);
        logger.debug("Output file created at " + mobi.toPath().toAbsolutePath().toString());
        assertTrue(Files.exists(mobi.toPath()));
        assertTrue(Files.size(mobi.toPath()) > 0L);

        // TODO assert filetype
    }

    @Test
    public void testGetExecOption() throws Exception {
        // TODO
    }

    @After
    public void tearDown() throws Exception {
        if (mobi != null) {
            logger.debug("Removing generated mobi file");
            Files.deleteIfExists(mobi.toPath());
        }
    }

}
