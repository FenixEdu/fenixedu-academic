package net.sourceforge.fenixedu.dataTransferObject;

import java.util.List;

import net.sourceforge.fenixedu.domain.IPersonAccount;

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

    public static InfoPersonAccount copyFromDomain(IPersonAccount personAccount) {
        InfoPersonAccount infoPersonAccount = new InfoPersonAccount();

        infoPersonAccount.setIdInternal(personAccount.getIdInternal());
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