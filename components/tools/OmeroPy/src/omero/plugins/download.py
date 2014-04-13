#!/usr/bin/env python
# -*- coding: utf-8 -*-
"""
   download plugin

   Plugin read by omero.cli.Cli during initialization. The method(s)
   defined here will be added to the Cli class for later use.

   Copyright 2007 Glencoe Software, Inc. All rights reserved.
   Use is subject to license terms supplied in LICENSE.txt

"""

import sys
import omero
import re
from omero.cli import BaseControl, CLI
from omero.rtypes import unwrap

HELP = """Download the given file id to the given filename"""


class DownloadControl(BaseControl):

    def _configure(self, parser):
        parser.add_argument(
            "object", help="ID of the OriginalFile to download or object of"
            " form <object>:<image_id>")
        parser.add_argument(
            "filename", help="Local filename to be saved to. '-' for stdout")
        parser.set_defaults(func=self.__call__)
        parser.add_login_arguments()

    def __call__(self, args):
        from omero_model_OriginalFileI import OriginalFileI as OFile

        # Retrieve connection
        client = self.ctx.conn(args)
        file_id = self.get_file_id(client.sf, args.object)
        orig_file = OFile(file_id)
        target_file = str(args.filename)

        try:
            if target_file == "-":
                client.download(orig_file, filehandle=sys.stdout)
                sys.stdout.flush()
            else:
                client.download(orig_file, target_file)
        except omero.ValidationException, ve:
            # Possible, though unlikely after previous check
            self.ctx.die(67, "Unknown ValidationException: %s"
                         % ve.message)

    def get_file_id(self, session, value):

        if ':' not in value:
            try:
                return long(value)
            except ValueError:
                self.ctx.die(601, 'Invalid OriginalFile ID input')

        # Assume input is of form OriginalFile:id
        file_id = self.parse_object_id("OriginalFile", value)
        if file_id:
            return file_id

        query = session.getQueryService()
        params = omero.sys.ParametersI()

        # Assume input is of form FileAnnotation:id
        fa_id = self.parse_object_id("FileAnnotation", value)
        if fa_id:
            fa = query.get("FileAnnotation", fa_id)
            return fa.getFile().id.val

        # Assume input is of form Image:id
        image_id = self.parse_object_id("Image", value)
        if image_id:
            params.addLong('iid', image_id)
            sql = "select f from Image i" \
                " join i.fileset as fs" \
                " join fs.usedFiles as uf" \
                " join uf.originalFile as f" \
                " where i.id = :iid"
            query_out = query.projection(sql, params)
            return unwrap(query_out[0])[0].id.val

        self.ctx.die(601, 'Invalid object input')

    def parse_object_id(self, object_type, value):

        pattern = r'%s:(?P<id>\d+)' % object_type
        pattern = re.compile('^' + pattern + '$')
        m = pattern.match(value)
        if not m:
            return
        return long(m.group('id'))

try:
    register("download", DownloadControl, HELP)
except NameError:
    if __name__ == "__main__":
        cli = CLI()
        cli.register("download", DownloadControl, HELP)
        cli.invoke(sys.argv[1:])
