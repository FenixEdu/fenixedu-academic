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
/*
 * Created on 30/Set/2003
 */
package org.fenixedu.academic.service.services.manager;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author jdnf, mrsp and Luis Cruz
 * 
 */
public class DeleteExecutionCourses {

    //ist150958: @Checked removed, however, service is used elsewhere in Manager
    // called from AssociateExecutionCourseToCurricularCourseDA
    //      (Admin -> Gest Estr Ensino -> Estr Cursos Antiga -> ...(curriculares)... -> Escolher disc exec)
    // called from ReadExecutionCourseAction
    //      (not present in any funcionality)
    @Atomic
    public static List<String> run(final List<String> executionCourseIDs) throws FenixServiceException {
        final List<String> undeletedExecutionCoursesCodes = new ArrayList<String>();

        for (final String executionCourseID : executionCourseIDs) {
            final ExecutionCourse executionCourse = FenixFramework.getDomainObject(executionCourseID);

            if (!executionCourse.isDeletable()) {
                undeletedExecutionCoursesCodes.add(executionCourse.getSigla());
            } else {
                executionCourse.delete();
            }
        }
        return undeletedExecutionCoursesCodes;
    }

}