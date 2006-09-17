package net.sourceforge.fenixedu.domain;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu.commons.CollectionUtils;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.curriculum.GradeFactory;
import net.sourceforge.fenixedu.domain.curriculum.IGrade;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.log.EnrolmentLog;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.util.EnrolmentAction;
import net.sourceforge.fenixedu.util.EnrolmentEvaluationState;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

/**
 * @author dcs-rjao
 * 
 * 24/Mar/2003
 */

public class Enrolment extends Enrolment_Base {

    public static final Comparator<Enrolment> COMPARATOR_BY_EXECUTION_PERIOD = new BeanComparator(
	    "executionPeriod");

    public static final Comparator<Enrolment> REVERSE_COMPARATOR_BY_EXECUTION_PERIOD = new ComparatorChain();
    static {
	((ComparatorChain) REVERSE_COMPARATOR_BY_EXECUTION_PERIOD).addComparator(new BeanComparator(
		"executionPeriod"), true);
    }

    private Integer accumulatedWeight;

    private Double ectsCredits;

    /*
         * static {
         * EnrolmentEvaluation.EnrolmentEnrolmentEvaluation.addListener(new
         * EnrolmentEnrolmentEvaluationListener()); }
         */

    public Enrolment() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    public Enrolment(StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod, EnrollmentCondition enrolmentCondition, String createdBy) {
	this();
	initializeAsNew(studentCurricularPlan, curricularCourse, executionPeriod, enrolmentCondition,
		createdBy);
	createEnrolmentLog(studentCurricularPlan.getStudent(), EnrolmentAction.ENROL);
    }

    @Deprecated
    public EnrollmentCondition getCondition() {
	return getEnrolmentCondition();
    }

    @Deprecated
    public void setCondition(EnrollmentCondition enrollmentCondition) {
	setEnrolmentCondition(enrollmentCondition);
    }

    public boolean isSpecialSeason() {
	boolean result = false;
	for (EnrolmentEvaluation enrolmentEvaluation : this.getEvaluations()) {
	    result |= enrolmentEvaluation.getEnrolmentEvaluationType().equals(
		    EnrolmentEvaluationType.SPECIAL_SEASON);
	}

	return result;
    }

    // new student structure methods
    public Enrolment(StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
	    EnrollmentCondition enrolmentCondition, String createdBy) {
	this();
	if (studentCurricularPlan == null || curriculumGroup == null || curricularCourse == null
		|| executionPeriod == null || enrolmentCondition == null || createdBy == null) {
	    throw new DomainException("invalid arguments");
	}
	//TODO: check this
	//validateDegreeModuleLink(curriculumGroup, curricularCourse);
	initializeAsNew(studentCurricularPlan, curriculumGroup, curricularCourse, executionPeriod,
		enrolmentCondition, createdBy);
    }

    protected void initializeAsNew(StudentCurricularPlan studentCurricularPlan,
	    CurriculumGroup curriculumGroup, CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod, EnrollmentCondition enrolmentCondition, String createdBy) {
	initializeAsNewWithoutEnrolmentEvaluation(studentCurricularPlan, curriculumGroup,
		curricularCourse, executionPeriod, enrolmentCondition, createdBy);
	createEnrolmentEvaluationWithoutGrade();
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(
	    StudentCurricularPlan studentCurricularPlan, CurriculumGroup curriculumGroup,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
	    EnrollmentCondition enrolmentCondition, String createdBy) {
	setCurricularCourse(curricularCourse);
	setEnrollmentState(EnrollmentState.ENROLLED);
	setExecutionPeriod(executionPeriod);
	setCurricularCourse(curricularCourse);
	setCurriculumGroup(curriculumGroup);
	setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
	setCreationDateDateTime(new DateTime());
	setEnrolmentCondition(enrolmentCondition);
	setCreatedBy(createdBy);

	createAttend(studentCurricularPlan.getStudent(), curricularCourse, executionPeriod);
    }

    // end

    public Integer getAccumulatedWeight() {
	return accumulatedWeight;
    }

    public void setAccumulatedWeight(Integer accumulatedWeight) {
	this.accumulatedWeight = accumulatedWeight;
    }

    protected void initializeAsNew(StudentCurricularPlan studentCurricularPlan,
	    CurricularCourse curricularCourse, ExecutionPeriod executionPeriod,
	    EnrollmentCondition enrolmentCondition, String createdBy) {
	initializeAsNewWithoutEnrolmentEvaluation(studentCurricularPlan, curricularCourse,
		executionPeriod, enrolmentCondition, createdBy);
	createEnrolmentEvaluationWithoutGrade();
    }

    protected void initializeAsNewWithoutEnrolmentEvaluation(
	    StudentCurricularPlan studentCurricularPlan, CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod, EnrollmentCondition enrolmentCondition, String createdBy) {
	setCurricularCourse(curricularCourse);
	setEnrollmentState(EnrollmentState.ENROLLED);
	setExecutionPeriod(executionPeriod);
	setStudentCurricularPlan(studentCurricularPlan);
	setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
	setCreationDate(new Date());
	setCondition(enrolmentCondition);
	setCreatedBy(createdBy);

	createAttend(studentCurricularPlan.getStudent(), curricularCourse, executionPeriod);
    }

    public void unEnroll() throws DomainException {

	for (EnrolmentEvaluation eval : getEvaluations()) {

	    if (eval.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.NORMAL)
		    && eval.getEnrolmentEvaluationState().equals(EnrolmentEvaluationState.TEMPORARY_OBJ)
		    && (eval.getGrade() == null || eval.getGrade().equals("")))
		;
	    else
		throw new DomainException("error.enrolment.cant.unenroll");
	}

	delete();
    }

    public void delete() {
	createEnrolmentLog(EnrolmentAction.UNENROL);
	// TODO: falta ver se � dos antigos enrolments ou dos novos
	final Registration registration = getStudentCurricularPlan().getStudent();

	removeExecutionPeriod();
	removeStudentCurricularPlan();

	Iterator<Attends> attendsIter = getAttendsIterator();
	while (attendsIter.hasNext()) {
	    Attends attends = attendsIter.next();

	    attendsIter.remove();
	    attends.removeEnrolment();

	    if (!attends.hasAnyAssociatedMarks() && !attends.hasAnyStudentGroups()) {
		boolean hasShiftEnrolment = false;
		for (Shift shift : attends.getDisciplinaExecucao().getAssociatedShifts()) {
		    if (shift.hasStudents(registration)) {
			hasShiftEnrolment = true;
			break;
		    }
		}

		if (!hasShiftEnrolment) {
		    attends.delete();
		}
	    }
	}

	Iterator<EnrolmentEvaluation> evalsIter = getEvaluationsIterator();
	while (evalsIter.hasNext()) {
	    EnrolmentEvaluation eval = evalsIter.next();
	    evalsIter.remove();
	    eval.delete();
	}

	Iterator<CreditsInAnySecundaryArea> creditsInAnysecundaryAreaIterator = getCreditsInAnySecundaryAreasIterator();

	while (creditsInAnysecundaryAreaIterator.hasNext()) {
	    CreditsInAnySecundaryArea credits = creditsInAnysecundaryAreaIterator.next();
	    creditsInAnysecundaryAreaIterator.remove();
	    credits.delete();
	}

	Iterator<CreditsInScientificArea> creditsInScientificAreaIterator = getCreditsInScientificAreasIterator();

	while (creditsInScientificAreaIterator.hasNext()) {
	    CreditsInScientificArea credits = creditsInScientificAreaIterator.next();
	    creditsInScientificAreaIterator.remove();
	    credits.delete();
	}

	Iterator<EquivalentEnrolmentForEnrolmentEquivalence> equivalentEnrolmentIterator = getEquivalentEnrolmentForEnrolmentEquivalencesIterator();

	while (equivalentEnrolmentIterator.hasNext()) {
	    EquivalentEnrolmentForEnrolmentEquivalence equivalentEnrolment = equivalentEnrolmentIterator
		    .next();
	    equivalentEnrolmentIterator.remove();
	    equivalentEnrolment.removeEquivalentEnrolment();

	    EnrolmentEquivalence equivalence = equivalentEnrolment.getEnrolmentEquivalence();
	    Enrolment enrolment = equivalence.getEnrolment();

	    equivalence.removeEnrolment();
	    enrolment.delete();
	    equivalentEnrolment.removeEnrolmentEquivalence();

	    equivalentEnrolment.delete();
	    equivalence.delete();
	}

	Iterator<EnrolmentEquivalence> equivalenceIterator = getEnrolmentEquivalencesIterator();

	while (equivalenceIterator.hasNext()) {
	    EnrolmentEquivalence equivalence = equivalenceIterator.next();
	    equivalenceIterator.remove();
	    equivalence.removeEnrolment();

	    Iterator<EquivalentEnrolmentForEnrolmentEquivalence> equivalentRestrictionIterator = equivalence
		    .getEquivalenceRestrictionsIterator();

	    while (equivalentRestrictionIterator.hasNext()) {
		EquivalentEnrolmentForEnrolmentEquivalence equivalentRestriction = equivalentRestrictionIterator
			.next();
		equivalentRestriction.removeEquivalentEnrolment();
		equivalentRestrictionIterator.remove();
		equivalentRestriction.removeEnrolmentEquivalence();

		equivalentRestriction.delete();
	    }
	    equivalence.delete();
	}

	super.delete();

    }

    public EnrolmentEvaluation getImprovementEvaluation() {

	for (EnrolmentEvaluation evaluation : getEvaluations()) {
	    if (evaluation.getEnrolmentEvaluationType().equals(EnrolmentEvaluationType.IMPROVEMENT)
		    && evaluation.getEnrolmentEvaluationState().equals(
			    EnrolmentEvaluationState.TEMPORARY_OBJ))

		return evaluation;
	}

	return null;
    }

    public EnrolmentEvaluation getEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(
	    final EnrolmentEvaluationType evaluationType, final String grade) {

	return (EnrolmentEvaluation) CollectionUtils.find(getEvaluations(), new Predicate() {

	    public boolean evaluate(Object o) {
		EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
		String evaluationGrade = enrolmentEvaluation.getGrade();

		return enrolmentEvaluation.getEnrolmentEvaluationType().equals(evaluationType)
			&& ((grade == null && evaluationGrade == null) || (evaluationGrade != null && evaluationGrade
				.equals(grade)));
	    }

	});
    }

    public EnrolmentEvaluation getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
	    final EnrolmentEvaluationState state, final EnrolmentEvaluationType type) {
	return (EnrolmentEvaluation) CollectionUtils.find(getEvaluations(), new Predicate() {

	    public boolean evaluate(Object o) {
		EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) o;
		return (enrolmentEvaluation.getEnrolmentEvaluationState().equals(state) && enrolmentEvaluation
			.getEnrolmentEvaluationType().equals(type));
	    }

	});
    }

    public List<EnrolmentEvaluation> getEnrolmentEvaluationsByEnrolmentEvaluationState(
	    final EnrolmentEvaluationState evaluationState) {
	List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();
	for (EnrolmentEvaluation evaluation : getEvaluationsSet()) {
	    if (evaluation.getEnrolmentEvaluationState().equals(evaluationState)) {
		result.add(evaluation);
	    }
	}
	return result;
    }

    public List<EnrolmentEvaluation> getEnrolmentEvaluationsByEnrolmentEvaluationType(
	    final EnrolmentEvaluationType evaluationType) {
	List<EnrolmentEvaluation> result = new ArrayList<EnrolmentEvaluation>();
	for (EnrolmentEvaluation evaluation : getEvaluationsSet()) {
	    if (evaluation.getEnrolmentEvaluationType().equals(evaluationType)) {
		result.add(evaluation);
	    }
	}
	return result;
    }

    public EnrolmentEvaluation submitEnrolmentEvaluation(
	    EnrolmentEvaluationType enrolmentEvaluationType, Mark publishedMark, Employee employee,
	    Person personResponsibleForGrade, Date evaluationDate, String observation) {

	EnrolmentEvaluation enrolmentEvaluation = getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
		EnrolmentEvaluationState.TEMPORARY_OBJ, enrolmentEvaluationType);

	// There can be only one enrolmentEvaluation with Temporary State
	if (enrolmentEvaluation == null) {
	    enrolmentEvaluation = new EnrolmentEvaluation();
	    enrolmentEvaluation.setEnrolment(this);
	}

	// teacher responsible for execution course
	String grade = null;
	if ((publishedMark == null) || (publishedMark.getMark().length() == 0))
	    grade = "NA";
	else
	    grade = publishedMark.getMark().toUpperCase();

	enrolmentEvaluation.setGrade(grade);

	enrolmentEvaluation.setEnrolmentEvaluationType(enrolmentEvaluationType);
	enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
	enrolmentEvaluation.setObservation(observation);
	enrolmentEvaluation.setPersonResponsibleForGrade(personResponsibleForGrade);

	enrolmentEvaluation.setEmployee(employee);

	Calendar calendar = Calendar.getInstance();
	enrolmentEvaluation.setWhen(new Timestamp(calendar.getTimeInMillis()));
	enrolmentEvaluation.setGradeAvailableDate(calendar.getTime());
	if (evaluationDate != null) {
	    enrolmentEvaluation.setExamDate(evaluationDate);
	} else {
	    enrolmentEvaluation.setExamDate(calendar.getTime());
	}

	enrolmentEvaluation.setCheckSum("");

	return enrolmentEvaluation;
    }

    protected void createEnrolmentEvaluationWithoutGrade() {

	EnrolmentEvaluation enrolmentEvaluation = getEnrolmentEvaluationByEnrolmentEvaluationTypeAndGrade(
		EnrolmentEvaluationType.NORMAL, null);

	if (enrolmentEvaluation == null) {
	    enrolmentEvaluation = new EnrolmentEvaluation();
	    enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
	    enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);

	    addEvaluations(enrolmentEvaluation);
	}
    }

    private void createAttend(Registration registration, CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod) {

	List executionCourses = curricularCourse.getExecutionCoursesByExecutionPeriod(executionPeriod);

	ExecutionCourse executionCourse = null;
	if (executionCourses.size() > 1) {
	    Iterator iterator = executionCourses.iterator();
	    while (iterator.hasNext()) {
		ExecutionCourse executionCourse2 = (ExecutionCourse) iterator.next();
		if (executionCourse2.getExecutionCourseProperties() == null
			|| executionCourse2.getExecutionCourseProperties().isEmpty()) {
		    executionCourse = executionCourse2;
		}
	    }
	} else if (executionCourses.size() == 1) {
	    executionCourse = (ExecutionCourse) executionCourses.get(0);
	}

	if (executionCourse != null) {
	    Attends attend = executionCourse.getAttendsByStudent(registration);

	    if (attend != null) {
		addAttends(attend);
	    } else {
		Attends attendToWrite = new Attends(registration, executionCourse);
		addAttends(attendToWrite);
	    }
	}
    }

    public void createEnrolmentEvaluationForImprovement(Employee employee,
	    ExecutionPeriod currentExecutionPeriod, Registration registration) {

	EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation();

	enrolmentEvaluation.setEmployee(employee);
	enrolmentEvaluation.setWhen(new Date());
	enrolmentEvaluation.setEnrolment(this);
	enrolmentEvaluation.setEnrolmentEvaluationState(EnrolmentEvaluationState.TEMPORARY_OBJ);
	enrolmentEvaluation.setEnrolmentEvaluationType(EnrolmentEvaluationType.IMPROVEMENT);

	createAttendForImprovment(currentExecutionPeriod, registration);
    }

    private void createAttendForImprovment(final ExecutionPeriod currentExecutionPeriod,
	    final Registration registration) {

	List executionCourses = getCurricularCourse().getAssociatedExecutionCourses();
	ExecutionCourse currentExecutionCourse = (ExecutionCourse) CollectionUtils.find(
		executionCourses, new Predicate() {

		    public boolean evaluate(Object arg0) {
			ExecutionCourse executionCourse = (ExecutionCourse) arg0;
			if (executionCourse.getExecutionPeriod().equals(currentExecutionPeriod))
			    return true;
			return false;
		    }

		});

	if (currentExecutionCourse != null) {
	    List attends = currentExecutionCourse.getAttends();
	    Attends attend = (Attends) CollectionUtils.find(attends, new Predicate() {

		public boolean evaluate(Object arg0) {
		    Attends frequenta = (Attends) arg0;
		    if (frequenta.getAluno().equals(registration))
			return true;
		    return false;
		}

	    });

	    if (attend != null) {
		attend.setEnrolment(this);
	    } else {
		attend = new Attends(registration, currentExecutionCourse);
		attend.setEnrolment(this);
	    }
	}
    }

    public boolean isImprovementForExecutionCourse(ExecutionCourse executionCourse) {
	return !getExecutionPeriod().equals(executionCourse.getExecutionPeriod());
    }

    public void unEnrollImprovement(final ExecutionPeriod executionPeriod) throws DomainException {
	EnrolmentEvaluation improvmentEnrolmentEvaluation = getImprovementEvaluation();
	if (improvmentEnrolmentEvaluation != null) {

	    improvmentEnrolmentEvaluation.delete();

	    final Registration registration = getStudentCurricularPlan().getStudent();
	    List<ExecutionCourse> executionCourses = getCurricularCourse()
		    .getAssociatedExecutionCourses();

	    ExecutionCourse currentExecutionCourse = (ExecutionCourse) CollectionUtils.find(
		    executionCourses, new Predicate() {

			public boolean evaluate(Object arg0) {
			    ExecutionCourse executionCourse = (ExecutionCourse) arg0;
			    if (executionCourse.getExecutionPeriod().equals(executionPeriod))
				return true;
			    return false;
			}
		    });

	    if (currentExecutionCourse != null) {
		List attends = currentExecutionCourse.getAttends();
		Attends attend = (Attends) CollectionUtils.find(attends, new Predicate() {

		    public boolean evaluate(Object arg0) {
			Attends frequenta = (Attends) arg0;
			if (frequenta.getAluno().equals(registration))
			    return true;
			return false;
		    }
		});

		if (attend != null) {
		    attend.delete();
		}
	    }
	} else {
	    throw new DomainException("error.enrolment.cant.unenroll.improvement");
	}
    }

    public List<EnrolmentEvaluation> getAllFinalEnrolmentEvaluations() {
	return (List<EnrolmentEvaluation>) CollectionUtils.select(getEvaluations(), new Predicate() {

	    public boolean evaluate(Object arg0) {
		EnrolmentEvaluation enrolmentEvaluation = (EnrolmentEvaluation) arg0;
		return enrolmentEvaluation.getEnrolmentEvaluationState().equals(
			EnrolmentEvaluationState.FINAL_OBJ)
			|| enrolmentEvaluation.getEnrolmentEvaluationState().equals(
				EnrolmentEvaluationState.RECTIFICATION_OBJ);
	    }

	});
    }

    private boolean hasEnrolmentEvaluationByType(EnrolmentEvaluationType enrolmentEvaluationType) {
	for (EnrolmentEvaluation enrolmentEvaluation : getEvaluations()) {
	    if (enrolmentEvaluation.getEnrolmentEvaluationType().equals(enrolmentEvaluationType)) {
		return true;
	    }
	}
	return false;
    }

    public boolean hasImprovement() {
	return hasEnrolmentEvaluationByType(EnrolmentEvaluationType.IMPROVEMENT);
    }

    public boolean hasSpecialSeason() {
	return hasEnrolmentEvaluationByType(EnrolmentEvaluationType.SPECIAL_SEASON);
    }

    public Integer getFinalGrade() {
	final EnrolmentEvaluation enrolmentEvaluation = getFinalEnrolmentEvaluation();
	return (enrolmentEvaluation == null || !StringUtils.isNumeric(enrolmentEvaluation.getGrade())) ? null
		: Integer.valueOf(enrolmentEvaluation.getGrade());

    }

    public EnrolmentEvaluation getFinalEnrolmentEvaluation() {
	EnrolmentEvaluation finalEnrolmentEvaluation = null;
	for (final EnrolmentEvaluation enrolmentEvaluation : getEvaluations()) {
	    if (enrolmentEvaluation.getEnrolmentEvaluationState().equals(
		    EnrolmentEvaluationState.FINAL_OBJ)) {
		if (finalEnrolmentEvaluation == null
			|| enrolmentEvaluation.compareTo(finalEnrolmentEvaluation) > 0) {
		    finalEnrolmentEvaluation = enrolmentEvaluation;
		}
	    }
	}
	return finalEnrolmentEvaluation;
    }

    public boolean isNotEvaluated() {
	final EnrolmentEvaluation finalEnrolmentEvaluation = getFinalEnrolmentEvaluation();
	return finalEnrolmentEvaluation == null || finalEnrolmentEvaluation.isNotEvaluated();
    }

    public boolean isFlunked() {
	final EnrolmentEvaluation finalEnrolmentEvaluation = getFinalEnrolmentEvaluation();
	return finalEnrolmentEvaluation != null && finalEnrolmentEvaluation.isFlunked();
    }

    public boolean isApproved() {
	final EnrolmentEvaluation finalEnrolmentEvaluation = getFinalEnrolmentEvaluation();
	return finalEnrolmentEvaluation != null && finalEnrolmentEvaluation.isApproved();
    }

    public Boolean isFirstTime() {
	List<Enrolment> enrollments = getStudentCurricularPlan().getActiveEnrolments(
		getCurricularCourse());
	for (Enrolment enrollment : enrollments) {
	    if (enrollment.getExecutionPeriod().isBefore(this.getExecutionPeriod())) {
		return Boolean.FALSE;
	    }
	}
	return Boolean.TRUE;
    }

    public int getNumberOfTotalEnrolmentsInThisCourse() {
	return this.getStudentCurricularPlan().countEnrolmentsByCurricularCourse(
		this.getCurricularCourse());
    }

    public CurricularCourse getCurricularCourse() {
	return (CurricularCourse) getDegreeModule();
    }

    public void setCurricularCourse(CurricularCourse curricularCourse) {
	setDegreeModule(curricularCourse);
    }

    protected void createEnrolmentLog(Registration registration, EnrolmentAction action) {
	new EnrolmentLog(action, registration, this.getCurricularCourse(), this.getExecutionPeriod(),
		getCurrentUser());
    }

    protected void createEnrolmentLog(EnrolmentAction action) {
	new EnrolmentLog(action, this.getStudentCurricularPlan().getStudent(), this
		.getCurricularCourse(), this.getExecutionPeriod(), getCurrentUser());
    }

    @Override
    public StringBuilder print(String tabs) {
	final StringBuilder builder = new StringBuilder();
	builder.append(tabs);
	builder.append("[E ").append(getDegreeModule().getName()).append(" ]\n");

	return builder;
    }

    public EnrolmentEvaluation addNewEnrolmentEvaluation(
	    EnrolmentEvaluationState enrolmentEvaluationState,
	    EnrolmentEvaluationType enrolmentEvaluationType, Person responsibleFor, String grade,
	    Date availableDate, Date examDate) {
	return new EnrolmentEvaluation(this, enrolmentEvaluationState, enrolmentEvaluationType,
		responsibleFor, grade, availableDate, examDate);
    }

    public boolean hasAssociatedMarkSheet(MarkSheetType markSheetType) {
	for (final EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {
	    if (enrolmentEvaluation.hasMarkSheet()
		    && enrolmentEvaluation.getEnrolmentEvaluationType() == markSheetType
			    .getEnrolmentEvaluationType()) {
		return true;
	    }
	}
	return false;
    }

    private EnrolmentEvaluation getEnrolmentEvaluationFinal() {
	final SortedSet<EnrolmentEvaluation> normal = new TreeSet<EnrolmentEvaluation>(
		EnrolmentEvaluation.SORT_SAME_TYPE_GRADE);
	final SortedSet<EnrolmentEvaluation> specialSeason = new TreeSet<EnrolmentEvaluation>(
		EnrolmentEvaluation.SORT_SAME_TYPE_GRADE);
	final SortedSet<EnrolmentEvaluation> improvment = new TreeSet<EnrolmentEvaluation>(
		EnrolmentEvaluation.SORT_SAME_TYPE_GRADE);

	for (final EnrolmentEvaluation enrolmentEvaluation : this.getEvaluationsSet()) {

	    if (enrolmentEvaluation.getEnrolmentEvaluationState().equals(
		    EnrolmentEvaluationState.FINAL_OBJ)
		    || enrolmentEvaluation.getEnrolmentEvaluationState().equals(
			    EnrolmentEvaluationState.RECTIFICATION_OBJ)) {

		switch (enrolmentEvaluation.getEnrolmentEvaluationType()) {
		case NORMAL:
		case EQUIVALENCE:
		    normal.add(enrolmentEvaluation);
		    break;
		case IMPROVEMENT:
		    improvment.add(enrolmentEvaluation);
		    break;
		case SPECIAL_SEASON:
		    specialSeason.add(enrolmentEvaluation);
		    break;
		default:
		    break;
		}
	    }
	}

	final SortedSet<EnrolmentEvaluation> finalGrade = new TreeSet<EnrolmentEvaluation>(
		EnrolmentEvaluation.SORT_BY_GRADE);

	if (!normal.isEmpty()) {
	    finalGrade.add(normal.last());
	}
	if (!specialSeason.isEmpty()) {
	    finalGrade.add(specialSeason.last());
	}
	if (!improvment.isEmpty()) {
	    finalGrade.add(improvment.last());
	}

	return finalGrade.last();
    }

    public IGrade getGradeFinal() {
	switch (this.getEnrollmentState()) {
	case APROVED:
	    return getEnrolmentEvaluationFinal().getGradeWrapper();
	case TEMPORARILY_ENROLLED:
	case ENROLLED:
	    return null;
	case NOT_EVALUATED:
	    return GradeFactory.getInstance().getGrade("NA");
	case NOT_APROVED:
	    return GradeFactory.getInstance().getGrade("RE");
	default:
	    throw new DomainException("error.enrolment.invalid.enrolment.state");
	}
    }

    /*
         * private static class EnrolmentEnrolmentEvaluationListener extends
         * dml.runtime.RelationAdapter<EnrolmentEvaluation, Enrolment> {
         * 
         * @Override public void afterAdd(EnrolmentEvaluation
         * enrolmentEvaluation, Enrolment enrolment) { if(enrolmentEvaluation !=
         * null && enrolment != null) {
         * enrolment.calculateNewEnrolmentState(enrolmentEvaluation.getEnrolmentEvaluationState()); } }
         *  }
         */
    public void calculateNewEnrolmentState(EnrolmentEvaluationState enrolmentEvaluationState) {
	// TODO: anulled
	if (enrolmentEvaluationState == EnrolmentEvaluationState.FINAL_OBJ
		|| enrolmentEvaluationState == EnrolmentEvaluationState.RECTIFICATION_OBJ) {

	    EnrolmentEvaluation enrolmentEvaluationFinal = getEnrolmentEvaluationFinal();
	    this.setEnrollmentState(enrolmentEvaluationFinal.getEnrollmentStateByGrade());
	}
    }

    public List<EnrolmentEvaluation> getConfirmedEvaluations(MarkSheetType markSheetType) {
	List<EnrolmentEvaluation> evaluations = new ArrayList<EnrolmentEvaluation>();
	for (EnrolmentEvaluation evaluation : this.getEvaluationsSet()) {
	    if (evaluation.hasMarkSheet()
		    && evaluation.getMarkSheet().getMarkSheetType() == markSheetType
		    && evaluation.getMarkSheet().isConfirmed()) {

		evaluations.add(evaluation);
	    }
	}
	Collections.sort(evaluations, new BeanComparator("when"));
	return evaluations;
    }

    public Attends getAttendsByExecutionCourse(ExecutionCourse executionCourse) {
	for (Attends attends : this.getAttendsSet()) {
	    if (attends.getDisciplinaExecucao() == executionCourse) {
		return attends;
	    }
	}
	return null;
    }

    public boolean hasAttendsFor(ExecutionPeriod executionPeriod) {
	for (final Attends attends : this.getAttendsSet()) {
	    if (attends.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		return true;
	    }
	}
	return false;
    }

    public void createSpecialSeasonEvaluation() {
	if (getEnrolmentEvaluationType() != EnrolmentEvaluationType.SPECIAL_SEASON && !isApproved()) {
	    setEnrolmentEvaluationType(EnrolmentEvaluationType.SPECIAL_SEASON);
	    if (getEnrollmentState() == EnrollmentState.ENROLLED) {
		setEnrolmentCondition(EnrollmentCondition.TEMPORARY);
	    } else {
		setEnrollmentState(EnrollmentState.ENROLLED);
		setEnrolmentCondition(EnrollmentCondition.FINAL);
	    }
	    EnrolmentEvaluation enrolmentEvaluation = new EnrolmentEvaluation(this,
		    EnrolmentEvaluationType.SPECIAL_SEASON, EnrolmentEvaluationState.TEMPORARY_OBJ);
	    enrolmentEvaluation.setWhenDateTime(new DateTime());
	} else {
	    throw new DomainException("error.invalid.enrolment.state");
	}
    }

    public void deleteSpecialSeasonEvaluation() {
	if (getEnrolmentEvaluationType() == EnrolmentEvaluationType.SPECIAL_SEASON
		&& getEnrollmentState().equals(EnrollmentState.ENROLLED) && hasSpecialSeason()) {
	    setEnrolmentCondition(EnrollmentCondition.FINAL);
	    setEnrolmentEvaluationType(EnrolmentEvaluationType.NORMAL);
	    EnrolmentEvaluation enrolmentEvaluation = getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
		    EnrolmentEvaluationState.TEMPORARY_OBJ, EnrolmentEvaluationType.SPECIAL_SEASON);
	    if (enrolmentEvaluation != null) {
		enrolmentEvaluation.delete();
	    }

	    EnrolmentEvaluation normalEnrolmentEvaluation = getEnrolmentEvaluationByEnrolmentEvaluationStateAndType(
		    EnrolmentEvaluationState.FINAL_OBJ, EnrolmentEvaluationType.NORMAL);
	    if (normalEnrolmentEvaluation != null) {
		setEnrollmentState(normalEnrolmentEvaluation.getEnrollmentStateByGrade());
	    }
	} else {
	    throw new DomainException("error.invalid.enrolment.state");
	}
    }

    public EnrolmentEvaluation getLatestEnrolmentEvaluation() {
	return (getStudentCurricularPlan().getDegreeType() == DegreeType.DEGREE) ? getLatestEnrolmentEvalution(this
		.getAllFinalEnrolmentEvaluations())
		: getLatestEnrolmentEvalution(this.getEvaluations());
    }

    private EnrolmentEvaluation getLatestEnrolmentEvalution(
	    List<EnrolmentEvaluation> enrolmentEvaluations) {
	return (enrolmentEvaluations == null || enrolmentEvaluations.isEmpty()) ? null : Collections
		.max(enrolmentEvaluations);
    }

    public Double getEctsCredits() {
	return ectsCredits;
    }

    public void setEctsCredits(Double ectsCredits) {
	this.ectsCredits = ectsCredits;
    }

    public ExecutionCourse getExecutionCourseFor(final ExecutionPeriod executionPeriod) {
	for (final Attends attend : getAttends()) {
	    if (attend.getDisciplinaExecucao().getExecutionPeriod() == executionPeriod) {
		return attend.getDisciplinaExecucao();
	    }
	}

	return null;
    }
   
    
    @Override
    public List<Enrolment> getEnrolments() {
        return Collections.singletonList(this);
    }
    
    @Override
    public StudentCurricularPlan getStudentCurricularPlan() {
        return hasCurriculumGroup() ? getCurriculumGroup().getStudentCurricularPlan() : super.getStudentCurricularPlan(); 
    }

}