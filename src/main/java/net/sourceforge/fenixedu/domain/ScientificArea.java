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

/**
 * 
 * @author Nuno Correia
 * @author Ricardo Rodrigues
 */

public class ScientificArea extends ScientificArea_Base {

    public ScientificArea() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public ScientificArea(final String name) {
        this();
        super.setName(name);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CurricularCourse> getAssociatedCurricularCourses() {
        return getAssociatedCurricularCoursesSet();
    }

    @Deprecated
    public boolean hasAnyAssociatedCurricularCourses() {
        return !getAssociatedCurricularCoursesSet().isEmpty();
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.CreditsInScientificArea> getCreditsInScientificAreas() {
        return getCreditsInScientificAreasSet();
    }

    @Deprecated
    public boolean hasAnyCreditsInScientificAreas() {
        return !getCreditsInScientificAreasSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

}
