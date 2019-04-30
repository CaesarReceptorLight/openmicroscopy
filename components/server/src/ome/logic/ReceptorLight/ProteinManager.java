package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlProtein;

public class ProteinManager
{	
    private static final String TABLE_NAME = "proteintable";
    
    private PostgresHelper postgresHelper;
    
    public ProteinManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlProtein.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
	public List<RlProtein> getProteins() throws Exception
    {
        return (List<RlProtein>)postgresHelper.getAllObjects(TABLE_NAME, RlProtein.class);
    }

    public RlProtein getProtein(long id) throws Exception
    {       	
        return (RlProtein)postgresHelper.getObjectById(TABLE_NAME, id, RlProtein.class);
    }

    public RlProtein createProtein(String name) throws Exception
    {
        if(name == null)
            throw new Exception("[createProtein] name is NULL");
        
        RlProtein protein = (RlProtein) MemberInfoHelper.createInstanceFromClass(RlProtein.class);
	protein.setName(name);
	protein.setUId(postgresHelper.createNewId(TABLE_NAME));
        protein.setStatus(1);
        protein.setOriginalObjectId(0);

	    postgresHelper.insertObject(TABLE_NAME, protein);
	    
		return protein;
    }
    
    public RlProtein saveProtein(RlProtein protein) throws Exception
    {
    	if(protein == null)
            throw new Exception("[saveProtein] protein is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, protein.getUId(), protein);
        return protein;
    }
    
    public void deleteProtein(long id) throws Exception
    {    	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }
}

