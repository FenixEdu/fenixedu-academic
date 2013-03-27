package pt.utl.ist.renderers.template;

import java.io.Reader;

public class TemplateAttributeValue extends AttributeValue {
    Template template;

    public TemplateAttributeValue(String attribute, Template template) {
        super(attribute);
        
        this.template = template;
    }

    protected Template getTemplate() {
        return this.template;
    }

    protected void setTemplate(Template template) {
        this.template = template;
    }

    @Override
    public Reader getReader() {
        return getTemplate().getContentReader();
    }
    
}