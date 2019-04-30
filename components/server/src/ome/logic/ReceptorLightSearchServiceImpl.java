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

package ome.logic;

import ome.annotations.RolesAllowed;
import ome.api.ServiceInterface;
import ome.api.IReceptorLightSearchService;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


import ome.model.IObject;
import ome.model.core.*;
import ome.logic.ReceptorLight.*;
import ome.system.PreferenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional(readOnly = true)
public class ReceptorLightSearchServiceImpl extends AbstractLevel1Service implements IReceptorLightSearchService
{	
    private final Logger log = LoggerFactory.getLogger(ReceptorLightSearchServiceImpl.class);
    private PostgresHelper queryHelper;
    private SearchService searchService;
    private transient PreferenceContext prefs;

    /**
     * Protects all access to the configuration properties.
     */
    private final transient ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public ReceptorLightSearchServiceImpl()
    {
         
    }
    
    public Class<? extends ServiceInterface> getServiceInterface()
    {
        return IReceptorLightSearchService.class;
    }
    
    /**
     * {@link PreferenceContext} setter for dependency injection.
     * 
     * @param prefs the preference context
     * @see ome.services.util.BeanHelper#throwIfAlreadySet(Object, Object)
     */
    public final void setPreferenceContext(PreferenceContext prefs)
    {
        getBeanHelper().throwIfAlreadySet(this.prefs, prefs);
        this.prefs = prefs;
        
        try
        {
            String rl_db_name = getInternalValue("omero.db.rl.name");
            String rl_db_user = getInternalValue("omero.db.rl.user");
            String rl_db_pass = getInternalValue("omero.db.rl.pass");

            queryHelper = new PostgresHelper(log);
        	queryHelper.openDatabase(rl_db_name, rl_db_user, rl_db_pass);
        	searchService = new SearchService(queryHelper);
        }
        catch(Exception ex)
        {
        	log.error("Exception while starting ReceptorLightSearchService: " + ex.getMessage());
        }
    }

    private String getInternalValue(String key)
    {
        key = prefs.resolveAlias(key);
        lock.readLock().lock();
        try
        {
            if (prefs.checkDatabase(key))
            {
                return prefs.getProperty(key);
            }
            return null;
        }
        finally
        {
            lock.readLock().unlock();
        }
    }
    
    @Override
    @RolesAllowed("user")
    public List<RlSearchResult> search(String pattern) throws Exception
    {
        return searchService.search(pattern);
    }
    
    @Override
    @RolesAllowed("user")
    public List<RlSearchResult> searchByName(String pattern, String memberName) throws Exception
    {
        return searchService.searchByName(pattern, memberName);
    }
    
    @Override
    @RolesAllowed("user")
    public List<RlSearchResult> searchByType(String pattern, String typeName) throws Exception
    {
        return searchService.searchByType(pattern, typeName);
    }
    
    @Override
    @RolesAllowed("user")
    public List<RlSearchResult> searchByNameAndType(String pattern, String memberName, String typeName) throws Exception
    {
        return searchService.searchByNameAndType(pattern, memberName, typeName);
    }
} 
