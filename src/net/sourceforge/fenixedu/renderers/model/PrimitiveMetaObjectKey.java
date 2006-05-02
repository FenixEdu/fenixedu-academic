package net.sourceforge.fenixedu.renderers.model;

public class PrimitiveMetaObjectKey extends SimpleMetaObjectKey {

    public PrimitiveMetaObjectKey(Object object, Class type) {
        super(object, type);
    }

    @Override
    public String toString() {
        return getObject() == null ? "" : getObject().toString();
    }
}
