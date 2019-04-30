package ome.logic.ReceptorLight;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ome.model.IObject;
import ome.model.core.StringValueList;
import ome.model.core.IntValueList;
import ome.model.core.RlBaseObject;
import ome.model.enums.RlFileType;
import ome.model.enums.RlUsedMaterialType;

public final class MemberInfoHelper
{
	/**
	 * Generates a list of members of the given type.
	 * Only members are listed, which are not inherited from a superclass or implemented from an interface.
	 * Members which are of type ome.model.core.* will ignored and therefore  split up into there members.
	 * @param cls Type of object to split up into member list.
	 * @return A list of member information.
	 */
	@SuppressWarnings("rawtypes")
	public static List<MemberInfo> getMemberInfosFromType(Class cls) throws Exception
    {
    	List<MemberInfo> list = new ArrayList<MemberInfo>();
    	
    	if(cls == null)
    		throw new Exception("[getMemberInfosFromType] cls is null.");

    	//get all members of given type
        Method[] subMethods = cls.getMethods();
        //get list of all members of superclass of given type
        Method[] superMethods = cls.getSuperclass().getMethods();

        Class superClass = cls.getSuperclass();
        while(superClass != null)
        {
            if(superClass == RlBaseObject.class)
                superMethods = superClass.getSuperclass().getMethods();

            superClass = superClass.getSuperclass();
        }
        
        //get list of members of implemented interfaces
        Class[] interfaces = cls.getInterfaces();
        List<String> interfaceMethods = new ArrayList<String>();
        for(Class c : interfaces)
            for(Method m : c.getMethods())
                interfaceMethods.add(m.getName());

        for (Method m : subMethods)
        {
        	//add only members which are not inherited from a superclass or implemented from an interface
            if(!Arrays.asList(superMethods).contains(m) && !interfaceMethods.contains(m.getName()))
            	//add only members which are readable by a getXXX() method 
                if(m.getName().startsWith("get"))
                {
                	Class returnType = m.getReturnType();
                	
                	if(returnType == StringValueList.class)
                	{
                		String name = m.getName().substring(3);
	                    	String t = "TEXT";
                		list.add(new MemberInfo(name, t, returnType));
                	}
                	else if(returnType == IntValueList.class)
                	{
                		String name = m.getName().substring(3);
	                    	String t = "TEXT";
                		list.add(new MemberInfo(name, t, returnType));
                	}
                	else if(returnType == RlFileType.class)
                	{
                		String name = m.getName().substring(3);
	                    	String t = "TEXT";
                		list.add(new MemberInfo(name, t, returnType));
                	}
                        else if(returnType == RlUsedMaterialType.class)
                	{
                		String name = m.getName().substring(3);
	                    	String t = "TEXT";
                		list.add(new MemberInfo(name, t, returnType));
                	}
                	//if member is a type inside the package of given type then split up into sub members
                	else if(returnType.getPackage() == cls.getPackage())
                	{
                        List<MemberInfo> l = getMemberInfosFromType(returnType);
                        for(MemberInfo tc : l)
                            list.add(tc);
                    }                	
                	else
                	{                	
	                    String name = m.getName().substring(3);
	                    String t = "TEXT";
	
	                    String retTxt = m.getReturnType().toString();
	                    
	                    if(retTxt.equals(Integer.class.toString()) || retTxt.equals("int") || retTxt.equals("long"))
	                    {
	                        t = "INT";
	                    }
	                    else if(retTxt.equals(Float.class.toString()) || retTxt.equals(Double.class.toString()) || retTxt.equals("float") || retTxt.equals("double"))
	                    {
	                        t = "REAL";
	                    }
                        else if(retTxt.equals(Boolean.class.toString()) || retTxt.equals("boolean"))
                        {
                            t = "boolean";
                        }

	
	                    list.add(new MemberInfo(name, t, returnType));
                	}
                }
        }

        return list;
    }
	
	/**
	 * Generates a query string of a table header of the given list of member infos.
	 * This query string is used to generate a table at the database. 
	 * @param members List of member infos.
	 * @return Query string for table header.
	 */
	public static String createTableHeaderFromMemberInfos(List<MemberInfo> members) throws Exception
    {
    	if(members == null)
    		throw new Exception("[createTableHeaderFromMemberInfos] List is null.");

        if(members.size() == 0)
        	throw new Exception("[createTableHeaderFromMemberInfos] List is empty.");

        String header = " (";

        for(MemberInfo m : members)
        {
        	if(m.name.equals("UId"))
                header += m.name.toLowerCase() + " " + m.type + " PRIMARY KEY NOT NULL, ";
        	else
        		header += m.name.toLowerCase() + " " + m.type + ", ";
        }

        return header.substring(0, header.length() - 2) + ") ";
    }
	
	@SuppressWarnings("rawtypes")
	public static IObject createInstanceFromClass(Class cls) throws Exception
    {
        if(cls == null)
            throw new Exception("[createInstanceFromClass] cls is null!");

        IObject obj = null;

        obj = (IObject) cls.newInstance();

        //get all members of given type
        Method[] subMethods = cls.getMethods();
        //get list of all members of superclass of given type
        Method[] superMethods = cls.getSuperclass().getMethods();

        Class superClass = cls.getSuperclass();
        while(superClass != null)
        {
            if(superClass == RlBaseObject.class)
                superMethods = superClass.getSuperclass().getMethods();

            superClass = superClass.getSuperclass();
        }

        //get list of members of implemented interfaces
        Class[] interfaces = cls.getInterfaces();
        List<String> interfaceMethods = new ArrayList<String>();
        for(Class c : interfaces)
            for(Method m : c.getMethods())
                interfaceMethods.add(m.getName());

        for (Method m : subMethods)
        {
            //add only members which are not inherited from a superclass or implemented from an interface
            if(!Arrays.asList(superMethods).contains(m) && !interfaceMethods.contains(m.getName()))
                //add only members which are readable by a setXXX() method
                if(m.getName().startsWith("set"))
                {
                    Class returnType = m.getParameterTypes()[0];

                    //if member is a type inside the package of given type then split up into sub members
                    if((returnType.getPackage() == cls.getPackage()))
                    {
                        IObject innerObj = createInstanceFromClass(returnType);
                        m.invoke(obj, innerObj);
                    }
                    else
                    {
                        if((returnType == Integer.class) || (returnType == int.class))
                        	m.invoke(obj, 0);
                        else if((returnType == Float.class) || (returnType == float.class))
                            m.invoke(obj, 0.0f);
                        else if((returnType == Double.class) || (returnType == double.class))
                            m.invoke(obj, 0.0);
                        else if((returnType == Boolean.class) || (returnType == boolean.class))
                            m.invoke(obj, false);
                        else
                        	m.invoke(obj, returnType.newInstance());
                    }
                }
        }

        return obj;
    }
	
	/**
	 * Creates a string for a psql insert-command to insert the given object into a psql database.
	 * @param object The object to insert.
	 * @return A  string for a psql insert-command.
	 */
	public static String createInsertQueryFromObject(IObject object) throws Exception
    {
		if(object == null)
			throw new Exception("[createInsertQueryFromObject] Object is null.");
		
        String columns = "";
        String values = "";

        List<MemberInfo> members = getMemberInfosFromType(object.getClass());

        for (MemberInfo m : members)
        {
            Object retValue = getValueFromMethod("get" + m.name, object);
            if(retValue == null)
                throw new Exception("[createInsertQueryFromObject] 'get" + m.name + "' don't exists in object.");

            if (retValue != null)
            {
                columns += m.name.toLowerCase() + ", ";
                if (retValue instanceof String)
                {
                    values += "'" + MemberInfoConverter.JavaStringToDbString(String.valueOf(retValue)) + "', ";
                }
                else if (retValue instanceof Integer)
                {
                    values += Integer.toString((int) retValue) + ", ";
                }
                else if (retValue instanceof Double)
                {
                    values += Double.toString((double) retValue) + ", ";
                }
                else if (retValue instanceof Float)
                {
                    values += Float.toString((float) retValue) + ", ";
                }
                else if (retValue instanceof Boolean)
                {
                    values += Boolean.toString((boolean) retValue) + ", ";
                }
                else if (retValue instanceof ome.model.core.StringValueList)
                {
                    values += "'" + MemberInfoConverter.StringValueListToDbString((StringValueList)retValue) + "', ";
                }
                else if (retValue instanceof ome.model.core.IntValueList)
                {
                    values += "'" + MemberInfoConverter.IntValueListToDbString((IntValueList)retValue) + "', ";
                }
                else if (retValue instanceof RlFileType)
                {
                    values += "'" + MemberInfoConverter.FileTypeEnumToDbString((RlFileType)retValue) + "', ";
                }
                else if (retValue instanceof RlUsedMaterialType)
                {
                    values += "'" + MemberInfoConverter.UsedMaterialTypeEnumToDbString((RlUsedMaterialType)retValue) + "', ";
                }
            }
        }

        if(columns.length() < 2)
        	throw new Exception("[createInsertQueryFromObject] No items for inserting.");

        columns = columns.substring(0, columns.length() - 2);
        values = values.substring(0, values.length() - 2);

        return " (" + columns + ") VALUES (" + values + "); ";
    }
	
	/**
	 * Creates a string for a psql update-command to insert the given object into a psql database.
	 * @param object The object to insert.
	 * @return A  string for a psql update-command.
	 */
	public static String createUpdateQueryFromObject(IObject object) throws Exception
    {
		if(object == null)
			throw new Exception("[createUpdateQueryFromObject] Object is null.");
		
        String query = "";

        List<MemberInfo> members = getMemberInfosFromType(object.getClass());

        for (MemberInfo m : members)
        {
            Object retValue = getValueFromMethod("get" + m.name, object);
            //if(retValue == null)
            //    throw new Exception("[createUpdateQueryFromObject] 'get" + m.name + "' don't exists in object.");

            if (retValue != null)
            {
                query += m.name.toLowerCase() + "=";
                if (retValue instanceof String)
                {
                    query += "'" + MemberInfoConverter.JavaStringToDbString(String.valueOf(retValue)) + "', ";
                }
                else if (retValue instanceof Integer)
                {
                	query += Integer.toString((int) retValue) + ", ";
                }
                else if (retValue instanceof Double)
                {
                    query += Double.toString((double) retValue) + ", ";
                }
                else if (retValue instanceof Float)
                {
                    query += Float.toString((float) retValue) + ", ";
                }
                else if (retValue instanceof Boolean)
                {
                    query += Boolean.toString((boolean) retValue) + ", ";
                }
                else if (retValue instanceof ome.model.core.StringValueList)
                {
                	query += "'" + MemberInfoConverter.StringValueListToDbString((StringValueList)retValue) + "', ";
                }
                else if (retValue instanceof ome.model.core.IntValueList)
                {
                	query += "'" + MemberInfoConverter.IntValueListToDbString((IntValueList)retValue) + "', ";
                }
                else if (retValue instanceof RlFileType)
                {
                	query += "'" + MemberInfoConverter.FileTypeEnumToDbString((RlFileType)retValue) + "', ";
                }
                else if (retValue instanceof RlUsedMaterialType)
                {
                	query += "'" + MemberInfoConverter.UsedMaterialTypeEnumToDbString((RlUsedMaterialType)retValue) + "', ";
                }
            }
        }

        if(query.length() < 2)
        	throw new Exception("[createUpdateQueryFromObject] No items for updating.");

        query = query.substring(0, query.length() - 2);

        return query + " ";
    }
	
	/**
	 * Receives the return object of method with given name of given object by invoking the method.
	 * @param methodName Name of method to invoke.
	 * @param object Instance to invoke..
	 * @return The return object of method with given name of given object.
	 */
	public static Object getValueFromMethod(String methodName, Object object) throws Exception
    {
		if(object == null)
			throw new Exception("[getValueFromMethod] Object is null.");
		
		if(methodName == null)
			throw new Exception("[getValueFromMethod] MethodName is null.");
		
        try
        {
            Method method = object.getClass().getMethod(methodName);
            return method.invoke(object);
        }
        catch(Exception e)
        {
            //method did not exists in object
        	// !!! don't throw this exception !!!
            //get childObjects and search for method in everyone of them
            List<Object> childObjects = getChildObjects(object);
            
            for(Object child : childObjects)
            {
                Object retVal = getValueFromMethod(methodName, child);
                if(retVal != null)
                    return retVal;
            }
        }

        return null;
    }
	
	/**
	 * Get a list of all objects which could received by a getXXX method of given object
	 * and are defined at the same package as the given object.
	 * @param object Object at searching for. 
	 * @return A list of objects which could received by a getXXX() method.
	 */
	@SuppressWarnings("rawtypes")
	private static List<Object> getChildObjects(Object object) throws Exception
    {
		if(object == null)
			throw new Exception("[getChildObjects] Object is null.");
		
        List<Object> list = new ArrayList<Object>();
        
	    Method[] subMethods = object.getClass().getMethods();
	    Method[] superMethods = object.getClass().getSuperclass().getMethods();
        Class superClass = object.getClass().getSuperclass();
        while(superClass != null)
        {
            if(superClass == RlBaseObject.class)
                superMethods = superClass.getSuperclass().getMethods();

            superClass = superClass.getSuperclass();
        }
	
	    Class[] interfaces = object.getClass().getInterfaces();
	    List<String> interfaceMethods = new ArrayList<String>();
	    for(Class c : interfaces)
	        for(Method m : c.getMethods())
	            interfaceMethods.add(m.getName());
	
	    for (Method m : subMethods)
	    {
	        if(!Arrays.asList(superMethods).contains(m) && !interfaceMethods.contains(m.getName()))
	        {
	            if(m.getName().startsWith("get"))
	            {
	                Class returnType = m.getReturnType();
	
	                if(returnType.getPackage() == object.getClass().getPackage())
	                {             
	                	Object obj = null;
	                	try
	                	{
	                		obj = m.invoke(object);
	                	}
	                	catch(Exception ex)
	                	{
	                		throw new Exception("[getChildObjects] Can't get value from method '" + m.getName() + "'.");
	                	}
	                	
	                    if(obj != null)
	                        list.add(obj);
	                }
	            }
	        }
	    }

        return list;
    }
	
	public static void updateObjectFromQueryResult(IObject object, ResultSet queryResult) throws Exception
    {
		if(queryResult == null)
			throw new Exception("[updateObjectFromQueryResult] QueryResult is null.");
		
		if(object == null)
			throw new Exception("[updateObjectFromQueryResult] Object is null.");

        List<MemberInfo> members = getMemberInfosFromType(object.getClass());

        for (MemberInfo m : members)
        {
        	Object value = null;
        	try
        	{
        		value = queryResult.getObject(m.name.toLowerCase());
        	}
        	catch(Exception ex)
        	{
        		throw new Exception("[updateObjectFromQueryResult] Can't get value '" + m.name + "' from QueryResult.");
        	}

            Object setValue;
            if((m.classType == Integer.class) || (m.classType == int.class))
                setValue = Integer.parseInt(value != null ? value.toString() : "0");
            else if((m.classType == Float.class) || (m.classType == float.class))
                setValue = Float.parseFloat(value != null ? value.toString() : "0");
            else if((m.classType == Double.class) || (m.classType == double.class))
                setValue = Double.parseDouble(value != null ? value.toString() : "0");
            else if((m.classType == Boolean.class) || (m.classType == boolean.class))
                setValue = Boolean.parseBoolean(value != null ? value.toString() : "false");
            else
                setValue = value != null ? value : "";

			String methodName = "set" + m.name;
			boolean retVal = setValueToMethod(methodName, setValue, object);
			if(retVal == false)
				throw new Exception("[updateObjectFromQueryResult] Can't set value to method '" + methodName + "' Value = "+ setValue.getClass().toString() + ":'" + setValue.toString() +"'.");
        }
    }
	
	public static boolean setValueToMethod(String methodName, Object value, Object object) throws Exception
    {	
        //Don't throw an exception here! A value can be NULL if a column was added to an table with existing rows!				
		//if(value == null)
		//	throw new Exception("[setValueToMethod] Value is null.");

		if(object == null)
			throw new Exception("[setValueToMethod] Object is null.");

        try
        {
        	Method[] methods = object.getClass().getMethods();
            for(Method m : methods)
            {
                if(m.getName().equals(methodName))
                {                    
                	Object setter = null;
                    Class retType = m.getParameterTypes()[0];
                    
                    if(value.getClass() == String.class)
                    { 
                        if(retType == String.class)
                            setter = MemberInfoConverter.DbStringToJavaString((String)value);
                        else if(retType == StringValueList.class)
                            setter = MemberInfoConverter.DbStringToStringValueList((String)value);
                        else if(retType == IntValueList.class)
                            setter = MemberInfoConverter.DbStringToIntValueList((String)value);
                        else if(retType == RlFileType.class)
                            setter = MemberInfoConverter.DbStringToFileTypeEnum((String)value);
                        else if(retType == RlUsedMaterialType.class)
                            setter = MemberInfoConverter.DbStringToUsedMaterialTypeEnum((String)value);
                    }
                        
                    if(setter == null)
                    {
                        if(value == null)
                        {
                            //generate object from std. constructor
                            if(retType.getSuperclass() == Number.class)
                            {
                                //numbers do not have an standard constructor
                                //so we instanciate them by a string
                                Constructor co = retType.getConstructor(String.class);
                                setter = co.newInstance("0");
                            }
                            else
                            {
                                Constructor co = retType.getConstructor();
                                setter = co.newInstance();
                            }
                        }
                        else
                        {
                            setter = value;
                        }
                    }
                    
                    m.invoke(object, setter);
                    return true;
                }
            }
            //method did not exists in object
            //get childObjects and search for method in everyone of them
            List<Object> childObjects = getChildObjects(object);
            for(Object child : childObjects)
            {
                boolean retVal = setValueToMethod(methodName, value, child);
                if(retVal == true)
                    return retVal;
            }
            
        }
        catch(Exception e)
        {
            //method did not exists in object
        	//!!! don't throw this exception !!!
            //get childObjects and search for method in everyone of them
            List<Object> childObjects = getChildObjects(object);
            for(Object child : childObjects)
            {
                boolean retVal = setValueToMethod(methodName, value, child);
                if(retVal == true)
                    return retVal;
            }
        }

        return false;
    }
}
