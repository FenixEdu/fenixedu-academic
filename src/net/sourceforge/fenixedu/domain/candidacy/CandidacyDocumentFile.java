package net.sourceforge.fenixedu.domain.candidacy;

import java.util.Collection;

import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class CandidacyDocumentFile extends CandidacyDocumentFile_Base {

    public CandidacyDocumentFile() {
	super();
    }

    public CandidacyDocumentFile(VirtualPath path, String filename, String displayName, Collection<FileSetMetaData> metadata,
	    byte[] content, Group group) {
	this();
	init(path, filename, displayName, metadata, content, group);
    }

    public void delete() {
	removeCandidacyDocument();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
