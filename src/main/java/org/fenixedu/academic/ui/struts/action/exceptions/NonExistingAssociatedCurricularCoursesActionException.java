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
 */
public class NonExistingAssociatedCurricularCoursesActionException extends FenixActionException {

    public static String key = "error.nonExisting.AssociatedCurricularCourses";

    public NonExistingAssociatedCurricularCoursesActionException() {
        super(key);
    }

    public NonExistingAssociatedCurricularCoursesActionException(Throwable cause) {
        super(key, cause);
    }

    public NonExistingAssociatedCurricularCoursesActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public NonExistingAssociatedCurricularCoursesActionException(Object[] values, Throwable cause) {
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
        NonExistingAssociatedCurricularCoursesActionException.key = key;
    }

    @Override
    public String toString() {
        String result = "[NonExistingAssociatedCurricularCoursesActionException\n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}