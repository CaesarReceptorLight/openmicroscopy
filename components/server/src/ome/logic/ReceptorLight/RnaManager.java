package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlRna;

public class RnaManager
{	
    private static final String TABLE_NAME = "rnatable";
    
    private PostgresHelper postgresHelper;
    
    public RnaManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlRna.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
	public List<RlRna> getRnas() throws Exception
    {
    	return (List<RlRna>)postgresHelper.getAllObjects(TABLE_NAME, RlRna.class);
    }

    public RlRna getRna(long id) throws Exception
    {       	
    	return (RlRna)postgresHelper.getObjectById(TABLE_NAME, id, RlRna.class);
    }

    public RlRna createRna(String name) throws Exception
    {
        if(name == null)
            throw new Exception("[createRna] name is NULL");
        
        RlRna rna = (RlRna) MemberInfoHelper.createInstanceFromClass(RlRna.class);
        rna.setName(name);
        rna.setUId(postgresHelper.createNewId(TABLE_NAME));
        rna.setStatus(1);
        rna.setOriginalObjectId(0);
        
        postgresHelper.insertObject(TABLE_NAME, rna);
		return rna;
    }
    
    public RlRna saveRna(RlRna rna) throws Exception
    {
    	if(rna == null)
            throw new Exception("[saveRna] rna is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, rna.getUId(), rna);
        return rna;
    }
    
    public void deleteRna(long id) throws Exception
    {       	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }
}

