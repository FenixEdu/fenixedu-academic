package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

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
	}};
    
    protected WebAddress() {
        super();
    }
    
    public WebAddress(final Party party, final PartyContactType type, final Boolean visible, final String url) {
        this(party, type, visible, false, url);
    }
    
    public WebAddress(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final String url) {
	this();
	init(party, type, visible, defaultContact, url);
    }
    
    public WebAddress(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact) {
	this();
	super.init(party, type, visible, defaultContact);
    }
    
    public WebAddress(final Party party, final PartyContactType type, final boolean defaultContact, final String url) {
	this(party, type, true, defaultContact, url);
    }
    
    protected void init(final Party party, final PartyContactType type, final boolean visible, final boolean defaultContact, final String url) {
	super.init(party, type, visible, defaultContact);
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
}
