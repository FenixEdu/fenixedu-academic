package net.sourceforge.fenixedu.domain;

import java.util.List;

import net.sourceforge.fenixedu.util.AgreementType;
import net.sourceforge.fenixedu.util.EntryPhase;
import net.sourceforge.fenixedu.util.StudentCurricularPlanState;
import net.sourceforge.fenixedu.util.StudentState;
import net.sourceforge.fenixedu.util.TipoCurso;
import net.sourceforge.fenixedu.commons.CollectionUtils;

import org.apache.commons.collections.Predicate;

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

    private IPerson person;

    private Integer personKey;

    private IExecutionYear registrationYear;

    private Integer keyRegistrationYear;

    private EntryPhase entryPhase;

    private Boolean payedTuition;

    private Boolean enrollmentForbidden;

    protected List studentCurricularPlans;

    private Boolean specialSeason;

    //Nuno Correia & Ricardo Rodrigues
    private Double entryGrade;

    private String contigent;

    private String ingression;

    private String istUniversity;

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
        setSpecialSeason(new Boolean(false));
    }

    public Student(Integer number, StudentState state, IPerson person, TipoCurso degreeType) {
        this();
        setNumber(number);
        setState(state);
        setPerson(person);
        setDegreeType(degreeType);

        setPersonKey(null);
        setSpecialSeason(new Boolean(false));
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
     * @return IPerson
     */
    public IPerson getPerson() {
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
    public void setPerson(IPerson person) {
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

        if (studentCurricularPlan == null) {
            studentCurricularPlan = (IStudentCurricularPlan) curricularPlans.get(0);
        }
        return studentCurricularPlan;
    }

    //Nuno Correia & Ricardo Rodrigues
    /**
     * @return Returns the contigent.
     */
    public String getContigent() {
        return contigent;
    }

    /**
     * @param contigent
     *            The contigent to set.
     */
    public void setContigent(String contigent) {
        this.contigent = contigent;
    }

    /**
     * @return Returns the entryGrade.
     */
    public Double getEntryGrade() {
        return entryGrade;
    }

    /**
     * @param entryGrade
     *            The entryGrade to set.
     */
    public void setEntryGrade(Double entryGrade) {
        this.entryGrade = entryGrade;
    }

    /**
     * @return Returns the ingression.
     */
    public String getIngression() {
        return ingression;
    }

    /**
     * @param ingression
     *            The ingression to set.
     */
    public void setIngression(String ingression) {
        this.ingression = ingression;
    }

    /**
     * @return Returns the istUniversity.
     */
    public String getIstUniversity() {
        return istUniversity;
    }

    /**
     * @param istUniversity
     *            The istUniversity to set.
     */
    public void setIstUniversity(String istUniversity) {
        this.istUniversity = istUniversity;
    }

    /**
     * @return Returns the specialSeason.
     */
    public Boolean getSpecialSeason() {
        return specialSeason;
    }

    /**
     * @param specialSeason
     *            The specialSeason to set.
     */
    public void setSpecialSeason(Boolean specialSeason) {
        this.specialSeason = specialSeason;
    }
}