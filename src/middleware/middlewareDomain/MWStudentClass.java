package middleware.middlewareDomain;


public class MWStudentClass  
{
	private Float average;
	private String className;	
	private Integer degreeCode;
	private String studentName;
	private String studentNumber;

	/**
	 * @return
	 */
	public Float getAverage()
	{
		return this.average;
	}

	/**
	 * @return
	 */
	public String getClassName()
	{
		return this.className;
	}

	/**
	 * @return
	 */
	public Integer getDegreeCode()
	{
		return this.degreeCode;
	}

	/**
	 * @return
	 */
	public String getStudentName()
	{
		return this.studentName;
	}

	/**
	 * @return
	 */
	public String getStudentNumber()
	{
		return this.studentNumber;
	}

	/**
	 * @param average
	 */
	public void setAverage(Float average)
	{
		this.average = average;
	}

	/**
	 * @param className
	 */
	public void setClassName(String className)
	{
		this.className = className;
	}

	/**
	 * @param degreeCode
	 */
	public void setDegreeCode(Integer degreeCode)
	{
		this.degreeCode = degreeCode;
	}

	/**
	 * @param studentName
	 */
	public void setStudentName(String studentName)
	{
		this.studentName = studentName;
	}

	/**
	 * @param studentNumber
	 */
	public void setStudentNumber(String studentNumber)
	{
		this.studentNumber = studentNumber;
	}

}
