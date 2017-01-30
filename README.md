Latex to Mobi Formula Converter
=============================

Bachelor Thesis

Michael Auß
e0525937@student.tuwien.ac.at

Interactive Media Systems, E188/2,
Favoritenstrasse 9-11, 4th floor,
1040 Wien, Austria

[![GitHub version](https://badge.fury.io/gh/sevyls%2Flatex-formulas-mobi-converter.svg)](http://badge.fury.io/gh/sevyls%2Flatex-formulas-mobi-converter) [![Build status](https://travis-ci.org/Sevyls/latex-formulas-mobi-converter.svg?branch=master "Build status")](https://travis-ci.org/Sevyls/latex-formulas-mobi-converter)

[https://www.ims.tuwien.ac.at/topics/239](https://www.ims.tuwien.ac.at/topics/239)

Interactive Media Systems is a multimedia research group at the
Institute of Software Technology and Interactive Systems, part of the
Faculty of Informatics at the Vienna University of Technology.

## About This Topic

"Develop a best-effort algorithm for the conversion of formulas set in Latex to Mobi (i.e. HTML 4). Currently, formulas are the biggest hurdle for the conversion of Latex documents into e-books. This component - if well done - would help providing costly scientifc books as cheap e-books."

## Introduction

The main purpose of this software is to provide the ability to automatically convert LaTeX documents into MOBI-eBooks, 
with the special interest in correct and automated rendering of mathematical formulas on Amazon Kindle eBook reader devices. 
Using the well known web markup and styling languages HTML and CSS, the rendering is independent from different screen sizes and densities.

## Libraries used

### Apache Commons
* CLI - [http://commons.apache.org/proper/commons-cli/](http://commons.apache.org/proper/commons-cli/)
* Configuration - [http://commons.apache.org/proper/commons-configuration/](http://commons.apache.org/proper/commons-configuration/)
* Exec - [http://commons.apache.org/proper/commons-exec/](http://commons.apache.org/proper/commons-exec/)
* IO - [http://commons.apache.org/proper/commons-io/](http://commons.apache.org/proper/commons-io/)

### Others
* Apache Log4j - [http://logging.apache.org/log4j/](http://logging.apache.org/log4j/)
* JDOM - [http://www.jdom.org/](http://www.jdom.org/)
* SnuggleTeX - [http://www2.ph.ed.ac.uk/snuggletex/](http://www2.ph.ed.ac.uk/snuggletex/)
* JEuclid - ([http://www.sourceforge.net/projects/jeuclid/](http://www.sourceforge.net/projects/jeuclid/))

## Requirements

## Java 7 or higher

You need to have the Java Runtime Environment installed in order to run this software.
Download Java here: [http://www.java.com](http://www.java.com/)

### Pandoc 1.13 or higher
This software makes great use of pandoc's LaTeX to HTML converter. 

Download & install from [http://johnmacfarlane.net/pandoc/](http://johnmacfarlane.net/pandoc/)

**Add the installed pandoc executable/directory to your operating system's PATH variable.**

##### OR: CLI arguments

Ad-hoc configuration is possible by passing the following *optional* command line argument:

``-p / --pandoc-exec <pandoc-filepath>``

Notice: this overrides all other previous discussed settings.

### Amazon KindleGen

This is the standard way to generate a .Mobi-File.

Download & install from [http://www.amazon.com/gp/feature.html?docId=1000765211](http://www.amazon.com/gp/feature.html?docId=1000765211)

**Add the installed kindlegen executable/directory to your operating system's PATH variable.**

##### OR: CLI arguments

Ad-hoc configuration is possible by passing the following *optional* command line argument:

``-k / --kindlegen-exec <KindleGen exec path>``

### Alternative: Calibre 

Calibre comes with a bunch of CLI tools including an ebook converter.
As an alternative to the proprietary Amazon KindleGen you are able to use latex2mobi with calibre instead.

As with the above executables the tool ``ebook-convert`` requires you to
 
**add the ebook-convert executable to your PATH variable** 

#### **OR** you specify its path with the following option:
 
``-c / --calibre-exec <calibre's ebook-convert exec path>``

## Run

### Usage

The default help dialogue gives a glimpse about the flags.
Required flag: -i

```
$ latex2mobi -h

usage: latex2mobi
LaTeX Formulas to Mobi Converter
(c) 2014-2015 by Michael Auss
 -c,--calibre-exec <arg>     Calibre executable location
 -d,--debugMarkupOutput      show debug output in html markup
 -f,--filename <arg>         output filename
 -h,--help                   show this help
 -i,--inputPaths <arg>       inputPaths file
 -k,--kindlegen-exec <arg>   Amazon KindleGen executable location
 -m,--export-markup          export html markup
 -n,--no-mobi                no Mobi conversion, just markup, NOTE: makes
                             -m implicit!
 -o,--output-dir <arg>       output directory
 -p,--pandoc-exec <arg>      pandoc executable location
 -r,--replace-with-images    replace latex formulas with pictures,
                             override html
 -t,--title <arg>            Document title
 -u,--use-calibre            use calibre ebook-convert instead of
                             kindlegen
```

### Example

Run program with an input file ``example.tex``:

``latex2mobi -i example.tex`` 

Results in a file ``example.mobi``.

## Build-Requirements

### Java Development Kit (JDK) 7 or higher

This software uses the Java SE JDK 7.

* Download & install from here [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

### Maven

This project is built with Maven 3.3.x

* Download & install from here [http://maven.apache.org/download.cgi](http://maven.apache.org)

### IntelliJ IDEA (optional)

This project was built with IntelliJ IDEA and comes with a configured dual module project. 
Just checkout the git repository and open the project's root directory with IntelliJ IDEA.

Find IntelliJ IDEA here: [https://www.jetbrains.com/idea/](https://www.jetbrains.com/idea/)

## Conversion strategy

Converting LaTeX and rendering formulas is a complex topic. I want to give a short explanation about my main conversion strategy:

### 1. Main document structure conversion (without formulas)

The complete LaTeX code will be transformed into an HTML representation, **except for the formulas**.

Initially Pandoc converts the LaTeX document input file to HTML5.

We use Pandoc's options just to mark the formulas with a "LaTeX" HTML class.

Rendering of other document parts is up to Pandoc alone. Some basic styling for tables and font sizes are set in ``main.css``

### 2. Converting all formulas from LaTeX to MathML with SnuggleTeX

In order to transform your LaTeX formulas for displaying inside a Mobi-/Kindle Format-Ebook,
we have to generate markup. A known standard for this is MathML and its presentation markup.
This project makes heavy use of **SnuggleTeX** to parse LaTeX **Formulas** and generate MathML2.

#### LaTeX Support Notes

For details see SPECIFICATION.md in latex2mobi-converter directory!

##### Note: SnuggleTeX only supports a subset of LaTeX:
http://www2.ph.ed.ac.uk/snuggletex/documentation/supported-latex.html

##### Note: SnuggleTeX requires clean LaTeX syntax

for correct parsing!

i.e.: LaTeX Command Arguments *have to be correctly surrounded by curly brackets* like:

This Command works: `\newcommand{\rfrac}[2]{{}^{#1}\!/_{#2}}`

**DOES NOT WORK:** `\newcommand\rfrac[2]{{}^{#1}\!/_{#2}}`

### 3. Transforming all formulas from MathML to pure HTML markup and applying a static stylesheet

At this step, every formula has been transformed into MathML which will be rendered to a pure HTML+CSS output.
The converter replaces each LaTeX formula with the resulting Markup.

#### Info:
As of today the Kindle devices and apps do not support rendering of MathML of any kind.
This software package makes reasonable assumptions for rendering MathML with the Mobi/Kindle Format *supported
subset* of HTML5 + CSS3.
On Kindle devices or apps, you **can not** use any JavaScript or other DOM processing languages.


### 4. Converting the resulting HTML document to the MOBI-Format

The last step for completion is the step to convert the HTML+CSS into a readable Mobi-Ebook.

The command line version of latex2mobi currently includes only an HtmlToMobiConverter which uses Amazon's Kindlegen Executable.

The Calibre plugin produces HTML5 markup and a CSS stylesheet which will be passed to a Calibre-builtin output converter. Mobi or other output files can be chosen from within calibre, see the plugin's [README](https://github.com/Sevyls/latex-formulas-mobi-converter/tree/master/calibre-plugin) for more information.    


## Known Issues

### Rendering issues

* LaTeX which results in MathML-Elements of munder, mover or similar
* Dynamically sized brackets, braces, parantheses etc. (except for Matrices), over- or underlines will not be rendered correctly

## Trademark Notice
Amazon, Kindle, Fire and all related logos are trademarks of Amazon.com, Inc. or its affiliates.

