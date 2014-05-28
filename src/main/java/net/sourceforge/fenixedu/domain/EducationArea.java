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

import org.fenixedu.bennu.core.domain.Bennu;

public class EducationArea extends EducationArea_Base {

    public EducationArea(String code, String description) {
        super();
        setRootDomainObject(Bennu.getInstance());
        setCode(code);
        setDescription(description);
    }

    public EducationArea(String code, String description, EducationArea parentArea) {
        this(code, description);
        setParentArea(parentArea);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.Formation> getAssociatedFormations() {
        return getAssociatedFormationsSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedFormations() {
        return !getAssociatedFormationsSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.EducationArea> getChildAreas() {
        return getChildAreasSet();
    }

    @Deprecated
    public boolean hasAnyChildAreas() {
        return !getChildAreasSet().isEmpty();
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
    public boolean hasCode() {
        return getCode() != null;
    }

}
