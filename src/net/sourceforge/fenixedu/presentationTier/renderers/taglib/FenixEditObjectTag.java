package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.factories.DomainMetaObject;
import pt.ist.fenixWebFramework.renderers.model.MetaObject;
import pt.ist.fenixWebFramework.renderers.schemas.Schema;
import pt.ist.fenixWebFramework.renderers.taglib.EditObjectTag;

public class FenixEditObjectTag extends EditObjectTag {
    private String oid;

    private String type;

    private String service;

    public String getOid() {
	return oid;
    }

    public void setOid(String oid) {
	this.oid = oid;
    }

    public String getType() {
	return type;
    }

    public void setType(String type) {
	this.type = type;
    }

    public String getService() {
	return this.service;
    }

    public void setService(String service) {
	this.service = service;
    }

    @Override
    protected Object getTargetObject() throws JspException {
	Object object = super.getTargetObject();

	if (object == null) {
	    object = getPersistentObject();

	    if (object != null) {
		return super.getTargetObjectByProperty(object);
	    }
	}

	return object;
    }

    protected Object getPersistentObject() throws JspException {
	if (getOid() != null && getType() != null) {
	    try {
		Class type = Class.forName(getType());
		return RootDomainObject.getInstance().readDomainObjectByOID(type, Integer.valueOf(getOid()));
	    } catch (Exception e) {
		throw new JspException(e);
	    }
	}

	return null;
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

	this.oid = null;
	this.type = null;
	this.service = null;
    }

}
