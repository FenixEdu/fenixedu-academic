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
package org.fenixedu.academic.service.services.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.ExportGrouping;
import org.fenixedu.academic.domain.Grouping;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.domain.ShiftGroupingProperties;
import org.fenixedu.academic.domain.StudentGroup;
import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.dto.InfoGrouping;
import org.fenixedu.academic.dto.InfoShift;
import org.fenixedu.academic.dto.InfoSiteGroupsByShift;
import org.fenixedu.academic.dto.InfoSiteShift;
import org.fenixedu.academic.dto.InfoSiteShiftsAndGroups;
import org.fenixedu.academic.dto.InfoSiteStudentGroup;
import org.fenixedu.academic.dto.InfoStudentGroup;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.predicate.IllegalDataAccessException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidSituationServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import org.fenixedu.academic.service.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadShiftsAndGroups {

    @Atomic
    public static InfoSiteShiftsAndGroups run(String groupingCode, String username) throws FenixServiceException {

        final Grouping grouping = FenixFramework.getDomainObject(groupingCode);
        if (grouping == null) {
            throw new InvalidSituationServiceException();
        }
        checkPermissions(grouping);
        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);

        if (!strategy.checkStudentInGrouping(grouping, username)) {
            throw new NotAuthorizedException();
        }

        return run(grouping);
    }

    private static void checkPermissions(Grouping grouping) {
        Person person = AccessControl.getPerson();
        if (RoleType.STUDENT.isMember(person.getUser())) {
            return;
        }
        for (ExecutionCourse executionCourse : grouping.getExecutionCourses()) {
            if (person.hasProfessorshipForExecutionCourse(executionCourse)) {
                return;
            }
        }
        throw new IllegalDataAccessException("", person);
    }

    @Atomic
    public static InfoSiteShiftsAndGroups run(Grouping grouping) {
        checkPermissions(grouping);
        final InfoSiteShiftsAndGroups infoSiteShiftsAndGroups = new InfoSiteShiftsAndGroups();

        final List<InfoSiteGroupsByShift> infoSiteGroupsByShiftList = new ArrayList<InfoSiteGroupsByShift>();
        infoSiteShiftsAndGroups.setInfoSiteGroupsByShiftList(infoSiteGroupsByShiftList);
        infoSiteShiftsAndGroups.setInfoGrouping(InfoGrouping.newInfoFromDomain(grouping));

        final IGroupEnrolmentStrategyFactory enrolmentGroupPolicyStrategyFactory = GroupEnrolmentStrategyFactory.getInstance();
        final IGroupEnrolmentStrategy strategy = enrolmentGroupPolicyStrategyFactory.getGroupEnrolmentStrategyInstance(grouping);

        if (strategy.checkHasShift(grouping)) {
            for (final ExportGrouping exportGrouping : grouping.getExportGroupingsSet()) {
                final ExecutionCourse executionCourse = exportGrouping.getExecutionCourse();
                for (final Shift shift : executionCourse.getAssociatedShifts()) {
                    if (shift.containsType(grouping.getShiftType())) {
                        infoSiteGroupsByShiftList.add(createInfoSiteGroupByShift(shift, grouping));
                    }
                }
            }
            Collections.sort(infoSiteGroupsByShiftList, new BeanComparator("infoSiteShift.infoShift.nome"));

            if (!grouping.getStudentGroupsWithoutShift().isEmpty()) {
                infoSiteGroupsByShiftList.add(createInfoSiteGroupByShift(grouping));
            }
        } else {
            infoSiteGroupsByShiftList.add(createInfoSiteGroupByShift(grouping));
        }

        return infoSiteShiftsAndGroups;

    }

    private static InfoSiteGroupsByShift createInfoSiteGroupByShift(final Shift shift, final Grouping grouping) {
        final InfoSiteGroupsByShift infoSiteGroupsByShift = new InfoSiteGroupsByShift();

        final InfoSiteShift infoSiteShift = new InfoSiteShift();
        infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);
        infoSiteShift.setInfoShift(InfoShift.newInfoFromDomain(shift));
        Collections.sort(infoSiteShift.getInfoShift().getInfoLessons());
        final List<StudentGroup> studentGroups = grouping.readAllStudentGroupsBy(shift);
        Integer capacity;
        if (grouping.getDifferentiatedCapacity()) {
            if (shift.getShiftGroupingProperties() == null) {
                new ShiftGroupingProperties(shift, grouping, 0);
            }
            capacity = shift.getShiftGroupingProperties().getCapacity();
        } else {
            capacity = grouping.getGroupMaximumNumber();
        }
        infoSiteShift.setNrOfGroups(calculateVacancies(capacity, studentGroups.size()));

        infoSiteGroupsByShift.setInfoSiteStudentGroupsList(createInfoStudentGroupsList(studentGroups));

        return infoSiteGroupsByShift;
    }

    private static InfoSiteGroupsByShift createInfoSiteGroupByShift(final Grouping grouping) {
        final InfoSiteGroupsByShift infoSiteGroupsByShift = new InfoSiteGroupsByShift();

        final InfoSiteShift infoSiteShift = new InfoSiteShift();
        infoSiteGroupsByShift.setInfoSiteShift(infoSiteShift);
        final List<StudentGroup> studentGroups = grouping.getStudentGroupsWithoutShift();
        infoSiteShift.setNrOfGroups(calculateVacancies(grouping.getGroupMaximumNumber(), studentGroups.size()));

        infoSiteGroupsByShift.setInfoSiteStudentGroupsList(createInfoStudentGroupsList(studentGroups));

        return infoSiteGroupsByShift;
    }

    private static Object calculateVacancies(Integer groupMaximumNumber, int studentGroupsCount) {
        return (groupMaximumNumber != null) ? Integer.valueOf((groupMaximumNumber.intValue() - studentGroupsCount)) : "Sem limite";
    }

    private static List<InfoSiteStudentGroup> createInfoStudentGroupsList(final List<StudentGroup> studentGroups) {
        final List<InfoSiteStudentGroup> infoSiteStudentGroups = new ArrayList<InfoSiteStudentGroup>();
        for (final StudentGroup studentGroup : studentGroups) {
            final InfoSiteStudentGroup infoSiteStudentGroup = new InfoSiteStudentGroup();
            infoSiteStudentGroup.setInfoStudentGroup(InfoStudentGroup.newInfoFromDomain(studentGroup));
            infoSiteStudentGroups.add(infoSiteStudentGroup);
        }
        Collections.sort(infoSiteStudentGroups, new BeanComparator("infoStudentGroup.groupNumber"));
        return infoSiteStudentGroups;
    }

}