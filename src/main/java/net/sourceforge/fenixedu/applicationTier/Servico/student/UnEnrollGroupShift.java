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
 * Created on 13/Nov/2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import static net.sourceforge.fenixedu.injectionCode.AccessControl.check;

import java.util.Collection;
import java.util.Iterator;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.ExistingServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidChangeServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidStudentNumberServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.domain.Attends;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.student.Registration;
import net.sourceforge.fenixedu.predicates.RolePredicates;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author joaosa and rmalo
 * 
 */

public class UnEnrollGroupShift {

    @Atomic
    public static Boolean run(String studentGroupCode, String groupPropertiesCode, String username) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);

        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);

        if (studentGroup == null) {
            throw new InvalidArgumentsServiceException();
        }

        if (!(studentGroup.getShift() != null && groupProperties.getShiftType() == null) || studentGroup.getShift() == null) {
            throw new InvalidStudentNumberServiceException();
        }

        Registration registration = Registration.readByUsername(username);

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

        if (!strategy.checkStudentInGrouping(groupProperties, username)) {
            throw new NotAuthorizedException();
        }

        if (!checkStudentInStudentGroup(registration, studentGroup)) {
            throw new InvalidSituationServiceException();
        }

        Shift shift = null;
        boolean result = strategy.checkNumberOfGroups(groupProperties, shift);
        if (!result) {
            throw new InvalidChangeServiceException();
        }
        studentGroup.setShift(shift);

        return true;
    }

    private static boolean checkStudentInStudentGroup(Registration registration, StudentGroup studentGroup) {
        boolean found = false;

        Collection studentGroupAttends = studentGroup.getAttends();
        Attends attend = null;
        Iterator iterStudentGroupAttends = studentGroupAttends.iterator();
        while (iterStudentGroupAttends.hasNext() && !found) {
            attend = ((Attends) iterStudentGroupAttends.next());
            if (attend.getRegistration().equals(registration)) {
                found = true;
            }
        }
        return found;
    }

}