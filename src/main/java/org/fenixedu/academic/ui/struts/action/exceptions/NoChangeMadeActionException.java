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

/**
 * 
 * @author Nuno Nunes (nmsn@rnl.ist.utl.pt) Joana Mota (jccm@rnl.ist.utl.pt)
 * 
 */
public class NoChangeMadeActionException extends FenixActionException {

    public static String key = "error.exception.noChangeMade";

    public NoChangeMadeActionException(Throwable cause) {
        super(key, cause);
    }

    public NoChangeMadeActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public NoChangeMadeActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public NoChangeMadeActionException(String key) {
        super(key);
    }

    public static String getKey() {
        return key;
    }

    public static void setKey(String key) {
        NoChangeMadeActionException.key = key;
    }

}