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

## Required Running Configuration

### With Plugin configuration

1. In Calibre go to `Settings->Plugins`
2. Choose the `LaTeX Formula Conversion Plugin` and click on `Customize plugin`.
3. Set the path for your Java executable
4. Set the path for your Pandoc installation

### Alternative: With system variables

1. Set the system variable `JAVA_HOME` to your Java installation
2. Add your pandoc executable to the system's `PATH` variable

## Example: Converting a .tex-file into .Mobi 

1. Import a .tex file into Calibre by dragging it into your library or opening it with your file chooser (choose "All files *.*"!)
2. Select the book in your Calibre library and right-click it, then choose "Convert books" -> "Convert individually"
3. Make sure your input format is **TEX**
4. Choose your output format, in our example "MOBI"
5. Choose "MOBI Output" from the icon bar on the left side
6. In "Kindle options" set the "MOBI file type" to **new** (important, the old format generator of Calibre will ignore any CSS stylesheet!)
7. Hit "OK" for converting your TeX file to a MOBI-eBook. 


### Supported conversion into the following output formats:

* MOBI (only the **"new"** Format (KF8, Kindle Format 8), check your MOBI-Output conversion options! 
* AZW3

### Tested, but unsupported output formats:

* EPUB
* PDF

In general output is optimized for Kindle Devices only - don't expect correct styles and layouts in unsupported formats, but maybe it is useful for you too.


## Build 

This project comes with a simple Makefile for building the project. It depends on the underlying Java/Maven Project "latex2mobi".

## Requirements

* Java JDK 7 or above
* Maven 3.x
* Calibre

For producing a standalone zip file, run ``make build-zip``

This will build a latex2mobi jar with Maven and zips it into ``calibre-plugin/build``

For debugging purposes build a zip file first like above, then run ``make install`` and ``make debug-gui`` to start Calibre in debug mode.

Note: Calibre executables must be in the PATH variable:

* Mac OS X: ``export PATH=$PATH:/Applications/calibre.app/Contents/MacOS/``
* Linux: find calibre-customize on your installation and add this to your PATH variable
* Windows: Do some GUI stuff... and add the Windows path for the directory containing "calibre-customize"

## Download calibre source code

For development purposes I added a make target to download the Calibre source code from GitHub.

Run ``make download-calibre-sources`` for downloading (only) the latest version of Calibre's sources.
