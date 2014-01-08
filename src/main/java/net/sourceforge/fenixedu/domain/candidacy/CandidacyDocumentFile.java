package net.sourceforge.fenixedu.domain.candidacy;

import net.sourceforge.fenixedu.domain.accessControl.Group;

public class CandidacyDocumentFile extends CandidacyDocumentFile_Base {

    public CandidacyDocumentFile() {
        super();
    }

    public CandidacyDocumentFile(String filename, String displayName, byte[] content, Group group) {
        this();
        init(filename, displayName, content, group);
    }

    @Override
    public void delete() {
        setCandidacyDocument(null);
        super.delete();
    }

    @Deprecated
    public boolean hasCandidacyDocument() {
        return getCandidacyDocument() != null;
    }

}
