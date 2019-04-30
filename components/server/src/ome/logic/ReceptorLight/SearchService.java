package ome.logic.ReceptorLight;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ome.model.core.RlBaseObject;
import ome.model.core.RlSearchResult;
import ome.logic.ReceptorLight.*;

public class SearchService
{
    private List<String> KnownTables;

    private PostgresHelper postgresHelper;
    
    public SearchService(PostgresHelper postgresHelper)
    {    	
        this.postgresHelper = postgresHelper;
        try
        {
            FillListOfKnownTables();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public List<RlSearchResult> search(String pattern) throws Exception
    {
        List<RlSearchResult> result = new ArrayList<RlSearchResult>();

        for(String tableName : KnownTables)
            result.addAll(searchByType(pattern, tableName));

        return result;
    }
    
    public List<RlSearchResult> searchByName(String pattern, String memberName) throws Exception
    {
        List<RlSearchResult> result = new ArrayList<RlSearchResult>();

        for(String tableName : KnownTables)
            result.addAll(searchByNameAndType(pattern, memberName, tableName));

        return result;
    }
    
    public List<RlSearchResult> searchByType(String pattern, String typeName) throws Exception
    {
        List<RlSearchResult> result = new ArrayList<RlSearchResult>();

        String tableName = TypeNameToTableName(typeName);
        if(tableName == null)
            return result;

        List<String> columns = postgresHelper.getAllColumnsInTable(tableName);

        for(String s : columns)
        {
            result.addAll(searchByNameAndType(pattern, s, tableName));
        }
        
        return result;
    }
    
    public List<RlSearchResult> searchByNameAndType(String pattern, String memberName, String typeName) throws Exception
    {
        List<RlSearchResult> result = new ArrayList<RlSearchResult>();
        
        String tableName = TypeNameToTableName(typeName);
        if(tableName == null)
            return result;

        String returnTypeName = tableName.substring(0, tableName.indexOf("table"));
            
        if(!postgresHelper.existsColumnInTable(tableName, memberName.toLowerCase()))
            return result;

        String query;
        String numberPattern = null;
        String dataType = postgresHelper.getDataTypeOfColumn(tableName, memberName.toLowerCase());

        if(dataType.toLowerCase().contains("text"))
        {
            query = "SELECT uid from " + tableName + " where " + memberName + " ilike '%" + pattern + "%';";
        }
        else
        {
            if(dataType.toLowerCase().contains("integer"))
            {
                try
                {
                    numberPattern = ((Integer) (Integer.parseInt(pattern))).toString();
                } catch (Exception e)
                {
                    return result;
                }
            }
            else if(dataType.toLowerCase().contains("real"))
            {
                try
                {
                    numberPattern = ((Double) (Double.parseDouble(pattern))).toString();
                } catch (Exception e)
                {
                    return result;
                }
            }
            else if(dataType.toLowerCase().contains("boolean"))
            {
                String tmp = pattern.toLowerCase();
                if(tmp.equals("true") || tmp.equals("1"))
                    numberPattern = "true";
                else if(tmp.equals("false") || tmp.equals("0"))
                    numberPattern = "false";
                else
                    return result;
            }

            if(numberPattern == null)
                return result;

            query = "SELECT uid from " + tableName + " where " + memberName + " = '" + numberPattern + "';";
        }

        List<Integer> list = postgresHelper.queryIds(query);
        for (Integer i : list)
        {
            result.add(CreateSearchResult(i.intValue(), returnTypeName, memberName));
        }
            
        return result;
    }
    
    private String TypeNameToTableName(String typeName)
    {
        String value = typeName.toLowerCase();

        for(String tableName : KnownTables)
            if(tableName.contains(value))
                return tableName;

        return null;
    }

    private RlSearchResult CreateSearchResult(int id, String returnTypeName, String memberName) throws Exception
    {
        RlSearchResult r = new RlSearchResult();
        r.setUId(id);
        r.setTypeName(returnTypeName);
        r.setMemberName(memberName);

        RlBaseObject obj = (RlBaseObject)postgresHelper.getObjectById(returnTypeName + "table", id, RlBaseObject.class);

        r.setName(obj.getName());
        r.setOriginalObjectId(obj.getOriginalObjectId());
        r.setOwnerId(obj.getOwnerId());
        r.setOwnerGroupId(obj.getOwnerGroupId());
        r.setStatus(obj.getStatus());

        return r;
    }

    private void FillListOfKnownTables() throws Exception
    {
        KnownTables = postgresHelper.queryStringList("SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname != 'pg_catalog' AND schemaname != 'information_schema';");
    }
}
