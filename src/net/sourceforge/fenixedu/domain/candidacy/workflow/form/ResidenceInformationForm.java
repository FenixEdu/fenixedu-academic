package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.util.workflow.Form;
import net.sourceforge.fenixedu.util.resources.LabelFormatter;

public class ResidenceInformationForm extends Form {

    private static final Integer DEFAULT_COUNTRY_ID = Integer.valueOf(1);

    private String address;

    private String areaCode; // zip code

    private String areaOfAreaCode; // location of zip code

    private String area; // location

    private String parishOfResidence;

    private String districtSubdivisionOfResidence;

    private String districtOfResidence;

    private DomainReference<Country> countryOfResidence;

    public ResidenceInformationForm() {
	super();
    }

    private ResidenceInformationForm(final String address, final String areaCode,
	    final String areaOfAreaCode, final String area, final String parishOfResidence,
	    final String districtSubdivisionOfResidence, final String districtOfResidence,
	    final Country countryOfResidence) {
	
	setAddress(address);
	setAreaCode(areaCode);
	setAreaOfAreaCode(areaOfAreaCode);
	setArea(area);
	setParishOfResidence(parishOfResidence);
	setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
	setDistrictOfResidence(districtOfResidence);
	setCountryOfResidence(countryOfResidence);
    }

    public static ResidenceInformationForm createFromPerson(final Person person) {
	if (person.hasDefaultPhysicalAddress()) {
	    final PhysicalAddress physicalAddress = person.getDefaultPhysicalAddress();
	    final Country country = getCountryOfResidenceFromPhysicalAddress(physicalAddress);

	    return new ResidenceInformationForm(
		    physicalAddress.getAddress(),
		    physicalAddress.getAreaCode(),
		    physicalAddress.getAreaOfAreaCode(),
		    physicalAddress.getArea(),
		    physicalAddress.getParishOfResidence(),
		    physicalAddress.getDistrictSubdivisionOfResidence(),
		    physicalAddress.getDistrictOfResidence(),
		    country);
	} else {
	    final ResidenceInformationForm residenceInformationForm = new ResidenceInformationForm();
	    residenceInformationForm.setCountryOfResidence(RootDomainObject.getInstance().readCountryByOID(DEFAULT_COUNTRY_ID));
	    return residenceInformationForm;
	}
    }

    private static Country getCountryOfResidenceFromPhysicalAddress(final PhysicalAddress physicalAddress) {
	return physicalAddress.hasCountryOfResidence() ? physicalAddress.getCountryOfResidence()
		: RootDomainObject.getInstance().readCountryByOID(DEFAULT_COUNTRY_ID);
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
	return (this.countryOfResidence != null) ? this.countryOfResidence.getObject() : null;
    }

    public void setCountryOfResidence(Country countryOfResidence) {
	this.countryOfResidence = (countryOfResidence != null) ? new DomainReference<Country>(
		countryOfResidence) : null;
    }

    @Override
    public List<LabelFormatter> validate() {
	return Collections.EMPTY_LIST;
    }

    @Override
    public String getFormName() {
	return "label.candidacy.workflow.residenceInformationForm";
    }
}