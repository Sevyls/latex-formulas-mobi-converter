package at.ac.tuwien.ims.latex2mobiformulaconv.tests.utils;

import org.apache.log4j.Logger;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
 *         Date: 08.10.2014
 *         Time: 21:07
 */
public class TestUtils {
    private static Logger logger = Logger.getLogger(TestUtils.class);

    /**
     * Returns the Working Directory of a given Test class
     *
     * @param clazz The class which will be searched upon
     * @return Java NIO Path Object for the Working Directory
     */
    public static Path getWorkingDirectory(Class clazz) {
        Path workingDirectory = null;

        try {
            workingDirectory = Paths.get(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
            logger.debug("Working directory for Class "
                    + clazz.getCanonicalName() + ": "
                    + workingDirectory.toAbsolutePath().toString());
        } catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
        }

        return workingDirectory;
    }


}
