package net.sourceforge.fenixedu.renderers.components;

import net.sourceforge.fenixedu.renderers.model.MetaSlot;
import net.sourceforge.fenixedu.renderers.validators.HtmlValidator;

public interface Validatable {
    public String getValue();
    public String[] getValues();
    
    public void setValidator(HtmlValidator validator);
    public void setValidator(MetaSlot slot);
    public HtmlValidator getValidator();
}
