#!/usr/bin/env python
# vim:fileencoding=utf-8
from __future__ import (unicode_literals, division, absolute_import,
                        print_function)

from calibre.gui2.convert import Widget


class PluginWidget(Widget):
    # TODO This is currently not used AND not working.
    TITLE = _('LaTeX Formula Input')
    COMMIT_NAME = 'latexformulas_input'
    ICON = None  # TODO ?
    HELP = ''  # TODO

    def __init__(self, parent, get_option, get_help, db=None, book_id=None):
        import traceback

        traceback.print_stack()
        Widget.__init__(self, parent,
            [])  # TODO add default Options
        self.initialize_options(get_option, get_help, db, book_id)
