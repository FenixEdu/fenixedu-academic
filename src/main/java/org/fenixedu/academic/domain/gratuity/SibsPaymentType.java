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
package net.sourceforge.fenixedu.domain.gratuity;

import net.sourceforge.fenixedu.domain.transactions.TransactionType;

public enum SibsPaymentType {

    SPECIALIZATION_GRATUTITY_TOTAL(TransactionType.GRATUITY_FULL_PAYMENT, 30),

    SPECIALIZATION_GRATUTITY_FIRST_PHASE(TransactionType.GRATUITY_FIRST_PHASE_PAYMENT, 31),

    SPECIALIZATION_GRATUTITY_SECOND_PHASE(TransactionType.GRATUITY_SECOND_PHASE_PAYMENT, 32),

    MASTER_DEGREE_GRATUTITY_TOTAL(TransactionType.GRATUITY_FULL_PAYMENT, 40),

    MASTER_DEGREE_GRATUTITY_FIRST_PHASE(TransactionType.GRATUITY_FIRST_PHASE_PAYMENT, 41),

    MASTER_DEGREE_GRATUTITY_SECOND_PHASE(TransactionType.GRATUITY_SECOND_PHASE_PAYMENT, 42),

    INSURANCE(TransactionType.INSURANCE_PAYMENT, 60),

    MICROPAYMENTS_CREDIT(TransactionType.MICROPAYMENTS_CREDIT, 61);

    private TransactionType transactionType;

    private int code;

    private SibsPaymentType(TransactionType transactionType, int code) {
        this.transactionType = transactionType;
        this.code = code;
    }

    public String getName() {
        return name();
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public int getCode() {
        return code;
    }

    public static SibsPaymentType fromCode(int sibsPaymentCode) {
        for (SibsPaymentType type : values()) {
            if (type.getCode() == sibsPaymentCode) {
                return type;
            }
        }
        return null;
    }
}
