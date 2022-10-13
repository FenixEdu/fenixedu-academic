/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.contacts;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;

public abstract class PartyContactValidation extends PartyContactValidation_Base {
    private static final int MAX_TRIES = 3;

    public PartyContactValidation() {
        super();
        reset();
    }

    public void delete() {
        setContactRoot(null);
        setPartyContact(null);
        deleteDomainObject();
    }

    public void reset() {
        setInvalid();
        setRequestDate(new DateTime());
        setTries(MAX_TRIES);
        setToken(null);
    }

    public void init(final PartyContact contact) {
        setPartyContact(contact);
        if (contact.isDefault()) {
            setToBeDefault(Boolean.TRUE);
        }
    }

    public PartyContactValidation(final PartyContact contact) {
        this();
        init(contact);
    }

    private boolean validate(final boolean result) {
        if (isInvalid()) {
            if (result) {
                setValid();
            } else {
                setInvalid();
            }
        }
        return isValid();
    }

    public boolean processValidation(final String token) {

        if (isRefused()) {
            return false;
        }

        if (!StringUtils.isEmpty(getToken()) && !StringUtils.isEmpty(token)) {
            validate(getToken().equals(token));
            if (isInvalid()) {
                setTries(getTries() - 1);
                if (getTries() <= 0) {
                    setRefused();
                }
            }
        }

        return isValid();
    }

    protected void setValid() {
        if (!isInvalid()) {
            return;
        }
        if (getContactRoot() != null) {
            setContactRoot(null);
        }

        super.setState(PartyContactValidationState.VALID);
        setLastChangeDate(new DateTime());
        final PartyContact partyContact = getPartyContact();
        partyContact.getParty().logValidContact(partyContact);
        if (partyContact.getPrevPartyContact() != null) {
            partyContact.getPrevPartyContact().deleteWithoutCheckRules();
        }

        final Boolean toBeDefault = getToBeDefault();
        if (toBeDefault != null) {
            partyContact.setDefaultContactInformation(toBeDefault);
        }
    }

    private void setNotValidState(final PartyContactValidationState state) {
        if (getContactRoot() == null) {
            setContactRoot(ContactRoot.getInstance());
        }
        super.setState(state);
        setLastChangeDate(new DateTime());
    }

    public Integer getAvailableTries() {
        return getTries() > 0 ? getTries() : 0;
    }

    protected void setInvalid() {
        setNotValidState(PartyContactValidationState.INVALID);
    }

    protected void setRefused() {
        if (!isRefused()) {
            getPartyContact().getParty().logRefuseContact(getPartyContact());
            setNotValidState(PartyContactValidationState.REFUSED);
            getPartyContact().deleteWithoutCheckRules();
        }
    }

    private boolean isState(final PartyContactValidationState state) {
        return state.equals(getState());
    }

    public boolean isValid() {
        return isState(PartyContactValidationState.VALID);
    }

    public boolean isInvalid() {
        return isState(PartyContactValidationState.INVALID);
    }

    public boolean isRefused() {
        return isState(PartyContactValidationState.REFUSED);
    }

}
