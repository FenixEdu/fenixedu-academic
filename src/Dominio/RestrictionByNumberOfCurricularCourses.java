package Dominio;

/**
 * @author David Santos in Jan 27, 2004
 */

public abstract class RestrictionByNumberOfCurricularCourses extends Restriction implements IRestrictionByNumberOfCurricularCourses
{
	protected Integer numberOfCurricularCourses;

	/**
	 * @return Returns the numberOfCurricularCourses.
	 */
	public Integer getNumberOfCurricularCourses()
	{
		return numberOfCurricularCourses;
	}

	/**
	 * @param numberOfCurricularCourses The numberOfCurricularCourses to set.
	 */
	public void setNumberOfCurricularCourses(Integer numberOfCurricularCourses)
	{
		this.numberOfCurricularCourses = numberOfCurricularCourses;
	}

	public boolean equals(Object obj)
	{
		boolean result = super.equals(obj);
		if ((result) && (obj instanceof IRestrictionByNumberOfCurricularCourses))
		{
			IRestrictionByNumberOfCurricularCourses restrictionByNumberOfCurricularCourses =
			(IRestrictionByNumberOfCurricularCourses) obj;
			result =
			restrictionByNumberOfCurricularCourses.getNumberOfCurricularCourses().equals(this.getNumberOfCurricularCourses())
			&& this.getClass().getName().equals(restrictionByNumberOfCurricularCourses.getClass().getName());
		} else if (result)
		{
			result = false;
		}
		return result;
	}

	public String toString()
	{
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Restriction(").append(this.getClass()).append("):").append("\n\t");
		stringBuffer.append(this.getNumberOfCurricularCourses()).append("\n");
		return stringBuffer.toString();
	}
}