package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlDna;

public class DnaManager
{	
    private static final String TABLE_NAME = "dnatable";
    
    private PostgresHelper postgresHelper;
    
    public DnaManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlDna.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
	public List<RlDna> getDnas() throws Exception
    {
    	return (List<RlDna>)postgresHelper.getAllObjects(TABLE_NAME, RlDna.class);
    }

    public RlDna getDna(long id) throws Exception
    {       	
    	return (RlDna)postgresHelper.getObjectById(TABLE_NAME, id, RlDna.class);
    }

    public RlDna createDna(String name) throws Exception
    {
        if(name == null)
            throw new Exception("[createDna] name is NULL");
        
        RlDna dna = (RlDna) MemberInfoHelper.createInstanceFromClass(RlDna.class);
        dna.setName(name);
        dna.setUId(postgresHelper.createNewId(TABLE_NAME));
        dna.setStatus(1);
        dna.setOriginalObjectId(0);
        
        postgresHelper.insertObject(TABLE_NAME, dna);
		return dna;
    }
    
    public RlDna saveDna(RlDna dna) throws Exception
    {
    	if(dna == null)
            throw new Exception("[saveDna] dna is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, dna.getUId(), dna);
        return dna;
    }
    
    public void deleteDna(long id) throws Exception
    {       	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }
}

