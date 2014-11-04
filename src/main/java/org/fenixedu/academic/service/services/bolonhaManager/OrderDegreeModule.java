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
package org.fenixedu.academic.service.services.bolonhaManager;

import org.fenixedu.academic.domain.degreeStructure.Context;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class OrderDegreeModule {

    @Atomic
    public static void run(final String contextID, final Integer position) throws FenixServiceException {
        if (contextID == null) {
            throw new FenixServiceException();
        }

        final Context context = FenixFramework.getDomainObject(contextID);
        if (context == null) {
            throw new FenixServiceException("error.noContext");
        }

        context.getParentCourseGroup().orderChild(context, position);
    }

}
