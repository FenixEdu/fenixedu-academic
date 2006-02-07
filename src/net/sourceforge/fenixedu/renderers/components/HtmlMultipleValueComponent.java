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
