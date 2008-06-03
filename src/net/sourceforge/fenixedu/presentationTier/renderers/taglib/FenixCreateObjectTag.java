package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import net.sourceforge.fenixedu.presentationTier.renderers.factories.DomainMetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.taglib.CreateObjectTag;

public class FenixCreateObjectTag extends CreateObjectTag {
    
    private String service;
    
    public String getService() {
        return this.service;
    }

    public void setService(String service) {
        this.service = service;
    }

    @Override
    protected MetaObject createMetaObject(Object targetObject, Schema schema) {
        MetaObject metaObject = super.createMetaObject(targetObject, schema);
        
        if (getService() != null && metaObject instanceof DomainMetaObject) {
            DomainMetaObject domainMetaObject = (DomainMetaObject) metaObject;
            
            domainMetaObject.setService(getService());
        }
        
        return metaObject;
    }

    @Override
    public void release() {
        super.release();
        
        this.service = null;
    }
    
}
