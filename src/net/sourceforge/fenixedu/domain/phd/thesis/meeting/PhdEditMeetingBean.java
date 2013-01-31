package net.sourceforge.fenixedu.domain.phd.thesis.meeting;

import java.io.Serializable;

import org.joda.time.DateTime;

public class PhdEditMeetingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private DateTime scheduledDate;
	private String scheduledPlace;

	public PhdEditMeetingBean(final PhdMeeting phdMeeting) {
		setScheduledDate(phdMeeting.getMeetingDate());
		setScheduledPlace(phdMeeting.getMeetingPlace());
	}

	public DateTime getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(DateTime scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getScheduledPlace() {
		return scheduledPlace;
	}

	public void setScheduledPlace(String scheduledPlace) {
		this.scheduledPlace = scheduledPlace;
	}

}
