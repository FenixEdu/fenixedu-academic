/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;

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

    public static Set<Party> readContributors() {
        ComparatorChain chain = new ComparatorChain();
        Comparator caseInsensitiveName = new Comparator<Party>() {
            public int compare(Party party, Party otherParty) {
                Collator collator = Collator.getInstance();
                return collator.compare(party.getName().toLowerCase(), otherParty.getName().toLowerCase());
            }
        };
        chain.addComparator(caseInsensitiveName);
        chain.addComparator(new BeanComparator("socialSecurityNumber"));
        
        Set<Party> result = new TreeSet<Party>(chain);
        for (final Party party : RootDomainObject.getInstance().getPartys()) {
            if (party.getSocialSecurityNumber() != null && !party.getSocialSecurityNumber().equals("")) {
                result.add(party);
            }
        }
        
        return result;
    }

    public static Party readByContributorNumber(String contributorNumber) {
        for (final Party party : RootDomainObject.getInstance().getPartys()) {
            if (party.getSocialSecurityNumber() != null && party.getSocialSecurityNumber().equalsIgnoreCase(contributorNumber)) {
                return party;
            }
        }
        
        return null;
    }

    public void editContributor(String contributorName, String contributorNumber,
            String contributorAddress, String areaCode, String areaOfAreaCode, String area,
            String parishOfResidence, String districtSubdivisionOfResidence, String districtOfResidence) {
        
        final Party existing = Party.readByContributorNumber(contributorNumber);
        if (existing != null && existing != this) {
            throw new DomainException("PARTY.editContributor.existing.contributor.number");
        }
        
        setName(contributorName);
        setSocialSecurityNumber(contributorNumber);
        setAddress(contributorAddress);
        setAddress(contributorAddress);
        setAreaCode(areaCode);
        setAreaOfAreaCode(areaOfAreaCode);
        setArea(area);
        setParishOfResidence(parishOfResidence);
        setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
        setDistrictOfResidence(districtOfResidence);
    }

}
