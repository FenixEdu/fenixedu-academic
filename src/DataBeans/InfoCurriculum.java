/*
 * Created on 14/Mar/2003
 *
 * 
 */
package DataBeans;

/**
 * @author João Mota
 */
public class InfoCurriculum {
	protected String generalObjectives;
	protected String operacionalObjectives;
	protected String program;
	protected String generalObjectivesEn;
	protected String operacionalObjectivesEn;
	protected String programEn;
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
		String generalObjectivesEn,
		String operacionalObjectivesEn,
		String programEn,
		InfoExecutionCourse infoExecutionCourse) {
		setInfoExecutionCourse(infoExecutionCourse);
		setGeneralObjectives(generalObjectives);
		setOperacionalObjectives(operacionalObjectives);
		setProgram(program);
		setGeneralObjectivesEn(generalObjectivesEn);
		setOperacionalObjectivesEn(operacionalObjectivesEn);
		setProgramEn(programEn);

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
			result = getInfoExecutionCourse().equals(
						infoCurriculum.getInfoExecutionCourse());
		}
		return result;
	}
	public String toString() {
		String result = "[INFOCURRICULUM";
		result += ", getGeneralObjectives=" + getGeneralObjectives();
		result += ", getOperacionalObjectives=" + getOperacionalObjectives();
		result += ", getProgram=" + getProgram();
		result += ", getGeneralObjectivesEn=" + getGeneralObjectivesEn();
		result += ", getOperacionalObjectivesEn=" + getOperacionalObjectivesEn();
		result += ", getProgramEn=" + getProgramEn();
		result += ", getInfoExecutionCourse=" + getInfoExecutionCourse();
		result += "]";
		return result;
	}
	/**
	 * @return
	 */
	public String getGeneralObjectivesEn() {
		return generalObjectivesEn;
	}

	/**
	 * @return
	 */
	public String getOperacionalObjectivesEn() {
		return operacionalObjectivesEn;
	}

	/**
	 * @return
	 */
	public String getProgramEn() {
		return programEn;
	}

	/**
	 * @param string
	 */
	public void setGeneralObjectivesEn(String string) {
		generalObjectivesEn = string;
	}

	/**
	 * @param string
	 */
	public void setOperacionalObjectivesEn(String string) {
		operacionalObjectivesEn = string;
	}

	/**
	 * @param string
	 */
	public void setProgramEn(String string) {
		programEn = string;
	}

}
