package net.sourceforge.fenixedu.presentationTier.renderers.factories;

import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.SimpleMetaSlot;
import net.sourceforge.fenixedu.renderers.model.UserIdentity;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;

public class CachedMetaSlot extends SimpleMetaSlot {

    private MetaObject cachedObject;
    private boolean isCached;
    
    public CachedMetaSlot(MetaObject metaObject, String name) {
        super(metaObject, name);
        
        this.isCached = false;
    }

    @Override
    public void setUser(UserIdentity user) {
        super.setUser(user);
        
        if (this.cachedObject != null) {
            this.cachedObject.setUser(user);
        }
    }

    @Override
    public Object getObject() {
        if (this.isCached) {
            return this.cachedObject.getObject();
        }
        else {
            return super.getObject();
        }
    }

    @Override
    public void setObject(Object object) {
        this.cachedObject = MetaObjectFactory.createObject(object, RenderKit.getInstance().findSchema(getSchema()));
        this.cachedObject.setUser(getUser());
        
        this.isCached = true;
    }

    public Object getCachedObject() {
        if (this.isCached) {
            return this.cachedObject.getObject();
        }
        else {
            return null;
        }
    }
}
