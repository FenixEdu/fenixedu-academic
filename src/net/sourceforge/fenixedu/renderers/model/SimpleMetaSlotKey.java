package net.sourceforge.fenixedu.renderers.model;

public class SimpleMetaSlotKey implements MetaSlotKey {

    private MetaObjectKey metaObjectKey;
    private String name;

    public SimpleMetaSlotKey(MetaObject metaObject, String name) {
        this.metaObjectKey = metaObject.getKey();
        this.name = name;
    }

    @Override
    public boolean equals(Object other) {
        if (! (other instanceof SimpleMetaSlotKey)) {
            return false;
        }
        
        SimpleMetaSlotKey otherSlotKey = (SimpleMetaSlotKey) other;
        return this.metaObjectKey.equals(otherSlotKey.metaObjectKey) && this.name.equals(otherSlotKey.name);
    }

    @Override
    public int hashCode() {
        return this.metaObjectKey.hashCode() + this.name.hashCode();
    }

    @Override
    public String toString() {
        return this.metaObjectKey.toString() + ":" + this.name;
    }
}
