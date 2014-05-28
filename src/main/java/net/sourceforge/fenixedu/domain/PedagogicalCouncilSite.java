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

import java.util.List;

import net.sourceforge.fenixedu.domain.accessControl.CoordinatorGroup;
import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.degree.DegreeType;
import net.sourceforge.fenixedu.domain.organizationalStructure.PedagogicalCouncilUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

/**
 * Specific site instance that is associated with the unit that represents the
 * Pedagogical Council of the institution.
 * <p>
 * There should exist only one instance of this site (matching the single unit that represents the council). Nevertheless that is
 * not verified.
 * 
 * @author cfgi
 */
public class PedagogicalCouncilSite extends PedagogicalCouncilSite_Base {

    public PedagogicalCouncilSite(PedagogicalCouncilUnit pedagogicalCouncil) {
        super();

        setUnit(pedagogicalCouncil);
    }

    @Override
    public Group getOwner() {
        return RoleGroup.get(RoleType.PEDAGOGICAL_COUNCIL).or(RoleGroup.get(RoleType.TUTORSHIP))
                .or(UserGroup.of(Person.convertToUsers(getManagers())));
    }

    @Override
    public List<Group> getContextualPermissionGroups() {
        List<Group> list = super.getContextualPermissionGroups();

        list.add(CoordinatorGroup.get(DegreeType.DEGREE));

        return list;
    }

    /**
     * This method searchs for the first instance of a PedagogicalCouncilSite.
     * 
     * @return the site associated with the Pedagogical Council or <code>null</code> if there is no such site
     */
    public static PedagogicalCouncilSite getSite() {
        final PedagogicalCouncilUnit pedagogicalCouncilUnit = PedagogicalCouncilUnit.getPedagogicalCouncilUnit();
        return pedagogicalCouncilUnit == null ? null : (PedagogicalCouncilSite) pedagogicalCouncilUnit.getSite();
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString("");
    }
}
