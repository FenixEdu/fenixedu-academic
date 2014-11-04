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
/*
 * Created on 13/Mar/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

/**
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt)
 * @author Joana Mota (jccm@rnl.ist.utl.pt)
 */
public class InvalidPasswordActionException extends FenixActionException {

    // public static String key = "error.exception.invalid.existing.password";

    public InvalidPasswordActionException(Throwable cause) {
        super(cause.getMessage(), cause);
    }

    public InvalidPasswordActionException(Object value, Throwable cause) {
        super(cause.getMessage(), value, cause);
    }

    public InvalidPasswordActionException(Object[] values, Throwable cause) {
        super(cause.getMessage(), values, cause);
    }

    /*
     * public static String getKey() { return get; }
     */

    /*
     * public static void setKey(String key) {
     * InvalidPasswordActionException.key = key; }
     */

    @Override
    public String toString() {
        String result = "[InvalidPasswordActionException\n";
        result += "property: " + this.getProperty() + "\n";
        result += "error: " + this.getError() + "\n";
        result += "cause: " + this.getCause() + "\n";
        result += "]";
        return result;
    }

}