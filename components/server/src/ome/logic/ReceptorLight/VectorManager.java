package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlVector;

public class VectorManager
{	
    private static final String TABLE_NAME = "vectortable";
    
    private PostgresHelper postgresHelper;
    
    public VectorManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlVector.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
	public List<RlVector> getVectors() throws Exception
    {
    	return (List<RlVector>)postgresHelper.getAllObjects(TABLE_NAME, RlVector.class);
    }

    public RlVector getVector(long id) throws Exception
    {       	
    	return (RlVector)postgresHelper.getObjectById(TABLE_NAME, id, RlVector.class);
    }

    public RlVector createVector(String name) throws Exception
    {
        if(name == null)
            throw new Exception("[createVector] name is NULL");
        
        RlVector vector = (RlVector) MemberInfoHelper.createInstanceFromClass(RlVector.class);
        vector.setName(name);
        vector.setUId(postgresHelper.createNewId(TABLE_NAME));
        vector.setStatus(1);
        vector.setOriginalObjectId(0);
        
        postgresHelper.insertObject(TABLE_NAME, vector);
		return vector;
    }
    
    public RlVector saveVector(RlVector vector) throws Exception
    {
    	if(vector == null)
            throw new Exception("[saveVector] vector is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, vector.getUId(), vector);
        return vector;
    }
    
    public void deleteVector(long id) throws Exception
    {       	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }
}

