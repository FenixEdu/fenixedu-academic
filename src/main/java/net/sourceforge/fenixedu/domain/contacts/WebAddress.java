package net.sourceforge.fenixedu.domain.contacts;

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.PersonInformationLog;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.util.BundleUtil;

import org.apache.commons.lang.StringUtils;

public class WebAddress extends WebAddress_Base {

    public static Comparator<WebAddress> COMPARATOR_BY_URL = new Comparator<WebAddress>() {
        @Override
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
        WebAddress result = null;
        if (!StringUtils.isEmpty(url)) {
            result =
                    new WebAddress(party, type, visibleToPublic, visibleToStudents, visibleToTeachers, visibleToEmployees,
                            visibleToAlumni, isDefault, url);
        }
        return result;
    }

    public static WebAddress createWebAddress(Party party, String url, PartyContactType type, boolean isDefault) {
        for (WebAddress webAddress : party.getWebAddresses()) {
            if (webAddress.getUrl().equals(url)) {
                return webAddress;
            }
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

    @Override
    public void logCreate(Person person) {
        logCreateAux(person, "label.partyContacts.WebAddress");
    }

    @Override
    public void logEdit(Person person, boolean propertiesChanged, boolean valueChanged, boolean createdNewContact, String newValue) {
        logEditAux(person, propertiesChanged, valueChanged, createdNewContact, newValue, "label.partyContacts.WebAddress");
    }

    @Override
    public void logEditAux(Person person, boolean propertiesChanged, boolean valueChanged, boolean createdNewContact,
            String newValue, String typeKey) {
        final String infoLabel = BundleUtil.getStringFromResourceBundle("resources.ApplicationResources", typeKey);

        boolean oldValueDiffersFromNew = false;
        if (valueChanged) {
            if (hasPrevPartyContact()) {
                oldValueDiffersFromNew = getPrevPartyContact().getPresentationValue().compareTo(getPresentationValue()) != 0;
            }
        }

        if (propertiesChanged && !valueChanged) {
            // only properties were changed
            PersonInformationLog.createLog(person, "resources.MessagingResources", "log.personInformation.contact.generic.edit",
                    infoLabel, this.getPresentationValue(), person.getIstUsername());
        } else if (valueChanged) {
            if (oldValueDiffersFromNew) {
                // value was changed
                PersonInformationLog.createLog(person, "resources.MessagingResources",
                        "log.personInformation.contact.generic.edit.values", infoLabel, getPrevPartyContact()
                                .getPresentationValue(), this.getPresentationValue(), person.getIstUsername());
            }
        }
    }

    @Override
    public void logDelete(Person person) {
        logDeleteAux(person, "label.partyContacts.WebAddress");
    }

}
