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
 * Created on 13/Nov/2004
 *
 */
package org.fenixedu.academic.service.services.student;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.Collection;
import java.util.Iterator;

import org.fenixedu.academic.domain.Attends;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.ExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidChangeServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidSituationServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidStudentNumberServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;

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

        Collection studentGroupAttends = studentGroup.getAttendsSet();
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