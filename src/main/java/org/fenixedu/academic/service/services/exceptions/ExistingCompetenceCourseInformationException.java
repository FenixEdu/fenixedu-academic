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
package org.fenixedu.academic.service.services.exceptions;

public class ExistingCompetenceCourseInformationException extends FenixServiceException {

    private final String key;
    private final String[] args;

    public ExistingCompetenceCourseInformationException(final String key, final String... args) {
        super(key);
        this.key = key;
        this.args = args;
    }

    public String getKey() {
        return key;
    }

    @Override
    public String[] getArgs() {
        return args;
    }
}
