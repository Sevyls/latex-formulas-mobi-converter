install:
	calibre-customize -b .

reload:
	calibre-debug -s; calibre-customize -b .; calibre

download-calibre-sources:
	git clone --depth=1 git://github.com/kovidgoyal/calibre.git ../calibre-src

clean:
	rm -rf ../calibre-src
