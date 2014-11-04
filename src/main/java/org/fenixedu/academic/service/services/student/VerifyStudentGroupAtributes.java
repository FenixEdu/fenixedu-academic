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
 * Created on 29/Jul/2003
 *
 */

package org.fenixedu.academic.service.services.student;

import static org.fenixedu.academic.predicate.AccessControl.check;

import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.ExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidChangeServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidSituationServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 * 
 */

public class VerifyStudentGroupAtributes {

    private static boolean checkGroupStudentEnrolment(String studentGroupCode, String username) throws FenixServiceException {
        boolean result = false;

        StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);
        if (studentGroup == null) {
            throw new FenixServiceException();
        }

        Grouping groupProperties = studentGroup.getGrouping();

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

        if (!strategy.checkStudentInGrouping(groupProperties, username)) {
            throw new NotAuthorizedException();
        }

        result = strategy.checkAlreadyEnroled(groupProperties, username);
        if (result) {
            throw new InvalidSituationServiceException();
        }

        result = strategy.checkPossibleToEnrolInExistingGroup(groupProperties, studentGroup);
        if (!result) {
            throw new InvalidArgumentsServiceException();
        }

        return true;
    }

    private static boolean checkGroupEnrolment(String groupPropertiesCode, String shiftCode, String username)
            throws FenixServiceException {
        boolean result = false;
        final Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);
        if (groupProperties == null) {
            throw new InvalidChangeServiceException();
        }

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

        if (!strategy.checkStudentInGrouping(groupProperties, username)) {
            throw new NotAuthorizedException();
        }

        Shift shift = FenixFramework.getDomainObject(shiftCode);
        result = strategy.checkNumberOfGroups(groupProperties, shift);

        if (!result) {
            throw new InvalidArgumentsServiceException();
        }

        result = strategy.checkAlreadyEnroled(groupProperties, username);

        if (result) {
            throw new InvalidSituationServiceException();
        }

        return true;
    }

    private static boolean checkUnEnrollStudentInGroup(String studentGroupCode, String username) throws FenixServiceException {

        boolean result = false;

        StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);
        if (studentGroup == null) {
            throw new FenixServiceException();
        }
        Grouping groupProperties = studentGroup.getGrouping();

        GroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

        if (!strategy.checkStudentInGrouping(groupProperties, username)) {
            throw new NotAuthorizedException();
        }

        result = strategy.checkNotEnroledInGroup(groupProperties, studentGroup, username);
        if (result) {
            throw new InvalidSituationServiceException();
        }

        result = strategy.checkNumberOfGroupElements(groupProperties, studentGroup);
        if (!result) {
            throw new InvalidArgumentsServiceException();
        }

        return true;
    }

    private static boolean checkEditStudentGroupShift(String studentGroupCode, String groupPropertiesCode, String username)
            throws FenixServiceException {
        boolean result = false;

        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);
        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);
        if (studentGroup == null) {
            throw new FenixServiceException();
        }

        GroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

        if (!strategy.checkStudentInGrouping(groupProperties, username)) {
            throw new NotAuthorizedException();
        }

        result = strategy.checkNotEnroledInGroup(groupProperties, studentGroup, username);
        if (result) {
            throw new InvalidSituationServiceException();
        }

        if (groupProperties.getShiftType() == null || studentGroup.getShift() == null) {
            throw new InvalidChangeServiceException();
        }

        return true;
    }

    private static boolean checkEnrollStudentGroupShift(String studentGroupCode, String groupPropertiesCode, String username)
            throws FenixServiceException {
        boolean result = false;
        Grouping groupProperties = FenixFramework.getDomainObject(groupPropertiesCode);
        if (groupProperties == null) {
            throw new ExistingServiceException();
        }

        StudentGroup studentGroup = FenixFramework.getDomainObject(studentGroupCode);
        if (studentGroup == null) {
            throw new FenixServiceException();
        }

        GroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(groupProperties);

        if (!strategy.checkStudentInGrouping(groupProperties, username)) {
            throw new NotAuthorizedException();
        }

        result = strategy.checkNotEnroledInGroup(groupProperties, studentGroup, username);
        if (result) {
            throw new InvalidSituationServiceException();
        }

        if (groupProperties.getShiftType() == null || studentGroup.getShift() != null) {
            throw new InvalidChangeServiceException();
        }

        return true;
    }

    /**
     * Executes the service.
     * 
     * @throws ExcepcaoPersistencia
     */

    @Atomic
    public static Boolean run(String groupPropertiesCode, String shiftCode, String studentGroupCode, String username,
            Integer option) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);

        boolean result = false;

        switch (option.intValue()) {

        case 1:
            result = checkGroupStudentEnrolment(studentGroupCode, username);
            return result;

        case 2:
            result = checkGroupEnrolment(groupPropertiesCode, shiftCode, username);
            return result;

        case 3:
            result = checkUnEnrollStudentInGroup(studentGroupCode, username);
            return result;

        case 4:
            result = checkEditStudentGroupShift(studentGroupCode, groupPropertiesCode, username);
            return result;

        case 5:
            result = checkEnrollStudentGroupShift(studentGroupCode, groupPropertiesCode, username);
            return result;
        }

        return result;
    }

}