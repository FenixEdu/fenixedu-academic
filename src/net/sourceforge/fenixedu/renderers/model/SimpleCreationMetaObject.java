package net.sourceforge.fenixedu.renderers.model;

public class SimpleCreationMetaObject extends SimpleMetaObject {

    public SimpleCreationMetaObject(Object object) {
        super(object);
    }

    public Object getCreatedObject() {
        return getObject();
    }
}
