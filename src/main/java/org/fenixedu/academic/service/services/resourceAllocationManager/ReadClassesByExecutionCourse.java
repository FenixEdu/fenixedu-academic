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
import java.util.List;
import java.util.Set;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.domain.SchoolClass;
import org.fenixedu.academic.dto.InfoClass;
import org.fenixedu.academic.service.filter.ResourceAllocationManagerAuthorizationFilter;
import org.fenixedu.academic.service.filter.StudentAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;

/**
 * @author LuisCruz & Sara Ribeiro
 */
public class ReadClassesByExecutionCourse {

    public List<InfoClass> run(ExecutionCourse executionCourse) {

        final Set<SchoolClass> classes = executionCourse.findSchoolClasses();
        final List<InfoClass> infoClasses = new ArrayList<InfoClass>(classes.size());

        for (final SchoolClass schoolClass : classes) {
            final InfoClass infoClass = InfoClass.newInfoFromDomain(schoolClass);
            infoClasses.add(infoClass);
        }

        return infoClasses;
    }

    // Service Invokers migrated from Berserk

    private static final ReadClassesByExecutionCourse serviceInstance = new ReadClassesByExecutionCourse();

    @Atomic
    public static List<InfoClass> runReadClassesByExecutionCourse(ExecutionCourse executionCourse) throws NotAuthorizedException {
        try {
            ResourceAllocationManagerAuthorizationFilter.instance.execute();
            return serviceInstance.run(executionCourse);
        } catch (NotAuthorizedException ex1) {
            try {
                StudentAuthorizationFilter.instance.execute();
                return serviceInstance.run(executionCourse);
            } catch (NotAuthorizedException ex2) {
                throw ex2;
            }
        }
    }

}