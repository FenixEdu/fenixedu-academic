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

import java.util.HashSet;
import java.util.Set;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.organizationalStructure.Unit;
import org.fenixedu.bennu.core.domain.Bennu;

public class UnitFileTag extends UnitFileTag_Base {

    public UnitFileTag(Unit unit, String name) {
        setRootDomainObject(Bennu.getInstance());
        setUnit(unit);
        setName(name);
    }

    public Integer getFileTagCount(Person person) {
        int count = 0;
        for (UnitFile file : getTaggedFilesSet()) {
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
        for (UnitFile file : getTaggedFilesSet()) {
            tags.addAll(file.getUnitFileTagsSet());
        }
        tags.remove(this);
        return tags;
    }

}
