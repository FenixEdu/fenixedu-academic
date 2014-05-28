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
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.dataTransferObject.InfoGrouping;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
import net.sourceforge.fenixedu.domain.Grouping;
import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author asnr and scpo
 * 
 */

public class CreateGrouping {

    protected Boolean run(String executionCourseID, InfoGrouping infoGrouping) throws FenixServiceException {

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        Grouping grouping =
                Grouping.create(infoGrouping.getName(), infoGrouping.getEnrolmentBeginDay().getTime(), infoGrouping
                        .getEnrolmentEndDay().getTime(), infoGrouping.getEnrolmentPolicy(), infoGrouping.getGroupMaximumNumber(),
                        infoGrouping.getIdealCapacity(), infoGrouping.getMaximumCapacity(), infoGrouping.getMinimumCapacity(),
                        infoGrouping.getProjectDescription(), infoGrouping.getShiftType(), infoGrouping.getAutomaticEnrolment(),
                        infoGrouping.getDifferentiatedCapacity(), executionCourse);

        if (infoGrouping.getDifferentiatedCapacity()) {
            grouping.createOrEditShiftGroupingProperties(infoGrouping.getInfoShifts());
        }
        return true;
    }

    // Service Invokers migrated from Berserk

    private static final CreateGrouping serviceInstance = new CreateGrouping();

    @Atomic
    public static Boolean runCreateGrouping(String executionCourseID, InfoGrouping infoGrouping) throws FenixServiceException,
            NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(executionCourseID);
        return serviceInstance.run(executionCourseID, infoGrouping);
    }

}