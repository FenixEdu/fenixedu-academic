package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.SimpleMetaSlot;

public class CachedMetaSlot extends SimpleMetaSlot {

    private Object cachedObject;
    private boolean isCached;
    
    public CachedMetaSlot(MetaObject metaObject, String name) {
        super(metaObject, name);
        
        this.isCached = false;
    }

    @Override
    public Object getObject() {
        if (this.isCached) {
            return this.cachedObject;
        }
        else {
            return super.getObject();
        }
    }

    @Override
    public void setObject(Object object) {
        this.cachedObject = object;
        this.isCached = true;
    }

    public Object getCachedObject() {
        return this.cachedObject;
    }
}
