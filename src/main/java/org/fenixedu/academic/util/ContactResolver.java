package org.fenixedu.academic.util;

import org.fenixedu.academic.domain.contacts.PartyContact;

public interface ContactResolver {

    public String getPresentationValue(PartyContact partyContact);

}
