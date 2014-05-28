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
 * Created on 17/Ago/2003
 */
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.domain.Grouping;
import net.sourceforge.fenixedu.domain.StudentGroup;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 */
public class EditGrouping {

    protected List run(String executionCourseID, InfoGrouping infoGroupProperties) throws FenixServiceException {
        final Grouping grouping = FenixFramework.getDomainObject(infoGroupProperties.getExternalId());
        if (grouping == null) {
            throw new InvalidArgumentsServiceException();
        }

        grouping.edit(infoGroupProperties.getName(), infoGroupProperties.getEnrolmentBeginDay().getTime(), infoGroupProperties
                .getEnrolmentEndDay().getTime(), infoGroupProperties.getEnrolmentPolicy(), infoGroupProperties
                .getGroupMaximumNumber(), infoGroupProperties.getIdealCapacity(), infoGroupProperties.getMaximumCapacity(),
                infoGroupProperties.getMinimumCapacity(), infoGroupProperties.getProjectDescription(), infoGroupProperties
                        .getShiftType(), infoGroupProperties.getAutomaticEnrolment(), infoGroupProperties
                        .getDifferentiatedCapacity(), infoGroupProperties.getInfoShifts());

        return findEditionErrors(grouping);
    }

    private List findEditionErrors(final Grouping grouping) {

        List<Integer> errors = new ArrayList<Integer>();

        Integer groupMaximumError = testGroupMaximumNumber(grouping);
        if (groupMaximumError != 0) {
            errors.add((groupMaximumError));
        }
        errors.addAll(testMaximumAndMininumCapacity(grouping));

        return errors;
    }

    private List<Integer> testMaximumAndMininumCapacity(final Grouping grouping) {
        Integer[] errors = { 0, 0 };
        List<Integer> result = new ArrayList();

        if (grouping.getMaximumCapacity() == null && grouping.getMinimumCapacity() == null) {
            return result;
        }
        for (final StudentGroup studentGroup : grouping.getStudentGroupsSet()) {
            if (grouping.getMaximumCapacity() != null && studentGroup.getAttendsSet().size() > grouping.getMaximumCapacity()) {
                errors[0] = -2;
            }
            if (grouping.getMinimumCapacity() != null && studentGroup.getAttendsSet().size() < grouping.getMinimumCapacity()) {
                errors[1] = -3;
            }
            if (errors[0] != 0 && errors[1] != 0) {
                break;
            }
        }
        if (errors[0] != 0) {
            result.add(errors[0]);
        }
        if (errors[1] != 0) {
            result.add(errors[1]);
        }
        return result;
    }

    private Integer testGroupMaximumNumber(final Grouping grouping) {
        if (grouping.getGroupMaximumNumber() != null) {
            for (final StudentGroup studentGroup : grouping.getStudentGroupsSet()) {
                Integer groupCapacity;
                if (studentGroup.getShift() != null) {
                    if (grouping.getDifferentiatedCapacity()) {
                        groupCapacity = studentGroup.getShift().getShiftGroupingProperties().getCapacity();
                    } else {
                        groupCapacity = grouping.getGroupMaximumNumber();
                    }
                    if (studentGroup.getShift().getAssociatedStudentGroups(grouping).size() > groupCapacity) {
                        return -1;
                    }
                } else if (!grouping.getDifferentiatedCapacity()
                        && grouping.getStudentGroupsSet().size() > grouping.getGroupMaximumNumber()) {
                    return -1;
                }
            }
        }
        return 0;
    }

    // Service Invokers migrated from Berserk

    private static final EditGrouping serviceInstance = new EditGrouping();

    @Atomic
    public static List runEditGrouping(String executionCourseID, InfoGrouping infoGroupProperties) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        return serviceInstance.run(executionCourseID, infoGroupProperties);
    }

}