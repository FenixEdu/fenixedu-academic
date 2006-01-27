package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;

public class CreationMetaObjectKey implements MetaObjectKey {
    private final Class type;

    public CreationMetaObjectKey(Class type) {
        this.type = type;
    }

    public Class getType() {
        return type;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof CreationMetaObjectKey) {
            CreationMetaObjectKey otherKey = (CreationMetaObjectKey) other;
            
            return this.type.equals(otherKey.type);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return this.type.hashCode();
    }

    @Override
    public String toString() {
        return getType().getName();
    }
}
