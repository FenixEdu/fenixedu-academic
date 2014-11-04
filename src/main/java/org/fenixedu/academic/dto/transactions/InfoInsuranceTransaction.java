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

import net.sourceforge.fenixedu.dataTransferObject.InfoExecutionYear;
import net.sourceforge.fenixedu.dataTransferObject.InfoStudent;
import net.sourceforge.fenixedu.domain.transactions.InsuranceTransaction;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InfoInsuranceTransaction extends InfoPaymentTransaction {

    private InfoExecutionYear infoExecutionYear;

    private InfoStudent infoStudent;

    public InfoInsuranceTransaction() {

    }

    protected void copyFromDomain(InsuranceTransaction insuranceTransaction) {

        super.copyFromDomain(insuranceTransaction);

        this.infoExecutionYear = InfoExecutionYear.newInfoFromDomain(insuranceTransaction.getExecutionYear());
        this.infoStudent = InfoStudent.newInfoFromDomain(insuranceTransaction.getStudent());
    }

    public static InfoInsuranceTransaction newInfoFromDomain(InsuranceTransaction insuranceTransaction) {

        if (insuranceTransaction == null) {
            return null;
        }

        InfoInsuranceTransaction infoInsuranceTransaction = new InfoInsuranceTransaction();
        infoInsuranceTransaction.copyFromDomain(insuranceTransaction);

        return infoInsuranceTransaction;
    }

    /**
     * @return Returns the infoExecutionYear.
     */
    public InfoExecutionYear getInfoExecutionYear() {
        return infoExecutionYear;
    }

    /**
     * @param infoExecutionYear
     *            The infoExecutionYear to set.
     */
    public void setInfoExecutionYear(InfoExecutionYear infoExecutionYear) {
        this.infoExecutionYear = infoExecutionYear;
    }

    /**
     * @return Returns the infoStudent.
     */
    public InfoStudent getInfoStudent() {
        return infoStudent;
    }

    /**
     * @param infoStudent
     *            The infoStudent to set.
     */
    public void setInfoStudent(InfoStudent infoStudent) {
        this.infoStudent = infoStudent;
    }
}