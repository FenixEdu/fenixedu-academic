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
package org.fenixedu.academic.domain.candidacy;

import org.fenixedu.academic.domain.person.RoleType;
import org.fenixedu.academic.util.FileUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.io.servlets.FileDownloadServlet;

public class CandidacyDocumentFile extends CandidacyDocumentFile_Base {

    public CandidacyDocumentFile(String filename, String displayName, byte[] content) {
        super();
        init(displayName, filename, content);
    }

    @Override
    public void setFilename(String filename) {
        super.setFilename(FileUtils.cleanupUserInputFilename(filename));
    }

    @Override
    public void setDisplayName(String displayName) {
        super.setDisplayName(FileUtils.cleanupUserInputFileDisplayName(displayName));
    }

    @Override
    public boolean isAccessible(User user) {
        if (RoleType.MASTER_DEGREE_ADMINISTRATIVE_OFFICE.isMember(user)) {
            return true;
        }
        if (RoleType.COORDINATOR.isMember(user)) {
            return true;
        }
        User candidate = getCandidacyDocument().getCandidacy().getPerson().getUser();
        if (candidate != null && candidate.equals(user)) {
            return true;
        }
        return false;
    }

    // Delete jsp usages and delete this method
    @Deprecated
    public String getDownloadUrl() {
        return FileDownloadServlet.getDownloadUrl(this);
    }

    @Override
    public void delete() {
        setCandidacyDocument(null);
        super.delete();
    }

}
