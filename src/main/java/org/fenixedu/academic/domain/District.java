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

import java.util.Comparator;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;

//TODO: Refactor remaining object to use district instead of strings
public class District extends District_Base {

    public static Comparator<District> COMPARATOR_BY_NAME = new Comparator<District>() {
        @Override
        public int compare(District leftDistrict, District rightDistrict) {
            int comparationResult = leftDistrict.getName().compareTo(rightDistrict.getName());
            return (comparationResult == 0) ? leftDistrict.getExternalId().compareTo(rightDistrict.getExternalId()) : comparationResult;
        }
    };

    private District() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public District(final String code, final String name) {
        this();
        init(code, name);
    }

    private void init(final String code, final String name) {
        checkParameters(code, name);

        super.setCode(code);
        super.setName(name);
    }

    private void checkParameters(final String code, final String name) {
        if (code == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.District.code.cannot.be.null");
        }

        if (name == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.District.name.cannot.be.null");
        }
    }

    public DistrictSubdivision getDistrictSubdivisionByName(final String name) {

        for (final DistrictSubdivision districtSubdivision : getDistrictSubdivisionsSet()) {
            if (districtSubdivision.getName().equals(name)) {
                return districtSubdivision;
            }
        }

        return null;

    }

    static public District readByCode(final String code) {
        for (final District district : Bennu.getInstance().getDistrictsSet()) {
            if (district.getCode().equals(code)) {
                return district;
            }
        }

        return null;
    }

    static public District readByName(final String name) {
        for (final District district : Bennu.getInstance().getDistrictsSet()) {
            if (district.getName().equals(name)) {
                return district;
            }
        }

        return null;
    }

}
