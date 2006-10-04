/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.parking.ParkingPartyClassification;

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

    public List<Party> getParentParties(AccountabilityTypeEnum accountabilityTypeEnum,
	    Class parentPartyClass) {
	final List<Party> result = new ArrayList<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getParentParty().getClass().equals(parentPartyClass)) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }

    public List<Party> getParentParties(Class parentPartyClass) {
	final List<Party> result = new ArrayList<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getParentParty().getClass().equals(parentPartyClass)) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }

    public List<Party> getChildParties(Class childPartyClass) {
	final List<Party> result = new ArrayList<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getChildParty().getClass().equals(childPartyClass)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    public List<Party> getChildParties(AccountabilityTypeEnum accountabilityTypeEnum,
	    Class childPartyClass) {
	final List<Party> result = new ArrayList<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getChildParty().getClass().equals(childPartyClass)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    public List<Party> getChildParties(List<AccountabilityTypeEnum> accountabilityTypeEnums,
	    Class childPartyClass) {
	final List<Party> result = new ArrayList<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountabilityTypeEnums.contains(accountability.getAccountabilityType().getType())
		    && accountability.getChildParty().getClass().equals(childPartyClass)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    public List<Accountability> getParentAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum) {
	final List<Accountability> result = new ArrayList<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public List<Accountability> getChildAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum) {
	final List<Accountability> result = new ArrayList<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public List<Accountability> getParentAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum,
	    Class accountabilityClass) {
	final List<Accountability> result = new ArrayList<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public List<Accountability> getChildAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum,
	    Class accountabilityClass) {
	final List<Accountability> result = new ArrayList<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public List<Accountability> getParentAccountabilities(Class parentClass) {
	final List<Accountability> result = new ArrayList<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getParentParty().getClass().equals(parentClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }
    
    public List<Accountability> getChildAccountabilities(Class childClass) {
	final List<Accountability> result = new ArrayList<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getChildParty().getClass().equals(childClass)) {
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
		return collator.compare(party.getName().toLowerCase(), otherParty.getName()
			.toLowerCase());
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

	if (contributorNumber != null) {
	    for (final Party party : RootDomainObject.getInstance().getPartys()) {
		if (party.getSocialSecurityNumber() != null
			&& party.getSocialSecurityNumber().equalsIgnoreCase(contributorNumber)) {
		    return party;
		}
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

    public boolean isPerson() {
	return false;
    }

    public abstract ParkingPartyClassification getPartyClassification();

}
