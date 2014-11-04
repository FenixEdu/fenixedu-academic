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
 * Created on 9/Mai/2003
 *
 */
package org.fenixedu.academic.domain;

import java.util.Comparator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.domain.Bennu;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

/**
 * @author asnr and scpo
 */
public class StudentGroup extends StudentGroup_Base {

    public static final Comparator<StudentGroup> COMPARATOR_BY_GROUP_NUMBER = new BeanComparator("groupNumber");
    static {
        getRelationStudentGroupAttend().addListener(new StudentGroupAttendListener());
    }

    private static class StudentGroupAttendListener extends RelationAdapter<StudentGroup, Attends> {
        @Override
        public void beforeRemove(StudentGroup studentGroup, Attends attends) {
            if (!studentGroup.getProjectSubmissionsSet().isEmpty()
                    && !studentGroup.getGrouping().isPersonTeacher(AccessControl.getPerson())) {
                throw new DomainException("error.studentGroup.cannotRemoveAttendsBecauseAlreadyHasProjectSubmissions");
            }

            super.beforeRemove(studentGroup, attends);
        }

    }

    public boolean wasDeleted() {
        return !this.getValid();
    }

    public StudentGroup() {
        super();
        super.setValid(true);
        setRootDomainObject(Bennu.getInstance());
    }

    public StudentGroup(Integer groupNumber, Grouping grouping) {
        this();
        super.setGroupNumber(groupNumber);
        super.setGrouping(grouping);
    }

    public StudentGroup(Integer groupNumber, Grouping grouping, Shift shift) {
        this();
        super.setGroupNumber(groupNumber);
        super.setGrouping(grouping);
        super.setShift(shift);
    }

    public void delete() {
        List<ExecutionCourse> ecs = getGrouping().getExecutionCourses();
        for (ExecutionCourse ec : ecs) {
            GroupsAndShiftsManagementLog.createLog(ec, Bundle.MESSAGING,
                    "log.executionCourse.groupAndShifts.grouping.group.removed", getGroupNumber().toString(), getGrouping()
                            .getName(), ec.getNome(), ec.getDegreePresentationString());

        }
        // teacher type of deletion after project submission
        if (!getProjectSubmissionsSet().isEmpty() && this.getGrouping().isPersonTeacher(AccessControl.getPerson())) {
            this.setValid(false);
        } else if (getProjectSubmissionsSet().isEmpty() && getAttendsSet().isEmpty()) {
            if (getStudentGroupGroup() != null) {
                this.setValid(false);
            } else {
                setShift(null);
                setGrouping(null);
                setRootDomainObject(null);
                deleteDomainObject();
            }
        } else {
            throw new DomainException("student.group.cannot.be.deleted");
        }
    }

    public void editShift(Shift shift) {
        if (this.getGrouping().getShiftType() == null || (!shift.containsType(this.getGrouping().getShiftType()))) {
            throw new DomainException(this.getClass().getName(), "");
        }
        this.setShift(shift);
    }

    public boolean isPersonInStudentGroup(Person person) {

        for (Attends attend : getAttendsSet()) {
            if (attend.getRegistration().getStudent().getPerson().equals(person)) {
                return true;
            }
        }
        return false;
    }

}
