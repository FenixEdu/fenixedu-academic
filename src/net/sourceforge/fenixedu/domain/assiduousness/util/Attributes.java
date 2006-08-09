package net.sourceforge.fenixedu.domain.assiduousness.util;

import java.util.EnumSet;

// Set that represents which intervals are open in a timePoint.
public class Attributes {
     
    private EnumSet<AttributeType> attributes;
    
    // Set is empty
    public Attributes() {
        attributes = EnumSet.noneOf(AttributeType.class);
    }

    public Attributes(AttributeType attributeType) {
        attributes = EnumSet.of(attributeType);
    }

    public Attributes(EnumSet<AttributeType> attributes) {
        setAttributes(attributes.clone());
    }

    public EnumSet<AttributeType> getAttributes() {
        return attributes;
    }
    
    public void setAttributes(EnumSet<AttributeType> newAttributes) {
        attributes = newAttributes;
    }
    
    // Adds one attribute
    public void addAttribute(AttributeType attributeType) {
        getAttributes().add(attributeType);
    }
    
    // Adds a set of attributes
    public void addAttributes(EnumSet<AttributeType> newAttributes) {
        for (AttributeType attribute: newAttributes) {
            getAttributes().add(attribute);
        }
    }
    
    public void addNullTime() {
        addAttribute(AttributeType.NULL);
    }

    public void addNormalWorkPeriod1Time() {
        addAttribute(AttributeType.NORMAL_WORK_PERIOD_1);
    }

    public void addNormalWorkPeriod2Time() {
        addAttribute(AttributeType.NORMAL_WORK_PERIOD_2);
    }

    public void addFixedPeriod1Time() {
        addAttribute(AttributeType.FIXED_PERIOD_1);
    }

    public void addFixedPeriod2Time() {
        addAttribute(AttributeType.FIXED_PERIOD_2);
    }

    public void addMealTime() {
        addAttribute(AttributeType.MEAL);
    }
    
    // Remove one attribute
    public void removeAttribute(AttributeType attribute) {
        getAttributes().remove(attribute);
    }

    // Removes a set of attributes
    public void removeAttributes(EnumSet<AttributeType> newAttributes) {
        for (AttributeType attribute: newAttributes) {
            getAttributes().remove(attribute);
        }
    }
    
    public void removeNullTime() {
        removeAttribute(AttributeType.NULL);
    }

    public void removeNormalWorkPeriod1Time() {
        removeAttribute(AttributeType.NORMAL_WORK_PERIOD_1);
    }

    public void removeNormalWorkPeriod2Time() {
        removeAttribute(AttributeType.NORMAL_WORK_PERIOD_2);
    }

    public void removeFixedPeriod1Time() {
        removeAttribute(AttributeType.FIXED_PERIOD_1);
    }

    public void removeFixedPeriod2Time() {
        removeAttribute(AttributeType.FIXED_PERIOD_2);
    }

    public void removeMealTime() {
        removeAttribute(AttributeType.MEAL);
    }
    
    // Check if this Attribute contains AttributeType
    public boolean contains(AttributeType attribute) {
        return getAttributes().contains(attribute);
    }
    
    // Check if Attribute contains another Attribute
    public boolean contains(Attributes attributes) {
        for (AttributeType attribute: attributes.getAttributes()) {
            if (contains(attribute)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Attributes attributes) {
        return getAttributes().equals(attributes.getAttributes());
    }
    
    // TODO carefull! usar apenas qd se sabe q a interseccao da 1 elemento
    public AttributeType intersects(Attributes attributes) {
        for (AttributeType attribute: attributes.getAttributes()) {
            if (getAttributes().contains(attribute)) {
                return attribute;
            }
        }
        return null;
    }
    
    
    public String toString() {
        return new String(getAttributes().toString());
    }
    
}
