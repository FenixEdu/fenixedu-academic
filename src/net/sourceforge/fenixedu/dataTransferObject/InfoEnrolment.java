package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.EnrolmentInExtraCurricularCourse;
import net.sourceforge.fenixedu.domain.EnrolmentInOptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;

/**
 * @author dcs-rjao
 * 
 * 22/Abr/2003
 */
public class InfoEnrolment extends InfoObject {
    private InfoStudentCurricularPlan infoStudentCurricularPlan;

    private InfoCurricularCourse infoCurricularCourse;

    private InfoExecutionPeriod infoExecutionPeriod;

    private EnrollmentState enrollmentState;

    private EnrolmentEvaluationType enrolmentEvaluationType;

    private Date creationDate;

    // to be used to keep the actual enrolment evaluation
    private InfoEnrolmentEvaluation infoEnrolmentEvaluation;

    // used in the curriculum marks list
    private InfoEnrolmentEvaluation infoNormalEnrolmentEvaluation;

    private InfoEnrolmentEvaluation infoImprovmentEnrolmentEvaluation;

    private InfoEnrolmentEvaluation infoSpecialSeasonEnrolmentEvaluation;

    private InfoEnrolmentEvaluation infoEquivalenceEnrolmentEvaluation;

    private List infoEvaluations;

    private Integer accumulatedWeight;

    private EnrollmentCondition condition;

    private String enrollmentTypeResourceKey;

    // this variable is used for in the interface for changing a students degree
    // check net.sourceforge.fenixedu.presentationTier.backBeans.degreeAdministrativeOffice.ChangeDegree.EnrolementOperation
    // for possible valies
    private String changeDegreeOperation;

    public InfoEnrolment() {
    }

    public InfoEnrolment(InfoStudentCurricularPlan infoStudentCurricularPlan,
            InfoCurricularCourse infoCurricularCourse, EnrollmentState state,
            InfoExecutionPeriod infoExecutionPeriod) {
        this();
        setInfoCurricularCourse(infoCurricularCourse);
        setInfoStudentCurricularPlan(infoStudentCurricularPlan);
        setEnrollmentState(state);
        setInfoExecutionPeriod(infoExecutionPeriod);
    }

    /**
     * @return Returns the accumulatedWeight.
     */
    public Integer getAccumulatedWeight() {
        return accumulatedWeight;
    }

    /**
     * @param accumulatedWeight
     *            The accumulatedWeight to set.
     */
    public void setAccumulatedWeight(Integer accumulatedWeight) {
        this.accumulatedWeight = accumulatedWeight;
    }

    public String getEnrollmentTypeResourceKey() {
        return enrollmentTypeResourceKey;
    }

    public void setEnrollmentTypeResourceKey(String enrolmentTypeResourceKey) {
        this.enrollmentTypeResourceKey = enrolmentTypeResourceKey;
    }

    public boolean equals(Object obj) {
        boolean resultado = false;
        if (obj instanceof InfoEnrolment) {
            InfoEnrolment enrolment = (InfoEnrolment) obj;
            // these kind of tests are necessary for the new cloner philosophy
            resultado = ((this.getInfoStudentCurricularPlan() == null && enrolment
                    .getInfoStudentCurricularPlan() == null) || (this.getInfoStudentCurricularPlan() != null
                    && enrolment.getInfoStudentCurricularPlan() != null && this
                    .getInfoStudentCurricularPlan().equals(enrolment.getInfoStudentCurricularPlan())))
                    && ((this.getInfoCurricularCourse() == null && enrolment.getInfoCurricularCourse() == null) || (this
                            .getInfoCurricularCourse() != null
                            && enrolment.getInfoCurricularCourse() != null && this
                            .getInfoCurricularCourse().equals(enrolment.getInfoCurricularCourse())))
                    && ((this.getInfoExecutionPeriod() == null && enrolment.getInfoExecutionPeriod() == null) || (this
                            .getInfoExecutionPeriod() != null
                            && enrolment.getInfoExecutionPeriod() != null && this
                            .getInfoExecutionPeriod().equals(enrolment.getInfoExecutionPeriod())));
        }
        return resultado;
    }

    public String toString() {
        String result = "[" + this.getClass().getName() + "; ";
        result += "infoStudentCurricularPlan = " + this.infoStudentCurricularPlan + "; ";
        result += "infoExecutionPeriod = " + this.infoExecutionPeriod + "; ";
        result += "state = " + this.enrollmentState + "; ";
        result += "infoCurricularCourse = " + this.infoCurricularCourse + "; ";
        result += "enrolmentEvaluationType = " + this.enrolmentEvaluationType + "; ";
        result += "infoEvaluations = " + this.infoEvaluations + "]\n";
        return result;
    }

    public InfoCurricularCourse getInfoCurricularCourse() {
        return infoCurricularCourse;
    }

    /**
     * @return InfoExecutionPeriod
     */
    public InfoExecutionPeriod getInfoExecutionPeriod() {
        return infoExecutionPeriod;
    }

    /**
     * @return InfoStudentCurricularPlan
     */
    public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
        return infoStudentCurricularPlan;
    }

    /**
     * @return EnrolmentState
     */
    public EnrollmentState getEnrollmentState() {
        return enrollmentState;
    }

    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
        this.infoCurricularCourse = infoCurricularCourse;
    }

    /**
     * Sets the infoExecutionPeriod.
     * 
     * @param infoExecutionPeriod
     *            The infoExecutionPeriod to set
     */
    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
        this.infoExecutionPeriod = infoExecutionPeriod;
    }

    /**
     * Sets the infoStudentCurricularPlan.
     * 
     * @param infoStudentCurricularPlan
     *            The infoStudentCurricularPlan to set
     */
    public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan infoStudentCurricularPlan) {
        this.infoStudentCurricularPlan = infoStudentCurricularPlan;
    }

    /**
     * Sets the state.
     * 
     * @param state
     *            The state to set
     */
    public void setEnrollmentState(EnrollmentState state) {
        this.enrollmentState = state;
    }

    public EnrolmentEvaluationType getEnrolmentEvaluationType() {
        return this.enrolmentEvaluationType;
    }

    public void setEnrolmentEvaluationType(EnrolmentEvaluationType type) {
        this.enrolmentEvaluationType = type;
    }

    public List getInfoEvaluations() {
        return infoEvaluations;
    }

    public void setInfoEvaluations(List list) {
        infoEvaluations = list;
    }

    /**
     * @return InfoEnrolmentEvaluation
     */
    public InfoEnrolmentEvaluation getInfoEnrolmentEvaluation() {
        return infoEnrolmentEvaluation;
    }

    /**
     * @param evaluation
     */
    public void setInfoEnrolmentEvaluation(InfoEnrolmentEvaluation evaluation) {
        infoEnrolmentEvaluation = evaluation;
    }

    /**
     * @return Returns the creationDate.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @param creationDate
     *            The creationDate to set.
     */
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void copyFromDomain(Enrolment enrollment) {
        super.copyFromDomain(enrollment);
        if (enrollment != null) {
            setCreationDate(enrollment.getCreationDate());
            setEnrolmentEvaluationType(enrollment.getEnrolmentEvaluationType());
            setEnrollmentState(enrollment.getEnrollmentState());
            setAccumulatedWeight(enrollment.getAccumulatedWeight());
            setCondition(enrollment.getCondition());
            setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(enrollment
                    .getCurricularCourse()));
            setInfoExecutionPeriod(InfoExecutionPeriod
                    .newInfoFromDomain(enrollment.getExecutionPeriod()));
            setInfoStudentCurricularPlan(InfoStudentCurricularPlan.newInfoFromDomain(enrollment
                    .getStudentCurricularPlan()));

            if (enrollment instanceof EnrolmentInExtraCurricularCourse) {
                setEnrollmentTypeResourceKey("option.curricularCourse.extra");
            } else if (enrollment instanceof EnrolmentInOptionalCurricularCourse) {
                setEnrollmentTypeResourceKey("option.curricularCourse.optional");
            } else {
                setEnrollmentTypeResourceKey(enrollment.getCurricularCourse().getType().getKeyName());
            }

        }
    }

    public static InfoEnrolment newInfoFromDomain(Enrolment enrollment) {
        InfoEnrolment infoEnrolment = null;
        if (enrollment != null) {
            infoEnrolment = new InfoEnrolment();
            infoEnrolment.copyFromDomain(enrollment);

        }
        return infoEnrolment;
    }

    /**
     * @return Returns the condition.
     */
    public EnrollmentCondition getCondition() {
        return condition;
    }

    /**
     * @param condition
     *            The condition to set.
     */
    public void setCondition(EnrollmentCondition condition) {
        this.condition = condition;
    }

    /**
     * @return Returns the infoImprovmentEnrolmentEvaluation.
     */
    public InfoEnrolmentEvaluation getInfoImprovmentEnrolmentEvaluation() {
        return infoImprovmentEnrolmentEvaluation;
    }

    /**
     * @param infoImprovmentEnrolmentEvaluation
     *            The infoImprovmentEnrolmentEvaluation to set.
     */
    public void setInfoImprovmentEnrolmentEvaluation(
            InfoEnrolmentEvaluation infoImprovmentEnrolmentEvaluation) {
        this.infoImprovmentEnrolmentEvaluation = infoImprovmentEnrolmentEvaluation;
    }

    /**
     * @return Returns the infoNormalEnrolmentEvaluation.
     */
    public InfoEnrolmentEvaluation getInfoNormalEnrolmentEvaluation() {
        return infoNormalEnrolmentEvaluation;
    }

    /**
     * @param infoNormalEnrolmentEvaluation
     *            The infoNormalEnrolmentEvaluation to set.
     */
    public void setInfoNormalEnrolmentEvaluation(InfoEnrolmentEvaluation infoNormalEnrolmentEvaluation) {
        this.infoNormalEnrolmentEvaluation = infoNormalEnrolmentEvaluation;
    }

    /**
     * @return Returns the infoSpecialSeasonEnrolmentEvaluation.
     */
    public InfoEnrolmentEvaluation getInfoSpecialSeasonEnrolmentEvaluation() {
        return infoSpecialSeasonEnrolmentEvaluation;
    }

    /**
     * @param infoSpecialSeasonEnrolmentEvaluation
     *            The infoSpecialSeasonEnrolmentEvaluation to set.
     */
    public void setInfoSpecialSeasonEnrolmentEvaluation(
            InfoEnrolmentEvaluation infoSpecialSeasonEnrolmentEvaluation) {
        this.infoSpecialSeasonEnrolmentEvaluation = infoSpecialSeasonEnrolmentEvaluation;
    }

    /**
     * @return Returns the infoEquivalenceEnrolmentEvaluation.
     */
    public InfoEnrolmentEvaluation getInfoEquivalenceEnrolmentEvaluation() {
        return infoEquivalenceEnrolmentEvaluation;
    }

    /**
     * @param infoEquivalenceEnrolmentEvaluation
     *            The infoEquivalenceEnrolmentEvaluation to set.
     */
    public void setInfoEquivalenceEnrolmentEvaluation(
            InfoEnrolmentEvaluation infoEquivalenceEnrolmentEvaluation) {
        this.infoEquivalenceEnrolmentEvaluation = infoEquivalenceEnrolmentEvaluation;
    }

    public String getChangeDegreeOperation() {
        return changeDegreeOperation;
    }

    public void setChangeDegreeOperation(String changeDegreeOperation) {
        System.out.println("Setting enrolement operation.");
        this.changeDegreeOperation = changeDegreeOperation;
    }

    public String getGrade() {
        final Enrolment enrolment = (Enrolment) RootDomainObject.getInstance().readCurriculumModuleByOID(getIdInternal());
        EnrolmentEvaluation enrolmentEvaluation = null;
        if(!enrolment.getEvaluationsSet().isEmpty()) {
            enrolmentEvaluation = (EnrolmentEvaluation) Collections.max(enrolment.getEvaluationsSet());
        }
        return enrolmentEvaluation == null ? null : enrolmentEvaluation.getGrade();
    }

}
