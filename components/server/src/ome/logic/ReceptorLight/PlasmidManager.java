package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlPlasmid;

public class PlasmidManager
{	
    private static final String TABLE_NAME = "plasmidtable";
    
    private PostgresHelper postgresHelper;
    
    public PlasmidManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlPlasmid.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
	public List<RlPlasmid> getPlasmids() throws Exception
    {
        return (List<RlPlasmid>)postgresHelper.getAllObjects(TABLE_NAME, RlPlasmid.class);
    }

    public RlPlasmid getPlasmid(long id) throws Exception
    {       	
        return (RlPlasmid)postgresHelper.getObjectById(TABLE_NAME, id, RlPlasmid.class);
    }

    public RlPlasmid createPlasmid(String name) throws Exception
    {
        if(name == null)
            throw new Exception("[createPlasmid] name is NULL");
        
        RlPlasmid plasmid = (RlPlasmid) MemberInfoHelper.createInstanceFromClass(RlPlasmid.class);
		plasmid.setName(name);
	    plasmid.setUId(postgresHelper.createNewId(TABLE_NAME));
        plasmid.setStatus(1);
        plasmid.setOriginalObjectId(0);

	    postgresHelper.insertObject(TABLE_NAME, plasmid);
	    
		return plasmid;
    }
    
    public RlPlasmid savePlasmid(RlPlasmid plasmid) throws Exception
    {
    	if(plasmid == null)
            throw new Exception("[savePlasmid] plasmid is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, plasmid.getUId(), plasmid);
        return plasmid;
    }
    
    public void deletePlasmid(long id) throws Exception
    {    	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }
}

