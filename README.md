Latex to Mobi Formula Converter
=============================

Bachelor Thesis

Michael Auß
e0525937@student.tuwien.ac.at

[![GitHub version](https://badge.fury.io/gh/sevyls%2Flatex-formulas-mobi-converter.svg)](http://badge.fury.io/gh/sevyls%2Flatex-formulas-mobi-converter) [![Build status](https://travis-ci.org/Sevyls/latex-formulas-mobi-converter.svg?branch=master "Build status")](https://travis-ci.org/Sevyls/latex-formulas-mobi-converter)

[https://www.ims.tuwien.ac.at/topics/239](https://www.ims.tuwien.ac.at/topics/239)

Interactive Media Systems is a multimedia research group at the
Institute of Software Technology and Interactive Systems, part of the
Faculty of Informatics at the Vienna University of Technology.

## Libraries used

### Apache Commons
* CLI - [http://commons.apache.org/proper/commons-cli/](http://commons.apache.org/proper/commons-cli/)
* Configuration - [http://commons.apache.org/proper/commons-configuration/](http://commons.apache.org/proper/commons-configuration/)
* Exec - [http://commons.apache.org/proper/commons-exec/](http://commons.apache.org/proper/commons-exec/)
* IO - [http://commons.apache.org/proper/commons-io/](http://commons.apache.org/proper/commons-io/)

### Others
* Apache Log4j - [http://logging.apache.org/log4j/](http://logging.apache.org/log4j/)
* JDOM - [http://www.jdom.org/](http://www.jdom.org/)
* JEuclid - ([http://www.sourceforge.net/projects/jeuclid/](http://www.sourceforge.net/projects/jeuclid/))

## Requirements

## Java 7 or higher

You need to have the Java Runtime Environment installed in order to run this software.
Download Java here: [http://www.java.com](http://www.java.com/)

### Pandoc 1.12 or higher
This software makes great use of pandoc's LaTeX to HTML converter. 

Download & install from [http://johnmacfarlane.net/pandoc/](http://johnmacfarlane.net/pandoc/)

**Add the installed pandoc executable/directory to your operating system's PATH variable.**

##### OR: config file

Inside the config file "*configuration.properties*" you can define a permanent  
> pandoc.exec = "file:///opt/pandoc-1.12.4.2/pandoc"

##### OR: CLI arguments

Ad-hoc configuration is possible by passing the following *optional* command line argument:

> -p / --pandoc-exec <pandoc-filepath>

Notice: this overrides all other previous discussed settings.

### Amazon KindleGen

This is the standard way to generate a .Mobi-File.

Download & install from [http://www.amazon.com/gp/feature.html?docId=1000765211](http://www.amazon.com/gp/feature.html?docId=1000765211)

**Add the installed kindlegen executable/directory to your operating system's PATH variable.**


## Build-Requirements

### Java Development Kit (JDK) 7 or higher

This software uses the Java SE JDK 7.

* Download & install from here [http://www.oracle.com/technetwork/java/javase/downloads/index.html](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

### Maven

This project is built with Maven 3.3.x

* Download & install from here [http://maven.apache.org/download.cgi](http://maven.apache.org/download.cgi)





#// TODO rest of documentation.


