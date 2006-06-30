package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class CandidacyDocumentFile extends CandidacyDocumentFile_Base {

    public CandidacyDocumentFile() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(this.getClass().getName());
    }

    public CandidacyDocumentFile(String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        this();
        init(filename, filename, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);
    }

    public void delete() {
        removeCandidacyDocument();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
