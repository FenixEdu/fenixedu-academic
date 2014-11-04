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
package org.fenixedu.academic.ui.spring.controller.teacher;

import javax.ws.rs.core.Response.Status;

import org.fenixedu.bennu.core.domain.exceptions.DomainException;

public class StudentGroupException extends DomainException {

    private static final long serialVersionUID = -1572696779817798521L;

    public StudentGroupException(Status status, String bundle, String key, String... args) {
        super(status, bundle, key, args);
    }

    public StudentGroupException(String key, String... args) {
        super("resources.FenixSpaceResources", key, args);
    }

    public StudentGroupException(Throwable cause, Status status, String bundle, String key, String... args) {
        super(cause, status, bundle, key, args);
    }

    public StudentGroupException(Throwable cause, String bundle, String key, String... args) {
        super(cause, bundle, key, args);
    }

}
