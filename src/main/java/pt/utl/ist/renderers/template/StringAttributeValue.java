package pt.utl.ist.renderers.template;

import java.io.Reader;
import java.io.StringReader;

public class StringAttributeValue extends AttributeValue {

    String value;
    
    public StringAttributeValue(String attribute, String value) {
        super(attribute);
        
        this.value = value;
    }

    protected String getValue() {
        return this.value;
    }

    protected void setValue(String value) {
        this.value = value;
    }

    @Override
    public Reader getReader() {
        return new StringReader(getValue());
    }
    
}