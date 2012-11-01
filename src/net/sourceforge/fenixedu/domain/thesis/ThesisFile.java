package net.sourceforge.fenixedu.domain.thesis;

import java.util.Collection;

import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.ThesisFileReadersGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class ThesisFile extends ThesisFile_Base {

    public ThesisFile(VirtualPath path, String filename, String displayName, Collection<FileSetMetaData> metadata, byte[] content, Group group) {
	super();
	init(path, filename, displayName, metadata, content, group);
    }

    public void delete() {
	Thesis thesis = getDissertationThesis();
	if (thesis == null) {
	    thesis = getAbstractThesis();
	}

	if (!thesis.isWaitingConfirmation()) {
	    throw new DomainException("thesis.file.delete.notAllowed");
	}

	deleteWithoutStateCheck();
    }

    public void deleteWithoutStateCheck() {
	Thesis thesis = getDissertationThesis();
	if (thesis == null) {
	    thesis = getAbstractThesis();
	}

	removeRootDomainObject();
	removeDissertationThesis();
	removeAbstractThesis();

	deleteDomainObject();
    }

    boolean areThesisFilesReadable() {
	final Group group = getPermittedGroup();
	return areThesisFilesReadable(group);
    }

    private boolean areThesisFilesReadable(final IGroup group) {
	if (group instanceof GroupUnion) {
	    final GroupUnion groupUnion = (GroupUnion) group;
	    for (IGroup child : groupUnion.getChildren()) {
		if (areThesisFilesReadable(child)) {
		    return true;
		}
	    }
	} else if (group instanceof ThesisFileReadersGroup) {
	    return true;
	}
	return false;
    }

}
