package DataBeans;

import java.util.Date;
import java.util.List;

import Dominio.IEnrollment;
import Util.EnrollmentState;
import Util.EnrolmentEvaluationType;
import Util.enrollment.EnrollmentCondition;

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

    private List infoEvaluations;

    private Integer accumulatedWeight;

    private EnrollmentCondition condition;

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

    public void copyFromDomain(IEnrollment enrollment) {
        super.copyFromDomain(enrollment);
        if (enrollment != null) {
            setCreationDate(enrollment.getCreationDate());
            setEnrolmentEvaluationType(enrollment.getEnrolmentEvaluationType());
            setEnrollmentState(enrollment.getEnrollmentState());
            setAccumulatedWeight(enrollment.getAccumulatedWeight());
            setCondition(enrollment.getCondition());
        }
    }

    public static InfoEnrolment newInfoFromDomain(IEnrollment enrollment) {
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
}