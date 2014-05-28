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
package net.sourceforge.fenixedu.domain.candidacy.workflow.form;

import java.util.Collections;
import java.util.List;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.util.workflow.Form;

import org.apache.commons.lang.StringUtils;
import org.joda.time.YearMonthDay;

import pt.utl.ist.fenix.tools.resources.LabelFormatter;

public class FiliationForm extends Form {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private YearMonthDay dateOfBirth;

    private Country nationality;

    private String parishOfBirth;

    private String districtSubdivisionOfBirth;

    private String districtOfBirth;

    private String fatherName;

    private String motherName;

    private Country countryOfBirth;

    public FiliationForm() {
        super();
    }

    public static FiliationForm createFromPerson(final Person person) {
        final Country nationality = person.getCountry() != null ? person.getCountry() : Country.readDefault();
        final Country countryOfBirth = person.hasCountryOfBirth() ? person.getCountryOfBirth() : Country.readDefault();

        return new FiliationForm(person.getDateOfBirthYearMonthDay(), person.getDistrictOfBirth(),
                person.getDistrictSubdivisionOfBirth(), person.getNameOfFather(), person.getNameOfMother(), nationality,
                person.getParishOfBirth(), countryOfBirth);
    }

    private FiliationForm(YearMonthDay dateOfBirth, String districtOfBirth, String districtSubdivisionOfBirth, String fatherName,
            String motherName, Country nationality, String parishOfBirth, Country countryOfBirth) {
        this();
        this.dateOfBirth = dateOfBirth;
        this.districtOfBirth = districtOfBirth;
        this.districtSubdivisionOfBirth = districtSubdivisionOfBirth;
        this.fatherName = fatherName;
        this.motherName = motherName;
        setNationality(nationality);
        this.parishOfBirth = parishOfBirth;
        setCountryOfBirth(countryOfBirth);
    }

    public YearMonthDay getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(YearMonthDay dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getDistrictOfBirth() {
        return districtOfBirth;
    }

    public void setDistrictOfBirth(String districtOfBirth) {
        this.districtOfBirth = districtOfBirth;
    }

    public String getDistrictSubdivisionOfBirth() {
        return districtSubdivisionOfBirth;
    }

    public void setDistrictSubdivisionOfBirth(String districtSubdivisionOfBirth) {
        this.districtSubdivisionOfBirth = districtSubdivisionOfBirth;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public Country getNationality() {
        return this.nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }

    public String getParishOfBirth() {
        return parishOfBirth;
    }

    public void setParishOfBirth(String parishOfBirth) {
        this.parishOfBirth = parishOfBirth;
    }

    public Country getCountryOfBirth() {
        return this.countryOfBirth;
    }

    public void setCountryOfBirth(Country countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    @Override
    public List<LabelFormatter> validate() {
        if (getCountryOfBirth().isDefaultCountry()) {
            if (StringUtils.isEmpty(getDistrictOfBirth()) || StringUtils.isEmpty(getDistrictSubdivisionOfBirth())
                    || StringUtils.isEmpty(getParishOfBirth())) {
                return Collections.singletonList(new LabelFormatter(
                        "error.candidacy.workflow.FiliationForm.zone.information.is.required.for.national.students",
                        "application"));
            }
        }

        return Collections.emptyList();
    }

    @Override
    public String getFormName() {
        return "label.candidacy.workflow.filiationForm";
    }
}