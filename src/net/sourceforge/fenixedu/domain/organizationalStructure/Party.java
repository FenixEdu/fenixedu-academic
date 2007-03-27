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

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accounting.Account;
import net.sourceforge.fenixedu.domain.accounting.AccountType;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactType;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.WebAddress;
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

    public PartyTypeEnum getType() {
	return getPartyType() != null ? getPartyType().getType() : null;
    }
    
    public void setType(PartyTypeEnum partyTypeEnum) {
        if(partyTypeEnum != null) {
            PartyType partyType = PartyType.readPartyTypeByType(partyTypeEnum);
            if(partyType == null) {
                throw new DomainException("error.Party.unknown.partyType");
            }
            setPartyType(partyType);
        } else {
            setPartyType(null);
        }
    }
    
    public Collection<? extends Party> getParentParties(AccountabilityTypeEnum accountabilityTypeEnum, Class<? extends Party> parentPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getParentParty().getClass().equals(parentPartyClass)) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getParentParties(Class<? extends Party> parentPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getParentParty().getClass().equals(parentPartyClass)) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getParentParties(List<AccountabilityTypeEnum> accountabilityTypeEnums, Class<? extends Party> parentPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountabilityTypeEnums.contains(accountability.getAccountabilityType().getType())
		    && accountability.getParentParty().getClass().equals(parentPartyClass)) {
		result.add(accountability.getParentParty());
	    }
	}
	return result;
    }


    public Collection<? extends Party> getChildParties(Class<? extends Party> childPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getChildParty().getClass().equals(childPartyClass)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getChildParties(AccountabilityTypeEnum accountabilityTypeEnum, Class<? extends Party> childPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getChildParty().getClass().equals(childPartyClass)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    public Collection<? extends Party> getChildParties(List<AccountabilityTypeEnum> accountabilityTypeEnums, Class<? extends Party> childPartyClass) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountabilityTypeEnums.contains(accountability.getAccountabilityType().getType())
		    && accountability.getChildParty().getClass().equals(childPartyClass)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }

    protected Collection<? extends Party> getChildParties(PartyTypeEnum type, Class<? extends Party> clazz) {
	final Set<Party> result = new HashSet<Party>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getChildParty().getType() == type 
		    && accountability.getChildParty().getClass().equals(clazz)) {
		result.add(accountability.getChildParty());
	    }
	}
	return result;
    }
        
    public Collection<? extends Accountability> getParentAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getParentAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum, Class<? extends Accountability> accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilities(AccountabilityTypeEnum accountabilityTypeEnum, Class<? extends Accountability> accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getAccountabilityType().getType() == accountabilityTypeEnum
		    && accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getParentAccountabilitiesByParentClass(Class<? extends Party> parentClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getParentsSet()) {
	    if (accountability.getParentParty().getClass().equals(parentClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    public Collection<? extends Accountability> getChildAccountabilitiesByChildClass(Class<? extends Party> childClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getChildParty().getClass().equals(childClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }
    
    public Collection<? extends Accountability> getChildAccountabilitiesByAccountabilityClass(Class<? extends Accountability> accountabilityClass) {
	final Set<Accountability> result = new HashSet<Accountability>();
	for (final Accountability accountability : getChildsSet()) {
	    if (accountability.getClass().equals(accountabilityClass)) {
		result.add(accountability);
	    }
	}
	return result;
    }

    protected void delete() {	
	for (; !getAccounts().isEmpty(); getAccounts().get(0).delete());
	removePartyType();	
	removeRootDomainObject();
	deleteDomainObject();
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
    
    public List<PartyContact> getPartyContacts(final Class<? extends PartyContact> clazz, final PartyContactType type) {
	final List<PartyContact> result = new ArrayList<PartyContact>();
	for (final PartyContact contact : getPartyContactsSet()) {
	    if (clazz.isAssignableFrom(contact.getClass()) && (type == null || contact.getType() == type)) {
		result.add(contact);
	    }
	}
	return result;
    }
    
    public List<PartyContact> getPartyContacts(final Class<? extends PartyContact> clazz) {
	return getPartyContacts(clazz, null);
    }
    
    public boolean hasAnyPartyContact(final Class<? extends PartyContact> clazz, final PartyContactType type) {
	for (final PartyContact contact : getPartyContactsSet()) {
	    if (clazz.isAssignableFrom(contact.getClass()) && contact.getType() == type) {
		return true;
	    }
	}
	return false;
    }
    
    public PartyContact getDefaultPartyContact(final Class<? extends PartyContact> clazz) {
	for (final PartyContact contact : getPartyContactsSet()) {
	    if (clazz.isAssignableFrom(contact.getClass()) && contact.isDefault()) {
		return contact;
	    }
	}
	return null;
    }
    
    public boolean hasDefaultPartyContact(final Class<? extends PartyContact> clazz) {
	return getDefaultPartyContact(clazz) != null;
    }
    
    public WebAddress getDefaultWebAddress() {
	return (WebAddress) getDefaultPartyContact(WebAddress.class);
    }
    
    public PhysicalAddress getDefaultPhysicalAddress() {
	return (PhysicalAddress) getDefaultPartyContact(PhysicalAddress.class);
    }
    
    public Phone getDefaultPhone() {
	return (Phone) getDefaultPartyContact(Phone.class);
    }
    
    public MobilePhone getDefaultMobilePhone() {
	return (MobilePhone) getDefaultPartyContact(MobilePhone.class);
    }
    
    public EmailAddress getDefaultEmailAddress() {
	return (EmailAddress) getDefaultPartyContact(EmailAddress.class);
    }
    
    /* ~~~~~~~~~~~~~~~~~~~~~
     * PartyContacts
     * ~~~~~~~~~~~~~~~~~~~~~
     * These methods are used to support current functionality (physicalAddress, webAddress, ... - limited to one for each type)
     * after interface changes we must edit contacts in another way and remove the following methods
     * 
     */
    protected PhysicalAddress getOrCreateDefaultPhysicalAddress() {
	final PhysicalAddress physicalAdress = getDefaultPhysicalAddress();
	return physicalAdress != null ? physicalAdress : PartyContact.createDefaultPersonalPhysicalAddress(this);
    }
    
    public String getAddress() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getAddress() : null;
    }
    
    public void setAddress(String address) {
	getOrCreateDefaultPhysicalAddress().setAddress(address);
    }
    
    public String getAreaCode() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getAreaCode() : null;
    }
    
    public void setAreaCode(String areaCode) {
	getOrCreateDefaultPhysicalAddress().setAreaCode(areaCode);
    }
    
    public String getAreaOfAreaCode() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getAreaOfAreaCode() : null;
    }
    
    public void setAreaOfAreaCode(String areaOfAreaCode) {
	getOrCreateDefaultPhysicalAddress().setAreaOfAreaCode(areaOfAreaCode);
    }
    
    public String getArea() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getArea() : null;
    }
    
    public void setArea(String area) {
	getOrCreateDefaultPhysicalAddress().setArea(area);
    }
    
    public String getParishOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getParishOfResidence() : null;
    }
    
    public void setParishOfResidence(String parishOfResidence) {
	getOrCreateDefaultPhysicalAddress().setParishOfResidence(parishOfResidence);
    }
    
    public String getDistrictSubdivisionOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getDistrictSubdivisionOfResidence() : null;
    }
    
    public void setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
	getOrCreateDefaultPhysicalAddress().setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
    }
    
    public String getDistrictOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getDistrictOfResidence() : null;
    }
    
    public void setDistrictOfResidence(String districtOfResidence) {
        getOrCreateDefaultPhysicalAddress().setDistrictOfResidence(districtOfResidence);
    }
    
    public Country getCountryOfResidence() {
	final PhysicalAddress physicalAddress = getDefaultPhysicalAddress();
	return physicalAddress != null ? physicalAddress.getCountryOfResidence() : null;
    }
    
    public void setCountryOfResidence(Country countryOfResidence) {
	getOrCreateDefaultPhysicalAddress().setCountryOfResidence(countryOfResidence);
    }

    protected WebAddress getOrCreateDefaultWebAddress() {
	final WebAddress webAddress = getDefaultWebAddress();
	return webAddress != null ? webAddress : PartyContact.createDefaultPersonalWebAddress(this);
    }
    
    public String getWebAddress() {
        final WebAddress webAddress = getDefaultWebAddress();
        return webAddress != null ? webAddress.getUrl() : null;
    }
    
    public void setWebAddress(String webAddress) {
	getOrCreateDefaultWebAddress().setUrl(webAddress);
    }
    
    protected Phone getOrCreateDefaultPhone() {
	final Phone phone = getDefaultPhone();
	return phone != null ? phone : (Phone) PartyContact.createDefaultPersonalPhone(this);
    }
    
    public String getPhone() {
	final Phone phone = getDefaultPhone();
	return phone != null ? phone.getNumber() : null;
    }
    
    public void setPhone(String phone) {
	getOrCreateDefaultPhone().setNumber(phone);
    }
    
    protected MobilePhone getOrCreateDefaultMobilePhone() {
	final MobilePhone mobilePhone = getDefaultMobilePhone();
	return mobilePhone != null ? mobilePhone : (MobilePhone) PartyContact.createDefaultPersonalMobilePhone(this);
    }
    
    public String getMobile() {
	final MobilePhone phone = getDefaultMobilePhone();
	return phone != null ? phone.getNumber() : null;
    }
    
    public void setMobile(String mobile) {
	getOrCreateDefaultMobilePhone().setNumber(mobile);
    }
    
    private EmailAddress getPersonalEmailAddress() {
	final List<PartyContact> partyContacts = getPartyContacts(EmailAddress.class, PartyContactType.PERSONAL);
	return partyContacts.isEmpty() ? null : (EmailAddress) partyContacts.get(0); // actually exists only one
    }

    public String getEmail() {
	final EmailAddress emailAddress = getPersonalEmailAddress();
	return emailAddress != null ? emailAddress.getValue() : null;
    }
    
    public void setEmail(String email) {
	final EmailAddress emailAddress = getPersonalEmailAddress();
	if (emailAddress == null) {
	    PartyContact.createEmailAddress(this, PartyContactType.PERSONAL, true, false, email);
	} else {
	    emailAddress.setValue(email);
	}
    }
    
    // this method is not necessary if we filter EmailAddress by visible attribute
    public Boolean getAvailableEmail() {
	final EmailAddress emailAddress = getPersonalEmailAddress();
	return emailAddress != null ? emailAddress.isVisible() : false;
    }
    
    public void setAvailableEmail(Boolean availableEmail) {
	final EmailAddress emailAddress = getPersonalEmailAddress();
	if (emailAddress != null) {
	    emailAddress.setVisible(availableEmail);
	}
    }
    /* ~~~~~~~~~~~~~~~~~~~~~
     * End: PartyContacts
     * ~~~~~~~~~~~~~~~~~~~~~
     */
}
