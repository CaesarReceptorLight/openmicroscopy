package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlFluorescentProtein;

public class FluorescentProteinManager
{	
    private static final String TABLE_NAME = "fluorescentproteintable";
    
    private PostgresHelper postgresHelper;
    
    public FluorescentProteinManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlFluorescentProtein.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
	public List<RlFluorescentProtein> getFluorescentProteins() throws Exception
    {
    	return (List<RlFluorescentProtein>)postgresHelper.getAllObjects(TABLE_NAME, RlFluorescentProtein.class);
    }

    public RlFluorescentProtein getFluorescentProtein(long id) throws Exception
    {       	
    	return (RlFluorescentProtein)postgresHelper.getObjectById(TABLE_NAME, id, RlFluorescentProtein.class);
    }

    public RlFluorescentProtein createFluorescentProtein(String name) throws Exception
    {
        if(name == null)
            throw new Exception("[createFluorescentProtein] name is NULL");
        
        RlFluorescentProtein fluorescentProtein = (RlFluorescentProtein) MemberInfoHelper.createInstanceFromClass(RlFluorescentProtein.class);
        fluorescentProtein.setName(name);
        fluorescentProtein.setUId(postgresHelper.createNewId(TABLE_NAME));
        fluorescentProtein.setStatus(1);
        fluorescentProtein.setOriginalObjectId(0);
        
        postgresHelper.insertObject(TABLE_NAME, fluorescentProtein);
		return fluorescentProtein;
    }
    
    public RlFluorescentProtein saveFluorescentProtein(RlFluorescentProtein fluorescentProtein) throws Exception
    {
    	if(fluorescentProtein == null)
            throw new Exception("[saveFluorescentProtein] fluorescentProtein is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, fluorescentProtein.getUId(), fluorescentProtein);
        return fluorescentProtein;
    }
    
    public void deleteFluorescentProtein(long id) throws Exception
    {       	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }
}

