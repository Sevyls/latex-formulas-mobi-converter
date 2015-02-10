#!/usr/bin/env python
# vim:fileencoding=UTF-8:ts=4:sw=4:sta:et:sts=4:ai
from __future__ import (unicode_literals, division, absolute_import,
                        print_function)

import os

from lxml import html
from lxml.html.builder import (
    HTML, HEAD, TITLE, BODY, LINK, META, P, SPAN, BR, DIV, SUP, A, DT, DL, DD, H1)

from calibre import guess_type
from calibre.customize.conversion import InputFormatPlugin
from calibre.ebooks.metadata.opf2 import OPFCreator


class LaTexFormulasInputPlugin(InputFormatPlugin):
    name = 'LaTeX Formula Conversion Plugin'
    description = 'TODO Description'
    supported_platforms = ['windows', 'osx', 'linux']
    author = 'Michael Auß'
    version = (0, 0, 1)
    minimum_calibre_version = (2, 19, 0)
    file_types = set(['tex'])

    # Empty html page for test purposes
    html = HTML(
        HEAD(
            META(charset='utf-8'),
            TITLE('Testoutput'),
            LINK(rel='stylesheet', type='text/css', href='main.css'),
        ),
        BODY(
            H1('Stubbed test output'),
        )
    )

    # TODO config widget
    # def gui_configuration_widget(self, parent, get_option_by_name,
    #                             get_option_help, db, book_id=None):
    #    from calibre_plugins.latexformulas_input import PluginWidget
    #    return PluginWidget(parent, get_option_by_name, get_option_help, db, book_id)

    def convert(self, stream, options, file_ext, log, accelerators):
        log('Enter convert() ...')
        dest_dir = os.getcwdu()  # TODO temp dir
        log('dest_dir: ' + dest_dir)

        mi = None  # TODO

        #temp_file = self.temporary_file('opf')
        #log('temp_file: ' + temp_file._name)

        opf = OPFCreator(dest_dir, mi)

        # Currently this produces just a test html page
        log.debug('Convert test HTML to raw output')
        raw = html.tostring(self.html, encoding='utf-8', doctype='<!DOCTYPE html>')
        rawOutputPath = os.path.join(dest_dir, 'index.html')
        log.debug('Write raw to output file to: ' + rawOutputPath)
        with open(rawOutputPath, 'wb') as f:
            f.write(raw)

        # TODO call latex2mobi with markup output only
        # TODO copy/move output files to dest_dir

        log.debug('CreateManifestFromFilesIn()')
        opf.create_manifest_from_files_in([dest_dir])
        for item in opf.manifest:
            if item.media_type == 'text/html':
                log.debug('Item ' + str(item) + ' is of type text/html')
                item.media_type = guess_type('a.xhtml')[0]
                log.debug('Guess type result: ' + item.media_type)

        log.debug('Create_spine()')
        opf.create_spine(['index.html'])

        output_path = os.path.join(dest_dir, 'metadata.opf')
        with open(output_path, 'wb') as of:
            opf.render(of)

        log('Exit convert() ...')
        return output_path

