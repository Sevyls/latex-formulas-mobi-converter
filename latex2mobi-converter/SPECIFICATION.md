# latex2mobi - LaTeX Formulas syntax support
##### Michael Auß - March 1st, 2015

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

## Symbols

TODO

### Greek letters

TODO

### Variant Characters

TODO

### Plusminus sign

TODO

## Powers

TODO

## Indices

TODO

## Accents

TODO

## Fractions

TODO

## Roots

TODO

## Sums

TODO

## Integrals

TODO

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