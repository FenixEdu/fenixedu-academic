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
package org.fenixedu.academic.ui.spring.controller.student;

import java.util.List;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.EnrolmentGroupPolicyType;
import org.springframework.stereotype.Service;

import pt.ist.fenixframework.Atomic;

@Service
public class StudentGroupingService {

    @Atomic
    public void enroll(StudentGroup studentGroup, Person person) {

        if (studentGroup == null) {
            throw new DomainException("error.shift.noShift");
        }

        Grouping grouping = studentGroup.getGrouping();

        if (!groupingIsOpenForEnrollment(grouping)) {
            throw new DomainException("error.grouping.notOpenToEnrollment");
        }

        if (!personInGroupingAttends(grouping, person)) {
            throw new DomainException("error.grouping.notEnroled");
        }

        if (grouping.getStudentGroupsSet().stream().anyMatch(sg -> personInStudentGroupAttends(sg, person))) {
            throw new DomainException("error.studentGroup.alreadyEnroled");
        }

        if (grouping.getEnrolmentPolicy().equals(EnrolmentGroupPolicyType.ATOMIC) && grouping.getMinimumCapacity() != null
                && studentGroup.getAttendsSet().size() + 1 < grouping.getMinimumCapacity()) {
            throw new DomainException("error.invalidNumberOfStudents");
        }

        if (grouping.getMaximumCapacity() != null && studentGroup.getAttendsSet().size() + 1 > grouping.getMaximumCapacity()) {
            throw new DomainException("error.invalidNumberOfStudents");
        }

        studentGroup.addAttends(grouping.getAttendsSet().stream()
                .filter(attends -> attends.getRegistration().getPerson() == person).findAny().get());
    }

    @Atomic
    public void unenroll(StudentGroup studentGroup, Person person) {
        Grouping grouping = studentGroup.getGrouping();

        if (!groupingIsOpenForEnrollment(grouping)) {
            throw new DomainException("error.grouping.notOpenToEnrollment");
        }

        if (!personInGroupingAttends(grouping, person) || !personInStudentGroupAttends(studentGroup, person)) {
            throw new DomainException("error.studentGroup.notEnroled");
        }

        if (grouping.getMinimumCapacity() != null && studentGroup.getAttendsSet().size() - 1 < grouping.getMinimumCapacity()) {
            throw new DomainException("error.invalidNumberOfStudents");
        }

        if (grouping.getMaximumCapacity() != null && studentGroup.getAttendsSet().size() - 1 > grouping.getMaximumCapacity()) {
            throw new DomainException("error.invalidNumberOfStudents");
        }

        studentGroup.removeAttends(grouping.getAttendsSet().stream()
                .filter(attends -> attends.getRegistration().getPerson() == person).findAny().get());

        if (studentGroup.getAttendsSet().isEmpty()) {
            studentGroup.delete();
        }
    }

    @Atomic
    public void createStudentGroup(Grouping grouping, Shift shift, List<Person> personList) {

        if (grouping.getGroupMaximumNumber() <= grouping.getStudentGroupsSet().size()) {
            throw new DomainException("error.grouping.maxGroupsNumber");
        }

        if (!personList.contains(AccessControl.getPerson())) {
            personList.add(AccessControl.getPerson());
        }

        List<Registration> registrationsList =
                grouping.getAttendsSet().stream().map(Attends::getRegistration)
                        .filter(reg -> personList.contains(reg.getPerson())).collect(Collectors.toList());

        if (!groupingIsOpenForEnrollment(grouping)) {
            throw new DomainException("error.grouping.notOpenToEnrollment");
        }

        if (personList.stream().anyMatch(person -> !personInGroupingAttends(grouping, person))) {
            throw new DomainException("error.studentGroup.notEnroled");
        }

        if (personList.stream().anyMatch(
                person -> grouping.getStudentGroupsSet().stream().anyMatch(sg -> personInStudentGroupAttends(sg, person)))) {
            throw new DomainException("error.studentGroup.alreadyEnrolled");
        }

        if (grouping.getShiftType() != null && !shift.getTypes().contains(grouping.getShiftType())) {
            throw new DomainException("error.wrongShiftType");
        }

        if (shift != null && !grouping.getExecutionCourses().contains(shift.getExecutionCourse())) {
            throw new DomainException("error.wrongShiftType");
        }

        if (grouping.getDifferentiatedCapacity()) {
            if (shift != null && shift.getShiftGroupingProperties().getCapacity() != null
                    && shift.getShiftGroupingProperties().getCapacity() < shift.getAssociatedStudentGroups(grouping).size() + 1) {
                throw new DomainException("error.invalidNumberOfStudentGroups");
            }
        } else {
            if (grouping.getGroupMaximumNumber() != null) {
                if (shift != null && grouping.getGroupMaximumNumber() < shift.getAssociatedStudentGroups(grouping).size() + 1) {
                    throw new DomainException("error.invalidNumberOfStudentGroups");
                }
                if (shift == null && grouping.getGroupMaximumNumber() < grouping.getStudentGroupsSet().size() + 1) {
                    throw new DomainException("error.invalidNumberOfStudentGroups");
                }
            }
        }

        if (grouping.getEnrolmentPolicy().equals(EnrolmentGroupPolicyType.ATOMIC) && grouping.getMinimumCapacity() != null
                && personList.size() < grouping.getMinimumCapacity()) {
            throw new DomainException("error.invalidNumberOfStudents");
        }

        if (grouping.getMaximumCapacity() != null && personList.size() > grouping.getMaximumCapacity()) {
            throw new DomainException("error.invalidNumberOfStudents");
        }

        grouping.createStudentGroup(shift, grouping.findMaxGroupNumber() + 1, registrationsList);

    }

    @Atomic
    public void changeShift(StudentGroup studentGroup, Shift shift) {
        Grouping grouping = studentGroup.getGrouping();

        if (!groupingIsOpenForEnrollment(grouping)) {
            throw new DomainException("error.grouping.notOpenToEnrollment");
        }
        if (shift == null && grouping.getShiftType() != null || !shift.getTypes().contains(grouping.getShiftType())) {
            throw new DomainException("error.wrongShiftType");
        }
        if (grouping.getDifferentiatedCapacity()) {
            if (shift.getShiftGroupingProperties().getCapacity() < shift.getAssociatedStudentGroups(grouping).size() + 1) {
                throw new DomainException("error.invalidNumberOfStudentGroups");
            }
        } else {
            if (grouping.getGroupMaximumNumber() < shift.getAssociatedStudentGroups(grouping).size() + 1) {
                throw new DomainException("error.invalidNumberOfStudentGroups");
            }
        }
        studentGroup.setShift(shift);
    }

    public Boolean groupingIsOpenForEnrollment(Grouping grouping) {
        return grouping.getEnrolmentBeginDayDateDateTime().isBeforeNow()
                && grouping.getEnrolmentEndDayDateDateTime().isAfterNow();
    }

    public Boolean personInStudentGroupAttends(StudentGroup studentGroup, Person person) {
        return studentGroup.getAttendsSet().stream().map(Attends::getRegistration).map(Registration::getPerson)
                .anyMatch(p -> p.equals(AccessControl.getPerson()));
    }

    public Boolean personInGroupingAttends(Grouping grouping, Person person) {
        return grouping.getAttendsSet().stream()
                .filter(attends -> grouping.getExecutionCourses().contains(attends.getExecutionCourse()))
                .map(Attends::getRegistration).map(Registration::getPerson).anyMatch(p -> p.equals(AccessControl.getPerson()));
    }
}
