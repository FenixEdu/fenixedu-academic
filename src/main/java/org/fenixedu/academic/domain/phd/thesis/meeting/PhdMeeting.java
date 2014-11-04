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

import java.util.Collections;

import org.fenixedu.academic.domain.Person;
import org.fenixedu.academic.domain.exceptions.DomainException;
import org.fenixedu.academic.domain.phd.PhdProgramDocumentUploadBean;
import org.fenixedu.academic.domain.phd.PhdProgramProcessDocument;
import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;

import pt.ist.fenixframework.Atomic;

public class PhdMeeting extends PhdMeeting_Base {

    public PhdMeeting() {
        super();
        setRootDomainObject(Bennu.getInstance());
    }

    public PhdMeeting init(final PhdMeetingSchedulingProcess process, final DateTime meetingDate, final String meetingPlace) {

        String[] args = {};
        if (process == null) {
            throw new DomainException("error.PhdMeeting.invalid.process", args);
        }

        setMeetingProcess(process);

        setMeetingDate(meetingDate);
        setMeetingPlace(meetingPlace);

        return this;
    }

    static public PhdMeeting create(final PhdMeetingSchedulingProcess process, final DateTime meetingDate,
            final String meetingPlace) {
        return new PhdMeeting().init(process, meetingDate, meetingPlace);
    }

    public PhdMeetingMinutesDocument getLatestDocumentVersion() {
        return !getDocumentsSet().isEmpty() ? Collections.max(getDocumentsSet(),
                PhdProgramProcessDocument.COMPARATOR_BY_UPLOAD_TIME) : null;
    }

    public boolean isDocumentsAvailable() {
        return !getDocumentsSet().isEmpty();
    }

    public Integer getVersionOfLatestDocumentVersion() {
        if (getLatestDocumentVersion() == null) {
            return null;
        }
        return getLatestDocumentVersion().getDocumentVersion();
    }

    public void addDocument(PhdProgramDocumentUploadBean each, Person responsible) {
        new PhdMeetingMinutesDocument(this, each.getType(), each.getRemarks(), each.getFileContent(), each.getFilename(),
                responsible);
    }

    @Atomic
    public void editAttributes(PhdEditMeetingBean bean) {
        setMeetingDate(bean.getScheduledDate());
        setMeetingPlace(bean.getScheduledPlace());
    }

}
