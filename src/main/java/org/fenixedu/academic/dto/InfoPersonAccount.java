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
package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.PersonAccount;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 * 
 */
public class InfoPersonAccount extends InfoObject {

    private Double balance;

    private InfoPerson infoPerson;

    private List InfoTransactions;

    public InfoPersonAccount() {
    }

    public static InfoPersonAccount copyFromDomain(PersonAccount personAccount) {
        InfoPersonAccount infoPersonAccount = new InfoPersonAccount();

        infoPersonAccount.setExternalId(personAccount.getExternalId());
        infoPersonAccount.setBalance(personAccount.getBalance());

        return infoPersonAccount;
    }

    /**
     * @return Returns the balance.
     */
    public Double getBalance() {
        return balance;
    }

    /**
     * @param balance
     *            The balance to set.
     */
    public void setBalance(Double balance) {
        this.balance = balance;
    }

    /**
     * @return Returns the infoPerson.
     */
    public InfoPerson getInfoPerson() {
        return infoPerson;
    }

    /**
     * @param infoPerson
     *            The infoPerson to set.
     */
    public void setInfoPerson(InfoPerson infoPerson) {
        this.infoPerson = infoPerson;
    }

    /**
     * @return Returns the infoTransactions.
     */
    public List getInfoTransactions() {
        return InfoTransactions;
    }

    /**
     * @param infoTransactions
     *            The infoTransactions to set.
     */
    public void setInfoTransactions(List infoTransactions) {
        InfoTransactions = infoTransactions;
    }
}