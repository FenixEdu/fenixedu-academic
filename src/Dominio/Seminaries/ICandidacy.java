/*
 * Created on 29/Jul/2003, 11:47:10
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package Dominio.Seminaries;

import java.util.List;

import Dominio.ICurricularCourse;
import Dominio.IDomainObject;
import Dominio.IStudent;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at 29/Jul/2003, 11:47:10
 *  
 */
public interface ICandidacy extends IDomainObject {
    /**
     * @return
     */
    public abstract List getCaseStudyChoices();

    /**
     * @param list
     */
    public abstract void setCaseStudyChoices(List list);

    public abstract String getMotivation();

    public abstract void setMotivation(String motivation);

    public abstract Boolean getApproved();

    public abstract void setApproved(Boolean approved);

    public ISeminary getSeminary();

    public void setSeminary(ISeminary seminary);

    public IStudent getStudent();

    public void setStudent(IStudent student);

    public IModality getModality();

    public void setModality(IModality modality);

    public ICurricularCourse getCurricularCourse();

    public void setCurricularCourse(ICurricularCourse curricularCourse);

    public ITheme getTheme();

    public void setTheme(ITheme theme);

}