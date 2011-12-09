package net.sourceforge.fenixedu.dataTransferObject.spaceManager;

import java.io.Serializable;

import net.sourceforge.fenixedu.domain.space.AllocatableSpace;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class SpaceOccupationEventBean implements Serializable {
    private static final String TIME_FORMAT = "HH:mm";
    private static final String DATE_FORMAT = "dd/MM/yyyy";

    /*
     * public static final Comparator<SpaceOccupationEventBean> COMPARATOR = new
     * Comparator<SpaceOccupationEventBean>() {
     * 
     * @Override public int compare(SpaceOccupationEventBean o1,
     * SpaceOccupationEventBean o2) {
     * 
     * if (o1 == null || o2 == null) { if (o1 == null && o2 == null) { return 0;
     * } return o2 == null ? -1 : 1; }
     * 
     * final DateMidnight dateDay = o1.getDateDay(); final DateMidnight dateDay2
     * = o2.getDateDay(); int dateDayValue = 0; if (dateDay != null && dateDay2
     * != null) { dateDayValue = dateDay.compareTo(dateDay2); } if (dateDayValue
     * == 0) { if (o1.getInterval() != null && o2.getInterval() != null) {
     * dateDayValue =
     * o1.getInterval().getStart().compareTo(o2.getInterval().getStart()); if
     * (dateDayValue == 0) { dateDayValue =
     * o1.getInterval().getEnd().compareTo(o2.getInterval().getEnd()); } } else
     * { dateDayValue = -1; } } return dateDayValue; }; };
     */

    private AllocatableSpace space;
    private Interval interval;
    private String description;
    private OccupationType type;

    public SpaceOccupationEventBean(AllocatableSpace space, Interval interval, String description, OccupationType type) {
	super();
	this.space = space;
	this.interval = interval;
	this.description = description;
	this.type = type;
    }

    public String getPresentationInterval() {
	final DateTime start = interval.getStart();
	final DateTime end = interval.getEnd();
	final DateMidnight dateMidnight = start.toDateMidnight();
	if (dateMidnight.equals(end.toDateMidnight())) {
	    return String.format("%s : %s as %s", dateMidnight.toString(DATE_FORMAT), getTime(start), getTime(end));
	}
	return interval.toString();
    }

    public boolean isIntervalInSameDay() {
	return getDateDay() != null;
    }

    public DateMidnight getDateDay() {
	final DateTime start = interval.getStart();
	final DateTime end = interval.getEnd();
	final DateMidnight dateMidnight = start.toDateMidnight();
	if (dateMidnight.equals(end.toDateMidnight())) {
	    return dateMidnight;
	}
	return null;
    }

    private String getTime(DateTime time) {
	return time.toString(TIME_FORMAT);
    }

    public String getStartTime() {
	return getTime(interval.getStart());
    }

    public String getEndTime() {
	return getTime(interval.getEnd());
    }

    public String getDay() {
	final DateMidnight dateDay = getDateDay();
	if (dateDay == null) {
	    return String.format("%s - %s", interval.getStart().toString("dd/MM/yyyy"), interval.getEnd().toString("dd/MM/yyyy"));
	}
	return dateDay.toString("dd/MM/yyyy");
    }

    public String getSpaceName() {
	return space.getIdentification();
    }

    public String getDescription() {
	return description;
    }

    public Interval getInterval() {
	return interval;
    }

    public AllocatableSpace getSpace() {
	return space;
    }

    public OccupationType getOccupationType() {
	return type;
    }

    public void setOccupationType(OccupationType type) {
	this.type = type;
    }
}
