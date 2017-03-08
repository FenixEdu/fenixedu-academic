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
 * InterceptingActionException.java
 *
 * March 2nd, 2003, 17h38
 */

package org.fenixedu.academic.ui.struts.action.exceptions;

/**
 * 
 * @author Luis Cruz &amp; Sara Ribeiro
 */

public class InvalidChangeActionException extends FenixActionException {

    public static String key = "error.exception.invalid.guideSituationChange";

    public InvalidChangeActionException(Throwable cause) {
        super(key, cause);
    }

    public InvalidChangeActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public InvalidChangeActionException(String key, Throwable cause) {
        super(key, cause);
    }

    public InvalidChangeActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        InvalidChangeActionException.key = key;
    }

}