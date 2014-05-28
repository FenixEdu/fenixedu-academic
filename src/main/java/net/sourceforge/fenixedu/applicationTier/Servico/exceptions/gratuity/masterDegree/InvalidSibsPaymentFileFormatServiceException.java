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
 * Created on Apr 30, 2004
 *
 */
package net.sourceforge.fenixedu.applicationTier.Servico.exceptions.gratuity.masterDegree;

import net.sourceforge.fenixedu.applicationTier.Servico.exceptions.FenixServiceException;

/**
 * @author - Shezad Anavarali (sana@mega.ist.utl.pt) - Nadir Tarmahomed
 *         (naat@mega.ist.utl.pt)
 */
public class InvalidSibsPaymentFileFormatServiceException extends FenixServiceException {

    /**
     *  
     */
    public InvalidSibsPaymentFileFormatServiceException() {
        super();
    }

    /**
     * @param errorType
     */
    public InvalidSibsPaymentFileFormatServiceException(String message) {
        super(message);
    }

    /**
     * @param cause
     */
    public InvalidSibsPaymentFileFormatServiceException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message
     * @param cause
     */
    public InvalidSibsPaymentFileFormatServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String toString() {
        String result = "[InvalidSibsFileFormatServiceException\n";
        result += "message" + this.getMessage() + "\n";
        result += "cause" + this.getCause() + "\n";
        result += "]";
        return result;
    }
}