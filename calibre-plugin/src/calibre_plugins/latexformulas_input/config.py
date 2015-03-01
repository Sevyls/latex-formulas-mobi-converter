#!/usr/bin/env python
# vim:fileencoding=UTF-8:ts=4:sw=4:sta:et:sts=4:ai
from __future__ import (unicode_literals, division, absolute_import,
                        print_function)

from PyQt5.Qt import QWidget, QHBoxLayout, QVBoxLayout, QLabel, QLineEdit

from calibre.utils.config import JSONConfig

prefs = JSONConfig('plugins/latexformulas_input')

# Set defaults
prefs.defaults['pandoc_exec'] = ''
prefs.defaults['java_exec'] = ''


class ConfigWidget(QWidget):
    def __init__(self):
        QWidget.__init__(self)
        self.l = QVBoxLayout()
        self.setLayout(self.l)

        self.pandoc_label = QLabel('Pandoc Executable path:')
        self.l1 = QHBoxLayout()
        self.l.addLayout(self.l1)
        self.l1.addWidget(self.pandoc_label)

        self.pandoc_path = QLineEdit(self)
        self.pandoc_path.setText(prefs['pandoc_exec'])
        self.l1.addWidget(self.pandoc_path)
        self.pandoc_label.setBuddy(self.pandoc_path)


        self.l2 = QHBoxLayout()
        self.l.addLayout(self.l2)
        self.java_label = QLabel('Java Executable path:')
        self.l2.addWidget(self.java_label)

        self.java_path = QLineEdit(self)
        self.java_path.setText(prefs['java_exec'])
        self.l2.addWidget(self.java_path)
        self.java_label.setBuddy(self.java_path)

        self.resize(self.sizeHint())

    def save_settings(self):
        prefs['pandoc_exec'] = unicode(self.pandoc_path.text())
        prefs['java_exec'] = unicode(self.java_path.text())