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

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.domain.Bennu;

public class UnitFileTag extends UnitFileTag_Base {

    public UnitFileTag(Unit unit, String name) {
        setRootDomainObject(Bennu.getInstance());
        setUnit(unit);
        setName(name);
    }

    public Integer getFileTagCount(Person person) {
        int count = 0;
        for (UnitFile file : getTaggedFiles()) {
            if (file.isPersonAllowedToAccess(person)) {
                count++;
            }
        }
        return count;
    }

    public boolean isTagAccessibleToUser(Person person) {
        return getFileTagCount(person) > 0;
    }

    public void delete() {
        if (getTaggedFilesSet().size() > 0) {
            throw new DomainException("error.cannot.delete.tag.with.files");
        }
        setUnit(null);
        setRootDomainObject(null);
        super.deleteDomainObject();
    }

    public Set<UnitFileTag> getNeighboursTags() {
        Set<UnitFileTag> tags = new HashSet<UnitFileTag>();
        for (UnitFile file : getTaggedFiles()) {
            tags.addAll(file.getUnitFileTags());
        }
        tags.remove(this);
        return tags;
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.UnitFile> getTaggedFiles() {
        return getTaggedFilesSet();
    }

    @Deprecated
    public boolean hasAnyTaggedFiles() {
        return !getTaggedFilesSet().isEmpty();
    }

    @Deprecated
    public boolean hasName() {
        return getName() != null;
    }

    @Deprecated
    public boolean hasBennu() {
        return getRootDomainObject() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
