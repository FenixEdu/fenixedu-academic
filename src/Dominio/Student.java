package Dominio;

import java.util.List;

import org.apache.commons.collections.Predicate;

import commons.CollectionUtils;

import Util.AgreementType;
import Util.EntryPhase;
import Util.StudentCurricularPlanState;
import Util.StudentState;
import Util.TipoCurso;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class Student extends DomainObject implements IStudent {

    protected Integer number;

    protected StudentState state;

    protected TipoCurso degreeType;

    private IStudentKind studentKind;
    private Integer studentKindKey;

    private AgreementType agreementType;

    private IPessoa person;
    private Integer personKey;


    private IExecutionYear registrationYear;
    private Integer keyRegistrationYear;

    private EntryPhase entryPhase;

    private Boolean payedTuition;

    private Boolean enrollmentForbidden;

    protected List studentCurricularPlans;

    public Student(Integer idInternal) {
        setIdInternal(idInternal);
    }

    public Student() {
        setNumber(null);
        setState(null);
        setPerson(null);
        setDegreeType(null);

        setPersonKey(null);
        setStudentKind(null);
        setStudentKindKey(null);
    }

    public Student(Integer number, StudentState state, IPessoa person, TipoCurso degreeType) {
        this();
        setNumber(number);
        setState(state);
        setPerson(person);
        setDegreeType(degreeType);

        setPersonKey(null);
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof IStudent) {
            IStudent student = (IStudent) obj;

            resultado = (student != null)
                    && ((this.getNumber().equals(student.getNumber()) && this.getDegreeType().equals(
                            student.getDegreeType())) || (this.getDegreeType().equals(
                            student.getDegreeType()) && this.getPerson().equals(student.getPerson())));
        }
        return resultado;
        //		return true;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "internalCode = " + getIdInternal() + "; ";
        result += "number = " + this.number + "; ";
        result += "state = " + this.state + "; ";
        result += "degreeType = " + this.degreeType + "; ";
        result += "studentKind = " + this.studentKind + "; ";
        //result += "person = " + this.person + "]";
        return result;
    }

    /**
     * Returns the degreeType.
     * 
     * @return TipoCurso
     */
    public TipoCurso getDegreeType() {
        return degreeType;
    }

    /**
     * Returns the number.
     * 
     * @return Integer
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * Returns the person.
     * 
     * @return IPessoa
     */
    public IPessoa getPerson() {
        return person;
    }

    /**
     * Returns the personKey.
     * 
     * @return Integer
     */
    public Integer getPersonKey() {
        return personKey;
    }

    /**
     * Returns the state.
     * 
     * @return StudentState
     */
    public StudentState getState() {
        return state;
    }

    /**
     * Sets the degreeType.
     * 
     * @param degreeType
     *            The degreeType to set
     */
    public void setDegreeType(TipoCurso degreeType) {
        this.degreeType = degreeType;
    }

    /**
     * Sets the number.
     * 
     * @param number
     *            The number to set
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * Sets the person.
     * 
     * @param person
     *            The person to set
     */
    public void setPerson(IPessoa person) {
        this.person = person;
    }

    /**
     * Sets the personKey.
     * 
     * @param personKey
     *            The personKey to set
     */
    public void setPersonKey(Integer personKey) {
        this.personKey = personKey;
    }

    /**
     * Sets the state.
     * 
     * @param state
     *            The state to set
     */
    public void setState(StudentState state) {
        this.state = state;
    }

    /**
     * @return
     */
    public IStudentKind getStudentKind() {
        return studentKind;
    }

    /**
     * @return
     */
    public Integer getStudentKindKey() {
        return studentKindKey;
    }

    /**
     * @param type
     */
    public void setStudentKind(IStudentKind studentKind) {
        this.studentKind = studentKind;
    }

    /**
     * @param integer
     */
    public void setStudentKindKey(Integer studentKindKey) {
        this.studentKindKey = studentKindKey;
    }

    /**
     * @return Returns the agreementType.
     */
    public AgreementType getAgreementType() {
        return agreementType;
    }

    /**
     * @param agreementType
     *            The agreementType to set.
     */
    public void setAgreementType(AgreementType agreementType) {
        this.agreementType = agreementType;
    }

    /**
     * @return Returns the entryPhase.
     */
    public EntryPhase getEntryPhase() {
        return entryPhase;
    }

    /**
     * @param entryPhase
     *            The entryPhase to set.
     */
    public void setEntryPhase(EntryPhase entryPhase) {
        this.entryPhase = entryPhase;
    }

    /**
     * @return Returns the keyRegistrationYear.
     */
    public Integer getKeyRegistrationYear() {
        return keyRegistrationYear;
    }

    /**
     * @param keyRegistrationYear
     *            The keyRegistrationYear to set.
     */
    public void setKeyRegistrationYear(Integer keyRegistrationYear) {
        this.keyRegistrationYear = keyRegistrationYear;
    }

    /**
     * @return Returns the payedTuition.
     */
    public Boolean getPayedTuition() {
        return payedTuition;
    }

    /**
     * @param payedTuition
     *            The payedTuition to set.
     */
    public void setPayedTuition(Boolean payedTuition) {
        this.payedTuition = payedTuition;
    }

    /**
     * @return Returns the registrationYear.
     */
    public IExecutionYear getRegistrationYear() {
        return registrationYear;
    }

    /**
     * @param registrationYear
     *            The registrationYear to set.
     */
    public void setRegistrationYear(IExecutionYear registrationYear) {
        this.registrationYear = registrationYear;
    }

    /**
     * @return Returns the forbiddenEnrollment.
     */
    public Boolean getEnrollmentForbidden() {
        return enrollmentForbidden;
    }

    /**
     * @param forbiddenEnrollment
     *            The forbiddenEnrollment to set.
     */
    public void setEnrollmentForbidden(Boolean forbiddenEnrollment) {
        this.enrollmentForbidden = forbiddenEnrollment;
    }

    public List getStudentCurricularPlans() {
        return studentCurricularPlans;
    }

    public void setStudentCurricularPlans(List studentCurricularPlans) {
        this.studentCurricularPlans = studentCurricularPlans;
    }

    /*
     * (non-Javadoc)
     * 
     * @see Dominio.IStudent#getActiveStudentCurricularPlan()
     */
    public IStudentCurricularPlan getActiveStudentCurricularPlan() {
        List curricularPlans = getStudentCurricularPlans();
        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) CollectionUtils.find(
                curricularPlans, new Predicate() {

                    public boolean evaluate(Object arg0) {
                        IStudentCurricularPlan studentCurricularPlan = (IStudentCurricularPlan) arg0;
                        return studentCurricularPlan.getCurrentState().getState().intValue() == StudentCurricularPlanState.ACTIVE;
                    }
                });
        
        if (studentCurricularPlan==null){
            studentCurricularPlan = (IStudentCurricularPlan) curricularPlans.get(0);
        }
        return studentCurricularPlan;
    }
}