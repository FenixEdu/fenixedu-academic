/*
 * @(#)ExecutionCourseView.java Created on Nov 5, 2004
 * 
 */
package DataBeans;

import java.io.Serializable;

/**
 *
 * @author Luis Cruz
 * @version 1.1, Nov 5, 2004
 * @since 1.1
 *
 */
public class ExecutionCourseView implements Serializable
{
	
	private Integer executionCourseOID;
    private String executionCourseName;
    private Integer semester;
    private Integer curricularYear;
    private Integer executionPeriodOID;

    public Integer getCurricularYear()
    {
        return curricularYear;
    }
    public void setCurricularYear(Integer curricularYear)
    {
        this.curricularYear = curricularYear;
    }
    public String getExecutionCourseName()
    {
        return executionCourseName;
    }
    public void setExecutionCourseName(String executionCourseName)
    {
        this.executionCourseName = executionCourseName;
    }
    public Integer getExecutionCourseOID()
    {
        return executionCourseOID;
    }
    public void setExecutionCourseOID(Integer executionCourseOID)
    {
        this.executionCourseOID = executionCourseOID;
    }
    public Integer getSemester()
    {
        return semester;
    }
    public void setSemester(Integer semester)
    {
        this.semester = semester;
    }
	/**
	 * @return Returns the executionPeriodOID.
	 */
	public Integer getExecutionPeriodOID() {
		return executionPeriodOID;
	}
	/**
	 * @param executionPeriodOID The executionPeriodOID to set.
	 */
	public void setExecutionPeriodOID(Integer executionPeriodOID) {
		this.executionPeriodOID = executionPeriodOID;
	}
}
