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
package net.sourceforge.fenixedu.presentationTier.Action.research.researchUnit;

import java.io.Serializable;
import java.util.Collection;

import net.sourceforge.fenixedu.domain.UnitFile;
import net.sourceforge.fenixedu.domain.UnitFileTag;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.NobodyGroup;

public class UnitFileBean implements Serializable {

    private final UnitFile file;

    private String name;

    private String description;

    private Group group;

    private String tags;

    protected UnitFileBean() {
        this.file = null;
        group = NobodyGroup.get();
    }

    public UnitFileBean(UnitFile file) {
        this.file = file;
        this.name = file.getDisplayName();
        this.description = file.getDescription();
        setupGroups(file.getPermittedGroup());
        setupTags(file.getUnitFileTags());
    }

    private void setupTags(Collection<UnitFileTag> unitFileTags) {
        String tags = "";
        int i = unitFileTags.size();
        for (UnitFileTag tag : unitFileTags) {
            tags += tag.getName();
            if (--i > 0) {
                tags += " ";
            }
        }
        setTags(tags);
    }

    private void setupGroups(Group permittedGroup) {
        group = group == null ? permittedGroup : group.or(permittedGroup);
    }

    public UnitFile getFile() {
        return file;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Group getGroup() {
        return group;
    }

    public Unit getUnit() {
        return getFile().getUnit();
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

}
