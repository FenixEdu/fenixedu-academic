/*
 * Created on 12/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package DataBeans.gesdis;

import java.util.List;

import DataBeans.InfoPerson;

/**
 * @author jmota
 */
public class TeacherView extends InfoPerson{

	private Integer teacherNumber;
	private List professorShipsExecutionCourses;
	private List responsibleForExecutionCourses;

	/**
	 * 
	 */
	public TeacherView() {
		
	}


	public TeacherView(Integer teacherNumber,List professorShipsExecutionCourses,List responsibleForExecutionCourses) {
			setTeacherNumber(teacherNumber);
			setProfessorShipsExecutionCourses(professorShipsExecutionCourses);
			setResponsibleForExecutionCourses(responsibleForExecutionCourses);
		}



	/**
	 * @return List
	 */
	public List getProfessorShipsExecutionCourses() {
		return professorShipsExecutionCourses;
	}

	/**
	 * @return List
	 */
	public List getResponsibleForExecutionCourses() {
		return responsibleForExecutionCourses;
	}

	/**
	 * @return Integer
	 */
	public Integer getTeacherNumber() {
		return teacherNumber;
	}

	/**
	 * Sets the professorShipsExecutionCourses.
	 * @param professorShipsExecutionCourses The professorShipsExecutionCourses to set
	 */
	public void setProfessorShipsExecutionCourses(List professorShipsExecutionCourses) {
		this.professorShipsExecutionCourses = professorShipsExecutionCourses;
	}

	/**
	 * Sets the responsibleForExecutionCourses.
	 * @param responsibleForExecutionCourses The responsibleForExecutionCourses to set
	 */
	public void setResponsibleForExecutionCourses(List responsibleForExecutionCourses) {
		this.responsibleForExecutionCourses = responsibleForExecutionCourses;
	}

	/**
	 * Sets the teacherNumber.
	 * @param teacherNumber The teacherNumber to set
	 */
	public void setTeacherNumber(Integer teacherNumber) {
		this.teacherNumber = teacherNumber;
	}

}
