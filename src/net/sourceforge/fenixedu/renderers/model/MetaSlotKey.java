package net.sourceforge.fenixedu.renderers.model;

/**
 * The MetaSlotKey represents an indentifier that allows to identify in a single
 * meta slot of a meta object. The string representation of this key can be
 * obtained by invoking {@link #toString()} and used in the interface to generate
 * identifiers that are unique to a certain slot.
 * 
 * @see net.sourceforge.fenixedu.renderers.components.HtmlFormComponent#setTargetSlot(MetaSlotKey)
 *
 * @author cfgi
 */
public class MetaSlotKey extends MetaObjectKey {

    private String name;

    public MetaSlotKey(MetaObject metaObject, String name) {
        super(metaObject.getKey());
        
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (! (other instanceof MetaSlotKey)) {
            return false;
        }
        
        MetaSlotKey otherSlotKey = (MetaSlotKey) other;
        return super.equals(other) && this.name.equals(otherSlotKey.name);
    }

    @Override
    public int hashCode() {
        return super.hashCode() + this.name.hashCode();
    }

    @Override
    public String toString() {
        return super.toString() + ":" + this.name;
    }
}
