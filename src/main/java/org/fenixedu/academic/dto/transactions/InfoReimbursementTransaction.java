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

import org.fenixedu.academic.domain.transactions.ReimbursementTransaction;
import org.fenixedu.academic.dto.guide.reimbursementGuide.InfoReimbursementGuideEntry;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InfoReimbursementTransaction extends InfoTransaction {

    private InfoReimbursementGuideEntry infoReimbursementGuideEntry;

    public void copyFromDomain(ReimbursementTransaction reimbursementTransaction) {

        super.copyFromDomain(reimbursementTransaction);

        this.infoReimbursementGuideEntry =
                InfoReimbursementGuideEntry.newInfoFromDomain(reimbursementTransaction.getReimbursementGuideEntry());
    }

    public static InfoReimbursementTransaction newInfoFromDomain(ReimbursementTransaction reimbursementTransaction) {

        if (reimbursementTransaction == null) {
            return null;
        }

        InfoReimbursementTransaction infoReimbursementTransaction = new InfoReimbursementTransaction();
        infoReimbursementTransaction.copyFromDomain(reimbursementTransaction);

        return infoReimbursementTransaction;
    }

    /**
     * @return Returns the infoReimbursementGuideEntry.
     */
    public InfoReimbursementGuideEntry getInfoReimbursementGuideEntry() {
        return infoReimbursementGuideEntry;
    }

    /**
     * @param infoReimbursementGuideEntry
     *            The infoReimbursementGuideEntry to set.
     */
    public void setInfoReimbursementGuideEntry(InfoReimbursementGuideEntry infoReimbursementGuideEntry) {
        this.infoReimbursementGuideEntry = infoReimbursementGuideEntry;
    }
}