/**
 * Copyright © 2002 Instituto Superior Técnico
 *
 * This file is part of FenixEdu Academic.
 *
 * FenixEdu Academic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu Academic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu Academic.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.academic.domain.thesis;

import java.text.DecimalFormat;

import org.fenixedu.academic.domain.accessControl.ThesisReadersGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;
import org.fenixedu.bennu.io.servlets.FileDownloadServlet;

public class ThesisFile extends ThesisFile_Base {

    public ThesisFile(String filename, String displayName, byte[] content, Group group) {
        super();
        init(filename, displayName, content, group);
    }

    // Delete jsp usages and delete this method
    @Deprecated
    public String getDownloadUrl() {
        return FileDownloadServlet.getDownloadUrl(this);
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

    public String getPrettyFileSize() {
        long size = getSize();
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
