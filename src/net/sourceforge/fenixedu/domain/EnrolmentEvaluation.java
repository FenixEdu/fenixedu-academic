package net.sourceforge.fenixedu.domain;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;

import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.curriculum.GradeFactory;
import net.sourceforge.fenixedu.domain.curriculum.IGrade;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.EnrolmentNotPayedException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationState;
import net.sourceforge.fenixedu.domain.student.registrationStates.RegistrationStateType;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;
import net.sourceforge.fenixedu.util.FenixDigestUtils;
import net.sourceforge.fenixedu.util.MarkType;

import org.apache.commons.beanutils.BeanComparator;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

public class EnrolmentEvaluation extends EnrolmentEvaluation_Base implements Comparable {

    public static final Comparator<EnrolmentEvaluation> SORT_BY_STUDENT_NUMBER = new BeanComparator(
	    "enrolment.studentCurricularPlan.student.number");

    public static final Comparator<EnrolmentEvaluation> SORT_SAME_TYPE_GRADE = new Comparator<EnrolmentEvaluation>() {
	public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
	    if (o1.getEnrolmentEvaluationType() != o2.getEnrolmentEvaluationType()) {
		throw new RuntimeException("error.enrolmentEvaluation.different.types");
	    }
	    if (o1.getEnrolmentEvaluationState().getWeight() == o2.getEnrolmentEvaluationState().getWeight()) {
		return o1.compareMyWhenAlteredDateToAnotherWhenAlteredDate(o2.getWhen());
	    }
	    return o1.getEnrolmentEvaluationState().getWeight() - o2.getEnrolmentEvaluationState().getWeight();
	}
    };

    public static final Comparator<EnrolmentEvaluation> SORT_BY_GRADE = new Comparator<EnrolmentEvaluation>() {
	public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
	    return o1.getGradeWrapper().compareTo(o2.getGradeWrapper());
	}
    };

    static final public Comparator<EnrolmentEvaluation> COMPARATOR_BY_EXAM_DATE = new Comparator<EnrolmentEvaluation>() {
	public int compare(EnrolmentEvaluation o1, EnrolmentEvaluation o2) {
	    if (o1.getExamDateYearMonthDay() == null && o2.getExamDateYearMonthDay() == null) {
		return 0;
	    }
	    if (o1.getExamDateYearMonthDay() == null) {
		return -1;
	    }
	    if (o2.getExamDateYearMonthDay() == null) {
		return 1;
	    }
	    return o1.getExamDateYearMonthDay().compareTo(o2.getExamDateYearMonthDay());
	}
    };

    private static final String RECTIFICATION = "RECTIFICAÇÃO";

    private static final String RECTIFIED = "RECTIFICADO";

    public EnrolmentEvaluation() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
	setGrade(Grade.createEmptyGrade());
    }

    public EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType type) {
	this();
	if (enrolment == null || type == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}
	setEnrolment(enrolment);
	setEnrolmentEvaluationType(type);
    }

    private EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationState enrolmentEvaluationState,
	    EnrolmentEvaluationType type, Person responsibleFor, Grade grade, Date availableDate, Date examDate) {

	this(enrolment, type);

	if (enrolmentEvaluationState == null || responsibleFor == null || examDate == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}
	setEnrolmentEvaluationState(enrolmentEvaluationState);
	setPersonResponsibleForGrade(responsibleFor);
	setGrade(grade);
	setGradeAvailableDate(availableDate);
	setExamDate(examDate);

	generateCheckSum();
    }

    protected EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationState enrolmentEvaluationState,
	    EnrolmentEvaluationType type, Person responsibleFor, Grade grade, Date availableDate, Date examDate, DateTime when) {
	this(enrolment, enrolmentEvaluationState, type, responsibleFor, grade, availableDate, examDate);
	setWhenDateTime(when);
    }

    protected EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType enrolmentEvaluationType,
	    EnrolmentEvaluationState evaluationState) {
	this(enrolment, enrolmentEvaluationType);
	if (evaluationState == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}
	setEnrolmentEvaluationState(evaluationState);
	setWhenDateTime(new DateTime());
    }

    protected EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType enrolmentEvaluationType,
	    EnrolmentEvaluationState evaluationState, Employee employee) {
	this(enrolment, enrolmentEvaluationType, evaluationState);
	if (employee == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}
	setEmployee(employee);
    }

    protected EnrolmentEvaluation(Enrolment enrolment, EnrolmentEvaluationType enrolmentEvaluationType,
	    EnrolmentEvaluationState evaluationState, Employee employee, ExecutionPeriod executionPeriod) {
	this(enrolment, enrolmentEvaluationType, evaluationState, employee);
	if (executionPeriod == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}
	setExecutionPeriod(executionPeriod);
    }

    public int compareTo(Object o) {
	EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
	if (this.getEnrolment().getStudentCurricularPlan().getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
	    return compareMyWhenAlteredDateToAnotherWhenAlteredDate(enrolmentEvaluation.getWhen());
	}
	if (this.getEnrolmentEvaluationType() == enrolmentEvaluation.getEnrolmentEvaluationType()) {
	    if ((this.isRectification() && enrolmentEvaluation.isRectification())
		    || (this.isRectified() && enrolmentEvaluation.isRectified())) {
		return compareMyWhenAlteredDateToAnotherWhenAlteredDate(enrolmentEvaluation.getWhen());
	    }
	    if (this.isRectification()) {
		return 1;
	    }
	    if (enrolmentEvaluation.isRectification()) {
		return -1;
	    }
	    return compareByGrade(this.getGrade(), enrolmentEvaluation.getGrade());

	} else {
	    return compareByGrade(this.getGrade(), enrolmentEvaluation.getGrade());
	}
    }

    private int compareByGrade(final Grade grade, final Grade otherGrade) {
	EnrollmentState gradeEnrolmentState = getEnrollmentStateByGrade();
	EnrollmentState otherGradeEnrolmentState = getEnrollmentStateByGrade();
	if (gradeEnrolmentState == EnrollmentState.APROVED && otherGradeEnrolmentState == EnrollmentState.APROVED) {
	    return grade.compareTo(otherGrade);
	}

	return compareByGradeState(gradeEnrolmentState, otherGradeEnrolmentState);
    }

    private int compareByGradeState(EnrollmentState gradeEnrolmentState, EnrollmentState otherGradeEnrolmentState) {
	if (gradeEnrolmentState == EnrollmentState.APROVED) {
	    return 1;
	}
	if (otherGradeEnrolmentState == EnrollmentState.APROVED) {
	    return -1;
	}
	if (gradeEnrolmentState == EnrollmentState.NOT_APROVED && otherGradeEnrolmentState == EnrollmentState.NOT_EVALUATED) {
	    return 1;
	}
	if (gradeEnrolmentState == EnrollmentState.NOT_EVALUATED && otherGradeEnrolmentState == EnrollmentState.NOT_APROVED) {
	    return -1;
	}

	return 0;
    }

    private int compareMyExamDateToAnotherExamDate(Date examDate) {
	if (this.getExamDate() == null) {
	    return -1;
	}
	if (examDate == null) {
	    return 1;
	}

	return this.getExamDate().compareTo(examDate);

    }

    private int compareMyWhenAlteredDateToAnotherWhenAlteredDate(Date whenAltered) {
	if (this.getWhen() == null) {
	    return -1;
	}
	if (whenAltered == null) {
	    return 1;
	}

	return this.getWhen().compareTo(whenAltered);

    }

    private int compareMyGradeToAnotherGrade(final Grade otherGrade) {
	return this.getGrade().compareTo(otherGrade);
    }

    private int compareForEqualStates(EnrollmentState myEnrolmentState, EnrollmentState otherEnrolmentState, Grade otherGrade,
	    Date otherExamDate) {
	if (myEnrolmentState.equals(EnrollmentState.APROVED)) {
	    return compareMyGradeToAnotherGrade(otherGrade);
	}
	return compareMyExamDateToAnotherExamDate(otherExamDate);
    }

    private int compareForNotEqualStates(EnrollmentState myEnrolmentState, EnrollmentState otherEnrolmentState) {
	if (myEnrolmentState.equals(EnrollmentState.APROVED)) {
	    return 1;
	} else if (myEnrolmentState.equals(EnrollmentState.NOT_APROVED) && otherEnrolmentState.equals(EnrollmentState.APROVED)) {
	    return -1;
	} else if (myEnrolmentState.equals(EnrollmentState.NOT_APROVED)) {
	    return 1;
	} else if (myEnrolmentState.equals(EnrollmentState.NOT_EVALUATED)) {
	    return -1;
	} else {
	    return 0;
	}
    }

    public EnrollmentState getEnrollmentStateByGrade() {
	return getGrade().getEnrolmentState();
    }

    public GradeScale getGradeScale() {
	return getEnrolment().getGradeScale();
    }

    public boolean isNormal() {
	return getEnrolmentEvaluationType() == EnrolmentEvaluationType.NORMAL;
    }

    public boolean isImprovment() {
	return getEnrolmentEvaluationType() == EnrolmentEvaluationType.IMPROVEMENT;
    }

    public boolean isSpecialSeason() {
	return getEnrolmentEvaluationType() == EnrolmentEvaluationType.SPECIAL_SEASON;
    }

    public boolean isNotEvaluated() {
	return getEnrollmentStateByGrade() == EnrollmentState.NOT_EVALUATED;
    }

    public boolean isFlunked() {
	return getEnrollmentStateByGrade() == EnrollmentState.NOT_APROVED;
    }

    public boolean isApproved() {
	return getEnrollmentStateByGrade() == EnrollmentState.APROVED;
    }

    public void edit(Person responsibleFor, Date evaluationDate) {
	if (responsibleFor == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}
	setPersonResponsibleForGrade(responsibleFor);
	setExamDate(evaluationDate);
	generateCheckSum();
    }

    public void edit(Person responsibleFor, String gradeValue, Date availableDate, Date examDate) {
	edit(responsibleFor, Grade.createGrade(gradeValue, getGradeScale()), availableDate, examDate);
    }

    public void edit(Person responsibleFor, Grade grade, Date availableDate, Date examDate) {
	if (responsibleFor == null) {
	    throw new DomainException("error.enrolmentEvaluation.invalid.parameters");
	}

	if (examDate != null) {
	    if (!grade.isNotEvaluated()) {
		final RegistrationState stateInExamDate = getRegistration().getStateInDate(
			YearMonthDay.fromDateFields(examDate).toDateTimeAtMidnight());
		if (stateInExamDate == null
			|| !(stateInExamDate.isActive() || stateInExamDate.getStateType() == RegistrationStateType.TRANSITED)) {
		    final Enrolment enrolment = getEnrolment();
		    final StudentCurricularPlan studentCurricularPlan = enrolment.getStudentCurricularPlan();
		    final Registration registration = studentCurricularPlan.getRegistration();
		    throw new DomainException("error.enrolmentEvaluation.examDateNotInRegistrationActiveState", registration
			    .getNumber().toString());
		}
	    }
	    setExamDateYearMonthDay(YearMonthDay.fromDateFields(examDate));
	} else if (grade.isEmpty()) {
	    setExamDateYearMonthDay(null);
	} else {
	    setExamDateYearMonthDay(YearMonthDay.fromDateFields(availableDate));
	}

	setGrade(grade);
	setGradeAvailableDateYearMonthDay(YearMonthDay.fromDateFields(availableDate));
	setPersonResponsibleForGrade(responsibleFor);

	generateCheckSum();
    }

    public void confirmSubmission(Employee employee, String observation) {
	confirmSubmission(EnrolmentEvaluationState.FINAL_OBJ, employee, observation);
    }

    public void confirmSubmission(EnrolmentEvaluationState enrolmentEvaluationState, Employee employee, String observation) {

	if (!isTemporary()) {
	    throw new DomainException("EnrolmentEvaluation.cannot.submit.not.temporary");
	}

	if (!hasGrade()) {
	    throw new DomainException("EnrolmentEvaluation.cannot.submit.with.empty.grade");
	}

	if (!hasExamDateYearMonthDay()) {
	    throw new DomainException("EnrolmentEvaluation.cannot.submit.without.exam.date");
	}

	if (isPayable() && !isPayed()) {
	    throw new EnrolmentNotPayedException("EnrolmentEvaluation.cannot.set.grade.on.not.payed.enrolment.evaluation",
		    getEnrolment());
	}

	if (enrolmentEvaluationState == EnrolmentEvaluationState.RECTIFICATION_OBJ && hasRectified()) {
	    getRectified().setEnrolmentEvaluationState(EnrolmentEvaluationState.RECTIFIED_OBJ);
	}

	setEnrolmentEvaluationState(enrolmentEvaluationState); // TODO:
	setEmployee(employee);
	setObservation(observation);
	setWhenDateTime(new DateTime());

	EnrollmentState newEnrolmentState = EnrollmentState.APROVED;
	if (!this.isImprovment()) {
	    if (MarkType.getRepMarks().contains(getGradeValue())) {
		newEnrolmentState = EnrollmentState.NOT_APROVED;
	    } else if (MarkType.getNaMarks().contains(getGradeValue())) {
		newEnrolmentState = EnrollmentState.NOT_EVALUATED;
	    }
	}

	this.getEnrolment().setEnrollmentState(newEnrolmentState);
    }

    public boolean getCanBeDeleted() {
	return isTemporary() && !hasConfirmedMarkSheet()
		&& (!hasImprovementOfApprovedEnrolmentEvent() || !getImprovementOfApprovedEnrolmentEvent().isPayed());
    }

    public boolean hasConfirmedMarkSheet() {
	return hasMarkSheet() && getMarkSheet().isConfirmed();
    }

    public boolean isTemporary() {
	return getEnrolmentEvaluationState() != null
		&& getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ);
    }

    public boolean isFinal() {
	return getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.FINAL_OBJ)
		|| getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ)
		|| getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFIED_OBJ);
    }

    public boolean isRectification() {
	return (this.getObservation() != null && this.getObservation().equals(RECTIFICATION))
		|| (this.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFICATION_OBJ));
    }

    public boolean isRectified() {
	return (this.getObservation() != null && this.getObservation().equals(RECTIFIED))
		|| (this.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFIED_OBJ));
    }

    public void delete() {

	if (hasImprovementOfApprovedEnrolmentEvent() && getImprovementOfApprovedEnrolmentEvent().isPayed()) {
	    throw new DomainException("error.enrolmentEvaluation.has.been.payed");
	}

	if (!getCanBeDeleted()) {
	    throw new DomainException("error.enrolmentEvaluation.cannot.be.deleted");
	}

	removePersonResponsibleForGrade();
	removeEmployee();
	removeEnrolment();
	removeMarkSheet();
	removeRectification();
	removeRectified();
	if (hasImprovementOfApprovedEnrolmentEvent()) {
	    getImprovementOfApprovedEnrolmentEvent().removeImprovementEnrolmentEvaluations(this);
	}
	removeExecutionPeriod();

	removeRootDomainObject();

	super.deleteDomainObject();
    }

    public void removeFromMarkSheet() {
	if (hasConfirmedMarkSheet()) {
	    throw new DomainException("error.enrolmentEvaluation.cannot.be.removed.from.markSheet");
	}

	setCheckSum(null);
	setExamDateYearMonthDay(null);
	setGradeAvailableDateYearMonthDay(null);

	removeMarkSheet();
    }

    public void insertStudentFinalEvaluationForMasterDegree(String gradeValue, Person responsibleFor, Date examDate)
	    throws DomainException {

	DegreeCurricularPlan degreeCurricularPlan = getEnrolment().getStudentCurricularPlan().getDegreeCurricularPlan();

	final Grade grade = Grade.createGrade(gradeValue, getGradeScale());
	if (!grade.isEmpty() && degreeCurricularPlan.isGradeValid(grade)) {
	    edit(responsibleFor, gradeValue, Calendar.getInstance().getTime(), examDate);
	} else {
	    throw new DomainException("error.invalid.grade");
	}
    }

    public void alterStudentEnrolmentEvaluationForMasterDegree(String gradeValue, Employee employee, Person responsibleFor,
	    EnrolmentEvaluationType evaluationType, Date evaluationAvailableDate, Date examDate, String observation)
	    throws DomainException {

	Enrolment enrolment = getEnrolment();
	DegreeCurricularPlan degreeCurricularPlan = getEnrolment().getStudentCurricularPlan().getDegreeCurricularPlan();

	final Grade grade = Grade.createGrade(gradeValue, getGradeScale());
	if (grade.isEmpty()) {
	    EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(enrolment, getEnrolmentEvaluationType());
	    enrolmentEvaluation.confirmSubmission(EnrolmentEvaluationState.FINAL_OBJ, employee, observation);
	    enrolment.setEnrollmentState(EnrollmentState.ENROLLED);
	} else {
	    if (degreeCurricularPlan.isGradeValid(grade)) {
		EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(enrolment, evaluationType);
		enrolmentEvaluation.edit(responsibleFor, grade, evaluationAvailableDate, examDate);
		enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ); // temporary
		// hack
		enrolmentEvaluation.confirmSubmission(EnrolmentEvaluationState.FINAL_OBJ, employee, observation);
	    } else {
		throw new DomainException("error.invalid.grade");
	    }
	}
    }

    protected void generateCheckSum() {
	StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(getExamDateYearMonthDay().toString()).append(getGradeValue());
	stringBuilder.append(getEnrolmentEvaluationType());
	stringBuilder.append(getEnrolment().getStudentCurricularPlan().getRegistration().getNumber());
	setCheckSum(FenixDigestUtils.createDigest(stringBuilder.toString()));
    }

    public IGrade getGradeWrapper() {
	return GradeFactory.getInstance().getGrade(getGradeValue());
    }

    @Override
    public String getGradeValue() {
	return getGrade().getValue();
    }

    @Override
    @Deprecated
    public void setGradeValue(final String grade) {
	setGrade(grade);
    }

    public void setGrade(final String grade) {
	setGrade(Grade.createGrade(grade, getGradeScale()));
    }

    @Override
    public void setGrade(final Grade grade) {

	if (isFinal()) {
	    throw new DomainException("EnrolmentEvaluation.cannot.set.grade.final");
	}

	super.setGrade(grade);

	// TODO remove this once we're sure migration to Grade went OK
	super.setGradeValue(grade.getValue());
    }

    @Deprecated
    public Registration getStudent() {
	return this.getRegistration();
    }

    public Registration getRegistration() {
	return getStudentCurricularPlan().getRegistration();
    }

    public DegreeCurricularPlan getDegreeCurricularPlan() {
	return getStudentCurricularPlan().getDegreeCurricularPlan();
    }

    public StudentCurricularPlan getStudentCurricularPlan() {
	return getEnrolment().getStudentCurricularPlan();
    }

    public MarkSheet getRectificationMarkSheet() {
	if (this.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.RECTIFIED_OBJ) && hasRectification()) {
	    return getRectification().getMarkSheet();
	} else {
	    return null;
	}
    }

    public boolean hasGrade() {
	return !getGrade().isEmpty();
    }

    final public boolean hasExamDateYearMonthDay() {
	return getExamDateYearMonthDay() != null;
    }

    public boolean isPayable() {
	return hasImprovementOfApprovedEnrolmentEvent();
    }

    public boolean isPayed() {
	return getImprovementOfApprovedEnrolmentEvent().isPayed();
    }

    @Override
    public ExecutionPeriod getExecutionPeriod() {
	if (getEnrolmentEvaluationType() == EnrolmentEvaluationType.IMPROVEMENT) {
	    return super.getExecutionPeriod();
	}

	if (getEnrolment() != null) {
	    return getEnrolment().getExecutionPeriod();
	}

	return null;
    }

}
