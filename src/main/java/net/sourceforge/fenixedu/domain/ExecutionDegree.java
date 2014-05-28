/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;
import org.joda.time.YearMonthDay;

import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Iterables;

/**
 * 
 * @author rpfi
 * @author Joao Carvalho (joao.pedro.carvalho@ist.utl.pt)
 */

public class ExecutionDegree extends ExecutionDegree_Base implements Comparable<ExecutionDegree> {

    public static final Comparator<ExecutionDegree> COMPARATOR_BY_DEGREE_CODE = new Comparator<ExecutionDegree>() {

        @Override
        public int compare(ExecutionDegree o1, ExecutionDegree o2) {
            final int dcc = o1.getDegree().getSigla().compareTo(o2.getDegree().getSigla());
            return dcc == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : dcc;
        }

    };

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

    static final public Comparator<ExecutionDegree> REVERSE_EXECUTION_DEGREE_COMPARATORY_BY_YEAR =
            new Comparator<ExecutionDegree>() {

                @Override
                public int compare(ExecutionDegree o1, ExecutionDegree o2) {
                    return o1.getExecutionYear().compareTo(o2.getExecutionYear());
                }
            };

    static final public Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME =
            new Comparator<ExecutionDegree>() {

                @Override
                public int compare(ExecutionDegree o1, ExecutionDegree o2) {
                    return Degree.COMPARATOR_BY_DEGREE_TYPE_AND_NAME_AND_ID.compare(o1.getDegree(), o2.getDegree());
                }
            };

    static final public Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATORY_BY_DEGREE_TYPE_AND_NAME_AND_EXECUTION_YEAR =
            new Comparator<ExecutionDegree>() {

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
        setRootDomainObject(Bennu.getInstance());
    }

    protected ExecutionDegree(DegreeCurricularPlan degreeCurricularPlan, ExecutionYear executionYear, Space campus,
            Boolean publishedExamMap) {
        this();

        if (degreeCurricularPlan == null || executionYear == null || campus == null) {
            throw new DomainException("execution.degree.null.args.to.constructor");
        }

        setDegreeCurricularPlan(degreeCurricularPlan);
        setExecutionYear(executionYear);
        setCampus(campus);

        if (publishedExamMap) {
            getPublishedExamMaps().addAll(executionYear.getExecutionPeriods());
        }

    }

    public boolean canBeDeleted() {
        return !hasAnySchoolClasses() && !hasAnyMasterDegreeCandidates() && !hasAnyGuides() && !hasScheduling()
                && !hasAnyAssociatedFinalDegreeWorkGroups() && !hasAnyAssociatedInquiriesCoursesByCourse()
                && !hasAnyAssociatedInquiriesCoursesByStudent() && !hasAnyStudentCandidacies()
                && !hasAnyShiftDistributionEntries();
    }

    public void delete() {

        if (canBeDeleted()) {

            for (; hasAnyCoordinatorsList(); getCoordinatorsList().iterator().next().delete()) {
                ;
            }
            for (; hasAnyScientificCommissionMembers(); getScientificCommissionMembers().iterator().next().delete()) {
                ;
            }

            if (hasGratuityValues()) {
                getGratuityValues().delete();
            }

            setExecutionYear(null);
            setDegreeCurricularPlan(null);
            setCampus(null);

            for (OccupationPeriodReference reference : getOccupationPeriodReferences()) {
                reference.delete();
            }

            setRootDomainObject(null);
            deleteDomainObject();

        } else {
            throw new DomainException("execution.degree.cannot.be.deleted");
        }
    }

    public void edit(ExecutionYear executionYear, Space campus, Boolean publishedExamMap,
            OccupationPeriod periodLessonsFirstSemester, OccupationPeriod periodExamsFirstSemester,
            OccupationPeriod periodLessonsSecondSemester, OccupationPeriod periodExamsSecondSemester,
            OccupationPeriod periodExamsSpecialSeason, OccupationPeriod gradeSubmissionNormalSeasonFirstSemester,
            OccupationPeriod gradeSubmissionNormalSeasonSecondSemester, OccupationPeriod gradeSubmissionSpecialSeason) {

        setExecutionYear(executionYear);
        setCampus(campus);

        for (ExecutionSemester executionSemester : this.getExecutionYear().getExecutionPeriods()) {
            if (publishedExamMap) {
                this.getPublishedExamMaps().add(executionSemester);
            } else {
                this.getPublishedExamMaps().remove(executionSemester);
            }
        }

        if (periodLessonsFirstSemester != getPeriodLessonsFirstSemester()) {
            setPeriodLessonsFirstSemester(periodLessonsFirstSemester);
        }

        if (periodExamsFirstSemester != getPeriodExamsFirstSemester()) {
            setPeriodExamsFirstSemester(periodExamsFirstSemester);
        }

        if (periodLessonsSecondSemester != getPeriodLessonsSecondSemester()) {
            setPeriodLessonsSecondSemester(periodLessonsSecondSemester);
        }

        if (periodExamsSecondSemester != getPeriodExamsSecondSemester()) {
            setPeriodExamsSecondSemester(periodExamsSecondSemester);
        }

        if (periodExamsSpecialSeason != getPeriodExamsSpecialSeason()) {
            setPeriodExamsSpecialSeason(periodExamsSpecialSeason);
        }

        if (gradeSubmissionNormalSeasonFirstSemester != getPeriodGradeSubmissionNormalSeasonFirstSemester()) {
            setPeriodGradeSubmissionNormalSeasonFirstSemester(gradeSubmissionNormalSeasonFirstSemester);
        }

        if (gradeSubmissionNormalSeasonSecondSemester != getPeriodGradeSubmissionNormalSeasonSecondSemester()) {
            setPeriodGradeSubmissionNormalSeasonSecondSemester(gradeSubmissionNormalSeasonSecondSemester);
        }

        if (gradeSubmissionSpecialSeason != getPeriodGradeSubmissionSpecialSeason()) {
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
        final Collection<ExecutionDegree> executionDegrees = this.getDegreeCurricularPlan().getExecutionDegrees();
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

    private static Comparator<ExecutionDegree> COMPARATOR_BY_DEGREE_CURRICULAR_PLAN_ID_INTERNAL_DESC =
            new Comparator<ExecutionDegree>() {

                @Override
                public int compare(ExecutionDegree o1, ExecutionDegree o2) {
                    return o2.getDegreeCurricularPlan().getExternalId().compareTo(o1.getDegreeCurricularPlan().getExternalId());
                }
            };

    public static List<ExecutionDegree> getAllByExecutionYear(String year) {

        if (year == null) {
            return Collections.emptyList();
        }

        final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
        for (ExecutionDegree executionDegree : Bennu.getInstance().getExecutionDegreesSet()) {
            if (year.equals(executionDegree.getExecutionYear().getYear())) {
                result.add(executionDegree);
            }
        }
        Collections.sort(result, COMPARATOR_BY_DEGREE_CURRICULAR_PLAN_ID_INTERNAL_DESC);

        return result;
    }

    public static List<ExecutionDegree> getAllByExecutionYear(ExecutionYear executionYear) {

        if (executionYear == null) {
            return Collections.emptyList();
        }

        final List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();

        for (final ExecutionDegree executionDegree : Bennu.getInstance().getExecutionDegreesSet()) {
            if (executionDegree.getExecutionYear() == executionYear) {
                result.add(executionDegree);
            }
        }
        Collections.sort(result, COMPARATOR_BY_DEGREE_CURRICULAR_PLAN_ID_INTERNAL_DESC);

        return result;
    }

    public static List<ExecutionDegree> getAllByExecutionCourseAndTeacher(ExecutionCourse executionCourse, Person person) {
        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();

        for (ExecutionDegree executionDegree : Bennu.getInstance().getExecutionDegreesSet()) {
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
            return Collections.emptyList();
        }

        final ExecutionYear executionYear = ExecutionYear.readExecutionYearByName(year);
        return getAllByExecutionYearAndDegreeType(executionYear, typeOfCourse);
    }

    public static List<ExecutionDegree> getAllByExecutionYearAndDegreeType(ExecutionYear executionYear,
            DegreeType... typeOfCourse) {

        if (executionYear == null || typeOfCourse == null) {
            return Collections.emptyList();
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

    public static List<ExecutionDegree> getAllByDegree(final Degree degree) {
        List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();

        for (ExecutionDegree executionDegree : Bennu.getInstance().getExecutionDegreesSet()) {
            if (executionDegree.getDegree() == degree) {
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

        for (ExecutionDegree executionDegree : Bennu.getInstance().getExecutionDegreesSet()) {
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
            Space campus) {
        for (final Degree degree : Degree.readAllByDegreeCode(degreeCode)) {
            final ExecutionDegree executionDegree =
                    degree.getMostRecentDegreeCurricularPlan().getExecutionDegreeByYear(executionYear);
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
        return markSheetType == MarkSheetType.SPECIAL_AUTHORIZATION;
    }

    private boolean checkOccupationPeriod(Date evaluationDate, ExecutionSemester executionSemester, MarkSheetType markSheetType) {
        OccupationPeriod occupationPeriod = getOccupationPeriodFor(executionSemester, markSheetType);
        return evaluationDate != null && occupationPeriod != null
                && occupationPeriod.nestedOccupationPeriodsContainsDay(YearMonthDay.fromDateFields(evaluationDate));
    }

    public OccupationPeriod getOccupationPeriodFor(ExecutionSemester executionSemester, MarkSheetType markSheetType) {
        Collection<OccupationPeriod> periods = null;
        switch (markSheetType) {
        case NORMAL:
        case IMPROVEMENT:
            periods = getPeriods(OccupationPeriodType.EXAMS, executionSemester.getSemester());
            break;

        case SPECIAL_SEASON:
            periods = getPeriods(OccupationPeriodType.EXAMS_SPECIAL_SEASON);
            break;

        default:
        }
        if (periods.isEmpty()) {
            return null;
        } else {
            return periods.iterator().next();
        }
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
        return !getResponsibleCoordinators().isEmpty();
    }

    public boolean isCoordinationTeamFormed() {
        return hasAnyCoordinatorsList();
    }

    public boolean isCoordinationResponsibleChosen() {
        return hasAnyResponsibleCoordinators();
    }

    @Deprecated
    public boolean isDateInFirstSemesterNormalSeasonOfGradeSubmission(YearMonthDay date) {
        return isDateInPeriodOfType(date.toDateTimeAtMidnight(), OccupationPeriodType.GRADE_SUBMISSION, 1);
    }

    @Deprecated
    public boolean isDateInSecondSemesterNormalSeasonOfGradeSubmission(YearMonthDay date) {
        return isDateInPeriodOfType(date.toDateTimeAtMidnight(), OccupationPeriodType.GRADE_SUBMISSION, 2);
    }

    @Deprecated
    public boolean isDateInSpecialSeasonOfGradeSubmission(YearMonthDay date) {
        return isDateInPeriodOfType(date.toDateTimeAtMidnight(), OccupationPeriodType.GRADE_SUBMISSION_SPECIAL_SEASON, null);
    }

    final public String getPresentationName() {
        return getDegreeCurricularPlan().getPresentationName(getExecutionYear());
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

        return Collections.emptyList();
    }

    public Integer getStudentNumberForShiftDistributionBasedOn(Integer studentNumberPosition) {
        final List<Integer> abstractStudentNumbers = new ArrayList<Integer>();
        for (final ShiftDistributionEntry shiftDistributionEntry : getShiftDistributionEntriesSet()) {
            if (!abstractStudentNumbers.contains(shiftDistributionEntry.getAbstractStudentNumber())) {
                abstractStudentNumbers.add(shiftDistributionEntry.getAbstractStudentNumber());
            }
        }
        Collections.sort(abstractStudentNumbers);
        return !abstractStudentNumbers.isEmpty() ? abstractStudentNumbers.get(studentNumberPosition) : null;
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
            // TODO remove this first if when data is corrected
            if (candidacy.getActiveCandidacySituation() != null) {
                if (candidacy.getActiveCandidacySituation().getCandidacySituationType() == candidacySituationType) {
                    result.add(candidacy);
                }
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
        Collection<Department> departments = this.getDegree().getDepartments();

        ArrayList<Teacher> possibleTeachers = new ArrayList<Teacher>();
        for (Department department : departments) {
            possibleTeachers.addAll(department.getPossibleTutors());
        }

        return possibleTeachers;
    }

    @SuppressWarnings({ "serial", "deprecation" })
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
            } else {
                academicCalendarEntry = academicCalendarEntry.getParentEntry();
            }
        }

        return Collections.emptyList();
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
        if (hasScheduling()) {
            return getScheduling().getProposalsSet();
        }
        return Collections.emptySet();
    }

    public OccupationPeriod getPeriodLessons(final ExecutionSemester executionSemester) {
        return getOnePeriod(OccupationPeriodType.LESSONS, executionSemester.getSemester());
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
     * Deprecated methods, created due to the refactoring.
     * 
     * Note that all these methods are not recommended, due to the fact that
     * with the new domain, there can be more than one period per type/semester
     * (due to the different years). This API gives NO guarantee regarding which
     * period will be returned. Different calls to the methods might even return
     * different periods!
     */

    @Deprecated
    public OccupationPeriod getPeriodLessonsFirstSemester() {
        return getOnePeriod(OccupationPeriodType.LESSONS, 1);
    }

    @Deprecated
    public OccupationPeriod getPeriodLessonsSecondSemester() {
        return getOnePeriod(OccupationPeriodType.LESSONS, 2);
    }

    @Deprecated
    public OccupationPeriod getPeriodExamsFirstSemester() {
        return getOnePeriod(OccupationPeriodType.EXAMS, 1);
    }

    @Deprecated
    public OccupationPeriod getPeriodExamsSecondSemester() {
        return getOnePeriod(OccupationPeriodType.EXAMS, 2);
    }

    @Deprecated
    public OccupationPeriod getPeriodExamsSpecialSeason() {
        return getOnePeriod(OccupationPeriodType.EXAMS_SPECIAL_SEASON, null);
    }

    @Deprecated
    public OccupationPeriod getPeriodGradeSubmissionNormalSeasonFirstSemester() {
        return getOnePeriod(OccupationPeriodType.GRADE_SUBMISSION, 1);
    }

    @Deprecated
    public OccupationPeriod getPeriodGradeSubmissionNormalSeasonSecondSemester() {
        return getOnePeriod(OccupationPeriodType.GRADE_SUBMISSION, 2);
    }

    @Deprecated
    public OccupationPeriod getPeriodGradeSubmissionSpecialSeason() {
        return getOnePeriod(OccupationPeriodType.GRADE_SUBMISSION_SPECIAL_SEASON, null);
    }

    @Deprecated
    public void setPeriodLessonsFirstSemester(OccupationPeriod period) {
        setPeriodForType(period, OccupationPeriodType.LESSONS, 1);
    }

    @Deprecated
    public void setPeriodLessonsSecondSemester(OccupationPeriod period) {
        setPeriodForType(period, OccupationPeriodType.LESSONS, 2);
    }

    @Deprecated
    public void setPeriodExamsFirstSemester(OccupationPeriod period) {
        setPeriodForType(period, OccupationPeriodType.EXAMS, 1);
    }

    @Deprecated
    public void setPeriodExamsSecondSemester(OccupationPeriod period) {
        setPeriodForType(period, OccupationPeriodType.EXAMS, 2);
    }

    @Deprecated
    public void setPeriodExamsSpecialSeason(OccupationPeriod period) {
        setPeriodForType(period, OccupationPeriodType.EXAMS_SPECIAL_SEASON, null);
    }

    @Deprecated
    public void setPeriodGradeSubmissionNormalSeasonFirstSemester(OccupationPeriod period) {
        setPeriodForType(period, OccupationPeriodType.GRADE_SUBMISSION, 1);
    }

    @Deprecated
    public void setPeriodGradeSubmissionNormalSeasonSecondSemester(OccupationPeriod period) {
        setPeriodForType(period, OccupationPeriodType.GRADE_SUBMISSION, 2);
    }

    @Deprecated
    public void setPeriodGradeSubmissionSpecialSeason(OccupationPeriod period) {
        setPeriodForType(period, OccupationPeriodType.GRADE_SUBMISSION_SPECIAL_SEASON, null);
    }

    /*
     * Temporary method to update the new relation. With some refactoring, might
     * become the actual method on the new API
     */

    private OccupationPeriod getOnePeriod(OccupationPeriodType type, Integer semester) {
        Collection<OccupationPeriod> periods = getPeriods(type, semester);
        if (periods.isEmpty()) {
            return null;
        } else {
            return periods.iterator().next();
        }
    }

    public void setPeriodForType(OccupationPeriod period, OccupationPeriodType type, Integer semester) {

        Collection<OccupationPeriodReference> periods = this.getPeriodReferences(type, semester, null);

        if (periods.size() == 0) {
            this.addOccupationPeriodReferences(new OccupationPeriodReference(period, this, type, semester, null));
            return;
        }

        for (OccupationPeriodReference reference : periods) {
            reference.setOccupationPeriod(period);
        }

    }

    /*
     * New API
     */

    public Collection<OccupationPeriod> getPeriods(OccupationPeriodType type, Integer semester, List<Integer> years) {
        return Collections2.transform(getPeriodReferences(type, semester, years), OccupationPeriodReference.FUNCTION_TO_PERIOD);
    }

    public Collection<OccupationPeriod> getAllPeriods() {
        return Collections2.transform(getOccupationPeriodReferences(), OccupationPeriodReference.FUNCTION_TO_PERIOD);
    }

    public Collection<OccupationPeriodReference> getPeriodReferences(final OccupationPeriodType type, final Integer semester,
            final List<Integer> years) {
        return Collections2.filter(getOccupationPeriodReferences(), new Predicate<OccupationPeriodReference>() {

            @Override
            public boolean apply(OccupationPeriodReference reference) {

                if (type != null && reference.getPeriodType() != type) {
                    return false;
                }

                if (semester != null && reference.getSemester() != null && reference.getSemester() != semester) {
                    return false;
                }

                if (years != null && !reference.getCurricularYears().getYears().containsAll(years)) {
                    return false;
                }

                return true;
            }

        });
    }

    public Collection<OccupationPeriod> getPeriods(OccupationPeriodType type) {
        return getPeriods(type, null, null);
    }

    public Collection<OccupationPeriod> getPeriods(OccupationPeriodType type, Integer semester) {
        return getPeriods(type, semester, null);
    }

    public Collection<OccupationPeriod> getPeriodsByCurricularYear(Integer year) {
        return getPeriods(null, null, Collections.singletonList(year));
    }

    public Collection<OccupationPeriod> getPeriodsByCurricularYears(List<Integer> years) {
        return getPeriods(null, null, years);
    }

    public boolean isDateInPeriodOfType(final DateTime date, OccupationPeriodType type, Integer semester) {

        return Iterables.any(getPeriods(type, semester), new Predicate<OccupationPeriod>() {

            @Override
            public boolean apply(OccupationPeriod period) {
                return period.getPeriodInterval().contains(date);
            }

        });

    }

    public boolean isPublishedExam(ExecutionSemester executionSemester) {
        return this.getPublishedExamMapsSet().contains(executionSemester);
    }

    public List<TutorshipIntention> getTutorshipIntentions() {
        List<TutorshipIntention> result = new ArrayList<TutorshipIntention>();
        for (TutorshipIntention tutorshipIntention : getDegreeCurricularPlan().getTutorshipIntentionSet()) {
            if (tutorshipIntention.getAcademicInterval().equals(getAcademicInterval())) {
                result.add(tutorshipIntention);
            }
        }
        Collections.sort(result, TutorshipIntention.COMPARATOR_FOR_ATTRIBUTING_TUTOR_STUDENTS);
        return result;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryCourseAnswer> getStudentInquiryCourseAnswers() {
        return getStudentInquiryCourseAnswersSet();
    }

    @Deprecated
    public boolean hasAnyStudentInquiryCourseAnswers() {
        return !getStudentInquiryCourseAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CoordinatorExecutionDegreeCoursesReport> getExecutionDegreeCoursesReports() {
        return getExecutionDegreeCoursesReportsSet();
    }

    @Deprecated
    public boolean hasAnyExecutionDegreeCoursesReports() {
        return !getExecutionDegreeCoursesReportsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContestGroup> getOutboundMobilityCandidacyContestGroup() {
        return getOutboundMobilityCandidacyContestGroupSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacyContestGroup() {
        return !getOutboundMobilityCandidacyContestGroupSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesCourse> getAssociatedInquiriesCoursesByCourse() {
        return getAssociatedInquiriesCoursesByCourseSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedInquiriesCoursesByCourse() {
        return !getAssociatedInquiriesCoursesByCourseSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.finalDegreeWork.FinalDegreeWorkGroup> getAssociatedFinalDegreeWorkGroups() {
        return getAssociatedFinalDegreeWorkGroupsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedFinalDegreeWorkGroups() {
        return !getAssociatedFinalDegreeWorkGroupsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesCourse> getAssociatedInquiriesCoursesByStudent() {
        return getAssociatedInquiriesCoursesByStudentSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedInquiriesCoursesByStudent() {
        return !getAssociatedInquiriesCoursesByStudentSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.degree.ShiftDistributionEntry> getShiftDistributionEntries() {
        return getShiftDistributionEntriesSet();
    }

    @Deprecated
    public boolean hasAnyShiftDistributionEntries() {
        return !getShiftDistributionEntriesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryCourseAnswer> getInquiryCourseAnswers() {
        return getInquiryCourseAnswersSet();
    }

    @Deprecated
    public boolean hasAnyInquiryCourseAnswers() {
        return !getInquiryCourseAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesCourseResult> getStudentInquiriesCourseResults() {
        return getStudentInquiriesCourseResultsSet();
    }

    @Deprecated
    public boolean hasAnyStudentInquiriesCourseResults() {
        return !getStudentInquiriesCourseResultsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Coordinator> getCoordinatorsList() {
        return getCoordinatorsListSet();
    }

    @Deprecated
    public boolean hasAnyCoordinatorsList() {
        return !getCoordinatorsListSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.SchoolClass> getSchoolClasses() {
        return getSchoolClassesSet();
    }

    @Deprecated
    public boolean hasAnySchoolClasses() {
        return !getSchoolClassesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryDelegateAnswer> getInquiryDelegatesAnswers() {
        return getInquiryDelegatesAnswersSet();
    }

    @Deprecated
    public boolean hasAnyInquiryDelegatesAnswers() {
        return !getInquiryDelegatesAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.OccupationPeriodReference> getOccupationPeriodReferences() {
        return getOccupationPeriodReferencesSet();
    }

    @Deprecated
    public boolean hasAnyOccupationPeriodReferences() {
        return !getOccupationPeriodReferencesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CoordinatorLog> getCoordinatorLog() {
        return getCoordinatorLogSet();
    }

    @Deprecated
    public boolean hasAnyCoordinatorLog() {
        return !getCoordinatorLogSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryResult> getInquiryResults() {
        return getInquiryResultsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryResults() {
        return !getInquiryResultsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Guide> getGuides() {
        return getGuidesSet();
    }

    @Deprecated
    public boolean hasAnyGuides() {
        return !getGuidesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.mobility.outbound.OutboundMobilityCandidacyContest> getOutboundMobilityCandidacyContest() {
        return getOutboundMobilityCandidacyContestSet();
    }

    @Deprecated
    public boolean hasAnyOutboundMobilityCandidacyContest() {
        return !getOutboundMobilityCandidacyContestSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryCoordinatorAnswer> getInquiryCoordinationAnswers() {
        return getInquiryCoordinationAnswersSet();
    }

    @Deprecated
    public boolean hasAnyInquiryCoordinationAnswers() {
        return !getInquiryCoordinationAnswersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.inquiries.InquiryGlobalComment> getInquiryGlobalComments() {
        return getInquiryGlobalCommentsSet();
    }

    @Deprecated
    public boolean hasAnyInquiryGlobalComments() {
        return !getInquiryGlobalCommentsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ExecutionSemester> getPublishedExamMaps() {
        return getPublishedExamMapsSet();
    }

    @Deprecated
    public boolean hasAnyPublishedExamMaps() {
        return !getPublishedExamMapsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy> getStudentCandidacies() {
        return getStudentCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyStudentCandidacies() {
        return !getStudentCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.ScientificCommission> getScientificCommissionMembers() {
        return getScientificCommissionMembersSet();
    }

    @Deprecated
    public boolean hasAnyScientificCommissionMembers() {
        return !getScientificCommissionMembersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.MasterDegreeCandidate> getMasterDegreeCandidates() {
        return getMasterDegreeCandidatesSet();
    }

    @Deprecated
    public boolean hasAnyMasterDegreeCandidates() {
        return !getMasterDegreeCandidatesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.StudentInquiriesTeachingResult> getStudentInquiriesTeachingResults() {
        return getStudentInquiriesTeachingResultsSet();
    }

    @Deprecated
    public boolean hasAnyStudentInquiriesTeachingResults() {
        return !getStudentInquiriesTeachingResultsSet().isEmpty();
    }

    @Deprecated
    public boolean hasGratuityValues() {
        return getGratuityValues() != null;
    }

    @Deprecated
    public boolean hasDegreeCurricularPlan() {
        return getDegreeCurricularPlan() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasScheduling() {
        return getScheduling() != null;
    }

    @Deprecated
    public boolean hasExecutionInterval() {
        return getExecutionInterval() != null;
    }

    @Deprecated
    public boolean hasAnotation() {
        return getAnotation() != null;
    }

    @Deprecated
    public boolean hasEndThesisCreationPeriod() {
        return getEndThesisCreationPeriod() != null;
    }

    @Deprecated
    public boolean hasBeginThesisCreationPeriod() {
        return getBeginThesisCreationPeriod() != null;
    }

    @Deprecated
    public boolean hasCampus() {
        return getCampus() != null;
    }

    @Deprecated
    public boolean hasExecutionYear() {
        return getExecutionYear() != null;
    }

    @Deprecated
    public boolean hasTemporaryExamMap() {
        return getTemporaryExamMap() != null;
    }

}
