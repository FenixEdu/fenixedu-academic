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
package net.sourceforge.fenixedu.domain.phd.migration.common;

import java.util.HashMap;

import net.sourceforge.fenixedu.domain.Country;
import net.sourceforge.fenixedu.domain.phd.migration.common.exceptions.InvalidCountryCodeException;

import org.apache.commons.lang.StringUtils;

public class NationalityTranslator {
    private static HashMap<Integer, String> translationMap = new HashMap<Integer, String>();

    static {
        translationMap.put(1, "PT");
        translationMap.put(2, "PT");
        translationMap.put(3, "PT");
        translationMap.put(4, "PT");
        translationMap.put(5, "PT");
        translationMap.put(6, "PT");
        translationMap.put(10, "AO");
        translationMap.put(11, "BR");
        translationMap.put(12, "CV");
        translationMap.put(13, "GW");
        translationMap.put(14, "MZ");
        translationMap.put(15, "ST");
        translationMap.put(20, "BE");
        translationMap.put(21, "DK");
        translationMap.put(22, "ES");
        translationMap.put(23, "FR");
        translationMap.put(24, "NL");
        translationMap.put(25, "IE");
        translationMap.put(26, "IT");
        translationMap.put(27, "LU");
        translationMap.put(28, "DE");
        translationMap.put(29, "UK");
        translationMap.put(32, "FI");
        translationMap.put(33, "ZA");
        translationMap.put(34, "AR");
        translationMap.put(35, "CA");
        translationMap.put(36, "CL");
        translationMap.put(37, "EC");
        translationMap.put(38, "US");
        translationMap.put(39, "IR");
        translationMap.put(40, "MA");
        translationMap.put(41, "VE");
        translationMap.put(42, "AU");
        translationMap.put(43, "PK");
        translationMap.put(50, "CU");
        translationMap.put(60, "RU");
        translationMap.put(61, "RU");
        translationMap.put(62, "UA");
        translationMap.put(70, "CN");
        translationMap.put(71, "JP");
        translationMap.put(63, "CZ");
        translationMap.put(64, "BG");
        translationMap.put(65, "YU");
        translationMap.put(66, "RO");
        translationMap.put(67, "HR");
        translationMap.put(68, "AM");
        translationMap.put(51, "MX");
        translationMap.put(44, "IN");
        translationMap.put(74, "EG");
        translationMap.put(52, "PE");
        translationMap.put(72, "CS");
        translationMap.put(73, "BY");
        translationMap.put(75, "PL");
        translationMap.put(76, "SE");
        translationMap.put(77, "CO");
        translationMap.put(77, "CO");
    }

    public static Country translate(String value) {
        if (StringUtils.isEmpty(value)) {
            return null;
        }

        String code = translationMap.get(Integer.valueOf(value));

        if (StringUtils.isEmpty(code)) {
            throw new InvalidCountryCodeException();
        }

        return Country.readByTwoLetterCode(code);
    }
}
