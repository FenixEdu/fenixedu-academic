package net.sourceforge.fenixedu.domain.phd.thesis.meeting;

import java.util.Collections;

import net.sourceforge.fenixedu.domain.Person;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.domain.phd.PhdProgramDocumentUploadBean;
import net.sourceforge.fenixedu.domain.phd.PhdProgramProcessDocument;

import org.joda.time.DateTime;

import pt.ist.fenixWebFramework.services.Service;

public class PhdMeeting extends PhdMeeting_Base {

	public PhdMeeting() {
		super();
		setRootDomainObject(RootDomainObject.getInstance());
	}

	public PhdMeeting init(final PhdMeetingSchedulingProcess process, final DateTime meetingDate, final String meetingPlace) {

		check(process, "error.PhdMeeting.invalid.process");

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
		return hasAnyDocuments() ? Collections.max(getDocumentsSet(), PhdProgramProcessDocument.COMPARATOR_BY_UPLOAD_TIME) : null;
	}

	public boolean isDocumentsAvailable() {
		return hasAnyDocuments();
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

	@Service
	public void editAttributes(PhdEditMeetingBean bean) {
		setMeetingDate(bean.getScheduledDate());
		setMeetingPlace(bean.getScheduledPlace());
	}
}
