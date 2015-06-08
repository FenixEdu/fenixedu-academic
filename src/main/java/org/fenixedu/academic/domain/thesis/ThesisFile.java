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
import java.util.Set;
import java.util.stream.Collectors;

import org.fenixedu.academic.domain.accessControl.ScientificCommissionGroup;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.core.groups.DynamicGroup;
import org.fenixedu.bennu.io.servlets.FileDownloadServlet;

public class ThesisFile extends ThesisFile_Base {

    public ThesisFile(String filename, String displayName, byte[] content) {
        super();
        init(displayName, filename, content);
    }

    // Delete jsp usages and delete this method
    @Deprecated
    public String getDownloadUrl() {
        return FileDownloadServlet.getDownloadUrl(this);
    }

    @Override
    public boolean isAccessible(User user) {
        Thesis thesis = getThesis();
        if (thesis.isEvaluated()
                && (thesis.getDocumentsAvailableAfter() == null || thesis.getDocumentsAvailableAfter().isBeforeNow())) {
            if (thesis.getVisibility() != null) {
                switch (thesis.getVisibility()) {
                case INTRANET:
                    return user != null;
                case PUBLIC:
                    return true;
                }
            }
        }
        return DynamicGroup.get("scientificCouncil").or(ScientificCommissionGroup.get(getThesis().getDegree())).isMember(user)
                || getThesisMembers().contains(user);
    }

    private Thesis getThesis() {
        return getDissertationThesis() != null ? getDissertationThesis() : getAbstractThesis();
    }

    private Set<User> getThesisMembers() {
        Set<User> members =
                getThesis().getParticipationsSet().stream().filter(p -> p.getPerson() != null)
                        .map((p) -> p.getPerson().getUser()).collect(Collectors.toSet());
        members.add(getThesis().getStudent().getPerson().getUser());
        return members;
    }

    @Override
    public void delete() {
        if (!getThesis().isWaitingConfirmation()) {
            throw new DomainException("thesis.file.delete.notAllowed");
        }

        deleteWithoutStateCheck();
    }

    public void deleteWithoutStateCheck() {
        setDissertationThesis(null);
        setAbstractThesis(null);

        super.delete();
    }

    public String getPrettyFileSize() {
        long size = getSize();
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size) / Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
