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
package org.fenixedu.academic.dto.contacts;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.contacts.PartyContact;
import org.fenixedu.academic.domain.contacts.PartyContactValidation;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.contacts.PhysicalAddressData;
import org.fenixedu.academic.domain.contacts.PhysicalAddressValidation;
import org.fenixedu.academic.domain.organizationalStructure.Party;

public class PhysicalAddressBean extends PartyContactBean {

    private static final String CONTACT_NAME = "PhysicalAddress";

    private static final long serialVersionUID = 852136165195545415L;

    private String address;
    private String areaCode;
    private String areaOfAreaCode;
    private String area;
    private String parishOfResidence;
    private String districtSubdivisionOfResidence;
    private String districtOfResidence;
    private boolean fiscalAddress;
    private PhysicalAddressValidationBean validationBean;

    private Country countryOfResidence;

    public PhysicalAddressBean(Party party) {
        super(party);
    }

    public PhysicalAddressBean(PhysicalAddress physicalAddress) {
        super(physicalAddress);
        setValue(physicalAddress.getPresentationValue());
        setAddress(physicalAddress.getAddress());
        setAreaCode(physicalAddress.getAreaCode());
        setAreaOfAreaCode(physicalAddress.getAreaOfAreaCode());
        setArea(physicalAddress.getArea());
        setParishOfResidence(physicalAddress.getParishOfResidence());
        setDistrictSubdivisionOfResidence(physicalAddress.getDistrictSubdivisionOfResidence());
        setDistrictOfResidence(physicalAddress.getDistrictOfResidence());
        setFiscalAddress(physicalAddress.isFiscalAddress());
        setCountryOfResidence(physicalAddress.getCountryOfResidence());
        if (physicalAddress.getPartyContactValidation() != null) {
            final PartyContactValidation partyContactValidation = physicalAddress.getPartyContactValidation();
            final PhysicalAddressValidationBean validationBean =
                    new PhysicalAddressValidationBean((PhysicalAddressValidation) partyContactValidation);
            setValidationBean(validationBean);
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAreaOfAreaCode() {
        return areaOfAreaCode;
    }

    public void setAreaOfAreaCode(String areaOfAreaCode) {
        this.areaOfAreaCode = areaOfAreaCode;
    }

    public String getDistrictOfResidence() {
        return districtOfResidence;
    }

    public void setDistrictOfResidence(String districtOfResidence) {
        this.districtOfResidence = districtOfResidence;
    }

    public String getDistrictSubdivisionOfResidence() {
        return districtSubdivisionOfResidence;
    }

    public void setDistrictSubdivisionOfResidence(String districtSubdivisionOfResidence) {
        this.districtSubdivisionOfResidence = districtSubdivisionOfResidence;
    }

    public String getParishOfResidence() {
        return parishOfResidence;
    }

    public void setParishOfResidence(String parishOfResidence) {
        this.parishOfResidence = parishOfResidence;
    }

    public Country getCountryOfResidence() {
        return this.countryOfResidence;
    }

    public void setCountryOfResidence(Country countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public boolean isFiscalAddress() {
        return fiscalAddress;
    }

    public void setFiscalAddress(boolean fiscalAddress) {
        this.fiscalAddress = fiscalAddress;
    }

    @Override
    public String getContactName() {
        return CONTACT_NAME;
    }

    @Override
    public PartyContact createNewContact() {
        final PhysicalAddress newPhysicalAddress = PhysicalAddress.createPhysicalAddress(getParty(),
                new PhysicalAddressData(getAddress(), getAreaCode(), getAreaOfAreaCode(), getArea(), getParishOfResidence(),
                        getDistrictSubdivisionOfResidence(), getDistrictOfResidence(), getCountryOfResidence()),
                getType(), getDefaultContact());

        newPhysicalAddress.setFiscalAddress(isFiscalAddress());

        return newPhysicalAddress;
    }

    public PhysicalAddressValidationBean getValidationBean() {
        return validationBean;
    }

    public void setValidationBean(PhysicalAddressValidationBean validationBean) {
        this.validationBean = validationBean;
    }

    @Override
    public boolean isValueChanged() {
        final PhysicalAddress address = (PhysicalAddress) getContact();

        return !getAddress().equals(address.getAddress()) || !getArea().equals(address.getArea())
                || !getAreaCode().equals(address.getAreaCode()) || !getAreaOfAreaCode().equals(address.getAreaOfAreaCode())
                || !getParishOfResidence().equals(address.getParishOfResidence())
                || !getDistrictSubdivisionOfResidence().equals(address.getDistrictSubdivisionOfResidence())
                || !getDistrictOfResidence().equals(address.getDistrictOfResidence())
                || !getCountryOfResidence().equals(address.getCountryOfResidence());
    }

    @Override
    public String getPresentationValue() {
        return getAddress();
    }

}
