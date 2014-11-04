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
package org.fenixedu.academic.util.tests;

public class ResponseLID extends Response {

    private String[] response;

    private Boolean[] isCorrect = null;

    public ResponseLID() {
        super();
    }

    public ResponseLID(String[] op) {
        super();
        setResponse(op);
    }

    public String[] getResponse() {
        return response;
    }

    public void setResponse(String[] op) {
        response = op;
        if (op != null) {
            super.setResponsed();
            if (op.length == 1 && op[0] == null) {
                response = new String[0];
            }
        }
    }

    public Boolean[] getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean[] isCorrect) {
        this.isCorrect = isCorrect;
    }

    @Override
    public boolean hasResponse(String responseOption) {
        if (isResponsed()) {
            for (String element : response) {
                if (element.equalsIgnoreCase(responseOption)) {
                    return true;
                }
            }
        }
        return false;
    }
}