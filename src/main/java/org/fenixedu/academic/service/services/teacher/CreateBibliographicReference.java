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
package net.sourceforge.fenixedu.applicationTier.Servico.teacher;

import net.sourceforge.fenixedu.applicationTier.Filtro.ExecutionCourseLecturingTeacherAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.InvalidArgumentsServiceException;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.ExecutionCourse;
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

        executionCourse.createBibliographicReference(newBibliographyTitle, newBibliographyAuthors, newBibliographyReference,
                newBibliographyYear, newBibliographyOptional);
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