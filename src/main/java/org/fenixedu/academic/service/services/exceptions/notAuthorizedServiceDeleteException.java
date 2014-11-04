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
 * Created on 6/Mar/2003
 *
 * 
 */
package org.fenixedu.academic.service.services.exceptions;

/**
 * @author João Mota
 */
public class notAuthorizedServiceDeleteException extends FenixServiceException {

    /**
     *  
     */
    public notAuthorizedServiceDeleteException() {
        super();
    }

    /**
     * @param s
     */
    public notAuthorizedServiceDeleteException(String s) {
        super(s);
    }

    /**
     * @param cause
     */
    public notAuthorizedServiceDeleteException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public notAuthorizedServiceDeleteException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        String result = "[notAuthorizedServiceDeleteException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }

}