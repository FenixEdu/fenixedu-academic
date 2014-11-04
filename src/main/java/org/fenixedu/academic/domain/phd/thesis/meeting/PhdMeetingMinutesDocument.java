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
package org.fenixedu.academic.domain.phd.thesis.meeting;

import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.accessControl.AcademicAuthorizationGroup;
import org.fenixedu.academic.domain.accessControl.CoordinatorGroup;
import org.fenixedu.academic.domain.accessControl.academicAdministration.AcademicOperationType;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramDocumentType;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgram;
import org.fenixedu.academic.domain.phd.PhdProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgramProcessDocument;
import org.fenixedu.bennu.core.groups.Group;

public class PhdMeetingMinutesDocument extends PhdMeetingMinutesDocument_Base {

    public PhdMeetingMinutesDocument() {
        super();
    }

    public PhdMeetingMinutesDocument(PhdMeeting meeting, PhdIndividualProgramDocumentType documentType, String remarks,
            byte[] content, String filename, Person uploader) {
        this();
        init(meeting, documentType, remarks, content, filename, uploader);

    }

    @SuppressWarnings("unchecked")
    protected void init(PhdMeeting meeting, PhdIndividualProgramDocumentType documentType, String remarks, byte[] content,
            String filename, Person uploader) {

        checkParameters(meeting.getMeetingProcess(), documentType, content, filename, uploader);

        setDocumentVersion(meeting, documentType);

        setPhdMeeting(meeting);
        super.setDocumentType(documentType);
        super.setRemarks(remarks);
        super.setUploader(uploader);
        super.setDocumentAccepted(true);

        final Group roleGroup = AcademicAuthorizationGroup.get(AcademicOperationType.MANAGE_PHD_PROCESSES);

        final PhdIndividualProgramProcess individualProgramProcess =
                meeting.getMeetingProcess().getThesisProcess().getIndividualProgramProcess();
        final PhdProgram phdProgram = individualProgramProcess.getPhdProgram();
        final Group coordinatorGroup = CoordinatorGroup.get(phdProgram.getDegree());

        final Group group = roleGroup.or(coordinatorGroup);
        super.init(filename, filename, content, group);
    }

    protected void setDocumentVersion(PhdMeeting meeting, PhdIndividualProgramDocumentType documentType) {
        if (documentType.isVersioned()) {
            final Set<PhdMeetingMinutesDocument> documents = meeting.getDocumentsSet();
            super.setDocumentVersion(documents.isEmpty() ? 1 : documents.size() + 1);
        } else {
            super.setDocumentVersion(1);
        }
    }

    @Override
    public PhdProgramProcess getPhdProgramProcess() {
        return getPhdMeeting().getMeetingProcess().getThesisProcess().getIndividualProgramProcess();
    }

    @Override
    public boolean isLast() {
        return getPhdMeeting().getLatestDocumentVersion() == this;
    }

    @Override
    public PhdProgramProcessDocument getLastVersion() {
        return getPhdMeeting().getLatestDocumentVersion();
    }

}
