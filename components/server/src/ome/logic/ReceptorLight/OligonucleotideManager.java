package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlOligonucleotide;

public class OligonucleotideManager
{	
    private static final String TABLE_NAME = "oligonucleotidetable";
    
    private PostgresHelper postgresHelper;
    
    public OligonucleotideManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlOligonucleotide.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
	public List<RlOligonucleotide> getOligonucleotides() throws Exception
    {
    	return (List<RlOligonucleotide>)postgresHelper.getAllObjects(TABLE_NAME, RlOligonucleotide.class);
    }

    public RlOligonucleotide getOligonucleotide(long id) throws Exception
    {       	
    	return (RlOligonucleotide)postgresHelper.getObjectById(TABLE_NAME, id, RlOligonucleotide.class);
    }

    public RlOligonucleotide createOligonucleotide(String name) throws Exception
    {
        if(name == null)
            throw new Exception("[createOligonucleotide] name is NULL");
        
        RlOligonucleotide oligonucleotide = (RlOligonucleotide) MemberInfoHelper.createInstanceFromClass(RlOligonucleotide.class);
        oligonucleotide.setName(name);
        oligonucleotide.setUId(postgresHelper.createNewId(TABLE_NAME));
        oligonucleotide.setStatus(1);
        oligonucleotide.setOriginalObjectId(0);
        
        postgresHelper.insertObject(TABLE_NAME, oligonucleotide);
		return oligonucleotide;
    }
    
    public RlOligonucleotide saveOligonucleotide(RlOligonucleotide oligonucleotide) throws Exception
    {
    	if(oligonucleotide == null)
            throw new Exception("[saveOligonucleotide] oligonucleotide is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, oligonucleotide.getUId(), oligonucleotide);
        return oligonucleotide;
    }
    
    public void deleteOligonucleotide(long id) throws Exception
    {       	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }
}

