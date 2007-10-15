package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sourceforge.fenixedu._development.LogLevel;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.BothAreasAreTheSameServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedBranchChangeException;
import net.sourceforge.fenixedu.dataTransferObject.administrativeOffice.dismissal.DismissalBean.SelectedCurricularCourse;
import net.sourceforge.fenixedu.domain.accounting.events.ImprovementOfApprovedEnrolmentEvent;
import net.sourceforge.fenixedu.domain.accounting.events.gratuity.GratuityEvent;
import net.sourceforge.fenixedu.domain.administrativeOffice.AdministrativeOffice;
import net.sourceforge.fenixedu.domain.branch.BranchType;
import net.sourceforge.fenixedu.domain.curricularPeriod.CurricularPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.MaximumNumberOfCreditsForEnrolmentPeriod;
import net.sourceforge.fenixedu.domain.curricularRules.executors.RuleResult;
import net.sourceforge.fenixedu.domain.curricularRules.executors.ruleExecutors.CurricularRuleLevel;
import net.sourceforge.fenixedu.domain.curriculum.CurricularCourseEnrollmentType;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentCondition;
import net.sourceforge.fenixedu.domain.curriculum.EnrollmentState;
import net.sourceforge.fenixedu.domain.curriculum.EnrolmentEvaluationType;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degree.enrollment.CurricularCourse2Enroll;
import net.sourceforge.fenixedu.domain.degree.enrollment.NotNeedToEnrollInCurricularCourse;
import net.sourceforge.fenixedu.domain.degree.enrollment.rules.IEnrollmentRule;
import net.sourceforge.fenixedu.domain.degreeStructure.Context;
import net.sourceforge.fenixedu.domain.degreeStructure.CourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleCourseGroup;
import net.sourceforge.fenixedu.domain.degreeStructure.CycleType;
import net.sourceforge.fenixedu.domain.degreeStructure.DegreeModule;
import net.sourceforge.fenixedu.domain.degreeStructure.OptionalCurricularCourse;
import net.sourceforge.fenixedu.domain.enrolment.DegreeModuleToEnrol;
import net.sourceforge.fenixedu.domain.enrolment.EnrolmentContext;
import net.sourceforge.fenixedu.domain.enrolment.IDegreeModuleToEvaluate;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.exceptions.FenixDomainException;
import net.sourceforge.fenixedu.domain.gratuity.GratuitySituationType;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.student.curriculum.Curriculum;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.StudentCurricularPlanState;
import net.sourceforge.fenixedu.domain.studentCurriculum.Credits;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumLine;
import net.sourceforge.fenixedu.domain.studentCurriculum.CurriculumModule;
import net.sourceforge.fenixedu.domain.studentCurriculum.CycleCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Dismissal;
import net.sourceforge.fenixedu.domain.studentCurriculum.Equivalence;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExternalCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.ExtraCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.NoCourseGroupCurriculumGroupType;
import net.sourceforge.fenixedu.domain.studentCurriculum.RootCurriculumGroup;
import net.sourceforge.fenixedu.domain.studentCurriculum.Substitution;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.CurriculumLineLocationBean;
import net.sourceforge.fenixedu.domain.studentCurriculum.curriculumLine.MoveCurriculumLinesBean;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.Checked;
import net.sourceforge.fenixedu.tools.enrollment.AreaType;
import net.sourceforge.fenixedu.util.PeriodState;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.collections.comparators.ReverseComparator;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

/**
 * @author David Santos in Jun 24, 2004
 */

public class StudentCurricularPlan extends StudentCurricularPlan_Base {

    static final public Comparator STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME = new ComparatorChain();
    static {
	((ComparatorChain) STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME).addComparator(new BeanComparator(
		"degreeCurricularPlan.degree.tipoCurso"));
	((ComparatorChain) STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME).addComparator(new BeanComparator(
		"degreeCurricularPlan.degree.name"));
    }

    static final public Comparator STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_DEGREE_NAME_AND_STUDENT_NUMBER_AND_NAME = new ComparatorChain();
    static {
	((ComparatorChain) STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_DEGREE_NAME_AND_STUDENT_NUMBER_AND_NAME)
		.addComparator(new BeanComparator("degreeCurricularPlan.degree.name"));
	((ComparatorChain) STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_DEGREE_NAME_AND_STUDENT_NUMBER_AND_NAME)
		.addComparator(new BeanComparator("student.number"));
	((ComparatorChain) STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_DEGREE_DEGREE_NAME_AND_STUDENT_NUMBER_AND_NAME)
		.addComparator(new BeanComparator("student.person.name"));
    }

    static final public Comparator<StudentCurricularPlan> STUDENT_CURRICULAR_PLAN_COMPARATOR_BY_START_DATE = new BeanComparator(
	    "startDateYearMonthDay");

    static final public Comparator<StudentCurricularPlan> DATE_COMPARATOR = new Comparator<StudentCurricularPlan>() {
	public int compare(StudentCurricularPlan leftState, StudentCurricularPlan rightState) {
	    int comparationResult = leftState.getStartDateYearMonthDay().compareTo(rightState.getStartDateYearMonthDay());
	    return (comparationResult == 0) ? leftState.getIdInternal().compareTo(rightState.getIdInternal()) : comparationResult;
	}
    };

    public StudentCurricularPlan() {
	super();
	setCurrentState(StudentCurricularPlanState.ACTIVE);
	setRootDomainObject(RootDomainObject.getInstance());
    }

    @Deprecated
    public StudentCurricularPlan(Registration registration, DegreeCurricularPlan degreeCurricularPlan, Branch branch,
	    YearMonthDay startDate, Double givenCredits, Specialization specialization) {

	this(registration, degreeCurricularPlan, startDate);

	setBranch(branch);
	setGivenCredits(givenCredits);
	setSpecialization(specialization);
    }

    public StudentCurricularPlan(Registration registration, DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate) {

	this();

	setDegreeCurricularPlan(degreeCurricularPlan);
	setStartDateYearMonthDay(startDate);
	setRegistration(registration);
	setWhenDateTime(new DateTime());
    }

    final public static StudentCurricularPlan createBolonhaStudentCurricularPlan(Registration registration,
	    DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate, ExecutionPeriod executionPeriod) {

	return createBolonhaStudentCurricularPlan(registration, degreeCurricularPlan, startDate, executionPeriod, null);
    }

    final public static StudentCurricularPlan createBolonhaStudentCurricularPlan(Registration registration,
	    DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate, ExecutionPeriod executionPeriod,
	    CycleType cycleType) {

	return new StudentCurricularPlan(registration, degreeCurricularPlan, startDate, executionPeriod, cycleType);
    }

    private StudentCurricularPlan(Registration registration, DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate,
	    ExecutionPeriod executionPeriod, CycleType cycleType) {

	this();
	init(registration, degreeCurricularPlan, startDate);
	createStudentCurriculumStructureFor(executionPeriod, cycleType);
    }

    private void createStudentCurriculumStructureFor(final ExecutionPeriod executionPeriod, CycleType cycleType) {
	if (getDegreeCurricularPlan().isBoxStructure()) {
	    new RootCurriculumGroup(this, getDegreeCurricularPlan().getRoot(), executionPeriod, cycleType);
	}
    }

    private void init(Registration registration, DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate) {

	checkParameters(registration, degreeCurricularPlan, startDate);

	setRegistration(registration);
	setDegreeCurricularPlan(degreeCurricularPlan);
	setStartDateYearMonthDay(startDate);
	setWhenDateTime(new DateTime());
	setGivenCredits(Double.valueOf(0));
    }

    private void checkParameters(Registration registration, DegreeCurricularPlan degreeCurricularPlan, YearMonthDay startDate) {

	if (registration == null) {
	    throw new DomainException("error.studentCurricularPlan.registration.cannot.be.null");
	}
	if (degreeCurricularPlan == null) {
	    throw new DomainException("error.studentCurricularPlan.degreeCurricularPlan.cannot.be.null");
	}
	if (startDate == null) {
	    throw new DomainException("error.studentCurricularPlan.startDate.cannot.be.null");
	}
    }

    public void delete() throws DomainException {

	checkRulesToDelete();

	removeDegreeCurricularPlan();
	removeBranch();
	removeEmployee();
	removeMasterDegreeThesis();

	for (; !getEnrolmentsSet().isEmpty(); getEnrolments().get(0).delete());

	if (getRoot() != null) {
	    getRoot().delete();
	}

	for (Iterator<NotNeedToEnrollInCurricularCourse> iter = getNotNeedToEnrollCurricularCoursesIterator(); iter.hasNext();) {
	    NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = iter.next();
	    iter.remove();
	    notNeedToEnrollInCurricularCourse.removeStudentCurricularPlan();
	    notNeedToEnrollInCurricularCourse.delete();
	}

	for (; !getCreditsInAnySecundaryAreas().isEmpty(); getCreditsInAnySecundaryAreas().get(0).delete());

	for (Iterator<CreditsInScientificArea> iter = getCreditsInScientificAreasIterator(); iter.hasNext();) {
	    CreditsInScientificArea creditsInScientificArea = iter.next();
	    iter.remove();
	    creditsInScientificArea.removeStudentCurricularPlan();
	    creditsInScientificArea.delete();
	}

	for (; hasAnyCredits(); getCredits().get(0).delete());
	for (; hasAnyTutorships(); getTutorships().get(0).delete());

	removeStudent();
	removeRootDomainObject();
	
	deleteDomainObject();
    }

    private void checkRulesToDelete() {
	if (hasAnyGratuityEvents()) {
	    throw new DomainException("error.StudentCurricularPlan.cannot.delete.because.already.has.gratuity.events");
	}

	if (hasAnyGratuitySituations()) {
	    throw new DomainException("error.StudentCurricularPlan.cannot.delete.because.already.has.gratuity.situations");
	}
    }

    final public String print() {
	if (hasRoot()) {
	    final StringBuilder result = new StringBuilder();
	    result.append("[SCP ").append(this.getIdInternal()).append("] ").append(this.getName()).append("\n");
	    result.append(getRoot().print(""));
	    return result.toString();
	} else {
	    return "";
	}
    }

    final public boolean isFirstCycle() {
	return getDegreeType().isFirstCycle();
    }

    final public boolean isSecondCycle() {
	return getDegreeType().isSecondCycle();
    }

    final public boolean hasConcludedCycle(CycleType cycleType, ExecutionYear executionYear) {
	return getRoot().hasConcludedCycle(cycleType, executionYear);
    }

    final public YearMonthDay getConclusionDate(final CycleType cycleType) {
	return getRoot().getConclusionDate(cycleType);
    }

    final public Curriculum getCurriculum(final ExecutionYear executionYear) {
	final RootCurriculumGroup rootCurriculumGroup = getRoot();
	if (rootCurriculumGroup == null) {
	    //System.out.println("[NO ROOT CURRICULUM GROUP!]" + "[STUDENT]" + getRegistration().getStudent().getNumber() + " [REGISTRATION]" + getRegistration().getNumber() + " [SCP NAME]" + getName());
	    return Curriculum.createEmpty(executionYear);
	} else {
	    return rootCurriculumGroup.getCurriculum(executionYear);
	}
    }

    final public boolean isActive() {
	return isLastStudentCurricularPlanFromRegistration() && getRegistration().isActive();
    }

    final public boolean isPast() {
	return getDegreeCurricularPlan().isPast();
    }

    public boolean hasIncompleteState() {
	return getCurrentState().equals(StudentCurricularPlanState.INCOMPLETE);
    }

    public boolean isTransition() {
	return getRegistration().isTransition();
    }

    final public boolean isBolonhaDegree() {
	return getDegreeCurricularPlan().isBolonhaDegree();
    }

    /**
     * Temporary method, after all degrees migration this is no longer necessary
     * 
     */
    final public boolean isBoxStructure() {
	return hasRoot();
    }

    final public boolean isEnrolable() {
	return isBoxStructure() && getRegistration().isActive() && getRegistration().getLastStudentCurricularPlan().equals(this);
    }

    final public Person getPerson() {
	return getRegistration().getPerson();
    }

    final public Degree getDegree() {
	return getDegreeCurricularPlan().getDegree();
    }

    final public DegreeType getDegreeType() {
	return getDegree().getDegreeType();
    }

    final public Integer getDegreeDuration() {
	return getDegreeCurricularPlan().getDegreeDuration();
    }

    final public boolean hasClassification() {
	return getClassification() != null && getClassification().doubleValue() != 0d;
    }

    @Override
    @Deprecated
    final public Registration getStudent() {
	return this.getRegistration();
    }

    @Override
    @Deprecated
    public void setStudent(final Registration registration) {
	this.setRegistration(registration);
    }

    public Registration getRegistration() {
	return super.getStudent();
    }

    public void setRegistration(final Registration registration) {
	super.setStudent(registration);
    }
    
    public boolean hasRegistration() {
	return super.hasStudent();
    }

    public Set<CurriculumLine> getAllCurriculumLines() {
	return isBoxStructure() ? getRoot().getAllCurriculumLines() : new HashSet<CurriculumLine>(super.getEnrolmentsSet());
    }

    @Override
    final public List<Enrolment> getEnrolments() {
	return hasRoot() ? getRoot().getEnrolments() : super.getEnrolments();
    }

    @Override
    final public Set<Enrolment> getEnrolmentsSet() {
	return hasRoot() ? getRoot().getEnrolmentsSet() : super.getEnrolmentsSet();
    }

    @Override
    final public boolean hasAnyEnrolments() {
	return hasRoot() ? getRoot().hasAnyEnrolments() : super.hasAnyEnrolments();
    }

    @Override
    final public boolean hasEnrolments(final Enrolment enrolment) {
	return hasRoot() ? getRoot().hasCurriculumModule(enrolment) : super.hasEnrolments(enrolment);
    }

    final public boolean hasEnrolments(final ExecutionYear executionYear) {
	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    final ExecutionPeriod executionPeriod = enrolment.getExecutionPeriod();
	    if (executionPeriod.getExecutionYear() == executionYear) {
		return true;
	    }
	}
	return false;
    }

    @Override
    final public int getEnrolmentsCount() {
	return hasRoot() ? getEnrolmentsSet().size() : super.getEnrolmentsCount();
    }

    final public int countCurrentEnrolments() {
	int result = 0;
	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    final ExecutionYear executionYear = enrolment.getExecutionPeriod().getExecutionYear();
	    if (executionYear.getState().equals(PeriodState.CURRENT) && enrolment.isEnroled()) {
		result++;
	    }
	}
	return result;
    }

    final public int getCountCurrentEnrolments() {
	return countCurrentEnrolments();
    }

    final public List<Enrolment> getEnrolments(final CurricularCourse curricularCourse) {
	final List<Enrolment> results = new ArrayList<Enrolment>();

	for (final Enrolment enrollment : this.getEnrolmentsSet()) {
	    if (enrollment.getCurricularCourse() == curricularCourse) {
		results.add(enrollment);
	    }
	}

	return results;
    }

    final public int countEnrolmentsByCurricularCourse(final CurricularCourse curricularCourse) {
	int count = 0;
	for (Enrolment enrolment : this.getEnrolmentsSet()) {
	    if (enrolment.getCurricularCourse() == curricularCourse) {
		count++;
	    }
	}
	return count;
    }

    final public int countEnrolmentsByCurricularCourse(final CurricularCourse curricularCourse,
	    final ExecutionPeriod untilExecutionPeriod) {
	int count = 0;
	for (Enrolment enrolment : getEnrolments(curricularCourse)) {
	    if (enrolment.getExecutionPeriod().isBeforeOrEquals(untilExecutionPeriod)) {
		count++;
	    }
	}
	return count;
    }

    final public List<Enrolment> getEnrolmentsByState(final EnrollmentState state) {
	List<Enrolment> results = new ArrayList<Enrolment>();
	for (Enrolment enrolment : this.getEnrolmentsSet()) {
	    if (enrolment.getEnrollmentState().equals(state)) {
		results.add(enrolment);
	    }
	}
	return results;
    }

    final public List<Enrolment> getEnrolmentsByExecutionPeriod(final ExecutionPeriod executionPeriod) {
	List<Enrolment> results = new ArrayList<Enrolment>();
	for (Enrolment enrolment : this.getEnrolmentsSet()) {
	    if (enrolment.getExecutionPeriod() == executionPeriod) {
		results.add(enrolment);
	    }
	}
	return results;
    }

    final protected Collection<Enrolment> getStudentEnrollmentsWithEnrolledState() {
	return (List) CollectionUtils.select(getEnrolmentsSet(), new Predicate() {
	    final public boolean evaluate(Object obj) {
		Enrolment enrollment = (Enrolment) obj;
		return enrollment.isEnroled() && !enrollment.isInvisible();
	    }
	});
    }

    final public int getNumberOfEnrolledCurricularCourses() {
	return getStudentEnrollmentsWithEnrolledState().size();
    }

    private Collection<Enrolment> getVisibleEnroledEnrolments(final ExecutionPeriod executionPeriod) {
	final Collection<Enrolment> result = new ArrayList<Enrolment>();

	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.isEnroled() && !enrolment.isInvisible()
		    && (executionPeriod == null || enrolment.isValid(executionPeriod))) {
		result.add(enrolment);
	    }
	}

	return result;
    }

    final public int countEnrolments(final ExecutionPeriod executionPeriod) {
	int numberEnrolments = 0;
	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.getExecutionPeriod() == executionPeriod) {
		numberEnrolments++;
	    }
	}
	return numberEnrolments;
    }

    final public boolean hasAnyEnrolmentForExecutionPeriod(final ExecutionPeriod executionPeriod) {
	for (final Enrolment enrolment : this.getEnrolmentsSet()) {
	    if (enrolment.getExecutionPeriod() == executionPeriod) {
		return true;
	    }
	}
	return false;
    }

    final public List<Enrolment> getEnrolmentsByExecutionYear(final ExecutionYear executionYear) {
	final List<Enrolment> result = new ArrayList<Enrolment>();
	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.getExecutionPeriod().getExecutionYear() == executionYear) {
		result.add(enrolment);
	    }
	}

	return result;
    }

    final public int countEnrolments(final ExecutionYear executionYear) {
	int numberEnrolments = 0;
	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.getExecutionPeriod().getExecutionYear() == executionYear) {
		numberEnrolments++;
	    }
	}
	return numberEnrolments;
    }

    final public boolean hasAnyEnrolmentForExecutionYear(final ExecutionYear executionYear) {
	for (final Enrolment enrolment : this.getEnrolmentsSet()) {
	    if (enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
		return true;
	    }
	}
	return false;
    }

    final public boolean hasAnyEnrolmentForCurrentExecutionYear() {
	return hasAnyEnrolmentForExecutionYear(ExecutionYear.readCurrentExecutionYear());
    }

    final public Collection<Enrolment> getLatestCurricularCoursesEnrolments(final ExecutionYear executionYear) {
	final Map<CurricularCourse, Enrolment> result = new HashMap<CurricularCourse, Enrolment>();

	for (final Enrolment enrolment : getEnrolmentsByExecutionYear(executionYear)) {
	    if (!result.containsKey(enrolment.getCurricularCourse())
		    || result.get(enrolment.getCurricularCourse()).isBefore(enrolment)) {
		result.put(enrolment.getCurricularCourse(), enrolment);
	    }
	}

	return result.values();
    }

    public void addApprovedEnrolments(final Collection<Enrolment> enrolments) {
	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (!enrolment.isInvisible() && enrolment.isApproved()) {
		enrolments.add(enrolment);
	    }
	}
    }

    public Set<Enrolment> getDismissalApprovedEnrolments() {
	Set<Enrolment> aprovedEnrolments = new HashSet<Enrolment>();
	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (!enrolment.isInvisible() && enrolment.isApproved()) {
		aprovedEnrolments.add(enrolment);
	    }
	}
	return aprovedEnrolments;
    }

    final public Collection<CurriculumLine> getApprovedCurriculumLines() {
	final Collection<CurriculumLine> result = new HashSet<CurriculumLine>();

	getRoot().addApprovedCurriculumLines(result);

	return result;
    }

    final public boolean hasAnyApprovedCurriculumLines() {
	return getRoot().hasAnyApprovedCurriculumLines();
    }

    final public List<Enrolment> getAprovedEnrolments() {
	final List<Enrolment> result = new ArrayList<Enrolment>();

	addApprovedEnrolments(result);

	return result;
    }

    public boolean hasAnyApprovedEnrolment() {
	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.isApproved()) {
		return true;
	    }
	}
	return false;
    }

    final public List<Enrolment> getAprovedEnrolmentsInExecutionPeriod(final ExecutionPeriod executionPeriod) {
	final List<Enrolment> result = new ArrayList<Enrolment>();

	for (final Enrolment enrolment : getEnrolmentsByExecutionPeriod(executionPeriod)) {
	    if (enrolment.isApproved()) {
		result.add(enrolment);
	    }
	}

	return result;
    }

    final public Collection<Enrolment> getPropaedeuticEnrolments() {
	final Collection<Enrolment> result = new ArrayList<Enrolment>();

	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.isPropaedeutic()) {
		result.add(enrolment);
	    }
	}

	return result;
    }

    final public Collection<Enrolment> getExtraCurricularEnrolments() {
	final Collection<Enrolment> result = new ArrayList<Enrolment>();

	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.isExtraCurricular()) {
		result.add(enrolment);
	    }
	}

	return result;
    }

    final public Collection<Enrolment> getDissertationEnrolments() {
	final Collection<Enrolment> result = new HashSet<Enrolment>();

	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.getCurricularCourse().isDissertation()) {
		result.add(enrolment);
	    }
	}

    for (Dismissal dismissal : getDismissals()) {
        for (IEnrolment enrolment : dismissal.getSourceIEnrolments()) {
            if (! enrolment.isEnrolment()) {
                continue;
            }
            
            Enrolment realEnrolment = (Enrolment) enrolment;
            if (realEnrolment.getCurricularCourse().isDissertation()) {
                result.add(realEnrolment);
            }
        }
    }
    
	return result;
    }

    final public Enrolment getLatestDissertationEnrolment() {
	final TreeSet<Enrolment> result = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_ID);
	result.addAll(getDissertationEnrolments());
	return result.isEmpty() ? null : result.last();
    }

    final public Enrolment getEnrolment(String executionYear, Integer semester, String code) {
	for (Enrolment enrolment : this.getEnrolmentsSet()) {
	    if (enrolment.getCurricularCourse().getCode().equals(code)
		    && enrolment.getExecutionPeriod().getSemester().equals(semester)
		    && enrolment.getExecutionPeriod().getExecutionYear().getYear().equals(executionYear)) {
		return enrolment;
	    }
	}
	return null;
    }

    final public Enrolment getEnrolment(ExecutionPeriod executionPeriod, String code) {
	for (Enrolment enrolment : this.getEnrolmentsSet()) {
	    if (enrolment.getCurricularCourse().getCode().equals(code) && enrolment.getExecutionPeriod() == executionPeriod) {
		return enrolment;
	    }
	}
	return null;
    }

    final public Enrolment findEnrolmentByEnrolmentID(final Integer enrolmentID) {
	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.getIdInternal().equals(enrolmentID)) {
		return enrolment;
	    }
	}
	return null;
    }

    final public Enrolment findEnrolmentFor(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	return isBoxStructure() ? getRoot().findEnrolmentFor(curricularCourse, executionPeriod)
		: getEnrolmentByCurricularCourseAndExecutionPeriod(curricularCourse, executionPeriod);
    }

    final public Enrolment getEnrolmentByCurricularCourseAndExecutionPeriod(final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod) {

	return (Enrolment) CollectionUtils.find(getEnrolmentsSet(), new Predicate() {
	    final public boolean evaluate(Object o) {
		Enrolment enrolment = (Enrolment) o;
		return (enrolment.getCurricularCourse().equals(curricularCourse))
			&& (enrolment.getExecutionPeriod().equals(executionPeriod));
	    }
	});
    }

    final public Set<ExecutionPeriod> getEnrolmentsExecutionPeriods() {
	final Set<ExecutionPeriod> result = new HashSet<ExecutionPeriod>();

	for (final Enrolment enrolment : this.getEnrolmentsSet()) {
	    result.add(enrolment.getExecutionPeriod());
	}

	return result;
    }

    final public Set<ExecutionYear> getEnrolmentsExecutionYears() {
	final Set<ExecutionYear> result = new TreeSet<ExecutionYear>(ExecutionYear.REVERSE_COMPARATOR_BY_YEAR);

	for (final Enrolment enrolment : this.getEnrolmentsSet()) {
	    result.add(enrolment.getExecutionPeriod().getExecutionYear());
	}

	return result;
    }

    final public ExecutionYear getStartExecutionYear() {
	return ExecutionYear.getExecutionYearByDate(getStartDateYearMonthDay());
    }

    public ExecutionPeriod getStartExecutionPeriod() {
	return getStartDateYearMonthDay() != null ? ExecutionPeriod.readByDateTime(getStartDateYearMonthDay()
		.toDateTimeAtMidnight()) : getFirstExecutionPeriod();
    }

    final public ExecutionPeriod getFirstExecutionPeriod() {
	ExecutionPeriod result = null;

	for (final Enrolment enrolment : this.getEnrolmentsSet()) {
	    if (result == null || result.isAfter(enrolment.getExecutionPeriod())) {
		result = enrolment.getExecutionPeriod();
	    }
	}

	return result;
    }

    public YearMonthDay getEndDate() {

	final StudentCurricularPlan nextStudentCurricularPlan = getNextStudentCurricularPlan();
	if (nextStudentCurricularPlan != null) {
	    return nextStudentCurricularPlan.getStartDateYearMonthDay().minusDays(1);
	} else if (!getRegistration().isActive()) {
	    return getRegistration().getActiveState().getStateDate().toYearMonthDay();
	}

	return null;
    }

    private StudentCurricularPlan getNextStudentCurricularPlan() {
	for (Iterator<StudentCurricularPlan> iter = getRegistration().getSortedStudentCurricularPlans().iterator(); iter
		.hasNext();) {
	    if (iter.next() == this) {
		return iter.hasNext() ? iter.next() : null;
	    }
	}
	return null;
    }

    public boolean isActive(ExecutionYear executionYear) {
	return !getStartDateYearMonthDay().isAfter(executionYear.getEndDateYearMonthDay())
		&& (getEndDate() == null || !getEndDate().isBefore(executionYear.getBeginDateYearMonthDay()));
    }

    final public ExecutionYear getLastExecutionYear() {
	ExecutionYear result = null;

	for (final Enrolment enrolment : this.getEnrolmentsSet()) {
	    if (result == null || result.isBefore(enrolment.getExecutionPeriod().getExecutionYear())) {
		result = enrolment.getExecutionPeriod().getExecutionYear();
	    }
	}

	return result;
    }

    protected Integer creditsInSecundaryArea;

    public Integer getCreditsInSecundaryArea() {
	// only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
	// return a value
	return new Integer(0);
    }

    public void setCreditsInSecundaryArea(Integer creditsInSecundaryArea) {
	// only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
	// set a value
    }

    protected Integer creditsInSpecializationArea;

    public Integer getCreditsInSpecializationArea() {
	// only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
	// return a value
	return new Integer(0);
    }

    public void setCreditsInSpecializationArea(Integer creditsInSpecializationArea) {
	// only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
	// set a value
    }

    public Branch getSecundaryBranch() {
	// only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
	// return a value
	return null;
    }

    public boolean hasSecundaryBranch() {
	return (getSecundaryBranch() != null);
    }

    public void setSecundaryBranch(Branch secundaryBranch) {
	// only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
	// set a value
    }

    final public Integer getSecundaryBranchKey() {
	// only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
	// return a value
	return null;
    }

    public void setSecundaryBranchKey(Integer secundaryBranchKey) {
	// only StudentCurricularPlanLEEC and StudentCurricularPlanLEIC should
	// set a value
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes (PUBLIC)
    // -------------------------------------------------------------

    public List getAllEnrollments() {
	List<Enrolment> allEnrollments = new ArrayList<Enrolment>();
	addNonInvisibleEnrolments(allEnrollments, getEnrolments());

	for (final StudentCurricularPlan studentCurricularPlan : getRegistration().getStudentCurricularPlans()) {
	    addNonInvisibleEnrolments(allEnrollments, studentCurricularPlan.getEnrolments());
	}

	return allEnrollments;
    }

    private void addNonInvisibleEnrolments(List<Enrolment> allEnrollments, List<Enrolment> enrollmentsToAdd) {
	for (Enrolment enrolment : enrollmentsToAdd) {
	    if (!enrolment.isInvisible()) {
		allEnrollments.add(enrolment);
	    }
	}
    }

    final public int getNumberOfStudentEnrollments() {
	return getAllEnrollments().size();
    }

    private List<Enrolment> getAllEnrollmentsExceptTheOnesWithEnrolledState() {
	final ExecutionPeriod actualExecutionPeriod = ExecutionPeriod.readActualExecutionPeriod();
	return (List) CollectionUtils.select(getAllEnrollments(), new Predicate() {
	    final public boolean evaluate(Object obj) {
		Enrolment enrollment = (Enrolment) obj;
		return !enrollment.isAnnulled()
			&& !(enrollment.isEnroled() && enrollment.getExecutionPeriod().equals(actualExecutionPeriod));
	    }
	});
    }

    public List<Enrolment> getStudentEnrollmentsWithApprovedState() {

	return (List) CollectionUtils.select(getAllEnrollments(), new Predicate() {
	    final public boolean evaluate(Object obj) {
		Enrolment enrollment = (Enrolment) obj;
		return enrollment.isEnrolmentStateApproved();
	    }
	});
    }

    final public int getNumberOfStudentEnrollmentsWithApprovedState() {

	return CollectionUtils.countMatches(getAllEnrollments(), new Predicate() {
	    final public boolean evaluate(Object obj) {
		Enrolment enrollment = (Enrolment) obj;
		return enrollment.isEnrolmentStateApproved();
	    }
	});
    }

    final public boolean isCurricularCourseApprovedInCurrentOrPreviousPeriod(final CurricularCourse course,
	    final ExecutionPeriod executionPeriod) {
	final List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();
	final List<CurricularCourse> approvedCurricularCourses = new ArrayList<CurricularCourse>();

	for (Iterator iter = studentApprovedEnrollments.iterator(); iter.hasNext();) {
	    Enrolment enrolment = (Enrolment) iter.next();
	    if (enrolment.getExecutionPeriod().compareTo(executionPeriod) <= 0) {
		approvedCurricularCourses.add(enrolment.getCurricularCourse());
	    }
	}

	return isApproved(course, approvedCurricularCourses);
    }

    final protected boolean isApproved(CurricularCourse curricularCourse, List<CurricularCourse> approvedCourses) {
	return hasEquivalenceIn(curricularCourse, approvedCourses) || hasEquivalenceInNotNeedToEnroll(curricularCourse);
    }

    final public boolean isApproved(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	return isBoxStructure() ? getRoot().isApproved(curricularCourse, executionPeriod)
		: isCurricularCourseApprovedInCurrentOrPreviousPeriod(curricularCourse, executionPeriod);
    }

    final public boolean isCurricularCourseApproved(CurricularCourse curricularCourse) {
	List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();

	List<CurricularCourse> result = (List<CurricularCourse>) CollectionUtils.collect(studentApprovedEnrollments,
		new Transformer() {
		    final public Object transform(Object obj) {
			Enrolment enrollment = (Enrolment) obj;

			return enrollment.getCurricularCourse();

		    }
		});

	return isApproved(curricularCourse, result);
    }

    final public boolean isApproved(final CurricularCourse curricularCourse) {
	return isBoxStructure() ? getRoot().isApproved(curricularCourse) : isCurricularCourseApproved(curricularCourse);
    }

    public int getNumberOfApprovedCurricularCourses() {
	int counter = 0;

	int size = getDegreeCurricularPlan().getCurricularCourses().size();
	for (int i = 0; i < size; i++) {
	    CurricularCourse curricularCourse = getDegreeCurricularPlan().getCurricularCourses().get(i);
	    if (isCurricularCourseApproved(curricularCourse)) {
		counter++;
	    }
	}

	return counter;
    }

    final public boolean isCurricularCourseNotExtraApprovedInCurrentOrPreviousPeriod(final CurricularCourse course,
	    final ExecutionPeriod executionPeriod) {
	final List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();
	final List<CurricularCourse> approvedCurricularCourses = new ArrayList<CurricularCourse>();

	for (Iterator iter = studentApprovedEnrollments.iterator(); iter.hasNext();) {
	    Enrolment enrolment = (Enrolment) iter.next();
	    if (!enrolment.isExtraCurricular() && enrolment.getExecutionPeriod().compareTo(executionPeriod) <= 0) {
		approvedCurricularCourses.add(enrolment.getCurricularCourse());
	    }
	}

	return isApproved(course, approvedCurricularCourses);
    }

    final public boolean isCurricularCourseApprovedWithoutEquivalencesInCurrentOrPreviousPeriod(final CurricularCourse course,
	    final ExecutionPeriod executionPeriod) {
	final List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();

	Enrolment enrolment = (Enrolment) CollectionUtils.find(studentApprovedEnrollments, new Predicate() {
	    final public boolean evaluate(Object arg0) {
		Enrolment enrolment = (Enrolment) arg0;
		if ((enrolment.getCurricularCourse().getIdInternal().equals(course.getIdInternal()))
			&& (enrolment.isApproved())
			&& (enrolment.getExecutionPeriod().compareTo(executionPeriod) <= 0)) {
		    return true;
		}
		return false;
	    }
	});

	if (enrolment != null)
	    return true;
	return false;
    }

    final public boolean isEquivalentAproved(CurricularCourse curricularCourse) {
	List studentApprovedEnrollments = getStudentEnrollmentsWithApprovedState();

	List<CurricularCourse> result = (List) CollectionUtils.collect(studentApprovedEnrollments, new Transformer() {
	    final public Object transform(Object obj) {
		Enrolment enrollment = (Enrolment) obj;

		return enrollment.getCurricularCourse();

	    }
	});

	return isThisCurricularCoursesInTheList(curricularCourse, result) || hasEquivalenceInNotNeedToEnroll(curricularCourse);
    }

    private boolean hasEquivalenceInNotNeedToEnroll(CurricularCourse curricularCourse) {

	if (notNeedToEnroll(curricularCourse)) {
	    return true;
	}

	for (CurricularCourseEquivalence equiv : curricularCourse.getCurricularCourseEquivalences()) {
	    if (allNotNeedToEnroll(equiv.getOldCurricularCourses())) {
		return true;
	    }
	}

	return false;
    }

    final public List<CurricularCourse2Enroll> getCurricularCoursesToEnroll(ExecutionPeriod executionPeriod)
	    throws FenixDomainException {

	calculateStudentAcumulatedEnrollments(executionPeriod);

	List<CurricularCourse2Enroll> setOfCurricularCoursesToEnroll = getCommonBranchAndStudentBranchesCourses(executionPeriod);

	initEctsCreditsToEnrol(setOfCurricularCoursesToEnroll, executionPeriod);

	setOfCurricularCoursesToEnroll = initAcumulatedEnrollments(setOfCurricularCoursesToEnroll);

	for (final IEnrollmentRule enrollmentRule : getListOfEnrollmentRules(executionPeriod)) {
	    setOfCurricularCoursesToEnroll = enrollmentRule.apply(setOfCurricularCoursesToEnroll);
	    if (setOfCurricularCoursesToEnroll.isEmpty()) {
		break;
	    }
	}

	return setOfCurricularCoursesToEnroll;
    }

    final public void initEctsCreditsToEnrol(List<CurricularCourse2Enroll> setOfCurricularCoursesToEnroll,
	    ExecutionPeriod executionPeriod) {
	for (CurricularCourse2Enroll curricularCourse2Enroll : setOfCurricularCoursesToEnroll) {
	    curricularCourse2Enroll.setEctsCredits(this.getAccumulatedEctsCredits(executionPeriod, curricularCourse2Enroll
		    .getCurricularCourse()));
	}
    }

    private boolean allNotNeedToEnroll(List<CurricularCourse> oldCurricularCourses) {
	for (CurricularCourse course : oldCurricularCourses) {
	    if (!notNeedToEnroll(course)) {
		return false;
	    }
	}
	return true;
    }

    final protected boolean hasEquivalenceIn(CurricularCourse curricularCourse, List<CurricularCourse> otherCourses) {
	if (otherCourses.isEmpty()) {
	    return false;
	}

	if (isThisCurricularCoursesInTheList(curricularCourse, otherCourses)) {
	    return true;
	}

	for (CurricularCourseEquivalence equiv : curricularCourse.getCurricularCourseEquivalences()) {
	    if (allCurricularCoursesInTheList(equiv.getOldCurricularCourses(), otherCourses)) {
		return true;
	    }
	}

	return false;
    }

    private boolean allCurricularCoursesInTheList(List<CurricularCourse> oldCurricularCourses, List<CurricularCourse> otherCourses) {
	for (CurricularCourse oldCurricularCourse : oldCurricularCourses) {
	    if (!isThisCurricularCoursesInTheList(oldCurricularCourse, otherCourses)
		    && !hasEquivalenceInNotNeedToEnroll(oldCurricularCourse)) {
		return false;
	    }
	}
	return true;
    }

    final public boolean isCurricularCourseEnrolled(CurricularCourse curricularCourse) {
	List result = (List) CollectionUtils.collect(getStudentEnrollmentsWithEnrolledState(), new Transformer() {
	    final public Object transform(Object obj) {
		Enrolment enrollment = (Enrolment) obj;
		return enrollment.getCurricularCourse();
	    }
	});

	return result.contains(curricularCourse);
    }

    final public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse) {
	return isBoxStructure() ? isEnroledInExecutionPeriod(curricularCourse, ExecutionPeriod.readActualExecutionPeriod())
		: isCurricularCourseEnrolled(curricularCourse);
    }

    final public boolean isCurricularCourseEnrolledInExecutionPeriod(CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod) {

	List<Enrolment> studentEnrolledEnrollments = getAllStudentEnrolledEnrollmentsInExecutionPeriod(executionPeriod);
	for (Enrolment enrolment : studentEnrolledEnrollments) {
	    if (enrolment.getCurricularCourse().equals(curricularCourse)) {
		return true;
	    }
	}
	return false;
    }

    final public boolean isEnroledInExecutionPeriod(final CurricularCourse curricularCourse, final ExecutionPeriod executionPeriod) {
	return isBoxStructure() ? getRoot().isEnroledInExecutionPeriod(curricularCourse, executionPeriod)
		: isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, executionPeriod);
    }

    final public Integer getCurricularCourseAcumulatedEnrollments(CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod) {

	String key = curricularCourse.getCurricularCourseUniqueKeyForEnrollment();

	Integer curricularCourseAcumulatedEnrolments = getAcumulatedEnrollmentsMap(executionPeriod).get(key);

	if (curricularCourseAcumulatedEnrolments == null) {
	    curricularCourseAcumulatedEnrolments = new Integer(0);
	}

	if (curricularCourseAcumulatedEnrolments.intValue() >= curricularCourse.getMinimumValueForAcumulatedEnrollments()
		.intValue()) {
	    curricularCourseAcumulatedEnrolments = curricularCourse.getMaximumValueForAcumulatedEnrollments();
	}

	if (curricularCourseAcumulatedEnrolments.intValue() == 0) {
	    curricularCourseAcumulatedEnrolments = curricularCourse.getMinimumValueForAcumulatedEnrollments();
	}

	return curricularCourseAcumulatedEnrolments;
    }

    final public Integer getCurricularCourseAcumulatedEnrollments(CurricularCourse curricularCourse) {
	return getCurricularCourseAcumulatedEnrollments(curricularCourse, ExecutionPeriod.readActualExecutionPeriod());
    }

    final public List<Enrolment> getAllStudentEnrolledEnrollmentsInExecutionPeriod(final ExecutionPeriod executionPeriod) {
	List<Enrolment> result = new ArrayList<Enrolment>();
	for (Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.getExecutionPeriod().equals(executionPeriod) && enrolment.isEnroled() && !enrolment.isInvisible()) {
		result.add(enrolment);
	    }
	}

	initEctsCredits(executionPeriod, result);
	return result;
    }

    private void initEctsCredits(ExecutionPeriod executionPeriod, List<Enrolment> enrolments) {
	for (final Enrolment enrolment : enrolments) {
	    enrolment.setAccumulatedEctsCredits(this.getAccumulatedEctsCredits(executionPeriod, enrolment.getCurricularCourse()));
	}
    }

    final public List<Enrolment> getAllStudentEnrollmentsInExecutionPeriod(final ExecutionPeriod executionPeriod) {
	calculateStudentAcumulatedEnrollments(executionPeriod);
	return initAcumulatedEnrollments((List) CollectionUtils.select(getEnrolmentsSet(), new Predicate() {
	    final public boolean evaluate(Object arg0) {

		return ((Enrolment) arg0).getExecutionPeriod().equals(executionPeriod);
	    }
	}));
    }

    final public List getStudentTemporarilyEnrolledEnrollments() {

	return initAcumulatedEnrollments((List) CollectionUtils.select(getEnrolmentsSet(), new Predicate() {
	    final public boolean evaluate(Object obj) {
		Enrolment enrollment = (Enrolment) obj;
		return (enrollment.isEnroled() && enrollment.isTemporary());
	    }
	}));
    }

    public CurricularCourseEnrollmentType getCurricularCourseEnrollmentType(CurricularCourse curricularCourse,
	    ExecutionPeriod currentExecutionPeriod) {

	if (getBranch() == null) {
	    if (!curricularCourse.hasActiveScopeInGivenSemester(currentExecutionPeriod.getSemester())) {
		return CurricularCourseEnrollmentType.NOT_ALLOWED;
	    }
	} else {
	    if (!curricularCourse.hasActiveScopeInGivenSemesterForCommonAndGivenBranch(currentExecutionPeriod.getSemester(),
		    getBranch())) {
		return CurricularCourseEnrollmentType.NOT_ALLOWED;
	    }
	}

	if (isCurricularCourseApproved(curricularCourse)) {
	    return CurricularCourseEnrollmentType.NOT_ALLOWED;
	}

	List enrollmentsWithEnrolledStateInCurrentExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(currentExecutionPeriod);

	for (int i = 0; i < enrollmentsWithEnrolledStateInCurrentExecutionPeriod.size(); i++) {
	    Enrolment enrollment = (Enrolment) enrollmentsWithEnrolledStateInCurrentExecutionPeriod.get(i);
	    if (curricularCourse.equals(enrollment.getCurricularCourse())) {
		return CurricularCourseEnrollmentType.NOT_ALLOWED;
	    }
	}

	List enrollmentsWithEnrolledStateInPreviousExecutionPeriod = getAllStudentEnrolledEnrollmentsInExecutionPeriod(currentExecutionPeriod
		.getPreviousExecutionPeriod());

	for (int i = 0; i < enrollmentsWithEnrolledStateInPreviousExecutionPeriod.size(); i++) {
	    Enrolment enrollment = (Enrolment) enrollmentsWithEnrolledStateInPreviousExecutionPeriod.get(i);
	    if (curricularCourse.equals(enrollment.getCurricularCourse())) {
		return CurricularCourseEnrollmentType.TEMPORARY;
	    }
	}

	CurricularCourseEnrollmentType courseEnrollmentType = CurricularCourseEnrollmentType.DEFINITIVE;
	for (CurricularCourseEquivalence curricularCourseEquivalence : curricularCourse.getCurricularCourseEquivalencesSet()) {
	    for (CurricularCourse eqCurricularCourse : curricularCourseEquivalence.getOldCurricularCoursesSet()) {
		if (this.isEquivalentAproved(eqCurricularCourse)) {
		    courseEnrollmentType = courseEnrollmentType.and(CurricularCourseEnrollmentType.DEFINITIVE);
		} else if (hasEnrolledStateInPreviousExecutionPerdiod(eqCurricularCourse,
			enrollmentsWithEnrolledStateInPreviousExecutionPeriod)) {
		    courseEnrollmentType = courseEnrollmentType.and(CurricularCourseEnrollmentType.TEMPORARY);
		} else {
		    courseEnrollmentType = courseEnrollmentType.and(CurricularCourseEnrollmentType.NOT_ALLOWED);
		}
	    }
	    if (courseEnrollmentType.equals(CurricularCourseEnrollmentType.TEMPORARY)) {
		return CurricularCourseEnrollmentType.TEMPORARY;
	    }
	}

	if (isMathematicalCourse(curricularCourse)) {
	    if (hasCurricularCourseEquivalenceIn(curricularCourse, enrollmentsWithEnrolledStateInPreviousExecutionPeriod))
		return CurricularCourseEnrollmentType.TEMPORARY;
	}

	return CurricularCourseEnrollmentType.DEFINITIVE;
    }

    final public boolean hasEnrolledStateInPreviousExecutionPerdiod(CurricularCourse curricularCourse,
	    List<Enrolment> enrollmentsWithEnrolledStateInPreviousExecutionPeriod) {
	for (Enrolment enrolment : enrollmentsWithEnrolledStateInPreviousExecutionPeriod) {
	    if (enrolment.getCurricularCourse().equals(curricularCourse)) {
		return true;
	    }
	}
	return false;
    }

    final protected boolean hasActiveScopeInGivenSemester(CurricularCourse curricularCourse,
	    ExecutionPeriod currentExecutionPeriod) {

	boolean result = true;
	List<CurricularCourse> curricularCoursesFromCommonBranches = new ArrayList<CurricularCourse>();
	List<Branch> commonAreas = getDegreeCurricularPlan().getCommonAreas();
	int commonAreasSize = commonAreas.size();

	for (int i = 0; i < commonAreasSize; i++) {
	    Branch area = (Branch) commonAreas.get(i);
	    curricularCoursesFromCommonBranches.addAll(getDegreeCurricularPlan()
		    .getCurricularCoursesFromArea(area, AreaType.BASE));
	}
	if (!curricularCoursesFromCommonBranches.contains(curricularCourse)) {

	    if (this.getBranch() != null && this.getSecundaryBranch() != null) {
		if (!curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(currentExecutionPeriod.getSemester(), this
			.getBranch())
			&& !curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(currentExecutionPeriod.getSemester(),
				this.getSecundaryBranch())) {
		    result = false;
		}
	    } else if (getBranch() != null && getSecundaryBranch() == null) {
		if (!curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(currentExecutionPeriod.getSemester(), this
			.getBranch())) {
		    result = false;
		}
	    } else if (getBranch() == null && getSecundaryBranch() != null) {
		if (!curricularCourse.hasActiveScopeInGivenSemesterForGivenBranch(currentExecutionPeriod.getSemester(), this
			.getSecundaryBranch())) {
		    result = false;
		}
	    } else if (getBranch() == null) {
		if (!curricularCourse.hasActiveScopeInGivenSemester(currentExecutionPeriod.getSemester())) {
		    result = false;
		}
	    }
	} else {
	    result = curricularCourse.hasActiveScopeInGivenSemester(currentExecutionPeriod.getSemester());
	}
	return result;
    }

    public boolean areNewAreasCompatible(Branch specializationArea, Branch secundaryArea)
	    throws BothAreasAreTheSameServiceException, InvalidArgumentsServiceException {

	return true;
    }

    public boolean getCanChangeSpecializationArea() {
	if (getBranch() != null) {
	    return false;
	}
	return true;
    }

    final public double getAccumulatedEctsCredits(final ExecutionPeriod executionPeriod) {
	double result = 0.0;

	for (final Enrolment enrolment : getVisibleEnroledEnrolments(executionPeriod)) {
	    result += getAccumulatedEctsCredits(executionPeriod, enrolment.getCurricularCourse());
	}

	return result;
    }

    final public double getAccumulatedEctsCredits(final ExecutionPeriod executionPeriod, final CurricularCourse curricularCourse) {
	if (curricularCourse.isBolonhaDegree()) {
	    return isAccumulated(executionPeriod, curricularCourse) ? MaximumNumberOfCreditsForEnrolmentPeriod
		    .getAccumulatedEctsCredits(curricularCourse, executionPeriod) : curricularCourse.getEctsCredits(
		    executionPeriod.getSemester(), executionPeriod);
	} else {
	    return getAccumulatedEctsCreditsForOldCurricularCourses(curricularCourse, executionPeriod);
	}
    }

    private double getAccumulatedEctsCreditsForOldCurricularCourses(final CurricularCourse curricularCourse,
	    ExecutionPeriod executionPeriod) {
	Double factor;
	Integer curricularCourseAcumulatedEnrolments = getAcumulatedEnrollmentsMap(executionPeriod).get(
		curricularCourse.getCurricularCourseUniqueKeyForEnrollment());
	if (curricularCourseAcumulatedEnrolments == null || curricularCourseAcumulatedEnrolments.intValue() == 0) {
	    factor = 1.0;
	} else {
	    factor = 0.75;
	}
	return curricularCourse.getEctsCredits() * factor;
    }

    private boolean isAccumulated(final ExecutionPeriod executionPeriod, final CurricularCourse curricularCourse) {
	if (curricularCourse.isBolonhaDegree()) {
	    return getPreviouslyEnroledCurricularCourses(executionPeriod).contains(curricularCourse);
	} else {
	    Integer curricularCourseAcumulatedEnrolments = getAcumulatedEnrollmentsMap(executionPeriod).get(
		    curricularCourse.getCurricularCourseUniqueKeyForEnrollment());
	    return curricularCourseAcumulatedEnrolments != null && curricularCourseAcumulatedEnrolments.intValue() != 0;
	}
    }

    private Map<ExecutionPeriod, Collection<CurricularCourse>> previouslyEnroledCurricularCoursesByExecutionPeriod;

    private Collection<CurricularCourse> getPreviouslyEnroledCurricularCourses(final ExecutionPeriod executionPeriod) {
	if (previouslyEnroledCurricularCoursesByExecutionPeriod == null) {
	    previouslyEnroledCurricularCoursesByExecutionPeriod = new HashMap<ExecutionPeriod, Collection<CurricularCourse>>();
	}

	Collection<CurricularCourse> previouslyEnroledCurricularCourses = previouslyEnroledCurricularCoursesByExecutionPeriod
		.get(executionPeriod);
	if (previouslyEnroledCurricularCourses == null && this.isBolonhaDegree()) {
	    previouslyEnroledCurricularCourses = new HashSet<CurricularCourse>();
	    previouslyEnroledCurricularCoursesByExecutionPeriod.put(executionPeriod, previouslyEnroledCurricularCourses);

	    for (final Enrolment enrolment : getEnrolmentsSet()) {
		if (!enrolment.isAnnulled() && enrolment.getExecutionPeriod().isBefore(executionPeriod)) {
		    previouslyEnroledCurricularCourses.add(enrolment.getCurricularCourse());
		}
	    }
	}

	return previouslyEnroledCurricularCourses;
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes (PUBLIC)
    // -------------------------------------------------------------

    final public double getEctsCredits(final ExecutionPeriod executionPeriod) {
	double result = 0.0;

	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.isValid(executionPeriod)) {
		result += enrolment.getEctsCredits();
	    }
	}

	return result;
    }

    final public double getDismissalsEctsCredits() {
	double result = 0.0;

	for (final Dismissal dismissal : getDismissals()) {
	    result += dismissal.getEctsCredits();
	}

	return result;
    }

    // -------------------------------------------------------------
    // BEGIN: Only for enrollment purposes (PROTECTED)
    // -------------------------------------------------------------

    private Map<ExecutionPeriod, Map<String, Integer>> acumulatedEnrollments;

    private void calculateStudentAcumulatedEnrollments(ExecutionPeriod executionPeriod) {
	if (!this.isBolonhaDegree()) {
	    if (this.acumulatedEnrollments == null) {
		this.acumulatedEnrollments = new HashMap<ExecutionPeriod, Map<String, Integer>>();
	    }

	    if (this.acumulatedEnrollments.get(executionPeriod) == null) {
		List<String> curricularCourses = new ArrayList<String>();
		for (Enrolment enrolment : this.getEnrolmentsSet()) {
		    if (!enrolment.isAnnulled() && enrolment.getExecutionPeriod().isBefore(executionPeriod)) {
			curricularCourses.add(enrolment.getCurricularCourse().getCurricularCourseUniqueKeyForEnrollment());
		    }
		}

		Map<String, Integer> map = CollectionUtils.getCardinalityMap(curricularCourses);
		this.acumulatedEnrollments.put(executionPeriod, map);
	    }
	}
    }

    final protected CurricularCourse2Enroll transformToCurricularCourse2Enroll(CurricularCourse curricularCourse,
	    ExecutionPeriod currentExecutionPeriod) {

	return new CurricularCourse2Enroll(curricularCourse, getCurricularCourseEnrollmentType(curricularCourse,
		currentExecutionPeriod), Boolean.FALSE, curricularCourse.getCurricularYearByBranchAndSemester(this.getBranch(),
		currentExecutionPeriod.getSemester()));
    }

    private CurricularCourse2Enroll transformToCurricularCourse2Enroll(CurricularCourse curricularCourse,
	    ExecutionPeriod currentExecutionPeriod, CurricularCourseEnrollmentType curricularCourseEnrollmentType) {

	return new CurricularCourse2Enroll(curricularCourse, curricularCourseEnrollmentType, Boolean.FALSE, curricularCourse
		.getCurricularYearByBranchAndSemester(this.getBranch(), currentExecutionPeriod.getSemester()));
    }

    final public List initAcumulatedEnrollments(List elements) {
	if (this.acumulatedEnrollments != null) {
	    List result = new ArrayList();
	    int size = elements.size();

	    for (int i = 0; i < size; i++) {
		try {
		    Enrolment enrollment = (Enrolment) elements.get(i);
		    enrollment.setAccumulatedWeight(getCurricularCourseAcumulatedEnrollments(enrollment.getCurricularCourse()));
		    result.add(enrollment);
		} catch (ClassCastException e) {
		    // FIXME shouldn't this be done in a clearer way?...

		    CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) elements.get(i);
		    curricularCourse2Enroll.setAccumulatedWeight(getCurricularCourseAcumulatedEnrollments(curricularCourse2Enroll
			    .getCurricularCourse()));
		    result.add(curricularCourse2Enroll);
		    // FIXME is this correct? adding a
		    // CurricularCourse2Enroll
		    // to a list of Enrolments?
		}
	    }

	    return result;
	}
	return elements;
    }

    private Set getCurricularCoursesInCurricularCourseEquivalences(final CurricularCourse curricularCourse) {

	final Set<CurricularCourse> curricularCoursesEquivalent = new HashSet<CurricularCourse>();
	final List<CurricularCourse> sameCompetenceCurricularCourses;

	if (curricularCourse.getCompetenceCourse() == null) {
	    sameCompetenceCurricularCourses = new ArrayList<CurricularCourse>();
	    sameCompetenceCurricularCourses.add(curricularCourse);
	} else {
	    sameCompetenceCurricularCourses = new ArrayList<CurricularCourse>();
	    for (final CurricularCourse course : curricularCourse.getCompetenceCourse().getAssociatedCurricularCourses()) {
		if (!course.isBolonhaDegree()) {
		    sameCompetenceCurricularCourses.add(course);
		}
	    }
	}

	for (CurricularCourse course : sameCompetenceCurricularCourses) {
	    for (CurricularCourseEquivalence curricularCourseEquivalence : course.getOldCurricularCourseEquivalences()) {
		curricularCoursesEquivalent.add(curricularCourseEquivalence.getEquivalentCurricularCourse());
	    }
	}

	return curricularCoursesEquivalent;
    }

    private boolean isThisCurricularCoursesInTheList(final CurricularCourse curricularCourse,
	    List<CurricularCourse> curricularCourses) {
	for (CurricularCourse otherCourse : curricularCourses) {
	    if ((curricularCourse == otherCourse) || haveSameCompetence(curricularCourse, otherCourse)) {
		return true;
	    }
	}
	return false;
    }

    final public NotNeedToEnrollInCurricularCourse findNotNeddToEnrol(final CurricularCourse curricularCourse) {
	for (NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse : getNotNeedToEnrollCurricularCoursesSet()) {
	    final CurricularCourse otherCourse = notNeedToEnrollInCurricularCourse.getCurricularCourse();
	    if ((curricularCourse == otherCourse) || haveSameCompetence(curricularCourse, otherCourse)) {
		return notNeedToEnrollInCurricularCourse;
	    }
	}
	return null;
    }

    final public boolean notNeedToEnroll(final CurricularCourse curricularCourse) {
	return findNotNeddToEnrol(curricularCourse) != null;
    }

    private boolean haveSameCompetence(CurricularCourse course1, CurricularCourse course2) {
	CompetenceCourse comp1 = course1.getCompetenceCourse();
	CompetenceCourse comp2 = course2.getCompetenceCourse();
	return (comp1 != null) && (comp1 == comp2);
    }

    private Map<String, Integer> getAcumulatedEnrollmentsMap(ExecutionPeriod executionPeriod) {
	if (!this.isBolonhaDegree()) {
	    if (this.acumulatedEnrollments == null) {
		calculateStudentAcumulatedEnrollments(executionPeriod);
	    } else {
		if (this.acumulatedEnrollments.get(executionPeriod) == null) {
		    calculateStudentAcumulatedEnrollments(executionPeriod);
		}
	    }
	    return this.acumulatedEnrollments.get(executionPeriod);
	}
	return null;
    }

    private List<IEnrollmentRule> getListOfEnrollmentRules(ExecutionPeriod executionPeriod) {
	return getDegreeCurricularPlan().getListOfEnrollmentRules(this, executionPeriod);
    }

    private List<CurricularCourse> getStudentNotNeedToEnrollCurricularCourses() {
	return (List<CurricularCourse>) CollectionUtils.collect(getNotNeedToEnrollCurricularCourses(), new Transformer() {
	    final public Object transform(Object obj) {
		NotNeedToEnrollInCurricularCourse notNeedToEnrollInCurricularCourse = (NotNeedToEnrollInCurricularCourse) obj;
		return notNeedToEnrollInCurricularCourse.getCurricularCourse();
	    }
	});
    }

    protected List<CurricularCourse2Enroll> getCommonBranchAndStudentBranchesCourses(ExecutionPeriod executionPeriod) {
	Set<CurricularCourse> curricularCourses = new HashSet<CurricularCourse>();
	DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
	List commonAreas = degreeCurricularPlan.getCommonAreas();
	int commonAreasSize = commonAreas.size();

	for (int i = 0; i < commonAreasSize; i++) {
	    Branch area = (Branch) commonAreas.get(i);
	    curricularCourses.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(area, AreaType.BASE));
	}

	if (getBranch() != null) {
	    curricularCourses.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(getBranch(), AreaType.SPECIALIZATION));
	}

	if (getSecundaryBranch() != null) {
	    curricularCourses.addAll(degreeCurricularPlan.getCurricularCoursesFromArea(getSecundaryBranch(), AreaType.SECONDARY));
	}

	curricularCourses.addAll(degreeCurricularPlan.getTFCs());

	List<CurricularCourse2Enroll> result = new ArrayList<CurricularCourse2Enroll>();
	for (final CurricularCourse curricularCourse : curricularCourses) {
	    if (curricularCourse.getEnrollmentAllowed().booleanValue()) {
		final CurricularCourseEnrollmentType curricularCourseEnrollmentType = getCurricularCourseEnrollmentType(
			curricularCourse, executionPeriod);
		if (curricularCourseEnrollmentType != CurricularCourseEnrollmentType.NOT_ALLOWED) {

		    result.add(transformToCurricularCourse2Enroll(curricularCourse, executionPeriod,
			    curricularCourseEnrollmentType));
		}
	    }
	}

	markOptionalCurricularCourses(result, executionPeriod);
	return result;
    }

    private void markOptionalCurricularCourses(List result, ExecutionPeriod executionPeriod) {

	List allOptionalCurricularCourseGroups = getDegreeCurricularPlan().getAllOptionalCurricularCourseGroups();

	List<CurricularCourse> curricularCoursesToRemove = new ArrayList<CurricularCourse>();
	List<CurricularCourse> curricularCoursesToKeep = new ArrayList<CurricularCourse>();

	int size = allOptionalCurricularCourseGroups.size();
	for (int i = 0; i < size; i++) {

	    CurricularCourseGroup optionalCurricularCourseGroup = (CurricularCourseGroup) allOptionalCurricularCourseGroups
		    .get(i);

	    if (getBranch() != null && getSecundaryBranch() != null) {
		if (optionalCurricularCourseGroup.getBranch().equals(getBranch())
			|| optionalCurricularCourseGroup.getBranch().equals(getSecundaryBranch())) {
		    selectOptionalCoursesToBeRemoved(curricularCoursesToRemove, curricularCoursesToKeep,
			    optionalCurricularCourseGroup, executionPeriod);
		}
	    } else if (getBranch() != null && getSecundaryBranch() == null) {
		if (optionalCurricularCourseGroup.getBranch().equals(getBranch())) {
		    selectOptionalCoursesToBeRemoved(curricularCoursesToRemove, curricularCoursesToKeep,
			    optionalCurricularCourseGroup, executionPeriod);
		}
	    } else if (getBranch() == null && getSecundaryBranch() != null) {
		if (optionalCurricularCourseGroup.getBranch().equals(getSecundaryBranch())) {
		    selectOptionalCoursesToBeRemoved(curricularCoursesToRemove, curricularCoursesToKeep,
			    optionalCurricularCourseGroup, executionPeriod);
		}
	    } else if (getBranch() == null) {
		if (optionalCurricularCourseGroup.getBranch().getBranchType().equals(BranchType.COMNBR)) {
		    selectOptionalCoursesToBeRemoved(curricularCoursesToRemove, curricularCoursesToKeep,
			    optionalCurricularCourseGroup, executionPeriod);
		}
	    }

	}

	Iterator iter = result.iterator();
	while (iter.hasNext()) {
	    CurricularCourse2Enroll curricularCourse2Enroll = (CurricularCourse2Enroll) iter.next();

	    if (curricularCoursesToRemove.contains(curricularCourse2Enroll.getCurricularCourse())
		    && !curricularCoursesToKeep.contains(curricularCourse2Enroll.getCurricularCourse())) {
		iter.remove();
		// result.remove(curricularCourse2Enroll);
	    } else if (curricularCoursesToKeep.contains(curricularCourse2Enroll.getCurricularCourse())) {
		curricularCourse2Enroll.setOptionalCurricularCourse(Boolean.TRUE);
	    }
	}
    }

    protected void selectOptionalCoursesToBeRemoved(List<CurricularCourse> curricularCoursesToRemove,
	    List<CurricularCourse> curricularCoursesToKeep, CurricularCourseGroup optionalCurricularCourseGroup,
	    ExecutionPeriod executionPeriod) {
	int count = 0;

	int size2 = optionalCurricularCourseGroup.getCurricularCourses().size();
	for (int j = 0; j < size2; j++) {
	    CurricularCourse curricularCourse = optionalCurricularCourseGroup.getCurricularCourses().get(j);
	    if (isCurricularCourseEnrolledInExecutionPeriod(curricularCourse, executionPeriod)
		    || isCurricularCourseApproved(curricularCourse)) {
		count++;
	    }
	}

	if (count >= optionalCurricularCourseGroup.getMaximumNumberOfOptionalCourses().intValue()) {
	    curricularCoursesToRemove.addAll(optionalCurricularCourseGroup.getCurricularCourses());
	} else {
	    curricularCoursesToKeep.addAll(optionalCurricularCourseGroup.getCurricularCourses());
	}
    }

    final protected boolean hasCurricularCourseEquivalenceIn(CurricularCourse curricularCourse, List curricularCoursesEnrollments) {

	int size = curricularCoursesEnrollments.size();
	for (int i = 0; i < size; i++) {
	    CurricularCourse tempCurricularCourse = ((Enrolment) curricularCoursesEnrollments.get(i)).getCurricularCourse();
	    Set curricularCourseEquivalences = getCurricularCoursesInCurricularCourseEquivalences(tempCurricularCourse);
	    if (curricularCourseEquivalences.contains(curricularCourse)) {
		return true;
	    }
	}

	List<CurricularCourse> studentNotNeedToEnrollCourses = getStudentNotNeedToEnrollCurricularCourses();

	if (isThisCurricularCoursesInTheList(curricularCourse, studentNotNeedToEnrollCourses)) {
	    return true;
	}

	size = studentNotNeedToEnrollCourses.size();
	for (int i = 0; i < size; i++) {
	    CurricularCourse ccNotNeedToDo = (CurricularCourse) studentNotNeedToEnrollCourses.get(i);
	    Set curricularCourseEquivalences = getCurricularCoursesInCurricularCourseEquivalences(ccNotNeedToDo);
	    if (curricularCourseEquivalences.contains(curricularCourse)) {
		return true;
	    }
	}

	return false;
    }

    final protected boolean isMathematicalCourse(CurricularCourse curricularCourse) {
	String code = curricularCourse.getCode();

	return code.equals("QN") || code.equals("PY") || code.equals("P5") || code.equals("UN") || code.equals("U8")
		|| code.equals("AZ2") || code.equals("AZ3") || code.equals("AZ4") || code.equals("AZ5") || code.equals("AZ6");
    }

    // -------------------------------------------------------------
    // END: Only for enrollment purposes (PROTECTED)
    // -------------------------------------------------------------

    final public void setStudentAreasWithoutRestrictions(Branch specializationArea, Branch secundaryArea) throws DomainException {

	if (specializationArea != null && secundaryArea != null && specializationArea.equals(secundaryArea))
	    throw new DomainException("error.student.curricular.plan.areas.conflict");

	setBranch(specializationArea);
	setSecundaryBranch(secundaryArea);
    }

    final public void setStudentAreas(Branch specializationArea, Branch secundaryArea) throws NotAuthorizedBranchChangeException,
	    BothAreasAreTheSameServiceException, InvalidArgumentsServiceException, DomainException {

	if (!getCanChangeSpecializationArea()) {
	    throw new NotAuthorizedBranchChangeException();
	}

	if (areNewAreasCompatible(specializationArea, secundaryArea)) {
	    setStudentAreasWithoutRestrictions(specializationArea, secundaryArea);
	}

	else {
	    throw new DomainException("error.student.curricular.plan.areas.conflict");
	}
    }

    final public GratuitySituation getGratuitySituationByGratuityValues(final GratuityValues gratuityValues) {

	return (GratuitySituation) CollectionUtils.find(getGratuitySituations(), new Predicate() {
	    final public boolean evaluate(Object arg0) {
		GratuitySituation gratuitySituation = (GratuitySituation) arg0;
		return gratuitySituation.getGratuityValues().equals(gratuityValues);
	    }
	});

    }

    final public GratuitySituation getGratuitySituationByGratuityValuesAndGratuitySituationType(
	    final GratuitySituationType gratuitySituationType, final GratuityValues gratuityValues) {

	GratuitySituation gratuitySituation = this.getGratuitySituationByGratuityValues(gratuityValues);
	if (gratuitySituation != null
		&& (gratuitySituationType == null || ((gratuitySituationType.equals(GratuitySituationType.CREDITOR) && gratuitySituation
			.getRemainingValue() < 0.0)
			|| (gratuitySituationType.equals(GratuitySituationType.DEBTOR) && gratuitySituation.getRemainingValue() > 0.0) || (gratuitySituationType
			.equals(GratuitySituationType.REGULARIZED) && gratuitySituation.getRemainingValue() == 0.0)))) {
	    return gratuitySituation;
	}
	return null;
    }

    final public GratuityEvent getGratuityEvent(final ExecutionYear executionYear) {
	for (final GratuityEvent gratuityEvent : getGratuityEvents()) {
	    if (!gratuityEvent.isCancelled() && gratuityEvent.getExecutionYear() == executionYear) {
		return gratuityEvent;
	    }
	}

	return null;
    }

    final public boolean hasGratuityEvent(final ExecutionYear executionYear) {
	return getGratuityEvent(executionYear) != null;
    }

    final public Set<GratuityEvent> getNotPayedGratuityEvents() {
	final Set<GratuityEvent> result = new HashSet<GratuityEvent>();

	for (final GratuityEvent gratuityEvent : getGratuityEvents()) {
	    if (gratuityEvent.isInDebt()) {
		result.add(gratuityEvent);
	    }
	}

	return result;
    }

    final public boolean hasAnyNotPayedGratuityEvents() {
	for (final GratuityEvent gratuityEvent : getGratuityEvents()) {
	    if (gratuityEvent.isInDebt()) {
		return true;
	    }
	}

	return false;
    }

    final public boolean hasAnyNotPayedGratuityEventsForPreviousYears() {
	final ExecutionYear currentExecutionYear = ExecutionYear.readCurrentExecutionYear();

	for (final GratuityEvent gratuityEvent : getGratuityEvents()) {
	    if (gratuityEvent.getExecutionYear() != currentExecutionYear && gratuityEvent.isInDebt()) {
		return true;
	    }
	}

	return false;
    }

    final public ImprovementOfApprovedEnrolmentEvent getNotPayedImprovementOfApprovedEnrolmentEvent() {
	return getPerson().getNotPayedImprovementOfApprovedEnrolmentEvent();
    }

    final public MasterDegreeProofVersion readActiveMasterDegreeProofVersion() {
	MasterDegreeThesis masterDegreeThesis = this.getMasterDegreeThesis();
	if (masterDegreeThesis != null) {
	    for (MasterDegreeProofVersion masterDegreeProofVersion : masterDegreeThesis.getMasterDegreeProofVersions()) {
		if (masterDegreeProofVersion.getCurrentState().getState().equals(State.ACTIVE)) {
		    return masterDegreeProofVersion;
		}
	    }
	}
	return null;
    }

    final public List<MasterDegreeProofVersion> readNotActiveMasterDegreeProofVersions() {
	MasterDegreeThesis masterDegreeThesis = this.getMasterDegreeThesis();
	List<MasterDegreeProofVersion> masterDegreeProofVersions = new ArrayList<MasterDegreeProofVersion>();
	if (masterDegreeThesis != null) {
	    for (MasterDegreeProofVersion masterDegreeProofVersion : masterDegreeThesis.getMasterDegreeProofVersions()) {
		if (!masterDegreeProofVersion.getCurrentState().getState().equals(State.ACTIVE)) {
		    masterDegreeProofVersions.add(masterDegreeProofVersion);
		}
	    }
	}
	Collections.sort(masterDegreeProofVersions, new ReverseComparator(MasterDegreeProofVersion.LAST_MODIFICATION_COMPARATOR));
	return masterDegreeProofVersions;
    }

    final public MasterDegreeThesisDataVersion readActiveMasterDegreeThesisDataVersion() {
	MasterDegreeThesis masterDegreeThesis = this.getMasterDegreeThesis();
	if (masterDegreeThesis != null) {
	    for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesis
		    .getMasterDegreeThesisDataVersions()) {
		if (masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
		    return masterDegreeThesisDataVersion;
		}
	    }
	}
	return null;
    }

    final public List<MasterDegreeThesisDataVersion> readNotActiveMasterDegreeThesisDataVersions() {
	MasterDegreeThesis masterDegreeThesis = this.getMasterDegreeThesis();
	List<MasterDegreeThesisDataVersion> masterDegreeThesisDataVersions = new ArrayList<MasterDegreeThesisDataVersion>();
	if (masterDegreeThesis != null) {
	    for (MasterDegreeThesisDataVersion masterDegreeThesisDataVersion : masterDegreeThesis
		    .getMasterDegreeThesisDataVersions()) {
		if (!masterDegreeThesisDataVersion.getCurrentState().getState().equals(State.ACTIVE)) {
		    masterDegreeThesisDataVersions.add(masterDegreeThesisDataVersion);
		}
	    }
	}
	Collections.sort(masterDegreeThesisDataVersions, new ReverseComparator(
		MasterDegreeThesisDataVersion.LAST_MODIFICATION_COMPARATOR));
	return masterDegreeThesisDataVersions;
    }

    final public boolean approvedInAllCurricularCoursesUntilInclusiveCurricularYear(final Integer curricularYearInteger) {
	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
	for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
	    final Collection<CurricularCourseScope> activeCurricularCourseScopes = curricularCourse.getActiveScopes();
	    for (final CurricularCourseScope curricularCourseScope : activeCurricularCourseScopes) {
		final CurricularSemester curricularSemester = curricularCourseScope.getCurricularSemester();
		final CurricularYear curricularYear = curricularSemester.getCurricularYear();
		if (curricularYearInteger == null || curricularYear.getYear().intValue() <= curricularYearInteger.intValue()) {
		    if (!isCurricularCourseApproved(curricularCourse)) {
		        if (LogLevel.INFO) {
		            System.out.println("curricular course failed: " + curricularCourse.getName() + " "
		                    + curricularCourse.getCode());
		        }
			return false;
		    }
		}
	    }
	}
	return true;
    }

    public int numberCompletedCoursesForSpecifiedDegrees(final Set<Degree> degrees) {
	int numberCompletedCourses = 0;
	for (final StudentCurricularPlan studentCurricularPlan : getRegistration().getStudentCurricularPlansSet()) {
	    for (Enrolment enrolment : studentCurricularPlan.getEnrolmentsSet()) {
		if (!enrolment.isInvisible() && enrolment.isApproved()) {
		    final ExecutionPeriod executionPeriod = enrolment.getExecutionPeriod();
		    final ExecutionYear executionYear = executionPeriod.getExecutionYear();
		    if (!PeriodState.CURRENT.equals(executionYear.getState())) {
			final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
			final DegreeCurricularPlan degreeCurricularPlan = curricularCourse.getDegreeCurricularPlan();
			final Degree degree = degreeCurricularPlan.getDegree();
			final CompetenceCourse competenceCourse = curricularCourse.getCompetenceCourse();
			if (degrees.contains(degree)
				|| (competenceCourse != null && competenceCourse.isAssociatedToAnyDegree(degrees))) {
			    numberCompletedCourses++;
			}
		    }
		}
	    }
	}
	return numberCompletedCourses;
    }

    public boolean isEnroledInSpecialSeason(final ExecutionPeriod executionPeriod) {
	for (final Enrolment enrolment : getAllStudentEnrollmentsInExecutionPeriod(executionPeriod)) {
	    if (enrolment.hasSpecialSeason()) {
		return true;
	    }
	}
	return false;
    }
    
    public boolean isEnroledInSpecialSeason(final ExecutionYear executionYear) {
	final List<ExecutionPeriod> executionPeriods = new ArrayList<ExecutionPeriod>(executionYear.getExecutionPeriods());
	Collections.sort(executionPeriods, new ReverseComparator(ExecutionPeriod.EXECUTION_PERIOD_COMPARATOR_BY_SEMESTER_AND_YEAR));

	for (final ExecutionPeriod executionPeriod : executionPeriods) {
	    if (isEnroledInSpecialSeason(executionPeriod)) {
		return true;
	    }
	}
	return false;
    }

    final public Collection<Enrolment> getSpecialSeasonToEnrol(ExecutionYear executionYear) {
	Map<CurricularCourse, Enrolment> result = new HashMap<CurricularCourse, Enrolment>();

	for (Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.getEnrolmentEvaluationType() != EnrolmentEvaluationType.SPECIAL_SEASON
		    && enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear) && !enrolment.isApproved()) {
		if (result.get(enrolment.getCurricularCourse()) != null) {
		    Enrolment enrolmentMap = result.get(enrolment.getCurricularCourse());
		    if (enrolment.getExecutionPeriod().compareTo(enrolmentMap.getExecutionPeriod()) > 0) {
			result.put(enrolment.getCurricularCourse(), enrolment);
		    }
		} else {
		    result.put(enrolment.getCurricularCourse(), enrolment);
		}
	    }
	}
	return new HashSet<Enrolment>(result.values());
    }

    final public Collection<Enrolment> getSpecialSeasonEnrolments(ExecutionYear executionYear) {
	if (isBolonhaDegree()) {
	    return getRoot().getSpecialSeasonEnrolments(executionYear);
	}

	final Set<Enrolment> result = new HashSet<Enrolment>();
	for (Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.getEnrolmentEvaluationType() == EnrolmentEvaluationType.SPECIAL_SEASON
		    && enrolment.getExecutionPeriod().getExecutionYear().equals(executionYear)) {
		result.add(enrolment);
	    }
	}
	return result;
    }

    public boolean hasSpecialSeasonForActualExecutionPeriod() {
	return hasSpecialSeasonFor(ExecutionPeriod.readActualExecutionPeriod());
    }

    public boolean hasSpecialSeasonFor(ExecutionPeriod executionPeriod) {
	if (executionPeriod.hasPreviousExecutionPeriod()
		&& isEnroledInSpecialSeason(executionPeriod.getPreviousExecutionPeriod().getExecutionYear())) {
	    return hasAnyEnrolmentPeriodInCurricularCoursesSpecialSeason(executionPeriod, new DateTime());
	}
	return false;
    }

    private boolean hasAnyEnrolmentPeriodInCurricularCoursesSpecialSeason(final ExecutionPeriod executionPeriod, final DateTime date) {
	    final EnrolmentPeriodInCurricularCoursesSpecialSeason periodInCurricularCoursesSpecialSeason = getDegreeCurricularPlan()
		    .getEnrolmentPeriodInCurricularCoursesSpecialSeasonByExecutionPeriod(executionPeriod);
	 return (periodInCurricularCoursesSpecialSeason != null && periodInCurricularCoursesSpecialSeason.containsDate(date));
    }
    
    public boolean hasSpecialSeasonOrHasSpecialSeasonInTransitedStudentCurricularPlan(final ExecutionPeriod executionPeriod) {
	return hasSpecialSeasonFor(executionPeriod) || hasTransitedRegistrationAndOpenEnrolmentPeriodInSpecialSeason(executionPeriod);
    }
    
    private boolean hasTransitedRegistrationAndOpenEnrolmentPeriodInSpecialSeason(final ExecutionPeriod executionPeriod) {
	final DateTime now = new DateTime();
	for (final Registration registration : getRegistration().getStudent().getTransitedRegistrations()) {
	    final StudentCurricularPlan studentCurricularPlan = registration.getLastStudentCurricularPlan();

	    if (!studentCurricularPlan.equals(this) && studentCurricularPlan.getDegreeCurricularPlan().hasTargetEquivalencePlanFor(getDegreeCurricularPlan())) {
		if (executionPeriod.hasPreviousExecutionPeriod()
			&& studentCurricularPlan.isEnroledInSpecialSeason(executionPeriod.getPreviousExecutionPeriod().getExecutionYear())
			&& hasAnyEnrolmentPeriodInCurricularCoursesSpecialSeason(executionPeriod, now)) {
		    return true;
		}
	    }
	}
	return false;
    }

    // Improvements
    final public void createEnrolmentEvaluationForImprovement(final Collection<Enrolment> toCreate, final Employee employee,
	    final ExecutionPeriod executionPeriod) {
	final Collection<EnrolmentEvaluation> created = new HashSet<EnrolmentEvaluation>();

	for (final Enrolment enrolment : toCreate) {
	    created.add(enrolment.createEnrolmentEvaluationForImprovement(employee, executionPeriod));
	}

	final ImprovementOfApprovedEnrolmentEvent improvementOfApprovedEnrolmentEvent = getNotPayedImprovementOfApprovedEnrolmentEvent();
	if (improvementOfApprovedEnrolmentEvent == null) {
	    new ImprovementOfApprovedEnrolmentEvent(employee.getAdministrativeOffice(), getPerson(), created);
	} else {
	    for (final EnrolmentEvaluation enrolmentEvaluation : created) {
		improvementOfApprovedEnrolmentEvent.addImprovementEnrolmentEvaluations(enrolmentEvaluation);
	    }
	}
    }

    final public List<Enrolment> getEnroledImprovements() {
	List<Enrolment> enroledImprovements = new ArrayList<Enrolment>();
	for (Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.isImprovementEnroled()) {
		enroledImprovements.add(enrolment);
	    }
	}
	return enroledImprovements;
    }

    final public List<Enrolment> getEnrolmentsToImprov(ExecutionPeriod executionPeriod) {
	List<Enrolment> previousExecPeriodAprovedEnrol = new ArrayList<Enrolment>();
	List<Enrolment> beforePreviousExecPeriodAprovedEnrol = new ArrayList<Enrolment>();
	List<Enrolment> beforeBeforePreviousExecPeriodAprovedEnrol = new ArrayList<Enrolment>();

	ExecutionPeriod previousExecPeriod = executionPeriod.getPreviousExecutionPeriod();
	ExecutionPeriod beforePreviousExecPeriod = previousExecPeriod.getPreviousExecutionPeriod();
	ExecutionPeriod beforeBeforePreviousExecPeriod = beforePreviousExecPeriod.getPreviousExecutionPeriod();

	if (previousExecPeriod != null) {
	    previousExecPeriodAprovedEnrol.addAll(getAprovedEnrolmentsNotImprovedInExecutionPeriod(previousExecPeriod));
	}

	if (beforePreviousExecPeriod != null) {
	    beforePreviousExecPeriodAprovedEnrol
		    .addAll(getAprovedEnrolmentsNotImprovedInExecutionPeriod(beforePreviousExecPeriod));
	}

	if (beforeBeforePreviousExecPeriod != null) {
	    beforeBeforePreviousExecPeriodAprovedEnrol
		    .addAll(getAprovedEnrolmentsNotImprovedInExecutionPeriod(beforeBeforePreviousExecPeriod));
	}

	// From Before Before Previous ExecutionPeriod remove the ones with
	// scope in
	// Previous ExecutionPeriod
	removeFromBeforeBeforePreviousExecutionPeriod(beforeBeforePreviousExecPeriodAprovedEnrol, previousExecPeriod);

	// From previous OccupationPeriod remove the ones that not take place in
	// the
	// Current OccupationPeriod
	previousExecPeriodAprovedEnrol = removeNotInCurrentExecutionPeriod(previousExecPeriodAprovedEnrol, executionPeriod);

	List<Enrolment> res = (List<Enrolment>) CollectionUtils.union(beforePreviousExecPeriodAprovedEnrol,
		previousExecPeriodAprovedEnrol);

	res = (List<Enrolment>) CollectionUtils.union(beforeBeforePreviousExecPeriodAprovedEnrol, res);

	return res;
    }

    private List<Enrolment> getAprovedEnrolmentsNotImprovedInExecutionPeriod(ExecutionPeriod executionPeriod) {
	List<Enrolment> result = new ArrayList<Enrolment>();
	for (Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.canBeImproved() && enrolment.getExecutionPeriod().equals(executionPeriod)) {
		result.add(enrolment);
	    }
	}
	return result;
    }

    private void removeFromBeforeBeforePreviousExecutionPeriod(List beforeBeforePreviousExecPeriodAprovedEnrol,
	    final ExecutionPeriod previousExecPeriod) {
	CollectionUtils.filter(beforeBeforePreviousExecPeriodAprovedEnrol, new Predicate() {

	    final public boolean evaluate(Object arg0) {
		Enrolment enrolment = (Enrolment) arg0;
		for (CurricularCourseScope curricularCourseScope : enrolment.getCurricularCourse().getScopes()) {
		    if (curricularCourseScope.isActiveForExecutionPeriod(previousExecPeriod)) {
			return false;
		    }
		}

		return true;
	    }

	});
    }

    private List<Enrolment> removeNotInCurrentExecutionPeriod(List<Enrolment> enrolments,
	    final ExecutionPeriod currentExecutionPeriod) {
	final List<Enrolment> res = new ArrayList<Enrolment>();
	for (final Enrolment enrolment : enrolments) {
	    final CurricularCourse curricularCourse = enrolment.getCurricularCourse();
	    Set<CurricularCourseScope> scopes = curricularCourse.findCurricularCourseScopesIntersectingPeriod(
		    currentExecutionPeriod.getBeginDate(), currentExecutionPeriod.getEndDate());
	    if (scopes != null && !scopes.isEmpty()) {
		CurricularCourseScope curricularCourseScope = (CurricularCourseScope) CollectionUtils.find(scopes,
			new Predicate() {

			    final public boolean evaluate(Object arg0) {
				CurricularCourseScope curricularCourseScope = (CurricularCourseScope) arg0;
				if (curricularCourseScope.getCurricularSemester().getSemester().equals(
					currentExecutionPeriod.getSemester())
					&& (curricularCourseScope.getEndDate() == null || (curricularCourseScope.getEnd()
						.compareTo(new Date())) >= 0))
				    return true;
				return false;
			    }
			});

		if (curricularCourseScope != null)
		    res.add(enrolment);
	    }

	}
	return res;
    }

    final public void createFirstTimeStudentEnrolmentsFor(ExecutionPeriod executionPeriod, String createdBy) {
	internalCreateFirstTimeStudentEnrolmentsFor(getRoot(), getDegreeCurricularPlan().getCurricularPeriodFor(1, 1),
		executionPeriod, createdBy);
    }

    /**
     * Note: This is enrolment without rules and should only be used for first
     * time enrolments
     */
    private void internalCreateFirstTimeStudentEnrolmentsFor(CurriculumGroup curriculumGroup, CurricularPeriod curricularPeriod,
	    ExecutionPeriod executionPeriod, String createdBy) {

	if (curriculumGroup.hasDegreeModule()) {
	    for (final Context context : curriculumGroup.getDegreeModule().getContextsWithCurricularCourseByCurricularPeriod(
		    curricularPeriod, executionPeriod)) {
		new Enrolment(this, curriculumGroup, (CurricularCourse) context.getChildDegreeModule(), executionPeriod,
			EnrollmentCondition.FINAL, createdBy);
	    }
	}

	if (curriculumGroup.hasAnyCurriculumModules()) {
	    for (final CurriculumModule curriculumModule : curriculumGroup.getCurriculumModulesSet()) {
		if (!curriculumModule.isLeaf()) {
		    internalCreateFirstTimeStudentEnrolmentsFor((CurriculumGroup) curriculumModule, curricularPeriod,
			    executionPeriod, createdBy);
		}
	    }
	}
    }

    final public void createModules(Collection<DegreeModuleToEnrol> degreeModulesToEnrol, ExecutionPeriod executionPeriod,
	    EnrollmentCondition enrollmentCondition) {

	for (DegreeModuleToEnrol degreeModuleToEnrol : degreeModulesToEnrol) {
	    final DegreeModule childDegreeModule = degreeModuleToEnrol.getContext().getChildDegreeModule();
	    if (childDegreeModule.isLeaf()) {
		new Enrolment(this, degreeModuleToEnrol.getCurriculumGroup(), (CurricularCourse) childDegreeModule,
			executionPeriod, enrollmentCondition, AccessControl.getUserView().getUtilizador());
	    } else if (childDegreeModule instanceof CycleCourseGroup) {
		new CycleCurriculumGroup((RootCurriculumGroup) degreeModuleToEnrol.getCurriculumGroup(),
			(CycleCourseGroup) childDegreeModule, executionPeriod);
	    } else {
		new CurriculumGroup(degreeModuleToEnrol.getCurriculumGroup(), (CourseGroup) childDegreeModule, executionPeriod);
	    }
	}
    }

    final public Set<IDegreeModuleToEvaluate> getDegreeModulesToEvaluate(final ExecutionPeriod executionPeriod) {
	return isBolonhaDegree() ? getRoot().getDegreeModulesToEvaluate(executionPeriod) : Collections.EMPTY_SET;
    }

    @Checked("StudentCurricularPlanPredicates.enrol")
    final public RuleResult enrol(final Person responsiblePerson, final ExecutionPeriod executionPeriod,
	    final Set<IDegreeModuleToEvaluate> degreeModulesToEnrol, final List<CurriculumModule> curriculumModulesToRemove,
	    final CurricularRuleLevel curricularRuleLevel) {

	assertEnrolmentPreConditions(responsiblePerson, executionPeriod, curricularRuleLevel);

	final EnrolmentContext enrolmentContext = new EnrolmentContext(responsiblePerson, this, executionPeriod,
		degreeModulesToEnrol, curriculumModulesToRemove, curricularRuleLevel);
	return net.sourceforge.fenixedu.domain.studentCurriculum.StudentCurricularPlanEnrolment.createManager(this,
		enrolmentContext, responsiblePerson).manage();
    }

    @Checked("StudentCurricularPlanPredicates.enrolInAffinityCycle")
    public void enrolInAffinityCycle(final CycleCourseGroup cycleCourseGroup, final ExecutionPeriod executionPeriod) {
	if (getDegreeCurricularPlan().getRoot().hasDegreeModule(cycleCourseGroup)) {
	    new CycleCurriculumGroup(getRoot(), cycleCourseGroup, executionPeriod);
	} else {
	    new ExternalCurriculumGroup(getRoot(), cycleCourseGroup, executionPeriod);
	}
    }

    private void assertEnrolmentPreConditions(final Person responsiblePerson, final ExecutionPeriod executionPeriod,
	    final CurricularRuleLevel level) {
	
	final Registration registration = this.getRegistration();
	if (!registration.isInRegisteredState(executionPeriod)) {
	    throw new DomainException("error.StudentCurricularPlan.cannot.enrol.with.registration.inactive");
	}

	if (!responsiblePerson.hasRole(RoleType.MANAGER) && registration.getStudent().isAnyTuitionInDebt()) {
	    throw new DomainException("error.StudentCurricularPlan.cannot.enrol.with.gratuity.debts.for.previous.execution.years");
	}

	final DegreeCurricularPlan degreeCurricularPlan = this.getDegreeCurricularPlan();
	if (responsiblePerson.hasRole(RoleType.STUDENT)) {

	    if (!responsiblePerson.getStudent().getRegistrationsToEnrolByStudent().contains(getRegistration())) {
		throw new DomainException("error.StudentCurricularPlan.student.is.not.allowed.to.perform.enrol");
	    }

	    if (level != CurricularRuleLevel.ENROLMENT_WITH_RULES) {
		throw new DomainException("error.StudentCurricularPlan.invalid.curricular.rule.level");
	    }

	    if (!degreeCurricularPlan.hasOpenEnrolmentPeriodInCurricularCoursesFor(executionPeriod)
		    && !hasSpecialSeasonOrHasSpecialSeasonInTransitedStudentCurricularPlan(executionPeriod)) {
		throw new DomainException("error.StudentCurricularPlan.students.can.only.perform.curricular.course.enrollment.inside.established.periods");
	    }

	    }
	}

    final public String getName() {
	return getDegreeCurricularPlan().getName();
    }

    final public Campus getCurrentCampus() {
	final Campus currentCampus = getDegreeCurricularPlan().getCurrentCampus();
	return currentCampus == null ? getLastCampus() : currentCampus;
    }

    final public Campus getLastCampus() {
	final Campus lastScpCampus = getDegreeCurricularPlan().getCampus(getLastExecutionYear());
	return lastScpCampus == null ? getDegreeCurricularPlan().getLastCampus() : lastScpCampus;
    }

    final public void createOptionalEnrolment(final CurriculumGroup curriculumGroup, final ExecutionPeriod executionPeriod,
	    final OptionalCurricularCourse optionalCurricularCourse, final CurricularCourse curricularCourse,
	    final EnrollmentCondition enrollmentCondition) {
	if (getRoot().isApproved(curricularCourse, executionPeriod)) {
	    throw new DomainException("error.already.aproved", new String[] { curricularCourse.getName() });
	}
	if (getRoot().isEnroledInExecutionPeriod(curricularCourse, executionPeriod)) {
	    throw new DomainException("error.already.enroled.in.executioPerdiod", new String[] { curricularCourse.getName(),
		    executionPeriod.getQualifiedName() });
	}

	new OptionalEnrolment(this, curriculumGroup, curricularCourse, executionPeriod, enrollmentCondition, AccessControl
		.getUserView().getUtilizador(), optionalCurricularCourse);
    }

    final public void createNoCourseGroupCurriculumGroupEnrolment(final CurricularCourse curricularCourse,
	    final ExecutionPeriod executionPeriod, final NoCourseGroupCurriculumGroupType groupType) {
	if (getRoot().isApproved(curricularCourse, executionPeriod)) {
	    throw new DomainException("error.already.aproved", new String[] { curricularCourse.getName() });
	}
	if (getRoot().isEnroledInExecutionPeriod(curricularCourse, executionPeriod)) {
	    throw new DomainException("error.already.enroled.in.executioPerdiod", new String[] { curricularCourse.getName(),
		    executionPeriod.getQualifiedName() });
	}

	getRoot().createNoCourseGroupCurriculumGroupEnrolment(this, curricularCourse, executionPeriod, groupType);
    }

    final public NoCourseGroupCurriculumGroup getNoCourseGroupCurriculumGroup(final NoCourseGroupCurriculumGroupType groupType) {
	return (getRoot() != null) ? getRoot().getNoCourseGroupCurriculumGroup(groupType) : null;
    }

    final public NoCourseGroupCurriculumGroup createNoCourseGroupCurriculumGroup(final NoCourseGroupCurriculumGroupType groupType) {
	final NoCourseGroupCurriculumGroup noCourseGroupCurriculumGroup = getNoCourseGroupCurriculumGroup(groupType);
	if (noCourseGroupCurriculumGroup != null) {
	    throw new DomainException("error.studentCurricularPlan.already.has.noCourseGroupCurriculumGroup.with.same.groupType");
	}
	return NoCourseGroupCurriculumGroup.createNewNoCourseGroupCurriculumGroup(groupType, getRoot());
    }

    public ExtraCurriculumGroup createExtraCurriculumGroup() {
	return (ExtraCurriculumGroup) createNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR);
    }

    public ExtraCurriculumGroup getExtraCurriculumGroup() {
	return (ExtraCurriculumGroup) getNoCourseGroupCurriculumGroup(NoCourseGroupCurriculumGroupType.EXTRA_CURRICULAR);
    }

    public boolean hasExtraCurriculumGroup() {
	return getExtraCurriculumGroup() != null;
    }

    public Collection<CurricularCourse> getAllCurricularCoursesToDismissal() {
	final Collection<CurricularCourse> result = new HashSet<CurricularCourse>();
	if (isBolonhaDegree()) {
	    for (final CycleType cycleType : getDegreeType().getSupportedCyclesToEnrol()) {
		final CourseGroup courseGroup = getCourseGroupWithCycleTypeToCollectCurricularCourses(cycleType);
		if (courseGroup != null) {
		    for (final CurricularCourse curricularCourse : courseGroup.getAllCurricularCourses()) {
			if (!isApproved(curricularCourse)) {
			    result.add(curricularCourse);
			}
		    }
		}
	    }
	} else {
	for (final CurricularCourse curricularCourse : getDegreeCurricularPlan().getCurricularCoursesSet()) {
		if (!isApproved(curricularCourse)) {
		result.add(curricularCourse);
		}
	    }
	}
	return result;
    }

    private CourseGroup getCourseGroupWithCycleTypeToCollectCurricularCourses(final CycleType cycleType) {
	final CycleCurriculumGroup curriculumGroup = getCycle(cycleType);
	return curriculumGroup != null ? curriculumGroup.getDegreeModule() : getDegreeCurricularPlan().getCycleCourseGroup(cycleType);
    }
    
    public Collection<ExternalCurriculumGroup> getExternalCurriculumGroups() {
	if (!hasRoot()) {
	    return Collections.emptyList();
	}
	
	final Collection<ExternalCurriculumGroup> result = new ArrayList<ExternalCurriculumGroup>();
	for (final CycleCurriculumGroup cycleCurriculumGroup : getRoot().getCycleCurriculumGroups()) {
	    if (cycleCurriculumGroup.isExternal()) {
		result.add((ExternalCurriculumGroup) cycleCurriculumGroup);
	    }
	}
	return result;
    }

    final public Credits createNewCreditsDismissal(CourseGroup courseGroup, Collection<SelectedCurricularCourse> dismissals,
	    Collection<IEnrolment> enrolments, Double givenCredits, ExecutionPeriod executionPeriod) {
	if (courseGroup != null) {
	    Collection<CurricularCourse> noEnrolCurricularCourse = new ArrayList<CurricularCourse>();
	    if (dismissals != null) {
		for (SelectedCurricularCourse selectedCurricularCourse : dismissals) {
		    noEnrolCurricularCourse.add(selectedCurricularCourse.getCurricularCourse());
	}
	    }
	    return new Credits(this, courseGroup, enrolments, noEnrolCurricularCourse, givenCredits, executionPeriod);
	} else {
	    return new Credits(this, dismissals, enrolments, executionPeriod);
	}
    }

    final public List<Dismissal> getDismissals() {
	final List<Dismissal> result = new ArrayList<Dismissal>();
	if (isBoxStructure()) {
	    getRoot().collectDismissals(result);
	}
	return result;
    }

    final public Dismissal getDismissal(final CurricularCourse curricularCourse) {
	return isBoxStructure() ? getRoot().getDismissal(curricularCourse) : null;
    }

    final public Substitution getSubstitution(final IEnrolment iEnrolment) {
	for (final Dismissal dismissal : getDismissals()) {
	    if (dismissal.getCredits().isSubstitution() && dismissal.getSourceIEnrolments().contains(iEnrolment)) {
		return (Substitution) dismissal.getCredits();
	    }
	}

	return null;
    }

    final public Equivalence createNewEquivalenceDismissal(CourseGroup courseGroup,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, Double givenCredits,
	    Grade givenGrade, ExecutionPeriod executionPeriod) {
	if (courseGroup != null) {
	    Collection<CurricularCourse> noEnrolCurricularCourse = new ArrayList<CurricularCourse>();
	    if (dismissals != null) {
		for (SelectedCurricularCourse selectedCurricularCourse : dismissals) {
		    noEnrolCurricularCourse.add(selectedCurricularCourse.getCurricularCourse());
	}
	    }
	    return new Equivalence(this, courseGroup, enrolments, noEnrolCurricularCourse, givenCredits, givenGrade,
		    executionPeriod);
	} else {
	    return new Equivalence(this, dismissals, enrolments, givenGrade, executionPeriod);
	}
    }

    final public Equivalence createNewSubstitutionDismissal(CourseGroup courseGroup,
	    Collection<SelectedCurricularCourse> dismissals, Collection<IEnrolment> enrolments, Double givenCredits,
	    ExecutionPeriod executionPeriod) {
	if (courseGroup != null) {
	    Collection<CurricularCourse> noEnrolCurricularCourse = new ArrayList<CurricularCourse>();
	    if (dismissals != null) {
		for (SelectedCurricularCourse selectedCurricularCourse : dismissals) {
		    noEnrolCurricularCourse.add(selectedCurricularCourse.getCurricularCourse());
		}
	    }
	    return new Substitution(this, courseGroup, enrolments, noEnrolCurricularCourse, givenCredits, executionPeriod);
	} else {
	    return new Substitution(this, dismissals, enrolments, executionPeriod);
	}
    }

    final public Substitution createSubstitution(final Collection<SelectedCurricularCourse> dismissals,
	    final Collection<IEnrolment> enrolments, ExecutionPeriod executionPeriod) {
	return new Substitution(this, dismissals, enrolments, executionPeriod);
    }

    final public Set<DegreeModule> getAllDegreeModules() {
	final Set<DegreeModule> degreeModules = new TreeSet<DegreeModule>(DegreeModule.COMPARATOR_BY_NAME);
	final RootCurriculumGroup rootCurriculumGroup = getRoot();
	if (rootCurriculumGroup != null) {
	    rootCurriculumGroup.getAllDegreeModules(degreeModules);
	}
	return degreeModules;
    }

    public boolean hasDegreeModule(final DegreeModule degreeModule) {
	return isBoxStructure() ? getRoot().hasDegreeModule(degreeModule) : false;
    }
    
    public boolean hasCurriculumModule(final CurriculumModule curriculumModule) {
	return isBoxStructure() ? getRoot().hasCurriculumModule(curriculumModule) : false;
    }

    public CurriculumGroup findCurriculumGroupFor(final CourseGroup courseGroup) {
	return isBoxStructure() ? getRoot().findCurriculumGroupFor(courseGroup) : null;
    }

    final public MasterDegreeCandidate getMasterDegreeCandidate() {
	if (getDegreeType().equals(DegreeType.MASTER_DEGREE)) {
	    if (this.getEnrolmentsSet().size() > 0) {
		ExecutionDegree firstExecutionDegree = this.getDegreeCurricularPlan().getExecutionDegreeByYear(
			this.getFirstExecutionPeriod().getExecutionYear());
		for (final MasterDegreeCandidate candidate : this.getPerson().getMasterDegreeCandidates()) {
		    if (candidate.getExecutionDegree() == firstExecutionDegree) {
			return candidate;
		    }
		}
	    } else if (this.getPerson().getMasterDegreeCandidatesCount() == 1) {
		return this.getPerson().getMasterDegreeCandidates().iterator().next();
	    }
	}
	return null;
    }

    final public Double getEctsCreditsForCourseGroup(final CourseGroup courseGroup) {
	final CurriculumGroup curriculumGroup = findCurriculumGroupFor(courseGroup);
	return (curriculumGroup == null) ? Double.valueOf(0d) : curriculumGroup.getAprovedEctsCredits();
    }

    final public void resetIsFirstTimeEnrolmentForCurricularCourse(final CurricularCourse curricularCourse) {
	final SortedSet<Enrolment> enrolments = new TreeSet<Enrolment>(Enrolment.COMPARATOR_BY_EXECUTION_PERIOD_AND_ID);
	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (curricularCourse == enrolment.getCurricularCourse()) {
		enrolments.add(enrolment);
	    }
	}
	Boolean b = Boolean.TRUE;
	for (final Enrolment enrolment : enrolments) {
	    if (!enrolment.isAnnulled()) {
		enrolment.setIsFirstTime(b);
		b = Boolean.FALSE;
	    } else {
		enrolment.setIsFirstTime(Boolean.FALSE);
	    }
	}
    }

    final public StudentCurricularPlanEquivalencePlan createStudentCurricularPlanEquivalencePlan() {
	return new StudentCurricularPlanEquivalencePlan(this);
    }

    final public boolean hasEnrolmentOrAprovalInCurriculumModule(final DegreeModule degreeModule) {
	final RootCurriculumGroup rootCurriculumGroup = getRoot();
	return rootCurriculumGroup != null && hasEnrolmentOrAprovalInCurriculumModule(rootCurriculumGroup, degreeModule);
    }

    private boolean hasEnrolmentOrAprovalInCurriculumModule(final CurriculumModule curriculumModule,
	    final DegreeModule degreeModule) {
	if (curriculumModule.getDegreeModule() == degreeModule) {
	    return true;
	}
	if (curriculumModule.isLeaf()) {
	    return false;
	}
	final CurriculumGroup curriculumGroup = (CurriculumGroup) curriculumModule;
	for (final CurriculumModule child : curriculumGroup.getCurriculumModulesSet()) {
	    if (hasEnrolmentOrAprovalInCurriculumModule(child, degreeModule)) {
		return true;
	    }
	}
	return false;
    }

    public List<Enrolment> getEnrolmentsWithExecutionYearBeforeOrEqualTo(final ExecutionYear executionYear) {
	final List<Enrolment> result = new ArrayList<Enrolment>();
	for (final Enrolment enrolment : getEnrolmentsSet()) {
	    if (enrolment.getExecutionPeriod().getExecutionYear().isBeforeOrEquals(executionYear)) {
		result.add(enrolment);
	    }
	}
	return result;
    }

    public Tutorship getLastTutorship() {
	return (Tutorship) Collections.max(getTutorships(), Tutorship.TUTORSHIP_START_DATE_COMPARATOR);
    }

    public Tutorship getActiveTutorship() {
	List<Tutorship> tutorships = getTutorships();
	if (!tutorships.isEmpty() && getLastTutorship().isActive()) {
	    return getLastTutorship();
	}
	return null;
    }

    @Override
    public void addTutorships(Tutorship tutorships) throws DomainException {
	for (Tutorship tutorship : getTutorships()) {
	    if (tutorship.getTeacher().equals(tutorships.getTeacher()) && tutorship.getEndDate().equals(tutorships.getEndDate())) {
		throw new DomainException("error.tutorships.duplicatedTutorship");
	    }

	    if (tutorship.isActive()) {
		throw new DomainException("error.tutorships.onlyOneActiveTutorship");
	    }
	}

	super.addTutorships(tutorships);
    }

    public boolean getHasAnyEquivalences() {
	return !this.getNotNeedToEnrollCurricularCourses().isEmpty();
    }

    public boolean isLastStudentCurricularPlanFromRegistration() {
	return hasRegistration() && getRegistration().getLastStudentCurricularPlan() == this;
    }

    @Checked("StudentCurricularPlanPredicates.moveCurriculumLines")
    public void moveCurriculumLines(final Person responsiblePerson, final MoveCurriculumLinesBean moveCurriculumLinesBean) {
	boolean runRules = false;
	for (final CurriculumLineLocationBean curriculumLineLocationBean : moveCurriculumLinesBean.getCurriculumLineLocations()) {
	    final CurriculumGroup curriculumGroup = curriculumLineLocationBean.getCurriculumGroup();
	    final CurriculumLine curriculumLine = curriculumLineLocationBean.getCurriculumLine();

	    if (curriculumLine.getCurriculumGroup() != curriculumGroup) {
		if (!responsiblePerson.hasRole(RoleType.MANAGER) && !curriculumGroup.canAdd(curriculumLine)) {
		    throw new DomainException("error.StudentCurricularPlan.cannot.move.curriculum.line.to.curriculum.group",
			    curriculumLine.getFullPath(), curriculumGroup.getFullPath());
		}

		runRules = true;
	    }

	    curriculumLine.setCurriculumGroup(curriculumGroup);
	}

	if (runRules && !responsiblePerson.hasRole(RoleType.MANAGER)) {
	    checkEnrolmentRules(responsiblePerson);
	}

    }

    public void checkEnrolmentRules(final Person responsiblePerson) {
	checkEnrolmentRules(responsiblePerson, ExecutionPeriod.readActualExecutionPeriod());

    }

    @SuppressWarnings("unchecked")
    public void checkEnrolmentRules(final Person responsiblePerson, final ExecutionPeriod executionPeriod) {
	enrol(responsiblePerson, executionPeriod, Collections.EMPTY_SET, Collections.EMPTY_LIST,
		CurricularRuleLevel.ENROLMENT_WITH_RULES);
    }
    
    public AdministrativeOffice getAdministrativeOffice() {
	return AdministrativeOffice.readByAdministrativeOfficeType(getDegree().getDegreeType()
			    .getAdministrativeOfficeType());
    }

   public CycleCurriculumGroup getCycle(final CycleType cycleType) {
	return isBoxStructure() ? getRoot().getCycleCurriculumGroup(cycleType) : null;
   }

    public boolean hasCycleCurriculumGroup(final CycleType cycleType) {
	return getCycle(cycleType) != null;
    }
    
    public Collection<? extends CurriculumGroup> getCurricularCoursePossibleGroups(final CurricularCourse curricularCourse) {
	return getRoot().getCurricularCoursePossibleGroups(curricularCourse);
    }

    public CycleCurriculumGroup getFirstCycle() {
	return isBoxStructure() ? getRoot().getCycleCurriculumGroup(CycleType.FIRST_CYCLE) : null;
    }
    
    public CycleCurriculumGroup getSecondCycle() {
	return isBoxStructure() ? getRoot().getCycleCurriculumGroup(CycleType.SECOND_CYCLE) : null;
    }

    public boolean isEmpty() {
	if (hasAnyEnrolments())
	    return false;
	if (hasAnyTutorships())
	    return false;
	if (hasRoot() && !getRoot().getAllCurriculumLines().isEmpty())
	    return false;
	if (hasEquivalencePlan())
	    return false;
	if (hasAnyGratuityEvents())
	    return false;
	if (hasAnyNotNeedToEnrollCurricularCourses())
	    return false;
	if (hasAnyCreditsInAnySecundaryAreas())
	    return false;
	if (hasAnyGratuitySituations())
	    return false;
	if (hasMasterDegreeThesis())
	    return false;
	if (hasAnyCreditsInScientificAreas())
	    return false;
	if (hasAnyCredits())
	    return false;
	return true;
    }
    
}
