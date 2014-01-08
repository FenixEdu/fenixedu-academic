package net.sourceforge.fenixedu.domain.thesis;

import net.sourceforge.fenixedu.domain.accessControl.Group;
import net.sourceforge.fenixedu.domain.accessControl.GroupUnion;
import net.sourceforge.fenixedu.domain.accessControl.ThesisFileReadersGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;
import net.sourceforge.fenixedu.injectionCode.IGroup;

public class ThesisFile extends ThesisFile_Base {

    public ThesisFile(String filename, String displayName, byte[] content, Group group) {
        super();
        init(filename, displayName, content, group);
    }

    @Override
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

        setDissertationThesis(null);
        setAbstractThesis(null);

        super.delete();
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

    @Deprecated
    public boolean hasAbstractThesis() {
        return getAbstractThesis() != null;
    }

    @Deprecated
    public boolean hasDissertationThesis() {
        return getDissertationThesis() != null;
    }

    @Deprecated
    public boolean hasSubTitle() {
        return getSubTitle() != null;
    }

    @Deprecated
    public boolean hasLanguage() {
        return getLanguage() != null;
    }

    @Deprecated
    public boolean hasTitle() {
        return getTitle() != null;
    }

}
