package net.sourceforge.fenixedu.domain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.sourceforge.fenixedu.domain.accessControl.Group;

import org.apache.ojb.broker.PBFactoryException;
import org.apache.ojb.broker.PersistenceBrokerFactory;
import org.apache.ojb.broker.accesslayer.LookupException;
import org.joda.time.DateTime;

import pt.utl.ist.fenix.tools.file.FileManagerFactory;

public abstract class File extends File_Base {

    public File() {
	super();
	setRootDomainObject(RootDomainObject.getInstance());
	setOjbConcreteClass(this.getClass().getName());
    }

    protected void init(String filename, String displayName, String mimeType, String checksum,
	    String checksumAlgorithm, Integer size, String externalStorageIdentification,
	    Group permittedGroup) {
	setFilename(filename);
	setDisplayName(displayName);
	setMimeType(mimeType);
	setChecksum(checksum);
	setChecksumAlgorithm(checksumAlgorithm);
	setSize(size);
	setExternalStorageIdentification(externalStorageIdentification);
	setPermittedGroup(permittedGroup);
	setUploadTime(new DateTime());
    }

    public boolean isPersonAllowedToAccess(Person person) {
	if (this.getPermittedGroup() == null) {
	    // everyone can access file
	    return true;
	} else {
	    return this.getPermittedGroup().isMember(person);
	}
    }

    /**
         * @return returns a public url that can be used by a client to download
         *         the associated file from the external file storage
         */
    public String getDownloadUrl() {
	// TODO: remove the dependancy between the domain and the dspace
        // infrastructure
	return FileManagerFactory.getFileManager().getDirectDownloadUrlFormat() + "/"
		+ getExternalStorageIdentification() + "/" + getFilename();
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------
    public static File readByExternalStorageIdentification(String externalStorageIdentification) {
	// For performance reasons...
	PreparedStatement stmt = null;
	try {
	    final Connection connection = PersistenceBrokerFactory.defaultPersistenceBroker()
		    .serviceConnectionManager().getConnection();
	    stmt = connection
		    .prepareStatement("SELECT ID_INTERNAL FROM FILE WHERE EXTERNAL_STORAGE_IDENTIFICATION = ?");

	    stmt.setString(1, externalStorageIdentification);
	    final ResultSet resultSet = stmt.executeQuery();
	    if (resultSet.next()) {
		return RootDomainObject.getInstance().readFileByOID(resultSet.getInt(1));
	    }

	    return null;

	} catch (PBFactoryException e) {
	    throw new Error(e);
	} catch (LookupException e) {
	    throw new Error(e);
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

    @Override
    public void setPermittedGroup(Group group) {
	super.setPermittedGroup(group);

	if (group == null) {
	    setPermittedGroupExpression(null);
	} else {
	    setPermittedGroupExpression(group.getExpression());
	}
    }
}
