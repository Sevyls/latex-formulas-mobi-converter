Latex to Mobi Formula Converter
=============================

Bachelor Thesis

Michael Auß
e0525937@student.tuwien.ac.at

![Build status](https://travis-ci.org/Sevyls/latex-formulas-mobi-converter.svg?branch=master "Build status")

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

## Requirements

### Pandoc
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



#// TODO rest of documentation.

