/*
 * Created on Jul 23, 2003
 *
 *By Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 */
package Dominio.Seminaries;

import java.util.Calendar;
import java.util.List;

import Dominio.IDomainObject;

/**
 * @author Goncalo Luiz gedl [AT] rnl [DOT] ist [DOT] utl [DOT] pt
 * 
 * 
 * Created at Jul 23, 2003, 9:49:19 AM
 *  
 */
public interface ISeminary extends IDomainObject {
    /**
     * @return
     */
    public abstract String getDescription();

    /**
     * @return
     */
    public abstract String getName();

    /**
     * @return
     */
    public abstract List getEquivalencies();

    /**
     * @param string
     */
    public abstract void setDescription(String string);

    /**
     * @param string
     */
    public abstract void setName(String string);

    /**
     * @param list
     */
    public abstract void setEquivalencies(List list);

    public abstract Integer getAllowedCandidaciesPerStudent();

    public void setAllowedCandidaciesPerStudent(Integer integer);

    public void setEnrollmentBeginTime(Calendar calendar);

    public void setEnrollmentBeginDate(Calendar calendar);

    public void setEnrollmentEndTime(Calendar calendar);

    public void setEnrollmentEndDate(Calendar calendar);

    public Calendar getEnrollmentBeginTime();

    public Calendar getEnrollmentBeginDate();

    public Calendar getEnrollmentEndTime();

    public Calendar getEnrollmentEndDate();

    public Boolean getHasCaseStudy();

    public Boolean getHasTheme();

    public void setHasCaseStudy(Boolean hasCaseStudy);

    public void setHasTheme(Boolean hasTheme);

    public List getCandidacies();

    public void setCandidacies(List candidacies);
}