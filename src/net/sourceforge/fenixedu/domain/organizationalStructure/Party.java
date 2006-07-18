/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.util.HashSet;
import java.util.Set;

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

    public Set<Party> getParentParties(AccountabilityTypeEnum accountabilityTypeEnum, Class parentPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getParentsSet()) {
            if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
                    && accountability.getParentParty().getClass().equals(parentPartyClass)) {
                result.add(accountability.getParentParty());
            }
        }
        return result;
    }

    public Set<Party> getParentParties(Class parentPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getParentsSet()) {
            if (accountability.getParentParty().getClass().equals(parentPartyClass)) {
                result.add(accountability.getParentParty());
            }
        }
        return result;
    }

    public Set<Party> getChildParties(Class childPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getChildsSet()) {
            if (accountability.getChildParty().getClass().equals(childPartyClass)) {
                result.add(accountability.getChildParty());
            }
        }
        return result;
    }

    public Set<Party> getChildParties(AccountabilityTypeEnum accountabilityTypeEnum, Class childPartyClass) {
        final Set<Party> result = new HashSet<Party>();
        for (final Accountability accountability : getChildsSet()) {
            if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
                    && accountability.getChildParty().getClass().equals(childPartyClass)) {
                result.add(accountability.getChildParty());
            }
        }
        return result;
    }

    public Set<Accountability> getParentAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum) {
        final Set<Accountability> result = new HashSet<Accountability>();
        for (final Accountability accountability : getParentsSet()) {
            if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
                result.add(accountability);
            }
        }
        return result;
    }

    public Set<Accountability> getChildAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum) {
        final Set<Accountability> result = new HashSet<Accountability>();
        for (final Accountability accountability : getChildsSet()) {
            if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
                result.add(accountability);
            }
        }
        return result;
    }

    public Set<Accountability> getParentAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum,
            Class accountabilityClass) {
        final Set<Accountability> result = new HashSet<Accountability>();
        for (final Accountability accountability : getParentsSet()) {
            if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
                    && accountability.getClass().equals(accountabilityClass)) {
                result.add(accountability);
            }
        }
        return result;
    }

    public Set<Accountability> getChildAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum,
            Class accountabilityClass) {
        final Set<Accountability> result = new HashSet<Accountability>();
        for (final Accountability accountability : getChildsSet()) {
            if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
                    && accountability.getClass().equals(accountabilityClass)) {
                result.add(accountability);
            }
        }
        return result;
    }

    protected void delete() {
        if (!canBeDeleted()) {
            throw new DomainException("error.party.cannot.be.deleted");
        }

        for (; !getAccounts().isEmpty(); getAccounts().get(0).delete())
            ;
        removeRootDomainObject();
        deleteDomainObject();
    }

    private boolean canBeDeleted() {
        return !hasAnyResearchInterests() && !hasAnyProjectParticipations()
                && !hasAnyEventParticipations() && !hasAnyBoards() && !hasAnyChilds();
    }
}
