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

import java.util.ArrayList;
import java.util.List;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.Bennu;

public class BusinessArea extends BusinessArea_Base {

    public BusinessArea(Integer level, String code, String description) {
        super();

        if (level == null) {
            throw new DomainException("businessArea.creation.level.null");
        }

        if (code == null) {
            throw new DomainException("businessArea.creation.code.null");
        }

        if (description == null) {
            throw new DomainException("businessArea.creation.description.null");
        }

        setRootDomainObject(Bennu.getInstance());
        setLevel(level);
        setCode(code);
        setDescription(description);
    }

    public BusinessArea(Integer level, String code, String description, BusinessArea parentArea) {
        this(level, code, description);
        setParentArea(parentArea);
    }

    public static List<BusinessArea> getParentBusinessAreas() {
        List<BusinessArea> parentAreas = new ArrayList<BusinessArea>();
        for (BusinessArea area : Bennu.getInstance().getBusinessAreasSet()) {
            if (area.getParentArea() == null) {
                parentAreas.add(area);
            }
        }
        return parentAreas;
    }

    public static Object getChildBusinessAreas(BusinessArea parentArea) {
        List<BusinessArea> childAreas = new ArrayList<BusinessArea>();
        for (BusinessArea area : Bennu.getInstance().getBusinessAreasSet()) {
            if (area.getParentArea() != null && area.getParentArea().equals(parentArea)) {
                childAreas.add(area);
            }
        }
        return childAreas;
    }

}
