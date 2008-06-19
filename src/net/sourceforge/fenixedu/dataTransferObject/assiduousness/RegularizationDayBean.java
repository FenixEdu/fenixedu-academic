package net.sourceforge.fenixedu.dataTransferObject.assiduousness;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import net.sourceforge.fenixedu.domain.assiduousness.AssiduousnessRecord;
import net.sourceforge.fenixedu.util.WeekDay;

import org.joda.time.LocalDate;
import org.joda.time.TimeOfDay;

import pt.utl.ist.fenix.tools.util.i18n.Language;

public class RegularizationDayBean implements Serializable {

    private LocalDate date;

    private TimeOfDay[] timeClockings = { null, null, null, null };

    private TimeOfDay[] timeClockingsToFill = { null, null, null, null };

    public RegularizationDayBean(LocalDate date, List<AssiduousnessRecord> clockings) {
	setDate(date);
	if (clockings != null) {
	    Iterator<AssiduousnessRecord> iterator = clockings.iterator();
	    for (int counter = 0; counter < 4 && iterator.hasNext(); counter++) {
		AssiduousnessRecord clocking = iterator.next();
		timeClockings[counter] = clocking.getDate().toTimeOfDay();
	    }
	}
    }

    public String getWeekDay() {
	ResourceBundle bundle = ResourceBundle.getBundle("resources.AssiduousnessResources", Language.getLocale());
	return bundle.getString(WeekDay.fromJodaTimeToWeekDay(getDate().toDateTimeAtStartOfDay()).toString() + "_ACRONYM");
    }

    public TimeOfDay[] getTimeClockingsToFill() {
	return timeClockingsToFill;
    }

    public LocalDate getDate() {
	return date;
    }

    public void setDate(LocalDate date) {
	this.date = date;
    }

    public TimeOfDay getFirstTimeClock() {
	return timeClockings[0];
    }

    public TimeOfDay getSecondTimeClock() {
	return timeClockings[1];
    }

    public TimeOfDay getThirdTimeClock() {
	return timeClockings[2];
    }

    public TimeOfDay getFourthTimeClock() {
	return timeClockings[3];
    }

    public TimeOfDay getFirstTimeClockToFill() {
	return timeClockingsToFill[0];
    }

    public TimeOfDay getSecondTimeClockToFill() {
	return timeClockingsToFill[1];
    }

    public TimeOfDay getThirdTimeClockToFill() {
	return timeClockingsToFill[2];
    }

    public TimeOfDay getFourthTimeClockToFill() {
	return timeClockingsToFill[3];
    }

    public void setFirstTimeClockToFill(TimeOfDay timeOfDay) {
	timeClockingsToFill[0] = timeOfDay;
    }

    public void setSecondTimeClockToFill(TimeOfDay timeOfDay) {
	timeClockingsToFill[1] = timeOfDay;
    }

    public void setThirdTimeClockToFill(TimeOfDay timeOfDay) {
	timeClockingsToFill[2] = timeOfDay;
    }

    public void setFourthTimeClockToFill(TimeOfDay timeOfDay) {
	timeClockingsToFill[3] = timeOfDay;
    }
}
