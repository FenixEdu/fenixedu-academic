package Util.enrollment;

import Util.FenixUtil;

/**
 * @author David Santos in Jun 15, 2004
 */

public class EnrollmentCondition extends FenixUtil
{
	public static final int FINAL_INT = 1;
	public static final int TEMPORARY_INT = 2;

	public static final String TEMPORARY_STR = "TMP";
	public static final String FINAL_STR = "FNL";

	public static final EnrollmentCondition FINAL = new EnrollmentCondition(EnrollmentCondition.FINAL_INT);
	public static final EnrollmentCondition TEMPORARY = new EnrollmentCondition(EnrollmentCondition.TEMPORARY_INT);

	private int type;

	private EnrollmentCondition(int type)
	{
		this.type = type;
	}

	public boolean equals(Object obj)
	{
		if (obj instanceof EnrollmentCondition)
		{
			EnrollmentCondition scopeObj = (EnrollmentCondition) obj;
			return this.type == scopeObj.getType();
		}
		return false;
	}

	/**
	 * @return
	 */
	public int getType()
	{
		return this.type;
	}
}