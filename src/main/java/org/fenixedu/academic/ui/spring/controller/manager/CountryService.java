/**
 * Copyright © 2017 Instituto Superior Técnico
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

package org.fenixedu.academic.ui.spring.controller.manager;

import org.fenixedu.academic.domain.Country;
import org.fenixedu.commons.i18n.LocalizedString;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.stereotype.Service;
import pt.ist.fenixframework.Atomic;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {

    public List<Country> getAllCountries() {
        return Bennu.getInstance().getCountrysSet().stream().sorted(Country.COMPARATOR_BY_NAME).collect(Collectors.toList());
    }

    @Atomic public void editCountry(Country country, LocalizedString localizedName, LocalizedString countryNationality) {
        country.setCountryNationality(countryNationality);
        country.setLocalizedName(localizedName);
    }
}
