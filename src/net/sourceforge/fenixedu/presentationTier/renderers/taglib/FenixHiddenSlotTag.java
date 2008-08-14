package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import java.util.Collection;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.domain.RootDomainObject;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;

import org.apache.struts.taglib.TagUtils;

import pt.ist.fenixWebFramework.renderers.components.state.HiddenSlot;
import pt.ist.fenixWebFramework.renderers.converters.EnumConverter;
import pt.ist.fenixWebFramework.renderers.model.MetaObjectFactory;
import pt.ist.fenixWebFramework.renderers.taglib.HiddenSlotTag;

public class FenixHiddenSlotTag extends HiddenSlotTag {

    private String oid;
    private String type;
    private boolean isCollection;

    public String getOid() {
	return this.oid;
    }

    public void setOid(String oid) {
	this.oid = oid;
    }

    public String getType() {
	return this.type;
    }

    public void setType(String type) {
	this.type = type;
    }

    @Override
    public void release() {
	super.release();

	this.isCollection = false;
    }

    @Override
    protected Object findObject() throws JspException {
	if (getName() != null) {
	    return super.findObject();
	} else {
	    if (getOid() == null || getType() == null) {
		throw new JspException("must specify at 'name' or 'oid' and 'type'");
	    }

	    Object object = getPersistentObject();

	    if (object == null) {
		throw new JspException("could not find object " + getType() + ":" + getOid());
	    }

	    return object;
	}
    }

    @Override
    protected void addHiddenSlot(String slot, Object value, String converterName) throws JspException {
	if (value == null && converterName == null) {
	    return;
	}

	if (value instanceof Collection) {
	    this.isCollection = true;

	    Collection collection = (Collection) value;

	    for (Object object : collection) {
		addHiddenSlot(slot, object, converterName);
	    }
	} else if (value instanceof DomainObject) {
	    String usedConverterName = getNextDomainObjectConverter();

	    DomainObject domainObject = (DomainObject) value;
	    String objectValue = MetaObjectFactory.createObject(domainObject, null).getKey().toString();

	    addHiddenSlot(slot, objectValue, usedConverterName);
	} else if (value instanceof Enum) {
	    String enumConverterName = EnumConverter.class.getName();
	    addHiddenSlot(slot, ((Enum) value).name(), enumConverterName);
	} else {
	    super.addHiddenSlot(slot, value, converterName);
	}
    }

    @Override
    protected boolean isMultiple() {
	return super.isMultiple() || this.isCollection;
    }

    private String getNextDomainObjectConverter() {
	if (getConverter() != null) {
	    return getConverter();
	}

	HiddenSlot slot = getContainerParent().getHiddenSlot(getSlot());
	if (slot != null) {
	    return DomainObjectKeyArrayConverter.class.getName();
	}

	if (isMultiple()) {
	    return DomainObjectKeyArrayConverter.class.getName();
	}

	return DomainObjectKeyConverter.class.getName();
    }

    protected Object getPersistentObject() throws JspException {
	try {
	    Class type = Class.forName(getType());
	    return RootDomainObject.readDomainObjectByOID(type, Integer.valueOf(getOid()));
	} catch (Exception e) {
	    throw new JspException(e);
	}
    }

    protected int getScopeByName(String scope) throws JspException {
	return TagUtils.getInstance().getScope(scope);
    }
}
