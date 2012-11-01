package net.sourceforge.fenixedu.domain;

import java.util.Collection;

import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import net.sourceforge.fenixedu.domain.accessControl.Group;

public class ProjectSubmissionFile extends ProjectSubmissionFile_Base {

    public ProjectSubmissionFile() {
	super();
    }

    public ProjectSubmissionFile(VirtualPath path, String filename, String displayName, Collection<FileSetMetaData> metadata,
	    byte[] content, Group group) {
	this();
	init(path, filename, displayName, metadata, content, group);

    }

    public void delete() {
	removeProjectSubmission();
	removeRootDomainObject();
	super.deleteDomainObject();
    }

}
