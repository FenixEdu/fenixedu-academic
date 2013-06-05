package net.sourceforge.fenixedu.domain.dissertation;

import java.util.Collection;

import net.sourceforge.fenixedu.domain.accessControl.DissertationFileReadersGroup;
import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.injectionCode.IGroup;

import pt.utl.ist.fenix.tools.file.FileSetMetaData;
import pt.utl.ist.fenix.tools.file.VirtualPath;

public class DissertationFile extends DissertationFile_Base {
    
    public  DissertationFile() {
        super();
    }
    
    private DissertationFile previousDissertationFile = null;
    
    public DissertationFile(VirtualPath path, String filename, String displayName, Collection<FileSetMetaData> metadata,
            byte[] content, Group group) {
        super();
        init(path, filename, displayName, metadata, content, group);
    }
    
    boolean areThesisFilesReadable() {
        final Group group = getPermittedGroup();
        return areDissertationFilesReadable(group);
    }

    private boolean areDissertationFilesReadable(final IGroup group) {
        if (group instanceof GroupUnion) {
            final GroupUnion groupUnion = (GroupUnion) group;
            for (IGroup child : groupUnion.getChildren()) {
                if (areDissertationFilesReadable(child)) {
                    return true;
                }
            }
        } else if (group instanceof DissertationFileReadersGroup) {
            return true;
        }
        return false;
    }

	public DissertationFile getPreviousDissertationFile() {
		return previousDissertationFile;
	}

	public void setPreviousDissertationFile(
			DissertationFile previousDissertationFile) {
		this.previousDissertationFile = previousDissertationFile;
	}
}
