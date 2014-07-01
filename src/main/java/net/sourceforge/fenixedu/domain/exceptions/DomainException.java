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
package net.sourceforge.fenixedu.domain.exceptions;

import javax.ws.rs.core.Response.Status;

import net.sourceforge.fenixedu.util.Bundle;

public class DomainException extends org.fenixedu.bennu.core.domain.exceptions.DomainException {

    private static final String DEFAULT_BUNDLE = Bundle.APPLICATION;

    protected DomainException() {
        this(null, (String[]) null);
    }

    public DomainException(final String key, final String... args) {
        super(DEFAULT_BUNDLE, key, args);
    }

    public DomainException(Status status, String key, String... args) {
        super(status, DEFAULT_BUNDLE, key, args);
    }

    public DomainException(final String key, final Throwable cause, final String... args) {
        super(cause, DEFAULT_BUNDLE, key, args);
    }

}
