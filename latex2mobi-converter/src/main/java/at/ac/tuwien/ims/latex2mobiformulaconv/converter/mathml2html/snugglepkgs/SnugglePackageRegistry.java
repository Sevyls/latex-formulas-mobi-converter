package at.ac.tuwien.ims.latex2mobiformulaconv.converter.mathml2html.snugglepkgs;

import uk.ac.ed.ph.snuggletex.SnugglePackage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 *
 *
 * This registry provides custom SnugglePackages for supporting more LaTeX syntax or change
 * default behaviour of SnuggleTeX
 *
 * See http://snuggletex.sourceforge.net/maven/apidocs/uk/ac/ed/ph/snuggletex/SnugglePackage.html for API reference
 *
 * @author Michael Auß
 *         Created: 13.07.14 17:05
 */
public class SnugglePackageRegistry {

    private static Map<Class, SnugglePackage> providerMap = new HashMap<Class, SnugglePackage>();

    static {
        configure();
    }

    /**
     * Add another SnugglePackage to the registry.
     * There will not be any kind of duplicate check.
     *
     * @param klass    The class of the implementing package
     * @param provider an instance of the class
     */
    public static void register(Class klass, SnugglePackageProvider provider) {
        providerMap.put(klass, provider.provide());
    }

    /**
     * static configuration for SnugglePackages
     * quasi-builtin packages are registered here
     */
    private static void configure() {
        register(Modulo.class, new Modulo());
        register(Binomial.class, new Binomial());
    }

    public static List<SnugglePackage> getPackages() {
        List<SnugglePackage> list = new ArrayList<SnugglePackage>();
        list.addAll(providerMap.values());
        return list;
    }
}
