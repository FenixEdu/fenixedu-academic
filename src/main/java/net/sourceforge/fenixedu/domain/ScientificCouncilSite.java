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

import net.sourceforge.fenixedu.domain.accessControl.RoleGroup;
import net.sourceforge.fenixedu.domain.organizationalStructure.ScientificCouncilUnit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UserGroup;

import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class ScientificCouncilSite extends ScientificCouncilSite_Base {

    public ScientificCouncilSite(ScientificCouncilUnit scientificCouncil) {
        super();

        setUnit(scientificCouncil);
    }

    @Override
    public Group getOwner() {
        return RoleGroup.get(RoleType.SCIENTIFIC_COUNCIL).or(UserGroup.of(Person.convertToUsers(getManagers())));
    }

    /**
     * This method searchs for the first instance of a ScientificCouncilSite.
     * 
     * @return the site associated with the Scientific Council or <code>null</code> if there is no such site
     */
    public static ScientificCouncilSite getSite() {
        final ScientificCouncilUnit scientificCouncilUnit = ScientificCouncilUnit.getScientificCouncilUnit();
        return scientificCouncilUnit == null ? null : (ScientificCouncilSite) scientificCouncilUnit.getSite();
    }

    @Override
    public MultiLanguageString getName() {
        return new MultiLanguageString("");
    }
}
