package net.sourceforge.fenixedu.renderers.model;

import java.util.List;
import java.util.Properties;

import net.sourceforge.fenixedu.renderers.components.converters.Converter;
import net.sourceforge.fenixedu.renderers.schemas.Schema;
import net.sourceforge.fenixedu.renderers.utils.RenderKit;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;
import net.sourceforge.fenixedu.renderers.utils.RendererPropertyUtils;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

import org.apache.commons.beanutils.PropertyUtils;

/**
 * A MetaSlot is an abstraction of a slot's value of a concrete domain object. The meta slots
 * provides an interface to present and manipulate that slot by the user without changing the
 * domain until it's really neaded. The meta slot also allows to propagete user inserted values
 * through out the interface.
 * 
 * @author cfgi
 */
public class MetaSlot extends MetaObject {
    
    private MetaObject metaObject;
    
    private String name;

    private String bundle;
    private String labelKey;
    
    private String layout;
    
    private Class<HtmlValidator> validator;
    private Properties validatorProperties;

    private Class<Converter> converter;

    private String defaultValue;
    
    private boolean readOnly;
    private boolean setterIgnored;

    private boolean isCached;
    private MetaObject valueMetaObject;
    
    public MetaSlot(MetaObject metaObject, String name) {
        super();
        
        this.metaObject = metaObject;
        this.name = name;
        
        this.valueMetaObject = null;
    }

    /**
     * Provides access to the meta object that holds this meta slot. 
     * 
     * @return the holder meta object
     */
    public MetaObject getMetaObject() {
        return this.metaObject;
    }

    /**
     * @return the name of the slot
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the key that allows to identify this slot
     */
    public MetaSlotKey getKey() {
        return new MetaSlotKey(getMetaObject(), getName());
    }

    public boolean hasValidator() {
        return getValidator() != null;
    }

    /**
     * This method allows you to obtain a localized label that can be presented in the interface.
     * This label is obtained by using the provided key and bundle for this slot or the default
     * naming conventions based on the type of the slot's object and this slot's name.
     * 
     * @return a string that can be used in the interface as a label for this slot
     * 
     * @see #getLabelKey()
     * @see #getBundle()
     * @see RenderUtils#getSlotLabel(Class, String, String, String)
     */
    public String getLabel() {
        Class type;
        
        if (getMetaObject().getSchema() != null) {
            type = getMetaObject().getSchema().getType();
        }
        else {
            type = getMetaObject().getType();
        }

        return RenderUtils.getSlotLabel(type, getName(), getBundle(), getLabelKey());
    }

    public void setLabelKey(String key) {
        this.labelKey = key;
    }

    /**
     * @return the key representing the label in a resource bundle
     */
    public String getLabelKey() {
        return this.labelKey;
    }
    
    public void setBundle(String bundle) {
        this.bundle = bundle;
    }

    /**
     * @return the bundle that should be used when obtaining the slot's label
     */
    public String getBundle() {
        return this.bundle;
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
    
    /**
     * The meta slot does not change the real slot until the commit is issued. This method
     * allows you to check if a value was already setted for this meta slot. 
     * 
     * @return <code>true</code> if a value was set in this slot
     */
    public boolean isCached() {
        return isCached;
    }

    protected void setCached(boolean isCached) {
        this.isCached = isCached;
    }

    /**
     * @return this slot's value
     */
    public Object getObject() {
        if (isCached()) {
            return getValueMetaObject().getObject();
        }
        else {
            try {
                return PropertyUtils.getProperty(getMetaObject().getObject(), getName());
            } catch (Exception e) {
                throw new RuntimeException("could not read property '" + getName() + "' from object " + getMetaObject().getObject(), e);
            }
        }
    }

    /**
     * @return this slot's type
     */
    public Class getType() {
        if (getObject() != null) {
            return getObject().getClass();
        }
        else {
            return getStaticType();
        }
    }

    /**
     * @return this's slots static type, that is, does not use the current value to determine the type
     */
    public Class getStaticType() {
        return RendererPropertyUtils.getPropertyType(getMetaObject().getType(), getName());
    }
    
    /**
     * Change this slot's value. 
     */
    public void setObject(Object object) {
        setValueMetaObject(object);
        setCached(true);
    }

    protected void setValueMetaObject(MetaObject metaObject) {
        this.valueMetaObject = metaObject;
        
        if (this.valueMetaObject != null) {
            this.valueMetaObject.setUser(getUser());
        }
    }
    
    private void setValueMetaObject(Object object) {
        setValueMetaObject(MetaObjectFactory.createObject(object, getSchema()));
    }
    
    protected MetaObject getValueMetaObject() {
        if (this.valueMetaObject == null) {
            setValueMetaObject(getObject());
        }
        
        return this.valueMetaObject;
    }

    public void setUser(UserIdentity user) {
        // When we are using a slot directly instead of accessing it though the base meta object
        if (getMetaObject() != null) {
            UserIdentity metaObjetUser = getMetaObject().getUser();
            
            if (metaObjetUser == null || !metaObjetUser.equals(user)) { // avoid recursion
                getMetaObject().setUser(user);
            }
        }
        
        if (this.valueMetaObject != null) {
            this.valueMetaObject.setUser(user);
        }
    }

    @Override
    public UserIdentity getUser() {
        if (getMetaObject() != null) {
            return getMetaObject().getUser();
        }
        else {
            return null;
        }
    }

    public List<MetaSlot> getSlots() {
        MetaObject valueMetaObject = getValueMetaObject();
        
        return valueMetaObject.getSlots();
    }

    public void addSlot(MetaSlot slot) {
        // ignore
    }

    public boolean removeSlot(MetaSlot slot) {
        // ignored
        return false;
    }

    public List<MetaSlot> getHiddenSlots() {
        MetaObject valueMetaObject = getValueMetaObject();

        return valueMetaObject.getHiddenSlots();
    }

    public void addHiddenSlot(MetaSlot slot) {
        // ignore
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
    
    public boolean isReadOnly() {
        return this.readOnly;
    }
    
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isSetterIgnored() {
        return this.setterIgnored;
    }

    public void setSetterIgnored(boolean setterIgnored) {
        this.setterIgnored = setterIgnored;
    }

    public void commit() {
        // delegate to parent meta object
        getMetaObject().commit(); 
    }

}
