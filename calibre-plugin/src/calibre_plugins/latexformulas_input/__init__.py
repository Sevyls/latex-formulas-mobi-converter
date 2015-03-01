#!/usr/bin/env python
# vim:fileencoding=UTF-8:ts=4:sw=4:sta:et:sts=4:ai
from __future__ import (unicode_literals, division, absolute_import,
                        print_function)

import os

from calibre import guess_type
from calibre.customize.conversion import InputFormatPlugin
from calibre.ebooks.metadata.opf2 import OPFCreator

JAR_FILENAME = 'latex2mobi-converter.jar'  # TODO build

class LaTexFormulasInputPlugin(InputFormatPlugin):
    name = 'LaTeX Formula Conversion Plugin'
    description = 'TODO Description'
    supported_platforms = ['windows', 'osx', 'linux']
    author = 'Michael Auß'
    version = (0, 0, 1)
    minimum_calibre_version = (2, 19, 0)
    file_types = set(['tex'])

    java_exec = 'java'
    pandoc_exec = 'pandoc'

    plugin_dir = ''

    actual_plugin = 'calibre_plugins.latexformulas_input:LaTexFormulasInputPlugin'

    def initialize(self):
        self.plugin_dir = os.path.abspath(os.path.join(self.plugin_path, os.path.pardir))
        from calibre_plugins.latexformulas_input.config import prefs
        if not prefs['java_exec']:
            print('java_exec not set in configuration! trying auto-configuration...')
            try:
                # Usually java sets a JAVA_HOME variable
                self.java_exec = os.path.join(os.getenv('JAVA_HOME'), 'bin', 'java')
                prefs['java_exec'] = unicode(self.java_exec)
            except KeyError:
                # if not the last resort is the PATH environment...
                self.java_exec = 'java'

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
        from calibre_plugins.latexformulas_input.config import ConfigWidget
        return ConfigWidget()


    def save_settings(self, config_widget):
        '''
        Save the settings specified by the user with config_widget.

        :param config_widget: The widget returned by :meth:`config_widget`.
        '''
        config_widget.save_settings()

        # Apply the changes
        self.apply_settings()

    def apply_settings(self):
        from calibre_plugins.latexformulas_input.config import prefs
        # In an actual non trivial plugin, you would probably need to
        # do something based on the settings in prefs
        print('java_exec: ' + prefs['java_exec'])
        print('pandoc_exec: ' + prefs['pandoc_exec'])

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
            log.debug(check_output([self.java_exec, '-jar', os.path.join(self.plugin_dir, JAR_FILENAME), '-i', stream.name,
                                    '-n', '-o', dest_dir], stderr=STDOUT))  # TODO pandoc exec argument if configured
        except CalledProcessError as e:
            log.debug(e.returncode)
            log.debug(e.cmd)
            log.debug(e.output)

        opf = OPFCreator(dest_dir, mi)
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
