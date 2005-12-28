package net.sourceforge.fenixedu.renderers.model;

public class SimpleMetaObjectKey implements MetaObjectKey {

    private Object object;
    private Class type;

    public SimpleMetaObjectKey(Object object, Class type) {
        this.object = object;
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public Class getType() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        if (! (other instanceof SimpleMetaObjectKey)) {
            return false;
        }
        
        SimpleMetaObjectKey otherKey = (SimpleMetaObjectKey) other;
        return this.object.equals(otherKey.object) && this.type.equals(otherKey.type);
    }

    @Override
    public int hashCode() {
        return this.object.hashCode() + this.type.hashCode();
    }

    @Override
    public String toString() {
        return this.type.getName();
    }
}
