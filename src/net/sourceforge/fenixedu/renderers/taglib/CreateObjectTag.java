package net.sourceforge.fenixedu.renderers.taglib;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.schemas.Schema;

public class CreateObjectTag extends EditObjectTag {
    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    protected MetaObject getNewMetaObject(Object targetObject, Schema schema) {
        return MetaObjectFactory.createObject((Class) targetObject, schema);
    }

    @Override
    protected Object getTargetObject() throws JspException {
        if (isPostBack()) {
            return getViewState().getMetaObject().getType();
        }

        try {
            return Class.forName(getType());
        } catch (ClassNotFoundException e) {
            throw new JspException("could not get class named " + getType(), e);
        }
    }
}
