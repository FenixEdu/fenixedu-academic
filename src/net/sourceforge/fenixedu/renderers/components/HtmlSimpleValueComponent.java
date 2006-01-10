package net.sourceforge.fenixedu.renderers.components;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

import org.apache.commons.beanutils.ConvertUtils;

public abstract class HtmlSimpleValueComponent extends HtmlFormComponent implements Validatable {

    private String value;
    
    public HtmlSimpleValueComponent() {
        super();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public Object getConvertedValue(Class type) {
        if (hasConverter()) {
            return getConverter().convert(type, getValue());
        }
        else {
            return ConvertUtils.convert(getValue(), type);
        }
        
    }
    
    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setAttribute("value", this.value);
        
        return tag;
    }
}
