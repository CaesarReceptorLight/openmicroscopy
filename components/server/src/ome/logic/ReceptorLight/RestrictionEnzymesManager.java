package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlRestrictionEnzyme;

public class RestrictionEnzymesManager
{	
    private static final String TABLE_NAME = "restrictionenzymetable";
    
    private PostgresHelper postgresHelper;
    
    public RestrictionEnzymesManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlRestrictionEnzyme.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
	public List<RlRestrictionEnzyme> getRestrictionEnzymes() throws Exception
    {
    	return (List<RlRestrictionEnzyme>)postgresHelper.getAllObjects(TABLE_NAME, RlRestrictionEnzyme.class);
    }

    public RlRestrictionEnzyme getRestrictionEnzyme(long id) throws Exception
    {       	
    	return (RlRestrictionEnzyme)postgresHelper.getObjectById(TABLE_NAME, id, RlRestrictionEnzyme.class);
    }

    public RlRestrictionEnzyme createRestrictionEnzyme(String name) throws Exception
    {
        if(name == null)
            throw new Exception("[createRestrictionEnzyme] name is NULL");
        
        RlRestrictionEnzyme restrictionEnzyme = (RlRestrictionEnzyme) MemberInfoHelper.createInstanceFromClass(RlRestrictionEnzyme.class);
        restrictionEnzyme.setName(name);
        restrictionEnzyme.setUId(postgresHelper.createNewId(TABLE_NAME));
        restrictionEnzyme.setStatus(1);
        restrictionEnzyme.setOriginalObjectId(0);
        
        postgresHelper.insertObject(TABLE_NAME, restrictionEnzyme);
		return restrictionEnzyme;
    }
    
    public RlRestrictionEnzyme saveRestrictionEnzyme(RlRestrictionEnzyme restrictionEnzyme) throws Exception
    {
    	if(restrictionEnzyme == null)
            throw new Exception("[saveRestrictionEnzyme] restrictionEnzyme is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, restrictionEnzyme.getUId(), restrictionEnzyme);
        return restrictionEnzyme;
    }
    
    public void deleteRestrictionEnzyme(long id) throws Exception
    {       	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }
}

