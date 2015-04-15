package at.ac.tuwien.ims.latex2mobiformulaconv.tests.integration;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.Converter;
import at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.FormulaConverter;
import at.ac.tuwien.ims.latex2mobiformulaconv.tests.utils.TestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
 *         <p/>
 *         Integration test
 *         Tests the standard ways of running a conversion with the formulas.tex test resource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:ConverterTest-context.xml"})
public class EndToEndConversionTest {
    private static final Logger logger = Logger.getLogger(EndToEndConversionTest.class);
    private final String inputFilename = "formulas.tex";
    private final String filename = "ConverterTestFile";
    private final String ext = ".mobi";
    @Autowired
    private Converter converter;
    private ArrayList<Path> inputPaths;
    private Path outputPath;
    private Path outputFilePath;
    private Path workingDirectory;
    private String title;
    @Resource(name = "image-formula-converter")
    private FormulaConverter img_converter;

    @Resource(name = "dom-formula-converter")
    private FormulaConverter dom_converter;

    @Before
    public void setUp() throws Exception {
        workingDirectory = TestUtils.getWorkingDirectory(this.getClass());
        logger.info("Working directory: " + workingDirectory.toAbsolutePath().toString());

        converter.setWorkingDirectory(workingDirectory);
        title = RandomStringUtils.randomAlphanumeric(64);
        logger.debug("Random title: " + title);

        inputPaths = new ArrayList<>();
        inputPaths.add(workingDirectory.resolve(Paths.get(inputFilename)));

        outputPath = workingDirectory.resolve("..");
        outputFilePath = outputPath.resolve(filename + ext);
        Files.deleteIfExists(outputFilePath);
    }

    @After
    public void tearDown() throws Exception {
        Files.deleteIfExists(outputFilePath);
    }

    @Test
    public void testConvertCreatesFilesWithImages() throws Exception {

        testConvertCreatesFiles(img_converter);
    }

    @Test
    public void testConvertCreatesFilesWithoutImages() throws Exception {
        testConvertCreatesFiles(dom_converter);
    }

    /**
     * Generic test runner
     *
     * @param formulaConverter an implementation of FormulaConverter interface
     * @throws Exception
     */
    private void testConvertCreatesFiles(FormulaConverter formulaConverter) throws Exception {
        converter.setFormulaConverter(formulaConverter);

        assertTrue(Files.exists(inputPaths.get(0)));
        assertTrue(!Files.exists(outputFilePath));

        try {
            converter.setInputPaths(inputPaths);
            converter.setOutputPath(outputPath);
            converter.setFilename(filename);
            converter.setTitle(title);
            converter.setDebugMarkupOutput(false);

            converter.convert();
            assertTrue(Files.exists(outputFilePath));
            assertTrue(Files.isRegularFile(outputFilePath));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            fail("Unexpected Exception thrown: " + e.getMessage());
        }
    }

    /**
     * tests the renaming scheme for handling existing filenames
     *
     * @throws Exception
     */
    @Test
    public void testConvertFileExists() throws Exception {
        Path existingFile = outputPath.resolve(filename + ext);
        assertTrue(Files.exists(existingFile) == false);
        Files.createFile(existingFile);
        assertTrue(Files.exists(existingFile));

        // Modify expected output file location
        outputFilePath = outputPath.resolve(filename + " (1)" + ext);
        testConvertCreatesFiles(dom_converter);
        assertTrue(Files.exists(existingFile));
        assertTrue(Files.exists(outputFilePath));

        // delete created stub file
        Files.deleteIfExists(existingFile);
    }
}
