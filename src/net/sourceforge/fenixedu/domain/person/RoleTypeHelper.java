package net.sourceforge.fenixedu.domain.person;

public class RoleTypeHelper {

	private RoleTypeHelper() {
		super();
	}

	
	public static String enumRoleTypeNamesToCSV()
	{
		StringBuffer retval=new StringBuffer();
		for(RoleType r:RoleType.values())
		{
			if(retval.length()>0) retval.append(",");
			retval.append(r.name());
		}
		return retval.toString();
	}
	
	public static String enumRoleTypeNamesToArrayFormat()
	{
		StringBuffer retval=new StringBuffer("{\"");
		for(RoleType r:RoleType.values())
		{
			if(retval.length()>0) retval.append("\",\"");
			retval.append(r.name());
		}
		retval.append("\"}");
		return retval.toString();
	}
	
	public static String enumRoleTypeLabelsToArrayFormat()
	{
		StringBuffer retval=new StringBuffer("{\"");
		for(RoleType r:RoleType.values())
		{
			if(retval.length()>0) retval.append("\",\"");
			retval.append(r.getDefaultLabel());
		}
		retval.append("\"}");
		return retval.toString();
	}
}
