package net.sourceforge.fenixedu.domain.thesis;

import net.sourceforge.fenixedu.domain.accessControl.ThesisReadersGroup;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;

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
        return areThesisFilesReadable(getPermittedGroup());
    }

    private boolean areThesisFilesReadable(final org.fenixedu.bennu.core.groups.Group group) {
        if (group instanceof UnionGroup) {
            final UnionGroup groupUnion = (UnionGroup) group;
            for (org.fenixedu.bennu.core.groups.Group child : groupUnion.getChildren()) {
                if (areThesisFilesReadable(child)) {
                    return true;
                }
            }
        } else if (group instanceof ThesisReadersGroup) {
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
