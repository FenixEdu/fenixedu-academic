package net.sourceforge.fenixedu.dataTransferObject.contacts;

import java.util.List;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.EmailAddress;
import net.sourceforge.fenixedu.domain.contacts.MobilePhone;
import net.sourceforge.fenixedu.domain.contacts.Phone;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;

public class PendingPartyContactBean {
    private Person person;

    public PendingPartyContactBean(final Person person) {
	this.person = person;
    }

    public Person getPerson() {
	return person;
    }

    public void setPerson(Person person) {
	this.person = person;
    }

    public PhysicalAddress getDefaultPhysicalAddress() {
	final List<PhysicalAddress> pendingOrValidPhysicalAddresses = getPerson().getPendingOrValidPhysicalAddresses();
	if (!pendingOrValidPhysicalAddresses.isEmpty()) {
	    if (pendingOrValidPhysicalAddresses.size() == 1) {
		return pendingOrValidPhysicalAddresses.get(0);
	    } else {
		for (PhysicalAddress physicalAddress : pendingOrValidPhysicalAddresses) {
		    if (physicalAddress.hasPartyContactValidation()) {
			if (physicalAddress.getPartyContactValidation().getToBeDefault() != null) {
			    if (physicalAddress.getPartyContactValidation().getToBeDefault()) {
				return physicalAddress;
			    }
			}
		    }
		}
		return null;
	    }
	} else {
	    return null;
	}
    }

    public Phone getDefaultPhone() {
	Phone defaultPhone = getPerson().getDefaultPhone();
	if (defaultPhone != null) {
	    return defaultPhone;
	}
	final List<Phone> pendingPhones = getPerson().getPendingPhones();
	for (Phone phone : pendingPhones) {
	    if (Boolean.TRUE.equals(phone.getPartyContactValidation().getToBeDefault())) {
		return phone;
	    }
	}
	return null;
    }

    public MobilePhone getDefaultMobilePhone() {
	MobilePhone defaultPhone = getPerson().getDefaultMobilePhone();
	if (defaultPhone != null) {
	    return defaultPhone;
	}
	final List<MobilePhone> pendingPhones = getPerson().getPendingMobilePhones();
	for (MobilePhone phone : pendingPhones) {
	    if (Boolean.TRUE.equals(phone.getPartyContactValidation().getToBeDefault())) {
		return phone;
	    }
	}
	return null;
    }

    public EmailAddress getDefaultEmailAddress() {
	EmailAddress defaultEmailAddress = getPerson().getDefaultEmailAddress();
	if (defaultEmailAddress != null) {
	    return defaultEmailAddress;
	}
	final List<EmailAddress> pendingEmailAddresses = getPerson().getPendingEmailAddresses();
	for (EmailAddress emailAddress : pendingEmailAddresses) {
	    if (Boolean.TRUE.equals(emailAddress.getPartyContactValidation().getToBeDefault())) {
		return emailAddress;
	    }
	}
	return null;
    }

}
