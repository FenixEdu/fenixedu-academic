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
package org.fenixedu.academic.domain.exceptions.misc;

import org.fenixedu.academic.domain.exceptions.FenixDomainException;

public class InvalidIntervalLimitsException extends FenixDomainException {

    public InvalidIntervalLimitsException() {
        super();
    }

    /**
     * @param errorType
     */
    public InvalidIntervalLimitsException(int errorType) {
        super(errorType);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param s
     */
    public InvalidIntervalLimitsException(String s) {
        super(s);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param cause
     */
    public InvalidIntervalLimitsException(Throwable cause) {
        super(cause);
        // TODO Auto-generated constructor stub
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidIntervalLimitsException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }

}
