package Util.enrollment;


/**
 * @author David Santos in Jun 9, 2004
 */

public final class EnrollmentRuleType 
{
	public static final int PARTIAL_TYPE = 1;
	public static final int TOTAL_TYPE = 2;
	public static final int EMPTY_TYPE = 3;

	public static final EnrollmentRuleType PARTIAL = new EnrollmentRuleType(EnrollmentRuleType.PARTIAL_TYPE);
	public static final EnrollmentRuleType TOTAL = new EnrollmentRuleType(EnrollmentRuleType.TOTAL_TYPE);
	public static final EnrollmentRuleType EMPTY = new EnrollmentRuleType(EnrollmentRuleType.EMPTY_TYPE);
	
	private Integer type;

	private EnrollmentRuleType(int type)
	{
		this.type = new Integer(type);
	}
	
	public boolean equals(Object object)
	{
		if (object instanceof EnrollmentRuleType)
		{
			EnrollmentRuleType obj = (EnrollmentRuleType) object;

			return (this.getType().intValue() == obj.getType().intValue());
		}
		
		return false;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}
}