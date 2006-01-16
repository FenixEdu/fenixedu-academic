package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.applicationTier.IUserView;
import net.sourceforge.fenixedu.presentationTier.Action.sop.utils.ServiceUtils;
import net.sourceforge.fenixedu.renderers.taglib.EditObjectTag;

public class FenixEditObjectTag extends EditObjectTag {
    private String oid;
    
    private String type;

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
            IUserView userView = (IUserView) pageContext.getAttribute("UserView", getScopeByName("Session"));
    
            try {
                Class type = Class.forName(getType());
                Object[] args = { type, Integer.valueOf(getOid())};

                return ServiceUtils.executeService(userView, "ReadDomainObject", args);
            } catch (Exception e) {
                throw new JspException(e);
            } 
        }
        
        return null;
    }

    @Override
    public void release() {
        super.release();
        
        this.oid = null;
        this.type = null;
    }
}
