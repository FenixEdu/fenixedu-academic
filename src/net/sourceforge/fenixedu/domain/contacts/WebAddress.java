package net.sourceforge.fenixedu.domain.contacts;

import org.apache.commons.lang.StringUtils;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;

public class WebAddress extends WebAddress_Base {
    
    protected WebAddress() {
        super();
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
