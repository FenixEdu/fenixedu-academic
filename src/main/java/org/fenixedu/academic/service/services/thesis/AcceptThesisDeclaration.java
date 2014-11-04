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
package net.sourceforge.fenixedu.applicationTier.Servico.thesis;

import net.sourceforge.fenixedu.applicationTier.Filtro.student.thesis.StudentThesisAuthorizationFilter;
import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.NotAuthorizedException;
import net.sourceforge.fenixedu.domain.thesis.Thesis;
import net.sourceforge.fenixedu.domain.thesis.ThesisVisibilityType;

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