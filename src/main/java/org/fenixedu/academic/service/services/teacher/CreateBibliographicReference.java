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
package org.fenixedu.academic.service.services.teacher;

import org.fenixedu.academic.domain.BibliographicReference;
import org.fenixedu.academic.domain.ExecutionCourse;
import org.fenixedu.academic.service.filter.ExecutionCourseLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério
 */
public class CreateBibliographicReference {

    protected Boolean run(String infoExecutionCourseID, String newBibliographyTitle, String newBibliographyAuthors,
            String newBibliographyReference, String newBibliographyYear, Boolean newBibliographyOptional)
            throws FenixServiceException {

        final ExecutionCourse executionCourse = FenixFramework.getDomainObject(infoExecutionCourseID);
        if (executionCourse == null) {
            throw new InvalidArgumentsServiceException();
        }

        final BibliographicReference reference = BibliographicReference.create(newBibliographyTitle, newBibliographyAuthors,
                newBibliographyReference, newBibliographyYear, newBibliographyOptional);
        reference.setExecutionCourse(executionCourse);
        return true;
    }

    // Service Invokers migrated from Berserk

    private static final CreateBibliographicReference serviceInstance = new CreateBibliographicReference();

    @Atomic
    public static Boolean runCreateBibliographicReference(String infoExecutionCourseID, String newBibliographyTitle,
            String newBibliographyAuthors, String newBibliographyReference, String newBibliographyYear,
            Boolean newBibliographyOptional) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseLecturingTeacherAuthorizationFilter.instance.execute(infoExecutionCourseID);
        return serviceInstance.run(infoExecutionCourseID, newBibliographyTitle, newBibliographyAuthors, newBibliographyReference,
                newBibliographyYear, newBibliographyOptional);
    }

}