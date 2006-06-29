package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.renderers.model.MetaObjectKey;

public class CreationMetaObjectKey extends MetaObjectKey {
    public CreationMetaObjectKey(Class type) {
        super(type, 0);
    }

    @Override
    public String toString() {
        return getType().getName();
    }
}
