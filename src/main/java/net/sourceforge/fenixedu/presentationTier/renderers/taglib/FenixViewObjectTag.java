package net.sourceforge.fenixedu.presentationTier.renderers.taglib;

import javax.servlet.jsp.JspException;

import pt.ist.fenixWebFramework.renderers.taglib.ViewObjectTag;
import pt.ist.fenixframework.pstm.AbstractDomainObject;

public class FenixViewObjectTag extends ViewObjectTag {
    private String oid;

    private String type;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
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
        if (getOid() != null) {
            try {
                return AbstractDomainObject.fromExternalId(getOid());
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
