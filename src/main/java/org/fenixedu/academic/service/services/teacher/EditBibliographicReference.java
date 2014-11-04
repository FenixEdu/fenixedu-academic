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
import org.fenixedu.academic.service.filter.ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.InvalidArgumentsServiceException;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

/**
 * @author Fernanda Quitério
 * 
 */
public class EditBibliographicReference {

    protected Boolean run(String bibliographicReferenceID, String newTitle, String newAuthors, String newReference,
            String newYear, Boolean optional) throws FenixServiceException {

        final BibliographicReference bibliographicReference = FenixFramework.getDomainObject(bibliographicReferenceID);
        if (bibliographicReference == null) {
            throw new InvalidArgumentsServiceException();
        }
        bibliographicReference.edit(newTitle, newAuthors, newReference, newYear, optional);

        return true;
    }

    // Service Invokers migrated from Berserk

    private static final EditBibliographicReference serviceInstance = new EditBibliographicReference();

    @Atomic
    public static Boolean runEditBibliographicReference(String bibliographicReferenceID, String newTitle, String newAuthors,
            String newReference, String newYear, Boolean optional) throws FenixServiceException, NotAuthorizedException {
        ExecutionCourseAndBibliographicReferenceLecturingTeacherAuthorizationFilter.instance.execute(bibliographicReferenceID);
        return serviceInstance.run(bibliographicReferenceID, newTitle, newAuthors, newReference, newYear, optional);
    }

}