/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Core.
 *
 * FenixEdu Core is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Core is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Core.  If not, see <http://www.gnu.org/licenses/>.
 */
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
