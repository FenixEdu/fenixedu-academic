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
 * Created on 24/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;

/**
 * @author asnr and scpo
 * 
 */

public abstract class GroupEnrolmentStrategy implements IGroupEnrolmentStrategy {

    @Override
    public boolean checkNumberOfGroups(Grouping grouping, Shift shift) {
        Integer maximumGroupCapacity = grouping.getGroupMaximumNumber();

        if (shift != null && grouping.getDifferentiatedCapacity()) {
            maximumGroupCapacity = shift.getShiftGroupingProperties().getCapacity();
        } else if (maximumGroupCapacity == null) {
            return true;
        }

        int numberOfGroups = 0;
        if (shift != null) {
            numberOfGroups = grouping.readAllStudentGroupsBy(shift).size();
        } else {
            numberOfGroups = grouping.getStudentGroupsWithoutShift().size();
        }
        if (maximumGroupCapacity == null || numberOfGroups < maximumGroupCapacity) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkEnrolmentDate(Grouping grouping, Calendar actualDate) {
        Long actualDateInMills = new Long(actualDate.getTimeInMillis());
        Long enrolmentBeginDayInMills = null;
        Long enrolmentEndDayInMills = null;

        if (grouping.getEnrolmentBeginDay() != null) {
            enrolmentBeginDayInMills = new Long(grouping.getEnrolmentBeginDay().getTimeInMillis());
        }

        if (grouping.getEnrolmentEndDay() != null) {
            enrolmentEndDayInMills = new Long(grouping.getEnrolmentEndDay().getTimeInMillis());
        }

        if (enrolmentBeginDayInMills == null && enrolmentEndDayInMills == null) {
            return true;
        }

        if (enrolmentBeginDayInMills != null && enrolmentEndDayInMills == null) {
            if (actualDateInMills.compareTo(enrolmentBeginDayInMills) > 0) {
                return true;
            }
        }

        if (enrolmentBeginDayInMills == null && enrolmentEndDayInMills != null) {
            if (actualDateInMills.compareTo(enrolmentEndDayInMills) < 0) {
                return true;
            }
        }

        if (actualDateInMills.compareTo(enrolmentBeginDayInMills) > 0 && actualDateInMills.compareTo(enrolmentEndDayInMills) < 0) {
            return true;
        }

        return false;
    }

    @Override
    public boolean checkShiftType(Grouping grouping, Shift shift) {
        if (shift != null) {
            return shift.containsType(grouping.getShiftType());
        } else {
            return grouping.getShiftType() == null;
        }
    }

    @Override
    public List checkShiftsType(Grouping grouping, List shifts) {
        List result = new ArrayList();
        if (grouping.getShiftType() != null) {
            for (final Shift shift : (List<Shift>) shifts) {
                if (shift.containsType(grouping.getShiftType())) {
                    result.add(shift);
                }
            }
        }
        return result;
    }

    @Override
    public boolean checkAlreadyEnroled(Grouping grouping, String studentUsername) {

        final Attends studentAttend = grouping.getStudentAttend(studentUsername);

        if (studentAttend != null) {
            Collection<StudentGroup> groupingStudentGroups = grouping.getStudentGroupsSet();
            for (final StudentGroup studentGroup : groupingStudentGroups) {
                Collection<Attends> studentGroupAttends = studentGroup.getAttends();
                for (final Attends attend : studentGroupAttends) {
                    if (attend == studentAttend) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean checkNotEnroledInGroup(Grouping grouping, StudentGroup studentGroup, String studentUsername) {

        final Attends studentAttend = grouping.getStudentAttend(studentUsername);

        if (studentAttend != null) {
            Collection<Attends> studentGroupAttends = studentGroup.getAttends();
            for (final Attends attend : studentGroupAttends) {
                if (attend == studentAttend) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public boolean checkPossibleToEnrolInExistingGroup(Grouping grouping, StudentGroup studentGroup) {

        final int numberOfElements = studentGroup.getAttends().size();
        final Integer maximumCapacity = grouping.getMaximumCapacity();
        if (maximumCapacity == null) {
            return true;
        }
        if (numberOfElements < maximumCapacity) {
            return true;
        }

        return false;
    }

    @Override
    public boolean checkIfStudentGroupIsEmpty(Attends attend, StudentGroup studentGroup) {

        final Collection allStudentGroupAttends = studentGroup.getAttends();
        if (allStudentGroupAttends.size() == 1 && allStudentGroupAttends.contains(attend)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean checkStudentInGrouping(Grouping grouping, String username) {

        final Attends attend = grouping.getStudentAttend(username);
        return attend != null;
    }

    @Override
    public boolean checkStudentsUserNamesInGrouping(List<String> studentUsernames, Grouping grouping) {
        for (final String studentUsername : studentUsernames) {
            if (grouping.getStudentAttend(studentUsername) == null) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean checkHasShift(Grouping grouping) {
        return grouping.getShiftType() != null;
    }

    @Override
    public abstract Integer enrolmentPolicyNewGroup(Grouping grouping, int numberOfStudentsToEnrole, Shift shift);

    @Override
    public abstract boolean checkNumberOfGroupElements(Grouping grouping, StudentGroup studentGroup);

}
