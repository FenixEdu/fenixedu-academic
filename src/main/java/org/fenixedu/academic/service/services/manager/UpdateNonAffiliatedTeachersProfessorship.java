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

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.NonAffiliatedTeacher;
import org.fenixedu.academic.service.filter.ManagerAuthorizationFilter;
import org.fenixedu.academic.service.filter.ScientificCouncilAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.NonExistingServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class UpdateNonAffiliatedTeachersProfessorship {

    protected void run(List<String> nonAffiliatedTeachersIds, String executionCourseId) throws FenixServiceException {

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseId);
        if (executionCourse == null) {
            throw new NonExistingServiceException("message.nonExistingCurricularCourse", null);
        }

        List<NonAffiliatedTeacher> nonAffiliatedTeachersToRemove = new ArrayList<NonAffiliatedTeacher>();
        for (NonAffiliatedTeacher nonAffiliatedTeacher : executionCourse.getNonAffiliatedTeachersSet()) {
            if (!nonAffiliatedTeachersIds.contains(nonAffiliatedTeacher.getExternalId())) {
                nonAffiliatedTeachersToRemove.add(nonAffiliatedTeacher);
            }
        }

        for (NonAffiliatedTeacher nonAffiliatedTeacher : nonAffiliatedTeachersToRemove) {
            executionCourse.removeNonAffiliatedTeachers(nonAffiliatedTeacher);
        }
    }

    // Service Invokers migrated from Berserk

    private static final UpdateNonAffiliatedTeachersProfessorship serviceInstance =
            new UpdateNonAffiliatedTeachersProfessorship();

    @Atomic
    public static void runUpdateNonAffiliatedTeachersProfessorship(List<String> nonAffiliatedTeachersIds, String executionCourseId)
            throws FenixServiceException, NotAuthorizedException {
        try {
            ManagerAuthorizationFilter.instance.execute();
            serviceInstance.run(nonAffiliatedTeachersIds, executionCourseId);
        } catch (NotAuthorizedException ex1) {
            try {
                ScientificCouncilAuthorizationFilter.instance.execute();
                serviceInstance.run(nonAffiliatedTeachersIds, executionCourseId);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}