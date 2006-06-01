package net.sourceforge.fenixedu.renderers.validators;

import javax.servlet.jsp.PageContext;

import net.sourceforge.fenixedu.renderers.components.HtmlComponent;
import net.sourceforge.fenixedu.renderers.components.Validatable;
import net.sourceforge.fenixedu.renderers.components.tags.HtmlTag;
import net.sourceforge.fenixedu.renderers.utils.RenderUtils;

public abstract class HtmlValidator extends HtmlComponent {

    private Validatable component;
    
    private boolean valid;
    
    private boolean isKey;
    
    private String message;
    
    public HtmlValidator(Validatable component) {
        super();
        
        this.component = component;
        setValid(true);
        setKey(true);
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
    
    public boolean isKey() {
        return this.isKey;
    }

    public void setKey(boolean isKey) {
        this.isKey = isKey;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        if (isKey()) {
            String errorMessage = getResourceMessage(getMessage());
            
            if (errorMessage != null) {
                return errorMessage;
            }
            else {
                return getMessage();
            }
        }
        else {
            return getMessage();
        }
    }
    
    protected String getResourceMessage(String message) {
        return RenderUtils.getResourceString(message);
    }

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
