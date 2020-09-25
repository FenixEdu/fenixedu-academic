/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
/*
 * ExecutionDegree.java
 *
 * Created on 2 de Novembro de 2002, 20:53
 */

package org.fenixedu.academic.domain;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.academic.domain.candidacy.*;
import org.fenixedu.academic.domain.candidacy.degree.ShiftDistributionEntry;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.degreeStructure.CurricularStage;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarRootEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicYearCE;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.domain.Space;
import org.joda.time.DateTime;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    static final public Comparator<ExecutionDegree> EXECUTION_DEGREE_COMPARATOR_BY_DEGREE_TYPE_AND_DEGREE_NAME =
            new Comparator<ExecutionDegree>() {

                @Override
                public int compare(ExecutionDegree o1, ExecutionDegree o2) {
                    return Degree.COMPARATOR_BY_DEGREE_TYPE_DEGREE_NAME_AND_ID.compare(o1.getDegree(), o2.getDegree());
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
            getPublishedExamMapsSet().addAll(executionYear.getExecutionPeriodsSet());
        }

    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!(getSchoolClassesSet().isEmpty() && getGuidesSet().isEmpty() && getStudentCandidaciesSet().isEmpty() && getShiftDistributionEntriesSet()
                .isEmpty())) {
            blockers.add(BundleUtil.getString(Bundle.APPLICATION, "execution.degree.cannot.be.deleted"));
        }
    }

    public boolean isDeletable() {
        return getDeletionBlockers().isEmpty();
    }

    public void delete() {
        DomainException.throwWhenDeleteBlocked(getDeletionBlockers());
        for (; !getCoordinatorsListSet().isEmpty(); getCoordinatorsListSet().iterator().next().delete()) {
            ;
        }
        for (; !getScientificCommissionMembersSet().isEmpty(); getScientificCommissionMembersSet().iterator().next().delete()) {
            ;
        }

        if (getGratuityValues() != null) {
            getGratuityValues().delete();
        }
        
        getPublishedExamMapsSet().clear();

        setExecutionYear(null);
        setDegreeCurricularPlan(null);
        setCampus(null);

        for (OccupationPeriodReference reference : getOccupationPeriodReferencesSet()) {
            reference.delete();
        }

        getCoordinatorLogSet().stream().forEach(log -> log.setExecutionDegree(null));

        setRootDomainObject(null);
        deleteDomainObject();
    }

    public void edit(ExecutionYear executionYear, Space campus, Boolean publishedExamMap) {

        setExecutionYear(executionYear);
        setCampus(campus);

        for (ExecutionSemester executionSemester : this.getExecutionYear().getExecutionPeriodsSet()) {
            if (publishedExamMap) {
                this.getPublishedExamMapsSet().add(executionSemester);
            } else {
                this.getPublishedExamMapsSet().remove(executionSemester);
            }
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
        final Collection<ExecutionDegree> executionDegrees = this.getDegreeCurricularPlan().getExecutionDegreesSet();
        return this == Collections.min(executionDegrees, EXECUTION_DEGREE_COMPARATORY_BY_YEAR);
    }

    public Set<Shift> findAvailableShifts(final CurricularYear curricularYear, final ExecutionSemester executionSemester) {
        final DegreeCurricularPlan degreeCurricularPlan = getDegreeCurricularPlan();
        final Set<Shift> shifts = new HashSet<Shift>();
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            if (curricularCourse.hasScopeInGivenSemesterAndCurricularYearInDCP(curricularYear, degreeCurricularPlan,
                    executionSemester)) {
                for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
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
        for (final SchoolClass schoolClass : getSchoolClassesSet()) {
            if (schoolClass.getExecutionPeriod() == executionSemester) {
                schoolClasses.add(schoolClass);
            }
        }
        return schoolClasses;
    }

    public Set<SchoolClass> findSchoolClassesByAcademicInterval(final AcademicInterval academicInterval) {
        final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
        for (final SchoolClass schoolClass : getSchoolClassesSet()) {
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
        for (final SchoolClass schoolClass : getSchoolClassesSet()) {
            if (schoolClass.getExecutionPeriod() == executionSemester && schoolClass.getAnoCurricular().equals(curricularYear)) {
                schoolClasses.add(schoolClass);
            }
        }
        return schoolClasses;
    }

    public Set<SchoolClass> findSchoolClassesByAcademicIntervalAndCurricularYear(final AcademicInterval academicInterval,
            final Integer curricularYear) {
        final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
        for (final SchoolClass schoolClass : getSchoolClassesSet()) {
            if (schoolClass.getExecutionPeriod().getAcademicInterval().equals(academicInterval)
                    && schoolClass.getAnoCurricular().equals(curricularYear)) {
                schoolClasses.add(schoolClass);
            }
        }
        return schoolClasses;
    }

    public SchoolClass findSchoolClassesByExecutionPeriodAndName(final ExecutionSemester executionSemester, final String name) {
        for (final SchoolClass schoolClass : getSchoolClassesSet()) {
            if (schoolClass.getExecutionPeriod() == executionSemester && schoolClass.getNome().equalsIgnoreCase(name)) {
                return schoolClass;
            }
        }
        return null;
    }

    public Coordinator getCoordinatorByTeacher(Person person) {
        for (Coordinator coordinator : getCoordinatorsListSet()) {
            if (coordinator.getPerson() == person) {
                return coordinator;
            }
        }

        return null;
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
            for (CurricularCourse curricularCourse : executionDegree.getDegreeCurricularPlan().getCurricularCoursesSet()) {
                if (curricularCourse.getAssociatedExecutionCoursesSet().contains(executionCourse)) {
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

        for (Coordinator coordinator : person.getCoordinatorsSet()) {
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

        for (ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
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

        for (ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
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

        for (ExecutionDegree executionDegree : degreeCurricularPlan.getExecutionDegreesSet()) {
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

        for (ExecutionDegree executionDegree : executionYear.getExecutionDegreesSet()) {
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

    public List<Coordinator> getResponsibleCoordinators() {
        List<Coordinator> result = new ArrayList<Coordinator>();
        for (final Coordinator coordinator : getCoordinatorsListSet()) {
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
        return !getCoordinatorsListSet().isEmpty();
    }

    public boolean isCoordinationResponsibleChosen() {
        return hasAnyResponsibleCoordinators();
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

        for (final StudentCandidacy studentCandidacy : getStudentCandidaciesSet()) {
            if (studentCandidacy instanceof DegreeCandidacy) {
                result.add((DegreeCandidacy) studentCandidacy);
            }
        }

        return result;
    }

    public Set<StudentCandidacy> getFirstCycleCandidacies() {
        final Set<StudentCandidacy> result = new HashSet<StudentCandidacy>();
        for (final StudentCandidacy studentCandidacy : getStudentCandidaciesSet()) {
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
            if (degreeCandidacy.getRegistration() != null) {
                result.add(degreeCandidacy.getRegistration());
            }
        }
        return result;
    }

    public List<Registration> getRegistrationsForFirstCycleCandidacies() {
        final List<Registration> result = new ArrayList<Registration>();
        for (final StudentCandidacy studentCandidacy : getFirstCycleCandidacies()) {
            if (studentCandidacy.getRegistration() != null) {
                result.add(studentCandidacy.getRegistration());
            }
        }
        return result;
    }

    public Set<DFACandidacy> getDFACandidacies() {
        final Set<DFACandidacy> result = new HashSet<DFACandidacy>();

        for (final StudentCandidacy studentCandidacy : getStudentCandidaciesSet()) {
            if (studentCandidacy instanceof DFACandidacy) {
                result.add((DFACandidacy) studentCandidacy);
            }
        }

        return result;
    }

    public List<ShiftDistributionEntry> getNextFreeShiftDistributions(final Integer registrationNumber) {
        final List<ShiftDistributionEntry> entries = getShiftDistributionEntriesSet().stream()
                .filter(entry -> isValidEntryFor(registrationNumber, entry.getAbstractStudentNumber()))
                .collect(Collectors.toList());
        Collections.sort(entries, ShiftDistributionEntry.NUMBER_COMPARATOR);

        final List<ShiftDistributionEntry> result = new ArrayList<>();
        Integer number = null;
        for (final ShiftDistributionEntry shiftDistributionEntry : entries) {
            if (!shiftDistributionEntry.getDistributed()) {
                if (number == null) {
                    number = shiftDistributionEntry.getAbstractStudentNumber();
                }
                if (shiftDistributionEntry.getAbstractStudentNumber().equals(number)) {
                    result.add(shiftDistributionEntry);
                } else if (number != null) {
                    break;
                }
            }
        }
        return result;
    }

    private boolean isValidEntryFor(final Integer registrationNumber, final Integer abstractStudentNumber) {
        return !Shift.RESTRICT_STUDENTS_TO_ODD_OR_EVEN_WEEKS || registrationNumber.intValue() % 2 == abstractStudentNumber.intValue() % 2;
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
        for (ScientificCommission commission : getScientificCommissionMembersSet()) {
            if (commission.getPerson() == person) {
                return true;
            }
        }

        return false;
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
                result.addAll(year.getExecutionDegreesSet());
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

    public OccupationPeriod getPeriodLessons(final ExecutionSemester executionSemester) {
        return getOnePeriod(OccupationPeriodType.LESSONS, executionSemester.getSemester());
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

    /*
     * Temporary method to update the new relation. With some refactoring, might
     * become the actual method on the new API
     */

    private OccupationPeriod getOnePeriod(OccupationPeriodType type, Integer semester) {
        return getPeriods(type, semester).findAny().orElse(null);
    }

    /*
     * New API
     */

    public Stream<OccupationPeriod> getPeriods(OccupationPeriodType type, Integer semester, List<Integer> years) {
        return getPeriodReferences(type, semester, years).map(OccupationPeriodReference::getOccupationPeriod);
    }

    public Stream<OccupationPeriodReference> getPeriodReferences(final OccupationPeriodType type, final Integer semester,
            final List<Integer> years) {
        Stream<OccupationPeriodReference> stream = getOccupationPeriodReferencesSet().stream();
        if (type != null) {
            stream = stream.filter(r -> r.getPeriodType() == type);
        }
        if (semester != null) {
            stream = stream.filter(r -> r.getSemester() == null || r.getSemester() == semester);
        }
        if (years != null) {
            stream = stream.filter(r -> r.getCurricularYears().getYears().containsAll(years));
        }
        return stream;
    }

    public Stream<OccupationPeriod> getPeriods(OccupationPeriodType type) {
        return getPeriods(type, null, null);
    }

    public Stream<OccupationPeriod> getPeriods(OccupationPeriodType type, Integer semester) {
        return getPeriods(type, semester, null);
    }

    public Stream<OccupationPeriod> getPeriodsByCurricularYear(Integer year) {
        return getPeriods(null, null, Collections.singletonList(year));
    }

    public Stream<OccupationPeriod> getPeriodsByCurricularYears(List<Integer> years) {
        return getPeriods(null, null, years);
    }

    public boolean isDateInPeriodOfType(final DateTime date, OccupationPeriodType type, Integer semester) {
        return getPeriods(type, semester).anyMatch(o -> o.getPeriodInterval().contains(date));

    }

    public boolean isPublishedExam(ExecutionSemester executionSemester) {
        return this.getPublishedExamMapsSet().contains(executionSemester);
    }

    public java.util.SortedSet<org.fenixedu.academic.domain.SchoolClass> getSortedSchoolClasses() {
        final SortedSet<SchoolClass> result = new TreeSet<>(SchoolClass.COMPARATOR_BY_NAME);
        result.addAll(getSchoolClassesSet());
        return result;
    }

}
