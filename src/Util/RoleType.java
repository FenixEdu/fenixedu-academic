/*
 * Created on 11/Mar/2003 by jpvl
 *  
 */
package Util;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

/**
 * @author jpvl
 */
public final class RoleType extends ValuedEnum implements Serializable
{
	/* must match with field ROLE_TYPE in table ROLE */
	public static final int PERSON_TYPE = 1;
	public static final int STUDENT_TYPE = 2;
	public static final int TEACHER_TYPE = 3;
	public static final int TIME_TABLE_MANAGER_TYPE = 4;
	public static final int MASTER_DEGREE_CANDIDATE_TYPE = 5;
	public static final int MASTER_DEGREE_ADMINISTRATIVE_OFFICE_TYPE = 6;
	public static final int TREASURY_TYPE = 7;
	public static final int COORDINATOR_TYPE = 8;
	public static final int EMPLOYEE_TYPE = 9;
	public static final int MANAGEMENT_ASSIDUOUSNESS_TYPE = 10;
	public static final int MANAGER_TYPE = 11;
	public static final int DEGREE_ADMINISTRATIVE_OFFICE_TYPE = 12;
	public static final int CREDITS_MANAGER_TYPE = 13;
	public static final int DEPARTMENT_CREDITS_MANAGER_TYPE = 14;
	public static final int ERASMUS_TYPE = 15;
	public static final int DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER_TYPE = 16;
	public static final int SCIENTIFIC_COUNCIL_TYPE = 17;
	public static final int ADMINISTRATOR_TYPE = 18;
	public static final int OPERATOR_TYPE = 19;
	public static final int SEMINARIES_COORDINATOR_TYPE = 20;
	public static final int WEBSITE_MANAGER_TYPE = 21;
	public static final int GRANT_OWNER_TYPE = 22;
	public static final int GRANT_OWNER_MANAGER_TYPE = 23;
	public static final int DEPARTMENT_MEMBER_TYPE = 24;
	public static final int GEP_TYPE = 25;
	public static final int DIRECTIVE_COUNCIL_TYPE = 26;

	public static final RoleType PERSON = new RoleType("person", RoleType.PERSON_TYPE);
	public static final RoleType STUDENT = new RoleType("student", RoleType.STUDENT_TYPE);
	public static final RoleType TEACHER = new RoleType("teacher", RoleType.TEACHER_TYPE);
	public static final RoleType TIME_TABLE_MANAGER =
		new RoleType("timeTableManager", RoleType.TIME_TABLE_MANAGER_TYPE);
	public static final RoleType MASTER_DEGREE_CANDIDATE =
		new RoleType("masterDegreeCandidate", RoleType.MASTER_DEGREE_CANDIDATE_TYPE);
	public static final RoleType MASTER_DEGREE_ADMINISTRATIVE_OFFICE =
		new RoleType(
			"masterDegreeAdministrativeOffice",
			RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE_TYPE);
	public static final RoleType TREASURY = new RoleType("treasury", RoleType.TREASURY_TYPE);
	public static final RoleType COORDINATOR = new RoleType("coordinator", RoleType.COORDINATOR_TYPE);
	public static final RoleType EMPLOYEE = new RoleType("employee", RoleType.EMPLOYEE_TYPE);
	public static final RoleType MANAGEMENT_ASSIDUOUSNESS =
		new RoleType("managementAssiduousness", RoleType.MANAGEMENT_ASSIDUOUSNESS_TYPE);
	public static final RoleType MANAGER = new RoleType("manager", RoleType.MANAGER_TYPE);
	public static final RoleType DEGREE_ADMINISTRATIVE_OFFICE =
		new RoleType("degreeAdministrativeOffice", RoleType.DEGREE_ADMINISTRATIVE_OFFICE_TYPE);
	public static final RoleType CREDITS_MANAGER =
		new RoleType("creditsManager", RoleType.CREDITS_MANAGER_TYPE);
	public static final RoleType DEPARTMENT_CREDITS_MANAGER =
		new RoleType("role.department.credits.manager", RoleType.DEPARTMENT_CREDITS_MANAGER_TYPE);
	public static final RoleType DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER =
		new RoleType(
			"degreeAdministrativeOfficeSuperUser",
			RoleType.DEGREE_ADMINISTRATIVE_OFFICE_SUPER_USER_TYPE);
	public static final RoleType ERASUMS = new RoleType("erasmus", RoleType.ERASMUS_TYPE);
	public static final RoleType SCIENTIFIC_COUNCIL =
		new RoleType("scientificCouncil", RoleType.SCIENTIFIC_COUNCIL_TYPE);
	public static final RoleType ADMINISTRATOR = new RoleType("admin", RoleType.ADMINISTRATOR_TYPE);
	public static final RoleType OPERATOR = new RoleType("operator", RoleType.OPERATOR_TYPE);
	public static final RoleType SEMINARIES_COORDINATOR =
		new RoleType("seminariesCoordinator", RoleType.SEMINARIES_COORDINATOR_TYPE);
	public static final RoleType WEBSITE_MANAGER =
		new RoleType("WebSiteManager", RoleType.WEBSITE_MANAGER_TYPE);
	public static final RoleType GRANT_OWNER = new RoleType("grantOwner", RoleType.GRANT_OWNER_TYPE);
	public static final RoleType GRANT_OWNER_MANAGER =
		new RoleType("grantOwnerManager", RoleType.GRANT_OWNER_MANAGER_TYPE);
	public static final RoleType DEPARTMENT_MEMBER =
		new RoleType("role.department.member", RoleType.DEPARTMENT_MEMBER_TYPE);
	public static final RoleType GEP = new RoleType("role.gep", RoleType.GEP_TYPE);
	public static final RoleType DIRECTIVE_COUNCIL =
		new RoleType("role.directiveCouncil", RoleType.DIRECTIVE_COUNCIL_TYPE);

	/**
	 * @param name
	 * @param value
	 */
	private RoleType(String name, int value)
	{
		super(name, value);
	}

	public static RoleType getEnum(String roleType)
	{
		return (RoleType) getEnum(RoleType.class, roleType);
	}

	public static RoleType getEnum(int roleType)
	{
		return (RoleType) getEnum(RoleType.class, roleType);
	}

	public static Map getEnumMap()
	{
		return getEnumMap(RoleType.class);
	}

	public static List getEnumList()
	{
		return getEnumList(RoleType.class);
	}

	public static Iterator iterator()
	{
		return iterator(RoleType.class);
	}

	public String toString()
	{
		String result = "Role Type:\n";
		result += "\n  - Role Type : " + this.getName();

		return result;
	}

}
