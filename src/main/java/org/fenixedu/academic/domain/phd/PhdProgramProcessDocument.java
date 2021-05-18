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
package org.fenixedu.academic.domain.phd;

import java.util.Comparator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.academic.domain.ExternalUser;
import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.util.FileUtils;
import org.fenixedu.bennu.core.domain.User;
import org.fenixedu.bennu.io.servlet.FileDownloadServlet;

public class PhdProgramProcessDocument extends PhdProgramProcessDocument_Base {

    public static Comparator<PhdProgramProcessDocument> COMPARATOR_BY_UPLOAD_TIME = new Comparator<PhdProgramProcessDocument>() {
        @Override
        public int compare(PhdProgramProcessDocument left, PhdProgramProcessDocument right) {
            int comparationResult = left.getCreationDate().compareTo(right.getCreationDate());
            return (comparationResult == 0) ? left.getExternalId().compareTo(right.getExternalId()) : comparationResult;
        }
    };

    public static Comparator<PhdProgramProcessDocument> COMPARATOR_BY_VERSION = new Comparator<PhdProgramProcessDocument>() {

        @Override
        public int compare(PhdProgramProcessDocument left, PhdProgramProcessDocument right) {
            return left.getDocumentVersion().compareTo(right.getDocumentVersion()) * -1;
        }

    };

    protected PhdProgramProcessDocument() {
        super();
    }

    public PhdProgramProcessDocument(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, String remarks,
            byte[] content, String filename, Person uploader) {
        this();
        init(process, documentType, remarks, content, filename, uploader);

    }

    protected void init(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, String remarks, byte[] content,
            String filename, Person uploader) {

        checkParameters(process, documentType, content, filename, uploader);

        setDocumentVersion(process, documentType);

        super.setPhdProgramProcess(process);
        super.setDocumentType(documentType);
        super.setRemarks(remarks);
        super.setUploader(uploader);
        super.setDocumentAccepted(true);

        super.init(filename, filename, content);
    }

    @Override
    public void setFilename(String filename) {
        super.setFilename(FileUtils.cleanupUserInputFilename(filename));
    }

    @Override
    public void setDisplayName(String displayName) {
        super.setDisplayName(FileUtils.cleanupUserInputFileDisplayName(displayName));
    }

    protected void setDocumentVersion(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType) {
        if (documentType.isVersioned()) {
            super.setDocumentVersion(process.getLastVersionNumber(documentType));
        } else {
            super.setDocumentVersion(1);
        }
    }

    protected void checkParameters(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, byte[] content,
            String filename, Person uploader) {

        String[] args = {};
        if (process == null) {
            throw new DomainException("error.phd.PhdProgramProcessDocument.process.cannot.be.null", args);
        }
        String[] args1 = {};
        if (uploader == null) {
            throw new DomainException(
                    "error.org.fenixedu.academic.domain.phd.candidacy.PhdProgramCandidacyProcessDocument.uploader.cannot.be.null",
                    args1);
        }

        if (documentType == null || content == null || content.length == 0 || StringUtils.isEmpty(filename)) {
            throw new DomainException("error.phd.PhdProgramProcessDocument.documentType.and.file.cannot.be.null");
        }
    }

    // Delete jsp usages and delete this method
    @Deprecated
    public String getDownloadUrl() {
        return FileDownloadServlet.getDownloadUrl(this);
    }

    @Override
    public void delete() {
        setUploader(null);
        setPhdProgramProcess(null);
        super.delete();
    }

//    /*
//     * This method works properly because disconnect is re-implemented and
//     * super.disconnect is called first
//     */
//    @Override
//    protected void createDeleteFileRequest() {
//        Person person = AccessControl.getPerson();
//        if (person == null) {
//            person = getPhdProgramProcess().getPerson();
//        }
//        new DeleteFileRequest(person, getExternalStorageIdentification());
//    }

    public boolean hasType(final PhdIndividualProgramDocumentType type) {
        return getDocumentType().equals(type);
    }

    @Override
    public boolean isAccessible(User user) {
        if(getAvailableAfter() != null && getAvailableAfter().isBeforeNow()) {
            return true;
        } else if (user != null && user.getPerson() != null) {
            if (getPhdProgramProcess().getPerson() == user.getPerson()
                    || AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESSES).isMember(user)
                    || getPhdProgramProcess().getIndividualProgramProcess().isCoordinatorForPhdProgram(user.getPerson())
                    || getPhdProgramProcess().getIndividualProgramProcess().isGuiderOrAssistentGuider(user.getPerson())
                    || ExternalUser.isExternalUser(user.getUsername())) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isPrivate() {
        return true;
    }

    public PhdProgramProcessDocument replaceDocument(PhdIndividualProgramDocumentType documentType, String remarks,
            byte[] content, String filename, Person uploader) {
        if (!this.getClass().equals(PhdProgramProcessDocument.class)) {
            throw new DomainException("error.phd.PhdProgramProcessDocument.override.replaceDocument.method.on.this.class");
        }

        if (!getDocumentType().equals(documentType)) {
            throw new DomainException("error.phd.PhdProgramProcessDocument.type.must.be.equal");
        }

        return new PhdProgramProcessDocument(getPhdProgramProcess(), documentType, remarks, content, filename, uploader);
    }

    public boolean isReplaceable() {
        return isVersioned();
    }

    public boolean isReplaceableAndNotJuryReportFeedbackType() {
        return isVersioned() && !isJuryReportFeedbackType();
    }

    public boolean isOtherType() {
        return hasType(PhdIndividualProgramDocumentType.OTHER);
    }

    public boolean isJuryReportFeedbackType() {
        return hasType(PhdIndividualProgramDocumentType.JURY_REPORT_FEEDBACK);
    }

    public boolean isVersioned() {
        return getDocumentType().isVersioned();
    }

    public boolean isLast() {
        return !isVersioned() || getPhdProgramProcess().getLatestDocumentVersionFor(getDocumentType()) == this;
    }

    public PhdProgramProcessDocument getLastVersion() {
        if (!isVersioned()) {
            return this;
        }

        return getPhdProgramProcess().getLatestDocumentVersionFor(getDocumentType());
    }

    public Set<PhdProgramProcessDocument> getAllVersions() {
        return getPhdProgramProcess().getAllDocumentVersionsOfType(getDocumentType());
    }

    public void removeFromProcess() {
        if (!isVersioned()) {
            setDocumentAccepted(false);
            return;
        }

        Set<PhdProgramProcessDocument> versions = getAllVersions();

        for (PhdProgramProcessDocument version : versions) {
            version.setDocumentAccepted(false);
        }
    }

}
