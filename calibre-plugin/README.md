LaTeX Formulas Input Conversion Plugin
======================================

This project includes a standalone plugin for Calibre (an ebook management program, http://www.calibre-ebook.com/)

## Requirements

* [Java 7 or above](http://java.com)
* [Pandoc](http://johnmacfarlane.net/pandoc/)

## Download

https://github.com/Sevyls/latex-formulas-mobi-converter/releases/download/v0.6.0/latex2mobi.calibre.plugin.zip

## Installation

1. Open up Calibre and go to `Settings->Plugins->"Load plugin from File"`
2. Choose the `latex2mobi.calibre.plugin.zip`
3. Accept the security question for installing a plugin
4. Done.

## Required Configuration

### With Plugin configuration

1. In Calibre go to `Settings->Plugins`
2. Choose the `LaTeX Formula Conversion Plugin` and click on `Customize plugin`.
3. Set the path for your Java executable
4. Set the path for your Pandoc installation

### Alternative: With system variables

1. Set the system variable `JAVA_HOME` to your Java installation
2. Add your pandoc executable to the system's `PATH` variable


# Build 

This project comes with a simple Makefile.

## Requirements

* Java JDK 7 or above
* Maven 3.x
* Calibre

For producing a standalone zip file, run ``make build-zip``

This will build a latex2mobi jar with Maven and zips it into ``calibre-plugin/build``

For debugging purposes run ``make install`` and ``make debug-gui`` to start Calibre in debug mode.

Note: Calibre binaries must be in the PATH variable


