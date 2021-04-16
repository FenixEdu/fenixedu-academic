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
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.util.LocaleUtils;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;

public class Country extends Country_Base {

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
     * @return default country
     */
    public static Country readDefault() {
        return readAll().stream().filter(country -> country.isDefaultCountry()).findAny().orElse(null);
    }

    public static Set<Country> readAll() {
        return Bennu.getInstance().getCountrysSet();
    }

    /**
     * @deprecated use {@link #readAll()}
     */
    @Deprecated
    public static Set<Country> readDistinctCountries() {
        return readAll();
    }

    public boolean isDefaultCountry() {
        return getDefaultCountry();
    }

    static public Country readByTwoLetterCode(String code) {
        return StringUtils.isBlank(code) ? null : readAll().stream().filter(country -> code.equals(country.getCode())).findAny()
                .orElse(null);
    }

    static public Country readByThreeLetterCode(String code) {
        return StringUtils.isBlank(code) ? null : readAll().stream().filter(country -> code.equals(country.getThreeLetterCode()))
                .findAny().orElse(null);
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
