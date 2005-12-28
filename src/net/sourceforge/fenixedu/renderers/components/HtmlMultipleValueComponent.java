package net.sourceforge.fenixedu.renderers.components;

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
    public Object getConvertedValue(Class type) {
        if (hasConverter()) {
            return getConverter().convert(type, getValues());
        }
        else {
            return ConvertUtils.convert(getValues(), type);
        }
        
    }
}
