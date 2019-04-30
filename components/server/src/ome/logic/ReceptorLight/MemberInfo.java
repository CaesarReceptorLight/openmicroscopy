package ome.logic.ReceptorLight;

/**
 * Class which holds information about a type member.
 */
public class MemberInfo
{
	public String name;
	
	public String type;

	public Class classType;
	
	public MemberInfo(String name, String type, Class classType)
	{
		this.name = name;
		this.type = type;
		this.classType = classType;
	}
}