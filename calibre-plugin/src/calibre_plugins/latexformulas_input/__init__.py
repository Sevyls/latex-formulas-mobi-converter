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

JAR_FILENAME = 'latex2mobi-converter-0.5.1.jar'

class LaTexFormulasInputPlugin(InputFormatPlugin):
    name = 'LaTeX Formula Conversion Plugin'
    description = 'TODO Description'
    supported_platforms = ['windows', 'osx', 'linux']
    author = 'Michael Auß'
    version = (0, 0, 1)
    minimum_calibre_version = (2, 19, 0)
    file_types = set(['tex'])

    plugin_dir = ''



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

    def initialize(self):
        # print('Initializing...')
        #print('Plugin path: ' + self.plugin_path)

        self.plugin_dir = os.path.abspath(os.path.join(self.plugin_path, os.path.pardir))
        #print("Plugin directory: " + plugin_dir)

        # extract latex2mobi jar from plugin zip to plugin directory
        jar_file = get_resources(JAR_FILENAME)
        with open(os.path.join(self.plugin_dir, JAR_FILENAME), 'wb') as f:
            f.write(jar_file)

    def is_customizable(self):
        '''
        This method must return True to enable customization via
        Preferences->Plugins
        '''
        return True


    def config_widget(self):
        # TODO config widget
        return None


    def save_settings(self, config_widget):
        # TODO
        return None


    # TODO GUI Configuration?
    # def gui_configuration_widget(self, parent, get_option_by_name,
    #                            get_option_help, db, book_id=None):
    #   from calibre_plugins.latexformulas_input.latexformulas_input import PluginWidget
    #
    #   return PluginWidget(parent, get_option_by_name, get_option_help, db, book_id)

    def customization_help(self, gui=False):
        return 'TODO customization_help'

    def convert(self, stream, options, file_ext, log, accelerators):
        log.debug('Enter convert() ...')
        dest_dir = os.getcwdu()  # note: temp dir from calibre process
        log.debug('dest_dir: ' + dest_dir)

        mi = None  # TODO

        # call latex2mobi with markup output only
        from subprocess import check_output, STDOUT, CalledProcessError

        try:
            log.debug(check_output(['java', '-jar', os.path.join(self.plugin_dir, JAR_FILENAME), '-i', stream.name,
                                    '-n', '-o', dest_dir], stderr=STDOUT))
        except CalledProcessError as e:
            log.debug(e.returncode)
            log.debug(e.cmd)
            log.debug(e.output)


        opf = OPFCreator(dest_dir, mi)

        # Currently this produces just a test html page
        # log.debug('Convert test HTML to raw output')
        #raw = html.tostring(self.html, encoding='utf-8', doctype='<!DOCTYPE html>')
        #rawOutputPath = os.path.join(dest_dir, 'index.html')
        #log.debug('Write raw to output file to: ' + rawOutputPath)
        #with open(rawOutputPath, 'wb') as f:
        #    f.write(raw)




        # TODO copy/move output files to dest_dir

        markup_dir = dest_dir + os.path.sep + os.path.basename(stream.name) + '-markup'
        log.debug('Markup-dir: ' + markup_dir)

        log.debug('CreateManifestFromFilesIn()')

        opf.create_manifest_from_files_in([markup_dir])
        for item in opf.manifest:
            if item.media_type == 'text/html':
                log.debug('Item ' + str(item) + ' is of type text/html')
                item.media_type = guess_type('a.html')[0]
                log.debug('Guess type result: ' + item.media_type)
            if item.media_type == 'text/css':
                log.debug('Item ' + str(item) + ' is of type text/css')
                item.media_type = guess_type('a.css')[0]
                log.debug('Guess type result: ' + item.media_type)

        log.debug('Create_spine()')

        opf.create_spine([os.path.basename(markup_dir) + os.path.sep + 'latex2mobi.html'])

        output_path = os.path.join(dest_dir, 'metadata.opf')
        with open(output_path, 'wb') as of:
            opf.render(of)

        log('Exit convert() ...')
        return output_path

