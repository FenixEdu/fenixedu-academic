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
package org.fenixedu.academic.domain.phd.thesis.meeting;

import java.util.Set;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.phd.PhdIndividualProgramDocumentType;
import org.fenixedu.academic.domain.phd.PhdProgramProcess;
import org.fenixedu.academic.domain.phd.PhdProgramProcessDocument;

public class PhdMeetingMinutesDocument extends PhdMeetingMinutesDocument_Base {

    public PhdMeetingMinutesDocument() {
        super();
    }

    public PhdMeetingMinutesDocument(PhdMeeting meeting, PhdIndividualProgramDocumentType documentType, String remarks,
            byte[] content, String filename, Person uploader) {
        this();
        init(meeting, documentType, remarks, content, filename, uploader);

    }

    protected void init(PhdMeeting meeting, PhdIndividualProgramDocumentType documentType, String remarks, byte[] content,
            String filename, Person uploader) {

        checkParameters(meeting.getMeetingProcess(), documentType, content, filename, uploader);

        setDocumentVersion(meeting, documentType);

        setPhdMeeting(meeting);
        super.setDocumentType(documentType);
        super.setRemarks(remarks);
        super.setUploader(uploader);
        super.setDocumentAccepted(true);

        super.init(filename, filename, content);
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
