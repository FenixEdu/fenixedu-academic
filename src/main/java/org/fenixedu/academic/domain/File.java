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

import java.util.regex.Pattern;

import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.FileUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.NobodyGroup;

@Deprecated
public abstract class File extends File_Base {
    protected void init(String filename, String displayName, byte[] content, Group group) {
        init(FileUtils.getFilenameOnly(displayName), FileUtils.getFilenameOnly(filename), content);
        setPermittedGroup(group);
    }

    @Override
    public void setDisplayName(String displayName) {
        checkInvalidCharacters(displayName);
        super.setDisplayName(displayName);
    }

    private void checkInvalidCharacters(String displayName) {
        // if the accepted character list is changed, consider changing the 'Content.java' list as well
        String validChars = "_\\- .,:;!()*$&'=@";
        if (!Pattern.matches("[\\p{IsLatin}0-9" + validChars + "]+", displayName)) {
            throw new DomainException("errors.file.displayName.invalid.characters", validChars.replace("\\", ""));
        }
    }

    public Group getPermittedGroup() {
        return getAccessGroup() != null ? getAccessGroup().toGroup() : NobodyGroup.get();
    }

    public void setPermittedGroup(Group group) {
        setAccessGroup(group.toPersistentGroup());
    }

    protected void disconnect() {
    }

    @Override
    public void delete() {
        disconnect();
        setAccessGroup(null);
        super.delete();
    }

    @Override
    public boolean isAccessible(User user) {
        return getPermittedGroup().isMember(user);
    }

}
