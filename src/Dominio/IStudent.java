/*
 * IStudent.java
 * 
 * Created on 28 of December 2002, 17:05
 */

package Dominio;

import java.util.List;

import Util.AgreementType;
import Util.EntryPhase;
import Util.StudentState;
import Util.TipoCurso;

/**
 * @author Ricardo Nortadas
 * @author João Mota
 */

public interface IStudent extends IDomainObject {

    public Integer getNumber();

    public StudentState getState();

    public IPessoa getPerson();

    public IStudentKind getStudentKind();

    public TipoCurso getDegreeType();

    public AgreementType getAgreementType();

    public void setAgreementType(AgreementType agreementType);

    public void setNumber(Integer number);

    public void setState(StudentState state);

    public void setPerson(IPessoa person);

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
}