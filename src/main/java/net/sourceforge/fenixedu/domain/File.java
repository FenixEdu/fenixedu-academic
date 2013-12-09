package net.sourceforge.fenixedu.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import net.sourceforge.fenixedu.domain.accessControl.EveryoneGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.util.ConnectionManager;
import net.sourceforge.fenixedu.util.FenixConfigurationManager;

import org.apache.commons.codec.digest.DigestUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import pt.ist.fenixframework.FenixFramework;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import pt.utl.ist.fenix.tools.util.FileUtils;

@Deprecated
public abstract class File extends File_Base {

    private static final Logger LOGGER = LoggerFactory.getLogger(File.class);

    protected void init(VirtualPath path, String filename, String displayName, Collection<FileSetMetaData> metadata,
            byte[] content, Group group) {
        init(FileUtils.getFilenameOnly(filename), FileUtils.getFilenameOnly(displayName), content);
        setMimeType(MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(filename));
        setSize(new Long(content == null ? 0 : content.length));
        setPermittedGroup(group);
        setCreationDate(new DateTime());
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

//    public void storeToContentManager() {
//        final FileDescriptor fileDescriptor =
//                FileManagerFactory
//                        .getFactoryInstance()
//                        .getFileManager()
//                        .saveFile(getLocalContent().getPath(), getFilename(), isPrivate(), getLocalContent().createMetadata(),
//                                new ByteArrayInputStream(getLocalContent().getContent().getBytes()));
//        setMimeType(fileDescriptor.getMimeType());
//        setChecksum(fileDescriptor.getChecksum());
//        setChecksumAlgorithm(fileDescriptor.getChecksumAlgorithm());
//        setSize(new Long(fileDescriptor.getSize()));
//        setExternalStorageIdentification(fileDescriptor.getUniqueId());
//        getLocalContent().delete();
//    }

    public boolean isPrivate() {
        if (getPermittedGroup() instanceof EveryoneGroup) {
            return false;
        }
        return true;
    }

//    @Override
//    public InputStream getStream() {
//        if (getLocalContent() != null) {
//            return new ByteArrayInputStream(getLocalContent().getContent().getBytes());
//        }
//        return super.getStream();
//    }

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

    public final static File getFileFromURL(String url) {
        Matcher match = Pattern.compile("downloadFile\\/([0-9]+)\\/").matcher(url);
        if (match.matches()) {
            if (match.groupCount() == 2) {
                String oid = match.group(1);
                if (oid != null) {
                    return FenixFramework.getDomainObject(oid);
                }
            }
        }
        return null;
    }

    protected void disconnect() {
    }

    @Override
    public void delete() {
        disconnect();
        deleteDomainObject();
    }

    public boolean isPersonAllowedToAccess(Person person) {
        final Group group = this.getPermittedGroup();
        return group == null || group.isMember(person);
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------
    public static File readByExternalStorageIdentification(String externalStorageIdentification) {
        // For performance reasons...
        PreparedStatement stmt = null;
        try {
            final Connection connection = ConnectionManager.getCurrentSQLConnection();
            stmt = connection.prepareStatement("SELECT OID FROM FILE WHERE EXTERNAL_STORAGE_IDENTIFICATION = ?");

            stmt.setString(1, externalStorageIdentification);
            final ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
                return FenixFramework.getDomainObject(resultSet.getString(1));
            }

            return null;
        } catch (SQLException e) {
            throw new Error(e);
        } finally {
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    throw new Error(e);
                }
            }
        }
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
    public boolean hasMimeType() {
        return getMimeType() != null;
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
