package net.sourceforge.fenixedu.renderers.validators;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;

public abstract class HtmlValidator extends HtmlComponent {

    private Validatable component;
    
    private boolean valid;
    
    public HtmlValidator(Validatable component) {
        super();
        
        this.component = component;
        this.valid = true; 
    }

    public boolean isValid() {
        return this.valid;
    }
    
    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public Validatable getComponent() {
        return this.component;
    }
    
    public abstract String getErrorMessage();
    
    public abstract void performValidation();

    @Override
    public HtmlTag getOwnTag(PageContext context) {
        HtmlTag tag = super.getOwnTag(context);
        
        tag.setName("span");
        if (! isValid()) {
            tag.setText(getErrorMessage());
        }
        
        return tag;
    }
    
}
