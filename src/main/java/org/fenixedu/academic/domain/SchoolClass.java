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
package org.fenixedu.academic.domain;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.domain.time.calendarStructure.AcademicInterval;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * 
 * @author Luis Cruz &amp; Sara Ribeiro
 */
public class SchoolClass extends SchoolClass_Base {

    public static final Comparator<SchoolClass> COMPARATOR_BY_NAME = new Comparator<SchoolClass>() {

        @Override
        public int compare(SchoolClass o1, SchoolClass o2) {
            final int i = o1.getNome().compareTo(o2.getNome());
            return i == 0 ? o1.getExternalId().compareTo(o2.getExternalId()) : i;
        }

    };

    public SchoolClass(final ExecutionDegree executionDegree, final ExecutionInterval executionInterval, final String name,
            final Integer curricularYear) {
//        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageSchoolClass);
        super();

        checkIfExistsSchoolClassWithSameName(executionDegree, executionInterval, curricularYear, name);

        setRootDomainObject(Bennu.getInstance());
        setExecutionDegree(executionDegree);
        setExecutionPeriod(executionInterval);
        setAnoCurricular(curricularYear);
        setNome(name);
    }

    public SchoolClass(ExecutionDegree executionDegree, AcademicInterval academicInterval, String name, Integer curricularYear) {
//        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageSchoolClass);
        super();

        ExecutionInterval executionInterval = ExecutionInterval.getExecutionInterval(academicInterval);
        checkIfExistsSchoolClassWithSameName(executionDegree, executionInterval, curricularYear, name);

        setRootDomainObject(Bennu.getInstance());
        setExecutionDegree(executionDegree);
        // FIXME: cast shouldn't be needed, SchoolClass should relate directly
        // with ExecutionInterval.
        setExecutionPeriod(executionInterval);
        setAnoCurricular(curricularYear);
        setNome(name);
    }

    public void edit(String name) {
        if (name != null && !StringUtils.isEmpty(name.trim())) {
            final SchoolClass otherClassWithSameNewName =
                    getExecutionDegree().findSchoolClassesByExecutionPeriodAndName(getExecutionInterval(), name.trim());
            if (otherClassWithSameNewName != null && !otherClassWithSameNewName.equals(this)) {
                throw new DomainException("Duplicate Entry: " + otherClassWithSameNewName.getNome());
            }
        }
        setNome(name);
    }

    public void delete() {
        getAssociatedShiftsSet().clear();
        super.setExecutionDegree(null);
        super.setExecutionPeriod(null);
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Override
    public void setExecutionDegree(ExecutionDegree executionDegree) {
        if (executionDegree == null) {
            throw new DomainException("error.SchoolClass.empty.executionDegree");
        }
        super.setExecutionDegree(executionDegree);
    }

    @Override
    public void setExecutionPeriod(ExecutionInterval executionInterval) {
        if (executionInterval == null) {
            throw new DomainException("error.SchoolClass.empty.executionPeriod");
        }
        super.setExecutionPeriod(executionInterval);
    }

    @Override
    public void setAnoCurricular(Integer anoCurricular) {
        if (anoCurricular == null || anoCurricular.intValue() < 1) {
            throw new DomainException("error.SchoolClass.invalid.curricularYear");
        }
        super.setAnoCurricular(anoCurricular);
    }

    @Override
    public void setNome(String name) {
        if (name == null || StringUtils.isEmpty(name.trim())) {
            throw new DomainException("error.SchoolClass.empty.name");
        }
        final DegreeCurricularPlan degreeCurricularPlan = getExecutionDegree().getDegreeCurricularPlan();
        final Degree degree = degreeCurricularPlan.getDegree();
        super.setNome(constructName(degree, name.trim(), getAnoCurricular()));
    }

    private void checkIfExistsSchoolClassWithSameName(ExecutionDegree executionDegree, ExecutionInterval executionInterval,
            Integer curricularYear, String className) {

        if (executionDegree != null && executionInterval != null && curricularYear != null && className != null) {

            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final Set<SchoolClass> classes = executionDegree.findSchoolClassesByAcademicIntervalAndCurricularYear(
                    executionInterval.getAcademicInterval(), curricularYear);
            final Degree degree = degreeCurricularPlan.getDegree();
            final String schoolClassName = degree.constructSchoolClassPrefix(curricularYear) + className;

            for (final SchoolClass schoolClass : classes) {
                if (!schoolClass.equals(this) && schoolClassName.equalsIgnoreCase(schoolClass.getNome())) {
                    throw new DomainException("Duplicate Entry: " + className + " for curricular year " + curricularYear
                            + " and degree curricular plan " + degreeCurricularPlan.getName());
                }
            }
        }
    }

    protected String constructName(final Degree degree, final String name, final Integer curricularYear) {
        return degree.constructSchoolClassPrefix(curricularYear) + name;
    }

    public void associateShift(Shift shift) {
        if (shift == null) {
            throw new NullPointerException();
        }
        if (!this.getAssociatedShiftsSet().contains(shift)) {
            this.getAssociatedShiftsSet().add(shift);
        }
        if (!shift.getAssociatedClassesSet().contains(this)) {
            shift.getAssociatedClassesSet().add(this);
        }
    }

    public Set<Shift> findAvailableShifts() {
        final ExecutionDegree executionDegree = getExecutionDegree();
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        final Set<Shift> shifts = new HashSet<Shift>();
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCoursesSet()) {
            if (curricularCourse.isActive(getExecutionInterval(), getAnoCurricular())) {
                for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCoursesSet()) {
                    if (executionCourse.getExecutionInterval() == getExecutionInterval()) {
                        shifts.addAll(executionCourse.getAssociatedShifts());
                    }
                }
            }
        }
        shifts.removeAll(getAssociatedShiftsSet());
        return shifts;
    }

    public Object getEditablePartOfName() {
        final DegreeCurricularPlan degreeCurricularPlan = getExecutionDegree().getDegreeCurricularPlan();
        final Degree degree = degreeCurricularPlan.getDegree();
        return StringUtils.substringAfter(getNome(), degree.constructSchoolClassPrefix(getAnoCurricular()));
    }

    public AcademicInterval getAcademicInterval() {
        return getExecutionInterval().getAcademicInterval();
    }

    public String getName() {
        final Object editablePartOfName = getEditablePartOfName();
        if (editablePartOfName != null && StringUtils.isNotBlank((String) editablePartOfName)) {
            return (String) editablePartOfName;
        }
        return getNome();
    }

    public void setName(final String name) {
        if (StringUtils.isBlank(name)) {
            throw new DomainException("error.SchoolClass.empty.name");
        }
        // super.setNome(name.trim()); 
        // for we will store name as the old way, in order to mantain compatibility to getEditablePartOfName() method across the solution
        setNome(name);
    }

    public Integer getCurricularYear() {
        return getAnoCurricular();
    }

    public void setCurricularYear(final Integer curricularYear) {
        setAnoCurricular(curricularYear);
    }

    public ExecutionInterval getExecutionInterval() {
        return super.getExecutionPeriod();
    }

    public boolean isFreeFor(final Registration registration) {

        final List<ExecutionCourse> attendingCourses = registration.getAttendingExecutionCoursesFor(getExecutionInterval());

        final List<Shift> shiftsForCoursesAndSchoolClass = getAssociatedShiftsSet().stream()
                .filter(s -> attendingCourses.contains(s.getExecutionCourse())).collect(Collectors.toList());

        final Map<ExecutionCourse, List<Shift>> shiftsGroupedByCourses =
                shiftsForCoursesAndSchoolClass.stream().collect(Collectors.groupingBy(Shift::getExecutionCourse));

        for (final Entry<ExecutionCourse, List<Shift>> entry : shiftsGroupedByCourses.entrySet()) {
            final Map<ShiftType, Collection<Shift>> shiftsGroupedByType = new HashMap<>();
            entry.getValue().forEach(
                    s -> s.getTypes().forEach(st -> shiftsGroupedByType.computeIfAbsent(st, x -> new HashSet<>()).add(s)));

            for (final ShiftType shiftType : shiftsGroupedByType.keySet()) {
                final Shift enrolledShift = registration.getShiftFor(entry.getKey(), shiftType);
                if (enrolledShift == null || !shiftsForCoursesAndSchoolClass.contains(enrolledShift)) {
                    if (shiftsGroupedByType.get(shiftType).stream().noneMatch(s -> s.isFreeFor(registration))) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

}
