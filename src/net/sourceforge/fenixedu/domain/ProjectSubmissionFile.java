package net.sourceforge.fenixedu.domain;

import net.sourceforge.fenixedu.domain.accessControl.Group;

public class ProjectSubmissionFile extends ProjectSubmissionFile_Base {

    public ProjectSubmissionFile() {
        super();
    }

    public ProjectSubmissionFile(String filename, String displayName, String mimeType, String checksum,
            String checksumAlgorithm, Integer size, String externalStorageIdentification,
            Group permittedGroup) {
        this();
        init(filename, displayName, mimeType, checksum, checksumAlgorithm, size,
                externalStorageIdentification, permittedGroup);

    }
    
    public void delete(){
        removeProjectSubmission();
        removeRootDomainObject();
        super.deleteDomainObject();
    }
            
}
