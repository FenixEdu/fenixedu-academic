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
package org.fenixedu.academic.service.services.resourceAllocationManager;

import java.util.ArrayList;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.Shift;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.dto.InfoExecutionCourseOccupancy;
import org.fenixedu.academic.dto.InfoShift;
import org.fenixedu.academic.service.filter.ReadShiftsByExecutionCourseIDAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadShiftsByExecutionCourseID {

    protected InfoExecutionCourseOccupancy run(String executionCourseID) {

        final InfoExecutionCourseOccupancy infoExecutionCourseOccupancy = new InfoExecutionCourseOccupancy();
        infoExecutionCourseOccupancy.setInfoShifts(new ArrayList());

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);
        final Set<Shift> shifts = executionCourse.getAssociatedShifts();

        infoExecutionCourseOccupancy.setInfoExecutionCourse(InfoExecutionCourse.newInfoFromDomain(executionCourse));

        for (final Shift shift : shifts) {
            Integer capacity = Integer.valueOf(1);
            if (shift.getLotacao() != null && shift.getLotacao().intValue() != 0) {
                capacity = shift.getLotacao();
            }

            final InfoShift infoShift = InfoShift.newInfoFromDomain(shift);

            infoExecutionCourseOccupancy.getInfoShifts().add(infoShift);
        }

        return infoExecutionCourseOccupancy;
    }

    // Service Invokers migrated from Berserk

    private static final ReadShiftsByExecutionCourseID serviceInstance = new ReadShiftsByExecutionCourseID();

    @Atomic
    public static InfoExecutionCourseOccupancy runReadShiftsByExecutionCourseID(String executionCourseID)
            throws NotAuthorizedException {
        ReadShiftsByExecutionCourseIDAuthorizationFilter.instance.execute(executionCourseID);
        return serviceInstance.run(executionCourseID);
    }

}