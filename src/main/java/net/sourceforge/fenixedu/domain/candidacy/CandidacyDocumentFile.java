package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class CandidacyDocumentFile extends CandidacyDocumentFile_Base {

    public CandidacyDocumentFile() {
        super();
    }

    public CandidacyDocumentFile(VirtualPath path, String filename, String displayName, Collection<FileSetMetaData> metadata,
            byte[] content, Group group) {
        this();
        init(path, filename, displayName, metadata, content, group);
    }

    @Override
    public void delete() {
        setCandidacyDocument(null);
        super.delete();
    }

}
