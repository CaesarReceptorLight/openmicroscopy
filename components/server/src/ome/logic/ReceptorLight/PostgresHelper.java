package ome.logic.ReceptorLight;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

import ome.model.IObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgresHelper
{   
    public static final String HOST = "jdbc:postgresql://localhost:";
    public static final String DB_PORT = "5432"; // at configuration file /etc/postgresql/9.3/main/postgresql.conf
    
    private Connection dbConnection = null;
    private Logger log;
    
    public PostgresHelper(Logger log)
    {
        this.log = log;
    }
    
    public void openDatabase(String rl_db_name, String rl_db_user, String rl_db_pass) throws Exception
    {
        if(dbConnection != null)
            return;
        
        try
        {
            Class.forName("org.postgresql.Driver");
            dbConnection = DriverManager.getConnection(
                    HOST + DB_PORT + "/" + rl_db_name,
                    rl_db_user, rl_db_pass);
        }
        catch(Exception e)
        {
        	throw new Exception("[openDatabase] Can't connect to database '" + rl_db_name + "'.");
        }
    }
    
    @SuppressWarnings("rawtypes")
    public void checkTable(String tableName, Class typeClass) throws Exception
    {           
    	if(tableName == null)
    		throw new Exception("[checkTable] tableName is null.");
    	
    	if(typeClass == null)
    		throw new Exception("[checkTable] typeClass is null.");
    	
    	List<MemberInfo> tableColumns = MemberInfoHelper.getMemberInfosFromType(typeClass);
    	if(tableColumns == null)
    		throw new Exception("[checkTable] tableColumns is null.");
    	
        if(existsTable(tableName) == false)
        {
            createTable(tableName, tableColumns);
        }
        else
        {
        	checkAllTableColumns(tableName, tableColumns);
        }
    }   
    
    @SuppressWarnings("rawtypes")
    public List<? extends IObject> queryObjectList(String query, Class targetClass) throws Exception
    {
        List<IObject> list = new ArrayList<IObject>();
        
        if(query == null)
            return list;
        
        if(targetClass == null)
        	return list;
        
    	dbConnection.setAutoCommit(false);
        Statement statement = dbConnection.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while(queryResult.next())
        {           
        	IObject obj = MemberInfoHelper.createInstanceFromClass(targetClass);       	
        	
        	MemberInfoHelper.updateObjectFromQueryResult(obj, queryResult);
            list.add(obj);
        }
        
        queryResult.close();                
        statement.close();
        
        return list;
    }

    @SuppressWarnings("rawtypes")
    public List<String> queryStringList(String query) throws Exception
    {
        List<String> list = new ArrayList<String>();
        
        if(query == null)
            return list;
        
    	dbConnection.setAutoCommit(false);
        Statement statement = dbConnection.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while(queryResult.next())
        {
            list.add(queryResult.getString(1));
        }
        
        queryResult.close();                
        statement.close();
        
        return list;
    }
    
    @SuppressWarnings("rawtypes")
	public IObject queryObject(String query, Class targetClass) throws Exception
    {
        IObject returnObject = null;
        
        if(query == null)
            return returnObject;
        
        if(targetClass == null)
        	return returnObject;
        
    	dbConnection.setAutoCommit(false);
        Statement statement = dbConnection.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        if(queryResult.next())
        {           
        	returnObject = MemberInfoHelper.createInstanceFromClass(targetClass);
        	
        	MemberInfoHelper.updateObjectFromQueryResult(returnObject, queryResult);
        }
        
        queryResult.close();                
        statement.close();
        
        return returnObject;
    }
    
    public List<Integer> queryIds(String query) throws Exception
    {
        List<Integer> list = new ArrayList<Integer>();
        
        if(query == null)
            return list;
            
        dbConnection.setAutoCommit(false);
        Statement statement = dbConnection.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while(queryResult.next())
        {           
        	int value = queryResult.getInt("uid");
            list.add(new Integer(value));
        }
        
        queryResult.close();                
        statement.close();
            
        return list;
    }
    
    public void queryUpdate(String query) throws Exception
    {        
        if(query == null)
        	throw new Exception("[queryUpdate] query is null.");
        
    	dbConnection.setAutoCommit(false);
    	Statement statement = dbConnection.createStatement();
    	
    	statement.executeUpdate(query);            
        
        statement.close();
        dbConnection.commit();
    }
    
    public int queryCount(String tabelName) throws Exception
    {
    	int count = 0;
        
	    dbConnection.setAutoCommit(false);
	    Statement statement = dbConnection.createStatement();
	    
        ResultSet queryResult = statement.executeQuery("SELECT count(*) FROM " + tabelName + ";");
        
        queryResult.next();
        count = queryResult.getInt("count");
        
        queryResult.close();
        statement.close();
        
        return count;
    }
    
    public boolean existsUId(String tabelName, int uid) throws Exception
    {
    	int count = 0;
        
	    dbConnection.setAutoCommit(false);
	    Statement statement = dbConnection.createStatement();
	    
        ResultSet queryResult = statement.executeQuery("SELECT count(*) FROM " + tabelName + " WHERE UId = " + Long.toString(uid) +";");
        
        queryResult.next();
        count = queryResult.getInt("count");
        
        queryResult.close();
        statement.close();
        
        return (count > 0);
    }
    
    private boolean existsTable(String tableName) throws Exception
    {
        boolean exists = false;
        
        Statement statement = dbConnection.createStatement();
        ResultSet queryResult = statement.executeQuery( "SELECT count(*) FROM pg_class WHERE relname='" + tableName + "' and relkind='r';");
        
        queryResult.next();
        int count = queryResult.getInt("count");
        if(count > 0)
            exists = true;
        
        queryResult.close();
        statement.close();        
        
        return exists;
    }
    
    private void checkAllTableColumns(String tableName, List<MemberInfo> tableColumns) throws Exception
    {
    	if(tableName == null)
    		throw new Exception("[checkAllTableColumns] tableName is null.");
    	
    	if(tableColumns == null)
    		throw new Exception("[checkAllTableColumns] tableColumns is null.");
    	
    	for(MemberInfo column : tableColumns)
    	{
    		if(existsColumnInTable(tableName, column.name.toLowerCase()) == false)
    		{
    			createColumnInTable(tableName, column);
    		}
    	}
    }
    
    public boolean existsColumnInTable(String tableName, String columnName) throws Exception
    {
        boolean exists = false;
        
        Statement statement = dbConnection.createStatement();
        ResultSet queryResult = statement.executeQuery( "SELECT count(attname) FROM pg_attribute WHERE attrelid = (SELECT oid FROM pg_class WHERE relname = '" + tableName + "') AND attname = '" + columnName + "';");
        
        queryResult.next();
        int count = queryResult.getInt("count");
        if(count > 0)
            exists = true;
        
        queryResult.close();
        statement.close();
        
        return exists;
    }
    
    private void createColumnInTable(String tableName, MemberInfo column) throws Exception
    {        
        log.info("Add column '" + column.name + "' to table '" + tableName + "'.");
        
        if(tableName == null)
        	throw new Exception("[createColumnInTable] tableName is null.");
    	
    	if(column == null)
    		throw new Exception("[createColumnInTable] column is null.");
        
        Statement statement = dbConnection.createStatement();
        statement.executeUpdate( "ALTER TABLE " + tableName + " ADD " + column.name.toLowerCase() + " " + column.type + ";");
        statement.close();
    }
    
    private void createTable(String tableName, List<MemberInfo> tableColumns) throws Exception
    {        
        if(tableName == null)
        	throw new Exception("[createTable] tableName is null.");
        
        if(tableColumns == null)
        	throw new Exception("[createTable] tableColumns is null.");
        
        String tableHeader = MemberInfoHelper.createTableHeaderFromMemberInfos(tableColumns);
        
        if(tableHeader == null)
        	throw new Exception("[createTable] tableHeader is null.");
        
        Statement stmt = dbConnection.createStatement();
        String sql = "CREATE TABLE " + tableName + tableHeader + ";";
        
        stmt.executeUpdate(sql);
        
        stmt.close();
    }
    
    public int createNewId(String tableName) throws Exception
    {
    	int newId = queryCount(tableName) + 1;
        
        while(existsUId(tableName, newId))
        {
            newId++;
        }
        
        return newId;
    }
    
    public void deleteById(String tableName, long id) throws Exception
    {       	
    	queryUpdate("DELETE FROM " + tableName + " WHERE UId = " + Long.toString(id) +";");
    }
    
    @SuppressWarnings("rawtypes")
	public IObject getObjectById(String tableName, long id, Class targetClass) throws Exception
    {       	
        return queryObject
        		("SELECT * FROM " + tableName + " WHERE UId = " + Long.toString(id) +";", 
        		targetClass);
    }
    
    @SuppressWarnings({ "rawtypes" })
    public List<? extends IObject> getAllObjects(String tableName, Class targetClass) throws Exception
    {
        return queryObjectList
        		("SELECT * FROM " + tableName + ";", 
        				targetClass);
    }

    public List<String> getAllColumnsInTable(String tableName) throws Exception
    {
        String query = "SELECT column_name FROM information_schema.columns WHERE table_schema='public' AND table_name='" + tableName + "';";
        List<String> columns = new ArrayList<String>();

        dbConnection.setAutoCommit(false);
        Statement statement = dbConnection.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while(queryResult.next())
        {
            String value = queryResult.getString("column_name");
            columns.add(value);
        }

        queryResult.close();
        statement.close();

        return columns;
    }

    public String getDataTypeOfColumn(String tableName, String columnName) throws Exception
    {
        String query = "SELECT data_type FROM information_schema.columns WHERE table_schema='public' AND table_name='" + tableName + "' AND column_name='" + columnName + "';";

        String dataType = "";

        dbConnection.setAutoCommit(false);
        Statement statement = dbConnection.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        if(queryResult.next())
        {
            dataType = queryResult.getString("data_type");
        }

        queryResult.close();
        statement.close();

        return dataType;
    }
    
    public void updateObject(String tableName, long id, IObject object) throws Exception
    {    	
    	if(object == null)
            throw new Exception("[updateObject] object is NULL");
        
    	String updateQuery = MemberInfoHelper.createUpdateQueryFromObject(object);
    	if(updateQuery == null)
    		throw new Exception("[updateObject] updateQuery is NULL");    
    	
        String sql = "UPDATE " + tableName + " SET " +
                updateQuery + 
                " WHERE UId=" + Long.toString(id) + 
                ";";
           
        queryUpdate(sql);
    }
    
    public void insertObject(String tableName, IObject object) throws Exception
    {
    	if(object == null)
            throw new Exception("[insertObject] object is NULL");
        
        String insertQuery = MemberInfoHelper.createInsertQueryFromObject(object);
        if(insertQuery == null)
        	throw new Exception("[insertObject] insertQuery is NULL");
                
        String sql = "INSERT INTO " + tableName + insertQuery;        
                
        queryUpdate(sql);
    }
}
