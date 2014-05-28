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
import net.sourceforge.fenixedu.domain.cms.CmsContent;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.domain.person.RoleType;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.Atomic;
import pt.utl.ist.fenix.tools.util.i18n.MultiLanguageString;

public class DegreeSite extends DegreeSite_Base {

    public DegreeSite(Degree degree) {
        super();

        setDegree(degree);
    }

    @Override
    public Group getOwner() {
        return CoordinatorGroup.get(getDegree());
    }

    @Override
    public List<Group> getContextualPermissionGroups() {
        List<Group> groups = super.getContextualPermissionGroups();

        groups.add(CoordinatorGroup.get(getDegree()));
        groups.add((Group) RoleGroup.get(RoleType.TEACHER));

        return groups;
    }

    @Override
    public void delete() {
        setDegree(null);
        super.delete();
    }

    @Override
    public MultiLanguageString getName() {
        final Degree degree = getDegree();
        final String name = degree.getSigla();
        return new MultiLanguageString().with(MultiLanguageString.pt, name);
    }

    @Override
    public Unit getUnit() {
        Unit unit = super.getUnit();
        if (unit == null) {
            if (hasDegree()) {
                unit = getDegree().getUnit();
                updateUnit(unit);
            }
        }
        return unit;
    }

    @Atomic
    private void updateUnit(Unit unit) {
        setUnit(unit);
    }

    @Deprecated
    public boolean hasDegree() {
        return getDegree() != null;
    }

    @Override
    public String getReversePath() {
        return super.getReversePath() + "/" + getDegree().getSigla().toLowerCase();
    }

    @Override
    public CmsContent getInitialContent() {
        List<Section> sections = getOrderedSections();
        return sections.isEmpty() ? null : sections.get(0);
    }
}
