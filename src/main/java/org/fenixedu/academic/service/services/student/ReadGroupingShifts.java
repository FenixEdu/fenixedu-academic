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
 * Created on 28/Ago/2003
 *  
 */
package org.fenixedu.academic.service.services.student;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.dto.InfoShift;
import org.fenixedu.academic.dto.InfoSiteShifts;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.ExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidSituationServiceException;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 * 
 */
public class ReadGroupingShifts {

    @Atomic
    public static InfoSiteShifts run(String groupingCode, String studentGroupCode) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);

        InfoSiteShifts infoSiteShifts = new InfoSiteShifts();
        List infoShifts = new ArrayList();
        Grouping grouping = null;
        boolean result = false;

        StudentGroup studentGroup = null;
        grouping = FenixFramework.getDomainObject(groupingCode);
        if (grouping == null) {
            throw new ExistingServiceException();
        }
        if (studentGroupCode != null) {

            studentGroup = FenixFramework.getDomainObject(studentGroupCode);

            if (studentGroup == null) {
                throw new InvalidSituationServiceException();
            }

            infoSiteShifts.setOldShift(InfoShift.newInfoFromDomain(studentGroup.getShift()));
        }

        IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);

        if (strategy.checkHasShift(grouping)) {
            List executionCourses = new ArrayList();
            executionCourses = grouping.getExecutionCourses();

            Iterator iterExecutionCourses = executionCourses.iterator();
            List executionCourseShifts = new ArrayList();
            while (iterExecutionCourses.hasNext()) {
                ExecutionCourse executionCourse = (ExecutionCourse) iterExecutionCourses.next();

                Set<Shift> someShifts = executionCourse.getAssociatedShifts();

                executionCourseShifts.addAll(someShifts);
            }

            List shifts = strategy.checkShiftsType(grouping, executionCourseShifts);
            if (shifts == null || shifts.isEmpty()) {

            } else {

                for (int i = 0; i < shifts.size(); i++) {
                    Shift shift = (Shift) shifts.get(i);
                    result = strategy.checkNumberOfGroups(grouping, shift);

                    if (result) {
                        infoShifts.add(InfoShift.newInfoFromDomain(shift));
                    }
                }
            }
        }

        infoSiteShifts.setShifts(infoShifts);
        return infoSiteShifts;

    }

}