package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlExperiment;

public class ExperimentManager
{	
    private static final String TABLE_NAME = "experimenttable";
    
    private PostgresHelper postgresHelper;
    
    public ExperimentManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlExperiment.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
	public List<RlExperiment> getExperiments() throws Exception
    {
    	return (List<RlExperiment>)postgresHelper.getAllObjects(TABLE_NAME, RlExperiment.class);
    }

    public RlExperiment getExperiment(long id) throws Exception
    {       	
    	return (RlExperiment)postgresHelper.getObjectById(TABLE_NAME, id, RlExperiment.class);
    }

    public RlExperiment createExperiment(String name) throws Exception
    {
        if(name == null)
            throw new Exception("[createExperiment] name is NULL");
        
        RlExperiment experiement = (RlExperiment) MemberInfoHelper.createInstanceFromClass(RlExperiment.class);
        experiement.setName(name);
        experiement.setUId(postgresHelper.createNewId(TABLE_NAME));
        experiement.setStatus(1);
        experiement.setOriginalObjectId(0);
        
        postgresHelper.insertObject(TABLE_NAME, experiement);
		return experiement;
    }
    
    public RlExperiment saveExperiment(RlExperiment experiement) throws Exception
    {
    	if(experiement == null)
            throw new Exception("[saveExperiment] experiement is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, experiement.getUId(), experiement);
        return experiement;
    }
    
    public void deleteExperiment(long id) throws Exception
    {       	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }
}

