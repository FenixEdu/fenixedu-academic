package net.sourceforge.fenixedu.renderers.model;

import java.util.Properties;

import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

public interface MetaSlot extends MetaObject {
    public MetaObject getMetaObject();
    public String getName();
    public String getSchema();
    public String getLayout();
    public MetaSlotKey getKey();
    public String getLabelKey();
    public boolean hasValidator();
    public String getLabel();
    public Class<HtmlValidator> getValidator();
    public boolean hasConverter();
    public Class<Converter> getConverter();
    public Properties getValidatorProperties();
    public boolean isReadOnly();
    
    public void setObject(Object object);
}
