package net.sourceforge.fenixedu.dataTransferObject.contacts;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.contacts.PartyContact;
import net.sourceforge.fenixedu.domain.contacts.PartyContactValidation;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressData;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddressValidation;
import net.sourceforge.fenixedu.domain.organizationalStructure.Party;
import pt.ist.fenixWebFramework.security.accessControl.Checked;
import pt.ist.fenixWebFramework.services.Service;

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
	setCountryOfResidence(physicalAddress.getCountryOfResidence());
	if (physicalAddress.hasPartyContactValidation()) {
	    final PartyContactValidation partyContactValidation = physicalAddress.getPartyContactValidation();
	    final PhysicalAddressValidationBean validationBean = new PhysicalAddressValidationBean(
		    (PhysicalAddressValidation) partyContactValidation);
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

    @Override
    public String getContactName() {
	return CONTACT_NAME;
    }

    @Override
    @Service
    public Boolean edit() {
	final boolean isValueChanged = super.edit();
	if (isValueChanged) {
	    ((PhysicalAddress) getContact()).edit(new PhysicalAddressData(getAddress(), getAreaCode(), getAreaOfAreaCode(),
		    getArea(), getParishOfResidence(), getDistrictSubdivisionOfResidence(), getDistrictOfResidence(),
		    getCountryOfResidence()));
	}
	return isValueChanged;
    }

    @Override
    @Checked("RolePredicates.PARTY_CONTACT_BEAN_PREDICATE")
    public PartyContact createNewContact() {
	return PhysicalAddress.createPhysicalAddress(getParty(), new PhysicalAddressData(getAddress(), getAreaCode(),
		getAreaOfAreaCode(), getArea(), getParishOfResidence(), getDistrictSubdivisionOfResidence(),
		getDistrictOfResidence(), getCountryOfResidence()), getType(), getDefaultContact());
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
}
