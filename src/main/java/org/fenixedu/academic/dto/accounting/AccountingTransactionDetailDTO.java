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

import java.io.Serializable;

import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.joda.time.DateTime;

public class AccountingTransactionDetailDTO implements Serializable {

    private DateTime whenRegistered;

    private PaymentMethod paymentMethod;

    private String paymentReference;

    private String comments;

    public AccountingTransactionDetailDTO(DateTime whenRegistered, PaymentMethod paymentMethod, String paymentReference, String comments) {
        this.comments = comments;
        this.paymentMethod = paymentMethod;
        this.paymentReference = paymentReference;
        this.whenRegistered = whenRegistered;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public DateTime getWhenRegistered() {
        return whenRegistered;
    }

    public void setWhenRegistered(DateTime whenRegistered) {
        this.whenRegistered = whenRegistered;
    }

    public boolean isSibsTransactionDetail() {
        return false;
    }

}
