package net.sourceforge.fenixedu.domain.assiduousness.util;

import net.sourceforge.fenixedu.domain.assiduousness.util.AttributeType;
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
        attributes.add(attributeType);
    }
    
    // Adds a set of attributes
    public void addAttributes(EnumSet<AttributeType> attributes) {
        for (AttributeType attribute: attributes) {
            this.attributes.add(attribute);
        }
    }
    
    public void addNullTime() {
        this.addAttribute(AttributeType.NULL);
    }

    public void addNormalWorkPeriod1Time() {
        this.addAttribute(AttributeType.NWP1);
    }

    public void addNormalWorkPeriod2Time() {
        this.addAttribute(AttributeType.NWP2);
    }

    public void addFixedPeriod1Time() {
        this.addAttribute(AttributeType.FP1);
    }

    public void addFixedPeriod2Time() {
        this.addAttribute(AttributeType.FP2);
    }

    public void addMealTime() {
        this.addAttribute(AttributeType.MEAL);
    }
    
    // Remove one attribute
    public void removeAttribute(AttributeType attribute) {
        this.attributes.remove(attribute);
    }

    // Removes a set of attributes
    public void removeAttributes(EnumSet<AttributeType> attributes) {
        for (AttributeType attribute: attributes) {
            this.attributes.remove(attribute);
        }
    }
    
    public void removeNullTime() {
        this.removeAttribute(AttributeType.NULL);
    }

    public void removeNormalWorkPeriod1Time() {
        this.removeAttribute(AttributeType.NWP1);
    }

    public void removeNormalWorkPeriod2Time() {
        this.removeAttribute(AttributeType.NWP2);
    }

    public void removeFixedPeriod1Time() {
        this.removeAttribute(AttributeType.FP1);
    }

    public void removeFixedPeriod2Time() {
        this.removeAttribute(AttributeType.FP2);
    }

    public void removeMealTime() {
        this.removeAttribute(AttributeType.MEAL);
    }
    
    // Check if this Attribute contains AttributeType
    public boolean contains(AttributeType attribute) {
        return this.getAttributes().contains(attribute);
    }
    
    // Check if Attribute contains another Attribute
    public boolean contains(Attributes attributes) {
        for (AttributeType attribute: attributes.getAttributes()) {
            if (this.contains(attribute)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(Attributes attributes) {
        return this.getAttributes().equals(attributes.getAttributes());
    }
    
    // TODO carefull! usar apenas qd se sabe q a interseccao da 1 elemento
    public AttributeType intersects(Attributes attributes) {
        for (AttributeType attribute: attributes.getAttributes()) {
            if (this.getAttributes().contains(attribute)) {
                return attribute;
            }
        }
        return null;
    }
    
    
    public String toString() {
        return new String(this.getAttributes().toString());
    }
    
}
