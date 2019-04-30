package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlSop;

public class SopManager
{	
    private static final String TABLE_NAME = "soptable";
    
    private PostgresHelper postgresHelper;
    
    public SopManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlSop.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
    public List<RlSop> getSops() throws Exception
    {
    	return (List<RlSop>)postgresHelper.getAllObjects(TABLE_NAME, RlSop.class);
    }

    public RlSop getSop(long id) throws Exception
    {       	
    	return (RlSop)postgresHelper.getObjectById(TABLE_NAME, id, RlSop.class);
    }

    public RlSop createSop(String name) throws Exception
    {        
        if(name == null)
            throw new Exception("[createSop] name is NULL");

        RlSop sop = (RlSop) MemberInfoHelper.createInstanceFromClass(RlSop.class);
        sop.setUId(postgresHelper.createNewId(TABLE_NAME));
        sop.setName(name);
        sop.setStatus(1);
        sop.setOriginalObjectId(0);
        
        postgresHelper.insertObject(TABLE_NAME, sop);
	return sop;
    }
    
    public RlSop saveSop(RlSop sop) throws Exception
    {
    	if(sop == null)
            throw new Exception("[saveSop] sop is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, sop.getUId(), sop);
        return sop;
    }
    
    public void deleteSop(long id) throws Exception
    {       	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }
}

