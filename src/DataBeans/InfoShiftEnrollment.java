/*
 * Created on 10/Fev/2004
 *  
 */
package DataBeans;

import java.util.List;
import java.util.Map;

/**
 * This class keeps all information usefull for shift enrollment use case
 * @author Tânia Pousão
 */
public class InfoShiftEnrollment
{
	
	
	/**
	 * Execution courses that student attends
	 */
	private List infoAttendingCourses;
	
	/**
	 * execution courses that belongs to execution degree selected
	 */
	private List infoExecutionCoursesList;
	
	/**
	 * execution degree selected
	 */
	private InfoExecutionDegree infoExecutionDegree;
	
	/**
	 * TODO: explain this.
	 */
	private List infoExecutionDegreesLabelsList;
	
	/**
	 * executionDegrees present.
	 */
	private List infoExecutionDegreesList;
    
	/**
	 * Student enrolling
	 */
	private InfoStudent infoStudent;
	

	/**
	 * @return Returns the infoAttendingCourses.
	 */
	public List getInfoAttendingCourses()
	{
		return infoAttendingCourses;
	}

	/**
	 * @return Returns the infoExecutionCoursesList.
	 */
	public List getInfoExecutionCoursesList()
	{
		return infoExecutionCoursesList;
	}

	/**
	 * @return Returns the infoExecutionDegree.
	 */
	public InfoExecutionDegree getInfoExecutionDegree()
	{
		return infoExecutionDegree;
	}

	/**
	 * @return Returns the infoExecutionDegreesLabelsList.
	 */
	public List getInfoExecutionDegreesLabelsList()
	{
		return infoExecutionDegreesLabelsList;
	}

	/**
	 * @return Returns the infoExecutionDegreesList.
	 */
	public List getInfoExecutionDegreesList()
	{
		return infoExecutionDegreesList;
	}

	/**
	 * @return Returns the infoStudent.
	 */
	public InfoStudent getInfoStudent()
	{
		return infoStudent;
	}

	/**
	 * @param infoAttendingCourses
	 *            The infoAttendingCourses to set.
	 */
	public void setInfoAttendingCourses(List infoAttendingCourses)
	{
		this.infoAttendingCourses = infoAttendingCourses;
	}

	/**
	 * @param infoExecutionCoursesList
	 *            The infoExecutionCoursesList to set.
	 */
	public void setInfoExecutionCoursesList(List infoExecutionCoursesList)
	{
		this.infoExecutionCoursesList = infoExecutionCoursesList;
	}

	/**
	 * @param infoExecutionDegree
	 *            The infoExecutionDegree to set.
	 */
	public void setInfoExecutionDegree(InfoExecutionDegree infoExecutionDegree)
	{
		this.infoExecutionDegree = infoExecutionDegree;
	}

	/**
	 * @param infoExecutionDegreesLabelsList
	 *            The infoExecutionDegreesLabelsList to set.
	 */
	public void setInfoExecutionDegreesLabelsList(List infoExecutionDegreesLabelsList)
	{
		this.infoExecutionDegreesLabelsList = infoExecutionDegreesLabelsList;
	}

	/**
	 * @param infoExecutionDegreesList
	 *            The infoExecutionDegreesList to set.
	 */
	public void setInfoExecutionDegreesList(List infoExecutionDegreesList)
	{
		this.infoExecutionDegreesList = infoExecutionDegreesList;
	}

	/**
	 * @param infoStudent
	 *            The infoStudent to set.
	 */
	public void setInfoStudent(InfoStudent infoStudent)
	{
		this.infoStudent = infoStudent;
	}

	public String toString()
	{
		StringBuffer string = new StringBuffer();

		string.append("[InfoShiftEnrollment ");
		string.append("\ninfoStudent: ");
		string.append(infoStudent.getNumber());
		if (infoExecutionDegreesList != null)
		{
			string.append("\ninfoExecutionDegreesList: ");
			string.append(infoExecutionDegreesList.size());
		}
		if (infoExecutionDegreesLabelsList != null)
		{
			string.append("\ninfoExecutionDegreesLabelsList: ");
			string.append(infoExecutionDegreesLabelsList.size());
		}
		string.append("\ninfoExecutionDegree: ");
		string.append(infoExecutionDegree.getIdInternal());
		if (infoExecutionCoursesList != null)
		{
			string.append("\ninfoExecutionCoursesList: ");
			string.append(infoExecutionCoursesList.size());
		}
		if (infoAttendingCourses != null)
		{
			string.append("\ninfoAttendingCourses: ");
			string.append(infoAttendingCourses.size());
		}
		string.append("]");

		return string.toString();
	}

}
