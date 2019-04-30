/*
  *   $Id$
  *
  * Copyright (C) 2016 Friedrich Schiller University Jena.
  * Mathematics and Computer Science Department 
  * All rights reserved.
  *
  * Written by: Daniel Walther, Daniel.Walther(at)uni-jena.de
  *
  */

#ifndef RECEPTOR_LIGHT_SEARCH_SERVICE_ICE
#define RECEPTOR_LIGHT_SEARCH_SERVICE_ICE

#include <omero/ServicesF.ice>

module omero
{
    module api
    {
        ["java:type:java.util.ArrayList<omero.model.RlSearchResult>:java.util.List<omero.model.RlSearchResult>"]
        sequence<omero::model::RlSearchResult> ResultList;
    
        ["ami", "amd"] interface IReceptorLightSearchService extends ServiceInterface
        {
            idempotent ResultList search(string pattern) throws ServerError;
            idempotent ResultList searchByName(string pattern, string memberName) throws ServerError;
            idempotent ResultList searchByType(string pattern, string typeName) throws ServerError;
            idempotent ResultList searchByNameAndType(string pattern, string memberName, string typeName) throws ServerError;
        };
    };
};

#endif
