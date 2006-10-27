package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.Group;

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
     * @return returns a public url that can be used by a client to download the
     *         associated file from the external file storage
     */
    public String getDownloadUrl() {
        // TODO: remove the dependancy between the domain and the dspace infrastructure
        return FileManagerFactory.getFileManager().getDirectDownloadUrlFormat() + "/"
                + getExternalStorageIdentification() + "/" + getFilename();
    }

    // -------------------------------------------------------------
    // read static methods
    // -------------------------------------------------------------
    public static File readByExternalStorageIdentification(String externalStorageIdentification) {
        for (File file : RootDomainObject.getInstance().getFiles()) {
            if (file.getExternalStorageIdentification().equals(externalStorageIdentification)) {
                return file;
            }
        }

        return null;
    }

}
