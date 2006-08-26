package net.sourceforge.fenixedu.domain.assiduousness.util;

import org.joda.time.DateTime;
import org.joda.time.TimeOfDay;
import org.joda.time.YearMonthDay;

public class TimePoint {

    private TimeOfDay time;

    private Attributes pointAttributes;

    private Attributes intervalAttributes;

    private boolean nextDay;

    public TimePoint(TimeOfDay timePoint, Attributes pointAttributes) {
        TimeOfDay timeIn = new TimeOfDay(timePoint.getHourOfDay(), timePoint.getMinuteOfHour(), 0);
        setTime(timeIn);
        setPointAttributes(pointAttributes);
        setIntervalAttributes(new Attributes());
        setNextDay(false);
    }

    public TimePoint(TimeOfDay timePoint, AttributeType attribute) {
        TimeOfDay timeIn = new TimeOfDay(timePoint.getHourOfDay(), timePoint.getMinuteOfHour(), 0);
        setTime(timeIn);
        setPointAttributes(new Attributes(attribute));
        setIntervalAttributes(new Attributes());
        setNextDay(false);
    }

    public TimePoint(TimeOfDay timePoint, boolean nextDay, AttributeType attribute) {
        TimeOfDay timeIn = new TimeOfDay(timePoint.getHourOfDay(), timePoint.getMinuteOfHour(), 0);
        setTime(timeIn);
        if (attribute != null) {
            setPointAttributes(new Attributes(attribute));
        } else {
            setPointAttributes(new Attributes());
        }
        setIntervalAttributes(new Attributes());
        setNextDay(nextDay);
    }

    public TimePoint(TimeOfDay timePoint, AttributeType attribute, AttributeType attributeToInterval) {
        TimeOfDay timeIn = new TimeOfDay(timePoint.getHourOfDay(), timePoint.getMinuteOfHour(), 0);
        setTime(timeIn);
        pointAttributes = new Attributes(attribute);
        intervalAttributes = new Attributes();
        if (attributeToInterval != null) {
            intervalAttributes.addAttribute(attributeToInterval);
        }
        setNextDay(false);
    }

    public boolean isNextDay() {
        return nextDay;
    }

    public void setNextDay(boolean nextDay) {
        this.nextDay = nextDay;
    }

    public TimeOfDay getTime() {
        return time;
    }

    public void setTime(TimeOfDay newTimePoint) {
        time = newTimePoint;
    }

    public Attributes getIntervalAttributes() {
        return intervalAttributes;
    }

    public void setIntervalAttributes(Attributes newIntervalAttributes) {
        intervalAttributes = newIntervalAttributes;
    }

    public Attributes getPointAttributes() {
        return pointAttributes;
    }

    public void setPointAttributes(Attributes newPointAttributes) {
        pointAttributes = newPointAttributes;
    }

    public boolean isBefore(TimePoint timePoint) {
        if (getTime().isBefore(timePoint.getTime())) {
            if (isNextDay() && timePoint.isNextDay()) {
                return true;
            } else if (!isNextDay() && !timePoint.isNextDay()) {
                return true;
            } else if (!isNextDay() && timePoint.isNextDay()) {
                return true;
            }
        } else {
            if (!isNextDay() && timePoint.isNextDay()) {
                return true;
            }
        }

        return false;
    }

    public boolean isAtSameTime(TimePoint timePoint) {
        return (nextDay == timePoint.isNextDay() && getTime().isEqual(timePoint.getTime()));
    }

    public String toString() {
        return new String(time.toString() + ", " + getPointAttributes().toString() + ", "
                + getIntervalAttributes().toString());
    }

    // Checks is point has both attributes
    // has both attributes if the point attribute contains a1 and interval attributes contains a2 (point
    // attribute is a1 but an a2 interval is open) or
    // if the point attribute contains a2 and interval attributes contains a1 (point attribute is a2 but
    // an attribute a1 is open) or
    // the point attribute contains both a1 and a2 (the point is both attribute types)
    public boolean hasAttributes(AttributeType attribute1, AttributeType attribute2) {
        return this.hasAttributes(attribute1, new Attributes(attribute2));
    }

    public boolean hasAttributes(AttributeType attribute1, Attributes attributes) {
        return ((getPointAttributes().contains(attribute1) && getIntervalAttributes().contains(
                attributes))
                || (getPointAttributes().contains(attributes) && getIntervalAttributes().contains(
                        attribute1)) || (getPointAttributes().contains(attribute1) && getPointAttributes()
                .contains(attributes)));
    }

    public AttributeType getWorkedAttribute(AttributeType attribute) {
        for (AttributeType attributeType : DomainConstants.WORKED_ATTRIBUTES.getAttributes()) {
            if ((getPointAttributes().contains(attribute) && getIntervalAttributes().contains(
                    attributeType))
                    || (getPointAttributes().contains(attributeType) && getIntervalAttributes()
                            .contains(attribute))
                    || (getPointAttributes().contains(attribute) && getPointAttributes().contains(
                            attributeType))) {
                return attributeType;
            }
        }
        return null;
    }

    public DateTime getDateTime(YearMonthDay day) {
        DateTime pointDateTime = day.toDateTime(getTime());
        if (isNextDay()) {
            pointDateTime = pointDateTime.plusDays(1);
        }
        return pointDateTime;
    }

}
