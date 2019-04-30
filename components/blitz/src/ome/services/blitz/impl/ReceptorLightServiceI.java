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
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import Ice.Current;

import ome.api.IReceptorLightService;
import ome.security.SecuritySystem;
import ome.services.blitz.util.ServiceFactoryAware;
import ome.services.blitz.util.BlitzExecutor;
import ome.services.sessions.SessionManager;

import omero.model.*;
import omero.model.enums.*;
import omero.ServerError;

import omero.api._IReceptorLightServiceOperations;

import omero.api.AMD_IReceptorLightService_getPlasmids;
import omero.api.AMD_IReceptorLightService_getPlasmid;
import omero.api.AMD_IReceptorLightService_createPlasmid;
import omero.api.AMD_IReceptorLightService_savePlasmid;
import omero.api.AMD_IReceptorLightService_deletePlasmid;

import omero.api.AMD_IReceptorLightService_getVectors;
import omero.api.AMD_IReceptorLightService_getVector;
import omero.api.AMD_IReceptorLightService_createVector;
import omero.api.AMD_IReceptorLightService_saveVector;
import omero.api.AMD_IReceptorLightService_deleteVector;

import omero.api.AMD_IReceptorLightService_getExperiments;
import omero.api.AMD_IReceptorLightService_getExperiment;
import omero.api.AMD_IReceptorLightService_getExperimentByDatasetId;
import omero.api.AMD_IReceptorLightService_createExperiment;
import omero.api.AMD_IReceptorLightService_saveExperiment;
import omero.api.AMD_IReceptorLightService_deleteExperiment;

import omero.api.AMD_IReceptorLightService_getChemicalSubstances;
import omero.api.AMD_IReceptorLightService_getChemicalSubstance;
import omero.api.AMD_IReceptorLightService_createChemicalSubstance;
import omero.api.AMD_IReceptorLightService_saveChemicalSubstance;
import omero.api.AMD_IReceptorLightService_deleteChemicalSubstance;

import omero.api.AMD_IReceptorLightService_getProteins;
import omero.api.AMD_IReceptorLightService_getProtein;
import omero.api.AMD_IReceptorLightService_createProtein;
import omero.api.AMD_IReceptorLightService_saveProtein;
import omero.api.AMD_IReceptorLightService_deleteProtein;

import omero.api.AMD_IReceptorLightService_getDnas;
import omero.api.AMD_IReceptorLightService_getDna;
import omero.api.AMD_IReceptorLightService_createDna;
import omero.api.AMD_IReceptorLightService_saveDna;
import omero.api.AMD_IReceptorLightService_deleteDna;

import omero.api.AMD_IReceptorLightService_getRnas;
import omero.api.AMD_IReceptorLightService_getRna;
import omero.api.AMD_IReceptorLightService_createRna;
import omero.api.AMD_IReceptorLightService_saveRna;
import omero.api.AMD_IReceptorLightService_deleteRna;

import omero.api.AMD_IReceptorLightService_getSolutions;
import omero.api.AMD_IReceptorLightService_getSolution;
import omero.api.AMD_IReceptorLightService_createSolution;
import omero.api.AMD_IReceptorLightService_saveSolution;
import omero.api.AMD_IReceptorLightService_deleteSolution;

import omero.api.AMD_IReceptorLightService_getRestrictionEnzymes;
import omero.api.AMD_IReceptorLightService_getRestrictionEnzyme;
import omero.api.AMD_IReceptorLightService_createRestrictionEnzyme;
import omero.api.AMD_IReceptorLightService_saveRestrictionEnzyme;
import omero.api.AMD_IReceptorLightService_deleteRestrictionEnzyme;

import omero.api.AMD_IReceptorLightService_getFluorescentProteins;
import omero.api.AMD_IReceptorLightService_getFluorescentProtein;
import omero.api.AMD_IReceptorLightService_createFluorescentProtein;
import omero.api.AMD_IReceptorLightService_saveFluorescentProtein;
import omero.api.AMD_IReceptorLightService_deleteFluorescentProtein;

import omero.api.AMD_IReceptorLightService_getOligonucleotides;
import omero.api.AMD_IReceptorLightService_getOligonucleotide;
import omero.api.AMD_IReceptorLightService_createOligonucleotide;
import omero.api.AMD_IReceptorLightService_saveOligonucleotide;
import omero.api.AMD_IReceptorLightService_deleteOligonucleotide;

import omero.api.AMD_IReceptorLightService_getUsedMaterials;
import omero.api.AMD_IReceptorLightService_getUsedMaterial;
import omero.api.AMD_IReceptorLightService_createUsedMaterial;
import omero.api.AMD_IReceptorLightService_saveUsedMaterial;
import omero.api.AMD_IReceptorLightService_deleteUsedMaterial;
import omero.api.AMD_IReceptorLightService_getUsedMaterialsBySourceId;
import omero.api.AMD_IReceptorLightService_getUsedMaterialsByTargetId;
import omero.api.AMD_IReceptorLightService_getUsedMaterialsBySourceType;
import omero.api.AMD_IReceptorLightService_getUsedMaterialsByTargetType;

import omero.api.AMD_IReceptorLightService_getSops;
import omero.api.AMD_IReceptorLightService_getSop;
import omero.api.AMD_IReceptorLightService_createSop;
import omero.api.AMD_IReceptorLightService_saveSop;
import omero.api.AMD_IReceptorLightService_deleteSop;

import omero.api.AMD_IReceptorLightService_uihelpGetSectionsOfType;
import omero.api.AMD_IReceptorLightService_uihelpGetElementsOfSections;
import omero.api.AMD_IReceptorLightService_uihelpGetElementInfo;

import omero.api.AMD_IReceptorLightService_getMemberNames;

import omero.api.AMD_IReceptorLightService_setMemberIObjectValue;
import omero.api.AMD_IReceptorLightService_setMemberStringValue;
import omero.api.AMD_IReceptorLightService_setMemberIntValue;

import omero.api.AMD_IReceptorLightService_getMemberIObjectValue;
import omero.api.AMD_IReceptorLightService_getMemberStringValue;
import omero.api.AMD_IReceptorLightService_getMemberIntValue;

public class ReceptorLightServiceI extends AbstractAmdServant implements _IReceptorLightServiceOperations, ServiceFactoryAware
{
    private ReceptorLightAccessHelper accessHelper;
    protected ServiceFactoryI factory;
    protected SessionManager sm;
    protected SecuritySystem ss;

    public ReceptorLightServiceI(IReceptorLightService service, BlitzExecutor be)
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

    public void getPlasmids_async(final AMD_IReceptorLightService_getPlasmids __cb,
            final Current __current)
            throws ServerError {
                
        AMD_IReceptorLightService_getPlasmids callback = new AMD_IReceptorLightService_getPlasmids()
        {
            @Override
            public void ice_response(List<RlPlasmid> __ret)
            {
                __cb.ice_response((List<RlPlasmid>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getPlasmid_async(final AMD_IReceptorLightService_getPlasmid __cb,
            long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getPlasmid callback = new AMD_IReceptorLightService_getPlasmid()
        {
            @Override
            public void ice_response(RlPlasmid __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getPlasmid] No read access for this plasmid!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }
    
    public void createPlasmid_async(final AMD_IReceptorLightService_createPlasmid __cb,
            String name, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_savePlasmid callBackSave = new AMD_IReceptorLightService_savePlasmid()
        {
            @Override
            public void ice_response(RlPlasmid __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createPlasmid callBackCreate = new AMD_IReceptorLightService_createPlasmid()
        {
            @Override
            public void ice_response(RlPlasmid __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "savePlasmid";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
    
    public void savePlasmid_async(final AMD_IReceptorLightService_savePlasmid __cb,
            RlPlasmid object , final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm) == true)
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[savePlasmid] No write access for this plasmid!"));
    }

    public void deletePlasmid_async(final AMD_IReceptorLightService_deletePlasmid __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getPlasmid callBackGet = new AMD_IReceptorLightService_getPlasmid()
        {
            @Override
            public void ice_response(RlPlasmid __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deletePlasmid";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deletePlasmid] No write access for this plasmid!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getPlasmid";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }
    
    public void getVectors_async(final AMD_IReceptorLightService_getVectors __cb,
            final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getVectors callback = new AMD_IReceptorLightService_getVectors()
        {
            @Override
            public void ice_response(List<RlVector> __ret)
            {
                __cb.ice_response((List<RlVector>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getVector_async(final AMD_IReceptorLightService_getVector __cb,
            long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getVector callback = new AMD_IReceptorLightService_getVector()
        {
            @Override
            public void ice_response(RlVector __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getVector] No read access for this vector!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }
    
    public void createVector_async(final AMD_IReceptorLightService_createVector __cb,
            String name, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_saveVector callBackSave = new AMD_IReceptorLightService_saveVector()
        {
            @Override
            public void ice_response(RlVector __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createVector callBackCreate = new AMD_IReceptorLightService_createVector()
        {
            @Override
            public void ice_response(RlVector __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "saveVector";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
    
    public void saveVector_async(final AMD_IReceptorLightService_saveVector __cb,
            RlVector object, final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm))
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[saveVector] No write access for this vector!"));
    }

    public void deleteVector_async(final AMD_IReceptorLightService_deleteVector __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getVector callBackGet = new AMD_IReceptorLightService_getVector()
        {
            @Override
            public void ice_response(RlVector __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteVector";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteVector] No write access for this vector!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getVector";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }

    public void getExperiments_async(final AMD_IReceptorLightService_getExperiments __cb,
                                     final Current __current)
            throws ServerError {

        AMD_IReceptorLightService_getExperiments callback = new AMD_IReceptorLightService_getExperiments()
        {
            @Override
            public void ice_response(List<RlExperiment> __ret)
            {
                __cb.ice_response((List<RlExperiment>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getExperiment_async(final AMD_IReceptorLightService_getExperiment __cb,
            long id, final Current __current)
            throws ServerError {
        AMD_IReceptorLightService_getExperiment callback = new AMD_IReceptorLightService_getExperiment()
        {
            @Override
            public void ice_response(RlExperiment __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getExperiment] No read access for this experiment!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }

	public void getExperimentByDatasetId_async(final AMD_IReceptorLightService_getExperimentByDatasetId __cb,
            long datasetId, final Current __current)
            throws ServerError {
        AMD_IReceptorLightService_getExperimentByDatasetId callback = new AMD_IReceptorLightService_getExperimentByDatasetId()
        {
            @Override
            public void ice_response(RlExperiment __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getExperimentByDatasetId] No read access for this experiment!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, datasetId);
    }
    
    public void createExperiment_async(final AMD_IReceptorLightService_createExperiment __cb,
                                       String name, final Current __current)
            throws ServerError {

        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_saveExperiment callBackSave = new AMD_IReceptorLightService_saveExperiment()
        {
            @Override
            public void ice_response(RlExperiment __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createExperiment callBackCreate = new AMD_IReceptorLightService_createExperiment()
        {
            @Override
            public void ice_response(RlExperiment __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "saveExperiment";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
    
    public void saveExperiment_async(final AMD_IReceptorLightService_saveExperiment __cb,
            RlExperiment object, final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm))
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[saveExperiment] No write access for this experiment!"));
    }

    public void deleteExperiment_async(final AMD_IReceptorLightService_deleteExperiment __cb,
            final long id, final Current __current)
            throws ServerError {

        AMD_IReceptorLightService_getExperiment callBackGet = new AMD_IReceptorLightService_getExperiment()
        {
            @Override
            public void ice_response(RlExperiment __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteExperiment";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteExperiment] No write access for this experiment!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getExperiment";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }
    
    public void getChemicalSubstances_async(final AMD_IReceptorLightService_getChemicalSubstances __cb,
            final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getChemicalSubstances callback = new AMD_IReceptorLightService_getChemicalSubstances()
        {
            @Override
            public void ice_response(List<RlChemicalSubstance> __ret)
            {
                __cb.ice_response((List<RlChemicalSubstance>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getChemicalSubstance_async(final AMD_IReceptorLightService_getChemicalSubstance __cb,
            long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getChemicalSubstance callback = new AMD_IReceptorLightService_getChemicalSubstance()
        {
            @Override
            public void ice_response(RlChemicalSubstance __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getChemicalSubstance] No read access for this substance!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }
    
    public void createChemicalSubstance_async(final AMD_IReceptorLightService_createChemicalSubstance __cb,
            String name, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_saveChemicalSubstance callBackSave = new AMD_IReceptorLightService_saveChemicalSubstance()
        {
            @Override
            public void ice_response(RlChemicalSubstance __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createChemicalSubstance callBackCreate = new AMD_IReceptorLightService_createChemicalSubstance()
        {
            @Override
            public void ice_response(RlChemicalSubstance __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "saveChemicalSubstance";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
    
    public void saveChemicalSubstance_async(final AMD_IReceptorLightService_saveChemicalSubstance __cb,
            RlChemicalSubstance object, final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm))
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[saveChemicalSubstance] No write access for this substance!"));
    }

    public void deleteChemicalSubstance_async(final AMD_IReceptorLightService_deleteChemicalSubstance __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getChemicalSubstance callBackGet = new AMD_IReceptorLightService_getChemicalSubstance()
        {
            @Override
            public void ice_response(RlChemicalSubstance __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteChemicalSubstance";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteChemicalSubstance] No write access for this substance!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getChemicalSubstance";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }
    
    public void getProteins_async(final AMD_IReceptorLightService_getProteins __cb,
            final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getProteins callback = new AMD_IReceptorLightService_getProteins()
        {
            @Override
            public void ice_response(List<RlProtein> __ret)
            {
                __cb.ice_response((List<RlProtein>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getProtein_async(final AMD_IReceptorLightService_getProtein __cb,
            long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getProtein callback = new AMD_IReceptorLightService_getProtein()
        {
            @Override
            public void ice_response(RlProtein __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getProtein] No read access for this protein!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }
    
    public void createProtein_async(final AMD_IReceptorLightService_createProtein __cb,
            String name, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_saveProtein callBackSave = new AMD_IReceptorLightService_saveProtein()
        {
            @Override
            public void ice_response(RlProtein __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createProtein callBackCreate = new AMD_IReceptorLightService_createProtein()
        {
            @Override
            public void ice_response(RlProtein __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "saveProtein";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
    
    public void saveProtein_async(final AMD_IReceptorLightService_saveProtein __cb,
            RlProtein object, final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm))
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[saveProtein] No write access for this protein!"));
    }

    public void deleteProtein_async(final AMD_IReceptorLightService_deleteProtein __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getProtein callBackGet = new AMD_IReceptorLightService_getProtein()
        {
            @Override
            public void ice_response(RlProtein __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteProtein";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteProtein] No write access for this protein!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getProtein";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }
    
    public void getDnas_async(final AMD_IReceptorLightService_getDnas __cb,
            final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getDnas callback = new AMD_IReceptorLightService_getDnas()
        {
            @Override
            public void ice_response(List<RlDna> __ret)
            {
                __cb.ice_response((List<RlDna>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getDna_async(final AMD_IReceptorLightService_getDna __cb,
            long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getDna callback = new AMD_IReceptorLightService_getDna()
        {
            @Override
            public void ice_response(RlDna __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getDna] No read access for this dna!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }
    
    public void createDna_async(final AMD_IReceptorLightService_createDna __cb,
            String name, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_saveDna callBackSave = new AMD_IReceptorLightService_saveDna()
        {
            @Override
            public void ice_response(RlDna __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createDna callBackCreate = new AMD_IReceptorLightService_createDna()
        {
            @Override
            public void ice_response(RlDna __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "saveDna";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
    
    public void saveDna_async(final AMD_IReceptorLightService_saveDna __cb,
            RlDna object, final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm))
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[saveDna] No write access for this dna!"));
    }

    public void deleteDna_async(final AMD_IReceptorLightService_deleteDna __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getDna callBackGet = new AMD_IReceptorLightService_getDna()
        {
            @Override
            public void ice_response(RlDna __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteDna";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteDna] No write access for this dna!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getDna";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }
    
    public void getRnas_async(final AMD_IReceptorLightService_getRnas __cb,
            final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getRnas callback = new AMD_IReceptorLightService_getRnas()
        {
            @Override
            public void ice_response(List<RlRna> __ret)
            {
                __cb.ice_response((List<RlRna>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getRna_async(final AMD_IReceptorLightService_getRna __cb,
            long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getRna callback = new AMD_IReceptorLightService_getRna()
        {
            @Override
            public void ice_response(RlRna __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getRna] No read access for this Rna!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }
    
    public void createRna_async(final AMD_IReceptorLightService_createRna __cb,
            String name, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_saveRna callBackSave = new AMD_IReceptorLightService_saveRna()
        {
            @Override
            public void ice_response(RlRna __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createRna callBackCreate = new AMD_IReceptorLightService_createRna()
        {
            @Override
            public void ice_response(RlRna __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "saveRna";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
    
    public void saveRna_async(final AMD_IReceptorLightService_saveRna __cb,
            RlRna object, final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm))
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[saveRna] No write access for this Rna!"));
    }

    public void deleteRna_async(final AMD_IReceptorLightService_deleteRna __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getRna callBackGet = new AMD_IReceptorLightService_getRna()
        {
            @Override
            public void ice_response(RlRna __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteRna";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteRna] No write access for this Rna!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getRna";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }

    public void getSolutions_async(final AMD_IReceptorLightService_getSolutions __cb,
            final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getSolutions callback = new AMD_IReceptorLightService_getSolutions()
        {
            @Override
            public void ice_response(List<RlSolution> __ret)
            {
                __cb.ice_response((List<RlSolution>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getSolution_async(final AMD_IReceptorLightService_getSolution __cb,
            long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getSolution callback = new AMD_IReceptorLightService_getSolution()
        {
            @Override
            public void ice_response(RlSolution __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getSolution] No read access for this Solution!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }
    
    public void createSolution_async(final AMD_IReceptorLightService_createSolution __cb,
            String name, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_saveSolution callBackSave = new AMD_IReceptorLightService_saveSolution()
        {
            @Override
            public void ice_response(RlSolution __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createSolution callBackCreate = new AMD_IReceptorLightService_createSolution()
        {
            @Override
            public void ice_response(RlSolution __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "saveSolution";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
    
    public void saveSolution_async(final AMD_IReceptorLightService_saveSolution __cb,
            RlSolution object, final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm))
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[saveSolution] No write access for this Solution!"));
    }

    public void deleteSolution_async(final AMD_IReceptorLightService_deleteSolution __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getSolution callBackGet = new AMD_IReceptorLightService_getSolution()
        {
            @Override
            public void ice_response(RlSolution __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteSolution";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteSolution] No write access for this Solution!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getSolution";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }

    public void getRestrictionEnzymes_async(final AMD_IReceptorLightService_getRestrictionEnzymes __cb,
            final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getRestrictionEnzymes callback = new AMD_IReceptorLightService_getRestrictionEnzymes()
        {
            @Override
            public void ice_response(List<RlRestrictionEnzyme> __ret)
            {
                __cb.ice_response((List<RlRestrictionEnzyme>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getRestrictionEnzyme_async(final AMD_IReceptorLightService_getRestrictionEnzyme __cb,
            long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getRestrictionEnzyme callback = new AMD_IReceptorLightService_getRestrictionEnzyme()
        {
            @Override
            public void ice_response(RlRestrictionEnzyme __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getRestrictionEnzyme] No read access for this RestrictionEnzyme!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }
    
    public void createRestrictionEnzyme_async(final AMD_IReceptorLightService_createRestrictionEnzyme __cb,
            String name, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_saveRestrictionEnzyme callBackSave = new AMD_IReceptorLightService_saveRestrictionEnzyme()
        {
            @Override
            public void ice_response(RlRestrictionEnzyme __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createRestrictionEnzyme callBackCreate = new AMD_IReceptorLightService_createRestrictionEnzyme()
        {
            @Override
            public void ice_response(RlRestrictionEnzyme __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "saveRestrictionEnzyme";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
    
    public void saveRestrictionEnzyme_async(final AMD_IReceptorLightService_saveRestrictionEnzyme __cb,
            RlRestrictionEnzyme object, final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm))
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[saveRestrictionEnzyme] No write access for this RestrictionEnzyme!"));
    }

    public void deleteRestrictionEnzyme_async(final AMD_IReceptorLightService_deleteRestrictionEnzyme __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getRestrictionEnzyme callBackGet = new AMD_IReceptorLightService_getRestrictionEnzyme()
        {
            @Override
            public void ice_response(RlRestrictionEnzyme __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteRestrictionEnzyme";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteRestrictionEnzyme] No write access for this RestrictionEnzyme!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getRestrictionEnzyme";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }

    public void getOligonucleotides_async(final AMD_IReceptorLightService_getOligonucleotides __cb,
            final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getOligonucleotides callback = new AMD_IReceptorLightService_getOligonucleotides()
        {
            @Override
            public void ice_response(List<RlOligonucleotide> __ret)
            {
                __cb.ice_response((List<RlOligonucleotide>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getOligonucleotide_async(final AMD_IReceptorLightService_getOligonucleotide __cb,
            long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getOligonucleotide callback = new AMD_IReceptorLightService_getOligonucleotide()
        {
            @Override
            public void ice_response(RlOligonucleotide __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getOligonucleotide] No read access for this Oligonucleotide!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }
    
    public void createOligonucleotide_async(final AMD_IReceptorLightService_createOligonucleotide __cb,
            String name, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_saveOligonucleotide callBackSave = new AMD_IReceptorLightService_saveOligonucleotide()
        {
            @Override
            public void ice_response(RlOligonucleotide __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createOligonucleotide callBackCreate = new AMD_IReceptorLightService_createOligonucleotide()
        {
            @Override
            public void ice_response(RlOligonucleotide __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "saveOligonucleotide";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
    
    public void saveOligonucleotide_async(final AMD_IReceptorLightService_saveOligonucleotide __cb,
            RlOligonucleotide object, final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm))
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[saveOligonucleotide] No write access for this Oligonucleotide!"));
    }

    public void deleteOligonucleotide_async(final AMD_IReceptorLightService_deleteOligonucleotide __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getOligonucleotide callBackGet = new AMD_IReceptorLightService_getOligonucleotide()
        {
            @Override
            public void ice_response(RlOligonucleotide __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteOligonucleotide";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteOligonucleotide] No write access for this Oligonucleotide!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getOligonucleotide";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }

    public void getFluorescentProteins_async(final AMD_IReceptorLightService_getFluorescentProteins __cb,
            final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getFluorescentProteins callback = new AMD_IReceptorLightService_getFluorescentProteins()
        {
            @Override
            public void ice_response(List<RlFluorescentProtein> __ret)
            {
                __cb.ice_response((List<RlFluorescentProtein>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getFluorescentProtein_async(final AMD_IReceptorLightService_getFluorescentProtein __cb,
            long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getFluorescentProtein callback = new AMD_IReceptorLightService_getFluorescentProtein()
        {
            @Override
            public void ice_response(RlFluorescentProtein __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getFluorescentProtein] No read access for this FluorescentProtein!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }
    
    public void createFluorescentProtein_async(final AMD_IReceptorLightService_createFluorescentProtein __cb,
            String name, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_saveFluorescentProtein callBackSave = new AMD_IReceptorLightService_saveFluorescentProtein()
        {
            @Override
            public void ice_response(RlFluorescentProtein __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createFluorescentProtein callBackCreate = new AMD_IReceptorLightService_createFluorescentProtein()
        {
            @Override
            public void ice_response(RlFluorescentProtein __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "saveFluorescentProtein";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
    
    public void saveFluorescentProtein_async(final AMD_IReceptorLightService_saveFluorescentProtein __cb,
            RlFluorescentProtein object, final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm))
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[saveFluorescentProtein] No write access for this FluorescentProtein!"));
    }

    public void deleteFluorescentProtein_async(final AMD_IReceptorLightService_deleteFluorescentProtein __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getFluorescentProtein callBackGet = new AMD_IReceptorLightService_getFluorescentProtein()
        {
            @Override
            public void ice_response(RlFluorescentProtein __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteFluorescentProtein";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteFluorescentProtein] No write access for this FluorescentProtein!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getFluorescentProtein";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }

    public void getUsedMaterials_async(final AMD_IReceptorLightService_getUsedMaterials __cb,
            final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getUsedMaterials callback = new AMD_IReceptorLightService_getUsedMaterials()
        {
            @Override
            public void ice_response(List<RlUsedMaterial> __ret)
            {
                __cb.ice_response((List<RlUsedMaterial>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getUsedMaterial_async(final AMD_IReceptorLightService_getUsedMaterial __cb,
            long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getUsedMaterial callback = new AMD_IReceptorLightService_getUsedMaterial()
        {
            @Override
            public void ice_response(RlUsedMaterial __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getUsedMaterial] No read access for this UsedMaterial!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }
    
    public void createUsedMaterial_async(final AMD_IReceptorLightService_createUsedMaterial __cb,
            final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_saveUsedMaterial callBackSave = new AMD_IReceptorLightService_saveUsedMaterial()
        {
            @Override
            public void ice_response(RlUsedMaterial __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createUsedMaterial callBackCreate = new AMD_IReceptorLightService_createUsedMaterial()
        {
            @Override
            public void ice_response(RlUsedMaterial __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "saveUsedMaterial";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current);
    }
    
    public void saveUsedMaterial_async(final AMD_IReceptorLightService_saveUsedMaterial __cb,
            RlUsedMaterial object, final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm))
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[saveUsedMaterial] No write access for this UsedMaterial!"));
    }

    public void deleteUsedMaterial_async(final AMD_IReceptorLightService_deleteUsedMaterial __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getUsedMaterial callBackGet = new AMD_IReceptorLightService_getUsedMaterial()
        {
            @Override
            public void ice_response(RlUsedMaterial __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteUsedMaterial";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteUsedMaterial] No write access for this UsedMaterial!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getUsedMaterial";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }

    public void getUsedMaterialsBySourceId_async(final AMD_IReceptorLightService_getUsedMaterialsBySourceId __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getUsedMaterialsBySourceId callback = new AMD_IReceptorLightService_getUsedMaterialsBySourceId()
        {
            @Override
            public void ice_response(List<RlUsedMaterial> __ret)
            {
                __cb.ice_response((List<RlUsedMaterial>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }

    public void getUsedMaterialsByTargetId_async(final AMD_IReceptorLightService_getUsedMaterialsByTargetId __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getUsedMaterialsByTargetId callback = new AMD_IReceptorLightService_getUsedMaterialsByTargetId()
        {
            @Override
            public void ice_response(List<RlUsedMaterial> __ret)
            {
                __cb.ice_response((List<RlUsedMaterial>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }

    public void getUsedMaterialsByTargetType_async(final AMD_IReceptorLightService_getUsedMaterialsByTargetType __cb,
            final RlUsedMaterialType targetType, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getUsedMaterialsByTargetType callback = new AMD_IReceptorLightService_getUsedMaterialsByTargetType()
        {
            @Override
            public void ice_response(List<RlUsedMaterial> __ret)
            {
                __cb.ice_response((List<RlUsedMaterial>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, targetType);
    }

    public void getUsedMaterialsBySourceType_async(final AMD_IReceptorLightService_getUsedMaterialsBySourceType __cb,
            final RlUsedMaterialType sourceType, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getUsedMaterialsBySourceType callback = new AMD_IReceptorLightService_getUsedMaterialsBySourceType()
        {
            @Override
            public void ice_response(List<RlUsedMaterial> __ret)
            {
                __cb.ice_response((List<RlUsedMaterial>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, sourceType);
    }

    public void getSops_async(final AMD_IReceptorLightService_getSops __cb,
            final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getSops callback = new AMD_IReceptorLightService_getSops()
        {
            @Override
            public void ice_response(List<RlSop> __ret)
            {
                __cb.ice_response((List<RlSop>)accessHelper.getReadableMembers(__current, __ret, factory, sm));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current);
    }
    
    public void getSop_async(final AMD_IReceptorLightService_getSop __cb,
            long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getSop callback = new AMD_IReceptorLightService_getSop()
        {
            @Override
            public void ice_response(RlSop __ret)
            {
                if(__ret == null)
                    __cb.ice_response(__ret);

                if(accessHelper.isObjectReadable(__ret, __current, factory, sm))
                    __cb.ice_response(__ret);
                else
                    __cb.ice_exception(new Exception("[getSop] No read access for this SOP!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callback, __current, id);
    }
    
    public void createSop_async(final AMD_IReceptorLightService_createSop __cb,
            String name, final Current __current)
            throws ServerError {
        
        final Long myId = accessHelper.getOwnUserId(factory, sm);
        final Long myGroupId = accessHelper.getOwnGroupId(factory, sm);

        final AMD_IReceptorLightService_saveSop callBackSave = new AMD_IReceptorLightService_saveSop()
        {
            @Override
            public void ice_response(RlSop __ret)
            {
                __cb.ice_response(__ret);
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        AMD_IReceptorLightService_createSop callBackCreate = new AMD_IReceptorLightService_createSop()
        {
            @Override
            public void ice_response(RlSop __ret)
            {
                if(__ret != null)
                {
                    __current.operation = "saveSop";
                    accessHelper.initObject(__ret, myId, myGroupId);
                    callInvokerOnRawArgs(callBackSave, __current, __ret);
                }
            }

            @Override
            public void ice_exception(Exception ex)
            {
                __cb.ice_exception(ex);
            }
        };

        callInvokerOnRawArgs(callBackCreate, __current, name);
    }
    
    public void saveSop_async(final AMD_IReceptorLightService_saveSop __cb,
            RlSop object, final Current __current)
            throws ServerError {

        if(accessHelper.isObjectWritable(object, __current, factory, sm))
            callInvokerOnRawArgs(__cb, __current, object);
        else
            __cb.ice_exception(new Exception("[saveSop] No write access for this SOP!"));
    }

    public void deleteSop_async(final AMD_IReceptorLightService_deleteSop __cb,
            final long id, final Current __current)
            throws ServerError {
        
        AMD_IReceptorLightService_getSop callBackGet = new AMD_IReceptorLightService_getSop()
        {
            @Override
            public void ice_response(RlSop __ret)
            {
                if(__ret == null)
                    __cb.ice_response();

                if(accessHelper.isObjectDeletable(__ret, __current, factory, sm))
                {
                    __current.operation = "deleteSop";
                    callInvokerOnRawArgs(__cb, __current, id);
                }
                else
                    __cb.ice_exception(new Exception("[deleteSop] No write access for this SOP!"));
            }

            @Override
            public void ice_exception(Exception ex)
            {

            }
        };

        __current.operation = "getSop";
        callInvokerOnRawArgs(callBackGet, __current, id);
    }

    public void uihelpGetSectionsOfType_async(AMD_IReceptorLightService_uihelpGetSectionsOfType __cb,
            String typeName, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, typeName);
    }

    public void uihelpGetElementsOfSections_async(AMD_IReceptorLightService_uihelpGetElementsOfSections __cb,
            String typeName, String sectionName, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, typeName, sectionName);
    }

    public void uihelpGetElementInfo_async(AMD_IReceptorLightService_uihelpGetElementInfo __cb,
            String typeName, String elementName, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, typeName, elementName);
    }
    
    public void getMemberNames_async(AMD_IReceptorLightService_getMemberNames __cb,
            IObject obj, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, obj);
    }
    
    public void setMemberIObjectValue_async(AMD_IReceptorLightService_setMemberIObjectValue __cb,
            IObject obj, String memberName, IObject value, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, obj, memberName, value);
    }
    
    public void setMemberStringValue_async(AMD_IReceptorLightService_setMemberStringValue __cb,
            IObject obj, String memberName, String value, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, obj, memberName, value);
    }
    
    public void setMemberIntValue_async(AMD_IReceptorLightService_setMemberIntValue __cb,
            IObject obj, String memberName, int value, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, obj, memberName, value);
    }
    
    public void getMemberIObjectValue_async(AMD_IReceptorLightService_getMemberIObjectValue __cb,
            IObject obj, String memberName, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, obj, memberName);
    }
    
    public void getMemberStringValue_async(AMD_IReceptorLightService_getMemberStringValue __cb,
            IObject obj, String memberName, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, obj, memberName);
    }
    
    public void getMemberIntValue_async(AMD_IReceptorLightService_getMemberIntValue __cb,
            IObject obj, String memberName, Current __current)
            throws ServerError {
        callInvokerOnRawArgs(__cb, __current, obj, memberName);
    }
}

