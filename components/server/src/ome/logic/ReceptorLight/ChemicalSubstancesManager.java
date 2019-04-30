package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlChemicalSubstance;

public class ChemicalSubstancesManager
{	
    private static final String TABLE_NAME = "chemicalstable";
    
    private PostgresHelper postgresHelper;
    
    public ChemicalSubstancesManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlChemicalSubstance.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
	public List<RlChemicalSubstance> getChemicalSubstances() throws Exception
    {
    	return (List<RlChemicalSubstance>)postgresHelper.getAllObjects(TABLE_NAME, RlChemicalSubstance.class);
    }

    public RlChemicalSubstance getChemicalSubstance(long id) throws Exception
    {       	
    	return (RlChemicalSubstance)postgresHelper.getObjectById(TABLE_NAME, id, RlChemicalSubstance.class);
    }

    public RlChemicalSubstance createChemicalSubstance(String name) throws Exception
    {
        if(name == null)
            throw new Exception("[createChemicalSubstance] name is NULL");
        
        RlChemicalSubstance substance = (RlChemicalSubstance) MemberInfoHelper.createInstanceFromClass(RlChemicalSubstance.class);
        substance.setName(name);
        substance.setUId(postgresHelper.createNewId(TABLE_NAME));
        substance.setStatus(1);
        substance.setOriginalObjectId(0);
        
        postgresHelper.insertObject(TABLE_NAME, substance);
		return substance;
    }
    
    public RlChemicalSubstance saveChemicalSubstance(RlChemicalSubstance substance) throws Exception
    {
    	if(substance == null)
            throw new Exception("[saveChemicalSubstance] substance is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, substance.getUId(), substance);
        return substance;
    }
    
    public void deleteChemicalSubstance(long id) throws Exception
    {       	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }
}

