package at.ac.tuwien.ims.latex2mobiformulaconv.tests.integration;

import at.ac.tuwien.ims.latex2mobiformulaconv.converter.Converter;
import at.ac.tuwien.ims.latex2mobiformulaconv.tests.utils.TestUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
 *         <p/>
 *         Integration test
 *         Tests the standard ways of running a conversion with the formulas.tex test resource
 */
public class ConverterTest {
    private static Logger logger = Logger.getLogger(ConverterTest.class);
    private Converter converter;
    private ArrayList<Path> inputPaths;
    private Path outputPath;
    private Path outputFilePath;
    private Path workingDirectory;
    private final String inputFilename = "formulas.tex";
    private String title;
    private final String filename = "ConverterTestFile";
    private final String ext = ".mobi";

    @Before
    public void setUp() throws Exception {
        workingDirectory = TestUtils.getWorkingDirectory(this.getClass());
        logger.info("Working directory: " + workingDirectory.toAbsolutePath().toString());
        converter = new Converter(workingDirectory);
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
        testConvertCreatesFiles(true);
    }

    @Test
    public void testConvertCreatesFilesWithoutImages() throws Exception {
        testConvertCreatesFiles(false);
    }

    /**
     * Generic test runner
     *
     * @param images flag for test mode - when true the formulas will get replaced by images, else by html+css
     * @throws Exception
     */
    private void testConvertCreatesFiles(boolean images) throws Exception {
        assertTrue(Files.exists(inputPaths.get(0)));
        assertTrue(!Files.exists(outputFilePath));

        try {
            converter.convert(inputPaths, images, outputPath, filename, title, false);
            assertTrue(Files.exists(outputFilePath));
            assertTrue(Files.isRegularFile(outputFilePath));
        } catch (Exception e) {
            e.printStackTrace();
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
        testConvertCreatesFiles(false);
        assertTrue(Files.exists(existingFile));
        assertTrue(Files.exists(outputFilePath));

        // delete created stub file
        Files.deleteIfExists(existingFile);
    }
}
