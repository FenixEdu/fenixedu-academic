package net.sourceforge.fenixedu.domain.thesis;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.ThesisFileReadersGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class ThesisFile extends ThesisFile_Base {

    public ThesisFile(String uniqueId, String name) {
	super();

	init(name, name, null, null, null, null, uniqueId, null);
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
