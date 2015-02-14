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
