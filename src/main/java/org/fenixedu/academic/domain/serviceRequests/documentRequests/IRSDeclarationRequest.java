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
package org.fenixedu.academic.domain.serviceRequests.documentRequests;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.dto.serviceRequests.DocumentRequestCreateBean;
import org.joda.time.YearMonthDay;

public class IRSDeclarationRequest extends IRSDeclarationRequest_Base {

    static final private int FIRST_VALID_YEAR = 2006;

    protected IRSDeclarationRequest() {
        super();
    }

    public IRSDeclarationRequest(final DocumentRequestCreateBean bean) {
        this();
        super.init(bean);

        checkParameters(bean);
        super.setYear(bean.getYear());
    }

    @Override
    protected void checkParameters(final DocumentRequestCreateBean bean) {
        if (bean.getYear() == null) {
            throw new DomainException(
                    "error.serviceRequests.documentRequests.SchoolRegistrationDeclarationRequest.year.cannot.be.null");
        }

        if (new YearMonthDay(bean.getYear(), 1, 1).isBefore(new YearMonthDay(FIRST_VALID_YEAR, 1, 1))) {
            throw new DomainException("IRSDeclarationRequest.only.available.after.first.valid.year");
        }
    }

    @Override
    final public DocumentRequestType getDocumentRequestType() {
        return DocumentRequestType.IRS_DECLARATION;
    }

    @Override
    final public String getDocumentTemplateKey() {
        return getClass().getName();
    }

    @Override
    final public void setYear(Integer year) {
        throw new DomainException("error.serviceRequests.documentRequests.IRSDeclarationRequest.cannot.modify.year");
    }

    @Override
    protected boolean hasFreeDeclarationRequests() {
        return false;
    }

}
