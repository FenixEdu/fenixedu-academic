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
package net.sourceforge.fenixedu.domain.phd;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.fenixedu.domain.ExternalUser;
import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.accessControl.AcademicAuthorizationGroup;
import net.sourceforge.fenixedu.domain.accessControl.CoordinatorGroup;
import net.sourceforge.fenixedu.domain.accessControl.GuidingsAndAssistantsOfPhdGroup;
import net.sourceforge.fenixedu.domain.accessControl.academicAdministration.AcademicOperationType;
import net.sourceforge.fenixedu.domain.exceptions.DomainException;

import org.apache.commons.lang.StringUtils;
import org.fenixedu.bennu.core.groups.Group;
import org.fenixedu.bennu.core.groups.UnionGroup;
import org.fenixedu.bennu.core.groups.UserGroup;

public class PhdProgramProcessDocument extends PhdProgramProcessDocument_Base {

    public static Comparator<PhdProgramProcessDocument> COMPARATOR_BY_UPLOAD_TIME = new Comparator<PhdProgramProcessDocument>() {
        @Override
        public int compare(PhdProgramProcessDocument left, PhdProgramProcessDocument right) {
            int comparationResult = left.getUploadTime().compareTo(right.getUploadTime());
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

    @SuppressWarnings("unchecked")
    protected void init(PhdProgramProcess process, PhdIndividualProgramDocumentType documentType, String remarks, byte[] content,
            String filename, Person uploader) {

        checkParameters(process, documentType, content, filename, uploader);

        setDocumentVersion(process, documentType);

        super.setPhdProgramProcess(process);
        super.setDocumentType(documentType);
        super.setRemarks(remarks);
        super.setUploader(uploader);
        super.setDocumentAccepted(true);

        final Set<Group> groups = new HashSet<Group>();
        groups.add(AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESSES));

        final PhdIndividualProgramProcess individualProgramProcess = process.getIndividualProgramProcess();
        final PhdProgram phdProgram = individualProgramProcess.getPhdProgram();
        if (phdProgram != null) {
            groups.add(CoordinatorGroup.get(phdProgram.getDegree()));
        }
        groups.add(GuidingsAndAssistantsOfPhdGroup.get(individualProgramProcess));
        final Person person = getPhdProgramProcess().getPerson();
        if (person != null && person.getUser() != null) {
            groups.add(UserGroup.of(person.getUser()));
        }

        final Group group = UnionGroup.of(groups);
        super.init(filename, filename, content, group);
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
                    "error.net.sourceforge.fenixedu.domain.phd.candidacy.PhdProgramCandidacyProcessDocument.uploader.cannot.be.null",
                    args1);
        }

        if (documentType == null || content == null || content.length == 0 || StringUtils.isEmpty(filename)) {
            throw new DomainException("error.phd.PhdProgramProcessDocument.documentType.and.file.cannot.be.null");
        }
    }

    @Override
    protected void disconnect() {
        setUploader(null);
        setPhdProgramProcess(null);
        super.disconnect();
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
    public boolean isPersonAllowedToAccess(Person person) {
        if (person != null) {
            if (getPhdProgramProcess().getPerson() == person
                    || AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESSES).isMember(person.getUser())
                    || getPhdProgramProcess().getIndividualProgramProcess().isCoordinatorForPhdProgram(person)
                    || getPhdProgramProcess().getIndividualProgramProcess().isGuiderOrAssistentGuider(person)
                    || ExternalUser.isExternalUser(person.getUsername())) {
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

    public boolean isOtherType() {
        return hasType(PhdIndividualProgramDocumentType.OTHER);
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

    @Deprecated
    public boolean hasPhdProgramProcess() {
        return getPhdProgramProcess() != null;
    }

    @Deprecated
    public boolean hasDocumentAccepted() {
        return getDocumentAccepted() != null;
    }

    @Deprecated
    public boolean hasRemarks() {
        return getRemarks() != null;
    }

    @Deprecated
    public boolean hasDocumentType() {
        return getDocumentType() != null;
    }

    @Deprecated
    public boolean hasDocumentVersion() {
        return getDocumentVersion() != null;
    }

    @Deprecated
    public boolean hasUploader() {
        return getUploader() != null;
    }

}
