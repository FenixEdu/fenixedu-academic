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

import net.sourceforge.fenixedu.dataTransferObject.InfoGratuitySituation;
import net.sourceforge.fenixedu.domain.transactions.GratuityTransaction;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InfoGratuityTransaction extends InfoPaymentTransaction {

    private InfoGratuitySituation infoGratuitySituation;

    protected void copyFromDomain(GratuityTransaction gratuityTransaction) {
        super.copyFromDomain(gratuityTransaction);
        this.infoGratuitySituation = InfoGratuitySituation.newInfoFromDomain(gratuityTransaction.getGratuitySituation());

    }

    public static InfoGratuityTransaction newInfoFromDomain(GratuityTransaction gratuityTransaction) {

        if (gratuityTransaction == null) {
            return null;
        }

        InfoGratuityTransaction infoGratuityTransaction = new InfoGratuityTransaction();
        infoGratuityTransaction.copyFromDomain(gratuityTransaction);

        return infoGratuityTransaction;
    }

    /**
     * @return Returns the infoGratuitySituation.
     */
    public InfoGratuitySituation getInfoGratuitySituation() {
        return infoGratuitySituation;
    }

    /**
     * @param infoGratuitySituation
     *            The infoGratuitySituation to set.
     */
    public void setInfoGratuitySituation(InfoGratuitySituation infoGratuitySituation) {
        this.infoGratuitySituation = infoGratuitySituation;
    }
}