package net.sourceforge.fenixedu.renderers.model;

public class SimpleCreationMetaObject extends SimpleMetaObject implements CreationMetaObject {

    public SimpleCreationMetaObject(Object object) {
        super(object);
    }

    public Object getCreatedObject() {
        return getObject();
    }
}
