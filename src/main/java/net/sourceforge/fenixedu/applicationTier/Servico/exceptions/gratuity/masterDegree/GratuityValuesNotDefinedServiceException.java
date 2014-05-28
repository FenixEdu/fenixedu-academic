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
 * Created on 22/Mar/2004
 *  
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class GratuityValuesNotDefinedServiceException extends FenixServiceException {

    /**
     *  
     */
    public GratuityValuesNotDefinedServiceException() {
        super();
    }

    /**
     * @param errorType
     */
    public GratuityValuesNotDefinedServiceException(int errorType) {
        super(errorType);
    }

    /**
     * @param s
     */
    public GratuityValuesNotDefinedServiceException(String s) {
        super(s);
    }

    /**
     * @param cause
     */
    public GratuityValuesNotDefinedServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public GratuityValuesNotDefinedServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        String result = "[GratuityValuesNotDefinedServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}