package at.ac.tuwien.ims.latex2mobiformulaconv.tests.integration;

import at.ac.tuwien.ims.latex2mobiformulaconv.tests.utils.AbstractMainTests;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasItem;
import static org.junit.Assert.assertThat;

/*
 * The MIT License (MIT)
 * latex2mobi -- LaTeX Formulas to Mobi Converter
 * Copyright (c) 2015 Michael Auß
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
 * Integration/Blackbox Test for the application's Main class
 *
 * @author Michael Auß
 *         Created on 11.04.15.
 * @see at.ac.tuwien.ims.latex2mobiformulaconv.app.Main
 */
public class MainApplicationTest {
    private static final Logger logger = Logger.getLogger(MainApplicationTest.class);

    private List<String> args;


    @Before
    public void setUp() throws Exception {
        logger.debug("setup()");
        args = new ArrayList<>();
        args.add("latex2mobi");
    }

    /**
     * Check Usage info on stdout
     */
    @Test
    public void testUsage() throws Exception {
        logger.debug("enter testUsage()...");
        args.add("-h");

        final String[] expected = new String[]{
                "usage: latex2mobi",
                "LaTeX Formulas to Mobi Converter",
                "(c) 2014-2015 by Michael Auss",
                " -c,--calibre-exec <arg>     Calibre executable location",
                " -d,--debugMarkupOutput      show debug output in html markup",
                " -f,--filename <arg>         output filename",
                " -h,--help                   show this help",
                " -i,--inputPaths <arg>       inputPaths file",
                " -k,--kindlegen-exec <arg>   Amazon KindleGen executable location",
                " -m,--export-markup          export html markup",
                " -n,--no-mobi                no Mobi conversion, just markup, NOTE: makes",
                "                             -m implicit!",
                " -o,--output-dir <arg>       output directory",
                " -p,--pandoc-exec <arg>      pandoc executable location",
                " -r,--replace-with-images    replace latex formulas with pictures,",
                "                             override html",
                " -t,--title <arg>            Document title"
        };

        String[] results = runMain();

        List<String> resultsList = Arrays.asList(results);

        for (String s : expected) {
            assertThat(resultsList, hasItem(s));
        }
    }


    /**
     * Runs the Main method in the Main class with args
     *
     * @return Output collected from stdout + stderr in an array of strings
     */
    private String[] runMain() {
        return AbstractMainTests.executeMainMethod("at.ac.tuwien.ims.latex2mobiformulaconv.app.Main",
                args.toArray(new String[args.size()]));
    }

    // TODO test convert with multiple option variations, successful and error output
}