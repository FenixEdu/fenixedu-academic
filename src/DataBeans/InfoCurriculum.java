/*
 * Created on 14/Mar/2003
 *
 * 
 */
package DataBeans;

import java.util.Date;


/**
 * @author João Mota
 */
public class InfoCurriculum extends InfoObject implements ISiteComponent{
	protected String generalObjectives;
	protected String operacionalObjectives;
	protected String program;
	protected String programEn;
	protected String generalObjectivesEn;
	protected String operacionalObjectivesEn;
	
	protected Date lastModificationDate;
	
	protected InfoCurricularCourse infoCurricularCourse;
	
	protected InfoPerson infoPersonWhoAltered;
	
	public InfoCurriculum() {

	}

	public InfoCurriculum(InfoCurricularCourse infoCurricularCourse) {
		setInfoCurricularCourse(infoCurricularCourse);

	}

	public InfoCurriculum(
		String generalObjectives,
		String operacionalObjectives,
		String program,
		String generalObjectivesEn,
		String operacionalObjectivesEn,
		String programEn,
		InfoCurricularCourse infoCurricularCourse) {
		setInfoCurricularCourse(infoCurricularCourse);
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
	public InfoCurricularCourse getInfoCurricularCourse() {
		return infoCurricularCourse;
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
	 * Sets the infoCurricularCourse.
	 * @param infoCurricularCourse The infoCurricularCourse to set
	 */
	public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
		this.infoCurricularCourse = infoCurricularCourse;
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
			result = getInfoCurricularCourse().equals(
						infoCurriculum.getInfoCurricularCourse());
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
		result += ", InfoCurricularCourse =" + getInfoCurricularCourse();
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

	public Date getLastModificationDate() {
		return lastModificationDate;
	}

	public void setLastModificationDate(Date lastModificationDate) {
		this.lastModificationDate = lastModificationDate;
	}

	/**
	 * @return Returns the infoPersonWhoAltered.
	 */
	public InfoPerson getInfoPersonWhoAltered()
	{
		return infoPersonWhoAltered;
	}

	/**
	 * @param infoPersonWhoAltered The infoPersonWhoAltered to set.
	 */
	public void setInfoPersonWhoAltered(InfoPerson infoPersonWhoAltered)
	{
		this.infoPersonWhoAltered = infoPersonWhoAltered;
	}

}
