package net.sourceforge.fenixedu.domain;

import java.util.regex.Pattern;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.util.FileUtils;

@Deprecated
public abstract class File extends File_Base {
    protected void init(String filename, String displayName, byte[] content, Group group) {
        init(FileUtils.getFilenameOnly(displayName), FileUtils.getFilenameOnly(filename), content);
        setPermittedGroup(group);
        setChecksum(DigestUtils.shaHex(content));
        setChecksumAlgorithm("SHA");
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
        return FenixConfigurationManager.getConfiguration().getFileDownloadUrlLocalContent();
    }

    protected void disconnect() {
    }

    @Override
    public void delete() {
        disconnect();
        super.delete();
    }

    public boolean isPersonAllowedToAccess(Person person) {
        final Group group = this.getPermittedGroup();
        return group == null || group.isMember(person);
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
    public boolean hasExternalStorageIdentification() {
        return getExternalStorageIdentification() != null;
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
