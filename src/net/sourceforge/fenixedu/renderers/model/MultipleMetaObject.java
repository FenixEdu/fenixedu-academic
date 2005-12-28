package net.sourceforge.fenixedu.renderers.model;

import java.util.List;

public interface MultipleMetaObject extends MetaObject {
    public List<MetaObject> getAllMetaObjects();
}
