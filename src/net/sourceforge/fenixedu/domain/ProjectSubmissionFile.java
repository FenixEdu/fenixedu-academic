package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.Group;

public class ProjectSubmissionFile extends ProjectSubmissionFile_Base {

    public ProjectSubmissionFile() {
        super();
        setRootDomainObject(RootDomainObject.getInstance());
        setOjbConcreteClass(this.getClass().getName());
    }

    public ProjectSubmissionFile(String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String dspaceBitstreamIdentification,
            Group permittedGroup) {
        this();
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                dspaceBitstreamIdentification, permittedGroup);

    }
    
    public void delete(){
        removeProjectSubmission();
        removeRootDomainObject();
        super.deleteDomainObject();
    }
            
}
