package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import javax.servlet.jsp.JspException;

import org.apache.struts.taglib.TagUtils;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.domain.DomainObject;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyArrayConverter;
import net.sourceforge.fenixedu.presentationTier.renderers.converters.DomainObjectKeyConverter;
import net.sourceforge.fenixedu.renderers.taglib.HiddenSlotTag;

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
            if (object instanceof DomainObject && getConverter() == null) {
                setConverter(isMultiple() ? DomainObjectKeyArrayConverter.class.getName() : DomainObjectKeyConverter.class.getName());

                DomainObject domainObject = (DomainObject) object;
                return domainObject.getClass().getName() + ":" + domainObject.getIdInternal();
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
