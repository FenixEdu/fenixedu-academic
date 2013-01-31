package net.sourceforge.fenixedu.dataTransferObject.resourceAllocationManager;

import java.io.Serializable;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

public class WeekBean implements Serializable {

	private LocalDate week;

	public WeekBean(DateTime date) {
		this.week = date.toLocalDate();
	}

	public WeekBean(LocalDate localDate) {
		this.week = localDate;
	}

	public LocalDate getWeek() {
		return week;
	}

	public void setWeek(LocalDate week) {
		this.week = week;
	}

	public String getPresentationName() {
		return week.toString() + " - " + week.plusDays(5).toString();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof WeekBean) {
			return ((WeekBean) obj).getWeek().equals(getWeek());
		}
		return false;
	}
}
