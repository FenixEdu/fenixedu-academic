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
package org.fenixedu.academic.domain.candidacy.workflow.form;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.PostalCodeValidator;
import org.fenixedu.academic.domain.Country;
import org.fenixedu.academic.domain.District;
import org.fenixedu.academic.domain.DistrictSubdivision;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.contacts.PhysicalAddress;
import org.fenixedu.academic.domain.util.workflow.Form;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LabelFormatter;

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

    private District districtOfResidence;

    private DistrictSubdivision districtSubdivisionOfResidence;

    private Boolean dislocatedFromPermanentResidence;

    private District schoolTimeDistrictOfResidence;

    private DistrictSubdivision schoolTimeDistrictSubdivisionOfResidence;

    private String schoolTimeAddress;

    private String schoolTimeAreaCode;

    private String schoolTimeAreaOfAreaCode;

    private String schoolTimeArea;

    private String schoolTimeParishOfResidence;

    private Country countryOfResidence;

    public ResidenceInformationForm() {
        super();
    }

    private ResidenceInformationForm(final String address, final String areaCode, final String areaOfAreaCode, final String area,
            final String parishOfResidence, final District districtOfResidence,
            final DistrictSubdivision districtSubdivisionOfResidence, final Country countryOfResidence) {

        setAddress(address);
        setAreaCode(areaCode);
        setAreaOfAreaCode(areaOfAreaCode);
        setArea(area);
        setParishOfResidence(parishOfResidence);
        setDistrictOfResidence(districtOfResidence);
        setDistrictSubdivisionOfResidence(districtSubdivisionOfResidence);
        setCountryOfResidence(countryOfResidence);
        setDislocatedFromPermanentResidence(Boolean.FALSE);
    }

    public static ResidenceInformationForm createFromPerson(final Person person) {
        if (person.hasDefaultPhysicalAddress()) {
            final PhysicalAddress physicalAddress = person.getDefaultPhysicalAddress();
            final Country country = getCountryOfResidenceFromPhysicalAddress(physicalAddress);
            final District districtOfResidence =
                    physicalAddress.getDistrictOfResidence() != null ? District.readByName(physicalAddress
                            .getDistrictOfResidence()) : null;

            final DistrictSubdivision districtSubdivisionOfResidence;
            if (districtOfResidence != null) {
                districtSubdivisionOfResidence =
                        physicalAddress.getDistrictSubdivisionOfResidence() != null ? districtOfResidence
                                .getDistrictSubdivisionByName(physicalAddress.getDistrictSubdivisionOfResidence()) : null;
            } else {
                districtSubdivisionOfResidence = null;
            }

            return new ResidenceInformationForm(physicalAddress.getAddress(), physicalAddress.getAreaCode(),
                    physicalAddress.getAreaOfAreaCode(), physicalAddress.getArea(), physicalAddress.getParishOfResidence(),
                    districtOfResidence, districtSubdivisionOfResidence, country);
        } else {
            final ResidenceInformationForm residenceInformationForm = new ResidenceInformationForm();
            residenceInformationForm.setCountryOfResidence(Country.readDefault());
            return residenceInformationForm;
        }
    }

    private static Country getCountryOfResidenceFromPhysicalAddress(final PhysicalAddress physicalAddress) {
        return physicalAddress.getCountryOfResidence() != null ? physicalAddress.getCountryOfResidence() : Country.readDefault();
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

    public District getDistrictOfResidence() {
        return this.districtOfResidence;
    }

    public void setDistrictOfResidence(District district) {
        this.districtOfResidence = district;
    }

    public DistrictSubdivision getDistrictSubdivisionOfResidence() {
        return this.districtSubdivisionOfResidence;
    }

    public void setDistrictSubdivisionOfResidence(DistrictSubdivision districtSubdivision) {
        this.districtSubdivisionOfResidence = districtSubdivision;
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

    public Boolean getDislocatedFromPermanentResidence() {
        return dislocatedFromPermanentResidence;
    }

    public void setDislocatedFromPermanentResidence(Boolean dislocatedFromPermanentResidence) {
        this.dislocatedFromPermanentResidence = dislocatedFromPermanentResidence;
    }

    public District getSchoolTimeDistrictOfResidence() {
        return this.schoolTimeDistrictOfResidence;
    }

    public void setSchoolTimeDistrictOfResidence(District district) {
        this.schoolTimeDistrictOfResidence = district;
    }

    public DistrictSubdivision getSchoolTimeDistrictSubdivisionOfResidence() {
        return this.schoolTimeDistrictSubdivisionOfResidence;
    }

    public void setSchoolTimeDistrictSubdivisionOfResidence(DistrictSubdivision districtSubdivision) {
        this.schoolTimeDistrictSubdivisionOfResidence = districtSubdivision;
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
        checkAreaCode(result);

        return result;
    }

    private void checkAddressInformationForForeignStudents(final List<LabelFormatter> result) {
        if (!getCountryOfResidence().isDefaultCountry() && !this.dislocatedFromPermanentResidence) {
            result.add(new LabelFormatter()
                    .appendLabel(
                            "error.candidacy.workflow.ResidenceInformationForm.non.nacional.students.should.select.dislocated.option.and.fill.address",
                            Bundle.CANDIDATE));
        }
    }

    private void checkAddressInformationForNationalStudents(final List<LabelFormatter> result) {
        if (getCountryOfResidence().isDefaultCountry() && !isResidenceInformationFilled()) {
            result.add(new LabelFormatter()
                    .appendLabel(
                            "error.candidacy.workflow.ResidenceInformationForm.address.national.students.should.supply.complete.address.information",
                            Bundle.CANDIDATE));
        }
    }

    private boolean isResidenceInformationFilled() {
        return !(getDistrictOfResidence() == null || getDistrictSubdivisionOfResidence() == null
                || StringUtils.isEmpty(this.parishOfResidence) || StringUtils.isEmpty(this.address)
                || StringUtils.isEmpty(this.areaCode) || StringUtils.isEmpty(this.areaOfAreaCode) || StringUtils
                    .isEmpty(this.area));
    }

    private void checkAddressInformationForDislocatedStudents(final List<LabelFormatter> result) {
        if (isAnySchoolTimeAddressInformationFilled() && !this.dislocatedFromPermanentResidence) {
            result.add(new LabelFormatter()
                    .appendLabel(
                            "error.candidacy.workflow.ResidenceInformationForm.only.dislocated.students.should.fill.school.time.address.information",
                            Bundle.CANDIDATE));
        }

        if (this.dislocatedFromPermanentResidence) {

            if (!isSchoolTimeRequiredInformationAddressFilled()) {
                result.add(new LabelFormatter()
                        .appendLabel(
                                "error.candidacy.workflow.ResidenceInformationForm.address.information.is.required.for.dislocated.students",
                                Bundle.CANDIDATE));
            } else {
                if (isAnyFilled(this.schoolTimeAddress, this.schoolTimeAreaCode, this.schoolTimeAreaOfAreaCode,
                        this.schoolTimeArea, this.schoolTimeParishOfResidence)
                        && isAnyEmpty(this.schoolTimeAddress, this.schoolTimeAreaCode, this.schoolTimeAreaOfAreaCode,
                                this.schoolTimeArea, this.schoolTimeParishOfResidence)) {

                    result.add(new LabelFormatter()
                            .appendLabel(
                                    "error.candidacy.workflow.ResidenceInformationForm.school.time.address.must.be.filled.completly.otherwise.fill.minimun.required",
                                    Bundle.CANDIDATE));
                }
            }

        }
    }

    private void checkAreaCode(final List<LabelFormatter> result) {
        if (getCountryOfResidence() != null) {
            if (getAreaCode() == null || !PostalCodeValidator.isValidAreaCode(getCountryOfResidence().getCode(), getAreaCode())) {
                result.add(new LabelFormatter()
                        .appendLabel(Bundle.CANDIDATE, "label.address.invalid.postCode.for.country",
                                getAreaCode(), getCountryOfResidence().getCode(),
                                PostalCodeValidator.formatFor(getCountryOfResidence().getCode()),
                                PostalCodeValidator.examplePostCodeFor(getCountryOfResidence().getCode())));
            }
        }
    }

    public boolean isAnySchoolTimeAddressInformationFilled() {
        return getSchoolTimeDistrictOfResidence() != null
                || getSchoolTimeDistrictSubdivisionOfResidence() != null
                || isAnyFilled(this.schoolTimeAddress, this.schoolTimeAreaCode, this.schoolTimeAreaOfAreaCode,
                        this.schoolTimeParishOfResidence, this.schoolTimeArea);

    }

    private boolean isAnyFilled(final String... fields) {
        for (final String each : fields) {
            if (!StringUtils.isEmpty(each)) {
                return true;
            }
        }

        return false;
    }

    private boolean isAnyEmpty(final String... fields) {
        for (final String each : fields) {
            if (StringUtils.isEmpty(each)) {
                return true;
            }
        }

        return false;
    }

    private boolean isSchoolTimeRequiredInformationAddressFilled() {
        return !(getSchoolTimeDistrictOfResidence() == null || getSchoolTimeDistrictSubdivisionOfResidence() == null);
        // || StringUtils.isEmpty(this.schoolTimeAddress) ||
        // StringUtils.isEmpty(this.schoolTimeAreaCode)
        // || StringUtils.isEmpty(this.schoolTimeAreaOfAreaCode) ||
        // StringUtils.isEmpty(this.schoolTimeParishOfResidence) || StringUtils
        // .isEmpty(this.schoolTimeArea));
    }

    public boolean isSchoolTimeAddressComplete() {
        return isSchoolTimeRequiredInformationAddressFilled()
                && !isAnyEmpty(this.schoolTimeAddress, this.schoolTimeAreaCode, this.schoolTimeAreaOfAreaCode,
                        this.schoolTimeParishOfResidence, this.schoolTimeArea);
    }

    @Override
    public String getFormName() {
        return "label.candidacy.workflow.residenceInformationForm";
    }

    @Override
    public String getFormDescription() {
        return "label.candidacy.workflow.residenceInformationForm.description";
    }

    @Override
    public String getSchemaName() {
        if (getCountryOfResidence() != null && !getCountryOfResidence().isDefaultCountry()) {
            return getClass().getSimpleName() + ".foreignAddress";
        }
        return getClass().getSimpleName();
    }
}