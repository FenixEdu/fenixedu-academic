package net.sourceforge.fenixedu.domain.candidacy;

import pt.utl.ist.fenix.tools.file.FileDescriptor;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class CandidacyDocumentFile extends CandidacyDocumentFile_Base {

    public CandidacyDocumentFile() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(this.getClass().getName());
    }

    public CandidacyDocumentFile(String filename, FileDescriptor fileDescriptor, Group permittedGroup) {
        this();
        init(filename, filename, fileDescriptor.getMimeType(), fileDescriptor.getChecksum(),
                fileDescriptor.getChecksumAlgorithm(), fileDescriptor.getSize(), fileDescriptor
                        .getUniqueId(), permittedGroup);
    }

    public void delete() {
        removeCandidacyDocument();
        removeRootDomainObject();
        super.deleteDomainObject();
    }

}
