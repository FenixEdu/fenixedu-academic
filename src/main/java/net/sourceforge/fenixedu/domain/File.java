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

import java.util.regex.Pattern;

import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.NobodyGroup;
import org.fenixedu.bennu.core.util.CoreConfiguration;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.FileUtils;

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

    public boolean isPrivate() {
        return getPermittedGroup() == null || !getPermittedGroup().isMember(null);
    }

    @Deprecated
    public byte[] getContents() {
        return getContent();
    }

    /**
     * @return returns a public url that can be used by a client to download the
     *         associated file from the external file storage
     */
    public String getDownloadUrl() {
        return getFileDownloadPrefix() + getExternalId() + "/" + getFilename();
    }

    public final static String getFileDownloadPrefix() {
        return CoreConfiguration.getConfiguration().applicationUrl() + "/downloadFile/";
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

    @Deprecated
    public boolean isPersonAllowedToAccess(Person person) {
        return isAccessible(person != null ? person.getUser() : null);
    }

    @Deprecated
    public DateTime getUploadTime() {
        return super.getCreationDate();
    }

    @Deprecated
    public boolean hasChecksum() {
        return getChecksum() != null;
    }

    @Deprecated
    public boolean hasPermittedGroup() {
        return getPermittedGroup() != null;
    }

    @Deprecated
    public boolean hasUploadTime() {
        return getUploadTime() != null;
    }

    @Deprecated
    public boolean hasChecksumAlgorithm() {
        return getChecksumAlgorithm() != null;
    }

    @Deprecated
    public boolean hasDisplayName() {
        return getDisplayName() != null;
    }

    @Deprecated
    public boolean hasFilename() {
        return getFilename() != null;
    }

    @Deprecated
    public boolean hasSize() {
        return getSize() != null;
    }

}
