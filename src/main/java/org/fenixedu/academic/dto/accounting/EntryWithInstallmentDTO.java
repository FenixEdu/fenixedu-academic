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

import org.fenixedu.academic.domain.accounting.EntryType;
import org.fenixedu.academic.domain.accounting.Event;
import org.fenixedu.academic.domain.accounting.Installment;
import org.fenixedu.academic.util.LabelFormatter;
import org.fenixedu.academic.util.Money;

public class EntryWithInstallmentDTO extends EntryDTO implements Serializable {

    private Installment installment;

    public EntryWithInstallmentDTO(EntryType entryType, Event event, Money totalAmount, LabelFormatter description,
            Installment installment) {
        super(entryType, event, totalAmount, Money.valueOf(0), totalAmount, description, totalAmount);
        setInstallment(installment);
    }

    public EntryWithInstallmentDTO(EntryType entryType, Event event, Money amountToPay, Installment installment) {
        super(entryType, event, amountToPay);
        setInstallment(installment);
    }

    public Installment getInstallment() {
        return this.installment;
    }

    public void setInstallment(Installment installment) {
        this.installment = installment;
    }

}
