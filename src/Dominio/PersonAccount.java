/*
 * Created on Jul 22, 2004
 *
 */
package Dominio;

import java.util.List;

/**
 * @author <a href="mailto:sana@ist.utl.pt">Shezad Anavarali </a>
 * @author <a href="mailto:naat@ist.utl.pt">Nadir Tarmahomed </a>
 *  
 */
public class PersonAccount extends DomainObject implements IPersonAccount {

    private Integer keyPerson;

    private Double balance;

    private IPerson person;

    private List transactions;

    public PersonAccount() {
    }

    public PersonAccount(IPerson person) {
        this.person = person;
        this.balance = new Double(0);

    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getKeyPerson() {
        return keyPerson;
    }

    public void setKeyPerson(Integer keyPerson) {
        this.keyPerson = keyPerson;
    }

    public IPerson getPerson() {
        return person;
    }

    public void setPerson(IPerson person) {
        this.person = person;
    }

    public List getTransactions() {
        return transactions;
    }

    public void setTransactions(List transactions) {
        this.transactions = transactions;
    }
}