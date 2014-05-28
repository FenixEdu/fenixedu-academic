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

import java.util.Collection;

import net.sourceforge.fenixedu.domain.accessControl.MembersLinkGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;
import net.sourceforge.fenixedu.injectionCode.AccessControl;

import org.fenixedu.bennu.core.groups.Group;

import pt.ist.fenixframework.dml.runtime.RelationAdapter;

public class UnitFile extends UnitFile_Base {

    static {
        getRelationFileTagging().addListener(new RelationAdapter<UnitFileTag, UnitFile>() {

            @Override
            public void afterRemove(UnitFileTag tag, UnitFile file) {
                super.afterRemove(tag, file);
                if (file != null && tag != null && tag.getTaggedFilesSet().size() == 0) {
                    tag.delete();
                }
            }

        });
    }

    public UnitFile(Unit unit, Person person, String description, String tags, String filename, String displayName,
            byte[] content, Group group) {
        super();
        setUnit(unit);
        setUploader(person);
        setDescription(description);
        if (tags != null && tags.length() > 0) {
            setUnitFileTags(tags);
        }
        init(filename, displayName, content, group);
    }

    public void setUnitFileTags(String tag) {
        getUnitFileTags().clear();
        String[] tagNames = tag.split("\\p{Space}+");
        for (String tagName : tagNames) {
            UnitFileTag unitFileTag = getUnit().getUnitFileTag(tagName.trim());
            addUnitFileTags((unitFileTag != null) ? unitFileTag : new UnitFileTag(getUnit(), tagName.trim()));
        }
    }

    @Override
    public void delete() {
        if (isEditableByCurrentUser()) {
            setUnit(null);
            for (; !getUnitFileTags().isEmpty(); getUnitFileTags().iterator().next().removeTaggedFiles(this)) {
                ;
            }
            setUploader(null);
            super.delete();
        } else {
            throw new DomainException("error.cannot.delete.file");
        }
    }

    public boolean isEditableByUser(Person person) {
        return getUploader().equals(person);
    }

    public boolean isEditableByCurrentUser() {
        return isEditableByUser(AccessControl.getPerson());
    }

    public void updatePermissions(MembersLinkGroup group) {
        setPermittedGroup(getPermittedGroup().minus(group));
    }

    public boolean hasUnitFileTags(Collection<UnitFileTag> tags) {
        return getUnitFileTags().containsAll(tags);
    }

    @Deprecated
    public java.util.Set<net.sourceforge.fenixedu.domain.UnitFileTag> getUnitFileTags() {
        return getUnitFileTagsSet();
    }

    @Deprecated
    public boolean hasAnyUnitFileTags() {
        return !getUnitFileTagsSet().isEmpty();
    }

    @Deprecated
    public boolean hasDescription() {
        return getDescription() != null;
    }

    @Deprecated
    public boolean hasUploader() {
        return getUploader() != null;
    }

    @Deprecated
    public boolean hasUnit() {
        return getUnit() != null;
    }

}
