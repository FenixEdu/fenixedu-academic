package pt.utl.ist.renderers.template;

import java.io.Reader;

public abstract class AttributeValue {
    
    String attribute;
    
    public AttributeValue(String attribute) {
        this.attribute = attribute;
    }
    
    protected String getAttribute() {
        return this.attribute;
    }

    public abstract Reader getReader();
}