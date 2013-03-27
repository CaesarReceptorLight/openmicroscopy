#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
   Integration test focused on the omero.api.ITypes interface
   a running server.

   Copyright 2009 Glencoe Software, Inc. All rights reserved.
   Use is subject to license terms supplied in LICENSE.txt

"""
import unittest, time
import integration.library as lib
import omero
from omero.rtypes import *

class TestTypes(lib.ITest):

    # ticket:1436
    def testGetEnumerationTypes(self):
        i = self.client.sf.getTypesService().getEnumerationTypes()

    def testAllEnumerations(self):
        types = self.root.sf.getTypesService()
        rv = dict()
        for e in types.getOriginalEnumerations():
            if rv.get(e.__class__.__name__) is None:
                rv[e.__class__.__name__] = list()
            rv[e.__class__.__name__].append(e)
        
        for r in rv:
            types.allEnumerations(str(r))

    def testGetEnumerationWithEntries(self):
        i = self.root.sf.getTypesService().getEnumerationsWithEntries().items()
    
    def testManageEnumeration(self):
        from omero_model_ExperimentTypeI import ExperimentTypeI
        uuid = self.root.sf.getAdminService().getEventContext().sessionUuid
        types = self.root.sf.getTypesService()
        
        # create enums
        obj = ExperimentTypeI()
        obj.setValue(rstring("test_value_%s" % uuid))
        enum = types.createEnumeration(obj)
        types.deleteEnumeration(enum)
        
        obj = ExperimentTypeI()
        obj.setValue(rstring("test_value2_%s" % (uuid)))
        new_entries=[obj]
        types.updateEnumerations(new_entries)
        
        types.resetEnumerations("ExperimentTypeI")
        
if __name__ == '__main__':
    unittest.main()
