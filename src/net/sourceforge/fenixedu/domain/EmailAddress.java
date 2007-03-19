package net.sourceforge.fenixedu.domain;


public class EmailAddress extends EmailAddress_Base {
    
    public EmailAddress(final Person person, final String value, final boolean isInstitutionalEmail) {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setPerson(person);
        setIsInstitutionalEmail(Boolean.valueOf(isInstitutionalEmail));
        setValue(value);
    }

    public void delete() {
	removePerson();
	removeRootDomainObject();
	deleteDomainObject();
    }

    public static EmailAddress find(final String emailAddressString) {
	for (final EmailAddress emailAddress : RootDomainObject.getInstance().getEmailAddressesSet()) {
	    if (emailAddress.getValue() != null &&
		    emailAddress.getValue().equalsIgnoreCase(emailAddressString)) {
		return emailAddress;
	    }
	}
	return null;
    }

}
