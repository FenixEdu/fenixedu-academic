package net.sourceforge.fenixedu.domain.assiduousness.util;

import org.joda.time.TimeOfDay;

public class TimePoint {

    private TimeOfDay timePoint;
    private Attributes pointAttributes;
    private Attributes intervalAttributes;
    private Object entity;

    public TimePoint(TimeOfDay timePoint) {
        setPoint(timePoint);
        pointAttributes = new Attributes(AttributeType.NULL);
//        this.pointAttributes = new Attributes(AttributeType.NULL);
        intervalAttributes = new Attributes();
        entity = null;
    }

    // Sets the attribute interval to the same point attribute
    public TimePoint(TimeOfDay timePoint, AttributeType attribute) {
        setPoint(timePoint);
        pointAttributes = new Attributes(attribute);
        intervalAttributes = new Attributes();
        entity = null;
    }

    
    public TimePoint(TimeOfDay timePoint, AttributeType attribute, AttributeType attributeToInterval) {
        setPoint(timePoint);
        pointAttributes = new Attributes(attribute);
        intervalAttributes = new Attributes();
        if (attributeToInterval != null) {
            intervalAttributes.addAttribute(attributeToInterval);
        }
        entity = null;
    }

    public TimePoint(TimeOfDay timePoint, AttributeType attribute, AttributeType attributeToInterval, Object object) {
        setPoint(timePoint);
        pointAttributes = new Attributes(attribute);
        intervalAttributes = new Attributes();
        if (attributeToInterval != null) {
            intervalAttributes.addAttribute(attributeToInterval);
        }
        entity = object;
    }

    
    public TimeOfDay getPoint() {
        return timePoint;
    }
    
    public void setPoint(TimeOfDay newTimePoint) {
        timePoint = newTimePoint;
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

    public void setEntity(Object object) {
    		entity = object;
    }
    
    public Object getEntity() {
    		return entity;
    }

    public boolean isAfter(TimePoint timePoint) {
        return getPoint().isAfter(timePoint.getPoint());
    }

    public boolean isBefore(TimePoint timePoint) {
        return getPoint().isBefore(timePoint.getPoint());
    }
    
    public boolean isAtSameTime(TimePoint timePoint) {
        return getPoint().isEqual(timePoint.getPoint());
    }
    
    public boolean equals(TimePoint timePoint) {
        return (getPoint().equals(timePoint.getPoint()) && getIntervalAttributes().equals(timePoint.getIntervalAttributes()) && 
                getPointAttributes().equals(timePoint.getPointAttributes()));
    }

    // Just to abstract the fact that the TimePoint is a TimeOfDay
    public TimePoint plusSeconds(int seconds) {
        setPoint(getPoint().plusSeconds(seconds));
        return this;
    }

    // Just to abstract the fact that the TimePoint is a TimeOfDay
    public TimePoint minusSeconds(int seconds) {
        setPoint(getPoint().minusSeconds(seconds));
        return this;
    }

    public String toString() {
        return new String(timePoint.toString() + ", " + getPointAttributes().toString() + ", " + getIntervalAttributes().toString());
    }

    // Checks is point has both attributes
    // has both attributes if the point attribute contains a1 and interval attributes contains a2 (point attribute is a1 but an a2 interval is open) or
    // if the point attribute contains a2 and interval attributes contains a1 (point attribute is a2 but an attribute a1 is open) or
    // the point attribute contains both a1 and a2 (the point is both attribute types)
    public boolean hasAttributes(AttributeType attribute1, AttributeType attribute2) {
        return this.hasAttributes(attribute1, new Attributes(attribute2));
    }

    
    public boolean hasAttributes(AttributeType attribute1, Attributes attributes) {
        return ((getPointAttributes().contains(attribute1) && getIntervalAttributes().contains(attributes)) ||
                (getPointAttributes().contains(attributes) && getIntervalAttributes().contains(attribute1)) || 
                (getPointAttributes().contains(attribute1) && getPointAttributes().contains(attributes)));
    }

}
