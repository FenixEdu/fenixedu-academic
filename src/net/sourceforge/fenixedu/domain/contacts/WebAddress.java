package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

import org.apache.commons.lang.StringUtils;

public class WebAddress extends WebAddress_Base {

    public static Comparator<WebAddress> COMPARATOR_BY_URL = new Comparator<WebAddress>() {
	public int compare(WebAddress contact, WebAddress otherContact) {
	    final String url = contact.getUrl();
	    final String otherUrl = otherContact.getUrl();
	    int result = 0;
	    if (url != null && otherUrl != null) {
		result = url.compareTo(otherUrl);
	    } else if (url != null) {
		result = 1;
	    } else if (otherUrl != null) {
		result = -1;
	    }
	    return (result == 0) ? COMPARATOR_BY_TYPE.compare(contact, otherContact) : result;
	}
    };

    public static WebAddress createWebAddress(Party party, String url, PartyContactType type, Boolean isDefault,
	    Boolean visibleToPublic, Boolean visibleToStudents, Boolean visibleToTeachers, Boolean visibleToEmployees,
	    Boolean visibleToAlumni) {
	// for (WebAddress emailAddress : party.getWebAddresses()) {
	// if (emailAddress.getUrl().equals(url) && emailAddress.isActive()) {
	// return emailAddress;
	// }
	// }
	return (!StringUtils.isEmpty(url)) ? new WebAddress(party, type, visibleToPublic, visibleToStudents, visibleToTeachers,
		visibleToEmployees, visibleToAlumni, isDefault, url) : null;
    }

    public static WebAddress createWebAddress(Party party, String url, PartyContactType type, boolean isDefault) {
	for (WebAddress webAddress : party.getWebAddresses()) {
	    if (webAddress.getUrl().equals(url))
		return webAddress;
	}
	return (!StringUtils.isEmpty(url)) ? new WebAddress(party, type, isDefault, url) : null;
    }

    protected WebAddress() {
	super();
	// no validation is necessary
    }

    protected WebAddress(final Party party, final PartyContactType type, final boolean defaultContact, final String url) {
	this();
	super.init(party, type, defaultContact);
	checkParameters(url);
	super.setUrl(url);
    }

    protected WebAddress(final Party party, final PartyContactType type, final boolean visibleToPublic,
	    final boolean visibleToStudents, final boolean visibleToTeachers, final boolean visibleToEmployees,
	    final boolean visibleToAlumni, final boolean defaultContact, final String url) {
	this();
	super.init(party, type, visibleToPublic, visibleToStudents, visibleToTeachers, visibleToEmployees, visibleToAlumni,
		defaultContact);
	checkParameters(url);
	super.setUrl(url);
    }

    private void checkParameters(final String url) {
	if (StringUtils.isEmpty(url)) {
	    throw new DomainException("error.domain.contacts.WebAddress.invalid.url");
	}
    }

    @Override
    public boolean isWebAddress() {
	return true;
    }

    public void edit(final String url) {
	super.setUrl(url);
    }

    public boolean hasUrl() {
	return getUrl() != null && getUrl().length() > 0;
    }

    @Override
    public String getPresentationValue() {
	return getUrl();
    }

    @Override
    public boolean hasValue(String value) {
	return hasUrl() && getUrl().equals(value);
    }

    @Override
    public void setValid() {
	if (hasPrevPartyContact()) {
	    getPrevPartyContact().deleteWithoutCheckRules();
	}
    }
}
