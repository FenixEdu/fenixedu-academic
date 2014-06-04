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
package net.sourceforge.fenixedu.domain;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import net.sourceforge.fenixedu.util.Bundle;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class Country extends Country_Base {

    static final public String PORTUGAL = "PORTUGAL";

    static final public String NATIONALITY_PORTUGUESE = "PORTUGUESA";

    static final public String DEFAULT_COUNTRY_NATIONALITY = NATIONALITY_PORTUGUESE;

    public static Comparator<Country> COMPARATOR_BY_NAME = new Comparator<Country>() {
        @Override
        public int compare(Country leftCountry, Country rightCountry) {
            int comparationResult = Collator.getInstance().compare(leftCountry.getName(), rightCountry.getName());
            return (comparationResult == 0) ? leftCountry.getExternalId().compareTo(rightCountry.getExternalId()) : comparationResult;
        }
    };

    private static Set<Country> CPLP_COUNTRIES;

    private Country() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setDefaultCountry(false);
    }

    public Country(final MultiLanguageString localizedName, final MultiLanguageString countryNationality, final String code,
            final String threeLetterCode) {
        this();
        setCode(code);
        setCountryNationality(countryNationality);
        setName(localizedName.getPreferedContent());
        setLocalizedName(localizedName);
        setThreeLetterCode(threeLetterCode);
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    /**
     * If the person country is undefined it is set to default. In a not
     * distance future this will not be needed since the coutry can never be
     * null.
     */
    public static Country readDefault() {
        for (final Country country : Bennu.getInstance().getCountrysSet()) {
            if (country.isDefaultCountry()) {
                return country;
            }
        }

        return null;
    }

    public static Country readCountryByNationality(final String nationality) {
        for (final Country country : Bennu.getInstance().getCountrysSet()) {
            if (country.getNationality().equals(nationality)) {
                return country;
            }
        }
        return null;
    }

    // FIXME: This method is wrong and should not exist
    // exists only because PORTUGAL is repeated
    // country object should be split in 2 objects: Country and District
    // where Country has many Districts
    public static Set<Country> readDistinctCountries() {
        final Set<Country> result = new HashSet<Country>();
        for (final Country country : Bennu.getInstance().getCountrysSet()) {
            if (!country.getName().equalsIgnoreCase(PORTUGAL)) {
                result.add(country);
            } else {
                if (country.getCountryNationality().getContent(MultiLanguageString.pt).equalsIgnoreCase(NATIONALITY_PORTUGUESE)) {
                    result.add(country);
                }
            }
        }

        return result;
    }

    /**
     * This method is (yet another) hack in Country due to strange values for
     * the Portuguese nationality.
     * 
     */
    @Deprecated
    public String getFilteredNationality(final Locale locale) {
        final String nationality = getCountryNationality().getContent(locale);
        if (this != readDefault()) {
            return nationality;
        }

        final String specialCase = BundleUtil.getString(Bundle.APPLICATION, "label.person.portugueseNationality").toUpperCase();
        return nationality.trim().contains(specialCase) ? specialCase : nationality;
    }

    public boolean isDefaultCountry() {
        return getDefaultCountry();
    }

    static public Country readByTwoLetterCode(String code) {

        if (StringUtils.isEmpty(code)) {
            return null;
        }

        // TODO: Hack to remove, when we no longer have 4(!!) Portugal countries
        // with same code (pt)
        final Country defaultCountry = readDefault();
        if (defaultCountry.getCode().equalsIgnoreCase(code)) {
            return defaultCountry;
        }

        for (final Country country : Bennu.getInstance().getCountrysSet()) {
            if (country.getCode().equalsIgnoreCase(code)) {
                return country;
            }
        }
        return null;
    }

    static public Country readByThreeLetterCode(String code) {

        if (StringUtils.isEmpty(code)) {
            return null;
        }

        // TODO: Hack to remove, when we no longer have 4(!!) Portugal countries
        // with same code (pt)
        Country defaultCountry = readDefault();
        if (defaultCountry.getThreeLetterCode().equalsIgnoreCase(code)) {
            return defaultCountry;
        }

        for (final Country country : Bennu.getInstance().getCountrysSet()) {
            if (country.getThreeLetterCode() != null && country.getThreeLetterCode().equalsIgnoreCase(code)) {
                return country;
            }
        }
        return null;
    }

    @Deprecated
    public String getNationality() {
        return getCountryNationality().getPreferedContent();
    }

    @Deprecated
    public void setNationality(final String nationality) {
        setCountryNationality(getCountryNationality().with(Locale.getDefault(), nationality));
    }

    public synchronized static Set<Country> getCPLPCountries() {
        if (CPLP_COUNTRIES == null) {
            CPLP_COUNTRIES = new HashSet<Country>();
            CPLP_COUNTRIES.add(Country.readByTwoLetterCode("PT"));
            CPLP_COUNTRIES.add(Country.readByTwoLetterCode("BR"));
            CPLP_COUNTRIES.add(Country.readByTwoLetterCode("AO"));
            CPLP_COUNTRIES.add(Country.readByTwoLetterCode("CV"));
            CPLP_COUNTRIES.add(Country.readByTwoLetterCode("GW"));
            CPLP_COUNTRIES.add(Country.readByTwoLetterCode("MZ"));
            CPLP_COUNTRIES.add(Country.readByTwoLetterCode("ST"));
            CPLP_COUNTRIES.add(Country.readByTwoLetterCode("TL"));
            CPLP_COUNTRIES.add(Country.readByTwoLetterCode("MO"));
        }
        return CPLP_COUNTRIES;
    }

    public static boolean isCPLPCountry(Country country) {
        return getCPLPCountries().contains(country);
    }

    public void delete() {
        setRootDomainObject(null);
        deleteDomainObject();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.personnelSection.contracts.PersonGrantOwnerEquivalent> getPersonGrantOwnerEquivalences() {
        return getPersonGrantOwnerEquivalencesSet();
    }

    @Deprecated
    public boolean hasAnyPersonGrantOwnerEquivalences() {
        return !getPersonGrantOwnerEquivalencesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyExternalPersonDetails> getExternalCandidacyPersonDetailsCountryOfResidence() {
        return getExternalCandidacyPersonDetailsCountryOfResidenceSet();
    }

    @Deprecated
    public boolean hasAnyExternalCandidacyPersonDetailsCountryOfResidence() {
        return !getExternalCandidacyPersonDetailsCountryOfResidenceSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Job> getJobs() {
        return getJobsSet();
    }

    @Deprecated
    public boolean hasAnyJobs() {
        return !getJobsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacy.StudentCandidacy> getAssociatedResidentPersonsCandidacies() {
        return getAssociatedResidentPersonsCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedResidentPersonsCandidacies() {
        return !getAssociatedResidentPersonsCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PersonalIngressionData> getPersonalIngressionsData() {
        return getPersonalIngressionsDataSet();
    }

    @Deprecated
    public boolean hasAnyPersonalIngressionsData() {
        return !getPersonalIngressionsDataSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacyExternalPersonDetails> getExternalCandidacyPersonDetailsNationality() {
        return getExternalCandidacyPersonDetailsNationalitySet();
    }

    @Deprecated
    public boolean hasAnyExternalCandidacyPersonDetailsNationality() {
        return !getExternalCandidacyPersonDetailsNationalitySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation> getOriginPrecedentDegreeInformations() {
        return getOriginPrecedentDegreeInformationsSet();
    }

    @Deprecated
    public boolean hasAnyOriginPrecedentDegreeInformations() {
        return !getOriginPrecedentDegreeInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.organizationalStructure.Party> getAssociatedParties() {
        return getAssociatedPartiesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedParties() {
        return !getAssociatedPartiesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.IndividualCandidacy> getAssociatedResidentIndividualCandidacies() {
        return getAssociatedResidentIndividualCandidaciesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedResidentIndividualCandidacies() {
        return !getAssociatedResidentIndividualCandidaciesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.candidacyProcess.CandidacyPrecedentDegreeInformation> getCandidacyPrecedentDegreeInformations() {
        return getCandidacyPrecedentDegreeInformationsSet();
    }

    @Deprecated
    public boolean hasAnyCandidacyPrecedentDegreeInformations() {
        return !getCandidacyPrecedentDegreeInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.internship.InternshipCandidacy> getThirdInternshipDestination() {
        return getThirdInternshipDestinationSet();
    }

    @Deprecated
    public boolean hasAnyThirdInternshipDestination() {
        return !getThirdInternshipDestinationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.student.PrecedentDegreeInformation> getPrecedentDegreeInformations() {
        return getPrecedentDegreeInformationsSet();
    }

    @Deprecated
    public boolean hasAnyPrecedentDegreeInformations() {
        return !getPrecedentDegreeInformationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.phd.candidacy.PhdCandidacyRefereeLetter> getRefereeLetters() {
        return getRefereeLettersSet();
    }

    @Deprecated
    public boolean hasAnyRefereeLetters() {
        return !getRefereeLettersSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.internship.InternshipCandidacySession> getInternshipCandidacySession() {
        return getInternshipCandidacySessionSet();
    }

    @Deprecated
    public boolean hasAnyInternshipCandidacySession() {
        return !getInternshipCandidacySessionSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.contacts.PhysicalAddress> getAssociatedPhysicalAddresses() {
        return getAssociatedPhysicalAddressesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedPhysicalAddresses() {
        return !getAssociatedPhysicalAddressesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.internship.InternshipCandidacy> getFirstInternshipDestination() {
        return getFirstInternshipDestinationSet();
    }

    @Deprecated
    public boolean hasAnyFirstInternshipDestination() {
        return !getFirstInternshipDestinationSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Person> getAssociatedBornedPersons() {
        return getAssociatedBornedPersonsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedBornedPersons() {
        return !getAssociatedBornedPersonsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Qualification> getAssociatedQualifications() {
        return getAssociatedQualificationsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedQualifications() {
        return !getAssociatedQualificationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.internship.InternshipCandidacy> getInternshipCandidacy() {
        return getInternshipCandidacySet();
    }

    @Deprecated
    public boolean hasAnyInternshipCandidacy() {
        return !getInternshipCandidacySet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.internship.InternshipCandidacy> getSecondInternshipDestination() {
        return getSecondInternshipDestinationSet();
    }

    @Deprecated
    public boolean hasAnySecondInternshipDestination() {
        return !getSecondInternshipDestinationSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasDefaultCountry() {
        return getDefaultCountry() != null;
    }

    @Deprecated
    public boolean hasCountryNationality() {
        return getCountryNationality() != null;
    }

    @Deprecated
    public boolean hasLocalizedName() {
        return getLocalizedName() != null;
    }

    @Deprecated
    public boolean hasThreeLetterCode() {
        return getThreeLetterCode() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

}
