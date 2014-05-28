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
package net.sourceforge.fenixedu.util.tests;

public class ResponseNUM extends Response {

    private String response;

    private Boolean isCorrect = null;

    public ResponseNUM() {
        super();
        super.setResponsed();
    }

    public ResponseNUM(String op) {
        super();
        setResponse(op);
        super.setResponsed();
    }

    public String getResponse() {
        return response != null ? response : "";
    }

    public void setResponse(String op) {
        response = op;
        if (op != null) {
            super.setResponsed();
            if (op.length() != 0) {
                response = op.replace(',', '.');
            }
        }
    }

    public Boolean getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(Boolean isCorrect) {
        this.isCorrect = isCorrect;
    }

    @Override
    public boolean hasResponse(String responseOption) {
        if (isResponsed()) {
            if (response.equalsIgnoreCase(responseOption)) {
                return true;
            }
        }
        return false;
    }
}