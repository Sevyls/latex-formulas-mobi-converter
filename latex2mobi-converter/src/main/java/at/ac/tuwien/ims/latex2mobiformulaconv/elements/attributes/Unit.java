package at.ac.tuwien.ims.latex2mobiformulaconv.elements.attributes;


import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

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
 *         Date: 05.01.2015
 *         Time: 15:03
 *
 * Implementation based on W3C MathML2 Fundamentals,
 * Chapter 2.4.4.2 Attributes with units
 * http://www.w3.org/TR/MathML2/chapter2.html#fund.units
 */
public class Unit {
    private Double number;
    private Identifier identifier;

    public static final Unit INFINITY = new Unit(Double.POSITIVE_INFINITY, null);

    private static Logger logger = Logger.getLogger(Unit.class);

    // TODO namedspace
    // namedspace is one of "veryverythinmathspace", "verythinmathspace", "thinmathspace", "mediummathspace",
    // "thickmathspace", "verythickmathspace", or "veryverythickmathspace"

    public static Unit parse(String unitText) {
        if (unitText == null) {
            return null;
        }

        for (Identifier id : Identifier.values()) {
            if (unitText.endsWith(id.getIdentifier())) {
                Double number = Double.parseDouble(unitText.replaceFirst(id.getIdentifier(), ""));

                Unit unit = new Unit(number, id.getIdentifier());

                return unit;
            }
        }

        return null;
    }

    private enum Identifier {
        EM ("em"),	        //em (font-relative unit traditionally used for horizontal lengths)
        EX ("ex"),	        //ex (font-relative unit traditionally used for vertical lengths)
        PIXELS ("px"),	    //pixels, or pixel size of the current display
        INCHES ("in"),	    //inches (1 inch = 2.54 centimeters)
        CENTIMETERS ("cm"), //centimeters
        MILLIMETERS ("mm"), //millimeters
        POINTS ("pt"),      //points (1 point = 1/72 inch)
        PICAS ("pc"),       //picas (1 pica = 12 points)
        PERCENT ("%");      // percentage of default value

        private final String identifier;

        private static final Map<String,Identifier> map;
        static {
            map = new HashMap<String,Identifier>();
            for (Identifier v : Identifier.values()) {
                map.put(v.identifier, v);
            }
        }

        public static Identifier findByValue(String i) {
            return map.get(i);
        }

        Identifier(String identifier) {
            this.identifier = identifier;
        }

        String getIdentifier() {
            return identifier;
        }
    }

    public Unit(Double number, String identifier) {
        this.number = number;
        this.identifier = Identifier.findByValue(identifier);
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public String getIdentifier() {
        return identifier.getIdentifier();
    }

    public void setIdentifierValue(String identifier) {
        this.identifier = Identifier.findByValue(identifier);
    }

    @Override
    public String toString() {
        if (number.equals(Double.POSITIVE_INFINITY)) {
            return "infinity";
        }

        String output = number.toString();
        if (identifier != null) {
            output += identifier.getIdentifier();
        }
        return output;
    }
}
