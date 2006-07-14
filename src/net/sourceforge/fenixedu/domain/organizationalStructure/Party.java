/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

public abstract class Party extends Party_Base {

    public Party() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(getClass().getName());
        createAccount(AccountType.INTERNAL);
        createAccount(AccountType.EXTERNAL);
    }

    public Account createAccount(AccountType accountType) {
        checkAccountsFor(accountType);
        return new Account(accountType, this);
    }

    private void checkAccountsFor(AccountType accountType) {
        if (getAccountBy(accountType) != null) {
            throw new DomainException("error.party.accounts.accountType.already.exist");
        }
    }

    public Account getAccountBy(AccountType accountType) {
        for (final Account account : getAccountsSet()) {
            if (account.getAccountType() == accountType) {
                return account;
            }
        }
        return null;
    }

    protected void delete() { 
        if(!canBeDeleted()) {
            throw new DomainException("error.party.cannot.be.deleted");
        }
        
        for (; !getAccounts().isEmpty(); getAccounts().get(0).delete());
        removeRootDomainObject();
        deleteDomainObject();         
    }         
    
    private boolean canBeDeleted() {
        return !hasAnyResearchInterests() && !hasAnyProjectParticipations() 
            && !hasAnyEventParticipations() && !hasAnyBoards() && !hasAnyChilds();  
    }
}
