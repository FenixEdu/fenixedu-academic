package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class RegularizationDayBean implements Serializable {

    private LocalDate date;

    private LocalTime[] timeClockings = { null, null, null, null };

    private LocalTime[] timeClockingsToFill = { null, null, null, null };

    public RegularizationDayBean(LocalDate date, List<AssiduousnessRecord> clockings) {
	setDate(date);
	if (clockings != null) {
	    Iterator<AssiduousnessRecord> iterator = clockings.iterator();
	    for (int counter = 0; counter < 4 && iterator.hasNext(); counter++) {
		AssiduousnessRecord clocking = iterator.next();
		timeClockings[counter] = clocking.getDate().toLocalTime();
	    }
	}
    }

    public String getWeekDay() {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());
	return bundle.getString(WeekDay.fromJodaTimeToWeekDay(getDate().toDateTimeAtStartOfDay()).toString() + "_ACRONYM");
    }

    public LocalTime[] getTimeClockingsToFill() {
	return timeClockingsToFill;
    }

    public LocalDate getDate() {
	return date;
    }

    public void setDate(LocalDate date) {
	this.date = date;
    }

    public LocalTime getFirstTimeClock() {
	return timeClockings[0];
    }

    public LocalTime getSecondTimeClock() {
	return timeClockings[1];
    }

    public LocalTime getThirdTimeClock() {
	return timeClockings[2];
    }

    public LocalTime getFourthTimeClock() {
	return timeClockings[3];
    }

    public LocalTime getFirstTimeClockToFill() {
	return timeClockingsToFill[0];
    }

    public LocalTime getSecondTimeClockToFill() {
	return timeClockingsToFill[1];
    }

    public LocalTime getThirdTimeClockToFill() {
	return timeClockingsToFill[2];
    }

    public LocalTime getFourthTimeClockToFill() {
	return timeClockingsToFill[3];
    }

    public void setFirstTimeClockToFill(LocalTime LocalTime) {
	timeClockingsToFill[0] = LocalTime;
    }

    public void setSecondTimeClockToFill(LocalTime LocalTime) {
	timeClockingsToFill[1] = LocalTime;
    }

    public void setThirdTimeClockToFill(LocalTime LocalTime) {
	timeClockingsToFill[2] = LocalTime;
    }

    public void setFourthTimeClockToFill(LocalTime LocalTime) {
	timeClockingsToFill[3] = LocalTime;
    }
}
