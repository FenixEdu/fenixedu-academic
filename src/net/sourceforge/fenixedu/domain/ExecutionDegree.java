/*
 * ExecutionDegree.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package net.sourceforge.fenixedu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.fenixedu.domain.candidacy.CandidacySituationType;
import net.sourceforge.fenixedu.domain.candidacy.DFACandidacy;
import net.sourceforge.fenixedu.domain.candidacy.DegreeCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy;
import net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistributionEntry;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.degreeStructure.CurricularStage;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.domain.studentCurricularPlan.Specialization;
import net.sourceforge.fenixedu.util.SituationName;
import net.sourceforge.fenixedu.util.State;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.joda.time.YearMonthDay;

/**
 * 
 * @author rpfi
 */

public class ExecutionDegree extends ExecutionDegree_Base implements Comparable {

    public static final Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME;

    public static final Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR;

    public static final Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATORY_BY_YEAR;
    static {
        final Comparator degreeTypeComparator = new BeanComparator(
                "degreeCurricularPlan.degree.tipoCurso");
        final Comparator degreeNameComparator = new BeanComparator("degreeCurricularPlan.degree.nome");
        EXECUTION_DEGREE_COMPARATORY_BY_YEAR = new BeanComparator("executionYear.year");
        EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME = new ComparatorChain();
        ((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME)
                .addComparator(degreeTypeComparator);
        ((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME)
                .addComparator(degreeNameComparator);
        EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR = new ComparatorChain();
        ((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR)
                .addComparator(degreeTypeComparator);
        ((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR)
                .addComparator(degreeNameComparator);
        ((ComparatorChain) EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR)
                .addComparator(EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
    }

    private ExecutionDegree() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
    }

    protected ExecutionDegree(DegreeCurricularPlan degreeCurricularPlan, ExecutionYear executionYear,
            Campus campus, Boolean temporaryExamMap) {
        this();

        if (degreeCurricularPlan == null || executionYear == null || campus == null) {
            throw new DomainException("execution.degree.null.args.to.constructor");
        }

        setDegreeCurricularPlan(degreeCurricularPlan);
        setExecutionYear(executionYear);
        setCampus(campus);
        setTemporaryExamMap(temporaryExamMap);
    }

    public void delete() {
        if (canBeDeleted()) {
            for (; hasAnyCoordinatorsList(); getCoordinatorsList().get(0).delete())
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
            occupationPeriodReference.deleteIfEmpty();
        }
    }

    private void deletePeriodGradeSubmissionNormalSeasonSecondSemester() {
        OccupationPeriod occupationPeriodReference = getPeriodGradeSubmissionNormalSeasonSecondSemester();
        if (occupationPeriodReference != null) {
            removePeriodGradeSubmissionNormalSeasonSecondSemester();
            occupationPeriodReference.deleteIfEmpty();
        }
    }

    private void deletePeriodGradeSubmissionNormalSeasonFirstSemester() {
        OccupationPeriod occupationPeriodReference = getPeriodGradeSubmissionNormalSeasonFirstSemester();
        if (occupationPeriodReference != null) {
            removePeriodGradeSubmissionNormalSeasonFirstSemester();
            occupationPeriodReference.deleteIfEmpty();
        }
    }

    private void deletePeriodExamsSpecialSeason() {
        OccupationPeriod occupationPeriodReference = getPeriodExamsSpecialSeason();
        if (occupationPeriodReference != null) {
            removePeriodExamsSpecialSeason();
            occupationPeriodReference.deleteIfEmpty();
        }
    }

    private void deletePeriodExamsSecondSemester() {
        OccupationPeriod occupationPeriodReference = getPeriodExamsSecondSemester();
        if (occupationPeriodReference != null) {
            removePeriodExamsSecondSemester();
            occupationPeriodReference.deleteIfEmpty();
        }
    }

    private void deletePeriodExamsFirstSemester() {
        OccupationPeriod occupationPeriodReference = getPeriodExamsFirstSemester();
        if (occupationPeriodReference != null) {
            removePeriodExamsFirstSemester();
            occupationPeriodReference.deleteIfEmpty();
        }
    }

    private void deletePeriodLessonsSecondSemester() {
        OccupationPeriod occupationPeriodReference = getPeriodLessonsSecondSemester();
        if (occupationPeriodReference != null) {
            removePeriodLessonsSecondSemester();
            occupationPeriodReference.deleteIfEmpty();
        }
    }

    private void deletePeriodLessonsFirstSemester() {
        OccupationPeriod occupationPeriodReference = getPeriodLessonsFirstSemester();
        if (occupationPeriodReference != null) {
            removePeriodLessonsFirstSemester();
            occupationPeriodReference.deleteIfEmpty();
        }
    }

    public boolean canBeDeleted() {
        return (!hasAnySchoolClasses() && !hasAnyMasterDegreeCandidates() && !hasAnyGuides()
                && !hasScheduling() && !hasAnyAssociatedFinalDegreeWorkGroups()
                && getPeriodLessonsFirstSemester().getRoomOccupations().isEmpty()
                && getPeriodLessonsSecondSemester().getRoomOccupations().isEmpty()
                && getPeriodExamsFirstSemester().getRoomOccupations().isEmpty() && getPeriodExamsSecondSemester()
                .getRoomOccupations().isEmpty());
    }

    public void edit(ExecutionYear executionYear, Campus campus, Boolean temporaryExamMap,
            OccupationPeriod periodLessonsFirstSemester, OccupationPeriod periodExamsFirstSemester,
            OccupationPeriod periodLessonsSecondSemester, OccupationPeriod periodExamsSecondSemester,
            OccupationPeriod periodExamsSpecialSeason,
            OccupationPeriod gradeSubmissionNormalSeasonFirstSemester,
            OccupationPeriod gradeSubmissionNormalSeasonSecondSemester,
            OccupationPeriod gradeSubmissionSpecialSeason) {

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

    public boolean isBolonha() {
        return this.getDegreeCurricularPlan().isBolonha();
    }

    public int compareTo(Object object) {
        final ExecutionDegree executionDegree = (ExecutionDegree) object;
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

        List<ExecutionDegree> executionDegrees = this.getDegreeCurricularPlan().getExecutionDegrees();

        ExecutionDegree firstExecutionDegree = (ExecutionDegree) Collections.min(executionDegrees,
                new BeanComparator("executionYear.year"));

        if (firstExecutionDegree.equals(this)) {
            return true;
        }

        return false;
    }

    public Set<Shift> findAvailableShifts(final CurricularYear curricularYear,
            final ExecutionPeriod executionPeriod) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final Set<Shift> shifts = new HashSet<Shift>();
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(curricularYear,
                    degreeCurricularPlan, executionPeriod)) {
                for (final ExecutionCourse executionCourse : curricularCourse
                        .getAssociatedExecutionCourses()) {
                    if (executionCourse.getExecutionPeriod() == executionPeriod) {
                        shifts.addAll(executionCourse.getAssociatedShifts());
                    }
                }
            }
        }
        return shifts;
    }

    public Set<SchoolClass> findSchoolClassesByExecutionPeriod(final ExecutionPeriod executionPeriod) {
        final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
        for (final SchoolClass schoolClass : getSchoolClasses()) {
            if (schoolClass.getExecutionPeriod() == executionPeriod) {
                schoolClasses.add(schoolClass);
            }
        }
        return schoolClasses;
    }

    public Set<SchoolClass> findSchoolClassesByExecutionPeriodAndCurricularYear(
            final ExecutionPeriod executionPeriod, final Integer curricularYear) {
        final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
        for (final SchoolClass schoolClass : getSchoolClasses()) {
            if (schoolClass.getExecutionPeriod() == executionPeriod
                    && schoolClass.getAnoCurricular().equals(curricularYear)) {
                schoolClasses.add(schoolClass);
            }
        }
        return schoolClasses;
    }

    public SchoolClass findSchoolClassesByExecutionPeriodAndName(final ExecutionPeriod executionPeriod,
            final String name) {
        for (final SchoolClass schoolClass : getSchoolClasses()) {
            if (schoolClass.getExecutionPeriod() == executionPeriod
                    && schoolClass.getNome().equals(name)) {
                return schoolClass;
            }
        }
        return null;
    }

    public List<CandidateSituation> getCandidateSituationsInSituation(List<SituationName> situationNames) {
        List<CandidateSituation> result = new ArrayList<CandidateSituation>();

        for (MasterDegreeCandidate candidate : getMasterDegreeCandidates()) {
            for (CandidateSituation situation : candidate.getSituations()) {

                if (situation.getValidation().getState() == null
                        || situation.getValidation().getState() != State.ACTIVE) {
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

    public Coordinator getCoordinatorByTeacher(Teacher teacher) {
        for (Coordinator coordinator : getCoordinatorsList()) {
            if (coordinator.getTeacher().equals(teacher)) {
                return coordinator;
            }
        }

        return null;
    }

    public MasterDegreeCandidate getMasterDegreeCandidateBySpecializationAndCandidateNumber(
            Specialization specialization, Integer candidateNumber) {

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
            if (masterDegreeCandidate.getSpecialization() == specialization
                    && masterDegreeCandidate.getCandidateNumber() != null) {
                maxCandidateNumber = Math.max(maxCandidateNumber, masterDegreeCandidate
                        .getCandidateNumber());
            }
        }
        return Integer.valueOf(++maxCandidateNumber);
    }

    private static Comparator<ExecutionDegree> COMPARATOR_BY_DEGREE_CURRICULAR_PLAN_ID_INTERNAL_DESC = new Comparator<ExecutionDegree>() {
        public int compare(ExecutionDegree o1, ExecutionDegree o2) {
            return o2.getDegreeCurricularPlan().getIdInternal().compareTo(
                    o1.getDegreeCurricularPlan().getIdInternal());
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

        for (final ExecutionDegree executionDegree : RootDomainObject.getInstance()
                .getExecutionDegrees()) {
            if (executionDegree.getExecutionYear() == executionYear) {
                result.add(executionDegree);
            }
        }
        Collections.sort(result, COMPARATOR_BY_DEGREE_CURRICULAR_PLAN_ID_INTERNAL_DESC);

        return result;
    }

    public static List<ExecutionDegree> getAllByExecutionCourseAndTeacher(
            ExecutionCourse executionCourse, Teacher teacher) {
        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();

        for (ExecutionDegree executionDegree : RootDomainObject.getInstance().getExecutionDegrees()) {
            boolean matchExecutionCourse = false;
            for (CurricularCourse curricularCourse : executionDegree.getDegreeCurricularPlan()
                    .getCurricularCourses()) {
                if (curricularCourse.getAssociatedExecutionCourses().contains(executionCourse)) {
                    matchExecutionCourse = true;
                    break;
                }
            }

            if (!matchExecutionCourse) {
                continue;
            }

            // if teacher is not a coordinator of the executionDegree
            if (executionDegree.getCoordinatorByTeacher(teacher) == null) {
                continue;
            }

            result.add(executionDegree);
        }

        return result;
    }

    public static List<ExecutionDegree> getAllCoordinatedByTeacher(Teacher teacher) {
        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();

        if (teacher == null) {
            return result;
        }

        for (Coordinator coordinator : teacher.getCoordinators()) {
            result.add(coordinator.getExecutionDegree());
        }

        Comparator<ExecutionDegree> degreNameComparator = new Comparator<ExecutionDegree>() {

            public int compare(ExecutionDegree o1, ExecutionDegree o2) {
                String name1 = o1.getDegreeCurricularPlan().getDegree().getName();
                String name2 = o2.getDegreeCurricularPlan().getDegree().getName();

                return String.CASE_INSENSITIVE_ORDER.compare(name1, name2);
            }

        };

        Comparator<ExecutionDegree> yearComparator = new Comparator<ExecutionDegree>() {

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

    public static List<ExecutionDegree> getAllByExecutionYearAndDegreeType(String year,
            DegreeType typeOfCourse) {

        if (year == null || typeOfCourse == null) {
            return Collections.EMPTY_LIST;
        }

        final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();

        for (final ExecutionDegree executionDegree : RootDomainObject.getInstance()
                .getExecutionDegrees()) {
            if (!year.equalsIgnoreCase(executionDegree.getExecutionYear().getYear())) {
                continue;
            }

            if (!typeOfCourse.equals(executionDegree.getDegreeCurricularPlan().getDegree()
                    .getTipoCurso())) {
                continue;
            }

            result.add(executionDegree);
        }
        Collections.sort(result, COMPARATOR_BY_DEGREE_CURRICULAR_PLAN_ID_INTERNAL_DESC);

        return result;
    }

    public static List<ExecutionDegree> getAllByExecutionYearAndDegreeType(ExecutionYear executionYear,
            DegreeType typeOfCourse) {

        if (executionYear == null || typeOfCourse == null) {
            return Collections.EMPTY_LIST;
        }

        final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        for (final ExecutionDegree executionDegree : RootDomainObject.getInstance()
                .getExecutionDegrees()) {

            if (executionDegree.getExecutionYear() != executionYear) {
                continue;
            }

            if (!typeOfCourse.equals(executionDegree.getDegreeCurricularPlan().getDegree()
                    .getTipoCurso())) {
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

    public static List<ExecutionDegree> getAllByDegreeAndCurricularStage(Degree degree,
            CurricularStage stage) {
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

    public static ExecutionDegree getByDegreeCurricularPlanAndExecutionYear(
            DegreeCurricularPlan degreeCurricularPlan, String executionYear) {
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

    public static ExecutionDegree getByDegreeCurricularPlanNameAndExecutionYear(String degreeName,
            ExecutionYear executionYear) {
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
    
    public static ExecutionDegree readByDegreeCodeAndExecutionYearAndCampus(String degreeCode,
	    ExecutionYear executionYear, Campus campus) {
	for (final Degree degree : Degree.readAllByDegreeCode(degreeCode)) {
	    final ExecutionDegree executionDegree = degree.getMostRecentDegreeCurricularPlan()
		    .getExecutionDegreeByYear(executionYear);
	    if (executionDegree.getCampus() == campus) {
		return executionDegree;
	    }
	}

	return null;
    }

    public boolean isEvaluationDateInExamPeriod(Date evaluationDate, ExecutionPeriod executionPeriod,
            MarkSheetType markSheetType) {
        return isSpecialAuthorization(markSheetType, executionPeriod, evaluationDate)
                || checkOccupationPeriod(evaluationDate, executionPeriod, markSheetType);
    }

    private boolean isSpecialAuthorization(MarkSheetType markSheetType, ExecutionPeriod executionPeriod,
            Date evaluationDate) {
        return (markSheetType == MarkSheetType.SPECIAL_AUTHORIZATION);
    }

    private boolean checkOccupationPeriod(Date evaluationDate, ExecutionPeriod executionPeriod,
            MarkSheetType markSheetType) {
        OccupationPeriod occupationPeriod = getOccupationPeriodFor(executionPeriod, markSheetType);
        return (evaluationDate != null && occupationPeriod != null && occupationPeriod
                .containsDay(evaluationDate));
    }

    public OccupationPeriod getOccupationPeriodFor(ExecutionPeriod executionPeriod,
            MarkSheetType markSheetType) {
        OccupationPeriod occupationPeriod = null;
        switch (markSheetType) {
        case NORMAL:
        case IMPROVEMENT:
            if (executionPeriod.getSemester().equals(Integer.valueOf(1))) {
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

    public boolean isDateInFirstSemesterNormalSeasonOfGradeSubmission(YearMonthDay date) {
        return (getPeriodGradeSubmissionNormalSeasonFirstSemester() != null && getPeriodGradeSubmissionNormalSeasonFirstSemester()
                .containsDay(date));
    }

    public boolean isDateInSecondSemesterNormalSeasonOfGradeSubmission(YearMonthDay date) {
        return (getPeriodGradeSubmissionNormalSeasonSecondSemester() != null && getPeriodGradeSubmissionNormalSeasonSecondSemester()
                .containsDay(date));
    }

    public boolean isDateInSpecialSeasonOfGradeSubmission(YearMonthDay date) {
        return (getPeriodGradeSubmissionSpecialSeason() != null && getPeriodGradeSubmissionSpecialSeason()
                .containsDay(date));
    }

    public Degree getDegree() {
        return getDegreeCurricularPlan().getDegree();
    }

    public Set<DFACandidacy> getDfaCandidacies() {
        final Set<DFACandidacy> result = new HashSet<DFACandidacy>();

        for (final StudentCandidacy studentCandidacy : getStudentCandidacies()) {
            if (studentCandidacy instanceof DFACandidacy) {
                result.add((DFACandidacy) studentCandidacy);
            }
        }

        return result;
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
    
    public List<Registration> getRegistrationsForDegreeCandidacies() {
	final List<Registration> result = new ArrayList<Registration>();
	for (final DegreeCandidacy degreeCandidacy : getDegreeCandidacies()) {
	    if (degreeCandidacy.hasRegistration()) {
		result.add(degreeCandidacy.getRegistration());
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
    
    public List<Shift> getShiftsFromShiftDistributionBasedOn(Integer studentNumberPosition) {
	final Integer studentNumber = getStudentNumberForShiftDistributionBasedOn(studentNumberPosition);
	if (studentNumber == null) {
	    throw new DomainException(
		    "error.candidacy.degree.ShiftDistribution.invalid.studentNumberPosition");
	}
	return getShiftsFor(studentNumber);
    }

    private Integer getStudentNumberForShiftDistributionBasedOn(Integer studentNumberPosition) {
	final List<Integer> abstractStudentNumbers = new ArrayList<Integer>();
	for (final ShiftDistributionEntry shiftDistributionEntry : getShiftDistributionEntriesSet()) {
	    if (!abstractStudentNumbers.contains(shiftDistributionEntry.getAbstractStudentNumber())) {
		abstractStudentNumbers.add(shiftDistributionEntry.getAbstractStudentNumber());
	    }
	}
	Collections.sort(abstractStudentNumbers);
	return (!abstractStudentNumbers.isEmpty()) ? abstractStudentNumbers.get(studentNumberPosition)
		: null;
    }

    private List<Shift> getShiftsFor(Integer studentNumber) {
	final List<Shift> result = new ArrayList<Shift>();
	for (final ShiftDistributionEntry shiftDistributionEntry : getShiftDistributionEntriesSet()) {
	    if (!shiftDistributionEntry.alreadyDistributed()
		    && shiftDistributionEntry.getAbstractStudentNumber().equals(studentNumber)) {
		shiftDistributionEntry.setDistributed(Boolean.TRUE);
		result.add(shiftDistributionEntry.getShift());
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

}
