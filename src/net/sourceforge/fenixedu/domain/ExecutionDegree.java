/*
 * ExecutionDegree.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package net.sourceforge.fenixedu.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.IMDCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistributionEntry;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.finalDegreeWork.Proposal;
import net.sourceforge.fenixedu.domain.inquiries.InquiryCoordinatorAnswer;
import net.sourceforge.fenixedu.domain.interfaces.HasExecutionYear;
import net.sourceforge.fenixedu.domain.space.Campus;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicCalendarRootEntry;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicYearCE;
import net.sourceforge.fenixedu.domain.util.FactoryExecutor;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

/**
 * 
 * @author rpfi
 */

public class ExecutionDegree extends ExecutionDegree_Base implements Comparable<ExecutionDegree> {

    public static final Comparator<ExecutionDegree> COMPARATOR_BY_DEGREE_NAME = new Comparator<ExecutionDegree>() {

	@Override
	public int compare(ExecutionDegree o1, ExecutionDegree o2) {
	    return o1.getDegree().getName().compareTo(o2.getDegree().getName());
	}

    };

    static final public Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATORY_BY_YEAR = new Comparator<ExecutionDegree>() {
	@Override
	public int compare(ExecutionDegree o1, ExecutionDegree o2) {
	    return o1.getExecutionYear().compareTo(o2.getExecutionYear());
	}
    };

    static final public Comparator<ExecutionDegree> REVERSE_EXECUTION_DEGREE_COMPARATORY_BY_YEAR = new Comparator<ExecutionDegree>() {
	@Override
	public int compare(ExecutionDegree o1, ExecutionDegree o2) {
	    return o1.getExecutionYear().compareTo(o2.getExecutionYear());
	}
    };

    static final public Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME = new Comparator<ExecutionDegree>() {
	@Override
	public int compare(ExecutionDegree o1, ExecutionDegree o2) {
	    return Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID.compare(o1.getDegree(), o2.getDegree());
	}
    };

    static final public Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR = new Comparator<ExecutionDegree>() {
	@Override
	public int compare(ExecutionDegree o1, ExecutionDegree o2) {
	    final ComparatorChain comparatorChain = new ComparatorChain();
	    comparatorChain.addComparator(EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME);
	    comparatorChain.addComparator(EXECUTION_DEGREE_COMPARATORY_BY_YEAR);

	    return comparatorChain.compare(o1, o2);
	}
    };

    private ExecutionDegree() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
    }

    protected ExecutionDegree(DegreeCurricularPlan degreeCurricularPlan, ExecutionYear executionYear, Campus campus,
	    Boolean temporaryExamMap) {
	this();

	if (degreeCurricularPlan == null || executionYear == null || campus == null) {
	    throw new DomainException("execution.degree.null.args.to.constructor");
	}

	setDegreeCurricularPlan(degreeCurricularPlan);
	setExecutionYear(executionYear);
	setCampus(campus);
	setTemporaryExamMap(temporaryExamMap);
    }

    public boolean canBeDeleted() {
	return !hasAnySchoolClasses() && !hasAnyMasterDegreeCandidates() && !hasAnyGuides() && !hasScheduling()
		&& !hasAnyAssociatedFinalDegreeWorkGroups() && !hasAnyAssociatedInquiriesCoursesByCourse()
		&& !hasAnyAssociatedInquiriesCoursesByStudent() && !hasAnyStudentCandidacies()
		&& !hasAnyShiftDistributionEntries();
    }

    public void delete() {

	if (canBeDeleted()) {

	    for (; hasAnyCoordinatorsList(); getCoordinatorsList().get(0).delete())
		;
	    for (; hasAnyScientificCommissionMembers(); getScientificCommissionMembers().get(0).delete())
		;

	    if (hasGratuityValues()) {
		getGratuityValues().delete();
	    }

	    deletePeriodLessonsFirstSemester();
	    deletePeriodLessonsSecondSemester();

	    deletePeriodExamsFirstSemester();
	    deletePeriodExamsSecondSemester();
	    deletePeriodExamsSpecialSeason();

	    deletePeriodGradeSubmissionNormalSeasonFirstSemester();
	    deletePeriodGradeSubmissionNormalSeasonSecondSemester();
	    deletePeriodGradeSubmissionSpecialSeason();

	    removeExecutionYear();
	    removeDegreeCurricularPlan();
	    removeCampus();

	    removeRootDomainObject();
	    deleteDomainObject();

	} else {
	    throw new DomainException("execution.degree.cannot.be.deleted");
	}
    }

    private void deletePeriodGradeSubmissionSpecialSeason() {
	OccupationPeriod occupationPeriodReference = getPeriodGradeSubmissionSpecialSeason();
	if (occupationPeriodReference != null) {
	    removePeriodGradeSubmissionSpecialSeason();
	    occupationPeriodReference.delete();
	}
    }

    private void deletePeriodGradeSubmissionNormalSeasonSecondSemester() {
	OccupationPeriod occupationPeriodReference = getPeriodGradeSubmissionNormalSeasonSecondSemester();
	if (occupationPeriodReference != null) {
	    removePeriodGradeSubmissionNormalSeasonSecondSemester();
	    occupationPeriodReference.delete();
	}
    }

    private void deletePeriodGradeSubmissionNormalSeasonFirstSemester() {
	OccupationPeriod occupationPeriodReference = getPeriodGradeSubmissionNormalSeasonFirstSemester();
	if (occupationPeriodReference != null) {
	    removePeriodGradeSubmissionNormalSeasonFirstSemester();
	    occupationPeriodReference.delete();
	}
    }

    private void deletePeriodExamsSpecialSeason() {
	OccupationPeriod occupationPeriodReference = getPeriodExamsSpecialSeason();
	if (occupationPeriodReference != null) {
	    removePeriodExamsSpecialSeason();
	    occupationPeriodReference.delete();
	}
    }

    private void deletePeriodExamsSecondSemester() {
	OccupationPeriod occupationPeriodReference = getPeriodExamsSecondSemester();
	if (occupationPeriodReference != null) {
	    removePeriodExamsSecondSemester();
	    occupationPeriodReference.delete();
	}
    }

    private void deletePeriodExamsFirstSemester() {
	OccupationPeriod occupationPeriodReference = getPeriodExamsFirstSemester();
	if (occupationPeriodReference != null) {
	    removePeriodExamsFirstSemester();
	    occupationPeriodReference.delete();
	}
    }

    private void deletePeriodLessonsSecondSemester() {
	OccupationPeriod occupationPeriodReference = getPeriodLessonsSecondSemester();
	if (occupationPeriodReference != null) {
	    removePeriodLessonsSecondSemester();
	    occupationPeriodReference.delete();
	}
    }

    private void deletePeriodLessonsFirstSemester() {
	OccupationPeriod occupationPeriodReference = getPeriodLessonsFirstSemester();
	if (occupationPeriodReference != null) {
	    removePeriodLessonsFirstSemester();
	    occupationPeriodReference.delete();
	}
    }

    public void edit(ExecutionYear executionYear, Campus campus, Boolean temporaryExamMap,
	    OccupationPeriod periodLessonsFirstSemester, OccupationPeriod periodExamsFirstSemester,
	    OccupationPeriod periodLessonsSecondSemester, OccupationPeriod periodExamsSecondSemester,
	    OccupationPeriod periodExamsSpecialSeason, OccupationPeriod gradeSubmissionNormalSeasonFirstSemester,
	    OccupationPeriod gradeSubmissionNormalSeasonSecondSemester, OccupationPeriod gradeSubmissionSpecialSeason) {

	setExecutionYear(executionYear);
	setCampus(campus);
	setTemporaryExamMap(temporaryExamMap);

	if (periodLessonsFirstSemester != getPeriodLessonsFirstSemester()) {
	    deletePeriodLessonsFirstSemester();
	    setPeriodLessonsFirstSemester(periodLessonsFirstSemester);
	}

	if (periodExamsFirstSemester != getPeriodExamsFirstSemester()) {
	    deletePeriodExamsFirstSemester();
	    setPeriodExamsFirstSemester(periodExamsFirstSemester);
	}

	if (periodLessonsSecondSemester != getPeriodLessonsSecondSemester()) {
	    deletePeriodLessonsSecondSemester();
	    setPeriodLessonsSecondSemester(periodLessonsSecondSemester);
	}

	if (periodExamsSecondSemester != getPeriodExamsSecondSemester()) {
	    deletePeriodExamsSecondSemester();
	    setPeriodExamsSecondSemester(periodExamsSecondSemester);
	}

	if (periodExamsSpecialSeason != getPeriodExamsSpecialSeason()) {
	    deletePeriodExamsSpecialSeason();
	    setPeriodExamsSpecialSeason(periodExamsSpecialSeason);
	}

	if (gradeSubmissionNormalSeasonFirstSemester != getPeriodGradeSubmissionNormalSeasonFirstSemester()) {
	    deletePeriodGradeSubmissionNormalSeasonFirstSemester();
	    setPeriodGradeSubmissionNormalSeasonFirstSemester(gradeSubmissionNormalSeasonFirstSemester);
	}

	if (gradeSubmissionNormalSeasonSecondSemester != getPeriodGradeSubmissionNormalSeasonSecondSemester()) {
	    deletePeriodGradeSubmissionNormalSeasonSecondSemester();
	    setPeriodGradeSubmissionNormalSeasonSecondSemester(gradeSubmissionNormalSeasonSecondSemester);
	}

	if (gradeSubmissionSpecialSeason != getPeriodGradeSubmissionSpecialSeason()) {
	    deletePeriodGradeSubmissionSpecialSeason();
	    setPeriodGradeSubmissionSpecialSeason(gradeSubmissionSpecialSeason);
	}
    }

    public boolean isBolonhaDegree() {
	return this.getDegreeCurricularPlan().isBolonhaDegree();
    }

    @Override
    public int compareTo(ExecutionDegree executionDegree) {
	final ExecutionYear executionYear = executionDegree.getExecutionYear();
	return getExecutionYear().compareTo(executionYear);
    }

    public boolean isAfter(ExecutionDegree executionDegree) {
	return this.compareTo(executionDegree) > 0;
    }

    public boolean isBefore(ExecutionDegree executionDegree) {
	return this.compareTo(executionDegree) < 0;
    }

    public boolean isFirstYear() {
	final List<ExecutionDegree> executionDegrees = this.getDegreeCurricularPlan().getExecutionDegrees();
	return this == Collections.min(executionDegrees, EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
    }

    public Set<Shift> findAvailableShifts(final CurricularYear curricularYear, final ExecutionSemester executionSemester) {
	final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
	final Set<Shift> shifts = new HashSet<Shift>();
	for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
	    if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(curricularYear, degreeCurricularPlan,
		    executionSemester)) {
		for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
		    if (executionCourse.getExecutionPeriod() == executionSemester) {
			shifts.addAll(executionCourse.getAssociatedShifts());
		    }
		}
	    }
	}
	return shifts;
    }

    @Deprecated
    public Set<SchoolClass> findSchoolClassesByExecutionPeriod(final ExecutionSemester executionSemester) {
	final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
	for (final SchoolClass schoolClass : getSchoolClasses()) {
	    if (schoolClass.getExecutionPeriod() == executionSemester) {
		schoolClasses.add(schoolClass);
	    }
	}
	return schoolClasses;
    }

    public Set<SchoolClass> findSchoolClassesByAcademicInterval(final AcademicInterval academicInterval) {
	final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
	for (final SchoolClass schoolClass : getSchoolClasses()) {
	    if (schoolClass.getExecutionPeriod().getAcademicInterval().equals(academicInterval)) {
		schoolClasses.add(schoolClass);
	    }
	}
	return schoolClasses;
    }

    @Deprecated
    public Set<SchoolClass> findSchoolClassesByExecutionPeriodAndCurricularYear(final ExecutionSemester executionSemester,
	    final Integer curricularYear) {
	final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
	for (final SchoolClass schoolClass : getSchoolClasses()) {
	    if (schoolClass.getExecutionPeriod() == executionSemester && schoolClass.getAnoCurricular().equals(curricularYear)) {
		schoolClasses.add(schoolClass);
	    }
	}
	return schoolClasses;
    }

    public Set<SchoolClass> findSchoolClassesByAcademicIntervalAndCurricularYear(final AcademicInterval academicInterval,
	    final Integer curricularYear) {
	final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
	for (final SchoolClass schoolClass : getSchoolClasses()) {
	    if (schoolClass.getExecutionPeriod().getAcademicInterval().equals(academicInterval)
		    && schoolClass.getAnoCurricular().equals(curricularYear)) {
		schoolClasses.add(schoolClass);
	    }
	}
	return schoolClasses;
    }

    public SchoolClass findSchoolClassesByExecutionPeriodAndName(final ExecutionSemester executionSemester, final String name) {
	for (final SchoolClass schoolClass : getSchoolClasses()) {
	    if (schoolClass.getExecutionPeriod() == executionSemester && schoolClass.getNome().equalsIgnoreCase(name)) {
		return schoolClass;
	    }
	}
	return null;
    }

    public List<CandidateSituation> getCandidateSituationsInSituation(List<SituationName> situationNames) {
	List<CandidateSituation> result = new ArrayList<CandidateSituation>();

	for (MasterDegreeCandidate candidate : getMasterDegreeCandidates()) {
	    for (CandidateSituation situation : candidate.getSituations()) {

		if (situation.getValidation().getState() == null || situation.getValidation().getState() != State.ACTIVE) {
		    continue;
		}

		if (situationNames != null && !situationNames.contains(situation.getSituation())) {
		    continue;
		}

		result.add(situation);
	    }
	}

	return result;
    }

    public Coordinator getCoordinatorByTeacher(Person person) {
	for (Coordinator coordinator : getCoordinatorsList()) {
	    if (coordinator.getPerson() == person) {
		return coordinator;
	    }
	}

	return null;
    }

    public MasterDegreeCandidate getMasterDegreeCandidateBySpecializationAndCandidateNumber(Specialization specialization,
	    Integer candidateNumber) {

	for (final MasterDegreeCandidate masterDegreeCandidate : this.getMasterDegreeCandidatesSet()) {
	    if (masterDegreeCandidate.getSpecialization() == specialization
		    && masterDegreeCandidate.getCandidateNumber().equals(candidateNumber)) {
		return masterDegreeCandidate;
	    }
	}
	return null;
    }

    public Integer generateCandidateNumberForSpecialization(Specialization specialization) {
	int maxCandidateNumber = 0;
	for (final MasterDegreeCandidate masterDegreeCandidate : this.getMasterDegreeCandidatesSet()) {
	    if (masterDegreeCandidate.getSpecialization() == specialization && masterDegreeCandidate.getCandidateNumber() != null) {
		maxCandidateNumber = Math.max(maxCandidateNumber, masterDegreeCandidate.getCandidateNumber());
	    }
	}
	return Integer.valueOf(++maxCandidateNumber);
    }

    private static Comparator<ExecutionDegree> COMPARATOR_BY_DEGREE_CURRICULAR_PLAN_ID_INTERNAL_DESC = new Comparator<ExecutionDegree>() {
	@Override
	public int compare(ExecutionDegree o1, ExecutionDegree o2) {
	    return o2.getDegreeCurricularPlan().getIdInternal().compareTo(o1.getDegreeCurricularPlan().getIdInternal());
	}
    };

    public static List<ExecutionDegree> getAllByExecutionYear(String year) {

	if (year == null) {
	    return Collections.EMPTY_LIST;
	}

	final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
	for (ExecutionDegree executionDegree : RootDomainObject.getInstance().getExecutionDegrees()) {
	    if (year.equals(executionDegree.getExecutionYear().getYear())) {
		result.add(executionDegree);
	    }
	}
	Collections.sort(result, COMPARATOR_BY_DEGREE_CURRICULAR_PLAN_ID_INTERNAL_DESC);

	return result;
    }

    public static List<ExecutionDegree> getAllByExecutionYear(ExecutionYear executionYear) {

	if (executionYear == null) {
	    return Collections.EMPTY_LIST;
	}

	final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();

	for (final ExecutionDegree executionDegree : RootDomainObject.getInstance().getExecutionDegrees()) {
	    if (executionDegree.getExecutionYear() == executionYear) {
		result.add(executionDegree);
	    }
	}
	Collections.sort(result, COMPARATOR_BY_DEGREE_CURRICULAR_PLAN_ID_INTERNAL_DESC);

	return result;
    }

    public static List<ExecutionDegree> getAllByExecutionCourseAndTeacher(ExecutionCourse executionCourse, Person person) {
	List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();

	for (ExecutionDegree executionDegree : RootDomainObject.getInstance().getExecutionDegrees()) {
	    boolean matchExecutionCourse = false;
	    for (CurricularCourse curricularCourse : executionDegree.getDegreeCurricularPlan().getCurricularCourses()) {
		if (curricularCourse.getAssociatedExecutionCourses().contains(executionCourse)) {
		    matchExecutionCourse = true;
		    break;
		}
	    }

	    if (!matchExecutionCourse) {
		continue;
	    }

	    // if teacher is not a coordinator of the executionDegree
	    if (executionDegree.getCoordinatorByTeacher(person) == null) {
		continue;
	    }

	    result.add(executionDegree);
	}

	return result;
    }

    public static List<ExecutionDegree> getAllCoordinatedByTeacher(Person person) {
	List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();

	if (person == null) {
	    return result;
	}

	for (Coordinator coordinator : person.getCoordinators()) {
	    result.add(coordinator.getExecutionDegree());
	}

	Comparator<ExecutionDegree> degreNameComparator = new Comparator<ExecutionDegree>() {

	    @Override
	    public int compare(ExecutionDegree o1, ExecutionDegree o2) {
		String name1 = o1.getDegreeCurricularPlan().getDegree().getName();
		String name2 = o2.getDegreeCurricularPlan().getDegree().getName();

		return String.CASE_INSENSITIVE_ORDER.compare(name1, name2);
	    }

	};

	Comparator<ExecutionDegree> yearComparator = new Comparator<ExecutionDegree>() {

	    @Override
	    public int compare(ExecutionDegree o1, ExecutionDegree o2) {
		String year1 = o1.getExecutionYear().getYear();
		String year2 = o2.getExecutionYear().getYear();

		return String.CASE_INSENSITIVE_ORDER.compare(year1, year2);
	    }

	};

	// sort by degreeCurricularPlan.degree.nome ascending,
	// executionYear.year descending
	ComparatorChain comparatorChain = new ComparatorChain();
	comparatorChain.addComparator(degreNameComparator, false);
	comparatorChain.addComparator(yearComparator, true);

	Collections.sort(result, comparatorChain);

	return result;
    }

    public static List<ExecutionDegree> getAllByExecutionYearAndDegreeType(String year, DegreeType... typeOfCourse) {

	if (year == null || typeOfCourse == null) {
	    return Collections.EMPTY_LIST;
	}

	final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(year);
	return getAllByExecutionYearAndDegreeType(executionYear, typeOfCourse);
    }

    public static List<ExecutionDegree> getAllByExecutionYearAndDegreeType(ExecutionYear executionYear,
	    DegreeType... typeOfCourse) {

	if (executionYear == null || typeOfCourse == null) {
	    return Collections.EMPTY_LIST;
	}

	final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
	for (final ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
	    boolean match = false;
	    for (DegreeType type : typeOfCourse) {
		match |= type.equals(executionDegree.getDegreeType());
	    }
	    if (!match) {
		continue;
	    }

	    result.add(executionDegree);
	}
	Collections.sort(result, COMPARATOR_BY_DEGREE_CURRICULAR_PLAN_ID_INTERNAL_DESC);

	return result;
    }

    public static List<ExecutionDegree> getAllByDegreeAndExecutionYear(Degree degree, String year) {
	List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();

	if (degree == null || year == null) {
	    return result;
	}

	ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(year);
	if (executionYear == null) {
	    return result;
	}

	for (ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
	    if (degree.equals(executionDegree.getDegreeCurricularPlan().getDegree())) {
		result.add(executionDegree);
	    }
	}

	return result;
    }

    public static List<ExecutionDegree> getAllByDegreeAndCurricularStage(Degree degree, CurricularStage stage) {
	List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();

	if (degree == null) {
	    return result;
	}

	if (stage == null) {
	    return result;
	}

	for (ExecutionDegree executionDegree : RootDomainObject.getInstance().getExecutionDegrees()) {
	    if (!degree.equals(executionDegree.getDegreeCurricularPlan().getDegree())) {
		continue;
	    }

	    if (!stage.equals(executionDegree.getDegreeCurricularPlan().getCurricularStage())) {
		continue;
	    }

	    result.add(executionDegree);
	}

	return result;
    }

    public static ExecutionDegree getByDegreeCurricularPlanAndExecutionYear(DegreeCurricularPlan degreeCurricularPlan,
	    ExecutionYear executionYear) {
	if (degreeCurricularPlan == null || executionYear == null) {
	    return null;
	}

	for (ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
	    if (executionYear == executionDegree.getExecutionYear()) {
		return executionDegree;
	    }
	}

	return null;
    }

    public static ExecutionDegree getByDegreeCurricularPlanAndExecutionYear(DegreeCurricularPlan degreeCurricularPlan,
	    String executionYear) {
	if (degreeCurricularPlan == null) {
	    return null;
	}

	if (executionYear == null) {
	    return null;
	}

	for (ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegrees()) {
	    if (executionYear.equalsIgnoreCase(executionDegree.getExecutionYear().getYear())) {
		return executionDegree;
	    }
	}

	return null;
    }

    public static ExecutionDegree getByDegreeCurricularPlanNameAndExecutionYear(String degreeName, ExecutionYear executionYear) {
	if (degreeName == null) {
	    return null;
	}

	if (executionYear == null) {
	    return null;
	}

	for (ExecutionDegree executionDegree : executionYear.getExecutionDegrees()) {
	    if (degreeName.equalsIgnoreCase(executionDegree.getDegreeCurricularPlan().getName())) {
		return executionDegree;
	    }
	}

	return null;
    }

    public static ExecutionDegree readByDegreeCodeAndExecutionYearAndCampus(String degreeCode, ExecutionYear executionYear,
	    Campus campus) {
	for (final Degree degree : Degree.readAllByDegreeCode(degreeCode)) {
	    final ExecutionDegree executionDegree = degree.getMostRecentDegreeCurricularPlan().getExecutionDegreeByYear(
		    executionYear);
	    if (executionDegree.getCampus() == campus) {
		return executionDegree;
	    }
	}

	return null;
    }

    public boolean isEvaluationDateInExamPeriod(Date evaluationDate, ExecutionSemester executionSemester,
	    MarkSheetType markSheetType) {
	return isSpecialAuthorization(markSheetType, executionSemester, evaluationDate)
		|| checkOccupationPeriod(evaluationDate, executionSemester, markSheetType);
    }

    private boolean isSpecialAuthorization(MarkSheetType markSheetType, ExecutionSemester executionSemester, Date evaluationDate) {
	return (markSheetType == MarkSheetType.SPECIAL_AUTHORIZATION);
    }

    private boolean checkOccupationPeriod(Date evaluationDate, ExecutionSemester executionSemester, MarkSheetType markSheetType) {
	OccupationPeriod occupationPeriod = getOccupationPeriodFor(executionSemester, markSheetType);
	return (evaluationDate != null && occupationPeriod != null && occupationPeriod
		.nestedOccupationPeriodsContainsDay(YearMonthDay.fromDateFields(evaluationDate)));
    }

    public OccupationPeriod getOccupationPeriodFor(ExecutionSemester executionSemester, MarkSheetType markSheetType) {
	OccupationPeriod occupationPeriod = null;
	switch (markSheetType) {
	case NORMAL:
	case IMPROVEMENT:
	    if (executionSemester.getSemester().equals(Integer.valueOf(1))) {
		occupationPeriod = this.getPeriodExamsFirstSemester();
	    } else {
		occupationPeriod = this.getPeriodExamsSecondSemester();
	    }
	    break;

	case SPECIAL_SEASON:
	    occupationPeriod = this.getPeriodExamsSpecialSeason();
	    break;

	default:
	}
	return occupationPeriod;
    }

    public List<Coordinator> getResponsibleCoordinators() {
	List<Coordinator> result = new ArrayList<Coordinator>();
	for (final Coordinator coordinator : getCoordinatorsList()) {
	    if (coordinator.getResponsible()) {
		result.add(coordinator);
	    }
	}
	return result;
    }

    public boolean hasAnyResponsibleCoordinators() {
	return (!getResponsibleCoordinators().isEmpty());
    }

    public boolean isCoordinationTeamFormed() {
	return hasAnyCoordinatorsList();
    }

    public boolean isCoordinationResponsibleChosen() {
	return hasAnyResponsibleCoordinators();
    }

    public boolean isDateInFirstSemesterNormalSeasonOfGradeSubmission(YearMonthDay date) {
	return (getPeriodGradeSubmissionNormalSeasonFirstSemester() != null && getPeriodGradeSubmissionNormalSeasonFirstSemester()
		.nestedOccupationPeriodsContainsDay(date));
    }

    public boolean isDateInSecondSemesterNormalSeasonOfGradeSubmission(YearMonthDay date) {
	return (getPeriodGradeSubmissionNormalSeasonSecondSemester() != null && getPeriodGradeSubmissionNormalSeasonSecondSemester()
		.nestedOccupationPeriodsContainsDay(date));
    }

    public boolean isDateInSpecialSeasonOfGradeSubmission(YearMonthDay date) {
	return (getPeriodGradeSubmissionSpecialSeason() != null && getPeriodGradeSubmissionSpecialSeason()
		.nestedOccupationPeriodsContainsDay(date));
    }

    final public String getPresentationName() {
	return getDegreeCurricularPlan().getPresentationName();
    }

    public String getDegreeName() {
	return getDegree().getNameFor(getExecutionYear()).getContent();
    }

    public Degree getDegree() {
	return getDegreeCurricularPlan().getDegree();
    }

    public DegreeType getDegreeType() {
	return getDegree().getDegreeType();
    }

    public Set<DFACandidacy> getDfaCandidacies() {
	return getDFACandidacies();
    }

    public Set<DegreeCandidacy> getDegreeCandidacies() {
	final Set<DegreeCandidacy> result = new HashSet<DegreeCandidacy>();

	for (final StudentCandidacy studentCandidacy : getStudentCandidacies()) {
	    if (studentCandidacy instanceof DegreeCandidacy) {
		result.add((DegreeCandidacy) studentCandidacy);
	    }
	}

	return result;
    }

    public Set<StudentCandidacy> getFirstCycleCandidacies() {
	final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
	for (final StudentCandidacy studentCandidacy : getStudentCandidacies()) {
	    if (studentCandidacy instanceof DegreeCandidacy || studentCandidacy instanceof IMDCandidacy) {
		result.add(studentCandidacy);
	    }
	}
	return result;
    }

    public Collection<StudentCandidacy> getFirstCycleCandidacies(final CandidacySituationType candidacySituationType) {
	final Collection<StudentCandidacy> result = new HashSet<StudentCandidacy>();
	for (final StudentCandidacy studentCandidacy : getStudentCandidaciesSet()) {
	    if ((studentCandidacy instanceof DegreeCandidacy || studentCandidacy instanceof IMDCandidacy)
		    && studentCandidacy.getActiveCandidacySituationType() == candidacySituationType) {
		result.add(studentCandidacy);
	    }
	}
	return result;
    }

    public List<Registration> getRegistrationsForDegreeCandidacies() {
	final List<Registration> result = new ArrayList<Registration>();
	for (final DegreeCandidacy degreeCandidacy : getDegreeCandidacies()) {
	    if (degreeCandidacy.hasRegistration()) {
		result.add(degreeCandidacy.getRegistration());
	    }
	}
	return result;
    }

    public List<Registration> getRegistrationsForFirstCycleCandidacies() {
	final List<Registration> result = new ArrayList<Registration>();
	for (final StudentCandidacy studentCandidacy : getFirstCycleCandidacies()) {
	    if (studentCandidacy.hasRegistration()) {
		result.add(studentCandidacy.getRegistration());
	    }
	}
	return result;
    }

    public Set<DFACandidacy> getDFACandidacies() {
	final Set<DFACandidacy> result = new HashSet<DFACandidacy>();

	for (final StudentCandidacy studentCandidacy : getStudentCandidacies()) {
	    if (studentCandidacy instanceof DFACandidacy) {
		result.add((DFACandidacy) studentCandidacy);
	    }
	}

	return result;
    }

    public List<ShiftDistributionEntry> getNextFreeShiftDistributions() {

	final ArrayList<ShiftDistributionEntry> entries = new ArrayList<ShiftDistributionEntry>(getShiftDistributionEntriesSet());
	Collections.sort(entries, ShiftDistributionEntry.NUMBER_COMPARATOR);

	for (final ShiftDistributionEntry shiftDistributionEntry : entries) {
	    if (!shiftDistributionEntry.getDistributed()) {
		return ShiftDistributionEntry.readByAbstractNumber(shiftDistributionEntry.getAbstractStudentNumber(),
			getExecutionYear());
	    }
	}

	return Collections.EMPTY_LIST;
    }

    public Integer getStudentNumberForShiftDistributionBasedOn(Integer studentNumberPosition) {
	final List<Integer> abstractStudentNumbers = new ArrayList<Integer>();
	for (final ShiftDistributionEntry shiftDistributionEntry : getShiftDistributionEntriesSet()) {
	    if (!abstractStudentNumbers.contains(shiftDistributionEntry.getAbstractStudentNumber())) {
		abstractStudentNumbers.add(shiftDistributionEntry.getAbstractStudentNumber());
	    }
	}
	Collections.sort(abstractStudentNumbers);
	return (!abstractStudentNumbers.isEmpty()) ? abstractStudentNumbers.get(studentNumberPosition) : null;
    }

    public List<ShiftDistributionEntry> getDistributedShiftsFor(Integer studentNumber) {
	return getShiftsFor(studentNumber, true);
    }

    private List<ShiftDistributionEntry> getShiftsFor(Integer studentNumber, boolean alreadyDistributed) {
	final List<ShiftDistributionEntry> result = new ArrayList<ShiftDistributionEntry>();
	for (final ShiftDistributionEntry shiftDistributionEntry : getShiftDistributionEntriesSet()) {
	    if (shiftDistributionEntry.getDistributed().booleanValue() == alreadyDistributed
		    && shiftDistributionEntry.getAbstractStudentNumber().equals(studentNumber)) {

		result.add(shiftDistributionEntry);
	    }
	}
	return result;
    }

    public List<DegreeCandidacy> getDegreeCandidaciesBy(CandidacySituationType candidacySituationType) {
	final List<DegreeCandidacy> result = new ArrayList<DegreeCandidacy>();
	for (final DegreeCandidacy candidacy : getDegreeCandidacies()) {
	    if (candidacy.getActiveCandidacySituation().getCandidacySituationType() == candidacySituationType) {
		result.add(candidacy);
	    }
	}
	return result;
    }

    public List<StudentCandidacy> getFirstCycleCandidaciesBy(CandidacySituationType candidacySituationType) {
	final List<StudentCandidacy> result = new ArrayList<StudentCandidacy>();
	for (final StudentCandidacy candidacy : getFirstCycleCandidacies()) {
	    if (candidacy.getActiveCandidacySituation().getCandidacySituationType() == candidacySituationType) {
		result.add(candidacy);
	    }
	}
	return result;
    }

    public List<DegreeCandidacy> getNotConcludedDegreeCandidacies() {
	final List<DegreeCandidacy> result = new ArrayList<DegreeCandidacy>();
	for (final DegreeCandidacy degreeCandidacy : getDegreeCandidacies()) {
	    if (!degreeCandidacy.isConcluded()) {
		result.add(degreeCandidacy);
	    }
	}

	return result;
    }

    public boolean isPersonInScientificCommission(Person person) {
	for (ScientificCommission commission : getScientificCommissionMembers()) {
	    if (commission.getPerson() == person) {
		return true;
	    }
	}

	return false;
    }

    /*
     * Returns a list of teachers from the coordinator department that can be
     * tutors of a student from the given execution degree
     */
    public List<Teacher> getPossibleTutorsFromExecutionDegreeDepartments() {
	List<Department> departments = this.getDegree().getDepartments();

	ArrayList<Teacher> possibleTeachers = new ArrayList<Teacher>();
	for (Department department : departments) {
	    possibleTeachers.addAll(department.getPossibleTutors());
	}

	return possibleTeachers;
    }

    public static class ThesisCreationPeriodFactoryExecutor implements FactoryExecutor, HasExecutionYear, Serializable {

	private ExecutionYear executionYear;

	private ExecutionDegree executionDegree;

	private YearMonthDay beginThesisCreationPeriod;

	private YearMonthDay endThesisCreationPeriod;

	@Override
	public Object execute() {
	    final ExecutionDegree executionDegree = getExecutionDegree();
	    if (executionDegree == null) {
		final ExecutionYear executionYear = getExecutionYear();
		if (executionYear != null) {
		    for (final ExecutionDegree otherExecutionDegree : executionYear.getExecutionDegreesSet()) {
			execute(otherExecutionDegree);
		    }
		}
	    } else {
		execute(executionDegree);
	    }
	    return null;
	}

	private void execute(final ExecutionDegree executionDegree) {
	    executionDegree.setBeginThesisCreationPeriod(beginThesisCreationPeriod);
	    executionDegree.setEndThesisCreationPeriod(endThesisCreationPeriod);
	}

	@Override
	public ExecutionYear getExecutionYear() {
	    return executionYear;
	}

	public void setExecutionYear(final ExecutionYear executionYear) {
	    this.executionYear = executionYear;
	}

	public ExecutionDegree getExecutionDegree() {
	    return executionDegree;
	}

	public void setExecutionDegree(final ExecutionDegree executionDegree) {
	    this.executionDegree = executionDegree;
	}

	public YearMonthDay getBeginThesisCreationPeriod() {
	    return beginThesisCreationPeriod;
	}

	public void setBeginThesisCreationPeriod(YearMonthDay beginThesisCreationPeriod) {
	    this.beginThesisCreationPeriod = beginThesisCreationPeriod;
	}

	public YearMonthDay getEndThesisCreationPeriod() {
	    return endThesisCreationPeriod;
	}

	public void setEndThesisCreationPeriod(YearMonthDay endThesisCreationPeriod) {
	    this.endThesisCreationPeriod = endThesisCreationPeriod;
	}

	public boolean hasExecutionYear() {
	    return getExecutionYear() != null;
	}

    }

    public boolean isScientificCommissionMember() {
	final Person person = AccessControl.getPerson();
	return isScientificCommissionMember(person);
    }

    public boolean isScientificCommissionMember(final Person person) {
	for (final ScientificCommission scientificCommission : getScientificCommissionMembersSet()) {
	    if (person == scientificCommission.getPerson()) {
		return true;
	    }
	}
	return false;
    }

    public static List<ExecutionDegree> filterByAcademicInterval(AcademicInterval academicInterval) {
	AcademicCalendarEntry academicCalendarEntry = academicInterval.getAcademicCalendarEntry();
	while (!(academicCalendarEntry instanceof AcademicCalendarRootEntry)) {
	    if (academicCalendarEntry instanceof AcademicYearCE) {
		ExecutionYear year = ExecutionYear.getExecutionYear((AcademicYearCE) academicCalendarEntry);
		List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
		result.addAll(year.getExecutionDegrees());
		return result;
	    } else
		academicCalendarEntry = academicCalendarEntry.getParentEntry();
	}

	return Collections.EMPTY_LIST;
    }

    public AcademicInterval getAcademicInterval() {
	return getExecutionYear().getAcademicInterval();
    }

    public CoordinatorExecutionDegreeCoursesReport getExecutionDegreeCoursesReports(final ExecutionInterval executionInterval) {
	for (final CoordinatorExecutionDegreeCoursesReport coordinatorExecutionDegreeCoursesReport : getExecutionDegreeCoursesReports()) {
	    if (coordinatorExecutionDegreeCoursesReport.getExecutionInterval() == executionInterval) {
		return coordinatorExecutionDegreeCoursesReport;
	    }
	}
	return null;
    }

    public Set<Proposal> getProposals() {
	Set<Proposal> proposals;
	if (hasScheduling()) {
	    return getScheduling().getProposalsSet();
	}
	return Collections.EMPTY_SET;
    }

    public OccupationPeriod getPeriodLessons(final ExecutionSemester executionSemester) {
	if (executionSemester.getSemester().intValue() == 1) {
	    return getPeriodLessonsFirstSemester();
	} else if (executionSemester.getSemester().intValue() == 2) {
	    return getPeriodLessonsSecondSemester();
	} else {
	    throw new Error("unexpected.semester: " + executionSemester.getSemester());
	}
    }

    public InquiryCoordinatorAnswer getInquiryCoordinationAnswers(ExecutionSemester executionSemester) {
	for (InquiryCoordinatorAnswer inquiryCoordinatorAnswer : getInquiryCoordinationAnswers()) {
	    if (inquiryCoordinatorAnswer.getExecutionSemester() == executionSemester) {
		return inquiryCoordinatorAnswer;
	    }
	}
	return null;
    }

    /*
     * Deprecated methods, created due to the refactoring
     */

    @Deprecated
    @Override
    public OccupationPeriod getPeriodLessonsFirstSemester() {

	// return getPeriodsByTypeAndSemester(OccupationPeriodType.LESSONS,
	// 1).get(0);

	return super.getPeriodLessonsFirstSemester();
    }

    @Deprecated
    @Override
    public OccupationPeriod getPeriodLessonsSecondSemester() {
	return super.getPeriodLessonsSecondSemester();
    }

    @Deprecated
    @Override
    public OccupationPeriod getPeriodExamsFirstSemester() {
	return super.getPeriodExamsFirstSemester();
    }

    @Deprecated
    @Override
    public OccupationPeriod getPeriodExamsSecondSemester() {
	return super.getPeriodExamsSecondSemester();
    }

    @Deprecated
    @Override
    public OccupationPeriod getPeriodExamsSpecialSeason() {
	return super.getPeriodExamsSpecialSeason();
    }

    @Deprecated
    @Override
    public OccupationPeriod getPeriodGradeSubmissionNormalSeasonFirstSemester() {
	return super.getPeriodGradeSubmissionNormalSeasonFirstSemester();
    }

    @Deprecated
    @Override
    public OccupationPeriod getPeriodGradeSubmissionNormalSeasonSecondSemester() {
	return super.getPeriodGradeSubmissionNormalSeasonSecondSemester();
    }

    @Deprecated
    @Override
    public OccupationPeriod getPeriodGradeSubmissionSpecialSeason() {
	return super.getPeriodGradeSubmissionSpecialSeason();
    }

    @Deprecated
    @Override
    public void setPeriodLessonsFirstSemester(OccupationPeriod period) {

	super.setPeriodLessonsFirstSemester(period);

	addPeriodForType(period, OccupationPeriodType.LESSONS, 1);

    }

    @Deprecated
    @Override
    public void setPeriodLessonsSecondSemester(OccupationPeriod period) {

	super.setPeriodLessonsSecondSemester(period);

	addPeriodForType(period, OccupationPeriodType.LESSONS, 2);

    }

    @Deprecated
    @Override
    public void setPeriodExamsFirstSemester(OccupationPeriod period) {

	super.setPeriodExamsFirstSemester(period);

	addPeriodForType(period, OccupationPeriodType.EXAMS, 1);

    }

    @Deprecated
    @Override
    public void setPeriodExamsSecondSemester(OccupationPeriod period) {

	super.setPeriodExamsSecondSemester(period);

	addPeriodForType(period, OccupationPeriodType.EXAMS, 2);

    }

    @Deprecated
    @Override
    public void setPeriodExamsSpecialSeason(OccupationPeriod period) {

	super.setPeriodExamsSpecialSeason(period);

	addPeriodForType(period, OccupationPeriodType.EXAMS_SPECIAL_SEASON, null);

    }

    @Deprecated
    @Override
    public void setPeriodGradeSubmissionNormalSeasonFirstSemester(OccupationPeriod period) {

	super.setPeriodGradeSubmissionNormalSeasonFirstSemester(period);

	addPeriodForType(period, OccupationPeriodType.GRADE_SUBMISSION, 1);

    }

    @Deprecated
    @Override
    public void setPeriodGradeSubmissionNormalSeasonSecondSemester(OccupationPeriod period) {

	super.setPeriodGradeSubmissionNormalSeasonSecondSemester(period);

	addPeriodForType(period, OccupationPeriodType.GRADE_SUBMISSION, 2);

    }

    @Deprecated
    @Override
    public void setPeriodGradeSubmissionSpecialSeason(OccupationPeriod period) {

	super.setPeriodGradeSubmissionSpecialSeason(period);

	addPeriodForType(period, OccupationPeriodType.GRADE_SUBMISSION_SPECIAL_SEASON, null);
    }

    @Deprecated
    @Override
    public void removePeriodLessonsFirstSemester() {
	super.removePeriodLessonsFirstSemester();
	removeReferencesOfType(OccupationPeriodType.LESSONS, 1);
    }

    @Deprecated
    @Override
    public void removePeriodLessonsSecondSemester() {
	super.removePeriodLessonsSecondSemester();
	removeReferencesOfType(OccupationPeriodType.LESSONS, 2);
    }

    @Deprecated
    @Override
    public void removePeriodExamsFirstSemester() {
	super.removePeriodExamsFirstSemester();
	removeReferencesOfType(OccupationPeriodType.EXAMS, 1);
    }

    @Deprecated
    @Override
    public void removePeriodExamsSecondSemester() {
	super.removePeriodExamsSecondSemester();
	removeReferencesOfType(OccupationPeriodType.EXAMS, 2);
    }

    @Deprecated
    @Override
    public void removePeriodExamsSpecialSeason() {
	super.removePeriodExamsSpecialSeason();
	removeReferencesOfType(OccupationPeriodType.EXAMS_SPECIAL_SEASON, null);
    }

    @Deprecated
    @Override
    public void removePeriodGradeSubmissionNormalSeasonFirstSemester() {
	super.removePeriodGradeSubmissionNormalSeasonFirstSemester();
	removeReferencesOfType(OccupationPeriodType.GRADE_SUBMISSION, 1);
    }

    @Deprecated
    @Override
    public void removePeriodGradeSubmissionNormalSeasonSecondSemester() {
	super.removePeriodGradeSubmissionNormalSeasonSecondSemester();
	removeReferencesOfType(OccupationPeriodType.GRADE_SUBMISSION, 2);
    }

    @Deprecated
    @Override
    public void removePeriodGradeSubmissionSpecialSeason() {
	super.removePeriodGradeSubmissionSpecialSeason();
	removeReferencesOfType(OccupationPeriodType.GRADE_SUBMISSION_SPECIAL_SEASON, null);
    }

    /*
     * Temporary method to update the new relation. With some refactoring, might
     * become the actual method on the new API
     */

    private void addPeriodForType(OccupationPeriod period, OccupationPeriodType type, Integer semester) {

	List<OccupationPeriodReference> periods = this.getPeriodReferences(type, semester, null);

	if (periods.size() == 0) {
	    periods.add(new OccupationPeriodReference(period, this, type, semester, null));
	    return;
	}

	for (OccupationPeriodReference reference : periods)
	    reference.setOccupationPeriod(period);

    }

    private void removeReferencesOfType(OccupationPeriodType type, Integer semester) {

	List<OccupationPeriodReference> periods = this.getPeriodReferences(type, semester, null);

	for (OccupationPeriodReference reference : periods)
	    reference.delete();

    }

    /*
     * New API
     */

    public List<OccupationPeriod> getPeriods(OccupationPeriodType type, Integer semester, List<Integer> years) {
	List<OccupationPeriod> periods = new ArrayList<OccupationPeriod>();
	for (Iterator<OccupationPeriodReference> references = getOccupationPeriodReferences().iterator(); references.hasNext();) {
	    OccupationPeriodReference reference = references.next();

	    if (type != null && reference.getPeriodType() != type)
		continue;

	    if (semester != null && reference.getSemester() != semester)
		continue;

	    if (years != null && !reference.getCurricularYears().containsYears(years))
		continue;

	    periods.add(reference.getOccupationPeriod());

	}
	return periods;
    }

    public List<OccupationPeriodReference> getPeriodReferences(OccupationPeriodType type, Integer semester, List<Integer> years) {
	List<OccupationPeriodReference> periods = new ArrayList<OccupationPeriodReference>();
	for (Iterator<OccupationPeriodReference> references = getOccupationPeriodReferences().iterator(); references.hasNext();) {
	    OccupationPeriodReference reference = references.next();

	    if (type != null && reference.getPeriodType() != type)
		continue;

	    if (semester != null && reference.getSemester() != semester)
		continue;

	    if (years != null && !reference.getCurricularYears().containsYears(years))
		continue;

	    periods.add(reference);

	}
	return periods;
    }

    public List<OccupationPeriod> getPeriodsByType(OccupationPeriodType type) {
	return getPeriods(type, null, null);
    }

    public List<OccupationPeriod> getPeriodsBySemester(Integer semester) {
	return getPeriods(null, semester, null);
    }

    public List<OccupationPeriod> getPeriodsByTypeAndSemester(OccupationPeriodType type, Integer semester) {
	return getPeriods(type, semester, null);
    }

    public List<OccupationPeriod> getPeriodsByCurricularYear(Integer year) {
	return getPeriods(null, null, Collections.singletonList(year));
    }

    public List<OccupationPeriod> getPeriodsByCurricularYears(List<Integer> years) {
	return getPeriods(null, null, years);
    }
}
