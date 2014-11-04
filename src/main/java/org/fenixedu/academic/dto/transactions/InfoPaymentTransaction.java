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
package net.sourceforge.fenixedu.dataTransferObject.transactions;

import net.sourceforge.fenixedu.dataTransferObject.InfoGuideEntry;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;
import net.sourceforge.fenixedu.domain.transactions.PaymentTransaction;

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