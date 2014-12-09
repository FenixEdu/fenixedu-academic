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

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;
import org.fenixedu.commons.StringNormalizer;

//TODO: Refactor remaining object to use district subdivision instead of strings
public class DistrictSubdivision extends DistrictSubdivision_Base {

    public static Comparator<DistrictSubdivision> COMPARATOR_BY_NAME = new Comparator<DistrictSubdivision>() {
        @Override
        public int compare(DistrictSubdivision leftDistrictSubdivision, DistrictSubdivision rightDistrictSubdivision) {
            int comparationResult = leftDistrictSubdivision.getName().compareTo(rightDistrictSubdivision.getName());
            return (comparationResult == 0) ? leftDistrictSubdivision.getExternalId().compareTo(
                    rightDistrictSubdivision.getExternalId()) : comparationResult;
        }
    };

    private DistrictSubdivision() {
        super();
        super.setRootDomainObject(Bennu.getInstance());
    }

    public DistrictSubdivision(final String code, final String name, final District district) {
        this();
        init(code, name, district);
    }

    private void init(final String code, final String name, final District district) {
        checkParameters(code, name, district);

        super.setCode(code);
        super.setName(name);
        super.setDistrict(district);
    }

    private void checkParameters(String code, String name, District district) {

        if (code == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.DistrictSubdivision.code.cannot.be.null");
        }

        if (name == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.DistrictSubdivision.name.cannot.be.null");
        }

        if (district == null) {
            throw new DomainException("error.org.fenixedu.academic.domain.DistrictSubdivision.district.cannot.be.null");
        }

    }

    static public Collection<DistrictSubdivision> findByName(String name, int size) {
        String normalizedName = StringNormalizer.normalize(name);
        Collection<DistrictSubdivision> result = new TreeSet<DistrictSubdivision>(COMPARATOR_BY_NAME);

        for (DistrictSubdivision districtSubdivision : Bennu.getInstance().getDistrictSubdivisionsSet()) {
            if (StringNormalizer.normalize(districtSubdivision.getName()).contains(normalizedName)) {
                result.add(districtSubdivision);
                if (result.size() >= size) {
                    break;
                }
            }
        }

        return result;
    }

    static public DistrictSubdivision readByCode(final String code) {
        DistrictSubdivision result = null;

        if (!StringUtils.isEmpty(code)) {
            for (final DistrictSubdivision iter : Bennu.getInstance().getDistrictSubdivisionsSet()) {
                if (iter.getCode().equalsIgnoreCase(code)) {
                    result = iter;
                }
            }
        }

        return result;
    }

}
