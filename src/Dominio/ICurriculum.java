/*
 * ICurriculum.java
 *
 * Created on 6 de Janeiro de 2003, 20:31
 */

package Dominio;

/**
 *
 * @author  EP 15 - fjgc
 * @author João Mota
 */
public interface ICurriculum extends IDomainObject{
	String getGeneralObjectives();
	String getOperacionalObjectives();
	String getProgram();
	String getGeneralObjectivesEn();
	String getOperacionalObjectivesEn();
	String getProgramEn();

	ICurricularCourse getCurricularCourse();

	void setGeneralObjectives(String generalObjectives);
	void setOperacionalObjectives(String operacionalObjectives);
	void setProgram(String program);
	void setGeneralObjectivesEn(String generalObjectivesEn);
	void setOperacionalObjectivesEn(String operacionalObjectivesEn);
	void setProgramEn(String programEn);
	void setCurricularCourse(ICurricularCourse CurricularCourse);

}
