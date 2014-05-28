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
package net.sourceforge.fenixedu.applicationTier.Servico.student;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidSituationServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.GroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategy;
import net.sourceforge.fenixedu.applicationTier.strategy.groupEnrolment.strategys.IGroupEnrolmentStrategyFactory;
import net.sourceforge.fenixedu.dataTransferObject.ISiteComponent;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.dataTransferObject.InfoShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteGroupsByShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShift;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteShiftsAndGroups;
import net.sourceforge.fenixedu.dataTransferObject.InfoSiteStudentGroup;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudentGroup;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.ExportGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.Shift;
import net.sourceforge.fenixedu.domain.ShiftGroupingProperties;
import net.sourceforge.fenixedu.domain.StudentGroup;
import net.sourceforge.fenixedu.domain.person.RoleType;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.injectionCode.IllegalDataAccessException;

import org.apache.commons.beanutils.BeanComparator;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadShiftsAndGroups {

    @Atomic
    public static ISiteComponent run(String groupingCode, String username) throws FenixServiceException {

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
        if (person.hasRole(RoleType.STUDENT)) {
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
            for (final ExportGrouping exportGrouping : grouping.getExportGroupings()) {
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
            if (!shift.hasShiftGroupingProperties()) {
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