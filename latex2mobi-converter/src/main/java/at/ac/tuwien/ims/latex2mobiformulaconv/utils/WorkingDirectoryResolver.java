package at.ac.tuwien.ims.latex2mobiformulaconv.utils;

import org.apache.log4j.Logger;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
 *         Date: 02.01.2015
 *         Time: 12:46
 *         <p/>
 *         Helper class for resolving the current working directory of a class
 */
public class WorkingDirectoryResolver {
    private static final Logger logger = Logger.getLogger(WorkingDirectoryResolver.class);

    /**
     * Returns the path of the working directory of a given class
     *
     * @param clazz Class object
     * @return a Path object of the working directory or null if it could not be resolved
     */
    public static Path getWorkingDirectory(Class clazz) {
        Path workingDirectory = null;
        try {
            workingDirectory = Paths.get(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
            if (workingDirectory.toFile().isFile()) {
                workingDirectory = workingDirectory.getParent();
            }
            logger.debug("Working Directory: " + workingDirectory);
        } catch (URISyntaxException e) {
            logger.error("Working directory could not be resolved!");
            logger.error(e.getMessage(), e);
        }

        return workingDirectory;
    }
}
