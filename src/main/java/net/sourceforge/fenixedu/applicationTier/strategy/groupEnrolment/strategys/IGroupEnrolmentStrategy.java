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
 * Created on 28/Jul/2003
 *
 */
package net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys;

import java.util.Calendar;
import java.util.List;

import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;

/**
 * @author scpo and asnr
 * 
 */
public interface IGroupEnrolmentStrategy {

    public boolean checkNumberOfGroups(Grouping grouping, Shift shift);

    public boolean checkEnrolmentDate(Grouping grouping, Calendar actualDate);

    public boolean checkShiftType(Grouping grouping, Shift shift);

    public boolean checkNumberOfGroupElements(Grouping grouping, StudentGroup studentGroup);

    public boolean checkIfStudentGroupIsEmpty(Attends attend, StudentGroup studentGroup);

    public List checkShiftsType(Grouping groupProperties, List shifts);

    public boolean checkPossibleToEnrolInExistingGroup(Grouping grouping, StudentGroup studentGroup);

    public Integer enrolmentPolicyNewGroup(Grouping grouping, int numberOfStudentsToEnrole, Shift shift);

    public boolean checkAlreadyEnroled(Grouping grouping, String username);

    public boolean checkNotEnroledInGroup(Grouping grouping, StudentGroup studentGroup, String username);

    public boolean checkStudentInGrouping(Grouping grouping, String username);

    public boolean checkHasShift(Grouping grouping);

    public boolean checkStudentsUserNamesInGrouping(List<String> studentUsernames, Grouping grouping);
}
