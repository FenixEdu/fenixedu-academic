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
package org.fenixedu.academic.service.services.thesis;

import org.fenixedu.academic.domain.thesis.Thesis;
import org.fenixedu.academic.domain.thesis.ThesisVisibilityType;
import org.fenixedu.academic.service.filter.student.thesis.StudentThesisAuthorizationFilter;
import org.fenixedu.academic.service.services.exceptions.NotAuthorizedException;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class AcceptThesisDeclaration {

    protected void run(Thesis thesis, ThesisVisibilityType visibility, DateTime availableAfter) {
        thesis.acceptDeclaration(visibility, availableAfter);
    }

    // Service Invokers migrated from Berserk

    private static final AcceptThesisDeclaration serviceInstance = new AcceptThesisDeclaration();

    @Atomic
    public static void runAcceptThesisDeclaration(Thesis thesis, ThesisVisibilityType visibility, DateTime availableAfter)
            throws NotAuthorizedException {
        StudentThesisAuthorizationFilter.instance.execute(thesis);
        serviceInstance.run(thesis, visibility, availableAfter);
    }

}