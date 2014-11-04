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
package org.fenixedu.academic.service.services.manager;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.dto.InfoExecutionCourse;
import org.fenixedu.academic.service.filter.ManagerAuthorizationFilter;
import org.fenixedu.academic.service.filter.ScientificCouncilAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class ReadExecutionCourseByID {

    protected InfoExecutionCourse run(String externalId) throws FenixServiceException {
        ExecutionCourse executionCourse = FenixFramework.getDomainObject(externalId);
        if (executionCourse == null) {
            throw new NonExistingServiceException();
        }

        return InfoExecutionCourse.newInfoFromDomain(executionCourse);
    }

    // Service Invokers migrated from Berserk

    private static final ReadExecutionCourseByID serviceInstance = new ReadExecutionCourseByID();

    @Atomic
    public static InfoExecutionCourse runReadExecutionCourseManagerByID(String externalId) throws FenixServiceException,
            NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(externalId);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                return serviceInstance.run(externalId);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}