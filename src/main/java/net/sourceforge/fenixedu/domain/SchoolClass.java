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
package net.sourceforge.fenixedu.domain;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.predicates.ResourceAllocationRolePredicates;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;

/**
 * 
 * @author Luis Cruz & Sara Ribeiro
 */
public class SchoolClass extends SchoolClass_Base {

    public static final Comparator<SchoolClass> COMPARATOR_BY_NAME = new Comparator<SchoolClass>() {

        @Override
        public int compare(SchoolClass o1, SchoolClass o2) {
            return o1.getNome().compareTo(o2.getNome());
        }

    };

    public SchoolClass(final ExecutionDegree executionDegree, final ExecutionSemester executionSemester, final String name,
            final Integer curricularYear) {
//        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageSchoolClass);
        super();

        checkIfExistsSchoolClassWithSameName(executionDegree, executionSemester, curricularYear, name);

        setRootDomainObject(Bennu.getInstance());
        setExecutionDegree(executionDegree);
        setExecutionPeriod(executionSemester);
        setAnoCurricular(curricularYear);
        setNome(name);
    }

    public SchoolClass(ExecutionDegree executionDegree, AcademicInterval academicInterval, String name, Integer curricularYear) {
//        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageSchoolClass);
        super();

        ExecutionSemester executionInterval = (ExecutionSemester) ExecutionInterval.getExecutionInterval(academicInterval);
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
        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageSchoolClass);
        if (name != null && !StringUtils.isEmpty(name.trim())) {
            final SchoolClass otherClassWithSameNewName =
                    getExecutionDegree().findSchoolClassesByExecutionPeriodAndName(getExecutionPeriod(), name.trim());
            if (otherClassWithSameNewName != null && !otherClassWithSameNewName.equals(this)) {
                throw new DomainException("Duplicate Entry: " + otherClassWithSameNewName.getNome());
            }
        }
        setNome(name);
    }

    public void delete() {
        check(this, ResourceAllocationRolePredicates.checkPermissionsToManageSchoolClass);
        getAssociatedShifts().clear();
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
    public void setExecutionPeriod(ExecutionSemester executionSemester) {
        if (executionSemester == null) {
            throw new DomainException("error.SchoolClass.empty.executionPeriod");
        }
        super.setExecutionPeriod(executionSemester);
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

    private void checkIfExistsSchoolClassWithSameName(ExecutionDegree executionDegree, ExecutionSemester executionSemester,
            Integer curricularYear, String className) {

        if (executionDegree != null && executionSemester != null && curricularYear != null && className != null) {

            final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();
            final Set<SchoolClass> classes =
                    executionDegree.findSchoolClassesByExecutionPeriodAndCurricularYear(executionSemester, curricularYear);
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
        if (!this.getAssociatedShifts().contains(shift)) {
            this.getAssociatedShifts().add(shift);
        }
        if (!shift.getAssociatedClasses().contains(this)) {
            shift.getAssociatedClasses().add(this);
        }
    }

    public Set<Shift> findAvailableShifts() {
        final ExecutionDegree executionDegree = getExecutionDegree();
        final DegreeCurricularPlan degreeCurricularPlan = executionDegree.getDegreeCurricularPlan();

        final Set<Shift> shifts = new HashSet<Shift>();
        for (final CurricularCourse curricularCourse : degreeCurricularPlan.getCurricularCourses()) {
            if (curricularCourse.hasScopeForCurricularYear(getAnoCurricular(), getExecutionPeriod())) {
                for (final ExecutionCourse executionCourse : curricularCourse.getAssociatedExecutionCourses()) {
                    if (executionCourse.getExecutionPeriod() == getExecutionPeriod()) {
                        shifts.addAll(executionCourse.getAssociatedShifts());
                    }
                }
            }
        }
        shifts.removeAll(getAssociatedShifts());
        return shifts;
    }

    public Object getEditablePartOfName() {
        final DegreeCurricularPlan degreeCurricularPlan = getExecutionDegree().getDegreeCurricularPlan();
        final Degree degree = degreeCurricularPlan.getDegree();
        return StringUtils.substringAfter(getNome(), degree.constructSchoolClassPrefix(getAnoCurricular()));
    }

    public AcademicInterval getAcademicInterval() {
        return getExecutionPeriod().getAcademicInterval();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.oldInquiries.InquiriesCourse> getAssociatedInquiriesCourses() {
        return getAssociatedInquiriesCoursesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedInquiriesCourses() {
        return !getAssociatedInquiriesCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Shift> getAssociatedShifts() {
        return getAssociatedShiftsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedShifts() {
        return !getAssociatedShiftsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasExecutionDegree() {
        return getExecutionDegree() != null;
    }

    @Deprecated
    public boolean hasExecutionPeriod() {
        return getExecutionPeriod() != null;
    }

    @Deprecated
    public boolean hasNome() {
        return getNome() != null;
    }

    @Deprecated
    public boolean hasAnoCurricular() {
        return getAnoCurricular() != null;
    }

}
