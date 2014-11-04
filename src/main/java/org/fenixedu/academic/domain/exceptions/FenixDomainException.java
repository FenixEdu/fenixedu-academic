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
 * Created on Feb 3, 2005
 *
 */
package net.sourceforge.fenixedu.domain.exceptions;

/**
 * @author <a href="mailto:shezad@ist.utl.pt">Shezad Anavarali </a>
 * 
 */
public class FenixDomainException extends Exception {
    private int errorType;

    /**
     * @return
     */
    public int getErrorType() {
        return this.errorType;
    }

    public FenixDomainException() {
    }

    public FenixDomainException(int errorType) {
        this.errorType = errorType;
    }

    public FenixDomainException(String s) {
        super(s);
    }

    public FenixDomainException(Throwable cause) {
        super(cause);
    }

    public FenixDomainException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        String result = "[" + this.getClass().getName() + "\n";
        result += "message " + this.getMessage() + "\n";
        result += "cause " + this.getCause() + "\n";
        result += "]";
        return result;
    }

}
