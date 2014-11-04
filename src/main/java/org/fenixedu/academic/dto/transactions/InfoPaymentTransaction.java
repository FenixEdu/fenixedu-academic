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
package org.fenixedu.academic.dto.transactions;

import org.fenixedu.academic.domain.transactions.GratuityTransaction;
import org.fenixedu.academic.domain.transactions.InsuranceTransaction;
import org.fenixedu.academic.domain.transactions.PaymentTransaction;
import org.fenixedu.academic.dto.InfoGuideEntry;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public abstract class InfoPaymentTransaction extends InfoTransaction {

    private InfoGuideEntry infoGuideEntry;

    public InfoPaymentTransaction() {

    }

    public static InfoPaymentTransaction newInfoFromDomain(PaymentTransaction paymentTransaction) {

        if (paymentTransaction == null) {
            return null;
        }

        InfoPaymentTransaction infoPaymentTransaction = null;

        if (paymentTransaction instanceof GratuityTransaction) {

            infoPaymentTransaction = InfoGratuityTransaction.newInfoFromDomain((GratuityTransaction) paymentTransaction);

        } else if (paymentTransaction instanceof InsuranceTransaction) {

            infoPaymentTransaction = InfoInsuranceTransaction.newInfoFromDomain((InsuranceTransaction) paymentTransaction);

        }

        return infoPaymentTransaction;
    }

    protected void copyFromDomain(PaymentTransaction paymentTransaction) {

        super.copyFromDomain(paymentTransaction);

        InfoGuideEntry infoGuideEntry = null;
        if (paymentTransaction.getGuideEntry() != null) {
            infoGuideEntry = InfoGuideEntry.newInfoFromDomain(paymentTransaction.getGuideEntry());
        }

        this.infoGuideEntry = infoGuideEntry;
    }

    /**
     * @return Returns the infoGuideEntry.
     */
    public InfoGuideEntry getInfoGuideEntry() {
        return infoGuideEntry;
    }

    /**
     * @param infoGuideEntry
     *            The infoGuideEntry to set.
     */
    public void setInfoGuideEntry(InfoGuideEntry infoGuideEntry) {
        this.infoGuideEntry = infoGuideEntry;
    }
}