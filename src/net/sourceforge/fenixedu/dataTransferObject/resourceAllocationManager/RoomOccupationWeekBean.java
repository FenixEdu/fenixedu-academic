package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.dataTransferObject.InfoRoom;
import net.sourceforge.fenixedu.domain.ExecutionSemester;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicInterval;
import net.sourceforge.fenixedu.domain.time.calendarStructure.AcademicPeriod;

import org.joda.time.DateTimeConstants;
import org.joda.time.LocalDate;

public class RoomOccupationWeekBean implements Serializable {

	private AcademicInterval academicInterval;

	private WeekBean weekBean;

	private InfoRoom room;

	public RoomOccupationWeekBean() {
		this.academicInterval = AcademicInterval.readDefaultAcademicInterval(AcademicPeriod.SEMESTER);
		LocalDate monday;
		for (monday = new LocalDate(); monday.getDayOfWeek() != DateTimeConstants.MONDAY; monday = monday.minusDays(1)) {
			;
		}
		this.weekBean = new WeekBean(monday);
	}

	public AcademicInterval getAcademicInterval() {
		return academicInterval;
	}

	public void setAcademicInterval(AcademicInterval academicInterval) {
		this.academicInterval = academicInterval;
	}

	public WeekBean getWeekBean() {
		return weekBean;
	}

	public void setWeekBean(WeekBean weekBean) {
		this.weekBean = weekBean;
	}

	public InfoRoom getRoom() {
		return room;
	}

	public void setRoom(InfoRoom room) {
		this.room = room;
	}

	@Deprecated
	public ExecutionSemester getExecutionSemester() {
		for (ExecutionSemester executionSemester : ExecutionSemester.readNotClosedExecutionPeriods()) {
			if (executionSemester.getAcademicInterval().equals(this.academicInterval)) {
				return executionSemester;
			}
		}

		return null;
	}

}
