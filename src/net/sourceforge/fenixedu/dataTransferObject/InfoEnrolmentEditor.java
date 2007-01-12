package net.sourceforge.fenixedu.dataTransferObject;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.sourceforge.fenixedu.domain.Enrolment;
import net.sourceforge.fenixedu.domain.EnrolmentEvaluation;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;

public class InfoEnrolmentEditor extends InfoObject {

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

    // this variable is used for in the interface for changing a students
        // degree
    // check
        // net.sourceforge.fenixedu.presentationTier.backBeans.degreeAdministrativeOffice.ChangeDegree.EnrolementOperation
    // for possible valies
    private String changeDegreeOperation;

    public InfoEnrolmentEditor() {
    }

    public InfoEnrolmentEditor(InfoStudentCurricularPlan infoStudentCurricularPlan,
	    InfoCurricularCourse infoCurricularCourse, EnrollmentState state,
	    InfoExecutionPeriod infoExecutionPeriod) {
	this();
	setInfoCurricularCourse(infoCurricularCourse);
	setInfoStudentCurricularPlan(infoStudentCurricularPlan);
	setEnrollmentState(state);
	setInfoExecutionPeriod(infoExecutionPeriod);
    }

    public Integer getAccumulatedWeight() {
	return accumulatedWeight;
    }

    public void setAccumulatedWeight(Integer accumulatedWeight) {
	this.accumulatedWeight = accumulatedWeight;
    }

    public String getEnrollmentTypeResourceKey() {
	return enrollmentTypeResourceKey;
    }

    public void setEnrollmentTypeResourceKey(String enrolmentTypeResourceKey) {
	this.enrollmentTypeResourceKey = enrolmentTypeResourceKey;
    }

    public InfoCurricularCourse getInfoCurricularCourse() {
	return infoCurricularCourse;
    }

    public InfoExecutionPeriod getInfoExecutionPeriod() {
	return infoExecutionPeriod;
    }

    public InfoStudentCurricularPlan getInfoStudentCurricularPlan() {
	return infoStudentCurricularPlan;
    }

    public EnrollmentState getEnrollmentState() {
	return enrollmentState;
    }

    public void setInfoCurricularCourse(InfoCurricularCourse infoCurricularCourse) {
	this.infoCurricularCourse = infoCurricularCourse;
    }

    public void setInfoExecutionPeriod(InfoExecutionPeriod infoExecutionPeriod) {
	this.infoExecutionPeriod = infoExecutionPeriod;
    }

    public void setInfoStudentCurricularPlan(InfoStudentCurricularPlan infoStudentCurricularPlan) {
	this.infoStudentCurricularPlan = infoStudentCurricularPlan;
    }

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

    public InfoEnrolmentEvaluation getInfoEnrolmentEvaluation() {
	return infoEnrolmentEvaluation;
    }

    public void setInfoEnrolmentEvaluation(InfoEnrolmentEvaluation evaluation) {
	infoEnrolmentEvaluation = evaluation;
    }

    public Date getCreationDate() {
	return creationDate;
    }

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
	    setCondition(enrollment.getEnrolmentCondition());
	    setInfoCurricularCourse(InfoCurricularCourse.newInfoFromDomain(enrollment
		    .getCurricularCourse()));
	    setInfoExecutionPeriod(InfoExecutionPeriod
		    .newInfoFromDomain(enrollment.getExecutionPeriod()));
	    setInfoStudentCurricularPlan(InfoStudentCurricularPlan.newInfoFromDomain(enrollment
		    .getStudentCurricularPlan()));

	    setEnrollmentTypeResourceKey(enrollment.getEnrolmentTypeName());
	}
    }

    public static InfoEnrolmentEditor newInfoFromDomain(Enrolment enrollment) {
	InfoEnrolmentEditor infoEnrolment = null;
	if (enrollment != null) {
	    infoEnrolment = new InfoEnrolmentEditor();
	    infoEnrolment.copyFromDomain(enrollment);

	}
	return infoEnrolment;
    }

    public EnrollmentCondition getCondition() {
	return condition;
    }

    public void setCondition(EnrollmentCondition condition) {
	this.condition = condition;
    }

    public InfoEnrolmentEvaluation getInfoImprovmentEnrolmentEvaluation() {
	return infoImprovmentEnrolmentEvaluation;
    }

    public void setInfoImprovmentEnrolmentEvaluation(
	    InfoEnrolmentEvaluation infoImprovmentEnrolmentEvaluation) {
	this.infoImprovmentEnrolmentEvaluation = infoImprovmentEnrolmentEvaluation;
    }

    public InfoEnrolmentEvaluation getInfoNormalEnrolmentEvaluation() {
	return infoNormalEnrolmentEvaluation;
    }

    public void setInfoNormalEnrolmentEvaluation(InfoEnrolmentEvaluation infoNormalEnrolmentEvaluation) {
	this.infoNormalEnrolmentEvaluation = infoNormalEnrolmentEvaluation;
    }

    public InfoEnrolmentEvaluation getInfoSpecialSeasonEnrolmentEvaluation() {
	return infoSpecialSeasonEnrolmentEvaluation;
    }

    public void setInfoSpecialSeasonEnrolmentEvaluation(
	    InfoEnrolmentEvaluation infoSpecialSeasonEnrolmentEvaluation) {
	this.infoSpecialSeasonEnrolmentEvaluation = infoSpecialSeasonEnrolmentEvaluation;
    }

    public InfoEnrolmentEvaluation getInfoEquivalenceEnrolmentEvaluation() {
	return infoEquivalenceEnrolmentEvaluation;
    }

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
	final Enrolment enrolment = (Enrolment) RootDomainObject.getInstance()
		.readCurriculumModuleByOID(getIdInternal());
	final EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) Collections.max(enrolment
		.getEvaluationsSet());
	return enrolmentEvaluation == null ? null : enrolmentEvaluation.getGrade();
    }

    public boolean equals(Object obj) {
	boolean resultado = false;
	if (obj instanceof InfoEnrolmentEditor) {
	    InfoEnrolmentEditor enrolment = (InfoEnrolmentEditor) obj;
	    // these kind of tests are necessary for the new cloner
                // philosophy
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
}
