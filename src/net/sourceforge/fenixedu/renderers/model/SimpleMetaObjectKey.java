package net.sourceforge.fenixedu.renderers.model;

public class SimpleMetaObjectKey implements MetaObjectKey {

    private Class type;
    private int code;

    public SimpleMetaObjectKey(Class type, int code) {
        this.type = type;
        this.code = code;
    }

    @Override
    public boolean equals(Object other) {
        if (! (other instanceof SimpleMetaObjectKey)) {
            return false;
        }
        
        SimpleMetaObjectKey otherKey = (SimpleMetaObjectKey) other;
        return this.code == otherKey.code && this.type.equals(otherKey.type);
    }

    @Override
    public int hashCode() {
        return this.code + this.type.hashCode();
    }

    @Override
    public String toString() {
        return this.type.getName() + ":" + this.code;
    }
}
