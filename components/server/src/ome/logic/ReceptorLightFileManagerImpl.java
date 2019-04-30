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

import java.util.List;
import ome.annotations.RolesAllowed;
import ome.api.ServiceInterface;
import ome.api.IReceptorLightFileManager;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import ome.model.IObject;
import ome.model.core.*;
import ome.logic.ReceptorLight.*;
import ome.system.PreferenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Transactional(readOnly = true)
public class ReceptorLightFileManagerImpl extends AbstractLevel1Service implements IReceptorLightFileManager
{	
    private final Logger log = LoggerFactory.getLogger(ReceptorLightFileManagerImpl.class);
    private PostgresHelper queryHelper;
    private FileManager fileManager;
    private transient PreferenceContext prefs;
    private String omeroDataDir;

    /**
     * Protects all access to the configuration properties.
     */
    private final transient ReadWriteLock lock = new ReentrantReadWriteLock();
    
    public ReceptorLightFileManagerImpl()
    {
        
    }
    
    public Class<? extends ServiceInterface> getServiceInterface()
    {
        return IReceptorLightFileManager.class;
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
            omeroDataDir = getInternalValue("omero.data.dir");

            queryHelper = new PostgresHelper(log);
        	queryHelper.openDatabase(rl_db_name, rl_db_user, rl_db_pass);
        	fileManager = new FileManager(queryHelper, omeroDataDir, log);
        }
        catch(Exception ex)
        {
        	log.error("Exception while starting ReceptorLightFileManagerImpl: " + ex.getMessage());
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
    public long createFile(String fileName) throws Exception
    {
        return fileManager.createFile(fileName);
    }

	@Override
    @RolesAllowed("user")
    public long createOmeroFile(String fileName, long omeroId) throws Exception
    {
        return fileManager.createOmeroFile(fileName, omeroId);
    }

	@Override
    @RolesAllowed("user")
    public long createDirectory(String directoryName) throws Exception
    {
        return fileManager.createDirectory(directoryName);
    }
    
    @Override
    @RolesAllowed("user")
    public void deleteFile(long fileId) throws Exception
    {
        fileManager.deleteFile(fileId);
    }

    @Override
    @RolesAllowed("user")
    public List<RlFileInformation> getFiles() throws Exception
    {
        return fileManager.getFiles();
    }
    
    @Override
    @RolesAllowed("user")
    public void openFile(long fileId, boolean forWriting) throws Exception
    {
        fileManager.openFile(fileId, forWriting);
    }   
         
    @Override
    @RolesAllowed("user")
    public void closeFile(long fileId) throws Exception
    {
        fileManager.closeFile(fileId);
    }
    
    @Override
    @RolesAllowed("user")
    public void appendFileData(long fileId, byte[] data) throws Exception
    {
        fileManager.appendFileData(fileId, data);
    }
    
    @Override
    @RolesAllowed("user")
    public byte[] readFileData(long fileId, long dataLength) throws Exception
    {
        return fileManager.readFileData(fileId, dataLength);
    } 
    
    @Override
    @RolesAllowed("user")
    public long[] searchFiles(String searchPattern) throws Exception
    {
        return fileManager.searchFiles(searchPattern);
    }
    
    @Override
    @RolesAllowed("user")
    public RlFileInformation getFileInformation(long fileId) throws Exception
    {
        return fileManager.getFileInformation(fileId);
    }

    @Override
    @RolesAllowed("user")
    public long setFileInformation(long fileId, RlFileInformation information) throws Exception
    {
        return fileManager.setFileInformation(fileId, information);
    }
    
    @Override
    @RolesAllowed("user")
    public String getDataDir( ) throws Exception
    {
        return omeroDataDir;
    }
} 
