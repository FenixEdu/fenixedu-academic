/*
 * ITeacher.java
 */
package Dominio;

import java.util.List;

import DataBeans.credits.InfoCredits;
import Dominio.credits.event.CreditsEvent;
import Dominio.credits.event.ICreditsEventOriginator;
import Dominio.teacher.ICategory;

/**
 * 
 * @author EP15
 * @author Ivo Brandão
 */
public interface ITeacher extends IDomainObject {
    public ICategory getCategory();

    public List getDegreeFinalProjectStudents();

    public List getInstitutionWorkTimePeriods();

    public IPessoa getPerson();

    public List getProfessorships();

    public Integer getTeacherNumber();

    /**
     * @return
     */
    public List getTeacherPublications();

    public void setCategory(ICategory category);

    public void setDegreeFinalProjectStudents(List degreeFinalProjectStudents);

    public void setInstitutionWorkTimePeriods(List institutionWorkTimePeriods);

    public void setPerson(IPessoa person);

    public void setProfessorships(List professorships);

    public void setTeacherNumber(Integer number);

    public void setTeacherPublications(List teacherPublications);

    public List getManagementPositions();

    public List getServiceExemptionSituations();

    public List getOtherTypeCreditLines();

    public void setManagementPositions(List managementPositions);

    public void setServiceExemptionSituations(List serviceExemptionSituations);

    public void setOtherTypeCreditLines(List otherTypeCreditLines);

    /**
     * This method returns the credits info for an execution period. It should
     * cache execution period info.
     * 
     * @param executionPeriod
     * @return
     */
    public InfoCredits getExecutionPeriodCredits(IExecutionPeriod executionPeriod);

    public void notifyCreditsChange(CreditsEvent creditsEvent,
            ICreditsEventOriginator creditsEventOriginator);
}