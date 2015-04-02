# Latex Formulas Input Conversion Plugin for Calibre 2.19.x or higher
# Created by Michael Auß
# michael.auss@gmail.com
#
# Part of the "LaTeX2Mobi Formula Converter"-Project on GitHub:
# https://github.com/Sevyls/latex-formulas-mobi-converter/

install:
	calibre-customize -b calibre_plugins/latexformulas_input

reinstall:
	calibre-debug -s; calibre-customize -b calibre_plugins/latexformulas_input;

uninstall:
	calibre-customize -r "LaTeX Formula Conversion Plugin"

debug-gui:
	calibre-debug -g

download-calibre-sources:
	git clone --depth=1 git://github.com/kovidgoyal/calibre.git ../calibre-src

clean:
	rm -rf ../calibre-src
	rm -rf ../build
	rm -f calibre_plugins/latexformulas_input/latex2mobi-converter.jar
	mvn clean -f "../../latex2mobi-converter/pom.xml" -q

build-zip:
	mvn package -f "../../latex2mobi-converter/pom.xml" -q
	cp ../../latex2mobi-converter/target/latex2mobi-converter-?.?.?.jar calibre_plugins/latexformulas_input/latex2mobi-converter.jar
	mkdir -p ../build
	cd calibre_plugins/latexformulas_input && zip -r ../../../build/calibre-latexformulas_input-plugin.zip *

