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
package net.sourceforge.fenixedu.domain.transactions;

public enum TransactionType {

    GRATUITY_FULL_PAYMENT,

    GRATUITY_FIRST_PHASE_PAYMENT,

    GRATUITY_SECOND_PHASE_PAYMENT,

    GRATUITY_THIRD_PHASE_PAYMENT,

    GRATUITY_FOURTH_PHASE_PAYMENT,

    GRATUITY_FIFTH_PHASE_PAYMENT,

    GRATUITY_ADHOC_PAYMENT,

    USER_SENT_SMS_PAYMENT,

    SYSTEM_SENT_SMS_TO_USER_PAYMENT,

    GRATUITY_REIMBURSEMENT,

    INSURANCE_PAYMENT,

    INSURANCE_REIMBURSEMENT,

    MICROPAYMENTS_CREDIT;

    public String getName() {
        return name();
    }

}
