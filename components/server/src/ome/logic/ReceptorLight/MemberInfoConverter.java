package ome.logic.ReceptorLight;

import java.util.Iterator;

import ome.model.core.StringValue;
import ome.model.core.StringValueList;
import ome.model.core.IntValue;
import ome.model.core.IntValueList;
import ome.model.enums.RlFileType;
import ome.model.enums.RlUsedMaterialType;

public final class MemberInfoConverter
{
	private static final String SPLITTER = "§";
	
	public static String DbStringToJavaString(String value)
	{
		if(value == null)
			return "";
		else
			return value.replace("''", "'");
	}
	
	public static String JavaStringToDbString(String value)
	{
		if(value == null)
			return "";
		else
			return value.replace("'", "''");
	}
	
	public static String IntValueListToDbString(IntValueList list)
	{
		String value = "";
		
		if(list != null)
		{
			Iterator<IntValue> i = list.iterateValues();
			while(i.hasNext())
			{
				IntValue lv = i.next();
				String str = Integer.toString(lv.getValue());
				value += JavaStringToDbString(str) + SPLITTER;
			}
			
			if(value.length() > 3)
				value = value.substring(0, value.length() - SPLITTER.length());
		}
		
		return value;
	}
	
	public static String StringValueListToDbString(StringValueList list)
	{
		String value = "";
		
		if(list != null)
		{
			Iterator<StringValue> i = list.iterateValues();
			while(i.hasNext())
			{
				StringValue sv = i.next();
				String str = sv.getValue();
				value += JavaStringToDbString(str) + SPLITTER;
			}
			
			if(value.length() > 3)
				value = value.substring(0, value.length() - SPLITTER.length());
		}
		
		return value;
	}
	
	public static IntValueList DbStringToIntValueList(String value)
	{
		IntValueList list = new IntValueList();
		
		if(value != null)
		{
			if(value.compareTo("") != 0)
			{
				String[] values = value.split(SPLITTER);
				for (String s : values)
				{
					if (s.compareTo("") != 0)
					{
						IntValue iv = new IntValue();
						iv.setValue(Integer.parseInt(DbStringToJavaString(s)));
						list.addIntValue(iv);
					}
				}
			}
		}
		
		return list;
	}
	
	public static StringValueList DbStringToStringValueList(String value)
	{
		StringValueList list = new StringValueList();
		
		if(value != null)
		{
			if(value.compareTo("") != 0)
			{
				String[] values = value.split(SPLITTER);
				for (String s : values)
				{
					StringValue sv = new StringValue();
					sv.setValue(DbStringToJavaString(s));
					list.addStringValue(sv);
				}
			}
		}
		
		return list;
	}

    public static String FileTypeEnumToDbString(RlFileType type)
	{
		String value = "";
		
		if(type != null)
		{
			value = type.getValue();
		}
		
		return value;
	}

    public static RlFileType DbStringToFileTypeEnum(String value)
    {
        RlFileType type = new RlFileType("ReceptorLightFile"); //fallback

        if(value != null)
        {
            type.setValue(value);
        }

        return type;
    }

    public static String UsedMaterialTypeEnumToDbString(RlUsedMaterialType type)
	{
		String value = "";
		
		if(type != null)
		{
			value = type.getValue();
		}
		
		return value;
	}

    public static RlUsedMaterialType DbStringToUsedMaterialTypeEnum(String value)
    {
        RlUsedMaterialType type = new RlUsedMaterialType("UNKNOWN"); //fallback

        if(value != null)
        {
            type.setValue(value);
        }

        return type;
    }
}
