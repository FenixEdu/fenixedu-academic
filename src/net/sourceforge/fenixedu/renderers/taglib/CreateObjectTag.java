package net.sourceforge.fenixedu.renderers.taglib;

import javax.servlet.jsp.JspException;

import net.sourceforge.fenixedu.renderers.model.MetaObject;
import net.sourceforge.fenixedu.renderers.model.MetaObjectFactory;
import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.schemas.SchemaSlotDescription;

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
        ContextTag parent = (ContextTag) findAncestorWithClass(this, ContextTag.class);
        
        if (parent == null || getSlot() == null) {
            return MetaObjectFactory.createObject((Class) targetObject, schema);
        }

        MetaObject metaObject = parent.getMetaObject();
        if (metaObject == null) {
            metaObject = MetaObjectFactory.createObject((Class) targetObject, schema);
            parent.setMetaObject(metaObject);
        }
        else {
            SchemaSlotDescription slotDescription = schema.getSlotDescription(getSlot());

            if (slotDescription != null) { // when hidden values are given
                MetaSlot slot = MetaObjectFactory.createSlot(metaObject, slotDescription);
                metaObject.addSlot(slot);
            }
        }
        
        return metaObject;
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
