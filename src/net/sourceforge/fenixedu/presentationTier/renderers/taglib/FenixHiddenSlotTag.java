package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import java.util.Collection;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.components.state.HiddenSlot;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.taglib.HiddenSlotTag;

import org.apache.struts.taglib.TagUtils;

public class FenixHiddenSlotTag extends HiddenSlotTag {

    private String oid;
    private String type;
    
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
    protected Object findObject() throws JspException {
        if (getName() != null) {
            Object object = super.findObject();
            
            // HACK: to ease the use of hidden slots
            if (object instanceof DomainObject) {
                setConverter(getNextDomainObjectConverter());

                DomainObject domainObject = (DomainObject) object;
                return MetaObjectFactory.createObject(domainObject, null).getKey().toString();
            }
            
            return object;
        }
        else {
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
    
    private String getNextDomainObjectConverter() {
        if (getConverter() != null) {
            return getConverter();
        }
        
        HiddenSlot slot = getContainerParent().getHiddenSlot(getSlot());
        if (slot != null) {
            return DomainObjectKeyArrayConverter.class.getName();
        }
        else {
            return isMultiple() ? DomainObjectKeyArrayConverter.class.getName() : DomainObjectKeyConverter.class.getName();
        }
    }

    @Override
    protected void addHiddenSlot(String slot, Object value, String converterName) throws JspException {
        if (value instanceof Collection) {
            Collection collection = (Collection) value;

            for (Object object : collection) {
                if (object instanceof DomainObject) {
                    String usedConverterName = getNextDomainObjectConverter();
                    
                    DomainObject domainObject = (DomainObject) object;
                    String objectValue = MetaObjectFactory.createObject(domainObject, null).getKey().toString();
                    
                    addHiddenSlot(slot, objectValue, usedConverterName);
                }
                else {
                    addHiddenSlot(slot, object, converterName);
                }
            }
        }
        else {
            super.addHiddenSlot(slot, value, converterName);
        }
    }

    protected Object getPersistentObject() throws JspException {
        IUserView userView = (IUserView) pageContext.getAttribute("UserView", getScopeByName("Session"));

        try {
            Class type = Class.forName(getType());
            Object[] args = { type, Integer.valueOf(getOid())};

            return ServiceUtils.executeService(userView, "ReadDomainObject", args);
        } catch (Exception e) {
            throw new JspException(e);
        } 
    }
    
    protected int getScopeByName(String scope) throws JspException {
        return TagUtils.getInstance().getScope(scope);
    }
}
