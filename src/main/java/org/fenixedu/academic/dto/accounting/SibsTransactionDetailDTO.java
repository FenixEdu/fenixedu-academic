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
package org.fenixedu.academic.dto.accounting;

import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.academic.util.sibs.incomming.SibsIncommingPaymentFileDetailLine;
import org.joda.time.DateTime;

public class SibsTransactionDetailDTO extends AccountingTransactionDetailDTO {

    private SibsIncommingPaymentFileDetailLine sibsDetailLine;

    private String sibsTransactionId;

    private String sibsCode;

    public SibsTransactionDetailDTO(SibsIncommingPaymentFileDetailLine sibsDetailLine, DateTime whenRegistered, String sibsTransactionId, String sibsCode) {
        this(sibsDetailLine, whenRegistered, sibsTransactionId, sibsCode, null);
    }

    public SibsTransactionDetailDTO(SibsIncommingPaymentFileDetailLine sibsDetailLine, DateTime whenRegistered, String sibsTransactionId, String sibsCode, String comments) {
        super(whenRegistered, PaymentMethod.getSibsPaymentMethod(), sibsCode, comments);
        this.sibsDetailLine = sibsDetailLine;
        this.sibsTransactionId = sibsTransactionId;
        this.sibsCode = sibsCode;
    }

    public String getSibsTransactionId() {
        return sibsTransactionId;
    }

    public void setSibsTransactionId(String sibsTransactionId) {
        this.sibsTransactionId = sibsTransactionId;
    }

    public String getSibsCode() {
        return sibsCode;
    }

    public void setSibsCode(String sibsCode) {
        this.sibsCode = sibsCode;
    }


    @Override
    public boolean isSibsTransactionDetail() {
        return true;
    }

    public SibsIncommingPaymentFileDetailLine getSibsDetailLine() {
        return sibsDetailLine;
    }

    public void setSibsDetailLine(SibsIncommingPaymentFileDetailLine sibsDetailLine) {
        this.sibsDetailLine = sibsDetailLine;
    }

}
