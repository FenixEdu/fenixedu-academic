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
 * Created on Sep 26, 2005
 *  by jdnf
 */
package org.fenixedu.academic.service.services.student.enrolment;

import static org.fenixedu.academic.predicate.AccessControl.check;

import org.fenixedu.academic.domain.student.Registration;
import org.fenixedu.academic.predicate.RolePredicates;
import org.fenixedu.academic.service.services.exceptions.FenixServiceException;

import pt.ist.fenixframework.Atomic;

public class ReadStudentByUsername {

    @Atomic
    public static Registration run(String studentUsername) throws FenixServiceException {
        check(RolePredicates.STUDENT_PREDICATE);
        final Registration registration = Registration.readByUsername(studentUsername);
        if (registration == null) {
            throw new FenixServiceException("error.noStudent");
        }
        return registration;
    }

}