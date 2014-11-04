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
 * Created on 3/Nov/2003
 *
 */
package net.sourceforge.fenixedu.presentationTier.Action.exceptions;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionForward;

/**
 * @author : - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 * 
 */
public class RequiredJuriesActionException extends FenixActionException {
    public static String key = "error.exception.masterDegree.noJuriesSelected";

    public RequiredJuriesActionException(Throwable cause) {
        super(key, cause);
    }

    public RequiredJuriesActionException(Object value, Throwable cause) {
        super(key, value, cause);
    }

    public RequiredJuriesActionException(Object[] values, Throwable cause) {
        super(key, values, cause);
    }

    public RequiredJuriesActionException(String text) {
        super(text);
    }

    public RequiredJuriesActionException(String text, ActionForward actionForward) {
        super(actionForward);
        error = new ActionError(text);
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
        RequiredJuriesActionException.key = key;
    }

    @Override
    public String toString() {
        String result = "[" + getClass().getName() + "n";
        result += "property" + this.getProperty() + "\n";
        result += "error" + this.getError() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}