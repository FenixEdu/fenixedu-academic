/*
 * Created on 5/Jan/2004
 *  
 */
package Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.enum.ValuedEnum;

/**
 * @author Tânia Pousão
 *  
 */
public class ExemptionGratuityType extends ValuedEnum
{
	public static final int INSTITUTION_TYPE = 1;
	public static final int INSTITUTION_GRANT_OWNER_TYPE = 2;
	public static final int OTHER_INSTITUTION_TYPE = 3;
	public static final int UNIVERSITY_TEACHER_TYPE = 4;
	public static final int POLYTECHNICAL_TEACHER_TYPE = 5;
	public static final int PALOP_TEACHER_TYPE = 6;
	public static final int STUDENT_TEACH_TYPE = 7;
	public static final int FCT_GRANT_OWNER_TYPE = 8;
	public static final int MILITARY_SON_TYPE = 9;
	public static final int OTHER_TYPE = 10;

	public static final ExemptionGratuityType INSTITUTION =
		new ExemptionGratuityType("institution", ExemptionGratuityType.INSTITUTION_TYPE);
	public static final ExemptionGratuityType INSTITUTION_GRANT_OWNER =
		new ExemptionGratuityType(
			"institutionGrantOwner",
			ExemptionGratuityType.INSTITUTION_GRANT_OWNER_TYPE);
	public static final ExemptionGratuityType OTHER_INSTITUTION =
		new ExemptionGratuityType(
			"otherInstitution",
			ExemptionGratuityType.OTHER_INSTITUTION_TYPE);
	public static final ExemptionGratuityType UNIVERSITY_TEACHER =
		new ExemptionGratuityType("universityTeacher", ExemptionGratuityType.UNIVERSITY_TEACHER_TYPE);
	public static final ExemptionGratuityType POLYTECHNICAL_TEACHER =
		new ExemptionGratuityType(
			"polytechnicalTeacher",
			ExemptionGratuityType.POLYTECHNICAL_TEACHER_TYPE);
	public static final ExemptionGratuityType PALOP_TEACHER =
	new ExemptionGratuityType(
			"PALOPTeacher",
			ExemptionGratuityType.PALOP_TEACHER_TYPE);
		public static final ExemptionGratuityType STUDENT_TEACHER =
	new ExemptionGratuityType(
			"StudentTeacher",
			ExemptionGratuityType.STUDENT_TEACH_TYPE);
	public static final ExemptionGratuityType FCT_GRANT_OWNER =
		new ExemptionGratuityType("FCTGrantOwner", ExemptionGratuityType.FCT_GRANT_OWNER_TYPE);
	public static final ExemptionGratuityType MILITARY_SON =
		new ExemptionGratuityType("MilitarySon", ExemptionGratuityType.MILITARY_SON_TYPE);

	protected ExemptionGratuityType(String name, int value)
	{
		super(name, value);
	}

	public static ExemptionGratuityType getEnum(String exemptionGratuityType)
	{
		return (ExemptionGratuityType) getEnum(ExemptionGratuityType.class, exemptionGratuityType);
	}

	public static ExemptionGratuityType getEnum(int exemptionGratuityType)
	{
		return (ExemptionGratuityType) getEnum(ExemptionGratuityType.class, exemptionGratuityType);
	}

	public static Map getEnumMap()
	{
		return getEnumMap(ExemptionGratuityType.class);
	}

	public static List getEnumList()
	{
		return getEnumList(ExemptionGratuityType.class);
	}

	public static Iterator iterator()
	{
		return iterator(ExemptionGratuityType.class);
	}

	public String toString()
	{
		String exemptionGratuityString = "\nExemption Gratuity String : " + this.getName();
		exemptionGratuityString += "\nExemption Gratuity String(value): " + this.getValue();
		return exemptionGratuityString;
	}
	
	public static List percentageOfExemption(){
		List percentage = new ArrayList();
		
		percentage.add(new Integer("25"));
		percentage.add(new Integer("50"));
		percentage.add(new Integer("75"));
		percentage.add(new Integer("100"));
		
		return percentage;
	}
}

