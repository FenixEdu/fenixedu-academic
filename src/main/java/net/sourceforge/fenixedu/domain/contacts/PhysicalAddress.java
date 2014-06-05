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

import java.util.Comparator;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import net.sourceforge.fenixedu.injectionCode.AccessControl;
import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class PhysicalAddress extends PhysicalAddress_Base {

    public static Comparator<PhysicalAddress> COMPARATOR_BY_ADDRESS = new Comparator<PhysicalAddress>() {
        @Override
        public int compare(PhysicalAddress contact, PhysicalAddress otherContact) {
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

    static public PhysicalAddress createPhysicalAddress(final Party party, final PhysicalAddressData data, PartyContactType type,
            Boolean isDefault) {
        return new PhysicalAddress(party, type, isDefault, data);
    }

    protected PhysicalAddress() {
        super();
        new PhysicalAddressValidation(this);
    }

    protected PhysicalAddress(final Party party, final PartyContactType type, final boolean defaultContact,
            PhysicalAddressData data) {
        this();
        super.init(party, type, defaultContact);
        setVisibleToAlumni(Boolean.FALSE);
        setVisibleToEmployees(Boolean.FALSE);
        setVisibleToPublic(Boolean.FALSE);
        setVisibleToStudents(Boolean.FALSE);
        setVisibleToTeachers(Boolean.FALSE);
        edit(data);
    }

    // Called from renders with constructor clause.
    public PhysicalAddress(final Party party, final PartyContactType type, final Boolean defaultContact, final String address,
            final String areaCode, final String areaOfAreaCode, final String area, final String parishOfResidence,
            final String districtSubdivisionOfResidence, final String districtOfResidence, final Country countryOfResidence) {
        this(party, type, defaultContact.booleanValue(), new PhysicalAddressData(address, areaCode, areaOfAreaCode, area,
                parishOfResidence, districtSubdivisionOfResidence, districtOfResidence, countryOfResidence));
    }

    public void edit(final PhysicalAddressData data) {
        if (data == null) {
            return;
        }
        if (!data.equals(new PhysicalAddressData(this))) {
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

    }

    // Called from renders with edit clause.
    public void edit(final PartyContactType type, final Boolean defaultContact, final String address, final String areaCode,
            final String areaOfAreaCode, final String area, final String parishOfResidence,
            final String districtSubdivisionOfResidence, final String districtOfResidence, final Country countryOfResidence) {
        super.edit(type, defaultContact);
        edit(new PhysicalAddressData(address, areaCode, areaOfAreaCode, area, parishOfResidence, districtSubdivisionOfResidence,
                districtOfResidence, countryOfResidence));
    }

    @Override
    public boolean isPhysicalAddress() {
        return true;
    }

    public String getCountryOfResidenceName() {
        return hasCountryOfResidence() ? getCountryOfResidence().getName() : StringUtils.EMPTY;
    }

    @Override
    public void deleteWithoutCheckRules() {
        super.deleteWithoutCheckRules();
        // setCountryOfResidence(null);

    }

    @Override
    public void delete() {
        super.delete();
        // setCountryOfResidence(null);
    }

    @Override
    protected void checkRulesToDelete() {
        if (getParty().getPartyContacts(getClass()).size() == 1) {
            throw new DomainException("error.domain.contacts.PhysicalAddress.cannot.remove.last.physicalAddress");
        }
    }

    @Override
    public String getPresentationValue() {
        return getAddress();
    }

    public String getPostalCode() {
        final StringBuilder result = new StringBuilder();
        result.append(getAreaCode());
        result.append(" ");
        result.append(getAreaOfAreaCode());
        return result.toString();
    }

    @Override
    public boolean hasValue(String value) {
        return false;
    }

    @Atomic
    @Override
    public void setValid() {
        if (!isValid()) {
            final PhysicalAddressValidation physicalAddressValidation = (PhysicalAddressValidation) getPartyContactValidation();
            physicalAddressValidation.setValid();
            final String userName = AccessControl.getPerson() == null ? "-" : AccessControl.getPerson().getUsername();
            physicalAddressValidation.setDescription(BundleUtil.getString(Bundle.ACADEMIC,
                    "label.contacts.physicalAddress.validation.description", userName));
        }
    }

    @Override
    public void logCreate(Person person) {
        logCreateAux(person, "label.partyContacts.PhysicalAddress");
    }

    @Override
    public void logEdit(Person person, boolean propertiesChanged, boolean valueChanged, boolean createdNewContact, String newValue) {
        logEditAux(person, propertiesChanged, valueChanged, createdNewContact, newValue, "label.partyContacts.PhysicalAddress");
    }

    @Override
    public void logDelete(Person person) {
        logDeleteAux(person, "label.partyContacts.PhysicalAddress");
    }

    @Override
    public void logValid(Person person) {
        logValidAux(person, "label.partyContacts.PhysicalAddress");
    }

    @Override
    public void logRefuse(Person person) {
        logRefuseAux(person, "label.partyContacts.PhysicalAddress");
    }

    @Deprecated
    public boolean hasParishOfResidence() {
        return getParishOfResidence() != null;
    }

    @Deprecated
    public boolean hasAreaCode() {
        return getAreaCode() != null;
    }

    @Deprecated
    public boolean hasAreaOfAreaCode() {
        return getAreaOfAreaCode() != null;
    }

    @Deprecated
    public boolean hasCountryOfResidence() {
        return getCountryOfResidence() != null;
    }

    @Deprecated
    public boolean hasArea() {
        return getArea() != null;
    }

    @Deprecated
    public boolean hasAddress() {
        return getAddress() != null;
    }

    @Deprecated
    public boolean hasDistrictSubdivisionOfResidence() {
        return getDistrictSubdivisionOfResidence() != null;
    }

    @Deprecated
    public boolean hasDistrictOfResidence() {
        return getDistrictOfResidence() != null;
    }

}
