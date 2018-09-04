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
package org.fenixedu.academic.service.services.residenceManagement;

import java.util.Collection;

import org.fenixedu.academic.domain.accounting.PaymentCode;
import org.fenixedu.academic.domain.accounting.PaymentCodeType;
import org.fenixedu.academic.domain.accounting.ResidenceEvent;
import org.fenixedu.academic.domain.accounting.paymentCodes.AccountingEventPaymentCode;

import org.fenixedu.academic.domain.accounting.paymentCodes.EventPaymentCode;
import org.fenixedu.academic.domain.accounting.paymentCodes.EventPaymentCodeEntry;
import org.fenixedu.academic.domain.accounting.paymentCodes.PaymentCodePool;
import org.joda.time.LocalDate;
import pt.ist.fenixframework.Atomic;

public class CreateResidencePaymentCodes {

    @Atomic
    public static void run(Collection<ResidenceEvent> events) {
        events.forEach(event -> EventPaymentCodeEntry.getOrCreate(event, event.getRoomValue(), new LocalDate()));
    }
}