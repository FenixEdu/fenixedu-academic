package net.sourceforge.fenixedu.renderers.model;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

import org.apache.commons.beanutils.PropertyUtils;

public class SimpleMetaSlot implements MetaSlot {
    
    private MetaObject metaObject;
    private String name;
    private String labelKey;
    private String schema;
    private String layout;
    private Class<HtmlValidator> validator;
    private String defaultValue;
    private Properties properties;
    private Properties validatorProperties;
    private Class<Converter> converter;

    public SimpleMetaSlot(MetaObject metaObject, String name) {
        super();
        
        this.metaObject = metaObject;
        this.name = name;
    }

    public MetaObject getMetaObject() {
        return this.metaObject;
    }

    public String getName() {
        return this.name;
    }

    public SimpleMetaSlotKey getKey() {
        return new SimpleMetaSlotKey(getMetaObject(), getName());
    }

    public boolean hasValidator() {
        return getValidator() != null;
    }

    public String getLabel() {
        return RenderUtils.getSlotLabel(getMetaObject().getType(), getName(), getLabelKey());
    }

    public void setLabelKey(String key) {
        this.labelKey = key;
    }

    public String getLabelKey() {
        return this.labelKey;
    }
    
    public void setValidator(Class<HtmlValidator> validator) {
        this.validator = validator;
    }

    public Class<HtmlValidator> getValidator() {
        return this.validator;
    }

    public void setValidatorProperties(Properties validatorProperties) {
        this.validatorProperties = validatorProperties;
    }
    
    public Properties getValidatorProperties() {
        return this.validatorProperties;
    }

    public boolean hasConverter() {
        return this.converter != null;
    }

    public Class<Converter> getConverter() {
        return this.converter;
    }

    public void setConverter(Class<Converter> converter) {
        this.converter = converter;
    }
    
    public void setObject(Object object) {
        // TODO: do something with exceptions
        try {
            PropertyUtils.setProperty(getMetaObject().getObject(), getName(), object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public void setUser(UserIdentity user) {
        // no user needed
    }

    public UserIdentity getUser() {
        return null;
    }

    public Object getObject() {
        // TODO: do something with exceptions
        try {
            return PropertyUtils.getProperty(getMetaObject().getObject(), getName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Class getType() {
        // TODO: do something with exceptions
        try {
            return PropertyUtils.getPropertyType(getMetaObject().getObject(), getName());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<MetaSlot> getSlots() {
        return MetaObjectFactory.createObject(getObject(), RenderKit.getInstance().findSchema(getSchema())).getSlots();
    }
    
    public List<MetaSlot> getHiddenSlots() {
        return new ArrayList<MetaSlot>();
    }

    public void addHiddenSlot(MetaSlot slot) {
    }

    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    public Properties getProperties() {
        return this.properties;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getSchema() {
        return this.schema;
    }
    
    public void setLayout(String layout) {
        this.layout = layout;
    }

    public String getLayout() {
        return this.layout;
    }
    
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void commit() {
        // nothing to be done
    }
}
