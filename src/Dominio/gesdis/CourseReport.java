/*
 * Created on 7/Nov/2003
 * 
 * To change the template for this generated file go to Window - Preferences -
 * Java - Code Generation - Code and Comments
 */
package Dominio.gesdis;

import java.util.Date;

import Dominio.DomainObject;
import Dominio.IExecutionCourse;

/**
 * @author Leonor Almeida
 * @author Sergio Montelobo
 * 
 */
public class CourseReport extends DomainObject implements ICourseReport {

	private String report;
	private Integer keyExecutionCourse;
	private IExecutionCourse executionCourse;
    private Date lastModificationDate;

	public CourseReport() {
	}

	/** Creates a new instance of CourseReport */
	public CourseReport(Integer idInternal) {
		setIdInternal(idInternal);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Dominio.ICourseReport#getReport()
	 */
	public String getReport() {
		return report;
	}

	public Integer getKeyExecutionCourse() {
		return keyExecutionCourse;
	}

	public IExecutionCourse getExecutionCourse() {
		return executionCourse;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see Dominio.ICourseReport#setReport(java.lang.String)
	 */
	public void setReport(String report) {
		this.report = report;
	}

	public void setKeyExecutionCourse(Integer keyExecutionCourse)  {
		this.keyExecutionCourse = keyExecutionCourse;
	}
	
	public void setExecutionCourse(IExecutionCourse executionCourse) {
		this.executionCourse = executionCourse;
	}
	
	public String toString() {
		String result = "[Dominio.gesdis.CourseReport ";
		result += ", report=" + getReport();
		result += ", executionCourse=" + getExecutionCourse();
		result += "]";
		return result;
	}
    /**
     * @return Returns the lastModificationDate.
     */
    public Date getLastModificationDate()
    {
        return lastModificationDate;
    }

    /**
     * @param lastModificationDate The lastModificationDate to set.
     */
    public void setLastModificationDate(Date lastModificationDate)
    {
        this.lastModificationDate = lastModificationDate;
    }

}

