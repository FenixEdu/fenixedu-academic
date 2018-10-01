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
package org.fenixedu.academic.service.services.accounting;

import static org.fenixedu.academic.predicate.AccessControl.check;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accounting.CreditNote;
import org.fenixedu.academic.domain.accounting.PaymentMethod;
import org.fenixedu.academic.dto.accounting.CreateCreditNoteBean;
import org.fenixedu.academic.predicate.AcademicPredicates;

import pt.ist.fenixframework.Atomic;

public class CreateCreditNote {

    @Atomic
    public static CreditNote run(final Person responsible, final CreateCreditNoteBean createCreditNoteBean) {
        check(AcademicPredicates.MANAGE_STUDENT_PAYMENTS);
        return createCreditNoteBean.getReceipt().createCreditNote(responsible, PaymentMethod.getCashPaymentMethod(),
                createCreditNoteBean.getSelectedEntries());
    }

}