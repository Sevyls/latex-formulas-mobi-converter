package at.ac.tuwien.ims.latex2mobiformulaconv.converter.html2mobi;

import org.apache.commons.cli.Option;

import java.io.File;
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
 * Converts HTML to Mobi by using the Calibre CLI
 *
 * @author mauss
 *         Created: 21.05.14 00:12
 */
public class CalibreHtmlToMobiConverter implements HtmlToMobiConverter {
    private Path execPath = null;

    @Override
    public File convertToMobi(File htmlFile) {
        // TODO implement
        return null;
    }

    @Override
    public Option getExecOption() {
        return new Option("c", "calibre-exec", true, "Calibre executable location");
    }

    @Override
    public void setExecPath(Path execPath) {
        this.execPath = execPath;
    }
}
