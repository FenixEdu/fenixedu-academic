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
package net.sourceforge.fenixedu.domain.raides;

import org.fenixedu.bennu.core.domain.Bennu;

public class DegreeClassification extends DegreeClassification_Base {

    public DegreeClassification(String code, String description1, String description2, String abbreviation, Integer order) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCode(code);
        setDescription1(description1);
        setDescription2(description2);
        setAbbreviation(abbreviation);
        setClassificationOrder(order);
    }

    public DegreeClassification(String code, String description1, String description2, String abbreviation) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCode(code);
        setDescription1(description1);
        setDescription2(description2);
        setAbbreviation(abbreviation);
    }

    public void delete() {
        for (DegreeDesignation designation : getDegreeDesignations()) {
            removeDegreeDesignations(designation);
        }
        setRootDomainObject(null);
        deleteDomainObject();
    }

    public static DegreeClassification readByCode(String code) {
        for (DegreeClassification degreeClassification : Bennu.getInstance().getDegreeClassificationsSet()) {
            if (degreeClassification.getCode().equals(code)) {
                return degreeClassification;
            }
        }
        return null;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.raides.DegreeDesignation> getDegreeDesignations() {
        return getDegreeDesignationsSet();
    }

    @Deprecated
    public boolean hasAnyDegreeDesignations() {
        return !getDegreeDesignationsSet().isEmpty();
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasClassificationOrder() {
        return getClassificationOrder() != null;
    }

    @Deprecated
    public boolean hasDescription1() {
        return getDescription1() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

    @Deprecated
    public boolean hasAbbreviation() {
        return getAbbreviation() != null;
    }

    @Deprecated
    public boolean hasDescription2() {
        return getDescription2() != null;
    }

}
