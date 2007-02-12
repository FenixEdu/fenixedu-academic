/*
 * Created on Feb 10, 2006
 *	by mrsp
 */
package net.sourceforge.fenixedu.domain.organizationalStructure;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.parking.ParkingPartyClassification;
import net.sourceforge.fenixedu.domain.research.activity.Participation;
import net.sourceforge.fenixedu.domain.research.activity.ResearchActivity;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.util.StringNormalizer;

public abstract class Party extends Party_Base {

    public Party() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(getClass().getName());
	createAccount(AccountType.INTERNAL);
	createAccount(AccountType.EXTERNAL);
    }
    
    @Override
    public void setName(String name) {
	if(name == null || StringUtils.isEmpty(name.trim())) {
	    throw new DomainException("error.unit.empty.name");
	}
	super.setName(name);
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

    public Collection<? extends Party> getParentParties(AccountabilityTypeEnum accountabilityTypeEnum,
	    Class parentPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getParentParty().getClass().equals(parentPartyClass)) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getParentParties(Class parentPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getParentParty().getClass().equals(parentPartyClass)) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getParentParties(
	    List<AccountabilityTypeEnum> accountabilityTypeEnums, Class parentPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountabilityTypeEnums.contains(accountability.getAccountabilityType().getType())
		    && accountability.getParentParty().getClass().equals(parentPartyClass)) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }


    public Collection<? extends Party> getChildParties(Class childPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getChildParty().getClass().equals(childPartyClass)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getChildParties(AccountabilityTypeEnum accountabilityTypeEnum,
	    Class childPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getChildParty().getClass().equals(childPartyClass)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getChildParties(
	    List<AccountabilityTypeEnum> accountabilityTypeEnums, Class childPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountabilityTypeEnums.contains(accountability.getAccountabilityType().getType())
		    && accountability.getChildParty().getClass().equals(childPartyClass)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    protected Collection<? extends Party> getChildParties(final PartyTypeEnum type, final Class<? extends Party> clazz) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getChildParty().getType() == type 
		    && accountability.getChildParty().getClass().equals(clazz)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }
    
    public Collection<? extends Accountability> getParentAccountabilities(
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilities(
	    AccountabilityTypeEnum accountabilityTypeEnum) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getParentAccountabilities(
	    AccountabilityTypeEnum accountabilityTypeEnum, Class accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilities(
	    AccountabilityTypeEnum accountabilityTypeEnum, Class accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getParentAccountabilities(Class parentClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getParentParty().getClass().equals(parentClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilities(Class childClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
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
	super.deleteDomainObject();
    }

    private boolean canBeDeleted() {
	return !hasAnyResearchInterests() && !hasAnyProjectParticipations()
		&& !hasAnyResearchActivities() && !hasAnyBoards() && !hasAnyChilds();
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
    
    public boolean isUnit() {
    	return false;
    }

    public abstract ParkingPartyClassification getPartyClassification();

    public boolean verifyNameEquality(String[] nameWords) {

	if (nameWords == null) {
	    return true;
	}

	if (getName() != null) {
	    String[] personNameWords = getName().trim().split(" ");
	    StringNormalizer.normalize(personNameWords);
	    int j, i;
	    for (i = 0; i < nameWords.length; i++) {
		if (!nameWords[i].equals("")) {
		    for (j = 0; j < personNameWords.length; j++) {
			if (personNameWords[j].equals(nameWords[i])) {
			    break;
			}
		    }
		    if (j == personNameWords.length) {
			return false;
		    }
		}
	    }
	    if (i == nameWords.length) {
		return true;
	    }
	}
	return false;
    }
    public List<ResearchActivity> getResearchActivities() {
    	List<ResearchActivity> activities = new ArrayList<ResearchActivity> ();
    	for(Participation participation : this.getParticipations()) {
    		activities.add(participation.getResearchActivity());
    	}
    	return activities;
    }
    
    public List<Participation> getAllEventParticipations(){
    	List<Participation> eventParticipations = new ArrayList<Participation> ();
    	for(Participation participation : this.getParticipations()) {
    		if(participation.isEventParticipation()) {
    			eventParticipations.add(participation);
    		}
    	}
    	return eventParticipations;
    }
    
    public List<Participation> getAllScientificJournalsParticipations(){
    	List<Participation> scientifJournalParticipations = new ArrayList<Participation> ();
    	for(Participation participation : this.getParticipations()) {
    		if(participation.isScientificJournaltParticipation()) {
    			scientifJournalParticipations.add(participation);
    		}
    	}
    	return scientifJournalParticipations;
    }
    
    public Integer getResearchActivitiesCount() {
    	return this.getResearchActivities().size();
    }
    
    public boolean hasAnyResearchActivities() {
    	return this.hasAnyParticipations();
    }
    
    public void removeResearchActivities(ResearchActivity researchActivity) {
    	for(Participation participation : this.getParticipations()) {
    		if(participation.getResearchActivity().equals(researchActivity)) {
    			participation.delete();
    		}
    	}
    }
}
