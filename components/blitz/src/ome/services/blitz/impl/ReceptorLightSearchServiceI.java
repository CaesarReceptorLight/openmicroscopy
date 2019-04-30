 /*
  *   $Id$
  *
  *   Copyright (C) 2016 Friedrich Schiller University Jena.
  *   Mathematics and Computer Science Department 
  *   All rights reserved.
  *
  *   Written by: Daniel Walther, Daniel.Walther(at)uni-jena.de
  *
  */

package ome.services.blitz.impl;

import ome.security.SecuritySystem;
import ome.services.blitz.util.BlitzExecutor;
import ome.services.blitz.util.ServiceFactoryAware;
import omero.ServerError;
import ome.services.sessions.SessionManager;

import omero.api.AMD_IReceptorLightSearchService_search;
import omero.api.AMD_IReceptorLightSearchService_searchByName;
import omero.api.AMD_IReceptorLightSearchService_searchByType;
import omero.api.AMD_IReceptorLightSearchService_searchByNameAndType;

import ome.api.IReceptorLightSearchService;
import omero.api._IReceptorLightSearchServiceOperations;
import Ice.Current;
import omero.model.RlSearchResult;

import java.util.List;

public class ReceptorLightSearchServiceI extends AbstractAmdServant implements _IReceptorLightSearchServiceOperations, ServiceFactoryAware
{
    private ReceptorLightAccessHelper accessHelper;
    protected ServiceFactoryI factory;
    protected SessionManager sm;
    protected SecuritySystem ss;
       
    public ReceptorLightSearchServiceI(IReceptorLightSearchService service, BlitzExecutor be)
    {        
	    super(service, be);

        accessHelper = new ReceptorLightAccessHelper();
    }

    public void setSessionManager(SessionManager sm)
    {
        this.sm = sm;
    }

    public void setSecuritySystem(SecuritySystem ss)
    {
        this.ss = ss;
    }

    public void setServiceFactory(ServiceFactoryI sf)
    {
        this.factory = sf;
    }

    public void search_async(final AMD_IReceptorLightSearchService_search __cb,
	    String pattern, final Current __current)
	    throws ServerError
    {
        AMD_IReceptorLightSearchService_search callback = new AMD_IReceptorLightSearchService_search()
        {
            @Override
            public void ice_response(List<RlSearchResult> __ret)
            {
                __cb.ice_response((List<RlSearchResult>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };
        
        callInvokerOnRawArgs(callback, __current, pattern);
    }
    
    public void searchByName_async(final AMD_IReceptorLightSearchService_searchByName __cb,
	    String pattern, String memberName, final Current __current)
	    throws ServerError
    {
        AMD_IReceptorLightSearchService_searchByName callback = new AMD_IReceptorLightSearchService_searchByName()
        {
            @Override
            public void ice_response(List<RlSearchResult> __ret)
            {
                __cb.ice_response((List<RlSearchResult>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, pattern, memberName);
    }
    
    public void searchByType_async(final AMD_IReceptorLightSearchService_searchByType __cb,
	    String pattern, String typeName, final Current __current)
	    throws ServerError
    {
        AMD_IReceptorLightSearchService_searchByType callback = new AMD_IReceptorLightSearchService_searchByType()
        {
            @Override
            public void ice_response(List<RlSearchResult> __ret)
            {
                __cb.ice_response((List<RlSearchResult>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, pattern, typeName);
    }
    
    public void searchByNameAndType_async(final AMD_IReceptorLightSearchService_searchByNameAndType __cb,
	    String pattern, String memberName, String typeName, final Current __current)
	    throws ServerError
    {
        AMD_IReceptorLightSearchService_searchByNameAndType callback = new AMD_IReceptorLightSearchService_searchByNameAndType()
        {
            @Override
            public void ice_response(List<RlSearchResult> __ret)
            {
                __cb.ice_response((List<RlSearchResult>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, pattern, memberName, typeName);
    }
} 
