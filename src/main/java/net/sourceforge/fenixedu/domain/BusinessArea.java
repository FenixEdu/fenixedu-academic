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

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

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
            if (!area.hasParentArea()) {
                parentAreas.add(area);
            }
        }
        return parentAreas;
    }

    public static Object getChildBusinessAreas(BusinessArea parentArea) {
        List<BusinessArea> childAreas = new ArrayList<BusinessArea>();
        for (BusinessArea area : Bennu.getInstance().getBusinessAreasSet()) {
            if (area.hasParentArea() && area.getParentArea().equals(parentArea)) {
                childAreas.add(area);
            }
        }
        return childAreas;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Job> getParentJobs() {
        return getParentJobsSet();
    }

    @Deprecated
    public boolean hasAnyParentJobs() {
        return !getParentJobsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.BusinessArea> getChildAreas() {
        return getChildAreasSet();
    }

    @Deprecated
    public boolean hasAnyChildAreas() {
        return !getChildAreasSet().isEmpty();
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
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasParentArea() {
        return getParentArea() != null;
    }

    @Deprecated
    public boolean hasLevel() {
        return getLevel() != null;
    }

    @Deprecated
    public boolean hasCode() {
        return getCode() != null;
    }

}
