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

import org.fenixedu.academic.util.FenixUtil;

/**
 * This is not compliant with what a value type should be, but take our word
 * that it is not modified without setting the entity type that contains it. If
 * you feel an urge to modify this, consider refactoring the whole online tests
 * functionality.
 */
public class Response extends FenixUtil {

    private boolean responsed = false;

    private Integer responseProcessingIndex = null;

    public Response() {
        super();
    }

    public void setResponsed() {
        responsed = true;
    }

    public void setResponsed(boolean isResponsed) {
        responsed = isResponsed;
    }

    public boolean getResponsed() {
        return responsed;
    }

    public boolean isResponsed() {
        return responsed;
    }

    public Integer getResponseProcessingIndex() {
        return responseProcessingIndex;
    }

    public void setResponseProcessingIndex(Integer responseProcessingIndex) {
        this.responseProcessingIndex = responseProcessingIndex;
    }

    public boolean hasResponse(String responseOption) {
        return false;
    }
}