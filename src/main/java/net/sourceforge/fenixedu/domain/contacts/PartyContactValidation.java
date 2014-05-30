/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.sourceforge.fenixedu.domain.contacts;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.predicates.Predicate;

public abstract class PartyContactValidation extends PartyContactValidation_Base {
    private static final int MAX_TRIES = 3;

    public static class PartyContactValidationPredicate extends Predicate<PartyContactValidation> {

        private final PartyContactValidationState state;

        public PartyContactValidationPredicate(PartyContactValidationState state) {
            this.state = state;
        }

        @Override
        public boolean eval(PartyContactValidation t) {
            return t.getState().equals(state);
        }

    }

    public static final PartyContactValidationPredicate PREDICATE_INVALID = new PartyContactValidationPredicate(
            PartyContactValidationState.INVALID);
    public static final PartyContactValidationPredicate PREDICATE_VALID = new PartyContactValidationPredicate(
            PartyContactValidationState.VALID);
    public static final PartyContactValidationPredicate PREDICATE_REFUSED = new PartyContactValidationPredicate(
            PartyContactValidationState.REFUSED);

    public PartyContactValidation() {
        super();
        reset();
    }

    public void reset() {
        setInvalid();
        setRequestDate(new DateTime());
        setTries(MAX_TRIES);
        setToken(null);
    }

    public void init(PartyContact contact) {
        setPartyContact(contact);
        if (contact.isDefault()) {
            setToBeDefault(Boolean.TRUE);
        }
    }

    public PartyContactValidation(PartyContact contact) {
        this();
        init(contact);
    }

    private boolean validate(boolean result) {
        if (isInvalid()) {
            if (result) {
                setValid();
            } else {
                setInvalid();
            }
        }
        return isValid();
    }

    @Atomic
    public Boolean processValidation(String token) {

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
        if (hasBennu()) {
            setRootDomainObject(null);
        }

        super.setState(PartyContactValidationState.VALID);
        setLastChangeDate(new DateTime());
        final PartyContact partyContact = getPartyContact();
        partyContact.getParty().logValidContact(partyContact);
        if (partyContact.hasPrevPartyContact()) {
            partyContact.getPrevPartyContact().deleteWithoutCheckRules();
        }

        final Boolean toBeDefault = getToBeDefault();
        if (toBeDefault != null) {
            partyContact.setDefaultContactInformation(toBeDefault);
        }
    }

    private void setNotValidState(PartyContactValidationState state) {
        if (!hasBennu()) {
            setRootDomainObject(Bennu.getInstance());
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

    private boolean isState(PartyContactValidationState state) {
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

    @Override
    @Atomic
    public void setState(PartyContactValidationState state) {
        switch (state) {
        case INVALID:
            setInvalid();
            break;
        case REFUSED:
            setRefused();
            break;
        case VALID:
            setValid();
            break;
        }
    }

    public void triggerValidationProcess() {
        // TODO Auto-generated method stub
    }

    public void triggerValidationProcessIfNeeded() {
        if (getToken() == null) {
            triggerValidationProcess();
        }
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasState() {
        return getState() != null;
    }

    @Deprecated
    public boolean hasRequestDate() {
        return getRequestDate() != null;
    }

    @Deprecated
    public boolean hasToBeDefault() {
        return getToBeDefault() != null;
    }

    @Deprecated
    public boolean hasTries() {
        return getTries() != null;
    }

    @Deprecated
    public boolean hasToken() {
        return getToken() != null;
    }

    @Deprecated
    public boolean hasLastChangeDate() {
        return getLastChangeDate() != null;
    }

    @Deprecated
    public boolean hasPartyContact() {
        return getPartyContact() != null;
    }

}
