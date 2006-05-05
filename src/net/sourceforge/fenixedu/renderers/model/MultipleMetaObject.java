package net.sourceforge.fenixedu.renderers.model;

import java.util.List;

public interface MultipleMetaObject extends MetaObject {
    
    public String getSchema(); // HACK: should be removed
    
    public void add(MetaObject metaObject);
    public boolean remove(MetaObject metaObject);
    public List<MetaObject> getAllMetaObjects();
}
