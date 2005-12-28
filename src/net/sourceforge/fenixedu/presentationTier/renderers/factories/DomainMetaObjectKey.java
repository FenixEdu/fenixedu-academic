package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;

public class DomainMetaObjectKey implements MetaObjectKey {

    private final int oid;
    private final Class type;

    public DomainMetaObjectKey(int oid, Class type) {
        this.oid = oid;
        this.type = type;
    }

    public int getOid() {
        return oid;
    }

    public Class getType() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DomainMetaObjectKey) {
            DomainMetaObjectKey otherKey = (DomainMetaObjectKey) other;
            
            return this.oid == otherKey.oid && this.type.equals(otherKey.type);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.oid + this.type.hashCode();
    }

    @Override
    public String toString() {
        return this.type.getName() + ":" + String.valueOf(this.oid);
    }
}
