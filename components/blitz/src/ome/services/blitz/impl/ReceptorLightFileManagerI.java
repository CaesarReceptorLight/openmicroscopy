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

import java.util.ArrayList;
import java.util.List;

import ome.security.SecuritySystem;
import ome.services.blitz.util.BlitzExecutor;
import ome.services.blitz.util.ServiceFactoryAware;
import ome.services.sessions.SessionManager;
import omero.ServerError;

import omero.api.AMD_IReceptorLightFileManager_createFile;
import omero.api.AMD_IReceptorLightFileManager_createOmeroFile;
import omero.api.AMD_IReceptorLightFileManager_createDirectory;
import omero.api.AMD_IReceptorLightFileManager_deleteFile;
import omero.api.AMD_IReceptorLightFileManager_getFiles;
import omero.api.AMD_IReceptorLightFileManager_openFile;
import omero.api.AMD_IReceptorLightFileManager_closeFile;
import omero.api.AMD_IReceptorLightFileManager_appendFileData;
import omero.api.AMD_IReceptorLightFileManager_readFileData;
import omero.api.AMD_IReceptorLightFileManager_searchFiles;
import omero.api.AMD_IReceptorLightFileManager_getFileInformation;
import omero.api.AMD_IReceptorLightFileManager_setFileInformation;
import omero.api.AMD_IReceptorLightFileManager_getDataDir;

import ome.api.IReceptorLightFileManager;
import omero.api._IReceptorLightFileManagerOperations;
import Ice.Current;
import omero.model.RlFileInformation;

public class ReceptorLightFileManagerI extends AbstractAmdServant implements _IReceptorLightFileManagerOperations, ServiceFactoryAware
{
    private ReceptorLightAccessHelper accessHelper;
    protected ServiceFactoryI factory;
    protected SessionManager sm;
    protected SecuritySystem ss;
    
    public ReceptorLightFileManagerI(IReceptorLightFileManager service, BlitzExecutor be)
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

    public void createFile_async(final AMD_IReceptorLightFileManager_createFile __cb,
            String name, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightFileManager_setFileInformation callBackSetFileInfo = new AMD_IReceptorLightFileManager_setFileInformation()
        {
            @Override
            public void ice_response(long fileId)
            {
                __cb.ice_response(fileId);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        final AMD_IReceptorLightFileManager_getFileInformation callBackGetFileInfo = new AMD_IReceptorLightFileManager_getFileInformation()
        {
            @Override
            public void ice_response(RlFileInformation __ret)
            {
                if(__ret != null)
                {
                    Long uid = Long.valueOf(__ret.getUId().getValue());
                    __current.operation = "setFileInformation";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSetFileInfo, __current, __ret.getUId().getValue(), __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightFileManager_createFile callBackCreate = new AMD_IReceptorLightFileManager_createFile()
        {
            @Override
            public void ice_response(long fileId)
            {
                __current.operation = "getFileInformation";
                callInvokerOnRawArgs(callBackGetFileInfo, __current, fileId);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
	
    public void createOmeroFile_async(final AMD_IReceptorLightFileManager_createOmeroFile __cb,
	    String fileName, long omeroId, final Current __current)
	    throws ServerError {

	final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

	final AMD_IReceptorLightFileManager_setFileInformation callBackSetFileInfo = new AMD_IReceptorLightFileManager_setFileInformation()
        {
            @Override
            public void ice_response(long fileId)
            {
                __cb.ice_response(fileId);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        final AMD_IReceptorLightFileManager_getFileInformation callBackGetFileInfo = new AMD_IReceptorLightFileManager_getFileInformation()
        {
            @Override
            public void ice_response(RlFileInformation __ret)
            {
                if(__ret != null)
                {
                    Long uid = Long.valueOf(__ret.getUId().getValue());
                    __current.operation = "setFileInformation";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSetFileInfo, __current, __ret.getUId().getValue(), __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightFileManager_createOmeroFile callBackCreate = new AMD_IReceptorLightFileManager_createOmeroFile()
        {
            @Override
            public void ice_response(long fileId)
            {
                __current.operation = "getFileInformation";
                callInvokerOnRawArgs(callBackGetFileInfo, __current, fileId);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

	callInvokerOnRawArgs(callBackCreate, __current, fileName, omeroId);
    }

    public void createDirectory_async(final AMD_IReceptorLightFileManager_createDirectory __cb,
            String directoryName, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightFileManager_setFileInformation callBackSetFileInfo = new AMD_IReceptorLightFileManager_setFileInformation()
        {
            @Override
            public void ice_response(long fileId)
            {
                __cb.ice_response(fileId);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        final AMD_IReceptorLightFileManager_getFileInformation callBackGetFileInfo = new AMD_IReceptorLightFileManager_getFileInformation()
        {
            @Override
            public void ice_response(RlFileInformation __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "setFileInformation";
                    Long uid = Long.valueOf(__ret.getUId().getValue());
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSetFileInfo, __current, __ret.getUId().getValue(), __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightFileManager_createDirectory callBackCreate = new AMD_IReceptorLightFileManager_createDirectory()
        {
            @Override
            public void ice_response(long fileId)
            {
                __current.operation = "getFileInformation";
                callInvokerOnRawArgs(callBackGetFileInfo, __current, fileId);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, directoryName);
    }
    
    public void deleteFile_async(final AMD_IReceptorLightFileManager_deleteFile __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightFileManager_getFileInformation callBackGet = new AMD_IReceptorLightFileManager_getFileInformation()
        {
            @Override
            public void ice_response(RlFileInformation __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteFile";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteFile] No write access for this file!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        __current.operation = "getFileInformation";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }

    public void getFiles_async(final AMD_IReceptorLightFileManager_getFiles __cb,
            final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightFileManager_getFiles callBackGet = new AMD_IReceptorLightFileManager_getFiles()
        {
            @Override
            public void ice_response(List<RlFileInformation> __ret)
            {
                if(__ret == null)
                    __cb.ice_response(null);

                __cb.ice_response((List<RlFileInformation>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                 __cb.ice_exception(ex);
            }
        };

        __current.operation = "getFiles";
        callInvokerOnRawArgs(callBackGet, __current);
    }
    
    public void openFile_async(final AMD_IReceptorLightFileManager_openFile __cb,
            final long id, final boolean forWriting, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightFileManager_getFileInformation callBackGet = new AMD_IReceptorLightFileManager_getFileInformation()
        {
            @Override
            public void ice_response(RlFileInformation __ret)
            {
                if(__ret == null)
                    __cb.ice_response();
                
                try
                {
		            if(forWriting && !accessHelper.isObjectWritable(__ret, __current, factory, sm))
		            {
		                __cb.ice_exception(new Exception("[openFile] No write access for this file!"));
		            }
		            else if(!forWriting && !accessHelper.isObjectReadable(__ret, __current, factory, sm))
		            {
		                __cb.ice_exception(new Exception("[openFile] No read access for this file!"));
		            }
		            else
		            {
		                __current.operation = "openFile";
		                callInvokerOnRawArgs(__cb, __current, id, forWriting);
		            }
                }
                catch(Exception ex)
                {
                    __cb.ice_exception(ex);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        __current.operation = "getFileInformation";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }
    
    public void closeFile_async(AMD_IReceptorLightFileManager_closeFile __cb,
	    long fileId, Current __current)
	    throws ServerError {
	callInvokerOnRawArgs(__cb, __current, fileId);
    }
    
    public void appendFileData_async(AMD_IReceptorLightFileManager_appendFileData __cb,
	    long fileId, byte[] data, Current __current)
	    throws ServerError {
	callInvokerOnRawArgs(__cb, __current, fileId, data);
    }
    
    public void readFileData_async(AMD_IReceptorLightFileManager_readFileData __cb,
	    long fileId, long dataLength, Current __current)
	    throws ServerError {
	callInvokerOnRawArgs(__cb, __current, fileId, dataLength);
    }
    
    public void searchFiles_async(AMD_IReceptorLightFileManager_searchFiles __cb,
	    String searchPattern, Current __current)
	    throws ServerError {
	callInvokerOnRawArgs(__cb, __current, searchPattern);
    }
    
    public void getFileInformation_async(final AMD_IReceptorLightFileManager_getFileInformation __cb,
            final long fileId, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightFileManager_getFileInformation callBackGet = new AMD_IReceptorLightFileManager_getFileInformation()
        {
            @Override
            public void ice_response(RlFileInformation __ret)
            {
                if(__ret == null)
                    __cb.ice_response(null);
                
	            if(!accessHelper.isObjectReadable(__ret, __current, factory, sm))
	            {
	                __cb.ice_exception(new Exception("[openFile] No read access for this file!"));
	            }
	            else
	            {
	                __cb.ice_response(__ret);
	            }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackGet, __current, fileId);
    }

    public void setFileInformation_async(final AMD_IReceptorLightFileManager_setFileInformation __cb,
            final long fileId, final RlFileInformation information, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightFileManager_getFileInformation callBackGet = new AMD_IReceptorLightFileManager_getFileInformation()
        {
            @Override
            public void ice_response(RlFileInformation __ret)
            {
                if(__ret == null)
                    __cb.ice_response(fileId);
                
                try
                {
		            if(!accessHelper.isObjectWritable(__ret, __current, factory, sm))
		            {
		                __cb.ice_exception(new Exception("[openFile] No write access for this file!"));
		            }
		            else
		            {
		                __current.operation = "setFileInformation";
		                callInvokerOnRawArgs(__cb, __current, fileId, information);
		            }
                }
                catch(Exception ex)
                {
                    __cb.ice_exception(ex);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        __current.operation = "getFileInformation";
        callInvokerOnRawArgs(callBackGet, __current, fileId);
    }
    
    public void getDataDir_async(AMD_IReceptorLightFileManager_getDataDir __cb,
	    Current __current)
	    throws ServerError {
	callInvokerOnRawArgs(__cb, __current);
    }
} 
