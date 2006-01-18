package net.sourceforge.fenixedu.renderers.model;

import java.io.Serializable;
import java.util.Properties;

import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

public interface MetaSlot extends MetaObject, Serializable {
    public MetaObject getMetaObject();
    public String getName();
    public String getSchema();
    public String getLayout();
    public MetaSlotKey getKey();
    public String getLabelKey();
    public boolean hasValidator();
    public String getLabel();
    public Class<HtmlValidator> getValidator();
    public Properties getValidatorProperties();
    
    public void setObject(Object object);
}
