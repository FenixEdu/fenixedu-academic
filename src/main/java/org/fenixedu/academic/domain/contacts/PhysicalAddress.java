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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.FenixEduAcademicConfiguration;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Party;
import org.fenixedu.academic.predicate.AccessControl;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class PhysicalAddress extends PhysicalAddress_Base {

    static {
        setResolver(PhysicalAddress.class, (pc) -> ((PhysicalAddress) pc).getAddress());
    }

    public static Comparator<PhysicalAddress> COMPARATOR_BY_ADDRESS = new Comparator<PhysicalAddress>() {
        @Override
        public int compare(final PhysicalAddress contact, final PhysicalAddress otherContact) {
            final String address = contact.getAddress();
            final String otherAddress = otherContact.getAddress();
            int result = 0;
            if (address != null && otherAddress != null) {
                result = address.compareTo(otherAddress);
            } else if (address != null) {
                result = 1;
            } else if (otherAddress != null) {
                result = -1;
            }
            return result == 0 ? COMPARATOR_BY_TYPE.compare(contact, otherContact) : result;
        }
    };

    static public PhysicalAddress createPhysicalAddress(final Party party, final PhysicalAddressData data,
            final PartyContactType type, final Boolean isDefault) {
        return new PhysicalAddress(party, type, isDefault, data);
    }

    protected PhysicalAddress() {
        super();
        new PhysicalAddressValidation(this);
    }
    
    protected PhysicalAddress(final Party party, final PartyContactType type, final boolean defaultContact,
            final PhysicalAddressData data) {
        new PhysicalAddress(party, type, defaultContact, data, true);
    }

    protected PhysicalAddress(final Party party, final PartyContactType type, final boolean defaultContact,
            final PhysicalAddressData data, final boolean hasCheckRules) {
        this();
        super.init(party, type, defaultContact);
        setVisibleToPublic(Boolean.FALSE);
        setVisibleToStudents(Boolean.FALSE);
        setVisibleToStaff(Boolean.FALSE);
        edit(data, hasCheckRules);

        if(hasCheckRules) {
            checkRules();
        }
    }

    // Called from renders with constructor clause.
    public PhysicalAddress(final Party party, final PartyContactType type, final Boolean defaultContact, final String address,
            final String areaCode, final String areaOfAreaCode, final String area, final String parishOfResidence,
            final String districtSubdivisionOfResidence, final String districtOfResidence, final Country countryOfResidence) {
        this(party, type, defaultContact.booleanValue(), new PhysicalAddressData(address, areaCode, areaOfAreaCode, area,
                parishOfResidence, districtSubdivisionOfResidence, districtOfResidence, countryOfResidence));

        checkRules();
    }

    public void edit(final PhysicalAddressData data) {
        edit(data, true);
    }
    
    protected void edit(final PhysicalAddressData data, final boolean hasCheckRules) {
        if (data == null) {
            return;
        }
        
        if (!data.equals(new PhysicalAddressData(this))) {
            if(isFiscalAddress() && getCountryOfResidence() != data.getCountryOfResidence()) {
                throw new DomainException("error.PhysicalAddress.cannot.change.countryOfResidence.in.fiscal.address");
            }
            
            super.setAddress(data.getAddress());
            super.setAreaCode(data.getAreaCode());
            super.setAreaOfAreaCode(data.getAreaOfAreaCode());
            super.setArea(data.getArea());
            super.setParishOfResidence(data.getParishOfResidence());
            super.setDistrictSubdivisionOfResidence(data.getDistrictSubdivisionOfResidence());
            super.setDistrictOfResidence(data.getDistrictOfResidence());
            super.setCountryOfResidence(data.getCountryOfResidence());

            if (!waitsValidation()) {
                new PhysicalAddressValidation(this);
            }
            setLastModifiedDate(new DateTime());
        }

        if(hasCheckRules) {
            checkRules();
        }
    }
    
    

    // Called from renders with edit clause.
    public void edit(final PartyContactType type, final Boolean defaultContact, final String address, final String areaCode,
            final String areaOfAreaCode, final String area, final String parishOfResidence,
            final String districtSubdivisionOfResidence, final String districtOfResidence, final Country countryOfResidence) {
        super.edit(type, defaultContact);
        edit(new PhysicalAddressData(address, areaCode, areaOfAreaCode, area, parishOfResidence, districtSubdivisionOfResidence,
                districtOfResidence, countryOfResidence));

        checkRules();
    }

    private void checkRules() {
        if(getCountryOfResidence() == null) {
            throw new DomainException("error.PhysicalAddres.countryOfResidence.required");
        }
    }

    @Override
    public boolean isPhysicalAddress() {
        return true;
    }

    public String getCountryOfResidenceName() {
        return getCountryOfResidence() != null ? getCountryOfResidence().getName() : StringUtils.EMPTY;
    }

    @Override
    public void deleteWithoutCheckRules() {
        if(getParty().getFiscalAddress() == this) {
            throw new DomainException("error.domain.contacts.PhysicalAddress.cannot.remove.fiscal.address");
        }
        
        setCountryOfResidence(null);
        super.deleteWithoutCheckRules();
    }

    @Override
    public void delete() {
        if(getParty().getFiscalAddress() == this) {
            throw new DomainException("error.domain.contacts.PhysicalAddress.cannot.remove.fiscal.address");
        }
        
        setCountryOfResidence(null);
        super.delete();
    }

    @Override
    protected void checkRulesToDelete() {
        if (getParty().getPartyContacts(getClass()).size() == 1) {
            throw new DomainException("error.domain.contacts.PhysicalAddress.cannot.remove.last.physicalAddress");
        }
    }

    public String getPostalCode() {
        final StringBuilder result = new StringBuilder();
        result.append(getAreaCode());
        result.append(" ");
        result.append(getAreaOfAreaCode());
        return result.toString();
    }

    @Override
    public boolean hasValue(final String value) {
        return false;
    }

    @Atomic
    @Override
    public void setValid() {
        if (!isValid()) {
            final PhysicalAddressValidation physicalAddressValidation = (PhysicalAddressValidation) getPartyContactValidation();
            physicalAddressValidation.setValid();
            final String userName = AccessControl.getPerson() == null ? "-" : AccessControl.getPerson().getUsername();
            physicalAddressValidation.setDescription(
                    BundleUtil.getString(Bundle.ACADEMIC, "label.contacts.physicalAddress.validation.description", userName));
        }
    }

    @Override
    public void logCreate(final Person person) {
        logCreateAux(person, "label.partyContacts.PhysicalAddress");
    }

    @Override
    public void logEdit(final Person person, final boolean propertiesChanged, final boolean valueChanged,
            final boolean createdNewContact, final String newValue) {
        logEditAux(person, propertiesChanged, valueChanged, createdNewContact, newValue, "label.partyContacts.PhysicalAddress");
    }

    @Override
    public void logDelete(final Person person) {
        logDeleteAux(person, "label.partyContacts.PhysicalAddress");
    }

    @Override
    public void logValid(final Person person) {
        logValidAux(person, "label.partyContacts.PhysicalAddress");
    }

    @Override
    public void logRefuse(final Person person) {
        logRefuseAux(person, "label.partyContacts.PhysicalAddress");
    }

    @Override
    public boolean isToBeValidated() {
        return requiresValidation();
    }
    
    public boolean isFiscalAddress() {
        return Boolean.TRUE.equals(super.getFiscalAddress());
    }

    public static boolean requiresValidation() {
        return FenixEduAcademicConfiguration.getPhysicalAddressRequiresValidation();
    }
    
    public String getUiFiscalPresentationValue() {
        final List<String> compounds = new ArrayList<>();
        
        if(StringUtils.isNotEmpty(getAddress())) {
            compounds.add(getAddress());
        }
        
        if(StringUtils.isNotEmpty(getAreaCode())) {
            compounds.add(getAreaCode());
        }
        
        if(StringUtils.isNotEmpty(getDistrictSubdivisionOfResidence())) {
            compounds.add(getDistrictSubdivisionOfResidence());
        }
        
        if(getCountryOfResidence() != null) {
            compounds.add(getCountryOfResidence().getLocalizedName().getContent());
        }
        
        return String.join(" ", compounds);
    }
    
}
