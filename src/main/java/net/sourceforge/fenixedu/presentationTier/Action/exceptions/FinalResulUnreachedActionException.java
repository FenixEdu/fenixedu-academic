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
package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

public class FinalResulUnreachedActionException extends FenixActionException {

    public static String key = "error.exception.unreached";

    public FinalResulUnreachedActionException(String value) {
        super(key, value);
    }

    public FinalResulUnreachedActionException(Throwable cause) {

        super(key, cause);
    }

    public FinalResulUnreachedActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public FinalResulUnreachedActionException(String key, Object value, Throwable cause) {
        super(key, value, cause);
    }

    public FinalResulUnreachedActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    /**
     * @return String
     */
    public static String getKey() {
        return key;
    }

    /**
     * Sets the key.
     * 
     * @param key
     *            The key to set
     */
    public static void setKey(String key) {
        FinalResulUnreachedActionException.key = key;
    }

    @Override
    public String toString() {
        String result = "[error.exception.unreached\n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
    // TODO find a way of internationalizing the message passed as argument to
    // the exception error message of the resource bundle
}