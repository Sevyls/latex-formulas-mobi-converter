package at.ac.tuwien.ims.latex2mobiformulaconv.converter;

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
        title = RandomStringUtils.random(64);
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
