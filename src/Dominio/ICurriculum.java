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
public interface ICurriculum extends IDomainObject {
	public String getGeneralObjectives();
	public String getOperacionalObjectives();
	public String getProgram();
	public String getGeneralObjectivesEn();
	public String getOperacionalObjectivesEn();
	public String getProgramEn();
	public ICurricularCourse getCurricularCourse();


	public void setGeneralObjectives(String generalObjectives);
	public void setOperacionalObjectives(String operacionalObjectives);
	public void setProgram(String program);
	public void setGeneralObjectivesEn(String generalObjectivesEn);
	public void setOperacionalObjectivesEn(String operacionalObjectivesEn);
	public void setProgramEn(String programEn);
	public void setCurricularCourse(ICurricularCourse CurricularCourse);
	
}
