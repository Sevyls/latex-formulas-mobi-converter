# latex2mobi - LaTeX Formulas syntax support
##### Michael Auß - March 1st, 2015

## Introduction

This document describes the currently supported LaTeX formula elements and their supported & unsupported features and properties.

This software uses Pandoc for converting the main LaTeX document elements into HTML.
At this stage formulas are deliberately ignored by calling Pandoc with its option --asciimathml, resulting in a converted document except for the formulas.
All formulas stayed in their TeX syntax and have been marked with the DOM class "LaTeX".

The next stage will convert every single formula by using SnuggleTeX.

Note that SnuggleTeX supports very much of the LaTeX formula input out of the box:
http://www2.ph.ed.ac.uk/snuggletex/documentation/supported-latex.html

Additionally this project adds some SnugglePackages, which add support for additional LaTeX syntax and/or change default behaviour of SnuggleTeX.

* Binomial (``\binom``)
* Modulo (``\mod``)

This would be the main starting point to add even more syntax support or modify current behaviour.

This project includes a test file "formulas.tex" and a reference PDF converted file "formulas.pdf".
Based on the content of this file, this specification describes the support of mathematical notation elements in common categories.

## Symbols

This project builds on the extraordinary support of Unicode in all the used third party software, including Pandoc, SnuggleTeX, Amazon Kindlegen and Calibre.
Whereever possible output will be represented as Unicode encoded characters. Amazon's Kindle devices and Kindlegen software support most of the vast charsets Unicode describes.



### Greek letters

The whole greek letters command list is supported by SnuggleTeX and will result in a conversion to correct Unicode characters.
For more have a look at http://www2.ph.ed.ac.uk/snuggletex/documentation/math-mode.html under "Greek Letters"

### Variant Characters

TODO

### Binary operators

Common mathematical binary operators are supported by SnuggleTeX, for a full list find it at http://www2.ph.ed.ac.uk/snuggletex/documentation/math-mode.html

Additionally the Modulo operator "mod" was added by this software as a SnugglePackage.

## Exponent / Powers 

``x^2`` - basically a superscript, supported by the corresponding HTML tag ``<sup>``


## Indices

``n_1`` - basically a subscript, supported by the corresponding HTML tag ``<sub>``

Note: The positioning and size of exponents and indexes in relation to the base have been altered from the standard values in order to keep it readable for complex formulas.

## Accents

SnuggleTeX supports a limited, but common list of accented characters, which are mapped to the corresponding Unicode representation. 
See more at http://www2.ph.ed.ac.uk/snuggletex/documentation/text-mode.html

There are a few examples inside the formulas.tex, which are currently not supported - especially the multistroked accents.
Some others are being composed with Powers/Exponent notation with additional styling in MathML and result in overlapping output on the Kindle devices. (Vectors)

## Fractions

Fractions are fully supported, rendering of nominator and denominator results in a dynamic resizing. This is necessary especically for inline formulas.
This leads to readability issues when multiple levels of fractions are used, depending on the complexity of the formulas displayed within the fractions parts.

``\frac{n!}{k!(n-k)!}`` - this example represents the basic accepted syntax of "New style fractions" - SnuggleTeX requires correct use of the curly brackets for the nominator and denominator arguments.
Old style fractions with ``\over`` are not supported by latex2mobi.



See details at

## Roots

Roots are fully supported - constructed by a the root character ``√`` in Unicode, an overline for the roots base und an optional superscript for the root's degree.

``\sqrt{x}`` - square root without an explicit degree

``\sqrt[n]{x}`` - n-th root with explicit degree - note that SnuggleTeX requires the correct syntax of brackets for the sqrt command's arguments to work properly.

## Sums

TODO

## Integrals

TODO

## Trigonomic, logarithmic, limits and other mathematical functions

Common mathematical functions are supported, see details under heading "Mathematical functions" at
http://www2.ph.ed.ac.uk/snuggletex/documentation/math-mode.html

## Brackets, braces and delimiters

TODO

### Over- and underline, -braces

TODO

## Intervals

TODO

## Matrices, tables

TODO

## Text in equations or vice-versa

TODO

### Spaces and Text in Formulas

TODO

### Inline and Displayed Formulas

TODO

## Formula Numbering

TODO