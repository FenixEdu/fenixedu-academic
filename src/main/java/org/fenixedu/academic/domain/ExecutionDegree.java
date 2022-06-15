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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.apache.commons.collections.comparators.ComparatorChain;
import org.fenixedu.academic.domain.degree.DegreeType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicCalendarRootEntry;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicYearCE;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.spaces.domain.Space;

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

    }

    @Override
    protected void checkForDeletionBlockers(Collection<String> blockers) {
        super.checkForDeletionBlockers(blockers);
        if (!(getSchoolClassesSet().isEmpty() && getStudentCandidaciesSet().isEmpty())) {
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

        setExecutionYear(null);
        setDegreeCurricularPlan(null);
        setCampus(null);

        for (OccupationPeriodReference reference : getOccupationPeriodReferencesSet()) {
            reference.delete();
        }

        setRootDomainObject(null);
        deleteDomainObject();
    }

//    public void edit(ExecutionYear executionYear, Space campus, Boolean publishedExamMap) {
//        setExecutionYear(executionYear);
//        setCampus(campus);
//    }

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

    public Set<SchoolClass> findSchoolClassesByAcademicInterval(final AcademicInterval academicInterval) {
        final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
        for (final SchoolClass schoolClass : getSchoolClassesSet()) {
            if (schoolClass.getExecutionInterval().getAcademicInterval().equals(academicInterval)) {
                schoolClasses.add(schoolClass);
            }
        }
        return schoolClasses;
    }

    public Set<SchoolClass> findSchoolClassesByAcademicIntervalAndCurricularYear(final AcademicInterval academicInterval,
            final Integer curricularYear) {
        final Set<SchoolClass> schoolClasses = new HashSet<SchoolClass>();
        for (final SchoolClass schoolClass : getSchoolClassesSet()) {
            if (schoolClass.getExecutionInterval().getAcademicInterval().equals(academicInterval)
                    && schoolClass.getAnoCurricular().equals(curricularYear)) {
                schoolClasses.add(schoolClass);
            }
        }
        return schoolClasses;
    }

    public SchoolClass findSchoolClassesByExecutionPeriodAndName(final ExecutionInterval executionInterval, final String name) {
        for (final SchoolClass schoolClass : getSchoolClassesSet()) {
            if (schoolClass.getExecutionInterval() == executionInterval && schoolClass.getNome().equalsIgnoreCase(name)) {
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

    public static List<ExecutionDegree> getAllByExecutionYear(ExecutionYear executionYear) {
        return executionYear == null ? Collections.emptyList() : executionYear.getExecutionDegreesSet().stream()
                .sorted(COMPARATOR_BY_DEGREE_CURRICULAR_PLAN_ID_INTERNAL_DESC).collect(Collectors.toList());
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

//    public static List<ExecutionDegree> filterByAcademicInterval(AcademicInterval academicInterval) {
//        AcademicCalendarEntry academicCalendarEntry = academicInterval.getAcademicCalendarEntry();
//        while (!(academicCalendarEntry instanceof AcademicCalendarRootEntry)) {
//            if (academicCalendarEntry instanceof AcademicYearCE) {
//                ExecutionYear year = ExecutionYear.getExecutionYear((AcademicYearCE) academicCalendarEntry);
//                List<ExecutionDegree> result = new ArrayList<ExecutionDegree>();
//                result.addAll(year.getExecutionDegreesSet());
//                return result;
//            } else {
//                academicCalendarEntry = academicCalendarEntry.getParentEntry();
//            }
//        }
//
//        return Collections.emptyList();
//    }

    public AcademicInterval getAcademicInterval() {
        return getExecutionYear().getAcademicInterval();
    }

    public Collection<OccupationPeriod> getPeriodLessons(final ExecutionInterval interval) {
        return getOccupationPeriodReferencesSet().stream()
                .filter(opr -> OccupationPeriodType.LESSONS.equals(opr.getPeriodType()) && opr.getExecutionInterval() == interval)
                .map(OccupationPeriodReference::getOccupationPeriod).collect(Collectors.toSet());
    }

    public java.util.SortedSet<org.fenixedu.academic.domain.SchoolClass> getSortedSchoolClasses() {
        final SortedSet<SchoolClass> result = new TreeSet<>(SchoolClass.COMPARATOR_BY_NAME);
        result.addAll(getSchoolClassesSet());
        return result;
    }

}
