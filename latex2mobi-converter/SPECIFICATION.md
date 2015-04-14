# latex2mobi - LaTeX Formulas syntax support
##### Michael Auß - April 2015

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

The following variant character sets are currently supported:

* Fraktur 
* Script
* Double-struck 

For details see http://www.w3.org/TR/MathML2/chapter6.html#chars.letter-like-tables

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

Nested fractions are supported for 4 levels of depth, deeper levels are mostly unreadable on kindle devices. Font size decreases to 85% with every level of nesting.

## Roots

Roots are fully supported - constructed by a the root character ``√`` in Unicode, an overline for the roots base und an optional superscript for the root's degree.

``\sqrt{x}`` - square root without an explicit degree

``\sqrt[n]{x}`` - n-th root with explicit degree - note that SnuggleTeX requires the correct syntax of brackets for the sqrt command's arguments to work properly.

## Sums

Basic sums including simple limits are supported.
The placement of the limits results to the right of the sigma symbol, as inline notation.
 Therefore complex wide and tall limits could result in deformation or hard readability.

## Integrals

Basic integrals are supported including their limit notation.
The placement of the limits results to the right of the integral symbol, as inline notation.
Therefore complex wide and tall limits could result in deformation or hard readability.

## Trigonomic, logarithmic, limits and other mathematical functions

Common mathematical functions are supported, see details under heading "Mathematical functions" at
http://www2.ph.ed.ac.uk/snuggletex/documentation/math-mode.html

## Parantheses, brackets, braces and delimiters

These are fully supported as symbols in every possible construct.  

Note: **Spanning** braces or parantheses of any kind are not supported. 
They have to be dynamically resized, which in turn is not supported by HTML & CSS.
Design ideas went into building surrounding ``<table>`` constructs and forming the border as implemented in handling of Matrices.

## Over- and underline, Over- and underbraces

As described in the former paragraphs, spanning constructs as braces and overlines are difficult to construct. 
Currently there is no support for this notation.

## Stretching of characters is not supported

Given the recommendations for rendering stretched operators like parantheses or over- and underlines here:
 http://www.w3.org/TR/MathML2/chapter3.html#id.3.2.5.8
 
NOTE: This behaviour is currently not supported due to relative scaling issues between containing element and text.

## Matrices

The following Matrix types are fully supported:

* matrix (no parentheses, brackets of any kind)
* pmatrix (parentheses)
* bmatrix (brackets)
* vmatrix (single vertical lines)
* Vmatrix (double vertical lines)

Bmatrix: Curly braces are not supported, but will be represented as normal round parentheses.

Specifying cell alignment is currently not supported (like in ``\begin{matrix*}[r]``) and will lead to broken output of the argument ``[r]``.

## Tables

Basic table support comes with pandoc. A default styling for tables is included.
Styling cells and borders with ``\hline`` and ``\cline`` will be ignored.
A default border-collapse style is in place for reading purposes.


## Text in equations 

Text in formulas is supported by the Mtext element of MathML. 

### Inline and Displayed Formulas

TODO

## Formula Numbering & referencing 

Commands like ``\label{eq:xdef}`` and ``\ref{eq:xdef}`` are currently not supported by SnuggleTeX and will lead to broken output. 

## Defining new commands

To define new commands you can use ``\newcommand`` as intended, but be aware of the syntax: curly braces are necessary.

Example:

``\newcommand{\fett}[1]{{\bf #1}}`` 

Now you are able to use ``\fett{bold text with german command name}`` within your LaTeX documents.
 Pandoc (document) and SnuggleTeX (formulas) support this construct.