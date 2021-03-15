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
package org.fenixedu.academic.domain;

import java.text.Collator;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.util.Bundle;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.bennu.core.i18n.BundleUtil;
import org.fenixedu.commons.i18n.LocalizedString;

public class Country extends Country_Base {

    static final public String PORTUGAL = "PORTUGAL";

    static final public String NATIONALITY_PORTUGUESE = "PORTUGUESA";

    static final public String DEFAULT_COUNTRY_NATIONALITY = NATIONALITY_PORTUGUESE;

    public static Comparator<Country> COMPARATOR_BY_NAME = new Comparator<Country>() {
        @Override
        public int compare(Country leftCountry, Country rightCountry) {
            int comparationResult = Collator.getInstance().compare(leftCountry.getLocalizedName().getContent(),
                    rightCountry.getLocalizedName().getContent());
            return (comparationResult == 0) ? leftCountry.getExternalId()
                    .compareTo(rightCountry.getExternalId()) : comparationResult;
        }
    };

    private static Set<Country> CPLP_COUNTRIES;

    private Country() {
        super();
        setRootDomainObject(Bennu.getInstance());
        setDefaultCountry(false);
    }

    public Country(final LocalizedString localizedName, final LocalizedString countryNationality, final String code,
            final String threeLetterCode) {
        this();
        setCode(code);
        setCountryNationality(countryNationality);
        setName(LocaleUtils.getPreferedContent(localizedName));
        setLocalizedName(localizedName);
        setThreeLetterCode(threeLetterCode);
    }

    /**
     * If setting country as default, first sets current default to false
     */
    @Override
    public void setDefaultCountry(Boolean defaultCountry) {
        if (Boolean.TRUE.equals(defaultCountry)) {
            final Country currentDefault = readDefault();
            if (currentDefault != null && currentDefault != this) {
                currentDefault.setDefaultCountry(false);
            }
        }
        super.setDefaultCountry(defaultCountry);
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------

    /**
     * If the person country is undefined it is set to default. In a not
     * distance future this will not be needed since the coutry can never be
     * null.
     * 
     * @return default country
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
                if (country.getCountryNationality().getContent(org.fenixedu.academic.util.LocaleUtils.PT)
                        .equalsIgnoreCase(NATIONALITY_PORTUGUESE)) {
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
        return LocaleUtils.getPreferedContent(getCountryNationality());
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

}
