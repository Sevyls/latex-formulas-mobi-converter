#!/usr/bin/env python
# vim:fileencoding=UTF-8:ts=4:sw=4:sta:et:sts=4:ai
from __future__ import (unicode_literals, division, absolute_import,
                        print_function)

# The class that all Interface Action plugin wrappers must inherit from
from calibre.customize.conversion import InputFormatPlugin


class LaTexFormulaConversionPlugin(InputFormatPlugin):
    name = 'LaTeX Formula Conversion Plugin'
    description = 'TODO Description'
    supported_platforms = ['windows', 'osx', 'linux']
    author = 'Michael Auß'
    version = (0, 0, 1)
    minimum_calibre_version = (2, 5, 0)
    file_types = set(['tex'])

    def convert(stream, options, file_ext, log, accelerators):
        log.info('Enter convert() ...')
        # TODO
        log.info('Exit  convert() ...')
