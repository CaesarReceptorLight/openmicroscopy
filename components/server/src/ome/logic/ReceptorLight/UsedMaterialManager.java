package ome.logic.ReceptorLight;

import java.util.List;

import ome.model.core.RlUsedMaterial;
import ome.model.enums.RlUsedMaterialType;

public class UsedMaterialManager
{	
    private static final String TABLE_NAME = "usedmaterialtable";
    
    private PostgresHelper postgresHelper;
    
    public UsedMaterialManager(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
        	this.postgresHelper.checkTable(TABLE_NAME, RlUsedMaterial.class);
        }
        catch(Exception e)
        {
        	//todo: write exception message to error log file.
        }
    }

    @SuppressWarnings("unchecked")
	public List<RlUsedMaterial> getUsedMaterials() throws Exception
    {
    	return (List<RlUsedMaterial>)postgresHelper.getAllObjects(TABLE_NAME, RlUsedMaterial.class);
    }

    public RlUsedMaterial getUsedMaterial(long id) throws Exception
    {       	
    	return (RlUsedMaterial)postgresHelper.getObjectById(TABLE_NAME, id, RlUsedMaterial.class);
    }

    public RlUsedMaterial createUsedMaterial() throws Exception
    {        
        RlUsedMaterial usedMaterial = (RlUsedMaterial) MemberInfoHelper.createInstanceFromClass(RlUsedMaterial.class);
        usedMaterial.setUId(postgresHelper.createNewId(TABLE_NAME));
        usedMaterial.setStatus(1);
        usedMaterial.setSourceType(new RlUsedMaterialType("UNKNOWN"));
        usedMaterial.setTargetType(new RlUsedMaterialType("UNKNOWN"));
        usedMaterial.setOriginalObjectId(0);
        
        postgresHelper.insertObject(TABLE_NAME, usedMaterial);
	return usedMaterial;
    }
    
    public RlUsedMaterial saveUsedMaterial(RlUsedMaterial usedMaterial) throws Exception
    {
    	if(usedMaterial == null)
            throw new Exception("[saveUsedMaterial] usedMaterial is NULL");
    	
        postgresHelper.updateObject(TABLE_NAME, usedMaterial.getUId(), usedMaterial);
        return usedMaterial;
    }
    
    public void deleteUsedMaterial(long id) throws Exception
    {       	
    	postgresHelper.deleteById(TABLE_NAME, id);
    }

    public List<RlUsedMaterial> getUsedMaterialsBySourceId(long id) throws Exception
    {
        return (List<RlUsedMaterial>)postgresHelper.queryObjectList("SELECT * FROM " + TABLE_NAME + " WHERE SourceID = " + Long.toString(id) +";", RlUsedMaterial.class);
    }

    public List<RlUsedMaterial> getUsedMaterialsByTargetId(long id) throws Exception
    {
        return (List<RlUsedMaterial>)postgresHelper.queryObjectList("SELECT * FROM " + TABLE_NAME + " WHERE TargetID = " + Long.toString(id) +";", RlUsedMaterial.class);
    }

    public List<RlUsedMaterial> getUsedMaterialsBySourceType(RlUsedMaterialType sourceType) throws Exception
    {
        return (List<RlUsedMaterial>)postgresHelper.queryObjectList("SELECT * FROM " + TABLE_NAME + " WHERE SourceType = '" + MemberInfoConverter.UsedMaterialTypeEnumToDbString((RlUsedMaterialType)sourceType) +"';", RlUsedMaterial.class);
    }

    public List<RlUsedMaterial> getUsedMaterialsByTargetType(RlUsedMaterialType targetType) throws Exception
    {
        return (List<RlUsedMaterial>)postgresHelper.queryObjectList("SELECT * FROM " + TABLE_NAME + " WHERE TargetType = '" + MemberInfoConverter.UsedMaterialTypeEnumToDbString((RlUsedMaterialType)targetType) +"';", RlUsedMaterial.class);
    }
}

