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

import java.io.InputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.organizationalStructure.Unit;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;

public class UnitFileUploadBean extends UnitFileBean implements Serializable {

    private Unit unit;

    private String fileName;
    private Long fileSize;

    private Set<Group> permittedGroups;
    private Group permittedGroup;

    transient private InputStream uploadFile;
    private String authorsName;

    public UnitFileUploadBean(Unit unit) {
        this.unit = unit;
        permittedGroups = new HashSet<Group>();
    }

    @Override
    public Unit getUnit() {
        return this.unit;
    }

    public String getAuthorsName() {
        return authorsName;
    }

    public void setAuthorsName(String authorsName) {
        this.authorsName = authorsName;
    }

    public InputStream getUploadFile() {
        return uploadFile;
    }

    public void setUploadFile(InputStream file) {
        this.uploadFile = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Group getPermittedGroup() {
        return (permittedGroup != null) ? permittedGroup : getUnion();
    }

    public void setPermittedGroup(Group permittedGroup) {
        this.permittedGroup = permittedGroup;
    }

    public Group getUnion() {
        return UnionGroup.of(getPermittedGroups());
    }

    public Set<Group> getPermittedGroups() {
        return permittedGroups;
    }

    public void setPermittedGroups(Set<Group> permittedGroups) {
        this.permittedGroups = permittedGroups;
    }
}
