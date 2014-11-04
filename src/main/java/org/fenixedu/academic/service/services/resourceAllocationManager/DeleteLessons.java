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
 * 
 * Created on 2003/08/15
 */

package org.fenixedu.academic.service.services.resourceAllocationManager;

import static org.fenixedu.academic.predicate.AccessControl.check;

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.Lesson;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;
import org.fenixedu.academic.service.services.exceptions.FenixServiceMultipleException;

import pt.ist.fenixframework.Atomic;
import pt.ist.fenixframework.FenixFramework;

public class DeleteLessons {

    @Atomic
    public static void run(final List<String> lessonOIDs) throws FenixServiceException {
        check(RolePredicates.RESOURCE_ALLOCATION_MANAGER_PREDICATE);
        final List<DomainException> exceptionList = new ArrayList<DomainException>();

        for (final String lessonOID : lessonOIDs) {
            try {
                Lesson lesson = FenixFramework.getDomainObject(lessonOID);
                if (lesson != null) {
                    lesson.delete();
                }
            } catch (DomainException e) {
                exceptionList.add(e);
            }
        }

        if (!exceptionList.isEmpty()) {
            throw new FenixServiceMultipleException(exceptionList);
        }
    }

}