/*
 * IStudent.java
 * 
 * Created on 28 of December 2002, 17:05
 */

package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.util.AgreementType;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.StudentState;
import net.sourceforge.fenixedu.util.TipoCurso;

/**
 * @author Ricardo Nortadas
 * @author João Mota
 */

public interface IStudent extends IDomainObject {

    public Integer getNumber();

    public StudentState getState();

    public IPerson getPerson();

    public IStudentKind getStudentKind();

    public TipoCurso getDegreeType();

    public AgreementType getAgreementType();

    public void setAgreementType(AgreementType agreementType);

    public void setNumber(Integer number);

    public void setState(StudentState state);

    public void setPerson(IPerson person);

    public void setDegreeType(TipoCurso degreeType);

    public void setStudentKind(IStudentKind studentKind);

    /**
     * @return Returns the entryPhase.
     */
    public EntryPhase getEntryPhase();

    /**
     * @param entryPhase
     *            The entryPhase to set.
     */
    public void setEntryPhase(EntryPhase entryPhase);

    /**
     * @return Returns the payedTuition.
     */
    public Boolean getPayedTuition();

    /**
     * @param payedTuition
     *            The payedTuition to set.
     */
    public void setPayedTuition(Boolean payedTuition);

    /**
     * @return Returns the specialSeason.
     */
    public Boolean getSpecialSeason();

    /**
     * @param specialSeason
     *            The specialSeason to set.
     */
    public void setSpecialSeason(Boolean specialSeason);

    /**
     * @return Returns the registrationYear.
     */
    public IExecutionYear getRegistrationYear();

    /**
     * @param registrationYear
     *            The registrationYear to set.
     */
    public void setRegistrationYear(IExecutionYear registrationYear);

    /**
     * @return Returns the forbiddenEnrollment.
     */
    public Boolean getEnrollmentForbidden();

    /**
     * @param forbiddenEnrollment
     *            The forbiddenEnrollment to set.
     */
    public void setEnrollmentForbidden(Boolean forbiddenEnrollment);

    public List getStudentCurricularPlans();

    public void setStudentCurricularPlans(List studentCurricularPlans);

    public IStudentCurricularPlan getActiveStudentCurricularPlan();

    //Nuno Correia & Ricardo Rodrigues
    public String getContigent();

    public void setContigent(String contigent);

    public Double getEntryGrade();

    public void setEntryGrade(Double entryGrade);

    public String getIngression();

    public void setIngression(String ingression);

    public String getIstUniversity();

    public void setIstUniversity(String istUniversity);
}