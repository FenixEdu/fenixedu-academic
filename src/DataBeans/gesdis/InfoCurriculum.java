/*
 * Created on 14/Mar/2003
 *
 * To change this generated comment go to 
 * Window>Preferences>Java>Code Generation>Code Template
 */
package DataBeans.gesdis;

import DataBeans.InfoExecutionCourse;

/**
 * @author jmota
 */
public class InfoCurriculum {
	protected String generalObjectives;
	protected String operacionalObjectives;
	protected String program;
	protected InfoExecutionCourse infoExecutionCourse;
	/**
	 * 
	 */
	public InfoCurriculum() {

	}

	public InfoCurriculum(InfoExecutionCourse infoExecutionCourse) {
		setInfoExecutionCourse(infoExecutionCourse);

	}

	public InfoCurriculum(
		String generalObjectives,
		String operacionalObjectives,
		String program,
		InfoExecutionCourse infoExecutionCourse) {
		setInfoExecutionCourse(infoExecutionCourse);
		setGeneralObjectives(generalObjectives);
		setOperacionalObjectives(operacionalObjectives);
		setProgram(program);

	}

	/**
	 * @return String
	 */
	public String getGeneralObjectives() {
		return generalObjectives;
	}

	/**
	 * @return InfoExecutionCourse
	 */
	public InfoExecutionCourse getInfoExecutionCourse() {
		return infoExecutionCourse;
	}

	/**
	 * @return String
	 */
	public String getOperacionalObjectives() {
		return operacionalObjectives;
	}

	/**
	 * @return String
	 */
	public String getProgram() {
		return program;
	}

	/**
	 * Sets the generalObjectives.
	 * @param generalObjectives The generalObjectives to set
	 */
	public void setGeneralObjectives(String generalObjectives) {
		this.generalObjectives = generalObjectives;
	}

	/**
	 * Sets the infoExecutionCourse.
	 * @param infoExecutionCourse The infoExecutionCourse to set
	 */
	public void setInfoExecutionCourse(InfoExecutionCourse infoExecutionCourse) {
		this.infoExecutionCourse = infoExecutionCourse;
	}

	/**
	 * Sets the operacionalObjectives.
	 * @param operacionalObjectives The operacionalObjectives to set
	 */
	public void setOperacionalObjectives(String operacionalObjectives) {
		this.operacionalObjectives = operacionalObjectives;
	}

	/**
	 * Sets the program.
	 * @param program The program to set
	 */
	public void setProgram(String program) {
		this.program = program;
	}

	public boolean equals(Object obj) {
		boolean result = false;
		if (obj instanceof InfoCurriculum) {
			InfoCurriculum infoCurriculum = (InfoCurriculum) obj;
			result =
				getOperacionalObjectives().equals(
					infoCurriculum.getOperacionalObjectives())
					&& getGeneralObjectives().equals(
						infoCurriculum.getGeneralObjectives())
					&& getInfoExecutionCourse().equals(
						infoCurriculum.getInfoExecutionCourse())
					&& getProgram().equals(infoCurriculum.getProgram());
		}
		return result;
	}
	public String toString() {
			String result = "[INFOCURRICULUM";
			result += ", getGeneralObjectives=" + getGeneralObjectives();
			result += ", getOperacionalObjectives=" + getOperacionalObjectives();
			result += ", getProgram=" + getProgram();
			result += ", getInfoExecutionCourse=" + getInfoExecutionCourse();
			result += "]";
			return result;
		}
}
