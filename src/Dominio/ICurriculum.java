/*
 * ICurriculum.java
 *
 * Created on 6 de Janeiro de 2003, 20:31
 */

package Dominio;

import java.util.Date;

/**
 * 
 * @author EP 15 - fjgc
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

    public IPessoa getPersonWhoAltered();

    public Date getLastModificationDate();

    public void setGeneralObjectives(String generalObjectives);

    public void setOperacionalObjectives(String operacionalObjectives);

    public void setProgram(String program);

    public void setGeneralObjectivesEn(String generalObjectivesEn);

    public void setOperacionalObjectivesEn(String operacionalObjectivesEn);

    public void setProgramEn(String programEn);

    public void setCurricularCourse(ICurricularCourse CurricularCourse);

    public void setPersonWhoAltered(IPessoa personWhoAltered);

    public void setLastModificationDate(Date lastModificationDate);

}