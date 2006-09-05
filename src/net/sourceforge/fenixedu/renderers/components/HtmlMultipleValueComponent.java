package net.sourceforge.fenixedu.renderers.components;

import net.sourceforge.fenixedu.renderers.model.MetaSlot;

import org.apache.commons.beanutils.ConvertUtils;

public abstract class HtmlMultipleValueComponent extends HtmlFormComponent {

    public String[] values;
    
    public HtmlMultipleValueComponent() {
        super();
        
        values = new String[0];
    }
    
    public void setValues(String ... values) {
        this.values = values;
    }
    
    public String[] getValues() {
        return values;
    }
    
    public String getValue() {
        String[] values = getValues();
        
        if (values == null) {
            return null;
        }
        
        if (values.length == 0) {
            return null;
        }
        
        return values[0];
    }
    
    @Override
    public Object getConvertedValue() {
        if (hasConverter()) {
            return getConverter().convert(Object.class, getValue());
        }
        
        return ConvertUtils.convert(getValue(), Object.class);
    }
    
    @Override
    public Object getConvertedValue(MetaSlot slot) {
        if (hasConverter()) {
            return getConverter().convert(slot.getType(), getValues());
        }
        
        if (slot.hasConverter()) {
            try {
                return slot.getConverter().newInstance().convert(slot.getType(), getValues());
            } catch (Exception e) {
                throw new RuntimeException("converter specified in meta slot generated an exception", e);
            }
        }
        
        return ConvertUtils.convert(getValues(), slot.getType());
    }
}
