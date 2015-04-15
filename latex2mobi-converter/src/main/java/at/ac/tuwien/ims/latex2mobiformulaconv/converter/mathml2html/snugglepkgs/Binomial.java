package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.snugglepkgs;

import uk.ac.ed.ph.snuggletex.SnugglePackage;
import uk.ac.ed.ph.snuggletex.definitions.LaTeXMode;
import uk.ac.ed.ph.snuggletex.definitions.TextFlowContext;
import uk.ac.ed.ph.snuggletex.semantics.MathOperatorInterpretation;

import static uk.ac.ed.ph.snuggletex.definitions.Globals.ALL_MODES;

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
 *         Created: 21.05.14 00:04
 *         <p/>
 *         Binomial representation
 */
public class Binomial implements SnugglePackageProvider {
    @Override
    public SnugglePackage provide() {
        SnugglePackage binomPackage = new SnugglePackage("binom");
        MathOperatorInterpretation binomInterpretation = new MathOperatorInterpretation("binom");
        LaTeXMode[] argModes = {LaTeXMode.MATH, LaTeXMode.MATH};

        binomPackage.addComplexCommand("binom", false, 2, ALL_MODES, argModes, new BinomHandler(), TextFlowContext.ALLOW_INLINE);

        return binomPackage;
    }
}
