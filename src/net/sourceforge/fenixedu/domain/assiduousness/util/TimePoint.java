package net.sourceforge.fenixedu.domain.assiduousness.util;

import org.joda.time.TimeOfDay;

public class TimePoint {

    private TimeOfDay timePoint;
    private Attributes pointAttributes;
    private Attributes intervalAttributes;

    public TimePoint(TimeOfDay timePoint) {
        this.setPoint(timePoint);
        this.pointAttributes = new Attributes(AttributeType.NULL);
//        this.pointAttributes = new Attributes(AttributeType.NULL);
        this.intervalAttributes = new Attributes();
    }

    // Sets the attribute interval to the same point attribute
    public TimePoint(TimeOfDay timePoint, AttributeType attribute) {
        this.setPoint(timePoint);
        this.pointAttributes = new Attributes(attribute);
        this.intervalAttributes = new Attributes();
    }

    
    public TimePoint(TimeOfDay timePoint, AttributeType attribute, AttributeType attributeToInterval) {
        this.setPoint(timePoint);
        this.pointAttributes = new Attributes(attribute);
        this.intervalAttributes = new Attributes();
        if (attributeToInterval != null) {
            this.intervalAttributes.addAttribute(attributeToInterval);
        }
    }
    
    public TimeOfDay getPoint() {
        return timePoint;
    }
    
    public void setPoint(TimeOfDay timePoint) {
        this.timePoint = timePoint;
    }
     
    public Attributes getIntervalAttributes() {
        return this.intervalAttributes;
    }
    
    public void setIntervalAttributes(Attributes intervalAttributes) {
        this.intervalAttributes = intervalAttributes;
    }

    public Attributes getPointAttributes() {
        return pointAttributes;
    }

    public void setPointAttributes(Attributes pointAttributes) {
        this.pointAttributes = pointAttributes;
    }

    public boolean isAfter(TimePoint timePoint) {
        return this.getPoint().isAfter(timePoint.getPoint());
    }

    public boolean isBefore(TimePoint timePoint) {
        return this.getPoint().isBefore(timePoint.getPoint());
    }
    
    public boolean isAtSameTime(TimePoint timePoint) {
        return this.getPoint().isEqual(timePoint.getPoint());
    }
    
    public boolean equals(TimePoint timePoint) {
        return (this.getPoint().equals(timePoint.getPoint()) && this.getIntervalAttributes().equals(timePoint.getIntervalAttributes()) && 
                this.getPointAttributes().equals(timePoint.getPointAttributes()));
    }

    // Just to abstract the fact that the TimePoint is a TimeOfDay
    public TimePoint plusSeconds(int seconds) {
        this.setPoint(this.getPoint().plusSeconds(seconds));
        return this;
    }

    // Just to abstract the fact that the TimePoint is a TimeOfDay
    public TimePoint minusSeconds(int seconds) {
        this.setPoint(this.getPoint().minusSeconds(seconds));
        return this;
    }

    public String toString() {
        return new String(this.timePoint.toString() + ", " + this.getPointAttributes().toString() + ", " + this.getIntervalAttributes().toString());
    }

    // Checks is point has both attributes
    // has both attributes if the point attribute contains a1 and interval attributes contains a2 (point attribute is a1 but an a2 interval is open) or
    // if the point attribute contains a2 and interval attributes contains a1 (point attribute is a2 but an attribute a1 is open) or
    // the point attribute contains both a1 and a2 (the point is both attribute types)
    public boolean hasAttributes(AttributeType attribute1, AttributeType attribute2) {
        return this.hasAttributes(attribute1, new Attributes(attribute2));
    }

    
    public boolean hasAttributes(AttributeType attribute1, Attributes attributes) {
        return ((this.getPointAttributes().contains(attribute1) && this.getIntervalAttributes().contains(attributes)) ||
                (this.getPointAttributes().contains(attributes) && this.getIntervalAttributes().contains(attribute1)) || 
                (this.getPointAttributes().contains(attribute1) && this.getPointAttributes().contains(attributes)));
    }

}
