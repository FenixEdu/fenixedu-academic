package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.DomainReference;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.contacts.PhysicalAddress;
import net.sourceforge.fenixedu.domain.util.workflow.Form;

import org.apache.commons.lang.StringUtils;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class ResidenceInformationForm extends Form {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String address;

    private String areaCode; // zip code

    private String areaOfAreaCode; // location of zip code

    private String area; // location

    private String parishOfResidence;

    private String districtSubdivisionOfResidence;

    private String districtOfResidence;

    private Boolean dislocatedFromPermanentResidence;

    private String schoolTimeDistrictOfResidence;

    private String schoolTimeDistrictSubdivisionOfResidence;

    private String schoolTimeAddress;

    private String schoolTimeAreaCode;

    private String schoolTimeAreaOfAreaCode;

    private String schoolTimeArea;

    private String schoolTimeParishOfResidence;

    private DomainReference<Country> countryOfResidence;

    public ResidenceInformationForm() {
	super();
    }

    private ResidenceInformationForm(final String address, final String areaCode, final String areaOfAreaCode, final String area,
	    final String parishOfResidence, final String districtSubdivisionOfResidence, final String districtOfResidence,
	    final Country countryOfResidence) {

	setAddress(address);
	setAreaCode(areaCode);
	setAreaOfAreaCode(areaOfAreaCode);
	setArea(area);
	setParishOfResidence(parishOfResidence);
	setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
	setDistrictOfResidence(districtOfResidence);
	setCountryOfResidence(countryOfResidence);
	setDislocatedFromPermanentResidence(Boolean.FALSE);
    }

    public static ResidenceInformationForm createFromPerson(final Person person) {
	if (person.hasDefaultPhysicalAddress()) {
	    final PhysicalAddress physicalAddress = person.getDefaultPhysicalAddress();
	    final Country country = getCountryOfResidenceFromPhysicalAddress(physicalAddress);

	    return new ResidenceInformationForm(physicalAddress.getAddress(), physicalAddress.getAreaCode(), physicalAddress
		    .getAreaOfAreaCode(), physicalAddress.getArea(), physicalAddress.getParishOfResidence(), physicalAddress
		    .getDistrictSubdivisionOfResidence(), physicalAddress.getDistrictOfResidence(), country);
	} else {
	    final ResidenceInformationForm residenceInformationForm = new ResidenceInformationForm();
	    residenceInformationForm.setCountryOfResidence(Country.readDefault());
	    return residenceInformationForm;
	}
    }

    private static Country getCountryOfResidenceFromPhysicalAddress(final PhysicalAddress physicalAddress) {
	return physicalAddress.hasCountryOfResidence() ? physicalAddress.getCountryOfResidence() : Country.readDefault();
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
	this.countryOfResidence = (countryOfResidence != null) ? new DomainReference<Country>(countryOfResidence) : null;
    }

    public Boolean getDislocatedFromPermanentResidence() {
	return dislocatedFromPermanentResidence;
    }

    public void setDislocatedFromPermanentResidence(Boolean dislocatedFromPermanentResidence) {
	this.dislocatedFromPermanentResidence = dislocatedFromPermanentResidence;
    }

    public String getSchoolTimeDistrictOfResidence() {
	return schoolTimeDistrictOfResidence;
    }

    public void setSchoolTimeDistrictOfResidence(String schoolTimeDistrictOfResidence) {
	this.schoolTimeDistrictOfResidence = schoolTimeDistrictOfResidence;
    }

    public String getSchoolTimeDistrictSubdivisionOfResidence() {
	return schoolTimeDistrictSubdivisionOfResidence;
    }

    public void setSchoolTimeDistrictSubdivisionOfResidence(String schoolTimeDistrictSubdivisionOfResidence) {
	this.schoolTimeDistrictSubdivisionOfResidence = schoolTimeDistrictSubdivisionOfResidence;
    }

    public String getSchoolTimeAddress() {
	return schoolTimeAddress;
    }

    public void setSchoolTimeAddress(String schoolTimeAddress) {
	this.schoolTimeAddress = schoolTimeAddress;
    }

    public String getSchoolTimeAreaCode() {
	return schoolTimeAreaCode;
    }

    public void setSchoolTimeAreaCode(String schoolTimeAreaCode) {
	this.schoolTimeAreaCode = schoolTimeAreaCode;
    }

    public String getSchoolTimeAreaOfAreaCode() {
	return schoolTimeAreaOfAreaCode;
    }

    public void setSchoolTimeAreaOfAreaCode(String schoolTimeAreaOfAreaCode) {
	this.schoolTimeAreaOfAreaCode = schoolTimeAreaOfAreaCode;
    }

    public String getSchoolTimeArea() {
	return schoolTimeArea;
    }

    public void setSchoolTimeArea(String schoolTimeArea) {
	this.schoolTimeArea = schoolTimeArea;
    }

    public String getSchoolTimeParishOfResidence() {
	return schoolTimeParishOfResidence;
    }

    public void setSchoolTimeParishOfResidence(String schoolTimeParishOfResidence) {
	this.schoolTimeParishOfResidence = schoolTimeParishOfResidence;
    }

    @Override
    public List<LabelFormatter> validate() {
	final List<LabelFormatter> result = new ArrayList<LabelFormatter>();

	checkAddressInformationForForeignStudents(result);
	checkAddressInformationForNationalStudents(result);
	checkAddressInformationForDislocatedStudents(result);

	return result;
    }

    private void checkAddressInformationForForeignStudents(final List<LabelFormatter> result) {
	if (!getCountryOfResidence().isDefaultCountry() && !this.dislocatedFromPermanentResidence) {
	    result
		    .add(new LabelFormatter()
			    .appendLabel(
				    "error.candidacy.workflow.ResidenceInformationForm.non.nacional.students.should.select.dislocated.option.and.fill.address",
				    "application"));
	}
    }

    private void checkAddressInformationForNationalStudents(final List<LabelFormatter> result) {
	if (getCountryOfResidence().isDefaultCountry() && !isResidenceInformationFilled()) {
	    result
		    .add(new LabelFormatter()
			    .appendLabel(
				    "error.candidacy.workflow.ResidenceInformationForm.address.national.students.should.supply.complete.address.information",
				    "application"));
	}
    }

    private boolean isResidenceInformationFilled() {
	return !(StringUtils.isEmpty(this.districtOfResidence) || StringUtils.isEmpty(this.districtSubdivisionOfResidence)
		|| StringUtils.isEmpty(this.parishOfResidence) || StringUtils.isEmpty(this.address)
		|| StringUtils.isEmpty(this.areaCode) || StringUtils.isEmpty(this.areaOfAreaCode) || StringUtils
		.isEmpty(this.area));
    }

    private void checkAddressInformationForDislocatedStudents(final List<LabelFormatter> result) {
	if (this.dislocatedFromPermanentResidence && !isSchoolTimeAddressFilled()) {
	    result.add(new LabelFormatter().appendLabel(
		    "error.candidacy.workflow.ResidenceInformationForm.address.information.is.required.for.dislocated.students",
		    "application"));

	}
    }

    private boolean isSchoolTimeAddressFilled() {
	return !(StringUtils.isEmpty(this.schoolTimeDistrictOfResidence)
		|| StringUtils.isEmpty(this.schoolTimeDistrictSubdivisionOfResidence)
		|| StringUtils.isEmpty(this.schoolTimeAddress) || StringUtils.isEmpty(this.schoolTimeAreaCode)
		|| StringUtils.isEmpty(this.schoolTimeAreaOfAreaCode) || StringUtils.isEmpty(this.schoolTimeParishOfResidence) || StringUtils
		.isEmpty(this.schoolTimeArea));
    }

    @Override
    public String getFormName() {
	return "label.candidacy.workflow.residenceInformationForm";
    }

    @Override
    public String getFormDescription() {
	return "label.candidacy.workflow.residenceInformationForm.description";
    }
}